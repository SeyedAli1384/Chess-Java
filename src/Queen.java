public class Queen extends Piece {

    public Queen(String color) {
        super(color, "q" + color + ".png");
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] pieces) {

        if (isSameColor(pieces[endRow][endCol])) {
            return false;
        } else {

            if (startRow == endRow) {
                boolean clear = isPathClear(startCol, endCol, startRow, true, pieces);
                if (clear) {
                    return true;
                } else {
                    return false;
                }
            }

            else if (startCol == endCol) {
                boolean clear = isPathClear(startRow, endRow, startCol, false, pieces);
                if (clear) {
                    return true;
                } else {
                    return false;
                }
            }

            else {
                int rowDiff = Math.abs(endRow - startRow);
                int colDiff = Math.abs(endCol - startCol);

                if (rowDiff == colDiff) {
                    boolean diagonalClear = isDiagonalPathClear(startRow, startCol, endRow, endCol, pieces);
                    if (diagonalClear) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private boolean isSameColor(Piece piece) {
        if (piece != null) {
            if (piece.color.equals(this.color)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathClear(int start, int end, int fixed, boolean isHorizontal, Piece[][] board) {
        int min = Math.min(start, end) + 1;
        int max = Math.max(start, end);

        for (int i = min; i < max; i++) {
            int row;
            int col;
            if (isHorizontal) {
                row = fixed;
                col = i;
            } else {
                row = i;
                col = fixed;
            }
            if (board[row][col] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isDiagonalPathClear(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int dx;
        int dy;

        if (endRow > startRow) {
            dx = 1;
        } else {
            dx = -1;
        }
        if (endCol > startCol) {
            dy = 1;
        } else {
            dy = -1;
        }

        int row = startRow + dx;
        int col = startCol + dy;
        while (row != endRow && col != endCol) {
            if (board[row][col] != null) {
                return false;
            }
            row = row + dx;
            col = col + dy;
        }
        return true;
    }
}
