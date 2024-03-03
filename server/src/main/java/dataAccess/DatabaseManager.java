package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    static void handleSQLError(Exception e) {
       System.out.println("SQL Error: " + e.getMessage());
    }

    static boolean databaseExists() {
        try (var conn = getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, databaseName);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("count");
                if (count == 1) {
                    sql = "SELECT COUNT(*) AS count " +
                            "FROM information_schema.tables " +
                            "WHERE table_schema = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, databaseName);
                    rs = stmt.executeQuery();
                    count = 0;
                    if (rs.next()) {
                        count = rs.getInt("count");
                        if (count == 3) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (DataAccessException | SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            //var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
//            var statement = "CREATE DATABASE IF NOT EXISTS ?;\n" +
//                    "USE ?;\n" +
//                    "CREATE TABLE IF NOT EXISTS user (\n" +
//                    "    username VARCHAR(50) PRIMARY KEY,\n" +
//                    "    password VARCHAR(50) NOT NULL,\n" +
//                    "    email VARCHAR(100) NOT NULL\n" +
//                    ");\n" +
//                    "CREATE TABLE IF NOT EXISTS auth (\n" +
//                    "    authToken VARCHAR(100) PRIMARY KEY,\n" +
//                    "    username VARCHAR(50) NOT NULL,\n" +
//                    "    FOREIGN KEY (username) REFERENCES user(username)\n" +
//                    ");\n" +
//                    "CREATE TABLE IF NOT EXISTS game (\n" +
//                    "    gameID INT PRIMARY KEY AUTO_INCREMENT,\n" +
//                    "    gameName VARCHAR(100) NOT NULL,\n" +
//                    "    gameString VARCHAR(1000),\n" +
//                    "    whitePlayer VARCHAR(50),\n" +
//                    "    blackPlayer VARCHAR(50),\n" +
//                    "    observers VARCHAR(255),\n" +
//                    "    FOREIGN KEY (whitePlayer) REFERENCES user(username),\n" +
//                    "    FOREIGN KEY (blackPlayer) REFERENCES user(username)\n" +
//                    ");";
            String createDb = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            String useDb = "USE " + databaseName;
            String createUser = "CREATE TABLE IF NOT EXISTS user (\n" +
                            "    username VARCHAR(50) PRIMARY KEY,\n" +
                            "    password VARCHAR(50) NOT NULL,\n" +
                            "    email VARCHAR(100) NOT NULL\n" +
                            ")";
            String createAuth = "CREATE TABLE IF NOT EXISTS auth (\n" +
                            "    authToken VARCHAR(100) PRIMARY KEY,\n" +
                            "    username VARCHAR(50) NOT NULL,\n" +
                            "    FOREIGN KEY (username) REFERENCES user(username)\n" +
                            ")";
            String createGame = "CREATE TABLE IF NOT EXISTS game (\n" +
                            "    gameID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "    gameName VARCHAR(100) NOT NULL,\n" +
                            "    gameString VARCHAR(1000),\n" +
                            "    whitePlayer VARCHAR(50),\n" +
                            "    blackPlayer VARCHAR(50),\n" +
                            "    observers VARCHAR(255),\n" +
                            "    FOREIGN KEY (whitePlayer) REFERENCES user(username),\n" +
                            "    FOREIGN KEY (blackPlayer) REFERENCES user(username)\n" +
                            ");";
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            Statement stmt = conn.createStatement();
            stmt.addBatch(createDb);
            stmt.addBatch(useDb);
            stmt.addBatch(createUser);
            stmt.addBatch(createAuth);
            stmt.addBatch(createGame);
            stmt.executeBatch();

//            try (var preparedStatement = conn.prepareStatement(statement)) {
//                preparedStatement.setString(1, databaseName);
//                preparedStatement.setString(2, databaseName);
//                preparedStatement.executeUpdate();
//            }
            System.out.println("Database created successfully.");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
