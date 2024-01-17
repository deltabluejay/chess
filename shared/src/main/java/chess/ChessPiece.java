package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        String sym = "z";
        switch (type) {
            case ROOK:
                sym = "r";
                break;
            case KNIGHT:
                sym = "n";
                break;
            case BISHOP:
                sym = "b";
                break;
            case QUEEN:
                sym = "q";
                break;
            case KING:
                sym = "k";
                break;
            case PAWN:
                sym = "p";
                break;
        }
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            sym = sym.toUpperCase();
        }
        return sym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Helper function for calculateMoves
     *
     * @return If myPosition is in the chess board boundaries or not
     */
    private boolean inBound(ChessPosition myPosition) {
       return !(myPosition.getRow() > 8 || myPosition.getRow() < 1 || myPosition.getColumn() > 8 || myPosition.getColumn() < 1);
    }

    /**
     * Helper function for pieceMoves
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> calculateMoves(int[][] directions, int limit, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int[] direction : directions) {
            ChessPosition checkPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
            for (int i = 0; i < limit; i++) {
                checkPos = new ChessPosition(checkPos.getRow() + direction[0], checkPos.getColumn() + direction[1]);

                if (inBound(checkPos)) {
                    ChessPiece piece = board.getPiece(checkPos);
                    ChessMove validMove = new ChessMove(myPosition, checkPos, null);
                    if (piece != null) {
                        if (piece.pieceColor != this.pieceColor) {
                            moves.add(validMove);
                        }
                        break;
                    } else {
                        moves.add(validMove);
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = null;
        switch (type) {
            case PieceType.BISHOP:
                directions = new int[][]{
                        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
                };
                return calculateMoves(directions, 100, board, myPosition);
            case PieceType.ROOK:
                directions = new int[][]{
                        {0, 1}, {1, 0}, {-1, 0}, {0, -1}
                };
                return calculateMoves(directions, 100, board, myPosition);
            case PieceType.QUEEN:
                directions = new int[][]{
                        {1, 1}, {1, -1}, {-1, 1}, {-1, -1},
                        {0, 1}, {1, 0}, {-1, 0}, {0, -1}
                };
                return calculateMoves(directions, 100, board, myPosition);
            case PieceType.KING:
                directions = new int[][]{
                        {1, 1}, {1, -1}, {-1, 1}, {-1, -1},
                        {0, 1}, {1, 0}, {-1, 0}, {0, -1}
                };
                return calculateMoves(directions, 1, board, myPosition);
            case PieceType.KNIGHT:
                directions = new int[][]{
                        {-1, 2}, {1, 2},
                        {-2, 1}, {-2, -1},
                        {-1, -2}, {1, -2},
                        {2, 1}, {2, -1}
                };
                return calculateMoves(directions, 1, board, myPosition);
            case PieceType.PAWN:

        }
        return null;
    }
}
