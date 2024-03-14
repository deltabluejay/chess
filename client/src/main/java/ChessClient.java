import model.AuthData;
import model.GameData;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class ChessClient {
    private String user = null;
    private String token = null;
    private boolean loggedIn = false;
    private final ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(RESET_TEXT_COLOR + "C S 240- Chess client. Sign in to start.");
        System.out.println(SET_TEXT_COLOR_BLUE + help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            String username = params[0];
            String password = params[1];
            AuthData authData = server.login(username, password);
            if (authData.username() != null && authData.authToken() != null) {
                user = authData.username();
                token = authData.authToken();
                loggedIn = true;
                return String.format("Successfully logged in as %s.", user);
            }
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    private String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            AuthData authData = server.register(username, password, email);
            if (authData.username() != null && authData.authToken() != null) {
                user = authData.username();
                token = authData.authToken();
                loggedIn = true;
                return String.format("Successfully registered and logged in as %s.", user);
            }
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    private String logout(String... params) throws ResponseException {
        server.logout(token);
        user = null;
        token = null;
        loggedIn = false;
        return String.format("Successfully logged out as %s.", user);
    }

    private String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            String name = params[0];
            GameData gameData = server.createGame(name, token);
            if (gameData != null) {
                int id = gameData.gameID();
                return String.format("Created new game with ID %d.", id);
            }
        }
        throw new ResponseException(400, "Expected: <name>");
    }

    private String listGames(String... params) throws ResponseException {
        List<GameData> games = server.listGames(token);
        if (games != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("List of games:");
            for (GameData game : games) {
                builder.append(String.format("\n  %d: %s", game.gameID(), game.gameName()));
                builder.append(String.format("\n    White player: %s", game.whiteUsername()));
                builder.append(String.format("\n    Black player: %s", game.blackUsername()));
            }
            return builder.toString();
        }
        throw new ResponseException(400, "Error");
    }

    private String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            int id;
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected: <id> <white|black>");
            }

            String color = params[1];
            System.out.println(color);
            if (!(color.equals("white") || color.equals("black"))) {
                throw new ResponseException(400, "Expected: <id> <white|black>");
            }

            server.joinGame(id, color, token);
            return String.format("Successfully joined game as %s player.", color);
        }
        throw new ResponseException(400, "Expected: <id> <white|black>");
    }

    private String observeGame(String... params) throws ResponseException {
        throw new ResponseException(404, "Idk man");
    }

    private String help() {
        if (loggedIn) {
            return """
                      help - Displays this help command.
                      logout - Logs out.
                      create <name> - Creates a game.
                      list - Lists all games.
                      join <id> <white|black> - Joins a game.
                      observe <id> - Observes a game.
                      quit - Exits the program.""";
        } else {
            return """
                      help - Displays this help command.
                      login <username> <password> - Login using a username and password.
                      register <username> <password> <email> - Register a new user.
                      quit - Exits the program.""";
        }
    }
}