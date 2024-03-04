package service;

import model.*;
import dataAccess.*;

import java.util.List;

public class GameService {
    private static UserDAO createUserDAO() {
        return new UserDAOSQL();
    }

    private static GameDAO createGameDAO() {
        return new GameDAOSQL();
    }

    private static AuthDAO createAuthDAO() {
        return new AuthDAOSQL();
    }

    public static void clear() {
        UserDAO userAccess = createUserDAO();
        userAccess.clear();
        GameDAO gameAccess = createGameDAO();
        gameAccess.clear();
        AuthDAO authAccess = createAuthDAO();
        authAccess.clear();
    }

    public static List<GameData> list(String authToken) throws UnauthorizedError, ServerError {
        AuthDAO authAccess = createAuthDAO();
        if (authAccess.getAuthToken(authToken) == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = createGameDAO();
        return gameAccess.list();
    }

    public static int create(String authToken, String gameName) throws UnauthorizedError, ServerError {
        AuthDAO authAccess = createAuthDAO();
        if (authAccess.getAuthToken(authToken) == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = createGameDAO();
        return gameAccess.create(gameName);
    }

    public static void join(String authToken, String playerColor, int gameID) throws BadRequestError, UnauthorizedError, ServerError, AlreadyTakenError {
        AuthDAO authAccess = createAuthDAO();
        AuthData auth = authAccess.getAuthToken(authToken);
        if (auth == null) {
            throw new UnauthorizedError();
        }

        GameDAO gameAccess = createGameDAO();
        if (!gameAccess.exists(gameID)) {
            // Not sure what to throw here, specs don't define it
            throw new BadRequestError();
        }

        gameAccess.join(auth.username(), playerColor, gameID);
    }
}
