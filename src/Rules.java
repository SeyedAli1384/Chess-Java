import java.util.ArrayList;
import java.util.List;

public class Rules {

    public static int counter = 0;

    public static class PiecePosition {
        int row, col;
        Piece piece;

        public PiecePosition(int row, int col, Piece piece) {
            this.row = row;
            this.col = col;
            this.piece = piece;
        }
    }

    // KING LOGIC
    public static PiecePosition findKing(ChessBoard game, String color) {
        Piece[][] board = game.getPieces();
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (board[r][c] instanceof King && board[r][c].color.equals(color))
                    return new PiecePosition(r, c, board[r][c]);
        return null;
    }

    public static boolean isKingInCheck(ChessBoard game, String kingColor) {
        PiecePosition king = findKing(game, kingColor);
        if (king == null) return false;

        for (PiecePosition attacker : getPiecesByColor(game, oppositeColor(kingColor))) {
            if (attacker.piece.isValidMove(attacker.row, attacker.col, king.row, king.col, game.getPieces()))
                return true;
        }
        return false;
    }

    public static boolean isCheckmate(ChessBoard game, String kingColor) {
        if (!isKingInCheck(game, kingColor)) return false;

        PiecePosition king = findKing(game, kingColor);
        if (king == null) return false;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int newRow = king.row + dr, newCol = king.col + dc;
                if (inBounds(newRow, newCol) && isValidMove(game, king.row, king.col, newRow, newCol))
                    return false;
            }
        }

        for (PiecePosition teammate : getPiecesByColor(game, kingColor)) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (isValidMove(game, teammate.row, teammate.col, r, c))
                        return false;
                }
            }
        }

        return true;
    }

    public static boolean isDraw(ChessBoard game, String color) {
        return isStalemate(game, color) || isInsufficientMaterial(game.getPieces()) || FiftyRules();
    }

    public static boolean isStalemate(ChessBoard game, String color) {
        if (isKingInCheck(game, color)) return false;

        for (PiecePosition piece : getPiecesByColor(game, color)) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (isValidMove(game, piece.row, piece.col, r, c)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean isInsufficientMaterial(Piece[][] board) {
        List<Piece> pieces = new ArrayList<>();
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (board[r][c] != null) pieces.add(board[r][c]);

        if (pieces.size() == 2) return true;

        // King + Bishop or King + Knight or King + 2ta Knight
        if (pieces.size() == 4 || pieces.size() == 3){
            for (Piece p : pieces) {
                int countWBishops = 0;
                int countBBishops = 0;
                int countWKnight = 0;
                int countBKnight = 0;
                if (p.color.equals("white")){
                    if (p instanceof Bishop)
                        countWBishops++;
                    else if (p instanceof Knight)
                        countWKnight++;
                }
                else if (p.color.equals("black")){
                    if (p instanceof Bishop)
                        countBBishops++;
                    else if (p instanceof Knight)
                        countBKnight++;
                }

                if (!(p instanceof King||p instanceof Bishop||p instanceof Knight) || countWBishops == 2 || countBBishops == 2 || countWKnight == countWBishops || countBKnight == countBBishops ){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public static boolean FiftyRules() {
        return counter >= 100; // 50 full moves = 100 moves for each side
    }

    public static boolean isValidMove(ChessBoard game, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow == toRow && fromCol == toCol) return false;

        Piece[][] board = game.getPieces();
        Piece piece = board[fromRow][fromCol];
        if (piece == null) return false;

        int[] enPassantTarget = game.getEnPassantTarget();
        if (piece instanceof Pawn && enPassantTarget != null
                && toRow == enPassantTarget[0] && toCol == enPassantTarget[1]) {
            if (Math.abs(toCol - fromCol) == 1 && ((piece.color.equals("w") && toRow == fromRow - 1) || (piece.color.equals("b") && toRow == fromRow + 1))) {
                return true;
            }
        }

        if (!piece.isValidMove(fromRow, fromCol, toRow, toCol, board))
            return false;

        if (piece instanceof King && Math.abs(toCol - fromCol) == 2 && fromRow == toRow)
            return canCastle(game, (King) piece, fromRow, fromCol, toRow, toCol);

        Piece[][] copy = copyBoard(board);
        if (piece instanceof Pawn && enPassantTarget != null && toRow == enPassantTarget[0] && toCol == enPassantTarget[1]) {
            copy[fromRow][toCol] = null; // remove captured pawn
        }
        copy[toRow][toCol] = copy[fromRow][fromCol];
        copy[fromRow][fromCol] = null;

        return !isKingInCheckSimulated(copy, piece.color);
    }

    private static boolean canCastle(ChessBoard game, King king, int row, int fromCol, int toRow, int toCol) {
        if (king.hasMoved()) return false;

        Piece[][] board = game.getPieces();
        int direction = (toCol - fromCol) > 0 ? 1 : -1;
        int rookCol = -1;

        // Find rook in castling direction
        for (int c = fromCol + direction; c >= 0 && c < 8; c += direction) {
            if (board[row][c] instanceof Rook && board[row][c].color.equals(king.color)) {
                rookCol = c;
                break;
            }
            if (board[row][c] != null && !(board[row][c] instanceof Rook)) return false; // Blocked
        }

        if (rookCol == -1) return false;

        Rook rook = (Rook) board[row][rookCol];
        if (rook.hasMoved()) return false;

        // Check if squares between king and rook are under attack
        for (int c = Math.min(fromCol, toCol); c <= Math.max(fromCol, toCol); c++) {
            if (isSquareUnderAttack(game, row, c, king.color)) return false;
        }

        return true;
    }


    private static boolean isSquareUnderAttack(ChessBoard game, int row, int col, String defenderColor) {
        for (PiecePosition enemy : getPiecesByColor(game, oppositeColor(defenderColor))) {
            if (enemy.piece.isValidMove(enemy.row, enemy.col, row, col, game.getPieces()))
                return true;
        }
        return false;
    }

    private static List<PiecePosition> getPiecesByColor(ChessBoard game, String color) {
        List<PiecePosition> list = new ArrayList<>();
        Piece[][] board = game.getPieces();
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (board[r][c] != null && board[r][c].color.equals(color))
                    list.add(new PiecePosition(r, c, board[r][c]));
        return list;
    }

    private static boolean inBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private static String oppositeColor(String color) {
        return color.equals("w") ? "b" : "w";
    }

    private static Piece[][] copyBoard(Piece[][] board) {
        Piece[][] copy = new Piece[8][8];
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                copy[r][c] = board[r][c];
        return copy;
    }

    private static boolean isKingInCheckSimulated(Piece[][] board, String kingColor) {
        int kingRow = -1, kingCol = -1;
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                if (board[r][c] instanceof King && board[r][c].color.equals(kingColor)) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
        if (kingRow == -1) return false;

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                Piece attacker = board[r][c];
                if (attacker != null && attacker.color.equals(oppositeColor(kingColor))) {
                    if (attacker.isValidMove(r, c, kingRow, kingCol, board))
                        return true;
                }
            }

        return false;
    }

    public static int[] getKingCoordinates(ChessBoard game, String color) {
        PiecePosition king = findKing(game, color);
        return king == null ? null : new int[]{king.row, king.col};
    }
}
