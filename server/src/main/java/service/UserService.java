package service;

import model.*;
import dataAccess.*;

public class UserService {
    public static AuthData register(UserData user) throws BadRequestError, AlreadyTakenError {
        UserDAO access = new UserDAOMemory();
        if (access.getUser(user.username()) != null) {
            throw new AlreadyTakenError("Error: already taken");
        } else {
            access.createUser(user);
            AuthData auth = access.createAuthToken(user.username());
            return auth;
        }
    }
}
