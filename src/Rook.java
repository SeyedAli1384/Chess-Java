public class Rook extends Piece {

    private boolean hasMoved = false;

    public Rook(String color) {
        super(color, "r" + color + ".png");
    }

    public void setMoved() {
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] pieces) {

        if (isSameColor(pieces[endRow][endCol])) {
            return false;
        }

        if (startRow == endRow) {
            return isPathClear(startCol, endCol, startRow, true, pieces);
        }

        else if (startCol == endCol) {
            return isPathClear(startRow, endRow, startCol, false, pieces);
        }

        return false;
    }

    private boolean isSameColor(Piece piece) {
        return piece != null && piece.color.equals(this.color);
    }

    private boolean isPathClear(int start, int end, int fixed, boolean isHorizontal, Piece[][] board) {
        int min = Math.min(start, end) + 1;
        int max = Math.max(start, end);

        for (int i = min; i < max; i++) {
            int row = isHorizontal ? fixed : i;
            int col = isHorizontal ? i : fixed;

            if (board[row][col] != null) {
                return false; // Something is blocking the path
            }
        }
        return true;
    }
}
