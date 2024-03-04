package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DataAccessTests {
    private AuthDAO createAuthDAO() {
        return new AuthDAOSQL();
    }

    private UserDAO createUserDAO() {
        return new UserDAOSQL();
    }

    private GameDAO createGameDAO() {
        return new GameDAOSQL();
    }

    @BeforeEach
    public void clearDb() throws Exception {
        AuthDAO authAccess = createAuthDAO();
        UserDAO userAccess = createUserDAO();
        GameDAO gameAccess = createGameDAO();
        authAccess.clear();
        userAccess.clear();
        gameAccess.clear();
    }

    @Test
    public void authClearPass() throws Exception {
        AuthDAO authAccess = createAuthDAO();
        assertDoesNotThrow(() -> {authAccess.clear();});
    }

    @Test
    public void userClearPass() throws Exception {
        UserDAO userAccess = createUserDAO();
        assertDoesNotThrow(() -> {userAccess.clear();});
    }

    @Test
    public void gameClearPass() throws Exception {
        GameDAO gameAccess = createGameDAO();
        assertDoesNotThrow(() -> {gameAccess.clear();});
    }

    @Test
    public void authCreatePass() throws Exception {
        UserDAO userAccess = createUserDAO();
        AuthDAO authAccess = createAuthDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);
        assertNotNull(authAccess.createAuthToken(user.username()));
    }

    @Test
    public void authCreateFail() throws Exception {
        AuthDAO authAccess = createAuthDAO();
        assertNull(authAccess.createAuthToken("no"));
    }

    @Test
    public void authGetPass() throws Exception {
        UserDAO userAccess = createUserDAO();
        AuthDAO authAccess = createAuthDAO();

        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);

        AuthData auth = authAccess.createAuthToken(user.username());
        assertNotNull(authAccess.getAuthToken(auth.authToken()));
    }

    @Test
    public void authGetFail() throws Exception {
        AuthDAO authAccess = createAuthDAO();
        assertNull(authAccess.getAuthToken("randomstring"));
    }

    @Test
    public void authDeletePass() throws Exception {
        UserDAO userAccess = createUserDAO();
        AuthDAO authAccess = createAuthDAO();

        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);

        AuthData auth = authAccess.createAuthToken(user.username());
        assertTrue(authAccess.deleteAuthToken(auth.authToken()));
    }

    @Test
    public void authDeleteFail() throws Exception {
        AuthDAO authAccess = createAuthDAO();
        assertFalse(authAccess.deleteAuthToken("randomstring"));
    }

    @Test
    public void userCreatePass() throws Exception {
        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        assertTrue(userAccess.createUser(user));
    }

    @Test
    public void userCreateFail() throws Exception {
        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);
        assertFalse(userAccess.createUser(user));
    }

    @Test
    public void userGetByDataPass() throws Exception {
        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);
        assertNotNull(userAccess.getUser(user));
    }

    @Test
    public void userGetByDataFail() throws Exception {
        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        assertNull(userAccess.getUser(user));
    }

    @Test
    public void userGetByNamePass() throws Exception {
        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);
        assertNotNull(userAccess.getUser(user.username()));
    }

    @Test
    public void userGetByNameFail() throws Exception {
        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        assertNull(userAccess.getUser(user.username()));
    }

    @Test
    public void gameListPass() throws Exception {
        GameDAO gameAccess = createGameDAO();
        gameAccess.create("mygame");
        assertNotNull(gameAccess.list());
    }

    // there's not really a fail for list
    @Test
    public void gameListFail() throws Exception {
        GameDAO gameAccess = createGameDAO();
        List<GameData> expected = new ArrayList<>();
        Object[] expectedArray = expected.toArray();
        List<GameData> actual = gameAccess.list();
        Object[] actualArray = expected.toArray();

        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void gameExistsPass() throws Exception {
        GameDAO gameAccess = createGameDAO();
        int id = gameAccess.create("mygame");
        assertTrue(gameAccess.exists(id));
    }

    @Test
    public void gameExistsFail() throws Exception {
        GameDAO gameAccess = createGameDAO();
        assertFalse(gameAccess.exists(0));
    }

    @Test
    public void gameCreatePass() throws Exception {
        GameDAO gameAccess = createGameDAO();
        int id = gameAccess.create("mygame");
        assertNotNull(gameAccess.list());
        assertTrue(gameAccess.exists(id));
    }

    @Test
    public void gameCreateFail() throws Exception {
        GameDAO gameAccess = createGameDAO();
        assertEquals(-1, gameAccess.create(null));
    }

    @Test
    public void gameJoinPass() throws Exception {
        GameDAO gameAccess = createGameDAO();
        int id = gameAccess.create("mygame");

        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);

        assertTrue(gameAccess.join("idk", "WHITE", id));
    }

    @Test
    public void gameJoinFail() throws Exception {
        GameDAO gameAccess = createGameDAO();

        UserDAO userAccess = createUserDAO();
        UserData user = new UserData("idk", "1234", "example@example.com");
        userAccess.createUser(user);

        assertTrue(gameAccess.join("idk", "WHITE", 0));
    }
}
