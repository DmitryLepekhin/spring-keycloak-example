package com.example.resourceserver;

import com.example.resourceserver.keycloak.KeycloakApi;
import org.junit.jupiter.api.Test;

public class KeycloakTest {
    @Test
    public void test_createUser() {
        KeycloakApi api = new KeycloakApi();
        api.createUser("test", "test");
    }

    @Test
    public void test_createUserAndRole_andMap() {
        KeycloakApi api = new KeycloakApi();

        String role = "testers";
        String username = "test";
        String password = "test";

        // create role
//        api.createRealmRoles(role);

        // create user
//        api.createUser(username, password);

        // map user to role
        api.mapUserRole(username, role);

    }
}
