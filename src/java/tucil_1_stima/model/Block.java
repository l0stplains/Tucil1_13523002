package tucil_1_stima.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class Block {
    private boolean[][] grid;
    private char symbol;

    /**
     * Constructor: deep-copies the given rectangular boolean grid.
     *
     * @param symbol the character that represents this block
     * @param grid   a 2D boolean array where true indicates a filled cell
     */
    public Block(char symbol, boolean[][] grid) {
        this.symbol = symbol;
        int rows = grid.length;
        this.grid = new boolean[rows][];
        for (int i = 0; i < rows; i++) {
            this.grid[i] = grid[i].clone();
        }
    }

    /**
     * Returns a deep copy of the block's grid.
     *
     * @return a new 2D boolean array representing the block's grid
     */
    public boolean[][] getGrid() {
        int rows = grid.length;
        boolean[][] copy = new boolean[rows][];
        for (int i = 0; i < rows; i++) {
            copy[i] = grid[i].clone();
        }
        return copy;
    }

    /**
     * Sets the block's grid with a deep copy of the provided grid.
     *
     * @param newGrid the new grid to set for this block
     */
    public void setGrid(boolean[][] newGrid) {
        int rows = newGrid.length;
        this.grid = new boolean[rows][];
        for (int i = 0; i < rows; i++) {
            this.grid[i] = newGrid[i].clone();
        }
    }

    /**
     * Returns the symbol representing this block.
     *
     * @return the block's symbol
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol representing this block.
     *
     * @param symbol the new symbol to set
     */
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Rotates the block 90° clockwise.
     * For a grid with dimensions r x c, the rotated grid has dimensions c x r.
     *
     * @return new rotated Block
     */
    public Block rotate() {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] rotated = new boolean[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = grid[i][j];
            }
        }
        return new Block(symbol, rotated);
    }

    /**
     * Mirrors the block horizontally (flips left to right).
     *
     * @return new mirrored block
     */
    public Block mirror() {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] mirrored = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            mirrored[i] = grid[i].clone();
            for (int j = 0; j < cols / 2; j++) {
                boolean temp = mirrored[i][j];
                mirrored[i][j] = mirrored[i][cols - 1 - j];
                mirrored[i][cols - 1 - j] = temp;
            }
        }
        return new Block(symbol, mirrored);
    }

    /**
     * Prints the block using its symbol for filled cells and a dot for empty cells.
     */
    public void print() {
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                System.out.print((cell ? symbol : '.') + " ");
            }
            System.out.println();
        }
    }

    /**
     * Generates all unique transformations from the original block by applying
     * four rotations (0°, 90°, 180°, 270°) and a horizontal mirror for each.
     *
     * @param original The original Block.
     * @return A list of unique Blocks.
     */
    public static List<Block> generateUnique(Block original) {
        Set<Block> uniqueSet = new HashSet<>();
        Block current = original;
        for (int i = 0; i < 4; i++) {
            uniqueSet.add(current);
            uniqueSet.add(current.mirror());
            current = current.rotate();
        }
        return new ArrayList<>(uniqueSet);
    }

    // Overrides equals to compare both grid content and symbol.
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Block other = (Block) obj;
        return this.symbol == other.symbol && Arrays.deepEquals(this.grid, other.grid);
    }

    // Overrides hashCode to be consistent with equals.
    @Override
    public int hashCode() {
        return 31 * Arrays.deepHashCode(grid) + Character.hashCode(symbol);
    }

    // Example usage
    public static void main(String[] args) {
        // Define a 3x2 block represented by a boolean grid.
        boolean[][] grid = {
                {true, false, true},
                {true, true, false}
        };

        Block block = new Block('A', grid);
        System.out.println("Original Block:");
        block.print();
        System.out.println();

        System.out.println("Rotated Block:");
        Block rotated = block.rotate();
        rotated.print();
        System.out.println();

        System.out.println("Mirrored Block:");
        Block mirrored = block.mirror();
        mirrored.print();
        System.out.println();

        System.out.println("Unique Transformations:");
        List<Block> uniqueBlocks = Block.generateUnique(block);
        int count = 1;
        for (Block b : uniqueBlocks) {
            System.out.println("Unique Block " + count++ + ":");
            b.print();
            System.out.println();
        }
    }
}
