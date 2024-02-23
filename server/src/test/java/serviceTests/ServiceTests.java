package serviceTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import passoffTests.testClasses.TestException;
import service.*;
import model.*;

public class ServiceTests {

    @BeforeEach
    public void clearDb() throws Exception {
        GameService.clear();
    }

    @Test
    public void registerUserPass() throws Exception {
        UserData newUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        assertNotNull(UserService.register(newUser));
    }

    @Test
    public void registerUserFail() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        UserData newUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        assertThrows(AlreadyTakenError.class, () -> {UserService.register(newUser);});
    }

    @Test
    public void loginUserPass() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        assertNotNull(UserService.login(existingUser));
    }

    @Test
    public void loginUserFail() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        assertThrows(UnauthorizedError.class, () -> {UserService.login(existingUser);});
    }

    @Test
    public void logoutUserPass() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        AuthData loggedInUser = UserService.login(existingUser);
        assertDoesNotThrow(() -> {UserService.logout(loggedInUser.authToken());});
    }

    @Test
    public void logoutUserFail() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        AuthData loggedInUser = UserService.login(existingUser);
        UserService.logout(loggedInUser.authToken());
        assertThrows(UnauthorizedError.class, () -> {UserService.logout(loggedInUser.authToken());});
    }

    @Test
    public void clearDbPass() throws Exception {
        assertDoesNotThrow(() -> {GameService.clear();});
    }

    @Test
    public void listGamesPass() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        AuthData loggedInUser = UserService.login(existingUser);

        assertNotNull(GameService.list(loggedInUser.authToken()));
    }

    @Test
    public void listGamesFail() throws Exception {
        assertThrows(UnauthorizedError.class, () -> {GameService.list("");});
    }

    @Test
    public void createGamePass() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        AuthData loggedInUser = UserService.login(existingUser);

        assertNotNull(GameService.create(loggedInUser.authToken(), "someGame"));
    }

    @Test
    public void createGameFail() throws Exception {
        assertThrows(UnauthorizedError.class, () -> {GameService.create("", "someGame");});
    }

    @Test
    public void joinGamePass() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        AuthData loggedInUser = UserService.login(existingUser);

        int gameID = GameService.create(loggedInUser.authToken(), "someGame");
        assertDoesNotThrow(() -> {GameService.join(loggedInUser.authToken(), "WHITE", gameID);});
    }

    @Test
    public void joinGameFail() throws Exception {
        UserData existingUser = new UserData("newUser", "somePassword", "someEmail@email.com");
        UserService.register(existingUser);
        AuthData loggedInUser = UserService.login(existingUser);

        int gameID = GameService.create(loggedInUser.authToken(), "someGame");
        assertThrows(BadRequestError.class, () -> {GameService.join(loggedInUser.authToken(), "WHITE", gameID-1);});
    }
}
