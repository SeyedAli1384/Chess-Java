import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text title = new Text("chess");
        title.setFont(Font.font("Arial", 36));
        title.setStyle("-fx-fill: white;");

        // Buttons
        Button startBtn = new Button("Start Game");
        Button PthemeBtn = new Button("Piece Theme");
        Button BthemeBtn = new Button("Board Theme");
        Button exitBtn = new Button("Exit");

        String buttonStyle = "-fx-font-size: 14px; -fx-background-color: #fff; -fx-text-fill: black; -fx-background-radius: 20;";
        startBtn.setStyle(buttonStyle);
        PthemeBtn.setStyle(buttonStyle);
        BthemeBtn.setStyle(buttonStyle);
        exitBtn.setStyle(buttonStyle);

        startBtn.setPrefWidth(100);
        PthemeBtn.setPrefWidth(100);
        BthemeBtn.setPrefWidth(100);
        exitBtn.setPrefWidth(100);

        startBtn.setOnMouseEntered(e -> startBtn.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        startBtn.setOnMouseExited(e -> startBtn.setStyle(buttonStyle));
        PthemeBtn.setOnMouseEntered(e -> PthemeBtn.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        PthemeBtn.setOnMouseExited(e -> PthemeBtn.setStyle(buttonStyle));
        BthemeBtn.setOnMouseEntered(e -> BthemeBtn.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        BthemeBtn.setOnMouseExited(e -> BthemeBtn.setStyle(buttonStyle));
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;" ));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle(buttonStyle));

        startBtn.setOnAction(e -> {
            try {
                new GameMode().start(new Stage());
                Mover.setBaseColor();
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        PthemeBtn.setOnAction(e -> {
            try {
                new PieceTheme().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        BthemeBtn.setOnAction(e -> {
            try {
                new BoardTheme().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        exitBtn.setOnAction(e -> primaryStage.close());

        VBox menu = new VBox(20, title, startBtn, PthemeBtn, BthemeBtn, exitBtn);
        menu.setAlignment(Pos.CENTER);
        menu.setTranslateY(0);

        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResourceAsStream("/icons/9532b012b6.jpeg")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(460, 288, false, false, false, false)
        );

        StackPane root = new StackPane(menu);
        root.setBackground(new Background(bgImage));

        Scene scene = new Scene(root, 460, 288);
        primaryStage.setTitle("Hitler was Legend");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/Flag.png")));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

