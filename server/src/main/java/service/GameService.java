package service;

import model.*;
import dataAccess.*;

import java.util.List;

public class GameService {
    public static void clear() {
        UserDAO userAccess = new UserDAOMemory();
        userAccess.clear();
        GameDAO gameAccess = new GameDAOMemory();
        gameAccess.clear();
        AuthDAO authAccess = new AuthDAOMemory();
        authAccess.clear();
    }

    public static List<GameData> list(String authToken) throws UnauthorizedError, ServerError {
        UserDAO userAccess = new UserDAOMemory();
        if (userAccess.getAuthToken(authToken) == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = new GameDAOMemory();
        return gameAccess.list();
    }

    public static int create(String authToken, String gameName) throws UnauthorizedError, ServerError {
        UserDAO userAccess = new UserDAOMemory();
        if (userAccess.getAuthToken(authToken) == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = new GameDAOMemory();
        return gameAccess.create(gameName);
    }
}
