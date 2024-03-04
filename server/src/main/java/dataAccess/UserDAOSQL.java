package dataAccess;

import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import service.UserService;

public class UserDAOSQL implements UserDAO {
    public UserDAOSQL() {
        if (!DatabaseManager.databaseExists()) {
            try {
                DatabaseManager.createDatabase();
            } catch (DataAccessException e) {
                DatabaseManager.handleSQLError(e, "UserDAOSql");
            }
        }
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        return hashedPassword;
    }

    private boolean passwordsMatch(String inputPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(inputPassword, hashedPassword);
    }

    @Override
    public void clear() {
        return;
    }

    @Override
    public boolean createUser(UserData userData) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String uuid = UUID.randomUUID().toString();
            stmt.setString(1, userData.username());
            stmt.setString(2, hashPassword(userData.password()));
            stmt.setString(3, userData.email());
            stmt.execute();
            return true;
        } catch (SQLException | DataAccessException e) {
            DatabaseManager.handleSQLError(e, "createUser");
        }
        return false;
    }

    @Override
    public UserData getUser(UserData userData) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT password, email FROM user WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userData.username());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString(1);
                String email = rs.getString(2);
                if (passwordsMatch(userData.password(), hashedPassword)) {
                    UserData user = new UserData(userData.username(), hashedPassword, email);
                    return user;
                }
            }
        } catch (SQLException | DataAccessException e) {
            DatabaseManager.handleSQLError(e, "getUser");
        }
        return null;
    }

    @Override
    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT password, email FROM user WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString(1);
                String email = rs.getString(2);
                UserData user = new UserData(username, hashedPassword, email);
                return user;
            }
        } catch (SQLException | DataAccessException e) {
            DatabaseManager.handleSQLError(e, "getUser");
        }
        return null;
    }
}
