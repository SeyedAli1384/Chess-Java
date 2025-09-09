import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameMode extends Application {

    // default
    private double selectedSecond = 10;
    private String selectedMode = "Classic";

    @Override
    public void start(Stage stage) {
        Label title = new Label("Choose Game Mode");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Classic section
        Label classicLabel = new Label("Classic:");
        HBox classicButtons = new HBox(10,
                new Button("10 min"),
                new Button("15|10 min"),
                new Button("30 min"));
        //classicButtons.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        classicButtons.setAlignment(Pos.CENTER);

        // Blitz section
        Label blitzLabel = new Label("Blitz:");
        HBox blitzButtons = new HBox(10,
                new Button("3 min"),
                new Button("3|2 min"),
                new Button("5 min"));
        blitzButtons.setAlignment(Pos.CENTER);

        // Bullet section
        Label bulletLabel = new Label("Bullet:");
        HBox bulletButtons = new HBox(10,
                new Button("1 min"),
                new Button("1|1 min"),
                new Button("2|1 min"));
        bulletButtons.setAlignment(Pos.CENTER);

        // Collect all buttons
        Button[] allButtons = {
                (Button) classicButtons.getChildren().get(0),
                (Button) classicButtons.getChildren().get(1),
                (Button) classicButtons.getChildren().get(2),
                (Button) blitzButtons.getChildren().get(0),
                (Button) blitzButtons.getChildren().get(1),
                (Button) blitzButtons.getChildren().get(2),
                (Button) bulletButtons.getChildren().get(0),
                (Button) bulletButtons.getChildren().get(1),
                (Button) bulletButtons.getChildren().get(2)
        };

        // Assign actions
        ((Button) classicButtons.getChildren().get(0)).setOnAction(e -> selectMode((Button) classicButtons.getChildren().get(0), allButtons, "Classic",600));
        ((Button) classicButtons.getChildren().get(1)).setOnAction(e -> selectMode((Button) classicButtons.getChildren().get(1), allButtons,"Classic" ,900));
        ((Button) classicButtons.getChildren().get(2)).setOnAction(e -> selectMode((Button) classicButtons.getChildren().get(2), allButtons, "Classic",1800));
        ((Button) blitzButtons.getChildren().get(0)).setOnAction(e -> selectMode((Button) blitzButtons.getChildren().get(0), allButtons, "Blitz",180));
        ((Button) blitzButtons.getChildren().get(1)).setOnAction(e -> selectMode((Button) blitzButtons.getChildren().get(1), allButtons, "Blitz",180));
        ((Button) blitzButtons.getChildren().get(2)).setOnAction(e -> selectMode((Button) blitzButtons.getChildren().get(2), allButtons, "Blitz",300));
        ((Button) bulletButtons.getChildren().get(0)).setOnAction(e -> selectMode((Button) bulletButtons.getChildren().get(0), allButtons, "Bullet",60));
        ((Button) bulletButtons.getChildren().get(1)).setOnAction(e -> selectMode((Button) bulletButtons.getChildren().get(1), allButtons, "Bullet",60));
        ((Button) bulletButtons.getChildren().get(2)).setOnAction(e -> selectMode((Button) bulletButtons.getChildren().get(2), allButtons, "Bullet",120));

        // Layout each section (Label + Buttons in a VBox)
        VBox classicBox = new VBox(5, classicLabel, classicButtons);
        classicBox.setAlignment(Pos.CENTER);
        VBox blitzBox = new VBox(5, blitzLabel, blitzButtons);
        blitzBox.setAlignment(Pos.CENTER);
        VBox bulletBox = new VBox(5, bulletLabel, bulletButtons);
        bulletBox.setAlignment(Pos.CENTER);

        VBox modeBox = new VBox(15, classicBox, blitzBox, bulletBox);
        modeBox.setAlignment(Pos.CENTER);

        // Bottom buttons
        Button startButton = new Button("Start");
        Button closeButton = new Button("Close");

        startButton.setOnAction(e -> {
            stage.close();
            new ChessBoard(selectedMode, selectedSecond).start(new Stage());
        });

        closeButton.setOnAction(e -> {
            stage.close();
            new MainMenu().start(new Stage());
        });

        HBox bottomBox = new HBox(20, startButton, closeButton);
        bottomBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, title, modeBox, bottomBox);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 450, 350);
        stage.setTitle("Game Mode");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/Flag.png")));
        stage.setScene(scene);
        stage.show();
    }

    private void selectMode(Button selected, Button[] all, String mode,double second) {
        for (Button b : all) {
            b.setStyle(""); // reset others
        }
        selected.setStyle("-fx-background-color: lightgray;");
        selectedSecond = second;
        selectedMode = mode;
    }
}
