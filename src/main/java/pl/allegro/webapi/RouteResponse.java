package pl.allegro.webapi;

public class RouteResponse {
    private final String group;
    private final String errorMessage;
    private final String errorCode;

    public RouteResponse(String group) {
        this.group = group;
        errorMessage = null;
        errorCode = null;
    }

    public RouteResponse(String errorMessage, String errorCode) {
        this.group = null;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
