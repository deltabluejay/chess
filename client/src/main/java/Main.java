import chess.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        var serverUrl = "http://localhost:1337";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new ChessClient(serverUrl).run();
    }
}