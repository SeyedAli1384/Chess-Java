import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class ChessBoard extends Application {
    private static final int SIZE = 8;
    private Button[][] board;
    private Piece[][] pieces;
    private Button selectedButton = null;
    private int selectedRow = -1, selectedCol = -1;
    private String currentTurn = "w";
    private GridPane grid;

    // Store the stage reference to control window actions
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        grid = new GridPane();
        board = new Button[SIZE][SIZE];
        pieces = new Piece[SIZE][SIZE];
        initializeBoard();

        Scene scene = new Scene(grid, 800, 800);
        primaryStage.setTitle("Chess");
        Image icon = new Image(getClass().getResourceAsStream("/icons/lichess-discord.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setMinSize(100, 100);
                button.setStyle((row + col) % 2 == 0
                        ? "-fx-background-color: " + ThemeManager.get00Theme() + ";"
                        : "-fx-background-color: " + ThemeManager.get01Theme() + ";");
                button.setId(row + " " + col);
                button.setOnAction(new Mover(this));  // Your existing move handler
                board[row][col] = button;
                grid.add(button, col, row);
            }
        }
        PieceState();
    }

    private void PieceState() {
        // Pawns
        for (int col = 0; col < SIZE; col++) {
            pieces[1][col] = new Pawn("b");
            pieces[6][col] = new Pawn("w");
            board[1][col].setGraphic(pieces[1][col].getIcon());
            board[6][col].setGraphic(pieces[6][col].getIcon());
        }

        // Rooks
        pieces[0][0] = new Rook("b");
        pieces[0][7] = new Rook("b");
        pieces[7][0] = new Rook("w");
        pieces[7][7] = new Rook("w");
        board[0][0].setGraphic(pieces[0][0].getIcon());
        board[0][7].setGraphic(pieces[0][7].getIcon());
        board[7][0].setGraphic(pieces[7][0].getIcon());
        board[7][7].setGraphic(pieces[7][7].getIcon());

        // Knights
        pieces[0][1] = new Knight("b");
        pieces[0][6] = new Knight("b");
        pieces[7][1] = new Knight("w");
        pieces[7][6] = new Knight("w");
        board[0][1].setGraphic(pieces[0][1].getIcon());
        board[0][6].setGraphic(pieces[0][6].getIcon());
        board[7][1].setGraphic(pieces[7][1].getIcon());
        board[7][6].setGraphic(pieces[7][6].getIcon());

        // Bishops
        pieces[0][2] = new Bishop("b");
        pieces[0][5] = new Bishop("b");
        pieces[7][2] = new Bishop("w");
        pieces[7][5] = new Bishop("w");
        board[0][2].setGraphic(pieces[0][2].getIcon());
        board[0][5].setGraphic(pieces[0][5].getIcon());
        board[7][2].setGraphic(pieces[7][2].getIcon());
        board[7][5].setGraphic(pieces[7][5].getIcon());

        // Queens
        pieces[0][3] = new Queen("b");
        pieces[7][3] = new Queen("w");
        board[0][3].setGraphic(pieces[0][3].getIcon());
        board[7][3].setGraphic(pieces[7][3].getIcon());

        // Kings
        pieces[0][4] = new King("b");
        pieces[7][4] = new King("w");
        board[0][4].setGraphic(pieces[0][4].getIcon());
        board[7][4].setGraphic(pieces[7][4].getIcon());
    }

    public void resetBoard() {
        stage.close();
        try {
            new ChessBoard().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnToMenu() {
        stage.close();
        try {
            new MainMenu().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters and setters
    public Button[][] getBoard() {
        return board;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public Button getSelectedButton() {
        return selectedButton;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setSelectedButton(Button button, int row, int col) {
        selectedButton = button;
        selectedRow = row;
        selectedCol = col;
    }

    public void clearSelection() {
        selectedButton = null;
        selectedRow = -1;
        selectedCol = -1;
    }

    public void switchTurn() {
        currentTurn = currentTurn.equals("w") ? "b" : "w";
    }
}
