package pl.allegro.webapi;

public class RouteResponse {
    private final String group;
    private final String errorMessage;
    private final String errorCode;

    /**
     * Valid response
     */
    public RouteResponse(String group) {
        this.group = group;
        errorMessage = null;
        errorCode = null;
    }

    /**
     * Error response
     */
    public RouteResponse(String errorMessage, String errorCode, String group) {
        this.group = group;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
