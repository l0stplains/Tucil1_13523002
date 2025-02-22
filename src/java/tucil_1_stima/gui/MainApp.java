package tucil_1_stima.gui;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainApp extends Application {

    private MediaPlayer backgroundPlayer;

    @Override
    public void start(Stage stage) {
        // welcome text
        Text splashText = new Text("Welcome to\n" + "IQ Puzzler Pro UwU");

        // Load custom font from resources
        Font customFont = Font.loadFont(
                getClass().getResource("/tucil_1_stima/gui/assets/GenkiDesu.otf").toExternalForm(),
                48
        );
        if (customFont != null) {
            splashText.setFont(customFont);
        } else {
            splashText.setFont(Font.font("System", 48));
        }
        splashText.setStyle("-fx-fill: white;");
        splashText.setTextAlignment(TextAlignment.CENTER);
        splashText.setOpacity(0); // Start fully transparent

        // Set up splash layout with a dark background
        StackPane splashLayout = new StackPane(splashText);
        StackPane.setAlignment(splashText, Pos.CENTER); // CENTER TEXT
        splashLayout.setStyle("-fx-background-color: black;");
        Scene splashScene = new Scene(splashLayout, 1200, 675);
        stage.setScene(splashScene);
        stage.setTitle("Loading...");
        stage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), splashText);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), splashText);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(2)); // Hold for 2 seconds before fading out

        fadeIn.setOnFinished(e -> fadeOut.play());
        fadeOut.setOnFinished(e -> loadMainScene(stage));
        fadeIn.play();
    }

    private void loadMainScene(Stage stage) {
        try {
            // Load main FXML scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tucil_1_stima/gui/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 675);
            scene.getStylesheets().add(getClass().getResource("/tucil_1_stima/gui/assets/styles.css").toExternalForm());
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();

            // Initialize and play background music
            initAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAudio() {
        try {
            // Load background music file from resources
            String bgMusicPath = getClass().getResource("/tucil_1_stima/gui/assets/bgm.mp3").toExternalForm();
            Media bgMedia = new Media(bgMusicPath);
            backgroundPlayer = new MediaPlayer(bgMedia);
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundPlayer.play();
        } catch (Exception e) {
            System.out.println("Error loading background music: " + e.getMessage());
        }
    }

    public static void show(){
        launch();
    }
}
