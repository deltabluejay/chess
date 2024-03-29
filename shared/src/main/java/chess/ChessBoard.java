package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
    }

    public ChessBoard(ChessBoard boardCopy) {
        this.board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            this.board[i] = Arrays.copyOf(boardCopy.board[i], 8);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("White:\n\n");
        for (int i = board.length - 1; i >= 0; i--) {
            builder.append(String.format("%d   |", i+1));
            for (ChessPiece piece : board[i]) {
                if (piece != null) {
                    builder.append(piece);
                } else {
                    builder.append(" ");
                }
                builder.append("|");
            }
            builder.append("\n");
        }
        builder.append("\n     a b c d e f g h \n");

        builder.append("\n");

        builder.append("Black:\n\n");
        for (int i = 0; i < board.length; i++) {
            builder.append(String.format("%d   |", i+1));
            for (ChessPiece piece : board[i]) {
                if (piece != null) {
                    builder.append(piece);
                } else {
                    builder.append(" ");
                }
                builder.append("|");
            }
            builder.append("\n");
        }
        builder.append("\n     h g f e d c b a \n");

        return builder.toString();
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    /** * Sets the board to the default starting board * (How the game of chess normally starts) */
    public void resetBoard() {
        for (int i = 0; i < 8; i++ ) {
            board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[2][i] = null;
            board[3][i] = null;
            board[4][i] = null;
            board[5][i] = null;
            board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            switch (i) {
                case 0:
                case 7:
                    board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                    board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                    break;
                case 1:
                case 6:
                    board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                    break;
                case 2:
                case 5:
                    board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                    break;
                case 3:
                    board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                    board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                    break;
                case 4:
                    board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                    board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                    break;
            }
        }
    }
}
