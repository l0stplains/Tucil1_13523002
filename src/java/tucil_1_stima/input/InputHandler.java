package tucil_1_stima.input;

import tucil_1_stima.model.Block;
import tucil_1_stima.model.Board;

import java.io.*;
import java.util.*;

import javafx.util.Pair;

public class InputHandler {

    /**
     * Parse and validate inputs from the test case file.
     *
     * Expected Format:
     *   N M P
     *   S
     *   [block definitions...]
     *
     * In this format, blocks do not have a separate header line.
     * Instead, the very first character that appears in a block's definition is used as its symbol.
     * Contiguous lines whose first non-space character is the same are grouped into one block.
     *
     * For example:
     * 5 5 2
     * DEFAULT
     * A A
     * AAA
     *  BBB
     *  BB
     *
     * In the example above, the first block is defined by the lines beginning with 'A'
     * and the second block is defined by the lines beginning with 'B'.
     *
     * @param filename the test case file name
     * @return a pair of the board and the list of blocks detected
     * @throws IOException if a file I/O error occurs or the input is malformed
     */
    public static Pair<Board, List<Block>> inputTestCaseFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Parse the first line: board dimensions and number of blocks.
            String[] variables = reader.readLine().split(" ");
            if (variables.length != 3) {
                throw new IllegalArgumentException("Wrong number of variables on the first line of file: " + filename);
            }
            int N, M, P;
            try {
                N = Integer.parseInt(variables[0]);
                M = Integer.parseInt(variables[1]);
                P = Integer.parseInt(variables[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Variable not parseable on the first line of file: " + filename);
            }

            // Parse the board type (second line) and initialize the board.
            String boardType = reader.readLine().trim();
            Board board;
            if (boardType.equals("DEFAULT")) {
                board = new Board(N, M);
            } else if (boardType.equals("CUSTOM")) {
                board = new Board(N, M); // Implement custom board logic as needed
            } else if (boardType.equals("PYRAMID")) {
                board = new Board(N, M); // Implement pyramid board logic as needed
            } else {
                throw new IllegalArgumentException("Unknown board type: " + boardType);
            }

            // Read all remaining non-empty lines.
            List<String> allLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { // just in case bro
                    allLines.add(line);
                }
            }

            // Now group lines into blocks by detecting a change in the starting symbol.
            List<Block> blocks = new ArrayList<>();
            List<String> currentBlockLines = new ArrayList<>();
            Character currentSymbol = null;

            for (int i = 0; i < allLines.size(); i++) {
                String currentLine = allLines.get(i).trim();
                if (currentLine.isEmpty()) continue; // Skip any accidental blank lines

                // The first non-space character is used as the block's symbol.
                char lineSymbol = currentLine.charAt(0);
                if (currentSymbol == null) {
                    // First block encountered.
                    currentSymbol = lineSymbol;
                    currentBlockLines.add(currentLine);
                } else if (lineSymbol == currentSymbol) {
                    // Same block: add the line.
                    currentBlockLines.add(currentLine);
                } else {
                    // A different symbol means the previous block's definition is complete.
                    blocks.add(createBlockFromLines(currentSymbol, currentBlockLines));
                    // Start a new block.
                    currentSymbol = lineSymbol;
                    currentBlockLines = new ArrayList<>();
                    currentBlockLines.add(currentLine);
                }
            }
            // Finalize the last block if there is one.
            if (currentSymbol != null && !currentBlockLines.isEmpty()) {
                blocks.add(createBlockFromLines(currentSymbol, currentBlockLines));
            }

            // Validate that the number of blocks read matches P, just in case.
            if (blocks.size() != P) {
                throw new IllegalArgumentException("Expected " + P + " blocks, but found " + blocks.size());
            }

            return new Pair<>(board, blocks);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filename, e);
        }
    }

    /**
     * Helper method to create a Block from its shape lines.
     *
     * @param symbol      the block's symbol
     * @param shapeLines  the lines representing the block's shape
     * @return a new Block instance with the boolean grid built from the shape lines
     */
    private static Block createBlockFromLines(char symbol, List<String> shapeLines) {
        int rows = shapeLines.size();
        // Determine the maximum row length
        int cols = shapeLines.stream().mapToInt(String::length).max().orElse(0); // lets get rusty
        boolean[][] grid = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            String row = shapeLines.get(i);
            for (int j = 0; j < cols; j++) {
                char c = (j < row.length()) ? row.charAt(j) : ' ';
                grid[i][j] = (c == symbol); // awokwok ah
            }
        }
        return new Block(symbol, grid);
    }
}
