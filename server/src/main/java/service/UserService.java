package service;

import model.*;
import dataAccess.*;

public class UserService {
    private static UserDAO createUserDAO() {
        return new UserDAOMemory();
    }

    private static GameDAO createGameDAO() {
        return new GameDAOMemory();
    }

    private static AuthDAO createAuthDAO() {
        return new AuthDAOMemory();
    }

    public static AuthData register(UserData user) throws BadRequestError, AlreadyTakenError, ServerError {
        UserDAO userAccess = createUserDAO();
        if (userAccess.getUser(user.username()) != null) {
            throw new AlreadyTakenError("Error: already taken");
        }
        userAccess.createUser(user);

        AuthDAO authAccess = createAuthDAO();
        AuthData auth = authAccess.createAuthToken(user.username());
        return auth;
    }

    public static AuthData login(UserData user) throws UnauthorizedError, ServerError {
        UserDAO userAccess = createUserDAO();
        if (userAccess.getUser(user) == null) {
            throw new UnauthorizedError();
        }
        AuthDAO authAccess = createAuthDAO();
        AuthData auth = authAccess.createAuthToken(user.username());
        return auth;
    }

    public static void logout(String authToken) throws UnauthorizedError, ServerError {
        AuthDAO authAccess = createAuthDAO();
        boolean result = authAccess.deleteAuthToken(authToken);
        if (!result) {
            throw new UnauthorizedError();
        }
    }
}
