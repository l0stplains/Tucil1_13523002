package tucil_1_stima.solver;

import tucil_1_stima.model.Block;
import tucil_1_stima.model.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PuzzleSolver} class is responsible for solving an IQ Puzzle Pro
 * where blocks must be placed on a board while following specific constraints.
 *
 * <p>The solver uses a recursive backtracking approach to find a valid
 * configuration where all blocks fit on the board without overlapping.</p>
 *
 * <p>
 *     If you are looking for speed just use C/C++. As said in Competitive Programming 4 Book
 *     by Steven "the G.O.A.T" Salim <i>"A biased opinion: Use C++ instead of Java (slower than C++)"</i>
 * </p>
 *
 * <p>
 *     But even so, i tried my best to not do a silly mistake that can lead to a poor performance.
 * </p>
 *
 */
public class PuzzleSolver {
    private final Board board;
    private final List<Block> blocks;
    private final List<List<Block>> blocksPermutation;
    private long searchTime;
    private long casesExamined;
    private boolean solved;
    private boolean impossible;

    /**
     * Constructs a {@code PuzzleSolver} with a given board and list of blocks.
     *
     * @param board  the board on which the puzzle will be solved
     * @param blocks the list of blocks to be placed on the board
     */
    public PuzzleSolver(Board board, List<Block> blocks) {
        this.board = board;
        this.blocks = blocks;
        this.solved = board.isFull();
        this.impossible = false;

        this.blocksPermutation = new ArrayList<>();
        for (Block block : blocks) {
            this.blocksPermutation.add(Block.generateUnique(block));
        }
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
        long startTime = System.currentTimeMillis();
        casesExamined = 0;
        solved = solve(0);
        searchTime = System.currentTimeMillis() - startTime;
        return solved;
    }

    /**
     * Recursively attempts to place blocks on the board in a valid configuration.
     * Uses backtracking to explore different placements.
     *
     * @param index the index of the block being placed
     * @return {@code true} if a valid solution is found, {@code false} otherwise
     */
    private boolean solve(int index) {
        // Early stopping if it's already impossible to solve (see the next `if` block to see the reason)
        if (impossible){
            return false;
        }

        // If all blocks have been placed
        if (index == blocks.size()) {
            // Impossible means that all blocks are not even enough to fill the board.
            impossible = !board.isFull();
//            if (impossible) {
//                board.printBoard(); // for aing nga debug
//            }
            // Returns true if and only if the board is filled completely and no remaining block (valid solution).
            return !impossible;
        }

        // Else
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                // Unique permutations from a block (mirror dan rotate)
                List<Block> uniqueBlocks = blocksPermutation.get(index);

                for (Block b : uniqueBlocks) {
                    casesExamined++;
                    if (board.placeBlock(b, i, j)) {
                        if (solve(index + 1)) {
                            solved = true;
                            return true;
                        } else {
                            board.removeBlock(b, i, j);
                        }
                    }
                }
            }
        }
        solved = false;
        return false;
    }
}
