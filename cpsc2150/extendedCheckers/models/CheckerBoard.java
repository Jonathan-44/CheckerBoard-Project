package cpsc2150.extendedCheckers.models;

import cpsc2150.extendedCheckers.util.DirectionEnum;
import cpsc2150.extendedCheckers.views.CheckersFE;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * CheckerBoard implements the ICheckerBoard interface, it represents the state of the checkerboard in a checkers game, 
 * including the board layout, the count of pieces per player, and the possible move directions for each player's pieces. 
 * The board layout includes playable and non-playable spaces, distingushed by different characters.
 * 
 * @invariant a player's piece can never move onto a black tile
 * @invariant a player's piece count can't go below 0
 * @invariant a player's piece can never move out of bound of the board
 * @invariant when a player moves a piece, the original position of the piece is changed to an empty position
 * @invariant when a player jumps a piece, the original position of both pieces is changed to an empty position
 * @invariant a player's piece count can never be above the starting piece count
 * @invariant while a player's piece is a normal piece, it can only move in a diagonal direction away from its starting position
 * @invariant a player can only move a piece to an empty position
 * @invariant the game can't continue if any player's piece count is equal to 0
 */

public class CheckerBoard extends AbsCheckerBoard{
    /**
     * A 2D array of characters used to represent our checkerboard.
     */
    private char[][] board;

    /**
     * A HashMap, with a Character key and an Integer value, that is used to map a player's char to the number of
     * tokens that player still has left on the board.
     */
    private HashMap<Character, Integer> pieceCount;

    /**
     * A HashMap, with a Character key and an ArrayList of DirectionEnums value, used to map a player (and its king
     * representation) to the directions that player can viably move in. A non-kinged (standard) piece can only move
     * in the diagonal directions away from its starting position. A kinged piece can move in the same directions the
     * standard piece can move in plus the opposite directions the standard piece can move in.
     */
    private HashMap<Character, ArrayList<DirectionEnum>> viableDirections;

    /**
     * Constructs a new CheckerBoard object
     *
     * @pre None
     *
     * @post [The checkerboard is initalized with the size of [@value ROW_NUM] rows and [@value COL_NUM] columns.
     * The board is represented by a 2D char arry [@value board].
     * Each player starts with [@value STARTING_COUNT] pieces.
     * Player 1 is represented by [@value PLAYER_ONE] and starts at the top of the board.
     * Player 2 is represented by [@value PLAYER_TWO] and start at the bottom of the board.
     * [@value pieceCount] maps each players char to the remaining tokens they still have left.
     * The board positions that dont have a piece are initalized with [@value EMPTY_POS] that represents an open
     * playable position or [@value BLACK_TILE] that represents black tiles.
     * [@value viableDirections] maps each players viable moves. Player 1 is at the top so they can only move
     * SW or SE. Player 2 is at the bottom so they can only move NW or NE.]
     */

    public CheckerBoard(int aDimension) {

        int rowsPerPlayer = (aDimension - 2) / 2;
        int piecesPerRow = aDimension / 2;
        int STARTING_COUNT = rowsPerPlayer * piecesPerRow;

        board = new char[aDimension][aDimension];
        pieceCount = new HashMap<>();
        pieceCount.put(CheckersFE.getPlayerOne(), STARTING_COUNT);
        pieceCount.put(CheckersFE.getPlayerTwo(), STARTING_COUNT);
    
        viableDirections = new HashMap<>();
        viableDirections.put(CheckersFE.getPlayerOne(), new ArrayList<>());
        viableDirections.put(Character.toUpperCase(CheckersFE.getPlayerOne()), new ArrayList<>());
        viableDirections.put(CheckersFE.getPlayerTwo(), new ArrayList<>());
        viableDirections.put(Character.toUpperCase(CheckersFE.getPlayerTwo()), new ArrayList<>());

        if (aDimension < BOARD_MIN_SIZE || aDimension > BOARD_MAX_SIZE || aDimension % 2 != 0) {
            throw new IllegalArgumentException("Invalid board size.");
        }

        for (int row = 0; row < aDimension; row++) {
            for (int col = 0; col < aDimension; col++) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = BLACK_TILE;
                } else {
                    board[row][col] = EMPTY_POS;
                }
            }
        }
        
        int rowsNeeded = (int) Math.ceil(STARTING_COUNT / (double) piecesPerRow);
        int piecesPlaced = 0;
        for (int row = 0; row < rowsNeeded && piecesPlaced < STARTING_COUNT; row++) {
            for (int col = 0; col < aDimension; col++) {
                if ((row + col) % 2 == 0 && board[row][col] == EMPTY_POS) {
                    placePiece(new BoardPosition(row, col), CheckersFE.getPlayerOne());
                    pieceCount.put(CheckersFE.getPlayerOne(), pieceCount.get(CheckersFE.getPlayerOne()) + ADD_ONE);
                    piecesPlaced++;
                }
            }
        }
        
        piecesPlaced = 0;
        for (int row = aDimension - 1; row >= aDimension - rowsNeeded && piecesPlaced < STARTING_COUNT; row--) {
            for (int col = 0; col < aDimension; col++) {
                if ((row + col) % 2 == 0 && board[row][col] == EMPTY_POS) {
                    placePiece(new BoardPosition(row, col), CheckersFE.getPlayerTwo());
                    pieceCount.put(CheckersFE.getPlayerTwo(), pieceCount.get(CheckersFE.getPlayerTwo()) + ADD_ONE);
                    piecesPlaced++;
                }
            }
        }
    
        addViableDirections(CheckersFE.getPlayerOne(), DirectionEnum.SE);
        addViableDirections(CheckersFE.getPlayerOne(), DirectionEnum.SW);
        addViableDirections(CheckersFE.getPlayerTwo(), DirectionEnum.NE);
        addViableDirections(CheckersFE.getPlayerTwo(), DirectionEnum.NW);
    }

    /**
     * Returns the viableDirections HashMap
     *
     * @pre None
     *
     * @post getViableDirections = viableDirections AND board = #board AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections
     *
     * @return A map where the keys are piece characters and the values are lists of
     * viable directions for that piece.
     */
    public HashMap<Character, ArrayList<DirectionEnum>> getViableDirections(){
        return viableDirections;
    }
    
    /**
     * Return PieceCounts HashMap
     *
     * @pre None
     *
     * @post getPieceCounts = [getPieceCounts HashMap, mapping players to their remaining without modifying it.] AND board = #board
     *       AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections
     *
     * @return The HashMap containg the amount of pieces on the baord.
     */
    public HashMap<Character, Integer> getPieceCounts() {
        return new HashMap<>(pieceCount);
    }

    public void placePiece(BoardPosition pos, char player) {
        board[pos.getRow()][pos.getColumn()] = player;
    }

    public char whatsAtPos(BoardPosition pos) {
        if (pos.getRow() < 0 || pos.getRow() >= getRowNum() || pos.getColumn() < 0 || pos.getColumn() >= getColNum()){
            return EMPTY_POS;
        }
        return board[pos.getRow()][pos.getColumn()];
    }

    /**
     * Gets the number of rows on the board.
     *
     * @pre None.
     *
     * @post getRowNum = [The current row number, no data is altered] AND board = #board AND
     *      pieceCounts = #pieceCounts AND viableDirections = #viableDirections
     *
     * @return The constannt ROW_NUM
     */
    public int getRowNum() {
        return board.length;
    }

    /**
     * Gets the number of columns on the board.
     *
     * @pre None.
     *
     * @post getColNum = [The current column number, no data is altered] AND board = #board AND
     *       pieceCounts = #pieceCounts AND viableDirections = #viableDirections
     *
     * @return The constant COL_NUM
     */
    public int getColNum() {
        return board[0].length;
    }
}
