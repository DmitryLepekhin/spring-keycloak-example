package com.example.resourceserver.keycloak;

import com.example.resourceserver.config.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.*;
import org.keycloak.util.JsonSerialization;

import javax.ws.rs.core.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeycloakApi {

    private Keycloak keycloak;
    private RealmResource realm;

    public KeycloakApi(KeycloakProperties props) {
        keycloak = Keycloak.getInstance(
                props.getKeycloakUrl(),
                props.getAdminRealm(),
                props.getUsername(),
                props.getPassword(),
                props.getAdminCliendId());
        realm = keycloak.realm(props.getRealm());
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

    public UserRepresentation newUser(String name, String password) {
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
        return user;
    }

    public void createUser(String name, String password) {
        realm.users().create(newUser(name, password));
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

    public RealmRepresentation exportRealm() {

        return realm.partialExport(true, true);
    }

    public Response importRealm(PartialImportRepresentation representation) {
        return realm.partialImport(representation);
    }

    public static <T> T loadJson(InputStream is, Class<T> type) {
        try {
            return JsonSerialization.readValue(is, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse json", e);
        }
    }

    public void saveToJson(String fileName, RealmRepresentation representation)
            throws IOException {
        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        JsonSerialization.writeValueToStream(fileOutputStream, representation);
        fileOutputStream.close();
    }

    public RealmRepresentation createRealmFromJson(String fileName) throws IOException {
        FileInputStream is = new FileInputStream(fileName);
        return loadJson(is, RealmRepresentation.class);

    }


}
