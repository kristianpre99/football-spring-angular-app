package it.kristianp.footballbackendwebapp.auth.config;

import it.kristianp.footballbackendwebapp.auth.AuthEndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            AuthEndpointConstants.REGISTRATION_ENDPOINT,
            AuthEndpointConstants.AUTHENTICATE_ENDPOINT,
            AuthEndpointConstants.H2_CONSOLE_PREFIX + "**",
            AuthEndpointConstants.SWAGGER_UI_PREFIX + "**",
            AuthEndpointConstants.SWAGGER_CONFIG_PREFIX,
            AuthEndpointConstants.SWAGGER_CONFIG_PREFIX + "/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

//        @Order(1)
//        @Bean
//        @Profile("dev")
//        public SecurityFilterChain securityFilterChainDev(HttpSecurity http) throws Exception {
//            return http
//                    .csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
//                    .headers(headers -> headers
//                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
//                            )
//                    )
//                    .build();
//        }
}
