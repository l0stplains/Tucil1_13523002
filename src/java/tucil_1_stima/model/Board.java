package tucil_1_stima.model;

import static tucil_1_stima.util.ColorUtil.*;

public class Board {
    private final int rows;
    private final int cols;
    private final char[][] grid;

    // Constants for empty cells and boundaries.
    public static final char EMPTY = 'X';
    public static final char BLOCK_BOUNDARY = '.';

    /**
     * Creates a new board with the given dimensions.
     * All cells are initially empty.
     *
     * @param rows number of rows in the board
     * @param cols number of columns in the board
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new char[rows][cols];
        // Initialize all cells as empty.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = EMPTY;
            }
        }
    }

    /**
     * Returns the number of rows on the board.
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of rows on the board.
     *
     * @return the number of rows
     */
    public int getCols() {
        return cols;
    }

    /**
     * Checks if the given block can be placed at position (row, col).
     * It verifies that every cell in the block that is "filled" (true)
     * maps to a valid board coordinate that is currently empty.
     *
     * @param block the block to be placed
     * @param row   the row on the board for the block's top-left corner
     * @param col   the column on the board for the block's top-left corner
     * @return true if the block can be placed, false otherwise
     */
    public boolean canPlace(Block block, int row, int col) {
        boolean[][] blockGrid = block.getGrid();
        int blockRows = blockGrid.length;
        int blockCols = blockGrid[0].length;

        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockCols; j++) {
                if (blockGrid[i][j]) {
                    int boardRow = row + i;
                    int boardCol = col + j;
                    // Check boundaries.
                    if (boardRow < 0 || boardRow >= rows || boardCol < 0 || boardCol >= cols) {
                        return false;
                    }
                    // Check that the target cell is empty.
                    if (grid[boardRow][boardCol] != EMPTY) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Places the block on the board at the given position (row, col) if possible.
     * This method assumes that {@link #canPlace(Block, int, int)} has been checked.
     *
     * @param block the block to place
     * @param row   the row on the board for the block's top-left corner
     * @param col   the column on the board for the block's top-left corner
     * @return true if the block was successfully placed, false if placement is invalid
     */
    public boolean placeBlock(Block block, int row, int col) {
        if (!canPlace(block, row, col)) {
            return false;
        }
        boolean[][] blockGrid = block.getGrid();
        int blockRows = blockGrid.length;
        int blockCols = blockGrid[0].length;
        char symbol = block.getSymbol();

        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockCols; j++) {
                if (blockGrid[i][j]) {
                    grid[row + i][col + j] = symbol;
                }
            }
        }
        return true;
    }

    /**
     * Removes the block from the board from the specified position (row, col).
     * The method checks that the block's symbol is present at the expected locations before removal.
     *
     * @param block the block to remove
     * @param row   the row where the block was placed
     * @param col   the column where the block was placed
     * @return true if the block was successfully removed, false if the board cells did not match the block's symbol
     */
    public boolean removeBlock(Block block, int row, int col) {
        boolean[][] blockGrid = block.getGrid();
        int blockRows = blockGrid.length;
        int blockCols = blockGrid[0].length;
        char symbol = block.getSymbol();

        // Verify that the block is present.
        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockCols; j++) {
                if (blockGrid[i][j]) {
                    if (grid[row + i][col + j] != symbol) {
                        return false;
                    }
                }
            }
        }
        // Remove the block by setting the affected cells to EMPTY.
        for (int i = 0; i < blockRows; i++) {
            for (int j = 0; j < blockCols; j++) {
                if (blockGrid[i][j]) {
                    grid[row + i][col + j] = EMPTY;
                }
            }
        }
        return true;
    }

    /**
     * Prints the board to the console.
     * Each cell is separated by a space.
     */
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = grid[i][j];
                String color = Character.isUpperCase(c) ? getAnsiColorForLetter(c) : "";
                System.out.print(color + c + RESET);
            }
            System.out.println();
        }
    }

    /**
     * Checks if the board is completely filled.
     * This method considers only cells that are available for filling (i.e. those that are EMPTY).
     *
     * @return true if no EMPTY cell exists in the board, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Applies a custom mask to the board.
     * The mask is provided as a 2D boolean array with the same dimensions as the board.
     * If mask[r][c] is true, that cell is set to {@link #BLOCK_BOUNDARY} to indicate it cannot be filled.
     *
     * @param mask a boolean 2D array representing boundary (non-fillable) cells
     * @throws IllegalArgumentException if the mask dimensions do not match the board dimensions
     */
    public void setMask(boolean[][] mask) {
        if (mask.length != rows || mask[0].length != cols) {
            throw new IllegalArgumentException("Mask dimensions must match board dimensions.");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mask[i][j]) {
                    grid[i][j] = BLOCK_BOUNDARY;
                } else {
                    grid[i][j] = EMPTY;
                }
            }
        }
    }
}
