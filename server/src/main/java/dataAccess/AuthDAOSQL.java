package dataAccess;

import model.AuthData;

import java.util.concurrent.RecursiveTask;

public class AuthDAOSQL implements AuthDAO {
    @Override
    public void clear() {
        return;
    }

    @Override
    public AuthData createAuthToken(String username) {
        return null;
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return null;
    }

    @Override
    public boolean deleteAuthToken(String authToken) {
        return false;
    }
}
