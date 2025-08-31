import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BoardTheme extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox themeList = new VBox(10);
        themeList.setPadding(new Insets(15));
        themeList.setAlignment(Pos.TOP_LEFT);

        themeList.getChildren().add(createBoardThemeItem("default", "white", "gray"));
        themeList.getChildren().add(createBoardThemeItem("Classic", "#f0d9b5", "#b58863"));
        themeList.getChildren().add(createBoardThemeItem("Lichess", "#eed2aa", "#a67d5d"));
        themeList.getChildren().add(createBoardThemeItem("Ocean", "#b3e5fc", "#0288d1"));
        themeList.getChildren().add(createBoardThemeItem("Forest", "#cdeac0", "#4c6444"));
        themeList.getChildren().add(createBoardThemeItem("Mono", "#eeeeee", "#444444"));
        themeList.getChildren().add(createBoardThemeItem("Sandstorm", "#f5deb3", "#8b5a2b"));
        themeList.getChildren().add(createBoardThemeItem("Dracula", "#f8f8f2", "#282a36"));
        themeList.getChildren().add(createBoardThemeItem("Solarized", "#fdf6e3", "#073642"));
        themeList.getChildren().add(createBoardThemeItem("Neon", "#d4fc79", "#96e6a1"));
        themeList.getChildren().add(createBoardThemeItem("Checkers", "#ff4040", "#464646"));

        ScrollPane scrollPane = new ScrollPane(themeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400);

        Scene scene = new Scene(scrollPane, 400, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Board Theme");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private HBox createBoardThemeItem(String name, String lightColorStr, String darkColorStr) {
        Button selectBtn = new Button("Select");
        selectBtn.setFont(Font.font(14));

        selectBtn.setOnAction(e -> {
            ThemeManager.setBTheme(lightColorStr, darkColorStr);
            ((Stage) selectBtn.getScene().getWindow()).close();
        });

        Text nameLabel = new Text(name);
        nameLabel.setFont(Font.font(16));
        nameLabel.setWrappingWidth(100);

        Rectangle lightSquare = new Rectangle(40, 40, Color.web(lightColorStr));
        lightSquare.setStroke(Color.GRAY);

        Rectangle darkSquare = new Rectangle(40, 40, Color.web(darkColorStr));
        darkSquare.setStroke(Color.GRAY);

        HBox item = new HBox(15, selectBtn, nameLabel, lightSquare, darkSquare);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f5f5f5;");

        return item;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
