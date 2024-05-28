package it.kristianp.footballbackendwebapp.auth;

public interface AuthEndpointConstants {

    public static final String BASE_ENDPOINT = "/api/v1/auth";
    public static final String REGISTRATION = "/register";
    public static final String REGISTRATION_ENDPOINT = BASE_ENDPOINT + REGISTRATION;
    public static final String AUTHENTICATE = "/authenticate";
    public static final String AUTHENTICATE_ENDPOINT = BASE_ENDPOINT + AUTHENTICATE;

}
