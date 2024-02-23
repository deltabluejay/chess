package service;

import model.*;
import dataAccess.*;

public class GameService {
    public static void clear() {
        UserDAO userAccess = new UserDAOMemory();
        userAccess.clear();
        GameDAO gameAccess = new GameDAOMemory();
        gameAccess.clear();
        AuthDAO authAccess = new AuthDAOMemory();
        authAccess.clear();
    }
}
