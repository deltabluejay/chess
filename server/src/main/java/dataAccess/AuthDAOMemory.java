package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class AuthDAOMemory implements AuthDAO {
    private static List<AuthData> authList = new ArrayList<>();

    @Override
    public void clear() {
        authList = new ArrayList<>();
    }

    @Override
    public AuthData createAuthToken(String username) {
        String uuid = UUID.randomUUID().toString();
        AuthData authData = new AuthData(uuid, username);
        authList.add(authData);
        return authData;
    }

    @Override
    public AuthData getAuthToken(AuthData authData) {
        for (AuthData auth : authList) {
            if (auth.equals(authData)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        for (AuthData auth : authList) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public boolean deleteAuthToken(AuthData authData) {
        return false;
    }

    @Override
    public boolean deleteAuthToken(String authToken) {
        Iterator<AuthData> iterator = authList.iterator();
        while (iterator.hasNext()) {
            AuthData auth = iterator.next();
            if (auth.authToken().equals(authToken)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
