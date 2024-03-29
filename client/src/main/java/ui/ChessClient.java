package ui;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class ChessClient {
    private String serverUrl;
    private String user = null;
    private String token = null;
    private boolean loggedIn = false;
    private GameData currentGame = null;
    private List<GameData> currentGames = null;
    private final ServerFacade server;
    private WebSocketFacade ws;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
                case "redraw" -> redrawBoard(params);
                case "leave" -> leaveGame(params);
                case "move" -> makeMove(params);
                case "resign" -> resignGame(params);
                case "highlight" -> highlightMoves(params);
                case "exit", "quit" -> "quit";
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
        return "Successfully logged out.";
    }

    private String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            String name = params[0];
            GameData gameData = server.createGame(name, token);
            if (gameData != null) {
                int id = gameData.gameID();
                return "Created new game.";
            }
        }
        throw new ResponseException(400, "Expected: <name>");
    }

    private String listGames(String... params) throws ResponseException {
        currentGames = server.listGames(token);
        if (currentGames != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("List of games:");
            for (int i = 0; i < currentGames.size(); i++) {
                GameData game = currentGames.get(i);
                builder.append(String.format("\n%d. %s", i + 1, game.gameName()));
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

            String color = params[1].toUpperCase();
            if (!(color.equals("WHITE") || color.equals("BLACK"))) {
                throw new ResponseException(400, "Expected: <id> <white|black>");
            }

            GameData joinedGame = currentGames.get(id-1);
            server.joinGame(joinedGame.gameID(), color, token);
            printBoard(joinedGame.game());
            currentGames = server.listGames(token);
            for (GameData game : currentGames) {
                if (game.gameID() == joinedGame.gameID()) {
                    currentGame = game;
                }
            }
            ws = new WebSocketFacade(serverUrl, this);
            ws.joinGame(token, joinedGame.gameID(), ChessGame.TeamColor.valueOf(color), user);
            return String.format("Successfully joined game %s as %s player.", joinedGame.gameName(), color);
        }
        throw new ResponseException(400, "Expected: <id> <white|black>");
    }

    private String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            int id;
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected: <id>");
            }

            GameData joinedGame = currentGames.get(id-1);
            server.observeGame(joinedGame.gameID(), token);
            printBoard(joinedGame.game());
            currentGames = server.listGames(token);
            for (GameData game : currentGames) {
                if (game.gameID() == joinedGame.gameID()) {
                    currentGame = game;
                }
            }
            return "Now observing game.";
        }
        throw new ResponseException(400, "Expected: <id>");
    }

    public String redrawBoard(String... params) {
        return "Redrawing board...";
    }

    private String leaveGame(String... params) {
        return "Leaving game...";
    }

    private String makeMove(String... params) {
        return "Making move...";
    }

    private String resignGame(String... params) {
        return "Resigning from game...";
    }

    public String highlightMoves(String... params) {
        return "Highlighting legal moves...";
    }

    private String help() {
        if (loggedIn) {
            if (currentGame != null) {
                return """
                      help - Displays this help command.
                      redraw - Redraws the chess board.
                      leave - Leaves the game.
                      move <pos1> <pos2> - Moves the chess piece at position 1 to position 2.
                      resign - Forfeits the game.
                      highlight <pos> - Highlights the legal moves for the piece at position.""";
            }
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

    private void printBoard(ChessGame game) {
        System.out.println(game);
    }
}
