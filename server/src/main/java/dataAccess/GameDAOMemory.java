package dataAccess;

import chess.ChessGame;
import model.GameData;
import service.AlreadyTakenError;

import java.util.ArrayList;
import java.util.List;

public class GameDAOMemory implements GameDAO {
    private static List<GameData> gameList = new ArrayList<>();
    private static int gameInt = 0;

    @Override
    public void clear() {
        gameList = new ArrayList<>();
    }

    @Override
    public List<GameData> list() {
        return gameList;
    }

    @Override
    public boolean exists(int gameID) {
        for (GameData game : gameList) {
            if (game.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }

    public int create(String gameName) {
        ChessGame newChessGame = new ChessGame();
        GameData newGame = new GameData(gameInt, null, null, gameName, newChessGame);
        gameList.add(newGame);
        return gameInt++;
    }

    public void join(String username, String playerColor, int gameID) throws AlreadyTakenError {
        for (int i = 0; i < gameList.size(); i++) {
            GameData game = gameList.get(i);
            if (game.gameID() == gameID) {
                if (playerColor != null) {
                    if (playerColor.equals("WHITE")) {
                        if (game.whiteUsername() != null) {
                            throw new AlreadyTakenError();
                        }
                        gameList.set(i, new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));
                    } else if (playerColor.equals("BLACK")) {
                        if (game.blackUsername() != null) {
                            throw new AlreadyTakenError();
                        }
                        gameList.set(i, new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
                    }
                } else {
                    //join as an observer?
                }
            }
        }
    }
}
