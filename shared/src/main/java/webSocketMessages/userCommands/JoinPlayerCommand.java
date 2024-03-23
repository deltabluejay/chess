package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    private Integer gameID;
    private ChessGame.TeamColor playerColor;
    private String username;

    public JoinPlayerCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor, String username) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.username = username;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public String getUsername() {
        return username;
    }
}
