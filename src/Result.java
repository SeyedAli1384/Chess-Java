import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Result {

    public static void showWinner(String winnerColor, ChessBoard game) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game Over");
        window.getIcons().add(new Image(Result.class.getResourceAsStream("/icons/lichess-discord.png")));
        window.setResizable(false);

        Text victoryMessage = new Text((winnerColor.equals("w") ? "White" : "Black") + " Wins!");
        victoryMessage.setFont(Font.font("Arial", 24));

        ImageView winnerImage = new ImageView();
        try {
            Image img = new Image("/icons/" + (winnerColor.equals("w") ? "whitewin.png" : "blackwin.png"));
            winnerImage.setImage(img);
            winnerImage.setFitWidth(200);
            winnerImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Image not found");
        }

        Button rematchButton = new Button("Rematch");
        Button menuButton = new Button("Menu");
        Button exitButton = new Button("Exit");

        String buttonStyle = "-fx-font-size: 14px; -fx-background-color: #fff; -fx-text-fill: black; -fx-background-radius: 20;";
        rematchButton.setStyle(buttonStyle);
        menuButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        rematchButton.setPrefWidth(100);
        menuButton.setPrefWidth(100);
        exitButton.setPrefWidth(100);

        rematchButton.setOnMouseEntered(e -> rematchButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        rematchButton.setOnMouseExited(e -> rematchButton.setStyle(buttonStyle));
        menuButton.setOnMouseEntered(e -> menuButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        menuButton.setOnMouseExited(e -> menuButton.setStyle(buttonStyle));
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;" ));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(buttonStyle));

        rematchButton.setOnAction(e -> {
            game.resetBoard();
            window.close();
        });

        menuButton.setOnAction(e -> {
            game.returnToMenu();
            window.close();
        });

        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(15);
        layout.getChildren().addAll(victoryMessage, winnerImage, rematchButton, menuButton, exitButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void showDraw(ChessBoard game) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game Over");
        window.getIcons().add(new Image(Result.class.getResourceAsStream("/icons/lichess-discord.png")));
        window.setResizable(false);

        Text drawMessage = new Text("Draw!");
        drawMessage.setFont(Font.font("Arial", 24));

        Button rematchButton = new Button("Rematch");
        Button menuButton = new Button("Menu");
        Button exitButton = new Button("Exit");

        String buttonStyle = "-fx-font-size: 14px; -fx-background-color: #fff; -fx-text-fill: black; -fx-background-radius: 20;";
        rematchButton.setStyle(buttonStyle);
        menuButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        rematchButton.setPrefWidth(100);
        menuButton.setPrefWidth(100);
        exitButton.setPrefWidth(100);

        rematchButton.setOnMouseEntered(e -> rematchButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        rematchButton.setOnMouseExited(e -> rematchButton.setStyle(buttonStyle));
        menuButton.setOnMouseEntered(e -> menuButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        menuButton.setOnMouseExited(e -> menuButton.setStyle(buttonStyle));
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 10;"));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(buttonStyle));

        rematchButton.setOnAction(e -> {
            game.resetBoard();
            window.close();
        });

        menuButton.setOnAction(e -> {
            game.returnToMenu();
            window.close();
        });

        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(15);
        layout.getChildren().addAll(drawMessage, rematchButton, menuButton, exitButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        window.setScene(scene);
        window.showAndWait();
    }

}
