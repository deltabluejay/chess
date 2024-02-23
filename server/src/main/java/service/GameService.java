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
        AuthDAO authAccess = new AuthDAOMemory();
        if (authAccess.getAuthToken(authToken) == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = new GameDAOMemory();
        return gameAccess.list();
    }

    public static int create(String authToken, String gameName) throws UnauthorizedError, ServerError {
        AuthDAO authAccess = new AuthDAOMemory();
        if (authAccess.getAuthToken(authToken) == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = new GameDAOMemory();
        return gameAccess.create(gameName);
    }

    public static void join(String authToken, String playerColor, int gameID) throws BadRequestError, UnauthorizedError, ServerError, AlreadyTakenError {
        AuthDAO authAccess = new AuthDAOMemory();
        AuthData auth = authAccess.getAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = new GameDAOMemory();
        if (!gameAccess.exists(gameID)) {
            // Not sure what to throw here, specs don't define it
            throw new BadRequestError();
        }

        gameAccess.join(auth.username(), playerColor, gameID);
    }
}
