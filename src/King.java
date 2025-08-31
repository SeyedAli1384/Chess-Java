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
        if (!hasMoved && startRow == endRow) {
            // Kingside castling (O-O)
            if (endCol == startCol + 2) {
                Piece rook = pieces[startRow][7];
                if (rook instanceof Rook && !((Rook) rook).hasMoved()) {
                    if (pieces[startRow][5] == null && pieces[startRow][6] == null) {
                        return true;
                    }
                }
            }
            // Queenside castling (O-O-O)
            if (endCol == startCol - 2) {
                Piece rook = pieces[startRow][0];
                if (rook instanceof Rook && !((Rook) rook).hasMoved()) {
                    if (pieces[startRow][1] == null && pieces[startRow][2] == null && pieces[startRow][3] == null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
