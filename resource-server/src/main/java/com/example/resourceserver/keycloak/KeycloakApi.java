package com.example.resourceserver.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeycloakApi {

    private final Keycloak keycloak;
    private final RealmResource realm;

    public KeycloakApi() {
        keycloak = Keycloak.getInstance(
                "http://localhost:9191/auth",
                "master",
                "admin",
                "admin",
                "admin-cli");
        realm = keycloak.realm("kofax");
    }

    public List<RoleRepresentation> getRealmRoles() {
        return realm.roles().list();
    }

    public void createRealmRoles(String roleId) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setId(roleId);
        roleRepresentation.setName(roleId);
        realm.roles().create(roleRepresentation);
    }

    public List<UserRepresentation> getUsers() {
        return realm.users().list();
    }

    public void createUser(String name, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setId(name);
        user.setUsername(name);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        List<CredentialRepresentation> credentials = user.getCredentials();
        if (credentials == null) {
            credentials = new ArrayList<>();
            user.setCredentials(credentials);
        }
        credentials.add(credential);

        user.setEnabled(true);

        realm.users().create(user);
    }

    public void mapUserRole(String username, String roleId) {
        // first find the user's id
        List<UserRepresentation> users = realm.users().search(username); // search by username
        users.stream().map(user -> user.getId()).forEach(userId -> {
            assignRoles(userId, roleId);
        });
    }

    private void assignRoles(String userId, String roleId) {
        List<RoleRepresentation> roleList = rolesToRealmRoleRepresentation(roleId);
        realm
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(roleList);

    }

    private List<RoleRepresentation> rolesToRealmRoleRepresentation(String roleId) {
        return realm
                .roles()
                .list()
                .stream().filter(
                    r -> roleId.equals(r.getName())
                )
                .collect(Collectors.toList());
    }
}
