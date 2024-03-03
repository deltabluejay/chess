package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();

    // Does this need to clear old authTokens?
    AuthData createAuthToken(String username);

    AuthData getAuthToken(String authToken);

    boolean deleteAuthToken(String authToken);
}
