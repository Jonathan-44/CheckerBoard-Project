package cpsc2150.extendedCheckers.models;

import cpsc2150.extendedCheckers.util.DirectionEnum;
import cpsc2150.extendedCheckers.views.CheckersFE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The interface for the CheckerBoard.
 * This interface contains methods required to manipulate and query the state of the checkerboard.
 *
 * @defines pos a position on the checkerboard
 *          player: a character representing the player
 *          dir: a playable direction
 *          self: the overall state of the board, including positions of all pieces
 *          pieceCounts: the count of pieces each player has on the board, hinting at the actual piece counts.
 *          viableDirections: the set of variable move directions for pieces, hinting at actual move possibilities.
 *          startingPos: the starting position of the board at the beginning of the game.
 *
 *
 *
 * @constraints self min size is 8x8, represented by BOARD_MIN_SIZE
 *              self max size is 16x16, represented by BOARD_MAX_SIZE
 *              self dimensions must always be equal and even
 *              self has a valid row and column number within the bounds of the board,
 *              and each player is represented by a character
 *              there must always be 2 empty rows of spaces between player 1 and player 2.
 *
 *
 * @initialization_ensures  a CheckerBoard object is initialized with a board of size (Dimension x Dimension), where Dimension is between 
 *                          BOARD_MIN_SIZE and BOARD_MAX_SIZE. Each player begins with a number of pieces calculated based on the board size, 
 *                          and pieces are placed in their starting positions according to the rules of checkers. The board's
 *                          overall state reflects a ready to play game setup.
 */
public interface ICheckerBoard {
    public static final char EMPTY_POS = ' ';
    public static final char BLACK_TILE = '*';

    public static final int BOARD_MIN_SIZE = 8;
    public static final int BOARD_MAX_SIZE = 16;

    public static final int ADD_ONE = 1;

    /**
    * Places a player's piece on the board at the specified position.
    *
    * @param pos a position on the checkerboard
    * @param player the player's piece character
    *
    * @pre player is either an 'x' or 'o', indicating a valid player.
    * @pre pos is a valid board position within the boundaries of the board and is not already occupied.
    *
    * @post placePiece = [the board's state is updated to reflect the placement of the player's piece at the position pos] AND
    *       BOARD_STATE_At_POS = #BOARD_STATE_At_POS with the specified position now occupied by player's piece.
    *
    *
    */
    void placePiece(BoardPosition pos, char player);

    /**
    * Gets the piece given at the specific board position.
    *
    * @param pos the position on the board to check.
    *
    * @pre pos is a valid board position within the defined boundaries of the board.
    *
    * @post whatsAtPos = [the piece located at the specified position, pos] AND
    *       self = #self AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections.
    *
    * @return The character piece at that position or EMPTY_POS if nothing's there.
    *
    */
    char whatsAtPos(BoardPosition pos);

    /**
    * Returns the viable movement directions for each player's pieces on the board
    *
    * @pre None
    *
    * @post getViableDirections =  [a mapping of player characters to their list of viable movement directions]
     *      AND self = #self AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections
    *
    * @return A hashmap where the keys are piece characters and the values are lists of
    *         viable direction enums for that piece.
    */
    HashMap<Character, ArrayList<DirectionEnum>> getViableDirections();

    /**
    * Retrieves a mapping of each player to their respective count of pieces remaining on the board.
    *
    * @pre None
    *
    * @post getPieceCounts = [getPieceCounts HashMap, mapping players to their remaining without modifying it.] AND
    *       self = #self AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections.
    *
    * @return The HashMap containing the amount of pieces each player has on the board.
    *
    */
    HashMap<Character, Integer> getPieceCounts();

      /**
    * Gets the number of rows on the board.
    *
    * @pre None.
    *
    * @post getRowNum = [The current row number of the Checker Board] AND self = #self AND
     *      pieceCounts = #pieceCounts AND viableDirections = #viableDirections
    *         
    * @return The constant ROW_NUM
    *
    */
    public int getRowNum();
    
    /**
    * Gets the number of columns on the board.
    *
    * @pre None.
    *
    * @post getColNum = [Get the current column number of the Checker Board, no data is altered] AND self = #self AND
    *       pieceCounts = #pieceCounts AND viableDirections = #viableDirections
    *
    * @return The constant COL_NUM
    *
    */
    public int getColNum();

    /**
    * Maps a players move to a legal direction.
    *
    * @param player as a character that is representing the player's piece to which will map the direction added.
    * @param dir as a DirectionEnum in which the player's piece is allowed to move during their turn.
    *
    * @pre [player piece is valid and the dir is valid from DirectionEnum].
    *
    * @post addViableDirections = [self with the new direction added to the player's viableDirections]
    *       AND self = #self AND pieceCounts = #pieceCounts AND viableDirections != #viableDirections for player.
    *
    *
    */
    default void addViableDirections(char player, DirectionEnum dir) {
        HashMap<Character, ArrayList<DirectionEnum>> directionsMap = getViableDirections();
        DirectionEnum oppositeDir = null;
    
        switch (dir) {
            case NE:
                oppositeDir = DirectionEnum.SW;
                break;
            case NW: 
                oppositeDir = DirectionEnum.SE;
                break;
            case SE:
                oppositeDir = DirectionEnum.NW;
                break;
            case SW:
                oppositeDir = DirectionEnum.NE;
                break;
        }
    
        directionsMap.computeIfAbsent(player, k -> new ArrayList<>()).add(dir);
    
        char kingPlayer = Character.toUpperCase(player);
        ArrayList<DirectionEnum> kingDirections = directionsMap.computeIfAbsent(kingPlayer, k -> new ArrayList<>());
        kingDirections.add(dir);
        if (oppositeDir != null) {
            kingDirections.add(oppositeDir);
        }

        char emptyPos = EMPTY_POS;
        directionsMap.computeIfAbsent(emptyPos, k -> new ArrayList<>()).add(dir);
    }

    /**
     * Removes pieces from a player's count after it's jumped.
     *
     * @param numPieces as an int, the number of pieces the player has lost. This number must be positive.
     * @param player as a char representing the player whose pieces are being decreased.
     * @param pieceCounts as a HashMap, mapping from player characters to their respective counts of pieces remaining on the board.
     *
     * @pre numPieces > 0 AND pieceCounts.get(player) >= numPieces,
     *      [indicating the player has at least as many pieces as are being removed].
     *
     * @post playerLostPieces = [the piece count for player is reduced by numPieces, but not below 0] AND
     *       self = #self AND pieceCounts = #pieceCounts as it is reduced AND viableDirections = #viableDirections.
     */
    default void playerLostPieces(int numPieces, char player, HashMap<Character, Integer> pieceCounts) {
        if (pieceCounts.containsKey(player)) {
            int currentCount = pieceCounts.get(player);
            pieceCounts.put(player, Math.max(currentCount - numPieces, 0));
        }
    }

    /**
    * Checks to see if the player won the game.
    *
    * @param player as a Character to see whoever won.
    *
    * @pre player is one of the valid player characters.
    *
    * @post checkPlayerWin = [true if opponent has no pieces remaining on the board; false otherwise] AND self = #self AND
    *       pieceCounts = #pieceCounts AND viableDirections = #viableDirections.
    *
    * @return true if the player won the game. The player wins if all the pieces one the board belong to them. False otherwise.
    *
    */
    default boolean checkPlayerWin(Character player) {
        int playerCount = 0;
        int opponentCount = 0;
        
        // Determine the opponent's character
        char opponent = (player == CheckersFE.getPlayerOne()) ? CheckersFE.getPlayerTwo() : CheckersFE.getPlayerOne();
        
        // Convert player and opponent to uppercase to correctly identify kinged pieces
        char playerKing = Character.toUpperCase(player);
        char opponentKing = Character.toUpperCase(opponent);
    
        for (int row = 0; row < getRowNum(); row++) {
            for (int col = 0; col < getColNum(); col++) {
                char piece = whatsAtPos(new BoardPosition(row, col));
                
                // Check for both regular and kinged pieces of the player
                if (piece == player || piece == playerKing) {
                    playerCount++;
                }
                // Check for both regular and kinged pieces of the opponent
                else if (piece == opponent || piece == opponentKing) {
                    opponentCount++;
                }
            }
        }

        // Player wins by eliminating all of the opponent's pieces
        return playerCount > 0 && opponentCount == 0;
    }

    /**
    * Kings a piece when it reaches the opposite end of the board.
    *
    * @param posOfPlayer the board position of the piece to be crowned.
    *
    * @pre posOfPlayer is a valid position within the boundaries and contains one of the players chars.
    *
    * @post crownPiece = [a piece at posOfPlayer is marked at a kinged piece if it reaches the opposing end] AND
    *       self = #self with the piece at posOfPlayer crowned if applicable, AND
    *       pieceCounts = #pieceCounts AND viableDirections = #viableDirections.
    *
    */
    default void crownPiece(BoardPosition posOfPlayer) {
        char currentPiece = whatsAtPos(posOfPlayer);
        if (Character.isLowerCase(currentPiece)) {
            if (currentPiece == CheckersFE.getPlayerOne() && posOfPlayer.getRow() == getRowNum() - 1) {
                placePiece(posOfPlayer, Character.toUpperCase(currentPiece));
            }
            else if (currentPiece == CheckersFE.getPlayerTwo() && posOfPlayer.getRow() == 0) {
                placePiece(posOfPlayer, Character.toUpperCase(currentPiece));
            }
        }
    }

    /**
    * Moves a piece on the board.
    *
    * @param startingPos current position of the piece to be moved.
    * @param dir direction as a DirectionEnum in which to move the piece.
    *
    * @pre A selected position holds a character's piece, and the direction chosen is a valid move.
    *
    * @post movePiece = [moves player's piece in the specified direction, as long as it's a valid direction otherwise stay at the original position]
    *                   AND self = #self AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections.
    *
    * @return The new position of piece after attempting the move, which may be the same as the starting position if the move was not valid.
    *
    */
    default BoardPosition movePiece(BoardPosition startingPos, DirectionEnum dir) {
        BoardPosition direction = getDirection(dir);

        int newRow = startingPos.getRow() + direction.getRow();
        int newCol = startingPos.getColumn() + direction.getColumn();

        if (newRow >= 0 && newRow < getRowNum() && newCol >= 0 && newCol < getColNum()) {
            char piece = whatsAtPos(startingPos);
            
            if (whatsAtPos(new BoardPosition(newRow, newCol)) == EMPTY_POS) {
                placePiece(new BoardPosition(newRow, newCol), piece);
                placePiece(startingPos, EMPTY_POS);
                return new BoardPosition(newRow, newCol);
            }
        }
        return startingPos;
    }

    /**
    * Moves a piece by jumping the opponent's piece.
    *
    * @param startingPos starting position on the board
    * @param dir direction as a DirectionEnum
    *
    * @pre startingPos contains one of the player's pieces and is adjacent to an opponent's in the direction of dir,
    *      with an empty space immediately beyond the opponent's piece in the same direction.
    *
    * @post jumpPiece = [the new position of the piece after a jump is made if jump is valid, otherwise the original position]
    *       AND self = #self with the jumping piece moved and the opponent's piece removed AND pieceCounts = #pieceCounts with the opponents
    *       piece decremented by 1 AND viableDirections = #viableDirections.
    *
    * @return The new position of the piece after the jump, or original position if the jump is invalid.
    *
    */
    default BoardPosition jumpPiece(BoardPosition startingPos, DirectionEnum dir) {
    
        BoardPosition jumpOverPosRel = getDirection(dir); 
    
        int jumpOverRow = startingPos.getRow() + jumpOverPosRel.getRow();
        int jumpOverCol = startingPos.getColumn() + jumpOverPosRel.getColumn();
        BoardPosition jumpOverPos = new BoardPosition(jumpOverRow, jumpOverCol);
    
        int finalRow = jumpOverRow + jumpOverPosRel.getRow();
        int finalCol = jumpOverCol + jumpOverPosRel.getColumn();
        BoardPosition finalPos = new BoardPosition(finalRow, finalCol);
    
        if (finalRow >= 0 && finalRow < getRowNum() && finalCol >= 0 && finalCol < getColNum()) {
            char opponentPiece = whatsAtPos(jumpOverPos);
            char currentPiece = whatsAtPos(startingPos);
    
            // Ensure jumping over an opponent's piece and landing on an empty position
            if (opponentPiece != currentPiece && opponentPiece != EMPTY_POS && whatsAtPos(finalPos) == EMPTY_POS) {
    
                placePiece(finalPos, currentPiece);
                placePiece(jumpOverPos, EMPTY_POS);
                placePiece(startingPos, EMPTY_POS);
                crownPiece(finalPos);

                return finalPos;
            }
        }
        return startingPos;
    }
    
    /**
    * Scans the surrounding area and returns pieces in their given positions and identifies the pieces in each direction
    *
    * @param startingPos a piece's starting position on the board.
    *
    * @pre The specified position, startingPos, is within the boundaries of the board.
    *
    * @post scanSurroundingPositions = [mapping of DirectionEnum to the character representing the piece in that direction from startingPos,
    *       or EMPTY_POS is no piece is present to scan surround positions] AND self = #self AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections.
    *
    * @return A HashMap mapping each DirectionEnum to their char piece in that direction from startingPos 
    *         or EMPTY_POS if the position is unoccupied.
    *
    */
    default HashMap<DirectionEnum, Character> scanSurroundingPositions(BoardPosition startingPos) {
        HashMap<DirectionEnum, Character> surroundingPieces = new HashMap<>();

        DirectionEnum[] directions = {DirectionEnum.NE, DirectionEnum.NW, DirectionEnum.SE, DirectionEnum.SW};

        for (DirectionEnum dir : directions) {
            int newRow = startingPos.getRow() + getDirection(dir).getRow();
            int newCol = startingPos.getColumn() + getDirection(dir).getColumn();

            if (newRow >= 0 && newRow < getRowNum() && newCol >= 0 && newCol < getColNum()) {
                char piece = whatsAtPos(new BoardPosition(newRow, newCol));
                surroundingPieces.put(dir, piece);
            }
            else {
                surroundingPieces.put(dir, EMPTY_POS);
            }
        }
        return surroundingPieces;
    }

    /**
    * Gets a BoardPosition offset for a direction
    *
    * @param dir as a DirectionEnum.
    *
    * @pre dir is one of the valid DirectionEnum values
    *
    * @post getDirection = [a new BoardPosition representing the offset for moving one space in the given direction. dir]
    *
    * @return a BoardPostion representing the offset for moving one space in the given direction specified by dir.
    *
    */
    public static BoardPosition getDirection(DirectionEnum dir) {
        int rowChange = 0;
        int colChange = 0;
    
        switch (dir) {
            case NE:
                rowChange = -1;
                colChange = 1;
                break;
            case NW:
                rowChange = -1;
                colChange = -1;
                break;
            case SE:
                rowChange = 1;
                colChange = 1;
                break;
            case SW:
                rowChange = 1;
                colChange = -1;
                break;
        }
        return new BoardPosition(rowChange, colChange);
    }
}

