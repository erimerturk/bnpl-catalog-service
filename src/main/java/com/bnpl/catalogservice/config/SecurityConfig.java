package com.bnpl.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/", "/properties/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().hasRole("employee")
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }



//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
////        http.securityMatcher( "/", "/properties/**").
//
//        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(AbstractHttpConfigurer::disable);
//
//        return http.authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/","/properties/", "/properties/**").permitAll())
//                .authorizeHttpRequests(authorize -> authorize.anyRequest().hasRole("employee"))
//                .build();


//        return http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(HttpMethod.GET, "/", "/properties/**").permitAll()
//                        .anyRequest().hasRole("employee")
//                )
//                .build();
//    }


//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.authorizeHttpRequests()
////                .requestMatchers("/api/public/**").permitAll()
//
//
//
//        http
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests
//                                .requestMatchers(HttpMethod.GET, "/", "/properties/**").permitAll()
//                                .anyRequest().hasRole("employee")
//                );
//
//        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
////                .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(AbstractHttpConfigurer::disable);
//
//        return http.build();
//
////        return http
////                .authorizeHttpRequests()
////
////                .anyRequest().hasRole("employee")
////                .and()
////                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//////                .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
////                .sessionManagement(sessionManagement ->
////                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .csrf(AbstractHttpConfigurer::disable)
////                .build();
//    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
