import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

class Mover implements EventHandler<ActionEvent> {
    private final ChessBoard game;

    private static final String HIGHLIGHT_COLOR = "#66cc88";
    private static String WHITE_COLOR = ThemeManager.get00Theme();
    private static String GRAY_COLOR = ThemeManager.get01Theme();

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
            Effects.flashRed(button);
            Effects.SoundEffect.playError();
            return;
        }

        game.setSelectedButton(button, row, col);
        button.setStyle("-fx-background-color: " + HIGHLIGHT_COLOR + ";");
    }

    private void handleMove(Button targetButton, int targetRow, int targetCol, Piece[][] pieces) {
        int srcRow = game.getSelectedRow();
        int srcCol = game.getSelectedCol();
        Piece piece = pieces[srcRow][srcCol];

        // If clicked the same square that is already selected: do nothing (keep selection)
        if (targetRow == srcRow && targetCol == srcCol) {
            return;
        }

        // If clicked on a friendly piece switch selection to that piece
        Piece clickedPiece = pieces[targetRow][targetCol];
        if (clickedPiece != null && clickedPiece.color.equals(game.getCurrentTurn())) {
            resetPreviousSelection();
            game.setSelectedButton(targetButton, targetRow, targetCol);
            targetButton.setStyle("-fx-background-color: " + HIGHLIGHT_COLOR + ";");
            return;
        }

        if (piece != null && piece.isValidMove(srcRow, srcCol, targetRow, targetCol, pieces)) {
            if (!Rules.isValidMove(game, srcRow, srcCol, targetRow, targetCol)) {
                Effects.flashRed(targetButton);
                Effects.SoundEffect.playError();
            } else {
                // Capture info BEFORE move
                Piece targetPieceBefore = game.getPieces()[targetRow][targetCol];
                String moverColor = piece.color;

                boolean isCastle = (piece instanceof King) && Math.abs(targetCol - srcCol) == 2;
                boolean isPromotion = (piece instanceof Pawn) && (targetRow == 0 || targetRow == 7);

                movePiece(piece, targetButton, targetRow, targetCol, srcRow, srcCol);

                // Promotion result
                Piece pieceAfter = game.getPieces()[targetRow][targetCol];

                if (targetPieceBefore != null) {
                    Effects.SoundEffect.playCapture();
                }
                else{
                    Effects.SoundEffect.playMove();
                }

                // Record notation to right panel
                String notation = formatMoveNotation(piece, pieceAfter, srcRow, srcCol, targetRow, targetCol, targetPieceBefore, isCastle, isPromotion);
                game.recordMove(moverColor, notation);

                // Status checks
                checkStatus(pieceAfter != null ? pieceAfter : piece);
            }
        } else {
            Effects.flashRed(targetButton);
            Effects.SoundEffect.playError();
        }

        resetPreviousSelection();
    }

    private void movePiece(Piece piece, Button targetButton, int toRow, int toCol, int fromRow, int fromCol) {

        Piece[][] pieces = game.getPieces();
        Button[][] board = game.getBoard();

        Piece targetPiece = game.getPieces()[toRow][toCol]; // check if capture happens

        // If capture or pawn move reset counter
        if (targetPiece != null || piece instanceof Pawn) {
            Rules.counter = 0;
        } else {
            Rules.counter++;
        }

        // Enpassant
        if (piece instanceof Pawn) {
            // Normal en passant capture (diagonal into empty square)
            if (Math.abs(toCol - fromCol) == 1 && toRow != fromRow && pieces[toRow][toCol] == null) {
                int capturedRow = fromRow;
                int capturedCol = toCol;
                pieces[capturedRow][capturedCol] = null;
                board[capturedRow][capturedCol].setGraphic(null);
            }

            // Check if pawn moved 2 squares forward mark en passant square
            if (Math.abs(toRow - fromRow) == 2) {
                game.setEnpassantTarget((fromRow + toRow) / 2 , fromCol);
            } else {
                game.setEnpassantTarget(-1,-1);
                game.setEnpassantTarget(-1,-1);
            }
        } else {
            game.setEnpassantTarget(-1,-1);
            game.setEnpassantTarget(-1,-1);
        }


        // Handle castling
        if (piece instanceof King && Math.abs(toCol - fromCol) == 2) {
            int direction = (toCol - fromCol) > 0 ? 1 : -1;
            int rookCol = -1;

            for (int c = fromCol + direction; c >= 0 && c < 8; c += direction) {
                if (pieces[fromRow][c] instanceof Rook && pieces[fromRow][c].color.equals(piece.color)) {
                    rookCol = c;
                    break;
                }
            }

            if (rookCol != -1) {
                Piece rook = pieces[fromRow][rookCol];
                int rookTargetCol = (direction > 0) ? toCol - 1 : toCol + 1;

                if (rookCol == rookTargetCol) {
                    ((Rook) rook).setMoved();
                } else {
                    pieces[fromRow][rookTargetCol] = rook;
                    pieces[fromRow][rookCol] = null;

                    Button rookFrom = game.getBoard()[fromRow][rookCol];
                    Button rookTo = game.getBoard()[fromRow][rookTargetCol];
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
                Effects.flashRed(kingButton);
                Effects.SoundEffect.playError();
            }
        }

        if (Rules.isCheckmate(game, opponentColor)) {
            Rules.counter = 0;
            game.stopTimer();
            Result.showWinner(movedPiece.color, game);
        } else if (Rules.isDraw(game, opponentColor)) {
            Rules.counter = 0;
            game.stopTimer();
            Result.showDraw(game);
        }
    }

    public static void WinOnTime(ChessBoard game, String winner) {
        Rules.counter = 0;
        game.stopTimer();
        Platform.runLater(() -> {
            Result.showWinner(winner, game);
        });
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
    public static void setBaseColor(){
        GRAY_COLOR = ThemeManager.get01Theme();
        WHITE_COLOR = ThemeManager.get00Theme();
    }

    private int getRow(Button button) {
        return Integer.parseInt(button.getId().split(" ")[0]);
    }

    private int getCol(Button button) {
        return Integer.parseInt(button.getId().split(" ")[1]);
    }

    private String formatMoveNotation(Piece originalPiece, Piece pieceAfter, int srcRow, int srcCol, int dstRow, int dstCol, Piece targetPieceBefore, boolean isCastle, boolean isPromotion) {
        // Castling
        if (isCastle) {
            if (game.Rotate && game.getCurrentTurn().equals("w")){
                return (dstCol < srcCol) ? "O-O" : "O-O-O";
            }else {
                return (dstCol > srcCol) ? "O-O" : "O-O-O";
            }
        }

        boolean isCapture = (targetPieceBefore != null);
        String dstSquare = squareName(dstRow, dstCol);

        String pieceLetter = pieceLetter(originalPiece);
        String text;

        if (originalPiece instanceof Pawn) {
            if (isCapture) {
                // For pawn capture include source file (e.g., exd5)
                char srcFile;
                if (game.Rotate && game.getCurrentTurn().equals("w")){
                    srcFile = (char) ('h' - srcCol);
                }else {
                    srcFile = (char) ('a' + srcCol);
                }
                text = srcFile + pieceLetter + "x" + dstSquare;
            } else {
                text = dstSquare;
            }
        } else {
            text = pieceLetter + (isCapture ? "x" : "") + dstSquare;
        }

        // Promotion
        if (isPromotion && pieceAfter != null && !(pieceAfter instanceof Pawn)) {
            String promoLetter = pieceLetter(pieceAfter);
            text = text + "=" + promoLetter;
        }

        return text;
    }

    private String squareName(int row, int col) {
        if (game.Rotate && game.getCurrentTurn().equals("w")) {
            char file = (char) ('h' - col);
            int rank = 1 + row;
            return "" + file + rank;
        } else {
            char file = (char) ('a' + col);
            int rank = 8 - row;
            return "" + file + rank;
        }
    }

    private String pieceLetter(Piece p) {
        String name = p.getClass().getSimpleName();
        char initial = name.charAt(0);
        if (name.equals("Knight")) initial = 'N';
        return String.valueOf(initial);
    }
}
