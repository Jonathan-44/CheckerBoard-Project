# Checkers


This project should be runnable with JDK17 and Junit 4.

Overview:
This program simulates a game of Checkers on a customizable board. The board can be of different sizes, allowing you to play traditional 8x8 checkers or on larger/smaller boards for a different experience. The program includes functionality to scan the board, place pieces, and enforce the rules of Checkers, such as legal moves and captures.

Features:
Customizable Board: You can create boards of various sizes, from the standard 8x8 to larger or smaller configurations.

Piece Placement: Players can place pieces on the board, following the standard Checkers rules for initial placement and movement.

Surrounding Scan: The program can scan the board around a piece to determine what other pieces are nearby, helping with move decisions.

Legal Move Enforcement: The program checks whether moves are legal based on the position of other pieces on the board.


Setting Up the Board:
Choose the size of the board you'd like to play on.
The board will be initialized with pieces in their standard starting positions for the selected size.

Making Moves:
Players take turns selecting a piece to move. Moves must follow standard Checkers rules.
The program will validate each move to ensure it is legal.

Checking Surroundings:
You can scan the surroundings of any piece to see which other pieces are nearby.
This is useful for strategizing your next move, particularly for making jumps or avoiding captures.