package tucil_1_stima.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import tucil_1_stima.model.Board;

import static tucil_1_stima.util.ColorUtil.*;

public class BoardViewBuilder {
    public static StackPane buildBoardView(Board board) {
        final double BOARD_SIZE = 600;
        int rows = board.getRows();
        int cols = board.getCols();

        // Each cell is sized so that the entire board fits into 600x600
        double cellSize = BOARD_SIZE / Math.max(rows, cols);
        double radius = cellSize * 0.40;

        Pane root = new Pane();
        root.setPrefSize(BOARD_SIZE, BOARD_SIZE);

        char[][] grid = board.getGrid();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char symbol = grid[r][c];
                if (symbol == Board.BLOCK_BOUNDARY) {
                    continue;
                }
                // Coordinates for center of this cell
                double centerX = c * cellSize + cellSize / 2.0;
                double centerY = r * cellSize + cellSize / 2.0;

                Color color = Color.web(getHexColorForLetter(symbol));
                Color strokeColor = Color.web(getHexColorForLetter(symbol), 0.8);

                // If it's not empty, draw bridging lines
                if (symbol != Board.EMPTY) {
                    // Right neighbor
                    if (c + 1 < cols && grid[r][c + 1] == symbol) {
                        double x2 = (c + 1) * cellSize + cellSize / 2.0;
                        double y2 = centerY;
                        Line bridge = new Line(centerX, centerY, x2, y2);
                        bridge.setStroke(strokeColor);
                        bridge.setStrokeWidth(radius * 0.8);
                        bridge.setStrokeLineCap(StrokeLineCap.ROUND);
                        root.getChildren().add(bridge);
                    }
                    // Bottom neighbor
                    if (r + 1 < rows && grid[r + 1][c] == symbol) {
                        double x2 = centerX;
                        double y2 = (r + 1) * cellSize + cellSize / 2.0;
                        Line bridge = new Line(centerX, centerY, x2, y2);
                        bridge.setStroke(strokeColor);
                        bridge.setStrokeWidth(radius * 0.8);
                        bridge.setStrokeLineCap(StrokeLineCap.ROUND);
                        root.getChildren().add(bridge);
                    }
                }
                Circle peg = new Circle(centerX, centerY, radius);
                peg.setFill(color);
                peg.setStroke(strokeColor);
                peg.setStrokeWidth(1.5);
                root.getChildren().add(peg);

                if (!(symbol == Board.EMPTY)) {
                    // Add the symbol text on top of the circle
                    Label label = new Label(String.valueOf(symbol));
                    label.setStyle(" -fx-text-fill: white; -fx-font-size: " + (cellSize * 0.4) + "px;");
                    label.setLayoutX(centerX - (cellSize * 0.13));
                    label.setLayoutY(centerY - (cellSize * 0.30));
                    root.getChildren().add(label);
                }
            }
        }

        StackPane container = new StackPane(root);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(BOARD_SIZE, BOARD_SIZE);
        return container;
    }
}
