package cpsc2150.extendedCheckers.views;

import cpsc2150.extendedCheckers.models.*;
import cpsc2150.extendedCheckers.util.DirectionEnum;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class is the front end of the Checkers game. It allows players to take turns moving pieces until one of the players
 * has won the game. The game board is represented by an instance of the ICheckerBoard interface. Player one is represented
 * with an 'x' and player two is represented with an 'o'.
 */
public class CheckersFE {
    private static char PLAYER_ONE = 'x';
    private static char PLAYER_TWO = 'o';
    private static ICheckerBoard checkerBoard;
    private static char currentPlayer;
    private static Scanner scanner;

    /**
     * Returns the character representing Player One.
     * 
     * @pre None.
     * 
     * @return The character representing Player One.
     * 
     * @post The returned character represents Player One.
     */
    public static char getPlayerOne() {
        return PLAYER_ONE;
    }

    /**
     * Returns the character representing Player Two.
     * 
     * @pre None.
     * 
     * @return The character representing Player Two.
     * 
     * @post The returned character represents Player Two.
     */
    public static char getPlayerTwo() {
        return PLAYER_TWO;
    }

    public static void main(String[] args) {

        System.out.println("Welcome to Checkers!");

        scanner = new Scanner(System.in);

        System.out.println("Player 1, enter your piece: ");
        PLAYER_ONE = scanner.nextLine().trim().charAt(0);
        
        do {
            System.out.println("Player 2, enter your piece: ");
            PLAYER_TWO = scanner.nextLine().trim().charAt(0);
            if (PLAYER_TWO == PLAYER_ONE) {
                System.out.println("Piece already taken by Player One. Please select a different piece.");
            }
        } while (PLAYER_TWO == PLAYER_ONE);

        System.out.println("Do you want a fast game (F/f) or a memory efficient game (M/m)?");
        String userChoice = scanner.nextLine().toUpperCase();
        while (!userChoice.equals("F") && !userChoice.equals("M")) {
         System.out.println("Please enter F or M");
         userChoice = scanner.nextLine().toUpperCase();
        }

        int boardSize = 0;
        while (boardSize < ICheckerBoard.BOARD_MIN_SIZE || boardSize > ICheckerBoard.BOARD_MAX_SIZE || boardSize % 2 != 0) {
            System.out.println("How big should the board be? It can be 8x8, 10x10, 12x12, 14x14, or 16x16. Enter one number: ");
            if (scanner.hasNextInt()) {
                boardSize = scanner.nextInt();
                scanner.nextLine();
                if (boardSize >= ICheckerBoard.BOARD_MIN_SIZE && boardSize <= ICheckerBoard.BOARD_MAX_SIZE && boardSize % 2 == 0) {
                    break;
                } else {
                    System.out.println("Invalid board size.");
                }
            } else {
                System.out.println("Please enter a valid integer.");
                scanner.next();
            }
        }

        if (userChoice.equals("F")) {
            checkerBoard = new CheckerBoard(boardSize);
        } else {
            checkerBoard = new CheckerBoardMem(boardSize);
        }
        
       
        currentPlayer = PLAYER_ONE;

        System.out.println(checkerBoard.toString());

        while (!gameOver()) {
            promptAndMakeMove();
        }
        displayResult();
    }

    /**
     * Prompts the user to select a piece and a direction to move the piece. Ensures the selected piece is the
     * current player's piece and the selected direction is valid. The selected piece is then moved. Otherwise,
     * prompts the user to try again.
     * 
     * @pre None.
     * 
     * @return True if player selected a valid piece and direction, false otherwise.
     * 
     * @post promptAndMakeMove = [True if player selected a valid piece and direction, if true checkerBoard 
     * is updated with the new position of the piece]
     */
    private static void promptAndMakeMove() {
        boolean validPieceSelected = false;
        BoardPosition startPos = null;

        while (!validPieceSelected) {
            System.out.println("player " + currentPlayer + ", which piece do you wish to move? Enter the row followed by a space followed by the column.");
            String[] position = scanner.nextLine().split(" ");

            try {
                int row = Integer.parseInt(position[0]);
                int col = Integer.parseInt(position[1]);
                startPos = new BoardPosition(row, col);

                char piece = checkerBoard.whatsAtPos(startPos);

                if (Character.toLowerCase(piece) == Character.toLowerCase(currentPlayer)) {
                    validPieceSelected = true;
                } else {
                    System.out.println("player " + currentPlayer + ", that isn't your piece. Pick one of your pieces.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please enter the row and column as numbers separated by a space.");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid input format. Please enter the row and column separated by a space.");
            }
        }

        HashMap<Character, ArrayList<DirectionEnum>> viableDirections = checkerBoard.getViableDirections();
        ArrayList<DirectionEnum> validDirections;

        char piece = checkerBoard.whatsAtPos(startPos);
        if (Character.isUpperCase(piece)) {
            validDirections = viableDirections.get(Character.toUpperCase(currentPlayer));
        } else {
            validDirections = viableDirections.get(currentPlayer);
        }

        System.out.println("In which direction do you wish to move the piece?");
        System.out.println("Enter one of these options: ");
        for (DirectionEnum dir : validDirections) {
            System.out.println(dir);
        }

        String direction = scanner.nextLine().toUpperCase();
        DirectionEnum dir = null;

        // Validate direction input
        try {
            dir = DirectionEnum.valueOf(direction);
            if (!validDirections.contains(dir)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid direction. Please enter a valid direction.");
            return;
        }

        if (makeMove(startPos, dir)) {
            System.out.println(checkerBoard.toString());
            currentPlayer = (currentPlayer == PLAYER_ONE) ? PLAYER_TWO : PLAYER_ONE;
        } else {
            System.out.println("Invalid move. Please try again.");
        }
    }

    /**
     * Moves a piece in a given direction on the checkerboard. Ensures the piece is the current player's piece
     * If the move is successful, updates the checkerboard.
     * 
     * @pre the piece to move is the current player's piece.
     * 
     * @param startPos is the starting position of the piece to move.
     * @param dir is the direction to move the piece in.
     * 
     * @return true if move is successful, false otherwise.
     * 
     * @post makeMove = [True if move is successful, if true checkerBoard is updated]
     */
    private static boolean makeMove(BoardPosition startPos, DirectionEnum dir) {
        char piece = checkerBoard.whatsAtPos(startPos);
        if (Character.toLowerCase(piece) != Character.toLowerCase(currentPlayer)) {
            return false;
        }

        BoardPosition newPos = checkerBoard.movePiece(startPos, dir);
        if (newPos.equals(startPos)) {
            newPos = checkerBoard.jumpPiece(startPos, dir);
        }

        if (!newPos.equals(startPos)) {
            checkerBoard.crownPiece(newPos);
            return true;
        }

        return false;
    }

    /**
     * Checks to see if the game is over by determining if either player has won.
     * 
     * @pre None.
     * 
     * @return True if either player has won, false otherwise.
     * 
     * @post gameOver = [True if either player has won]
     */
    private static boolean gameOver() {
        return checkerBoard.checkPlayerWin(PLAYER_ONE) || checkerBoard.checkPlayerWin(PLAYER_TWO);
    }

    /**
     * Displays the result of the tame and prompts user to play again if desired.
     * Resets the game if user chooses to play again.
     * 
     * @pre None.
     * 
     * @return None.
     * 
     * @post displayResult = [Displays the result of the game, restarts game if user chooses, terminates program otherwise]
     */
    private static void displayResult() {
        if (checkerBoard.checkPlayerWin(PLAYER_ONE)) {
            System.out.println("Player x has won!");
        } else {
            System.out.println("Player o has won!");
        }
        System.out.print("Would you like to play again? Enter Y or N: ");
        String choice = scanner.nextLine().toUpperCase();
        if (choice.equals("Y")) {
            //checkerBoard = new CheckerBoard();
            currentPlayer = PLAYER_ONE;
            main(null);
        } else {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
    }
}
