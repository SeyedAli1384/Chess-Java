import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

public class ChessBoard extends Application {
    private static final int SIZE = 8;
    private Button[][] board;
    private Piece[][] pieces;
    private double GamemodeT;
    private double Bonus;
    private String Gamemode;
    private String TimerMode;
    public Boolean Rotate;
    private Button selectedButton = null;
    private int selectedRow = -1, selectedCol = -1;
    private String currentTurn = "w";
    private GridPane grid;

    // Window reference
    private Stage stage;

    // A 3-column grid: [#] [W] [B]
    private GridPane movesGrid;
    private ScrollPane movesScroll;
    private int moveNumber = 1;         // current row number
    private boolean awaitingBlack = false; // if true, next move fills Black column of current row

    // Timer labels
    private Label whiteTimerLabel;
    private Label blackTimerLabel;
    private int whiteSeconds;
    private int blackSeconds;
    private Timeline timer;

    private static int enPassantRow = -1;
    private static int enPassantCol = -1;

    public ChessBoard(String mode, String TimerT, double timer, double bonus, Boolean rotate) {
        Gamemode = mode;
        TimerMode = TimerT;
        GamemodeT = timer;
        Bonus = 2 * bonus;
        Rotate = rotate;
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Left: chessboard grid
        grid = new GridPane();
        grid.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        board = new Button[SIZE][SIZE];
        pieces = new Piece[SIZE][SIZE];
        initializeBoard();

        // Top: timer + game mode panel
        HBox topPanel = buildTopPanel();

        // Right: move panel 200x900
        VBox rightPanel = buildMovePanel();

        // Left side (top panel + board stacked vertically)
        VBox leftSide = new VBox(topPanel, grid);
        leftSide.setPrefSize(800, 900);

        // Root layout
        BorderPane root = new BorderPane();
        root.setLeft(leftSide);
        root.setRight(rightPanel);

        // Scene: 1000x900
        Scene scene = new Scene(root, 1000, 900);
        primaryStage.setTitle("Chess");
        Image icon = new Image(getClass().getResourceAsStream("/icons/lichess-discord.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        startTimer();
    }

    private HBox buildTopPanel() {
        whiteTimerLabel = new Label("White Timer");
        blackTimerLabel = new Label("Black Timer");
        Label modeLabel = new Label("Game Mode: " + Gamemode+"-"+TimerMode);

        whiteTimerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        blackTimerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        modeLabel.setStyle("-fx-font-size: 18px;");

        HBox topPanel = new HBox(50, modeLabel, whiteTimerLabel, blackTimerLabel);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPrefSize(800, 100);
        topPanel.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/icons/background.jpg").toExternalForm(),
                        800, 100, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        topPanel.setBackground(new Background(bgImage));

        // initialize timers (Gamemode is in seconds)
        whiteSeconds = (int) GamemodeT;
        blackSeconds = (int) GamemodeT;

        return topPanel;
    }

    private VBox buildMovePanel() {
        // Header row: empty cell for number, then W and B
        movesGrid = new GridPane();
        movesGrid.setHgap(10);
        movesGrid.setVgap(6);
        movesGrid.setPadding(new Insets(10));

        ColumnConstraints numCol = new ColumnConstraints();
        numCol.setPercentWidth(20); // number column
        ColumnConstraints wCol = new ColumnConstraints();
        wCol.setPercentWidth(40);
        ColumnConstraints bCol = new ColumnConstraints();
        bCol.setPercentWidth(40);
        movesGrid.getColumnConstraints().addAll(numCol, wCol, bCol);

        Label numHeader = new Label("#");
        Label wHeader = new Label("W");
        Label bHeader = new Label("B");
        numHeader.setStyle("-fx-font-weight: bold;");
        wHeader.setStyle("-fx-font-weight: bold;");
        bHeader.setStyle("-fx-font-weight: bold;");

        movesGrid.add(numHeader, 0, 0);
        movesGrid.add(wHeader, 1, 0);
        movesGrid.add(bHeader, 2, 0);

        GridPane.setHalignment(numHeader, HPos.CENTER);
        GridPane.setHalignment(wHeader, HPos.CENTER);
        GridPane.setHalignment(bHeader, HPos.CENTER);

        // Prepare first row number
        addOrEnsureRowLabel(moveNumber);

        movesScroll = new ScrollPane(movesGrid);
        movesScroll.setFitToWidth(true);
        movesScroll.setPrefViewportWidth(200);
        movesScroll.setPrefViewportHeight(900);

        VBox rightPanel = new VBox(movesScroll);
        rightPanel.setPrefSize(200, 900);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        return rightPanel;
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setMinSize(100, 100);
                button.setMaxSize(100, 100);
                button.setStyle((row + col) % 2 == 0
                        ? "-fx-background-color: " + ThemeManager.get00Theme() + ";"
                        : "-fx-background-color: " + ThemeManager.get01Theme() + ";");
                button.setId(row + " " + col);
                button.setOnAction(new Mover(this));  // move handler
                board[row][col] = button;
                grid.add(button, col, row);
                GridPane.setHgrow(button, Priority.NEVER);
                GridPane.setVgrow(button, Priority.NEVER);
            }
        }
        PieceState();
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimers()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stopTimer() {
        timer.stop();
    }

    private void updateTimers() {
        if (currentTurn.equals("w")) {
            whiteSeconds--;
            if (whiteSeconds <= 0) {
                Mover.WinOnTime(this, "b");
            }
        } else {
            blackSeconds--;
            if (blackSeconds <= 0) {
                Mover.WinOnTime(this, "w");
            }
        }
        whiteTimerLabel.setText("White: "+formatTime(whiteSeconds));
        blackTimerLabel.setText("Black: "+formatTime(blackSeconds));
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void PieceState() {
        // Pawns
        for (int col = 0; col < SIZE; col++) {
            pieces[1][col] = new Pawn("b");
            pieces[6][col] = new Pawn("w");
            board[1][col].setGraphic(pieces[1][col].getIcon());
            board[6][col].setGraphic(pieces[6][col].getIcon());
        }
        if (Gamemode.equals("standard")) {
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
        else if (Gamemode.equals("chess960")) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                numbers.add(i);
            }
            do {
                Collections.shuffle(numbers);
            } while ((numbers.get(4) % 2) == (numbers.get(5) % 2) // Bishop same color and King not between two Rook
                    || ( (numbers.get(0) < numbers.get(7) && numbers.get(1) < numbers.get(7))
                    || (numbers.get(0) > numbers.get(7) && numbers.get(1) > numbers.get(7)) ));

            // Rooks
            pieces[0][numbers.get(0)] = new Rook("b");
            pieces[0][numbers.get(1)] = new Rook("b");
            pieces[7][numbers.get(0)] = new Rook("w");
            pieces[7][numbers.get(1)] = new Rook("w");
            board[0][numbers.get(0)].setGraphic(pieces[0][numbers.get(0)].getIcon());
            board[0][numbers.get(1)].setGraphic(pieces[0][numbers.get(1)].getIcon());
            board[7][numbers.get(0)].setGraphic(pieces[7][numbers.get(0)].getIcon());
            board[7][numbers.get(1)].setGraphic(pieces[7][numbers.get(1)].getIcon());

            // Knights
            pieces[0][numbers.get(2)] = new Knight("b");
            pieces[0][numbers.get(3)] = new Knight("b");
            pieces[7][numbers.get(2)] = new Knight("w");
            pieces[7][numbers.get(3)] = new Knight("w");
            board[0][numbers.get(2)].setGraphic(pieces[0][numbers.get(2)].getIcon());
            board[0][numbers.get(3)].setGraphic(pieces[0][numbers.get(3)].getIcon());
            board[7][numbers.get(2)].setGraphic(pieces[7][numbers.get(2)].getIcon());
            board[7][numbers.get(3)].setGraphic(pieces[7][numbers.get(3)].getIcon());

            // Bishops
            pieces[0][numbers.get(4)] = new Bishop("b");
            pieces[0][numbers.get(5)] = new Bishop("b");
            pieces[7][numbers.get(4)] = new Bishop("w");
            pieces[7][numbers.get(5)] = new Bishop("w");
            board[0][numbers.get(4)].setGraphic(pieces[0][numbers.get(4)].getIcon());
            board[0][numbers.get(5)].setGraphic(pieces[0][numbers.get(5)].getIcon());
            board[7][numbers.get(4)].setGraphic(pieces[7][numbers.get(4)].getIcon());
            board[7][numbers.get(5)].setGraphic(pieces[7][numbers.get(5)].getIcon());

            // Queens
            pieces[0][numbers.get(6)] = new Queen("b");
            pieces[7][numbers.get(6)] = new Queen("w");
            board[0][numbers.get(6)].setGraphic(pieces[0][numbers.get(6)].getIcon());
            board[7][numbers.get(6)].setGraphic(pieces[7][numbers.get(6)].getIcon());

            // Kings
            pieces[0][numbers.get(7)] = new King("b");
            pieces[7][numbers.get(7)] = new King("w");
            board[0][numbers.get(7)].setGraphic(pieces[0][numbers.get(7)].getIcon());
            board[7][numbers.get(7)].setGraphic(pieces[7][numbers.get(7)].getIcon());
        }
    }

    public void resetBoard() {
        stage.close();
        try {
            new ChessBoard(Gamemode, TimerMode, GamemodeT, Bonus, Rotate).start(new Stage());
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

    // Record a move
    public void recordMove(String moverColor, String notation) {
        addOrEnsureRowLabel(moveNumber);

        if (moverColor.equals("w")) {
            Label w = new Label(notation);
            movesGrid.add(w, 1, moveNumber);
            awaitingBlack = true;
        } else {
            Label b = new Label(notation);
            movesGrid.add(b, 2, moveNumber);
            moveNumber++;
            awaitingBlack = false;
            addOrEnsureRowLabel(moveNumber);
        }

        movesScroll.layout();
        movesScroll.setVvalue(1.0);
    }

    private void addOrEnsureRowLabel(int number) {
        if (number < 1) return;

        boolean exists = movesGrid.getChildren().stream().anyMatch(node ->
                GridPane.getRowIndex(node) != null &&
                        GridPane.getColumnIndex(node) != null &&
                        GridPane.getRowIndex(node) == number &&
                        GridPane.getColumnIndex(node) == 0
        );
        if (!exists) {
            Label num = new Label(String.valueOf(number));
            movesGrid.add(num, 0, number);
            GridPane.setHalignment(num, HPos.CENTER);
        }
    }

    // Getters and setters
    public int[] getEnPassantTarget() {
        if (enPassantRow >= 0 && enPassantCol >= 0) {
            return new int[]{enPassantRow, enPassantCol};
        }
        return null;
    }
    public static boolean getEnPassant() {
        if (enPassantRow >= 0 && enPassantCol >= 0) {
            return true;
        }
        return false;
    }
    public static int getEnpassantrow() {
        return enPassantRow;
    }
    public static int getEnpassantcol() {
        return enPassantCol;
    }
    public void setEnpassantTarget(int enPassantRow, int enPassantCol) {
        this.enPassantRow = enPassantRow;
        this.enPassantCol = enPassantCol;
    }
    public Button[][] getBoard() { return board; }
    public Piece[][] getPieces() { return pieces; }
    public Button getSelectedButton() { return selectedButton; }
    public int getSelectedRow() { return selectedRow; }
    public int getSelectedCol() { return selectedCol; }
    public String getCurrentTurn() { return currentTurn; }

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

    public void Rotation() {
        Piece[][] newPieces = new Piece[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (pieces[row][col] != null) {
                    int newRow = SIZE - 1 - row;
                    int newCol = SIZE - 1 - col;
                    newPieces[newRow][newCol] = pieces[row][col];
                }
            }
        }
        pieces = newPieces;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col].setGraphic(null);
                if (pieces[row][col] != null) {
                    board[row][col].setGraphic(pieces[row][col].getIcon());
                }
            }
        }
    }

    public void switchTurn() {
        if (Bonus > 0) {
            if (currentTurn.equals("w")) {
                Bonus -= 5;
                whiteSeconds += 5;
            } else if (currentTurn.equals("b")) {
                Bonus -= 5;
                blackSeconds += 5;
            }
        }
        if (Rotate){
            Rotation();
        }
        
        currentTurn = currentTurn.equals("w") ? "b" : "w";
    }
}
