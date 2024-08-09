package cpsc2150.extendedCheckers.tests;
import cpsc2150.extendedCheckers.models.CheckerBoard;
import cpsc2150.extendedCheckers.models.ICheckerBoard;
import cpsc2150.extendedCheckers.models.BoardPosition;
import cpsc2150.extendedCheckers.util.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestCheckerBoard {

    private ICheckerBoard makeBoard(int dimension) {
        return new CheckerBoard(dimension);
    }

    private String arrayToString(ICheckerBoard board, char[][] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("|  ");
        for (int col = 0; col < array[0].length; col++) {
            if (col < 10) {
                sb.append("| ").append(col);
            }
            else {
                sb.append("|").append(col);
            }
        }
        sb.append("|\n");

        for (int row = 0; row < array[1].length; row++) {
            if (row < 10) {
                sb.append("|" + row + " ");
            }
            else {
                sb.append("|" + row);
            }
            for (int col = 0; col < array[0].length; col++) {
                BoardPosition pos = new BoardPosition(row, col);
                sb.append("|");
                sb.append(board.whatsAtPos(pos)).append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    @Test
    public void testCheckerBoardMinimumSize() {
        int dimension = 8;
        ICheckerBoard board = makeBoard(dimension);
        char[][] expected = {
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', 'x', '*', 'x', '*', 'x'},
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', ' ', '*', ' ', '*', ' ', '*', ' '},
                {' ', '*', ' ', '*', ' ', '*', ' ', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'},
                {'o', '*', 'o', '*', 'o', '*', 'o', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'}
        };
        String expectedString = arrayToString(board, expected);
        assertEquals(expectedString, board.toString());
    }

    @Test
    public void testCheckerBoardMaximumSize_16x16() {
        int dimension = 16;
        ICheckerBoard board = makeBoard(dimension);
        // 16x16 Game board here, the state is unchanged.
        assertEquals(16, board.getRowNum());
        assertEquals(16, board.getColNum());
    }

    @Test
    public void testCheckerBoardInvalidSize() {
        int invalidDimension = 6;
        try {
            makeBoard(invalidDimension);
            fail("Expected IllegalArgumentException to be thrown for invalid board size.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid board size.", e.getMessage());
        }
    }

    @Test
    public void testWhatsAtPos_MaxRow_MaxCol(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition pos = new BoardPosition(7, 7);
        assertEquals ('o', board.whatsAtPos(pos));
    }

    @Test
    public void testWhatsAtPos_MaxRow_MinCol(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition pos = new BoardPosition(7, 0);
        assertEquals ('*', board.whatsAtPos(pos));
    }

    @Test
    public void testWhatsAtPos_MinRow_MaxCol(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition pos = new BoardPosition(0, 7);
        assertEquals ('*', board.whatsAtPos(pos));
    }

    @Test
    public void testWhatsAtPos_MinRow_MinCol() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition pos = new BoardPosition (0, 0);
        assertEquals ('x', board.whatsAtPos(pos));
    }

    @Test
    public void whatsAtPos_MiddleOfTheBoard(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition pos = new BoardPosition(1, 6);
        assertEquals ('*', board.whatsAtPos(pos));
    }

    @Test
    public void testPlacePieceInMiddle(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition test = new BoardPosition(1, 6);

        char[][] expectedBoard = {
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', 'x', '*', 'x', '*', 'x'},
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', ' ', '*', ' ', '*', ' ', '*', ' '},
                {'x', '*', ' ', '*', ' ', '*', ' ', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'},
                {'o', '*', 'o', '*', 'o', '*', 'o', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'}
        };
        board.placePiece(test, '*');
        char expected = '*';
        char observed = board.whatsAtPos(test);
        assertEquals(expected, observed);
        String expectedString = arrayToString(board, expectedBoard);
        assertEquals(expectedString, board.toString());
    }

    @Test
    public void testPlacePiece_OnPreviouslyOccupiedSpace(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(2,2);
        BoardPosition endingPos = new BoardPosition(3,1);

        board.placePiece(startingPos, 'x');
        board.movePiece(startingPos, DirectionEnum.SW);

        char expectedBeginning = ' ';
        char expectedEnding = 'x';
        char observedBeginning = board.whatsAtPos(startingPos);
        char observedEnding = board.whatsAtPos(endingPos);

        assertEquals(expectedBeginning, observedBeginning);
        assertEquals(expectedEnding, observedEnding);

        board.placePiece(startingPos, 'x');

        char expectedNewBeginning = 'x';
        char observedNewBeginning = board.whatsAtPos(startingPos);

        assertEquals(expectedNewBeginning, observedNewBeginning);

        char[][] expectedBoard = {
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', 'x', '*', 'x', '*', 'x'},
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', ' ', '*', ' ', '*', ' '},
                {' ', '*', ' ', '*', ' ', '*', ' ', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'},
                {'o', '*', 'o', '*', 'o', '*', 'o', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'}
        };
        String expectedString = arrayToString(board, expectedBoard);
        assertEquals(expectedString, board.toString());
    }

    @Test
    public void testOccupiedSpot(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition test = new BoardPosition(2, 3);
        char[][] expectedBoard = {
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', 'x', '*', 'x', '*', 'x'},
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', ' ', '*', ' ', '*', ' '},
                {' ', '*', ' ', '*', ' ', '*', ' ', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'},
                {'o', '*', 'o', '*', 'o', '*', 'o', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'}
        };
        char expected = '*';
        char observed = board.whatsAtPos(test);
        assertEquals(expected, observed);
        String expectedString = arrayToString(board, expectedBoard);
        assertEquals(expectedString, board.toString());
    }

    @Test
    public void testPlacePieceMinRow() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition minRowPosition = new BoardPosition(0, 0);
        board.placePiece(minRowPosition, 'o');
        assertEquals(board.whatsAtPos(minRowPosition), 'o');
    }

    @Test
    public void testPlacePieceMinColumn() {
        ICheckerBoard board = makeBoard(8);
        BoardPosition minRowPosition = new BoardPosition(2, 0);
        board.placePiece(minRowPosition, 'x');
        assertEquals(board.whatsAtPos(minRowPosition), 'x');
    }

    @Test
    public void testPlacePieceOnBlackSquare(){
        ICheckerBoard board = makeBoard(8);
        char[][] expectedBoard = {
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', 'x', '*', 'x', '*', 'x'},
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', ' ', '*', ' ', '*', ' '},
                {' ', '*', ' ', '*', ' ', '*', ' ', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'},
                {'o', '*', 'o', '*', 'o', '*', 'o', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'}
        };
        BoardPosition trial = new BoardPosition(2,1);
        char expected = '*';
        char observed = board.whatsAtPos(trial);
        assertEquals(expected,observed);
        String expectedString = arrayToString(board, expectedBoard);
        String observedBoard = board.toString();
        assertEquals(expectedString, observedBoard);
    }

    @Test
    public void testGetInitialPieceCounts(){
        ICheckerBoard board = makeBoard(8);
        HashMap<Character, Integer> expected = new HashMap<>();
        expected.put('x', 24);
        expected.put('o', 24);
        HashMap<Character, Integer> observed = board.getPieceCounts();
        assertEquals(expected, observed);

        char[][] expectedBoard = {
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', 'x', '*', 'x', '*', 'x', '*', 'x'},
                {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
                {'*', ' ', '*', ' ', '*', ' ', '*', ' '},
                {' ', '*', ' ', '*', ' ', '*', ' ', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'},
                {'o', '*', 'o', '*', 'o', '*', 'o', '*'},
                {'*', 'o', '*', 'o', '*', 'o', '*', 'o'}
        };
        String expectedString = arrayToString(board, expectedBoard);
        assertEquals(expectedString, board.toString());
    }

    @Test
    public void testGetInitialViableDirections(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition test = new BoardPosition(5, 1);
        HashMap<Character, ArrayList<DirectionEnum>> expected = new HashMap<>();

        ArrayList<DirectionEnum> allDirections = new ArrayList<>();
        allDirections.add(DirectionEnum.SE);
        allDirections.add(DirectionEnum.SW);
        allDirections.add(DirectionEnum.NE);
        allDirections.add(DirectionEnum.NW);

        ArrayList<DirectionEnum> playerOneDirections = new ArrayList<>();
        playerOneDirections.add(DirectionEnum.SE);
        playerOneDirections.add(DirectionEnum.SW);

        ArrayList<DirectionEnum> playerOneCrownDirections = new ArrayList<>();
        playerOneCrownDirections.add(DirectionEnum.SE);
        playerOneCrownDirections.add(DirectionEnum.NW);
        playerOneCrownDirections.add(DirectionEnum.SW);
        playerOneCrownDirections.add(DirectionEnum.NE);

        ArrayList<DirectionEnum> playerTwoDirections = new ArrayList<>();
        playerTwoDirections.add(DirectionEnum.NE);
        playerTwoDirections.add(DirectionEnum.NW);

        ArrayList<DirectionEnum> playerTwoCrownDirections = new ArrayList<>();
        playerTwoCrownDirections.add(DirectionEnum.NE);
        playerTwoCrownDirections.add(DirectionEnum.SW);
        playerTwoCrownDirections.add(DirectionEnum.NW);
        playerTwoCrownDirections.add(DirectionEnum.SE);

        expected.put(' ', allDirections);
        expected.put('x', playerOneDirections);
        expected.put('X', playerOneCrownDirections);
        expected.put('o', playerTwoDirections);
        expected.put('O', playerTwoCrownDirections);

        HashMap<Character, ArrayList<DirectionEnum>> observed = board.getViableDirections();
        assertEquals(expected, observed);
    }

    @Test
    public void testInitialDirectionAddition(){
        ICheckerBoard board = makeBoard(8);
        HashMap<Character, ArrayList<DirectionEnum>> addPlayer = new HashMap<>();

        char newPlayer = 'z';
        board.addViableDirections(newPlayer, DirectionEnum.NE);
        board.addViableDirections(newPlayer, DirectionEnum.NW);

        ArrayList<DirectionEnum> expected = new ArrayList<>();
        expected.add(DirectionEnum.NE);
        expected.add(DirectionEnum.NW);

        ArrayList<DirectionEnum> observed = board.getViableDirections().get(newPlayer);

        assertEquals(expected, observed);
    }

    @Test
    public void testGetRowNumForBoard(){
        ICheckerBoard board = makeBoard(8);
        int expectedRowNum = 8;
        int actualRowNum = board.getRowNum();
        assertEquals(expectedRowNum, actualRowNum);
    }

    @Test
    public void testGetColNumForBoard(){
        ICheckerBoard board = makeBoard(8);
        int expectedColNum = 8;
        int actualColNum = board.getColNum();
        assertEquals(expectedColNum, actualColNum);        
    }

    @Test
    public void testCheckPlayerOneWins(){
        ICheckerBoard board = makeBoard(8);

        //clearing the board ensure no 'o' pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.placePiece(new BoardPosition(row, col), ICheckerBoard.EMPTY_POS);
            }
        }

        //placing 'x' pieces to simulate win condition
        board.placePiece(new BoardPosition(1, 7), 'x');
        board.placePiece(new BoardPosition(3, 3), 'x');
        board.placePiece(new BoardPosition(4, 4), 'x');

        // Check if 'x' is recognized as the winner
        boolean observedResult = board.checkPlayerWin('x');
        assertTrue(observedResult);
    }

    @Test
    public void testCheckNoOneWins(){
        ICheckerBoard board = makeBoard(8);

        //clearing board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.placePiece(new BoardPosition(row, col), ICheckerBoard.EMPTY_POS);
            }
        }

        board.placePiece(new BoardPosition(1, 7), 'x');
        board.placePiece(new BoardPosition(3, 3), 'x');
        board.placePiece(new BoardPosition(4 ,4), 'x');
        board.placePiece(new BoardPosition(7, 7), 'o');

        boolean observedResult = board.checkPlayerWin('x');
        assertFalse(observedResult);

    }

    @Test
    public void testCrownPieceLargeBoard(){
        ICheckerBoard board = makeBoard(16);
        BoardPosition pos = new BoardPosition(15, 14);
        char player = 'x';

        // Set up the board state
        board.placePiece(pos, player);

        // Crown the piece at position (15, 14)
        board.crownPiece(pos);

        // Check that the piece at position (15, 14) is crowned
        assertEquals('X', board.whatsAtPos(pos));

        // Check state of the board is correct
        char[][] expected = new char[16][16];
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                if ((row + col) % 2 == 0) {
                    expected[row][col] = '*';
                } else {
                    expected[row][col] = ' ';
                }
            }
        }
        expected[15][14] = 'X';
        String expectedString = arrayToString(board, expected);
        assertEquals(expectedString, board.toString());
    }

    @Test
    public void testCrownPlayerTwo(){
        
        ICheckerBoard board = makeBoard(8);
        BoardPosition pos = new BoardPosition(1, 1);
        char player = 'o';

        // Set up the state of the board
        board.placePiece(pos, player);

        // Attempt to Crown the piece at (1, 1)
        board.crownPiece(pos);

        // Make sure the piece isn't crowned
        assertEquals('o', board.whatsAtPos(pos));

        // Check that the board state is correct
        char[][] expected = {
            {' ', '*', 'x', '*', 'x', '*', 'x', '*'},
            {'*', 'o', '*', 'x', '*', 'x', '*', 'x'},
            {'x', '*', ' ', '*', 'x', '*', 'x', '*'},
            {'*', ' ', '*', ' ', '*', ' ', '*', ' '},
            {' ', '*', ' ', '*', ' ', '*', ' ', '*'},
            {'*', ' ', '*', 'o', '*', 'o', '*', 'o'},
            {'o', '*', 'o', '*', 'o', '*', 'o', '*'},
            {'*', 'o', '*', 'o', '*', 'o', '*', 'o'}
        };

        String expectedString = arrayToString(board, expected);
        assertEquals(expectedString, board.toString());   
    }

    @Test
    public void testCrownAlreadyCrownedPiece(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startPos = new BoardPosition(6, 2);
        BoardPosition endPos = new BoardPosition (7, 3);

        // Set up the state of the board with  a crowned piece at (6, 2)
        board.placePiece(endPos, ' ');
        board.placePiece(startPos, 'X');
        board.movePiece(startPos, DirectionEnum.SE);

        // Check that the piece at position (6, 2) remains crowned
        assertEquals('X', board.whatsAtPos(endPos));
        // Verify the starting pos is empty
        assertEquals(' ', board.whatsAtPos(startPos));


        char[][] expected = {
            {'x', '*', 'x', '*', 'x', '*', 'x', '*'},
            {'*', 'x', '*', 'x', '*', 'x', '*', 'x'},
            {' ', '*', ' ', '*', 'x', '*', 'x', '*'},
            {'*', ' ', '*', ' ', '*', ' ', '*', ' '},
            {' ', '*', ' ', '*', ' ', '*', ' ', '*'},
            {'*', ' ', '*', 'o', '*', 'o', '*', 'o'},
            {'o', '*', ' ', '*', 'o', '*', 'o', '*'},
            {'*', 'o', '*', 'X', '*', 'o', '*', 'o'}
        };

        String expectedString = arrayToString(board, expected);
        assertEquals(expectedString, board.toString());
    }

    @Test
    public void testCrownPieceMinRowMinCol(){
        ICheckerBoard board = makeBoard(16);
        char playerTwo = 'o'; // Set playerTwo directly here
        BoardPosition startingPos = new BoardPosition(1, 1);
        BoardPosition endingPos = new BoardPosition(0, 0);

        board.placePiece(startingPos, playerTwo);
        board.placePiece(endingPos, ' ');
        board.movePiece(startingPos, DirectionEnum.NW);
        board.crownPiece(endingPos); // Crown the piece

        assertEquals('O', board.whatsAtPos(endingPos));
    }

    @Test
    public void testMoveToValidPos(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(2, 0);
        DirectionEnum dir = DirectionEnum.SE;

        board.movePiece(startingPos, dir);    // Moves piece from (2,0) to (3,1)
        assertEquals(' ', board.whatsAtPos(startingPos));    // Checks the first position (2,0)

        BoardPosition newPos = new BoardPosition(3, 1);    // Checks the new position
        assertEquals('x', board.whatsAtPos(newPos));
    }

    @Test
    public void testMoveToOutOfBounds(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(2, 0);
        DirectionEnum dir = DirectionEnum.SW;    

        board.movePiece(startingPos, dir);    // Attempts to move the piece off of the board
        assertEquals('x', board.whatsAtPos(startingPos));
    }

    @Test
    public void testMoveToOccupiedSpace(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(6, 0);
        DirectionEnum dir = DirectionEnum.NE;

        board.movePiece(startingPos, dir);    // Attempt to move the piece from (6,0) to (5,1)
        assertEquals('o', board.whatsAtPos(startingPos));    // Verifys that the starting position is still occupied
        BoardPosition secondPos = new BoardPosition(5, 1);    // Verifies that the position (5, 1) is still occupied with its original piece
        assertEquals('o', board.whatsAtPos(secondPos));
    }

    @Test
    public void jumpPiece_testValidJump(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(3, 3);
        DirectionEnum dir = DirectionEnum.SW;

        // Set up the state of the board
        board.placePiece(startingPos, 'x');
        board.placePiece(new BoardPosition(4, 2), 'o');

        board.placePiece(new BoardPosition(5,1), ' ');
        BoardPosition newPos = board.jumpPiece(startingPos, dir);

        // Check the original position (3, 3)
        assertEquals(' ', board.whatsAtPos(startingPos));

        // Check the jumped position (4, 2)
        assertEquals(' ', board.whatsAtPos(new BoardPosition(4, 2)));

        // Check the new position (5, 1)
        assertEquals('x', board.whatsAtPos(newPos));
        assertEquals(new BoardPosition(5, 1), newPos);
    }

    @Test
    public void jumpPiece_testJumpOwnPiece(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(2, 4);
        DirectionEnum dir = DirectionEnum.SW;

        // Set up the state of the board
        board.placePiece(startingPos, 'x');
        board.placePiece(new BoardPosition(3, 3), 'x');

        // Try jumping own piece
        BoardPosition newPos = board.jumpPiece(startingPos, dir);

        // Check that the original position (2, 4) is still there
        assertEquals('x', board.whatsAtPos(startingPos));

        // Check that the jumped position (3, 3) is also there
        assertEquals('x', board.whatsAtPos(new BoardPosition(3, 3)));

        // Verify the jump wasn't performed
        assertEquals(startingPos, newPos);
    }

    @Test
    public void testJumpOffBoard(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startingPos = new BoardPosition(7, 7);
        DirectionEnum dir = DirectionEnum.SE;

        // Set up the state of the board
        board.placePiece(startingPos, 'o');
        board.placePiece(new BoardPosition(6, 6), 'x');

        // Try jumping off the board
        BoardPosition newPos = board.jumpPiece(startingPos, dir);

        // Check that the original position (7, 7) is still occupied
        assertEquals('o', board.whatsAtPos(startingPos));

        // Check that the jumped position (6, 6) is still occupied
        assertEquals('x', board.whatsAtPos(new BoardPosition(6, 6)));

        // Check that the jump was not performed (newPos should be the same as startingPos)
        assertEquals(startingPos, newPos);
    }

    @Test
    public void testPlayerOneLosesPieces(){
        ICheckerBoard board = makeBoard(8);

        HashMap<Character, Integer> pieceCounts = new HashMap<>();
        pieceCounts.put('x', 5);  //count of pieces for 'x'
        pieceCounts.put('o', 5);  //count of pieces for 'o'

        //number of pieces 'x' loses
        int numPieces = 3;
        board.playerLostPieces(numPieces, 'x', pieceCounts);


        //expected outcome
        int expectedPiecesX = 2;
        int expectedPiecesO = 5; //never changed
        int observedValueX = (int)pieceCounts.get('x');
        int observedValueO = (int)pieceCounts.get('o');

        assertEquals(expectedPiecesX, observedValueX);
        assertEquals(expectedPiecesO, observedValueO);

    }

    @Test
    public void testScanSurroundingPositionsAtMaxRow(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startPos = new BoardPosition(7,3);


        HashMap<DirectionEnum, Character> observedItem = board.scanSurroundingPositions(startPos);
        HashMap<DirectionEnum, Character> expectedItem = new HashMap<>();
        expectedItem.put(DirectionEnum.NE, 'o');
        expectedItem.put(DirectionEnum.NW, 'o');
        expectedItem.put(DirectionEnum.SE, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.SW, ICheckerBoard.EMPTY_POS);

        assertEquals(expectedItem, observedItem);
    }

    @Test
    public void testScanSurroundingPositionsFromCenter(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startPos = new BoardPosition(4, 4);


        HashMap<DirectionEnum, Character> observedItem = board.scanSurroundingPositions(startPos);

        HashMap<DirectionEnum, Character> expectedItem = new HashMap<>();
        expectedItem.put(DirectionEnum.NE, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.NW, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.SE, 'o');
        expectedItem.put(DirectionEnum.SW, 'o');

        assertEquals(expectedItem, observedItem);

    }

    @Test
    public void testScanSurroundingPositionsFromNewCrownedKingPositionAtMinRow(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startPos = new BoardPosition(0, 4);

        //set up the board for the test scenario
        board.placePiece(startPos, 'O'); //crowned king position
        board.placePiece(new BoardPosition(0, 0), 'x');
        board.placePiece(new BoardPosition(0, 2), 'x');
        board.placePiece(new BoardPosition(0, 4), 'O'); //crowned king position
        board.placePiece(new BoardPosition(0, 6), 'x');

        board.placePiece(new BoardPosition(1, 1), 'x');
        board.placePiece(new BoardPosition(1, 3), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(1, 5), 'x');
        board.placePiece(new BoardPosition(1, 7), 'x');

        board.placePiece(new BoardPosition(2, 0), 'x');
        board.placePiece(new BoardPosition(2, 2), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(2, 4), 'x');
        board.placePiece(new BoardPosition(2, 6), 'x');

        board.placePiece(new BoardPosition(3, 3), 'o');
        board.placePiece(new BoardPosition(3, 7), ICheckerBoard.EMPTY_POS);

        board.placePiece(new BoardPosition(4, 2), 'o');
        board.placePiece(new BoardPosition(4, 4), 'o');

        board.placePiece(new BoardPosition(5, 1), 'o');
        board.placePiece(new BoardPosition(5, 3), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(5, 5), 'o');
        board.placePiece(new BoardPosition(5, 7), 'o');

        board.placePiece(new BoardPosition(6, 0), 'o');
        board.placePiece(new BoardPosition(6, 2), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(6, 4), 'o');
        board.placePiece(new BoardPosition(6, 6), 'o');

        board.placePiece(new BoardPosition(7, 1), 'o');
        board.placePiece(new BoardPosition(7, 3), 'o');
        board.placePiece(new BoardPosition(7, 5), 'o');
        board.placePiece(new BoardPosition(7, 7), ICheckerBoard.EMPTY_POS);


        HashMap<DirectionEnum, Character> observedItem = board.scanSurroundingPositions(startPos);

        HashMap<DirectionEnum, Character> expectedItem = new HashMap<>();
        expectedItem.put(DirectionEnum.NE, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.NW, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.SE, 'x');
        expectedItem.put(DirectionEnum.SW, ICheckerBoard.EMPTY_POS);

        assertEquals(expectedItem, observedItem);

    }

    @Test
    public void testScanSurroundingPositionsAtMaxCol(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startPos = new BoardPosition(3, 7);

        //setting up the board for the test scenario
        board.placePiece(new BoardPosition(0, 0), 'x');
        board.placePiece(new BoardPosition(0, 2), 'x');
        board.placePiece(new BoardPosition(0, 4), 'x');
        board.placePiece(new BoardPosition(0, 6), 'x');

        board.placePiece(new BoardPosition(1, 1), 'x');
        board.placePiece(new BoardPosition(1, 3), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(1, 5), 'x');
        board.placePiece(new BoardPosition(1, 7), 'x');

        board.placePiece(new BoardPosition(2, 0), 'x');
        board.placePiece(new BoardPosition(2, 2), 'o');
        board.placePiece(new BoardPosition(2, 4), 'o');
        board.placePiece(new BoardPosition(2, 6), 'x');

        board.placePiece(new BoardPosition(3, 1), 'x');
        board.placePiece(new BoardPosition(3, 3), 'o');
        board.placePiece(new BoardPosition(3, 5), 'x');
        board.placePiece(new BoardPosition(3, 7), 'o'); //piece at max col

        board.placePiece(new BoardPosition(4, 0), 'x');
        board.placePiece(new BoardPosition(4, 2), 'o');
        board.placePiece(new BoardPosition(4, 6), 'o');

        board.placePiece(new BoardPosition(5, 1), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(5, 3), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(5, 5), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(5, 7), ICheckerBoard.EMPTY_POS);

        board.placePiece(new BoardPosition(6, 0), 'o');
        board.placePiece(new BoardPosition(6, 4), 'o');
        board.placePiece(new BoardPosition(6, 2), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(6, 6), ICheckerBoard.EMPTY_POS);

        board.placePiece(new BoardPosition(7, 1), 'o');
        board.placePiece(new BoardPosition(7, 3), 'o');
        board.placePiece(new BoardPosition(7, 5), 'o');
        board.placePiece(new BoardPosition(7, 7), 'o');


        HashMap<DirectionEnum, Character> observedItem = board.scanSurroundingPositions(startPos);

        HashMap<DirectionEnum, Character> expectedItem = new HashMap<>();
        expectedItem.put(DirectionEnum.NE, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.NW, 'x');
        expectedItem.put(DirectionEnum.SE, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.SW, 'o');

        assertEquals(expectedItem, observedItem);

    }

    @Test
    public void test_ScanSurroundingPositions_AtMinCol(){
        ICheckerBoard board = makeBoard(8);
        BoardPosition startPos = new BoardPosition(2, 0);

        //setting up the board for the test scenario
        board.placePiece(new BoardPosition(0, 0), 'x');
        board.placePiece(new BoardPosition(0, 2), 'x');
        board.placePiece(new BoardPosition(0, 4), 'x');
        board.placePiece(new BoardPosition(0, 6), 'x');

        board.placePiece(new BoardPosition(1, 1), 'x');
        board.placePiece(new BoardPosition(1, 3), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(1, 5), 'x');
        board.placePiece(new BoardPosition(1, 7), 'x');

        board.placePiece(new BoardPosition(2, 0), 'x'); //piece at min col
        board.placePiece(new BoardPosition(2, 2), 'o');
        board.placePiece(new BoardPosition(2, 4), 'o');
        board.placePiece(new BoardPosition(2, 6), 'x');

        board.placePiece(new BoardPosition(3, 1), 'x');
        board.placePiece(new BoardPosition(3, 3), 'o');
        board.placePiece(new BoardPosition(3, 5), 'x');
        board.placePiece(new BoardPosition(3, 7), 'o');

        board.placePiece(new BoardPosition(4, 0), 'x');
        board.placePiece(new BoardPosition(4, 2), 'o');
        board.placePiece(new BoardPosition(4, 6), 'o');

        board.placePiece(new BoardPosition(5, 1), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(5, 3), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(5, 5), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(5, 7), ICheckerBoard.EMPTY_POS);

        board.placePiece(new BoardPosition(6, 0), 'o');
        board.placePiece(new BoardPosition(6, 4), 'o');
        board.placePiece(new BoardPosition(6, 2), ICheckerBoard.EMPTY_POS);
        board.placePiece(new BoardPosition(6, 6), ICheckerBoard.EMPTY_POS);

        board.placePiece(new BoardPosition(7, 1), 'o');
        board.placePiece(new BoardPosition(7, 3), 'o');
        board.placePiece(new BoardPosition(7, 5), 'o');
        board.placePiece(new BoardPosition(7, 7), 'o');

        HashMap<DirectionEnum, Character> observedItem = board.scanSurroundingPositions(startPos);

        HashMap<DirectionEnum, Character> expectedItem = new HashMap<>();
        expectedItem.put(DirectionEnum.NE, 'x');
        expectedItem.put(DirectionEnum.NW, ICheckerBoard.EMPTY_POS);
        expectedItem.put(DirectionEnum.SE, 'x');
        expectedItem.put(DirectionEnum.SW, ICheckerBoard.EMPTY_POS);

        assertEquals(expectedItem, observedItem);

    }

    @Test
    public void testGetDirectionsAllDirections(){
        assertEquals(new BoardPosition(-1, 1), ICheckerBoard.getDirection(DirectionEnum.NE));
        assertEquals(new BoardPosition(-1, -1), ICheckerBoard.getDirection(DirectionEnum.NW));
        assertEquals(new BoardPosition(1, 1), ICheckerBoard.getDirection(DirectionEnum.SE));
        assertEquals(new BoardPosition(1, -1), ICheckerBoard.getDirection(DirectionEnum.SW));
    }
}
