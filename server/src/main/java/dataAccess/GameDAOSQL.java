package dataAccess;

import model.GameData;
import service.AlreadyTakenError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAOSQL implements GameDAO {
    public GameDAOSQL() {
        if (!DatabaseManager.databaseExists()) {
            try {
                DatabaseManager.createDatabase();
            } catch (DataAccessException e) {
                DatabaseManager.handleSQLError(e);
            }
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String sql0 = "USE chess;";
            String sql1 = "DROP TABLE IF EXISTS auth";
            String sql2 = "DROP TABLE IF EXISTS game";
            String sql3 = "DROP TABLE IF EXISTS user";
            Statement stmt = conn.createStatement();
            stmt.addBatch(sql1);
            stmt.addBatch(sql2);
            stmt.addBatch(sql3);
            stmt.executeBatch();
        } catch (DataAccessException | SQLException e) {
            DatabaseManager.handleSQLError(e);
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
            DatabaseManager.handleSQLError(e);
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
