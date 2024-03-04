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
                DatabaseManager.handleSQLError(e, "GameDAOSQL");
            }
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String dropAuth = "DROP TABLE IF EXISTS auth";
            String dropObservers = "DROP TABLE IF EXISTS observers";
            String dropGame = "DROP TABLE IF EXISTS game";
            String dropUser = "DROP TABLE IF EXISTS user";
            Statement stmt = conn.createStatement();
            stmt.addBatch(dropAuth);
            stmt.addBatch(dropObservers);
            stmt.addBatch(dropGame);
            stmt.addBatch(dropUser);
            stmt.executeBatch();
        } catch (DataAccessException | SQLException e) {
            DatabaseManager.handleSQLError(e, "clear");
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
            DatabaseManager.handleSQLError(e, "list");
        }
        return gameList;
    }

    @Override
    public boolean exists(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM game WHERE gameID = ?;";
            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (DataAccessException | SQLException e) {
            DatabaseManager.handleSQLError(e, "exists");
        }
        return false;
    }

    @Override
    public int create(String gameName) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO game (gameName) VALUES (?)";
            var stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, gameName);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int gameID = rs.getInt(1);
                return gameID;
            }
        } catch (DataAccessException | SQLException e) {
            DatabaseManager.handleSQLError(e, "create");
        }
        return -1;
    }

    @Override
    public void join(String username, String playerColor, int gameID) throws AlreadyTakenError {
        try (var conn = DatabaseManager.getConnection()) {
            String checkSql;
            String updateSql;
            PreparedStatement stmt;
            if (playerColor != null) {
                if (playerColor.equals("WHITE")) {
                    checkSql = "SELECT whitePlayer FROM game WHERE gameID = ?";
                    updateSql = "UPDATE game SET whitePlayer = ? WHERE gameID = ?";
                } else {
                    checkSql = "SELECT blackPlayer FROM game WHERE gameID = ?";
                    updateSql = "UPDATE game SET blackPlayer = ? WHERE gameID = ?";
                }
            } else {
                updateSql = "INSERT INTO observers (gameID, username) VALUES (?, ?)";
                stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, gameID);
                stmt.setString(2, username);
                stmt.execute();
                return;
            }
            stmt = conn.prepareStatement(checkSql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getString(1) != null) {
                    throw new AlreadyTakenError();
                }
            }

            stmt = conn.prepareStatement(updateSql);
            stmt.setString(1, username);
            stmt.setInt(2, gameID);
            stmt.execute();
        } catch (DataAccessException | SQLException e) {
            DatabaseManager.handleSQLError(e, "join");
        }
    }
}
