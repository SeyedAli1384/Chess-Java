import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;

public class Effects {

    public static void flashRed(Button button) {
        String originalColor = ((getRow(button) + getCol(button)) % 2 == 0) ? ThemeManager.get00Theme() : ThemeManager.get01Theme();

        button.setStyle("-fx-background-color: red;");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> button.setStyle("-fx-background-color: " + originalColor + ";"));
        pause.play();
    }

    public class SoundEffect {
        private static final AudioClip MoveSound = new AudioClip(
                SoundEffect.class.getResource("/sounds/standard/Move.mp3").toExternalForm()
        );
        private static final AudioClip CaptureSound = new AudioClip(
                SoundEffect.class.getResource("/sounds/standard/Capture.mp3").toExternalForm()
        );
        private static final AudioClip ErrorSound = new AudioClip(
                SoundEffect.class.getResource("/sounds/standard/Error.mp3").toExternalForm()
        );

        public static void playMove() {
            MoveSound.play();
        }
        public static void playCapture() {
            CaptureSound.play();
        }
        public static void playError() {
            ErrorSound.play();
        }
    }

    private static int getRow(Button button) {
        return Integer.parseInt(button.getId().split(" ")[0]);
    }

    private static int getCol(Button button) {
        return Integer.parseInt(button.getId().split(" ")[1]);
    }
}
