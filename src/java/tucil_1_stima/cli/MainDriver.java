package tucil_1_stima.cli;

import tucil_1_stima.solver.PuzzleSolver;
import tucil_1_stima.util.InputHandler;
import tucil_1_stima.model.Block;
import tucil_1_stima.model.Board;
import static tucil_1_stima.util.ColorUtil.*;

import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main driver class for handling user interaction and game loop.
 */
public class MainDriver {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        Board board = null;
        List<Block> blocks = null;

        // Load the board and blocks from file
        while (board == null) {
            try {
                System.out.print("Enter test case filename: ");
                String filename = scanner.nextLine().trim();
                Pair<Board, List<Block>> result = InputHandler.inputTestCaseFromFile(filename);
                board = result.getKey();
                blocks = result.getValue();
                System.out.println("Successfully loaded board and blocks from file.");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("\n===== Board State =====");
        board.printBoard();
        System.out.println("=======================\n");
        // Start game loop
        boolean running = true;
        while (running) {
            System.out.println("Menu:");
            System.out.println("1. Print board");
            System.out.println("2. Print blocks");
            System.out.println("3. Solve");
            System.out.println("4. Reset board");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": {
                    board.printBoard();
                    break;
                }
                case "2": {
                    for (Block block : blocks) {
                        System.out.print(getAnsiColorForLetter(block.getSymbol()));
                        block.print();
                        System.out.print(RESET);
                    }
                    break;
                }
                case "3": {
                    PuzzleSolver solver = new PuzzleSolver(board, blocks);
                    if (solver.solve()) {
                        System.out.println("Solved board: ");
                        solver.getBoard().printBoard();

                    } else {
                        System.out.println("Puzzle failed.");
                    }
                    System.out.println("Time for search: " + solver.getSearchTime() + "ms");
                    System.out.println("Number of cases examined: " + solver.getCasesExamined());
                    break;
                }
                case "4": {
                    board.reset();
                    break;
                }
                case "5": {
                    running = false;
                    break;
                }
                default: {
                    System.out.println("Invalid option.");
                }
            }
        }
        scanner.close();
    }
}
