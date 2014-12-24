package pl.allegro.webapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static spark.Spark.*;

/**
 * Main entrance for AB-Route REST API
 */
public class ABRoute {
    final static Logger logger = LoggerFactory.getLogger(ABRoute.class);
    
    private static String USAGE_TIP = "Usage: java -jar ab-splitter-service.jar <config file path>";
    private static Configuration configuration;
    private static Splitter splitter;

    public static void main(String[] args) {
        init(args);
        startService();
    }
    
    //TODO add integration testing
    private static void startService() {
        //noinspection deprecation
        setPort(configuration.getPort());
        get(":version/route/:user", (request, response) -> {
            String apiVersion = request.params(":version");
            if (!isValidApiVersion(apiVersion)) {
                response.status(404);
                return new RouteResponse("Wrong API version", "404", "");
            }
            String user = request.params(":user");
            String version = request.headers("version");

            //TODO depending on requests repeatability - consider adding a cache
            try {
                return new RouteResponse(splitter.getGroupForUser(user)); // no overhead measured compared to using string concat or StringBuilder
            } catch (Exception e) {
                logger.error("Problem serving the request.", e);
                return new RouteResponse(e.getMessage(), "500", "");
            }
        }, new JsonTransformer());
    }

    private static boolean isValidApiVersion(String apiVersion) {
        return apiVersion.equals("v1");
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
