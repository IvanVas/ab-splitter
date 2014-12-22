package pl.allegro.webapi;

import static spark.Spark.get;

/**
 * Main entrance for AB-Route REST API
 */
public class ABRoute {
    public static void main(String[] args) {
        init(args);
        setupService();
    }

    private static void setupService() {
        get(":version/route/:user", (request, response) -> {
            return new RouteResponse("C");
        }, new JsonTransformer());
    }

    private static void init(String[] args) {
    }
}
