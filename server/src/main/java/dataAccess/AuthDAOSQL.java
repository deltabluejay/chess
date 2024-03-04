package dataAccess;

import model.AuthData;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.RecursiveTask;

public class AuthDAOSQL implements AuthDAO {
    public AuthDAOSQL() {
        if (!DatabaseManager.databaseExists()) {
            try {
                DatabaseManager.createDatabase();
            } catch (DataAccessException e) {
                DatabaseManager.handleSQLError(e, "AuthDAOSQL");
            }
        }
    }

    @Override
    public void clear() {
        return;
    }

    @Override
    public AuthData createAuthToken(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String uuid = UUID.randomUUID().toString();
            AuthData authData = new AuthData(uuid, username);

            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.execute();
            return authData;
        } catch (SQLException | DataAccessException e) {
            DatabaseManager.handleSQLError(e, "createAuthToken");
        }
        return null;
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT authToken, username FROM auth WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String token = rs.getString(1);
                String username = rs.getString(2);
                return new AuthData(token, username);
            }
        } catch (SQLException | DataAccessException e) {
            DatabaseManager.handleSQLError(e, "getAuthToken");
        }
        return null;
    }

    @Override
    public boolean deleteAuthToken(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM auth WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }
        } catch (SQLException | DataAccessException e) {
            DatabaseManager.handleSQLError(e, "getAuthToken");
        }
        return false;
    }
}
