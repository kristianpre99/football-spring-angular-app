package it.kristianp.footballbackendwebapp.auth.controller;

import it.kristianp.footballbackendwebapp.auth.AuthEndpointConstants;
import it.kristianp.footballbackendwebapp.auth.payload.AuthenticationRequest;
import it.kristianp.footballbackendwebapp.auth.payload.AuthenticationResponse;
import it.kristianp.footballbackendwebapp.auth.payload.RegisterRequest;
import it.kristianp.footballbackendwebapp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(AuthEndpointConstants.BASE_ENDPOINT)
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = AuthEndpointConstants.REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping(value = AuthEndpointConstants.AUTHENTICATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
