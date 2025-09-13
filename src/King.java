public class King extends Piece {

    private boolean hasMoved = false;

    public King(String color) {
        super(color, "k" + color + ".png");
    }

    public void setMoved() {
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] pieces) {

        if (pieces[endRow][endCol] != null && pieces[endRow][endCol].color.equals(this.color)) {
            return false;
        }

        if (Math.abs(endRow - startRow) <= 1 && Math.abs(endCol - startCol) <= 1) {
            return true;
        }

        // Castling conditions
        if (!hasMoved && startRow == endRow && Math.abs(endCol - startCol) == 2) {
            int direction = (endCol - startCol) > 0 ? 1 : -1;
            int rookCol = -1;

            // Search for rook in the direction of castling
            for (int c = startCol + direction; c >= 0 && c < 8; c += direction) {
                if (pieces[startRow][c] instanceof Rook) {
                    rookCol = c;
                    break;
                }
                if (pieces[startRow][c] != null && !(pieces[startRow][c] instanceof Rook)) break; // Blocked
            }

            if (rookCol != -1) {
                Rook rook = (Rook) pieces[startRow][rookCol];
                if (!rook.hasMoved()) {
                    return true;
                }
            }
        }

        return false;
    }
}
