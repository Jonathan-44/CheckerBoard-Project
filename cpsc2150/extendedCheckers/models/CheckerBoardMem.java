package cpsc2150.extendedCheckers.models;

import cpsc2150.extendedCheckers.util.DirectionEnum;
import cpsc2150.extendedCheckers.views.CheckersFE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CheckerBoardMem implements the ICheckerBoard interface and represents the state of the checkerboard
 * in a checkers game using memory as the underlying storage mechanism.
 * 
 * @invariant A player's piece can never move onto a black tile.
 * @invariant A player's piece count can't go below 0.
 * @invariant A player's piece can never move out of bounds of the board.
 * @invariant When a player moves a piece, the original position of the piece is changed to an empty position.
 * @invariant When a player jumps a piece, the original position of both pieces is changed to an empty position.
 * @invariant A player's piece count can never be above the starting piece count.
 * @invariant While a player's piece is a normal piece, it can only move in a diagonal direction away from its starting position.
 * @invariant A player can only move a piece to an empty position.
 * @invariant The game can't continue if any player's piece count is equal to 0.
 */
public class CheckerBoardMem extends AbsCheckerBoard{

    private Map<Character, List<BoardPosition>> playerPositionsMap;
    private HashMap<Character, Integer> pieceCount;
    private char playerOne;
    private char playerTwo;

    /**
     * @pre none
     * @param aDimension is the dimension of the board
     * @return none
     * @post [A CheckerBoardMem object is constructed with a board of size aDimension x aDimension. 
     * The board is initialized with pieces for playerOne and playerTwo placed on opposited sides of the board.
     * Each player's piece is represented by their respective characters on the board.]
     */
    public CheckerBoardMem(int aDimension) {
        super();

        if (aDimension < BOARD_MIN_SIZE || aDimension > BOARD_MAX_SIZE || aDimension % 2 != 0) {
            throw new IllegalArgumentException("Invalid board size.");
        }

        playerPositionsMap = new HashMap<>();
        pieceCount = new HashMap<>();
        // Initialize player one and player two characters
        playerOne = CheckersFE.getPlayerOne();
        playerTwo = CheckersFE.getPlayerTwo();
        // Initialize piece counts for player one and player two
        pieceCount.put(playerOne, 0);
        pieceCount.put(playerTwo, 0);
        initializePlayerPositions(aDimension);
    }

    /**
     * Gets player one's piece character
     * @pre None
     * @param playerOne
     * @return a character piece
     * @post [playerOne's piece character]
     */
    public void setplayerOne(char playerOne) {
        this.playerOne = playerOne;
    }

    /**
     * Gets player two's piece character
     * @pre None
     * @param playerOne
     * @return a character piece
     * @post [playerTwo's piece character]
     */
    public void setplayerTwo(char playerTwo) {
        this.playerTwo = playerTwo;
    }   

    /**
     * Initializes player positions based on size of board
     * @pre aDimension != null
     * @param aDimension is the dimension of the board
     * @return A map of the positions of each player's pieces
     * @post playerPositionsMap = [A map where the keys are player characters and the values are lists
     */
    private void initializePlayerPositions(int aDimension) {
        // Calculate the midpoint of the board
        int midRow = aDimension / 2;
    
        // Iterate over the board positions
        for (int row = 0; row < aDimension; row++) {
            for (int col = 0; col < aDimension; col++) {
                if ((row + col) % 2 == 0) { // Check if position is playable
                    char player;
                    if (row < midRow - 1) {
                        player = playerOne; // Player One's pieces in the top rows
                    } else if (row >= midRow + 1) {
                        player = playerTwo; // Player Two's pieces in the bottom rows
                    } else {
                        continue; // Skip empty rows between players
                    }
                    placePiece(new BoardPosition(row, col), player); // Place the piece on the board
                }
            }
        }
    }
    /**
     * Places a piece on the board at the given position
     * @pre newPos!= null AND player!= null
     * @param newPos is the position to place the piece
     * @param player is the player to place the piece
     * @return A piece is placed on the board at the given position
     * @post [A piece is placed on the board at the given position for the given player character
     *         the piece count is incremented and the board is updated to reflect the change]
     */
    @Override
    public void placePiece(BoardPosition newPos, char player) {
        for (List<BoardPosition> positions : playerPositionsMap.values()) {
            positions.removeIf(pos -> pos.equals(newPos));
        }

        // Add the piece to the new position
        if (player == CheckersFE.getPlayerOne() || player == CheckersFE.getPlayerTwo()) {
            // Check if the player character is valid

            if (!playerPositionsMap.containsKey(player)) {
                playerPositionsMap.put(player, new ArrayList<>());
            }
            playerPositionsMap.get(player).add(newPos);
        }
    }
    /**
     * Gets the piece at the given position
     * @pre pos!= null
     * @param pos is the position to get the piece
     * @return The piece at the given position
     * @post [The piece at the given position is returned.]
     */

    
    public char whatsAtPos(BoardPosition pos) {
    
        for (Map.Entry<Character, List<BoardPosition>> entry : playerPositionsMap.entrySet()) {
            for (BoardPosition piecePos : entry.getValue()) {
                if (piecePos.equals(pos)) {
                    return entry.getKey();
                }
            }
        }
        return ' ';
    }
    /**
     * Gets the number of pieces each player has remaining on the board
     * @pre None
     * @param None
     * @return the number of pieces each player has remaining on the board
     * @post [A HashMap of the number of pieces each player has remaining, no data is altered]
     */
    @Override
    public HashMap<Character, Integer> getPieceCounts() {
        HashMap<Character, Integer> pieceCounts = new HashMap<>();
        pieceCounts.put(CheckersFE.getPlayerOne(), 0);
        pieceCounts.put(CheckersFE.getPlayerTwo(), 0);

        for (int row = 0; row < getRowNum(); row++) {
            for (int col = 0; col < getColNum(); col++) {
                char piece = whatsAtPos(new BoardPosition(row, col));
                if (piece == CheckersFE.getPlayerOne() || piece == Character.toUpperCase(CheckersFE.getPlayerOne())) {
                    pieceCounts.put(CheckersFE.getPlayerOne(), pieceCounts.get(CheckersFE.getPlayerOne()) + 1);
                } 
                else if (piece == CheckersFE.getPlayerTwo() || piece == Character.toUpperCase(CheckersFE.getPlayerTwo())) {
                    pieceCounts.put(CheckersFE.getPlayerTwo(), pieceCounts.get(CheckersFE.getPlayerTwo()) + 1);
                }
            }
        }

        return pieceCounts;        
    }

    /**
     * Gets the available directions for each piece
     * @pre None
     * @param None
     * @return the available directions for each piece
     * @post [A HashMap of the available directions for each piece, no data is altered]
     */
    @Override
    public HashMap<Character, ArrayList<DirectionEnum>> getViableDirections() {
        HashMap<Character, ArrayList<DirectionEnum>> viableDirections = new HashMap<>();
    
        // Adding viable directions for each player
        for (char player : playerPositionsMap.keySet()) {
            ArrayList<DirectionEnum> directions = new ArrayList<>();
    
            // Determines player's piece directions
            if (Character.toLowerCase(player) == playerOne) {
                directions.add(DirectionEnum.SW);
                directions.add(DirectionEnum.SE);
            } else if (Character.toLowerCase(player) == playerTwo) {
                directions.add(DirectionEnum.NW);
                directions.add(DirectionEnum.NE);
            }
        
            viableDirections.put(player, directions);
        }
    
        return viableDirections;
    }
    /**
     * Gets the row number of the board
     * @pre None
     * @param None
     * @return the row number of the board
     * @post [The row number of the board is returned as an int.]
     */
    @Override
    public int getRowNum() {
        int maxRow = 0;
        for (List<BoardPosition> positions : playerPositionsMap.values()) {
            for (BoardPosition pos : positions) {
                maxRow = Math.max(maxRow, pos.getRow());
            }
        }
        // Adding 1 to account for 0-based indexing
        return maxRow + 1;
    }
    /**
     * Gets the column number of the board
     * @pre None
     * @param None
     * @return the column number of the board
     * @post [The column number of the board is returned as an int.]
     */
    @Override
    public int getColNum() {
        int maxCols = 0;
        for (List<BoardPosition> positions : playerPositionsMap.values()) {
            for (BoardPosition pos : positions) {
                maxCols = Math.max(maxCols, pos.getColumn() + 1); // Increment by 1 to account for 0-based indexing
            }
        }
        return maxCols;
    }
    /**
     * Creates a string to represent the state of the board
     * @pre Board is not null
     * @param none
     * @return a string representation of the board
     * @post [A string representation of the board created via stringbuilded and returned.]
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("|  ");
        for (int col = 0; col < getColNum(); col++) {
            if (col < 10) {
                sb.append("| ").append(col);
            } else {
                sb.append("|").append(col);
            }
        }
        sb.append("|\n");

        for (int row = 0; row < getRowNum(); row++) {
            if (row < 10) {
                sb.append("|" + row + " ");
            } else {
                sb.append("|" + row);
            }
            for (int col = 0; col < getColNum(); col++) {
                BoardPosition pos = new BoardPosition(row, col);
                char piece = whatsAtPos(pos);
                if ((row + col) % 2 != 0 && piece == EMPTY_POS) {
                    sb.append("|* ");
                } else {
                    sb.append("|").append(piece).append(" ");
                }
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
    /**
     * Moves a piece on the board
     * @pre Board is not null
     * @param startingPos the starting position of the piece
     * @param dir the direction to move the piece
     * @return the new position of the piece
     * @post [A BoardPosition object representing the new position of the piece is returned.]
     */
    @Override
    public BoardPosition movePiece(BoardPosition startingPos, DirectionEnum dir) {
        BoardPosition direction = getDirection(dir);
    
        int newRow = startingPos.getRow() + direction.getRow();
        int newCol = startingPos.getColumn() + direction.getColumn();
    
        if (newRow >= 0 && newRow < getRowNum() && newCol >= 0 && newCol < getColNum()) {
            char piece = whatsAtPos(startingPos);
    
            // Check if the new position is empty
            if (whatsAtPos(new BoardPosition(newRow, newCol)) == EMPTY_POS) {
                // Update player map
                updatePlayerMap(startingPos, new BoardPosition(newRow, newCol), piece);
                
                // Check if the piece can be crowned
                crownPiece(new BoardPosition(newRow, newCol));
    
                return new BoardPosition(newRow, newCol);
            }
        }
    
        return startingPos;
    }
    /**
     * Jumps a piece on the board
     * @pre Board is not null
     * @param startingPos the starting position of the piece
     * @param dir the direction to jump the piece
     * @return the new position of the piece
     * @post [A BoardPosition object representing the new position of the piece is returned,
     *        if the jump is valid, the jumping piece is moved two positions forward and
     *        the jumped piece is removed from the board.]
     */
    @Override
    public BoardPosition jumpPiece(BoardPosition startingPos, DirectionEnum dir) {

        BoardPosition jumpOverPosRel = getDirection(dir);

        int jumpOverRow = startingPos.getRow() + jumpOverPosRel.getRow();
        int jumpOverCol = startingPos.getColumn() + jumpOverPosRel.getColumn();
        BoardPosition jumpOverPos = new BoardPosition(jumpOverRow, jumpOverCol);

        int finalRow = jumpOverRow + jumpOverPosRel.getRow();
        int finalCol = jumpOverCol + jumpOverPosRel.getColumn();
        BoardPosition finalPos = new BoardPosition(finalRow, finalCol);

        // Check if jumpOverPos and finalPos are within bounds of the board
        if (isWithinBounds(jumpOverPos) && isWithinBounds(finalPos)) {

            // Ensure jumping over an opponent's piece and landing on an empty position
            if (finalRow >= 0 && finalRow < getRowNum() && finalCol >= 0 && finalCol < getColNum()) {
                char opponentPiece = whatsAtPos(jumpOverPos);
                char currentPiece = whatsAtPos(startingPos);
            
                // Ensure jumping over an opponent's piece and landing on an empty position
                if (opponentPiece != currentPiece && opponentPiece != EMPTY_POS && whatsAtPos(finalPos) == EMPTY_POS) {
                    // Jump is valid
                    placePiece(finalPos, currentPiece);
                    placePiece(jumpOverPos, EMPTY_POS);
                    placePiece(startingPos, EMPTY_POS);
                    crownPiece(finalPos); // Crown the piece if it reaches the opposite end
                    return finalPos;
                }
            }
        }

        return startingPos;
    }
    /**
     * Crowns a piece on the board
     * @pre Board is not null
     * @param pos the position of the piece to crown
     * @return the capitalized piece on the board
     * @post [Assigns pieces that made it to the other side as king, capitalizes the piece and adds
     *        the piece to the character map.]
     */
    @Override
    public void crownPiece(BoardPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        char piece = whatsAtPos(pos);

        // Check if the piece is at the opposite end of the board
        if ((piece == playerOne && row == getRowNum() - 1) || (piece == playerTwo && row == 0)) {
            // Crown the piece
            playerPositionsMap.get(piece).remove(pos);
            char crownedPiece = Character.toUpperCase(piece);
            if (!playerPositionsMap.containsKey(crownedPiece)) {
                playerPositionsMap.put(crownedPiece, new ArrayList<>());
            }
            placePiece(pos, crownedPiece);
            playerPositionsMap.get(crownedPiece).add(pos);
        }
    }
    /**
     * Checks if a space on the board is within the bounds of the board
     * @pre Board is not null
     * @param pos the position to check
     * @return if the position is in bounds
     * @post [true is returned if the position is within the bounds of the board, false otherwise.]
     */
    // Check if a position is within the bounds of the board
    private boolean isWithinBounds(BoardPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        return row >= 0 && row < getRowNum() && col >= 0 && col < getColNum();
    }
    /**
     * Updates the player map
     * @pre none
     * @param startingPos the starting position of the piece
     * @param newPos the new position of the piece
     * @param piece the character piece to be updated
     * @return none
     * @post [Updates the player map with the new position of the piece and the piece to be updated.]
     */
    private void updatePlayerMap(BoardPosition startingPos, BoardPosition newPos, char piece) {
        List<BoardPosition> positions = playerPositionsMap.get(piece);
        if (positions != null) {
            positions.remove(startingPos);
            positions.add(newPos);
        }
    }
    /**
     * Defines the directions of each cardinal direction
     * @pre none
     * @param dir the direction of the piece
     * @return the direction of each cardinal direction
     * @post [A BoardPosition object representing the direction of each cardinal direction is returned.]
     */
    private BoardPosition getDirection(DirectionEnum dir) {
        switch (dir) {
            case SW:
                return new BoardPosition(1, -1);
            case SE:
                return new BoardPosition(1, 1);
            case NW:
                return new BoardPosition(-1, -1);
            case NE:
                return new BoardPosition(-1, 1);
            default:
                throw new IllegalArgumentException("Invalid direction.");
        }
    }
}
