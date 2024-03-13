import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class ChessClient {
    private String username = null;
    private String serverUrl;
    private final ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 C S 240- Chess client. Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame();
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String logout(String... params) throws ResponseException {
        return String.format("Successfully logged out as %s.", username);
    }

    private String createGame(String... params) throws ResponseException {
        return "Created new game.";
    }

    private String listGames(String... params) throws ResponseException {
        return "List of games:";
    }

    private String joinGame(String... params) throws ResponseException {
        return "Joined game.";
    }

    private String observeGame(String... params) throws ResponseException {
        throw new ResponseException(404, "Idk man");
    }

    private String help() {
        return "No help for you.";
    }
}
