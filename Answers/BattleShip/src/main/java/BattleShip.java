import java.util.Random;
import java.util.Scanner;
/**
 * The BattleShip class manages the gameplay of the Battleship game between two players.
 * It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
     * The main method that runs the game loop.
     * It initializes the grids for both players, places ships randomly, and manages turns.
     * The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }
    /**
     * Initializes the grid by filling it with water ('~').
     *
     * @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = '~';
            }
        }
    }
    /**
     * Places ships randomly on the given grid.
     * This method is called for both players to place their ships on their respective grids.
     *
     * @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        // Place a single ship of anysize
        placeShip(grid,1,'A');
        placeShip(grid,2,'B');
        placeShip(grid,3,'C');
        placeShip(grid,4,'D');
    }
    /**
     * Places a ship of a given size on the grid.
     *
     * @param grid The grid where the ship needs to be placed.
     * @param size The size of the ship.
     * @param symbol The symbol to represent the ship.
     */

    static void placeShip(char[][] grid, int size, char symbol) {
        Random random = new Random();
        boolean horizontal = random.nextBoolean();
        int row, col;

        if (horizontal) {
            row = random.nextInt(GRID_SIZE);
            col = random.nextInt(GRID_SIZE - size + 1);
        } else {
            row = random.nextInt(GRID_SIZE - size + 1);
            col = random.nextInt(GRID_SIZE);
        }

        if (canPlaceShip(grid, row, col, size, horizontal)) {
            if (horizontal) {
                for (int i = 0; i < size; i++) {
                    grid[row][col + i] = symbol;
                }
            } else {
                for (int i = 0; i < size; i++) {
                    grid[row + i][col] = symbol;
                }
            }
        } else {
            placeShip(grid, size, symbol);
        }
    }

    /**
     * Checks if a ship can be placed at the specified location on the grid.
     * This includes checking the size of the ship, its direction (horizontal or vertical),
     * and if there's enough space to place it.
     *
     * @param grid The grid where the ship is to be placed.
     * @param row The starting row for the ship.
     * @param col The starting column for the ship.
     * @param size The size of the ship.
     * @param horizontal The direction of the ship (horizontal or vertical).
     * @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != '~') {
                    return false;
                }
            }
        } else {
            if (row + size > GRID_SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != '~') {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Manages a player's turn, allowing them to attack the opponent's grid
     * and updates their tracking grid with hits or misses.
     *
     * @param opponentGrid The opponent's grid to attack.
     * @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        System.out.print("Enter your attack coordinates (e.g., A5): ");
        String input = scanner.next().toUpperCase();
        if (!isValidInput(input)) {
            System.out.println("Invalid input. Please try again.");
            playerTurn(opponentGrid, trackingGrid);
            return;
        }
        int row = input.charAt(0) - 'A';
        int col = Integer.parseInt(input.substring(1)) - 1;
        if (trackingGrid[row][col] == 'H' || trackingGrid[row][col] == 'M') {
            System.out.println("You've already attacked this cell. Please choose another one.");
            playerTurn(opponentGrid, trackingGrid);
            return;
        }
        if (opponentGrid[row][col] == '~') {
            System.out.println("Miss!");
            trackingGrid[row][col] = 'M';
        } else {
            System.out.println("Hit!");
            trackingGrid[row][col] = 'H';
            opponentGrid[row][col] = '~';
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    /**
     * Checks if the game is over by verifying if all ships are sunk.
     *
     * @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }
    /**
     * Checks if all ships have been destroyed on a given grid.
     *
     * @param grid The grid to check for destroyed ships.
     * @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] != '~' && grid[i][j] != 'M' && grid[i][j] != 'H') {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Validates if the user input is in the correct format (e.g., A5).
     *
     * @param input The input string to validate.
     * @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if (input.length() > 3) {
            return false;
        }
        char letter = input.charAt(0);
        if (letter < 'A' || letter >= 'A' + GRID_SIZE) {
            return false;
        }
        String number = input.substring(1);
        try {
            int num = Integer.parseInt(number);
            if (num < 1 || num > GRID_SIZE) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Prints the current state of the player's tracking grid.
     * This method displays the grid, showing hits, misses, and untried locations.
     *
     * @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        System.out.print("  ");
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();

        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
