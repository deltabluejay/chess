package clientTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import server.Server;
import service.GameService;
import model.*;
import ui.*;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(1337);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDb() {
        GameService.clear();
    }

    @Test
    public void registerTestPass() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email.com");
        assertNotNull(auth);
    }

    @Test
    public void registerTestFail() throws ResponseException {
        registerTestPass();
        assertThrows(ResponseException.class, this::registerTestPass);
    }

    @Test
    public void loginTestPass() throws ResponseException {
        registerTestPass();
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        assertNotNull(facade.login("username", "password"));
    }

    @Test
    public void loginTestFail() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        assertThrows(ResponseException.class, () -> facade.login("username", "password"));
    }

    @Test
    public void logoutTestPass() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email");
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    public void logoutTestFail() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        assertThrows(ResponseException.class, () -> facade.logout(""));
    }

    @Test
    public void createTestPass() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email");
        assertDoesNotThrow(() -> facade.createGame("asdf", auth.authToken()));
    }

    @Test
    public void createTestFail() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        assertThrows(ResponseException.class, () -> facade.createGame("asdf", ""));
    }

    @Test
    void listTestPass() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email");
        assertDoesNotThrow(() -> facade.listGames(auth.authToken()));
    }

    @Test
    void listTestFail() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        assertThrows(ResponseException.class, () -> facade.listGames(""));
    }

    @Test
    void joinTestPass() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email");
        GameData game = facade.createGame("asdf", auth.authToken());
        assertDoesNotThrow(() -> facade.joinGame(game.gameID(), "WHITE", auth.authToken()));
    }

    @Test
    void joinTestFail() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email");
        assertThrows(ResponseException.class, () -> facade.joinGame(-1, "WHITE", auth.authToken()));
    }

    @Test
    void observeTestPass() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email");
        GameData game = facade.createGame("asdf", auth.authToken());
        assertDoesNotThrow(() -> facade.observeGame(game.gameID(), auth.authToken()));
    }

    @Test
    void observeTestFail() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:1337");
        AuthData auth = facade.register("username", "password", "email@email");
        assertThrows(ResponseException.class, () -> facade.observeGame(-1, auth.authToken()));
    }
}
