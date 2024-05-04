package it.kristianp.footballbackendwebapp.auth.service;

import it.kristianp.footballbackendwebapp.auth.payload.AuthenticationRequest;
import it.kristianp.footballbackendwebapp.auth.payload.AuthenticationResponse;
import it.kristianp.footballbackendwebapp.auth.payload.RegisterRequest;

public interface AuthService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
