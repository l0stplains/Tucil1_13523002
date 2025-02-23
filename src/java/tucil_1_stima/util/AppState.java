package tucil_1_stima.util;

import tucil_1_stima.model.Board;
import tucil_1_stima.model.Block;

import java.util.ArrayList;
import java.util.List;

public class AppState {
    private static Board board;
    private static List<Block> blocks;
    private static boolean initialized = false;
    private static List<PuzzleConfig> puzzleConfigs = new ArrayList<>();

    public static List<PuzzleConfig> getPuzzleConfigs() {
        return puzzleConfigs;
    }
    public static void addPuzzleConfig(PuzzleConfig config) {
        // Avoid duplicates by file name
        for(PuzzleConfig pc : puzzleConfigs) {
            if(pc.getFileName().equals(config.getFileName())) {
                // Overwrite or skip
                return;
            }
        }
        puzzleConfigs.add(config);
    }
    public static PuzzleConfig findPuzzleConfigByFileName(String fileName) {
        for(PuzzleConfig pc : puzzleConfigs) {
            if(pc.getFileName().equals(fileName)) return pc;
        }
        return null;
    }

    public static Board getBoard() {
        return board;
    }
    public static void setBoard(Board b) {
        board = b;
    }
    public static List<Block> getBlocks() {
        return blocks;
    }
    public static void setBlocks(List<Block> b) {
        blocks = b;
    }
    public static boolean isInitialized() {
        return initialized;
    }
    public static void setInitialized(boolean init) {
        initialized = init;
    }
}
