/*
  This class models a generic board. It contains a 2-D array
  of square tiles. This class contains methods for adding a 
  ship to the board and checking if guess is a hit.
*/

public class Board {

	// declare instance variables
	private Square[][] board; // 2-D array of square tiles

	// default constructor for initializing the board array
	public Board() {
		board = new Square[8][8];
		// initializing each square tile in board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new Square();
			}
		}
	}

	// returning the board
	public Square[][] getBoard() {
		return board;
	}

	// adding a ship to the board
	public void addShip(Ship s) {
		// adding first point in the ship to the board
		board[s.getStartPosRow()][s.getStartPosCol()].addShip();
		board[s.getStartPosRow()][s.getStartPosCol()].setLength(s.getLength());
		// adding rest of the ship
		if (s.getDirection().equals("UP")) { // if boat was facing up direction
			for (int i = 1; i < s.getLength(); i++) {
				// adding the tiles above starting position
				board[s.getStartPosRow() - i][s.getStartPosCol()].addShip();
				board[s.getStartPosRow() - i][s.getStartPosCol()].setLength(s.getLength());
			}
		} else if (s.getDirection().equals("DOWN")) { // if boat was facing down direction
			for (int i = 1; i < s.getLength(); i++) {
				// adding the tiles below starting position
				board[s.getStartPosRow() + i][s.getStartPosCol()].addShip();
				board[s.getStartPosRow() + i][s.getStartPosCol()].setLength(s.getLength());
			}
		} else if (s.getDirection().equals("RIGHT")) { // if boat was facing right direction
			for (int i = 1; i < s.getLength(); i++) {
				// adding the tiles to the right of starting position
				board[s.getStartPosRow()][s.getStartPosCol() + i].addShip();
				board[s.getStartPosRow()][s.getStartPosCol() + i].setLength(s.getLength());
			}
		} else if (s.getDirection().equals("LEFT")) { // if boat was facing left direction
			for (int i = 1; i < s.getLength(); i++) {
				// adding the tiles to the left of starting position
				board[s.getStartPosRow()][s.getStartPosCol() - i].addShip();
				board[s.getStartPosRow()][s.getStartPosCol() - i].setLength(s.getLength());
			}
		}
	}

	// checking if a guess is a hit on a ship
	public boolean isHit(Point p) {
		if (board[p.getRow()][p.getCol()].hasShip()) { // if the position has a ship
			// mark tile as a hit if there is a ship there
			board[p.getRow()][p.getCol()].markHit();
			return true;
		} else {
			// mark tile as a miss if there isn't a ship there
			board[p.getRow()][p.getCol()].markMissed();
		}
		return false;
	}

}
