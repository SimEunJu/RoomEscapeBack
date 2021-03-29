package com.sej.escape.config;

import com.sej.escape.config.oauth2.GoogleRegistration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @NonNull
    GoogleRegistration googleRegistration;

    @Value("${front.url.base}")
    private String FRONT_BASE_URL;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();
        //config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
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
                //.cors(Customizer.withDefaults())

                .formLogin()
                .and()

                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/auth/oauth2")
                        .and()
                    .clientRegistrationRepository(clientRegistrationRepository())
                    .redirectionEndpoint()
                        .baseUri("/auth/oauth2/code/*")
                        .and()
                    .defaultSuccessUrl(FRONT_BASE_URL)
                    .failureUrl(FRONT_BASE_URL)
                .and()

                .logout();
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
