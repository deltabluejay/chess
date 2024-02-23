package service;

import model.*;
import dataAccess.*;

public class UserService {
    public static AuthData register(UserData user) throws BadRequestError, AlreadyTakenError, ServerError {
        UserDAO userAccess = new UserDAOMemory();
        if (userAccess.getUser(user.username()) != null) {
            throw new AlreadyTakenError("Error: already taken");
        }
        userAccess.createUser(user);

        AuthDAO authAccess = new AuthDAOMemory();
        AuthData auth = authAccess.createAuthToken(user.username());
        return auth;
    }

    public static AuthData login(UserData user) throws UnauthorizedError, ServerError {
        UserDAO userAccess = new UserDAOMemory();
        if (userAccess.getUser(user) == null) {
            throw new UnauthorizedError();
        }
        AuthDAO authAccess = new AuthDAOMemory();
        AuthData auth = authAccess.createAuthToken(user.username());
        return auth;
    }

    public static void logout(String authToken) throws UnauthorizedError, ServerError {
        AuthDAO authAccess = new AuthDAOMemory();
        boolean result = authAccess.deleteAuthToken(authToken);
        if (!result) {
            throw new UnauthorizedError();
        }
    }
}
