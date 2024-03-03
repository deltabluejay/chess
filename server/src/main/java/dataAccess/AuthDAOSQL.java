package dataAccess;

import model.AuthData;

public class AuthDAOSQL implements AuthDAO {
    @Override
    public void clear() {

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
