package dataAccess;

import model.AuthData;
import model.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Iterator;

public class UserDAOMemory implements UserDAO {
    private static List<UserData> userList = new ArrayList<>();
    private static List<AuthData> authList = new ArrayList<>();

    @Override
    public void clear() {
        userList = new ArrayList<>();
        authList = new ArrayList<>();
    }

    @Override
    public void createUser(UserData userData) {
        userList.add(userData);
    }

    @Override
    public UserData getUser(UserData userData) {
        for (UserData user : userList) {
            if (user.equals(userData)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserData getUser(String username) {
        for (UserData user : userList) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
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
