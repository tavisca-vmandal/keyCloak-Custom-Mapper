package com.tavisca.datasetup;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class UserSetup {

    private static String PASSWORD = "password";

    private final UsersResource users;

    public UserSetup(Keycloak keycloak) {
        this.users = keycloak.realm(RealmSetup.REALM).users();
    }

    public void execute() {
        createUser("mmustermann", "Max", "Mustermann","536");
        createUser("jdoe", "John", "Doe", "1234");
    }

    public String createUser(String name, String firstName, String lastName,String memberId) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(name);
        user.setFirstName(firstName);
        Map<String, List<String>> customAttribute=new HashMap<>();
        customAttribute.put("memberId",Arrays.asList(memberId));
        user.setAttributes(customAttribute);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setCredentials(Arrays.asList(createPassword(PASSWORD)));
        Response response = users.create(user);
        return getCreatedId(response);
    }


    private CredentialRepresentation createPassword(final String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }

    private String getCreatedId(Response response) {
        return response.getLocation().toString().replaceAll(".*/", "");
    }
}
