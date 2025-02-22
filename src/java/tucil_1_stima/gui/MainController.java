package tucil_1_stima.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MainController {
    @FXML
    private Button actionButton1;
    @FXML
    private Button actionButton2;
    @FXML
    private Button actionButton3;
    @FXML
    private Button actionButton4;
    @FXML
    private ImageView backgroundImageView;

    private AudioClip clickSound;
    private AudioClip hoverSound;

    @FXML
    public void initialize() {
        // Load and configure the background image
        Image bgImage = new Image(getClass().getResource("/tucil_1_stima/gui/assets/background.jpg").toExternalForm());
        backgroundImageView.setImage(bgImage);
        backgroundImageView.setScaleX(1.25);
        backgroundImageView.setScaleY(1.25);
        backgroundImageView.setOpacity(0.2);
        backgroundImageView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
                    double maxMove = 20;
                    double moveX = ((e.getSceneX() / newScene.getWidth()) - 0.5) * maxMove;
                    double moveY = ((e.getSceneY() / newScene.getHeight()) - 0.5) * maxMove;
                    backgroundImageView.setTranslateX(moveX);
                    backgroundImageView.setTranslateY(moveY);
                });
            }
        });

        // Initialize sound effects
        clickSound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/click.wav").toExternalForm());
        hoverSound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/hover.wav").toExternalForm());

        // Load the custom font
        Font genkiFont = Font.loadFont(getClass().getResource("/tucil_1_stima/gui/assets/GenkiDesu.otf").toExternalForm(), 24);
        if(genkiFont != null) {
            applyButtonFonts(actionButton1, genkiFont);
            applyButtonFonts(actionButton2, genkiFont);
            applyButtonFonts(actionButton3, genkiFont);
            applyButtonFonts(actionButton4, genkiFont);
        }

        // Apply the same hover/click effects to all buttons
        applyHoverEffects(actionButton1);
        applyHoverEffects(actionButton2);
        applyHoverEffects(actionButton3);
        applyHoverEffects(actionButton4);
    }
    private void applyButtonFonts(Button button, Font font) {
        String fontFamily = font.getName(); // Check what the font's family name is.
        button.setFont(font);
        button.setStyle("-fx-font-family: '" + fontFamily + "'; -fx-font-size: 24px;");
    }
    private void applyHoverEffects(Button button) {
        // Set the default opacity
        button.setOpacity(0.85);

        // Mouse Enter: shift left, scale up, and fade to full opacity
        button.setOnMouseEntered(e -> {
            if (hoverSound != null) {
                hoverSound.play();
            }
            TranslateTransition translate = new TranslateTransition(Duration.millis(100), button);
            translate.setToX(-30); // move 10px left

//            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
//            scale.setToX(1.05);
//            scale.setToY(1.05);

            FadeTransition fade = new FadeTransition(Duration.millis(100), button);
            fade.setToValue(1.0); // fully opaque

            ParallelTransition pt = new ParallelTransition(translate, fade);
            pt.play();
        });

        // Mouse Exit: reset translate, scale, and opacity
        button.setOnMouseExited(e -> {
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), button);
            translate.setToX(0);

//            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
//            scale.setToX(1.0);
//            scale.setToY(1.0);

            FadeTransition fade = new FadeTransition(Duration.millis(200), button);
            fade.setToValue(0.85); // back to default

            ParallelTransition pt = new ParallelTransition(translate, fade);
            pt.play();
        });

        // On click: play click sound and log button text
        button.setOnAction(e -> {
            if (clickSound != null) {
                clickSound.play();
            }
            System.out.println("Button clicked: " + button.getText());
        });
    }
}
