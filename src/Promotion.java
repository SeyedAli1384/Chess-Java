import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Promotion {

    public static Piece showPromotionDialog(String color) {
        final Piece[] selected = new Piece[1];
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.getIcons().add(new Image(Result.class.getResourceAsStream("/icons/lichess-discord.png")));
        window.setTitle("Choose Promotion");

        // Create promoted pieces using your constructor style
        Piece queen = new Queen(color);
        Piece rook = new Rook(color);
        Piece bishop = new Bishop(color);
        Piece knight = new Knight(color);

        // Create buttons with their icons
        Button queenBtn = new Button();
        queenBtn.setGraphic(queen.getIcon());
        queenBtn.setOnAction(e -> { selected[0] = queen; window.close(); });

        Button rookBtn = new Button();
        rookBtn.setGraphic(rook.getIcon());
        rookBtn.setOnAction(e -> { selected[0] = rook; window.close(); });

        Button bishopBtn = new Button();
        bishopBtn.setGraphic(bishop.getIcon());
        bishopBtn.setOnAction(e -> { selected[0] = bishop; window.close(); });

        Button knightBtn = new Button();
        knightBtn.setGraphic(knight.getIcon());
        knightBtn.setOnAction(e -> { selected[0] = knight; window.close(); });

        HBox choices = new HBox(10, queenBtn, rookBtn, bishopBtn, knightBtn);
        VBox layout = new VBox(10, choices);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        window.setScene(new Scene(layout));
        window.showAndWait();
        return selected[0];
    }
}
