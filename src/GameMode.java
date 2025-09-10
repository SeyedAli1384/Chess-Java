import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameMode extends Application {

    private double selectedSecond = 600; // default 10 min
    public double Bonus = 0;
    private String ModeType = "Rapid";
    private String ModeName = "standard";
    String buttonStyle = "-fx-font-size: 14px; -fx-background-color: #ffffff; -fx-border-color: black; -fx-border-radius: 20;-fx-text-fill: black; -fx-background-radius: 20;";

    @Override
    public void start(Stage stage) {
        // Left side: Game type
        Label leftTitle = new Label("Game mode");
        leftTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button standardBtn = new Button("Standard");
        standardBtn.setStyle(buttonStyle);
        Button chess960Btn = new Button("Chess 960");
        chess960Btn.setStyle(buttonStyle);

        standardBtn.setMinWidth(120);
        chess960Btn.setMinWidth(120);

        VBox leftBox = new VBox(30, leftTitle, standardBtn, chess960Btn);
        leftBox.setAlignment(Pos.TOP_CENTER);
        leftBox.setPadding(new Insets(30));

        // Right side: Time controls
        Label bulletLabel = new Label("Bullet:");
        bulletLabel.setStyle("-fx-font-weight: bold;");
        Button b1 = new Button("1 min");
        b1.setStyle(buttonStyle);
        Button b2 = new Button("1|1");
        b2.setStyle(buttonStyle);
        Button b3 = new Button("1|2");
        b3.setStyle(buttonStyle);
        equalizeButtonSize(b1, b2, b3);
        HBox bulletRow = new HBox(15, b1, b2, b3);
        bulletRow.setAlignment(Pos.CENTER_LEFT);

        Label blitzLabel = new Label("Blitz:");
        blitzLabel.setStyle("-fx-font-weight: bold;");
        Button bl1 = new Button("3 min");
        bl1.setStyle(buttonStyle);
        Button bl2 = new Button("3|2");
        bl2.setStyle(buttonStyle);
        Button bl3 = new Button("5 min");
        bl3.setStyle(buttonStyle);
        equalizeButtonSize(bl1, bl2, bl3);
        HBox blitzRow = new HBox(15, bl1, bl2, bl3);
        blitzRow.setAlignment(Pos.CENTER_LEFT);

        Label rapidLabel = new Label("Rapid:");
        rapidLabel.setStyle("-fx-font-weight: bold;");
        Button r1 = new Button("10 min");
        r1.setStyle(buttonStyle);
        Button r2 = new Button("15|10");
        r2.setStyle(buttonStyle);
        Button r3 = new Button("30 min");
        r3.setStyle(buttonStyle);
        equalizeButtonSize(r1, r2, r3);
        HBox rapidRow = new HBox(15, r1, r2, r3);
        rapidRow.setAlignment(Pos.CENTER_LEFT);

        // Collect all buttons
        List<Button> TButtons = new ArrayList<>();
        TButtons.addAll(List.of(b1, b2, b3, bl1, bl2, bl3, r1, r2, r3));
        List<Button> MButtons = new ArrayList<>();
        MButtons.addAll(List.of(standardBtn, chess960Btn));

        // Assign actions
        standardBtn.setOnAction(e -> selectMode(standardBtn, MButtons, "standard"));
        chess960Btn.setOnAction(e -> selectMode(chess960Btn, MButtons, "chess960"));

        r1.setOnAction(e -> selectMode(r1, TButtons, "Rapid", 600));
        r2.setOnAction(e -> selectMode(r2, TButtons, "Rapid", 900, 600));
        r3.setOnAction(e -> selectMode(r3, TButtons, "Rapid", 1800));
        bl1.setOnAction(e -> selectMode(bl1, TButtons, "Blitz", 180));
        bl2.setOnAction(e -> selectMode(bl2, TButtons, "Blitz", 180, 120));
        bl3.setOnAction(e -> selectMode(bl3, TButtons, "Blitz", 300));
        b1.setOnAction(e -> selectMode(b1, TButtons, "Bullet", 60));
        b2.setOnAction(e -> selectMode(b2, TButtons, "Bullet", 60, 60));
        b3.setOnAction(e -> selectMode(b3, TButtons, "Bullet", 120, 60));

        // Start button at bottom-left of right section
        Button startBtn = new Button("Start");
        startBtn.setStyle(buttonStyle);

        startBtn.setOnMouseEntered(e -> startBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #888; -fx-border-color: black; -fx-border-radius: 20;-fx-text-fill: black; -fx-background-radius: 20;"));
        startBtn.setOnMouseExited(e -> startBtn.setStyle(buttonStyle));

        startBtn.setMinWidth(80);
        startBtn.setOnAction(e -> {
            stage.close();
            new ChessBoard(ModeName, ModeType, selectedSecond, Bonus).start(new Stage());
        });
        BorderPane rightPane = new BorderPane();
        VBox timeBox = new VBox(20, bulletLabel, bulletRow, blitzLabel, blitzRow, rapidLabel, rapidRow);
        timeBox.setPadding(new Insets(20));
        timeBox.setAlignment(Pos.TOP_LEFT);

        rightPane.setCenter(timeBox);
        rightPane.setBottom(startBtn);
        BorderPane.setAlignment(startBtn, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(startBtn, new Insets(10));

        // Layout: split left & right with borders
        HBox mainContent = new HBox();
        mainContent.getChildren().addAll(leftBox, rightPane);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        leftBox.setStyle("-fx-border-color: black; -fx-border-width: 5 0 5 5;");
        rightPane.setStyle("-fx-border-color: black; -fx-border-width: 5 5 5 5;");

        Scene scene = new Scene(mainContent, 650, 420);
        stage.setTitle("Game Mode");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/Flag.png")));
        stage.setScene(scene);

        // closes window (go back to main menu)
        stage.setOnCloseRequest(e -> {
            stage.close();
            new MainMenu().start(new Stage());
        });

        stage.show();
    }

    private void equalizeButtonSize(Button... buttons) {
        for (Button b : buttons) {
            b.setMinWidth(70);
        }
    }

    private void selectMode(Button selected, List<Button> all, String mode, double second) {
        selectMode(selected, all, mode, second, 0);
    }

    private void selectMode(Button selected, List<Button> all, String modeT, double second, double bonus) {
        for (Button b : all) {
            b.setStyle(buttonStyle); // reset all
        }
        selected.setStyle("-fx-font-size: 14px; -fx-background-color: #888; -fx-border-color: black; -fx-border-radius: 20;-fx-text-fill: black; -fx-background-radius: 20;");
        selectedSecond = second;
        ModeType = modeT;
        Bonus = bonus;
    }

    private void selectMode(Button selected, List<Button> all, String selectedmode) {
        for (Button b : all) {
            b.setStyle(buttonStyle); // reset all
        }
        selected.setStyle("-fx-font-size: 14px; -fx-background-color: #888; -fx-border-color: black; -fx-border-radius: 20;-fx-text-fill: black; -fx-background-radius: 20;");
        ModeName = selectedmode;
    }
}
