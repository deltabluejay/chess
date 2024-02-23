package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameDAOMemory implements GameDAO {
    private static List<GameData> gameList = new ArrayList<>();
    private static int gameInt = 0;

    @Override
    public void clear() {

    }

    @Override
    public List<GameData> list() {
        return gameList;
    }

    public int create(String gameName) {
        ChessGame newChessGame = new ChessGame();
        GameData newGame = new GameData(gameInt, null, null, gameName, newChessGame);
        gameList.add(newGame);
        return gameInt++;
    }
}
