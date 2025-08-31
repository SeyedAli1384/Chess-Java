import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

abstract class Piece {
    protected String color;
    protected ImageView icon;

    public Piece(String color, String imagePath) {
        this.color = color;
        String theme = ThemeManager.getPTheme();
        Image image = new Image(getClass().getResourceAsStream("/icons/" + theme + "/" + imagePath));
        this.icon = new ImageView(image);
    }

    public ImageView getIcon() {
        return icon;
    }

    public abstract boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] pieces);
}
