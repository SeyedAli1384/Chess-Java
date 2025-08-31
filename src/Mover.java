import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

class Mover implements EventHandler<ActionEvent> {
    private final ChessBoard game;

    private static final String HIGHLIGHT_COLOR = "#66cc88";
    private static final String WHITE_COLOR = ThemeManager.get00Theme();
    private static final String GRAY_COLOR = ThemeManager.get01Theme();

    public Mover(ChessBoard game) {
        this.game = game;
    }

    @Override
    public void handle(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();
        int row = getRow(clickedButton);
        int col = getCol(clickedButton);
        Piece[][] pieces = game.getPieces();

        if (game.getSelectedButton() == null) {
            handleSelection(clickedButton, row, col, pieces);
        } else {
            handleMove(clickedButton, row, col, pieces);
        }
    }

    private void handleSelection(Button button, int row, int col, Piece[][] pieces) {
        Piece piece = pieces[row][col];

        if (piece == null || !piece.color.equals(game.getCurrentTurn())) {
            VisualEffect.flashRed(button);
            return;
        }

        game.setSelectedButton(button, row, col);
        button.setStyle("-fx-background-color: " + HIGHLIGHT_COLOR + ";");
    }

    private void handleMove(Button targetButton, int targetRow, int targetCol, Piece[][] pieces) {
        int srcRow = game.getSelectedRow();
        int srcCol = game.getSelectedCol();
        Piece piece = pieces[srcRow][srcCol];

        if (piece != null && piece.isValidMove(srcRow, srcCol, targetRow, targetCol, pieces)) {
            if (!Rules.isValidMove(game, srcRow, srcCol, targetRow, targetCol)) {
                VisualEffect.flashRed(targetButton);
            } else {
                movePiece(piece, targetButton, targetRow, targetCol, srcRow, srcCol);
                checkStatus(piece);
                System.out.println(getMoveNotation(piece, targetRow, targetCol));
            }
        } else {
            VisualEffect.flashRed(targetButton);
        }

        resetPreviousSelection();
    }

    private void movePiece(Piece piece, Button targetButton, int toRow, int toCol, int fromRow, int fromCol) {
        // Handle castling
        if (piece instanceof King && Math.abs(toCol - fromCol) == 2) {
            int row = fromRow;

            if (toCol > fromCol) {
                // Kingside castling
                Piece rook = game.getPieces()[row][7];
                if (rook instanceof Rook) {
                    game.getPieces()[row][5] = rook;
                    game.getPieces()[row][7] = null;

                    Button rookFrom = game.getBoard()[row][7];
                    Button rookTo = game.getBoard()[row][5];
                    rookTo.setGraphic(rookFrom.getGraphic());
                    rookFrom.setGraphic(null);
                    ((Rook) rook).setMoved();
                }
            } else {
                // Queenside castling
                Piece rook = game.getPieces()[row][0];
                if (rook instanceof Rook) {
                    game.getPieces()[row][3] = rook;
                    game.getPieces()[row][0] = null;

                    Button rookFrom = game.getBoard()[row][0];
                    Button rookTo = game.getBoard()[row][3];
                    rookTo.setGraphic(rookFrom.getGraphic());
                    rookFrom.setGraphic(null);
                    ((Rook) rook).setMoved();
                }
            }

            ((King) piece).setMoved();
        }

        // Normal move
        targetButton.setGraphic(game.getSelectedButton().getGraphic());
        game.getSelectedButton().setGraphic(null);
        game.getPieces()[toRow][toCol] = piece;
        game.getPieces()[fromRow][fromCol] = null;

        if (piece instanceof King) ((King) piece).setMoved();
        if (piece instanceof Rook) ((Rook) piece).setMoved();

        // pawn promotion
        if (piece instanceof Pawn && (toRow == 0 || toRow == 7)) {
            Piece promotedPiece = Promotion.showPromotionDialog(piece.color);
            game.getPieces()[toRow][toCol] = promotedPiece;
            game.getBoard()[toRow][toCol].setGraphic(promotedPiece.getIcon());
            game.switchTurn();
            return;
        }

        game.switchTurn();
    }

    private void checkStatus(Piece movedPiece) {
        String opponentColor = movedPiece.color.equals("w") ? "b" : "w";

        if (Rules.isKingInCheck(game, opponentColor)) {
            int[] kingPos = Rules.getKingCoordinates(game, opponentColor);
            if (kingPos != null) {
                Button kingButton = game.getBoard()[kingPos[0]][kingPos[1]];
                VisualEffect.flashRed(kingButton);
            }
        }

        if (Rules.isCheckmate(game, opponentColor)) {
            Result.showWinner(movedPiece.color, game);
        } else if (Rules.isDraw(game, opponentColor)) {
            Result.showDraw(game);
        }
    }

    private void resetPreviousSelection() {
        Button previous = game.getSelectedButton();
        if (previous == null) return;

        int row = game.getSelectedRow();
        int col = game.getSelectedCol();
        String baseColor = (row + col) % 2 == 0 ? WHITE_COLOR : GRAY_COLOR;
        previous.setStyle("-fx-background-color: " + baseColor + ";");
        game.clearSelection();
    }

    private int getRow(Button button) {
        return Integer.parseInt(button.getId().split(" ")[0]);
    }

    private int getCol(Button button) {
        return Integer.parseInt(button.getId().split(" ")[1]);
    }

    private String getMoveNotation(Piece piece, int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        char FirstLetter = piece.getClass().getSimpleName().charAt(0);
        if (piece.getClass().getSimpleName().equals("Knight"))
            FirstLetter = 'N';

        return FirstLetter + "" + file + rank;
    }
}
