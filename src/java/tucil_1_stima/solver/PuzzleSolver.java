package tucil_1_stima.solver;

import tucil_1_stima.model.Block;
import tucil_1_stima.model.Board;

import java.util.List;

/**
 * The {@code PuzzleSolver} class is responsible for solving a puzzle
 * where blocks must be placed on a board while following specific constraints.
 *
 * <p>The solver uses a recursive backtracking approach to find a valid
 * configuration where all blocks fit on the board without overlapping.</p>
 */
public class PuzzleSolver {
    private final Board board;
    private final List<Block> blocks;
    private long searchTime;
    private long casesExamined;

    /**
     * Constructs a {@code PuzzleSolver} with a given board and list of blocks.
     *
     * @param board  the board on which the puzzle will be solved
     * @param blocks the list of blocks to be placed on the board
     */
    public PuzzleSolver(Board board, List<Block> blocks) {
        this.board = board;
        this.blocks = blocks;
    }

    /**
     * Returns the board being used for the puzzle.
     *
     * @return the puzzle board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the time taken to search for a solution in milliseconds.
     *
     * @return the search time in milliseconds
     */
    public long getSearchTime() {
        return searchTime;
    }

    /**
     * Returns the total number of cases examined during the solving process.
     *
     * @return the number of cases examined
     */
    public long getCasesExamined() {
        return casesExamined;
    }

    /**
     * Attempts to solve the puzzle by placing all blocks on the board.
     * This method initializes the search time and calls the recursive solver.
     *
     * @return {@code true} if a solution is found, {@code false} otherwise
     */
    public boolean solve() {
        searchTime = System.currentTimeMillis();
        casesExamined = 0;
        return solve(0);
    }

    /**
     * Recursively attempts to place blocks on the board in a valid configuration.
     * Uses backtracking to explore different placements.
     *
     * @param index the index of the block being placed
     * @return {@code true} if a valid solution is found, {@code false} otherwise
     */
    private boolean solve(int index) {
        if (index == blocks.size()) {
            return board.isFull();
        }
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                List<Block> uniqueBlocks = Block.generateUnique(blocks.get(index));
                for (Block b : uniqueBlocks) {
                    casesExamined++;
                    if (board.placeBlock(b, i, j)) {
                        if (solve(index + 1)) {
                            searchTime = System.currentTimeMillis() - searchTime;
                            return true;
                        } else {
                            board.removeBlock(b, i, j);
                        }
                    }
                }
            }
        }
        return false;
    }
}
