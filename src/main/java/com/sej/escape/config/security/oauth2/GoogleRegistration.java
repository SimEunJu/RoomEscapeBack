package com.sej.escape.config.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

@Component
public class GoogleRegistration {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String SECRET;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String[] scopes;

    public ClientRegistration getGoogleRegistration(){
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId(ID)
                .clientSecret(SECRET)
                .scope(scopes)
                .redirectUriTemplate("{baseUrl}/auth/oauth2/code/{registrationId}")
                .build();
    }
}
