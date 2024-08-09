package cpsc2150.extendedCheckers.models;

    /**
     * Returns a string representation of the checkerboard.
     *
     * @pre None.
     *
     * @post toString = [a string representation of the checkerboard with all pieces on it in their current position]
     *       AND board = #board AND pieceCounts = #pieceCounts AND viableDirections = #viableDirections
     *
     * @return [A string with multiple lines. The first line wiill have the column numbers. The first column (on the left) will contain
     * the row numbers. The ramining spaces will contain the state of the board (pieces represented by their character and empty spaces).
     * This dosen't print or output the string.]
     */

    public abstract class AbsCheckerBoard implements ICheckerBoard {

    @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("|  ");
            for (int col = 0; col < getColNum(); col++) {
                if (col < 10) {
                    sb.append("| ").append(col);
                }
                else {
                    sb.append("|").append(col);
                }
            }
            sb.append("|\n");

        for (int row = 0; row < getRowNum(); row++) {
            if (row < 10) {
                sb.append("|" + row + " ");
            }
            else {
                sb.append("|" + row);
            }
            for (int col = 0; col < getColNum(); col++) {
                BoardPosition pos = new BoardPosition(row, col);
                sb.append("|");
                sb.append(whatsAtPos(pos)).append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}

