package tucil_1_stima.gui;

import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import tucil_1_stima.model.Board;
import tucil_1_stima.model.Block;
import tucil_1_stima.solver.PuzzleSolver;
import tucil_1_stima.util.AppState;
import tucil_1_stima.util.PuzzleConfig;
import javafx.scene.paint.Color;
import tucil_1_stima.gui.BoardViewBuilder;
import tucil_1_stima.gui.BlockViewBuilder;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SolveController {
    @FXML private StackPane boardPane;
    @FXML private BorderPane borderPane;
    @FXML private Label timerLabel;
    @FXML private Label caseLabel;
    @FXML private Label timerTitle;
    @FXML private Label caseTitle;
    @FXML private Label countdownLabel;
    @FXML private ProgressIndicator loadingIndicator;
    @FXML private Button backButton;
    @FXML private Button saveButton;
    @FXML private Button saveTxtButton;
    @FXML private Button solveButton;
    @FXML private ImageView backgroundImageView;
    @FXML private ListView<String> puzzleListView; // puzzle config list

    private AudioClip count3Sound, count2Sound, count1Sound, goSound, clickSound, finishSound, failedSound, hoverSound, backSound;
    private MediaPlayer pageBgm;

    private Board board;
    private List<Block> blocks;
    private PuzzleConfig config;
    @FXML
    public void initialize() {
        // Parallax background
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
        // Sounds
        count3Sound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/3.wav").toExternalForm());
        count2Sound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/2.wav").toExternalForm());
        count1Sound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/1.wav").toExternalForm());
        goSound     = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/go.wav").toExternalForm());
        clickSound  = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/click.wav").toExternalForm());
        finishSound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/congratulations.wav").toExternalForm());
        failedSound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/cry.wav").toExternalForm());
        hoverSound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/hover.wav").toExternalForm());
        backSound = new AudioClip(getClass().getResource("/tucil_1_stima/gui/assets/back.wav").toExternalForm());

        Media mediaBgm = new Media(getClass().getResource("/tucil_1_stima/gui/assets/slowBgm.mp3").toExternalForm());
        pageBgm = new MediaPlayer(mediaBgm);
        pageBgm.setCycleCount(MediaPlayer.INDEFINITE);
        pageBgm.setVolume(0.15);
        pageBgm.play();

        applyBackButtonEffects(backButton);


        // Fonts
        DropShadow outline = new DropShadow();
        outline.setOffsetX(0);
        outline.setOffsetY(0);
        outline.setColor(Color.BLACK);
        outline.setRadius(3);
        outline.setSpread(0.7);
        Font genkiFont36 = Font.loadFont(getClass().getResource("/tucil_1_stima/gui/assets/GenkiDesu.otf").toExternalForm(), 36);
        Font genkiFont24 = Font.loadFont(getClass().getResource("/tucil_1_stima/gui/assets/GenkiDesu.otf").toExternalForm(), 24);
        timerTitle.setFont(genkiFont36);
        caseTitle.setFont(genkiFont36);
        backButton.setFont(genkiFont24);
        saveButton.setFont(genkiFont24);
        saveTxtButton.setFont(genkiFont24);
        solveButton.setFont(genkiFont24);

        timerTitle.setEffect(outline);
        caseTitle.setEffect(outline);
        timerLabel.setEffect(outline);
        caseLabel.setEffect(outline);

        // Effect
        applyHoverEffects(saveButton);
        applyHoverEffects(saveTxtButton);
        applyHoverEffects(solveButton);
        saveButton.setOpacity(0.3);
        saveTxtButton.setOpacity(0.3);
        solveButton.setOpacity(0.3);

        countdownLabel.setStyle("-fx-stroke: black; -fx-stroke-width: 2px;");

        // Setup puzzle list
        refreshPuzzleListView();
        puzzleListView.setOnMouseClicked(e -> {
            String selected = puzzleListView.getSelectionModel().getSelectedItem();
            if(selected != null) {
                config = AppState.findPuzzleConfigByFileName(selected);
                if(config != null) {
                    AppState.setBoard(config.getBoard());
                    AppState.setBlocks(config.getBlocks());
                    AppState.setInitialized(true);
                    Long timeBefore = config.getSearchTime();
                    Long caseBefore = config.getCasesExamined();
                    if(timeBefore == null){
                        timerLabel.setText("-");
                    } else {
                        timerLabel.setText(timeBefore + "ms");
                    }
                    if(caseBefore == null){
                        caseLabel.setText("-");
                    } else {
                        caseLabel.setText(caseBefore + "");
                    }
                    // Show the new puzzle
                    loadCurrentPuzzle();
                }
            }
        });

//        if(AppState.isInitialized()) {
//            loadCurrentPuzzle();
//        }

        saveButton.setOnAction(e -> saveBoardAsPNG());
        saveTxtButton.setOnAction(e -> saveBoardAsTXT());
        // Solve button triggers countdown
        solveButton.setOnAction(e -> {
            if(clickSound != null) clickSound.play();
            startCountdownAndSolve();
        });
    }

    private void loadCurrentPuzzle() {
        saveButton.setDisable(false);
        saveTxtButton.setDisable(false);
        solveButton.setDisable(false);
        solveButton.setOpacity(0.85);
        saveButton.setOpacity(0.85);
        saveTxtButton.setOpacity(0.85);
        this.board = AppState.getBoard();
        this.blocks = AppState.getBlocks();
        if(board == null || blocks == null) {
            // Clear board
            boardPane.getChildren().clear();
            return;
        }
        // Show the board
        Pane boardView = BoardViewBuilder.buildBoardView(board);
        boardPane.getChildren().clear();
        boardPane.getChildren().add(boardView);
        // timerLabel.setText("Ready to solve!");
    }

    private void refreshPuzzleListView() {
        puzzleListView.getItems().clear();
        for(var config : AppState.getPuzzleConfigs()) {
            puzzleListView.getItems().add(config.getFileName());
        }
    }

    private void startCountdownAndSolve() {
        if(board == null || blocks == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No puzzle selected!");
            alert.showAndWait();
            return;
        }
        board.reset();
        config.setCasesExamined(null);
        config.setSearchTime(null);
        loadCurrentPuzzle();
        // Countdown
        SequentialTransition seq = new SequentialTransition();
        seq.getChildren().add(createFadeMessage("3", "#FFFF00", count3Sound));
        seq.getChildren().add(createFadeMessage("2", "#FFFF00", count2Sound));
        seq.getChildren().add(createFadeMessage("1", "#FFFF00", count1Sound));
        seq.getChildren().add(createFadeMessage("GO", "#00FF00", goSound));
        seq.setOnFinished(e -> startSolving());
        seq.play();
    }

    private SequentialTransition createFadeMessage(String text, String color, AudioClip sound){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), countdownLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus == Animation.Status.RUNNING) {
                countdownLabel.setText(text);
                countdownLabel.setStyle("-fx-font-size: 72px; -fx-text-fill: " + color + ";");
            }
        });
        fadeIn.setOnFinished(e -> {
            sound.play();
        });

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), countdownLabel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(0.5));

        SequentialTransition transition = new SequentialTransition(fadeIn, fadeOut);


        return transition;
    }

    private void startSolving() {
        loadingIndicator.setVisible(true);

        // Concurrent process for the solver and GUI to prevent blocking (dah cocok sister blom :P)
        Task<Boolean> solveTask = new Task<>() {
            @Override
            protected Boolean call() {
                board.reset();
                PuzzleSolver solver = new PuzzleSolver(board, blocks);
                boolean solved = solver.solve();
                config.setCasesExamined(solver.getCasesExamined());
                config.setSearchTime(solver.getSearchTime());
                updateMessage(solver.getSearchTime() + "ms");
                updateTitle(solver.getCasesExamined() + ""); // nasty trick
                return solved;
            }
        };
        timerLabel.textProperty().bind(solveTask.messageProperty());
        caseLabel.textProperty().bind(solveTask.titleProperty());
        solveTask.setOnSucceeded(e -> {
            loadingIndicator.setVisible(false);
            timerLabel.textProperty().unbind();
            caseLabel.textProperty().unbind();
            SequentialTransition seq = new SequentialTransition();
            if (solveTask.getValue()) {
                // Succeeded
                Pane boardView = BoardViewBuilder.buildBoardView(board);
                boardPane.getChildren().clear();
                boardPane.getChildren().add(boardView);
                seq.getChildren().add(createFadeMessage("Solved", "#00FF00", finishSound));
            } else {
                seq.getChildren().add(createFadeMessage("Failed", "#FF0000", failedSound));
            }
            seq.play();
        });
        new Thread(solveTask).start();
    }

    private void saveBoardAsPNG() {
        try {
            // Temporary black the screen
            borderPane.setStyle("-fx-background-color: #000000;");
            // Capture the snapshot
            WritableImage snapshot = borderPane.snapshot(null, null);

            int originalWidth = (int) snapshot.getWidth();
            int originalHeight = (int) snapshot.getHeight();

            // Define cropping region (Crop from left)
            int cropX = 300;
            int newWidth = Math.max(0, originalWidth - cropX);

            if (newWidth <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cropping area is too large. Cannot save.");
                alert.showAndWait();
                return;
            }

            PixelReader reader = snapshot.getPixelReader();
            WritableImage croppedImage = new WritableImage(reader, cropX, 0, newWidth, originalHeight);

            // Revert background
            borderPane.setStyle("-fx-background-color: transparent;");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Board as PNG");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File file = fileChooser.showSaveDialog(boardPane.getScene().getWindow());

            if (file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, null), "png", file);
            }

        } catch (NoClassDefFoundError ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Saving PNG requires the javafx.swing module. Please add it to your dependencies.\n\n"
                            + ex.getMessage());
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save PNG: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void saveBoardAsTXT() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Board as TXT");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
            File file = fileChooser.showSaveDialog(boardPane.getScene().getWindow());
            if (file != null) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    char[][] grid = board.getGrid();
                    for (char[] row : grid) {
                        writer.write(new String(row));
                        writer.newLine();
                    }
                    writer.write(new String("Time Elapsed: " + config.getSearchTime() + "ms\nCase Examined: " + config.getCasesExamined() + "\n"));
                }
            }
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save TXT: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void applyBackButtonEffects(Button button) {
        button.setOnAction(e -> {
            if(clickSound != null) clickSound.play();
            if (pageBgm != null) pageBgm.stop();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tucil_1_stima/gui/main_menu.fxml"));
                Scene scene = new Scene(loader.load(), button.getScene().getWidth(), button.getScene().getHeight());
                MainApp mainApp = (MainApp) button.getScene().getWindow().getUserData();
                mainApp.backgroundPlayer.play();
                mainApp.switchScene(scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        button.setOnMouseEntered(e -> {
            if (hoverSound != null) {
                hoverSound.play();
            }
            TranslateTransition translate = new TranslateTransition(Duration.millis(100), button);
            translate.setToX(10); // move 10px right

            FadeTransition fade = new FadeTransition(Duration.millis(100), button);
            fade.setToValue(1.0); // fully opaque

            ParallelTransition pt = new ParallelTransition(translate, fade);
            pt.play();
        });

        // Mouse Exit: reset translate, scale, and opacity
        button.setOnMouseExited(e -> {
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), button);
            translate.setToX(0);

            FadeTransition fade = new FadeTransition(Duration.millis(200), button);
            fade.setToValue(0.85); // back to default

            ParallelTransition pt = new ParallelTransition(translate, fade);
            pt.play();
        });

        button.setOnAction(e -> {
            if (backSound != null) {
                backSound.play();
            }
            if(pageBgm != null) pageBgm.stop();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tucil_1_stima/gui/main_menu.fxml"));
                Scene scene = new Scene(loader.load(), backButton.getScene().getWidth(), backButton.getScene().getHeight());
                MainApp mainApp = (MainApp) backButton.getScene().getWindow().getUserData();
                mainApp.backgroundPlayer.play();
                mainApp.switchScene(scene);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void applyHoverEffects(Button button) {
        // Set the default opacity
        if (!button.isDisabled()) {
            button.setOpacity(0.85);
        }

        // Mouse Enter: shift left, scale up, and fade to full opacity
        button.setOnMouseEntered(e -> {
            if (hoverSound != null) {
                hoverSound.play();
            }

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.05);
            scale.setToY(1.05);

            FadeTransition fade = new FadeTransition(Duration.millis(100), button);
            fade.setToValue(1.0); // fully opaque

            ParallelTransition pt = new ParallelTransition(scale, fade);

            if (!button.isDisabled()) {
                pt.play();
            }
        });

        // Mouse Exit: reset translate, scale, and opacity
        button.setOnMouseExited(e -> {

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.0);
            scale.setToY(1.0);

            FadeTransition fade = new FadeTransition(Duration.millis(200), button);
            fade.setToValue(0.85); // back to default

            ParallelTransition pt = new ParallelTransition(scale, fade);
            if (!button.isDisabled()) {
                pt.play();
            }
        });

        button.setOnAction(e -> {
            if (clickSound != null) {
                clickSound.play();
            }
        });
    }
}
