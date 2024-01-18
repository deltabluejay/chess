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
    private boolean inBounds(ChessPosition myPosition) {
       return !(myPosition.getRow() > 8 || myPosition.getRow() < 1 || myPosition.getColumn() > 8 || myPosition.getColumn() < 1);
    }

    /**
     * Function specifically for calculating a pawn's moves
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> calculatePawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int forward;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            forward = 1;
        } else {
            forward = -1;
        }

        ChessPosition forwardPos = new ChessPosition(myPosition.getRow() + forward, myPosition.getColumn());
        ChessPosition checkLeftEnemy = new ChessPosition(myPosition.getRow() + forward, myPosition.getColumn() - 1);
        ChessPosition checkRightEnemy = new ChessPosition(myPosition.getRow() + forward, myPosition.getColumn() + 1);

        if (inBounds(forwardPos)) {
            ChessPiece forwardPiece = board.getPiece(forwardPos);
            if (forwardPiece == null) {
                ChessMove forwardMove;
                if (forwardPos.getRow() == 8 || forwardPos.getRow() == 1) {
                    forwardMove = new ChessMove(myPosition, forwardPos, PieceType.ROOK);
                    moves.add(forwardMove);
                    forwardMove = new ChessMove(myPosition, forwardPos, PieceType.QUEEN);
                    moves.add(forwardMove);
                    forwardMove = new ChessMove(myPosition, forwardPos, PieceType.KNIGHT);
                    moves.add(forwardMove);
                    forwardMove = new ChessMove(myPosition, forwardPos, PieceType.BISHOP);
                    moves.add(forwardMove);
                } else {
                    forwardMove = new ChessMove(myPosition, forwardPos, null);
                    moves.add(forwardMove);
                }

                if ((myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE) || (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK)) {
                    ChessPosition twoForwardPos = new ChessPosition(myPosition.getRow() + 2*forward, myPosition.getColumn());
                    ChessPiece twoForwardPiece = board.getPiece(twoForwardPos);
                    if (twoForwardPiece == null) {
                        ChessMove twoForwardMove = new ChessMove(myPosition, twoForwardPos, null);
                        moves.add(twoForwardMove);
                    }
                }
            }
        }

        if (inBounds(checkLeftEnemy)) {
            ChessPiece leftPiece = board.getPiece(checkLeftEnemy);
            if (leftPiece != null && leftPiece.getTeamColor() != pieceColor) {
                ChessMove leftCapture;
                if (forwardPos.getRow() == 8 || forwardPos.getRow() == 1) {
                    leftCapture = new ChessMove(myPosition, checkLeftEnemy, PieceType.ROOK);
                    moves.add(leftCapture);
                    leftCapture = new ChessMove(myPosition, checkLeftEnemy, PieceType.QUEEN);
                    moves.add(leftCapture);
                    leftCapture = new ChessMove(myPosition, checkLeftEnemy, PieceType.KNIGHT);
                    moves.add(leftCapture);
                    leftCapture = new ChessMove(myPosition, checkLeftEnemy, PieceType.BISHOP);
                    moves.add(leftCapture);
                } else {
                    leftCapture = new ChessMove(myPosition, checkLeftEnemy, null);
                    moves.add(leftCapture);
                }
            }
        }

        if (inBounds(checkRightEnemy)) {
            ChessPiece rightPiece = board.getPiece(checkRightEnemy);
            if (rightPiece != null && rightPiece.getTeamColor() != pieceColor) {
                ChessMove rightCapture;
                if (forwardPos.getRow() == 8 || forwardPos.getRow() == 1) {
                    rightCapture = new ChessMove(myPosition, checkRightEnemy, PieceType.ROOK);
                    moves.add(rightCapture);
                    rightCapture = new ChessMove(myPosition, checkRightEnemy, PieceType.QUEEN);
                    moves.add(rightCapture);
                    rightCapture = new ChessMove(myPosition, checkRightEnemy, PieceType.KNIGHT);
                    moves.add(rightCapture);
                    rightCapture = new ChessMove(myPosition, checkRightEnemy, PieceType.BISHOP);
                    moves.add(rightCapture);
                } else {
                    rightCapture = new ChessMove(myPosition, checkRightEnemy, null);
                    moves.add(rightCapture);
                }
            }
        }


        return moves;
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

                if (inBounds(checkPos)) {
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
                return calculatePawn(board, myPosition);
        }
        return null;
    }
}
