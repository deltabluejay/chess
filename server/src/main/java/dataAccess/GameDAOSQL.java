package dataAccess;

import model.GameData;
import service.AlreadyTakenError;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDAOSQL implements GameDAO {
    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "USE chess;\n" +
                    "DROP TABLE IF EXISTS auth;\n" +
                    "DROP TABLE IF EXISTS game;\n" +
                    "DROP TABLE IF EXISTS user;\n" +
                    "DROP DATABASE IF EXISTS chess;";
            var stmt = conn.prepareStatement(sql);
            stmt.execute();
        } catch (DataAccessException | SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    @Override
    public List<GameData> list() {
        List<GameData> gameList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM game;";
            var stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int gameID = rs.getInt(1);
                String gameName = rs.getString(2);
                String gameString = rs.getString(3);
                String whitePlayer = rs.getString(4);
                String blackPlayer = rs.getString(5);
                // TODO: Deserialize gameString
                GameData newGame = new GameData(gameID, whitePlayer, blackPlayer, gameName, null);
                gameList.add(newGame);
            }
        } catch (DataAccessException | SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return gameList;
    }

    @Override
    public boolean exists(int gameID) {
        return false;
    }

    @Override
    public int create(String gameName) {
        return 0;
    }

    @Override
    public void join(String username, String playerColor, int gameID) throws AlreadyTakenError {

    }
}
