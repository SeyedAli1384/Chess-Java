class Pawn extends Piece {
    public Pawn(String color) {
        super(color, "p" + color + ".png");
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        int direction;
        int startRow;

        if (Piece.Rotate) {
            direction = -1;
            startRow = 6;
        } else {
            direction = (this.color.equals("w")) ? -1 : 1;
            startRow = (this.color.equals("w")) ? 6 : 1;
        }

        // Normal 1-step forward
        if (toCol == fromCol && toRow == fromRow + direction && board[toRow][toCol] == null) {
            return true;
        }

        // Double step from starting row
        if (toCol == fromCol && fromRow == startRow && toRow == fromRow + 2 * direction
                && board[fromRow + direction][fromCol] == null && board[toRow][toCol] == null) {
            return true;
        }

        // Normal diagonal capture
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction
                && board[toRow][toCol] != null && !board[toRow][toCol].color.equals(color)) {
            return true;
        }

        // En Passant Capture
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction && board[toRow][toCol] == null) {
            // There must be an enemy pawn adjacent at fromRow and toCol
            Piece adjacent = board[fromRow][toCol];
            if (adjacent instanceof Pawn && !adjacent.color.equals(this.color)) {
                if (ChessBoard.getEnPassant() && ChessBoard.getEnpassantrow()==toRow && ChessBoard.getEnpassantcol()==toCol)
                    return true;
            }
        }
        return false;
    }
}
