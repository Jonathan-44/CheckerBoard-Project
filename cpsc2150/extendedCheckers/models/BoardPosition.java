package cpsc2150.extendedCheckers.models;
/**
 * Represents player position on a game board
 * This class provides methods for accessing rows and columns on a game board
 */
public class BoardPosition{
    public static final int doubleValue = 2;
    
    
    /**
     * Row component of the BoardPosition
     */
    private int row;

    /**
     * Column component of the BoardPosition
     */
    private int column;

    /**
     * Constructor for the BoardPosition object. This should set both the row and column instance variables to their default size.
     *
     * @param aRow the row of the BoardPosition, where 0 <= aRow < CheckerBoard.ROW_NUM
     * @param aCol the column of the BoardPosition, where 0 <= aCol < CheckerBoard.COL_NUM
     *
     * @pre 0 <= aRow < CheckerBoard.ROW_NUM AND 0 <= aCol < CheckerBoard.COL_NUM
     * @post row = aRow AND column = aCol
     */
    public BoardPosition(int aRow, int aCol) {
        this.row = aRow;
        this.column = aCol;
    }

    /**
    * Gets the row value of BoardPosition.
    *
    * @pre none.
    *
    * @return the row value of this BoardPosition
    *
    * @post getRow = [row value of this BoardPosition, ensures the row value is unchanged]
    * AND row = #row AND column = #column
    */
    public int getRow() {
        return row;
    }

    /**
    * Gets the column value of BoardPosition.
    *
    * @pre none.
    *
    * @return the column value of this BoardPosition.
    *
    * @post getColumn = [column value of this BoardPosition, ensures the column value is unchanged]
    * AND row = #row AND column = #column
    */
    public int getColumn() {
        return column;
    }
    
    /**
    * Adds two BoardPosition objects together.
    *
    * @pre none.
    *
    * @param posOne The first BoardPosition.
    * @param posTwo The second BoardPosition.
    *
    * @return A new BoardPosition with row and column values being the sum of posOne and posTwo's row and column values.
    *
    * @post add = [BoardPosition with row and column values that are the sums of rows and columns of posOne and posTwo.]
    */
    public static BoardPosition add(BoardPosition posOne, BoardPosition posTwo) {
        int newRow = posOne.getRow() + posTwo.getRow();
        int newCol = posOne.getColumn() + posTwo.getColumn();
        return new BoardPosition(newRow, newCol);

    }
    
    /**
     * Creates a new BoardPosition with both row and column values being double of the specified BoardPosition.
     *
     * @pre none.
     *
     * @param pos The BoardPosition whose row and column are to be doubled.
     *
     * @return A BoardPosition instance where both the row and column are twice as large as those in the given pos.
     *
     * @post doubleBoardPosition = [BoardPosition where row and column are twice the values of pos's row and column.]
     *
     */
    public static BoardPosition doubleBoardPosition(BoardPosition pos) {
        int newRow = pos.getRow() * doubleValue;
        int newCol = pos.getColumn() * doubleValue;
        return new BoardPosition(newRow, newCol);
    }

    /**
     * Checks if this BoardPosition is within bounds of the board.
     *
     * @pre none.
     *
     * @param rowBound The row boundary of the board.
     * @param columnBound The column boundary of the board.
     *
     * @return true if this BoardPosition is within the bounds (0 <= row < rowBound and 0 <= column < columnBound), false otherwise.
     *
     * @post isValid = [TRUE if this BoardPosition is within the bounds of 0 and rowBound and columnBound Return FALSE otherwise.]
     * AND row = #row AND column = #column
     */
    public boolean isValid(int rowBound, int columnBound) {
        boolean rowValid = row >= 0 && row < rowBound;
        boolean columnValid = column >= 0 && column < columnBound;
        return rowValid && columnValid;
    }

    /**
     * Checks if this BoardPosition is equal to another object.
     *
     * @pre none.
     *
     * @param obj The object to compare with.
     *
     * @return true if obj is a BoardPosition with the same row and column as this BoardPosition, false otherwise.
     *
     * @post equals = [TRUE if this BoardPosition == Object obj, two BoardPositions are equal if row and column values are the same.]
     * AND row = #row AND column = #column
     */
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        BoardPosition other = (BoardPosition) obj;
        return row == other.row && column == other.column;
    }

    /**
     * Returns a String representation of the BoardPosition.
     *
     * @pre none
     *
     * @return A String in the format "row,column".
     *
     * @post BoardPosition = [A string representing the current state of BoardPosition in "row, column" format without changing the state.]
     * AND row = #row AND column = #column
     */
    public String toString() {
        return row + "," + column;
    }
}
