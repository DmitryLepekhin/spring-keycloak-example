package com.example.resourceserver;

import com.example.resourceserver.config.KeycloakProperties;
import com.example.resourceserver.keycloak.KeycloakApi;
import com.example.resourceserver.keycloak.KeycloakApiService;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class KeycloakTest {

    @Autowired
    private KeycloakApiService apiService;

    @Autowired
    private KeycloakProperties props;

    @Test
    public void test_createUser() {
        KeycloakApi api = apiService.getApi();
        api.createUser("test", "test");
    }

    @Test
    public void test_createUserAndRole_andMap() {
        KeycloakApi api = apiService.getApi();

        String role = "testers";
        String username = "test";
        String password = "test";

        // create role
        api.createRealmRoles(role);

        // create user
        api.createUser(username, password);

        // map user to role
        api.mapUserRole(username, role);

    }

    @Test
    public void test_exportImport() {
        KeycloakApi api = apiService.getApi();

        UserRepresentation user = api.newUser("importedUser", "importedUser");

        RealmRepresentation realm = api.exportRealm();

        PartialImportRepresentation representation = new PartialImportRepresentation();

        representation.setClients(realm.getClients());
        representation.setGroups(realm.getGroups());
        representation.setIdentityProviders(realm.getIdentityProviders());
        representation.setRoles(realm.getRoles());
        representation.setIfResourceExists("OVERWRITE");

        List<UserRepresentation> users = api.getUsers();
        users.add(user);
        representation.setUsers(users);

        Response response = api.importRealm(representation);
        System.out.println(response);
    }

    @Test void test_export() {
        KeycloakApi api = apiService.getApi();

        RealmRepresentation realm = api.exportRealm();

        try {
            api.saveToJson(props.getOutputDir() + "testrealm.json", realm);
        }
        catch (IOException e) {
            System.out.println("Can't export representation to file");
        }
    }

    @Test void test_import() {
        KeycloakApi api = apiService.getApi();

        try {
            RealmRepresentation realm = api.createRealmFromJson(props.getOutputDir() + "testrealm.json");
            PartialImportRepresentation representation = new PartialImportRepresentation();

            UserRepresentation user = api.newUser("importedUser2", "importedUser2");

            representation.setClients(realm.getClients());
            representation.setGroups(realm.getGroups());
            representation.setIdentityProviders(realm.getIdentityProviders());
            representation.setRoles(realm.getRoles());
            representation.setIfResourceExists("OVERWRITE");

            List<UserRepresentation> users = api.getUsers();
            users.add(user);
            representation.setUsers(users);

            Response response = api.importRealm(representation);
            System.out.println(response);

        } catch (IOException e) {
            System.out.println("File not found");
        }

    }
}
