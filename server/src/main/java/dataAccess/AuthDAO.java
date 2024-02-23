package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();

    // Does this need to clear old authTokens?
    AuthData createAuthToken(String username);

    AuthData getAuthToken(AuthData authData);

    AuthData getAuthToken(String authToken);

    boolean deleteAuthToken(AuthData authData);

    boolean deleteAuthToken(String authToken);
}
