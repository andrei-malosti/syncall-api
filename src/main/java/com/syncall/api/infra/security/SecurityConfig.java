package com.syncall.api.infra.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GetIdFilter getIdFilter) throws Exception{
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/attendants").hasAuthority("SCOPE_MANAGER")
                        .requestMatchers(HttpMethod.POST, "/clients").hasAuthority("SCOPE_MANAGER")
                        .requestMatchers(HttpMethod.GET,"/tickets").hasAnyAuthority("SCOPE_ATTENDANT", "SCOPE_MANAGER")
                        .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("SCOPE_CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/tickets/*/assign").hasAnyAuthority("SCOPE_ATTENDANT", "SCOPE_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tickets/client").hasAuthority("SCOPE_CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/tickets/conclude").hasAuthority("SCOPE_CLIENT")
                        .requestMatchers(HttpMethod.GET, "/tickets/attendant").hasAnyAuthority("SCOPE_ATTENDANT", "SCOPE_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tickets/*").hasAnyAuthority("SCOPE_ATTENDANT", "SCOPE_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/reports").hasAuthority("SCOPE_MANAGER")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .addFilterAfter(getIdFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA256");
        ImmutableSecret<SecurityContext> secret = new ImmutableSecret<>(secretKeySpec);
        return new NimbusJwtEncoder(secret);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
