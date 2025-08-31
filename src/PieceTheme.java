import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PieceTheme extends Application {

    String Theme;

    @Override
    public void start(Stage primaryStage) {
        VBox themeList = new VBox(10);
        themeList.setPadding(new Insets(15));
        themeList.setAlignment(Pos.TOP_LEFT);

        themeList.getChildren().add(createThemeItem("alpha", "icons/alpha/pb.png"));
        themeList.getChildren().add(createThemeItem("caliente", "icons/caliente/pb.png"));
        themeList.getChildren().add(createThemeItem("fantasy", "icons/fantasy/pb.png"));
        themeList.getChildren().add(createThemeItem("kosal", "icons/kosal/pb.png"));
        themeList.getChildren().add(createThemeItem("maestro", "icons/maestro/pb.png"));
        themeList.getChildren().add(createThemeItem("shape", "icons/shape/pb.png"));
        themeList.getChildren().add(createThemeItem("spatial", "icons/spatial/pb.png"));


        ScrollPane scrollPane = new ScrollPane(themeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400);

        Scene scene = new Scene(scrollPane, 350, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Choose Your piece Theme");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/lichess-discord.png")));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private HBox createThemeItem(String themeName, String imagePath) {
        Button selectButton = new Button("   ");
        selectButton.setFont(Font.font(14));
        selectButton.setOnAction(e -> {
            ThemeManager.setPTheme(themeName);
            ((Stage) selectButton.getScene().getWindow()).close();
        });


        Text themeLabel = new Text(themeName);
        themeLabel.setFont(Font.font(16));
        themeLabel.setWrappingWidth(120);

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(imagePath, 100, 100, true, true);
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Image not found: " + imagePath);
        }

        HBox item = new HBox(15, selectButton, themeLabel, imageView);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f5f5f5;");

        return item;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
