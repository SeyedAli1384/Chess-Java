import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class VisualEffect {

    public static void flashRed(Button button) {
        String originalColor = ((getRow(button) + getCol(button)) % 2 == 0) ? ThemeManager.get00Theme() : ThemeManager.get01Theme();

        button.setStyle("-fx-background-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> button.setStyle("-fx-background-color: " + originalColor + ";"));
        pause.play();
    }

    public static void flashgreen(Button button) {
        String originalColor = ((getRow(button) + getCol(button)) % 2 == 0) ? ThemeManager.get00Theme() : ThemeManager.get01Theme();

        button.setStyle("-fx-background-color: #66cc88;");
        PauseTransition pause = new PauseTransition(Duration.seconds(100));
        pause.setOnFinished(e -> button.setStyle("-fx-background-color: " + originalColor + ";"));
        pause.play();
    }

    private static int getRow(Button button) {
        return Integer.parseInt(button.getId().split(" ")[0]);
    }

    private static int getCol(Button button) {
        return Integer.parseInt(button.getId().split(" ")[1]);
    }
}
