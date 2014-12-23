package pl.allegro.webapi;

import java.io.File;

import static spark.Spark.*;

/**
 * Main entrance for AB-Route REST API
 */
public class ABRoute {
    private static String USAGE_TIP = "Usage: java -jar blabla";
    private static Configuration configuration;

    public static void main(String[] args) {
        init(args);
        setupService();
    }

    private static void setupService() {
        setPort(configuration.getPort());
        get(":version/route/:user", (request, response) -> {
            String version = request.headers("version");
            return new RouteResponse("C");
        }, new JsonTransformer());
    }

    private static void init(String[] args) {
        File configFile = getConfigFile(args);
        try {
            configuration = new Configuration(configFile);
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
        if (!(new File(configFilePath).exists())) {
            System.out.println("Configuration file doesn't exist.\n" + USAGE_TIP);
            System.exit(1);
        }
        return new File(configFilePath);
    }
}
