package com.sej.escape.config;

import com.sej.escape.config.security.oauth2.GoogleRegistration;
import com.sej.escape.security.OauthAuthenticationSuccessHandler;
import com.sej.escape.service.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final GoogleRegistration googleRegistration;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Value("${front.url.base}")
    private String FRONT_BASE_URL;

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new OauthAuthenticationSuccessHandler();
    }

    @Bean
    public ExceptionMappingAuthenticationFailureHandler exceptionMappingAuthenticationFailureHandler(){
        ExceptionMappingAuthenticationFailureHandler handler = new ExceptionMappingAuthenticationFailureHandler();
        Map<String, String> failureUrlMap = new HashMap<>();
        failureUrlMap.put("OAuth2SignUpAttemptException", FRONT_BASE_URL+"/signin");
        handler.setExceptionMappings(failureUrlMap);
        handler.setDefaultFailureUrl(FRONT_BASE_URL);
        return handler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(FRONT_BASE_URL);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        Map<String, CorsConfiguration> configurations = new HashMap<>();
        configurations.put("/api/**", config);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.setCorsConfigurations(configurations);

        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .cors()
                .and()

                .formLogin()
                .and()

                .sessionManagement()
                .and()

                .oauth2Login()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .authorizationEndpoint()
                        .baseUri("/auth/oauth2")
                        .and()
                    .clientRegistrationRepository(clientRegistrationRepository())
                    .redirectionEndpoint()
                        .baseUri("/auth/oauth2/code/*")
                        .and()
                    .defaultSuccessUrl(FRONT_BASE_URL)
                    //.successHandler(authenticationSuccessHandler())
                    .failureHandler(exceptionMappingAuthenticationFailureHandler())
                .and()

                .logout()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl(FRONT_BASE_URL)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
        ;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        return new ClientRegistrationRepository() {
            @Override
            public ClientRegistration findByRegistrationId(String registrationId) {
                switch (registrationId){
                    case "google": return googleRegistration.getGoogleRegistration();
                }

                return null;
            }
        };
    }
}
