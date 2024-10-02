package it.kristianp.footballbackendwebapp.auth.config;

import it.kristianp.footballbackendwebapp.auth.AuthEndpointConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            if (request.getRequestURI() != null &&
                    AuthEndpointConstants.REGISTRATION_ENDPOINT.equals(request.getRequestURI()) ||
                    AuthEndpointConstants.AUTHENTICATE_ENDPOINT.equals(request.getRequestURI()) ||
                    request.getRequestURI().startsWith(AuthEndpointConstants.H2_CONSOLE_PREFIX) ||
                    request.getRequestURI().startsWith(AuthEndpointConstants.SWAGGER_UI_PREFIX) ||
                    request.getRequestURI().startsWith(AuthEndpointConstants.SWAGGER_CONFIG_PREFIX)
            ) {
                filterChain.doFilter(request, response);
            } else {
                handlerExceptionResolver.resolveException(request, response, null, new AccessDeniedException("Not authorized"));
            }
            return;
        }
        try {
            jwt = authHeader.substring(TOKEN_PREFIX.length());
            userEmail = jwtService.extractUsername(jwt); // extract from token
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
