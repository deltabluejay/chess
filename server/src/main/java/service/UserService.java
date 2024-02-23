package service;

import model.*;
import dataAccess.*;

public class UserService {
    public static AuthData register(UserData user) throws BadRequestError, AlreadyTakenError, ServerError {
        UserDAO access = new UserDAOMemory();
        if (access.getUser(user.username()) != null) {
            throw new AlreadyTakenError("Error: already taken");
        }
        access.createUser(user);
        AuthData auth = access.createAuthToken(user.username());
        return auth;
    }

    public static AuthData login(UserData user) throws UnauthorizedError, ServerError {
        UserDAO access = new UserDAOMemory();
        if (access.getUser(user) == null) {
            throw new UnauthorizedError();
        }
        AuthData auth = access.createAuthToken(user.username());
        return auth;
    }
}
