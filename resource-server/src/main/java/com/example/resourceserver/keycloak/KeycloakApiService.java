package com.example.resourceserver.keycloak;

import com.example.resourceserver.config.KeycloakProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeycloakApiService {

    @Autowired
    private KeycloakProperties props;

    public KeycloakApi getApi() {
        return new KeycloakApi(props);
    }
}
