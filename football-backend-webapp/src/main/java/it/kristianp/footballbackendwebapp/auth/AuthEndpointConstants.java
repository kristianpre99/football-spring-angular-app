package it.kristianp.footballbackendwebapp.auth;

public interface AuthEndpointConstants {

    public static final String BASE_ENDPOINT = "/api/v1/auth";
    public static final String REGISTRATION = "/register";
    public static final String REGISTRATION_ENDPOINT = BASE_ENDPOINT + REGISTRATION;
    public static final String AUTHENTICATE = "/authenticate";
    public static final String AUTHENTICATE_ENDPOINT = BASE_ENDPOINT + AUTHENTICATE;

    public static final String H2_CONSOLE_PREFIX = "/h2-console/";
    public static final String SWAGGER_UI_PREFIX = "/swagger-ui/";
    public static final String SWAGGER_CONFIG_PREFIX = "/v3/api-docs";

}
