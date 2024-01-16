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
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch (type) {
            case PieceType.BISHOP:
                for (int i = 0; i < 4; i++) {
                    ChessPosition checkPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
                    while (true) {
                        switch (i) {
                            case 0:
                                checkPos = new ChessPosition(checkPos.getRow()+1, checkPos.getColumn()+1);
                                break;
                            case 1:
                                checkPos = new ChessPosition(checkPos.getRow()-1, checkPos.getColumn()+1);
                                break;
                            case 2:
                                checkPos = new ChessPosition(checkPos.getRow()-1, checkPos.getColumn()-1);
                                break;
                            case 3:
                                checkPos = new ChessPosition(checkPos.getRow()+1, checkPos.getColumn()-1);
                                break;
                        }

                        if (checkPos.getRow() > 8 || checkPos.getRow() < 1 || checkPos.getColumn() > 8 || checkPos.getColumn() < 1) {
                            break;
                        }
                        ChessPiece piece = board.getPiece(checkPos);
                        if (piece != null) {
                            break;
                        } else {
                            ChessMove validMove = new ChessMove(myPosition, checkPos, null);
                            moves.add(validMove);
                        }
                    }
                }
                break;
        }
        return moves;
    }
}
