package pl.allegro.webapi;

import java.io.File;

import static spark.Spark.*;

/**
 * Main entrance for AB-Route REST API
 */
public class ABRoute {
    private static String USAGE_TIP = "Usage: java -jar ab-splitter-service.jar <config file path>";
    private static Configuration configuration;
    private static Splitter splitter;

    public static void main(String[] args) {
        init(args);
        startService();
    }

    private static void startService() {
        setPort(configuration.getPort());
        get(":version/route/:user", (request, response) -> {
            String apiVersion = request.params(":version");
            if (!apiVersion.equals("v1")) {
                response.status(404);
                return new RouteResponse("Wrong API version", "101");
            }
            String user = request.params(":user");
            String version = request.headers("version");
            //TODO: depending on request repeatability - consider adding a cache
            return new RouteResponse(splitter.getGroupForUser(user)); // no overhead measured compared to using string concat or StringBuilder
        }, new JsonTransformer());
    }

    private static void init(String[] args) {
        File configFile = getConfigFile(args);
        try {
            configuration = new Configuration(configFile);
            splitter = new Splitter(configuration);
        } catch (Configuration.ConfigurationException e) {
            System.out.println(e.getMessage() + "\n" + USAGE_TIP);
            System.exit(1);
        }
    }

    private static File getConfigFile(String[] args) {
        if (args.length == 0) {
            System.out.println("Configuration file was not specified.\n" + USAGE_TIP);
            System.exit(1);
        }
        String configFilePath = args[0];
        File configFile = new File(configFilePath);
        if (!(configFile.exists())) {
            System.out.println("Configuration file doesn't exist.\n" + USAGE_TIP);
            System.exit(1);
        }
        return configFile;
    }
}
