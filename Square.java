/*
 This class models a square tile class that contains
 the status for that tile on the board.  It also 
 contains methods to get and set the status of the
 tile object
*/

public class Square {

	// constants for the different options for status
	private static final int MISSED = -1;
	private static final int UNGUESSED = 0;
	private static final int HIT = 1;

	// declare instance variables
	private int status; // status of the tile
	private boolean hasShip; // if the tile has a ship on it
	private int length; // length of the ship on the tile

	// default constructor for setting default status for the tile
	public Square() {
		status = UNGUESSED;
		hasShip = false;
		length = 0;
	}

	// setting the length of the ship on the tile
	public void setLength(int l) {
		length = l;
	}

	// getting the length of the ship on the tile
	public int getLength() {
		return length;
	}

	// checking whether the tile has been guessed or not
	public boolean isUnguessed() {
		// if the status is unguessed then it hasn't been guessed
		if (status == UNGUESSED) {
			return true;
		}
		return false;
	}

	// checking whether the tile has a ship on it
	public boolean hasShip() {
		return hasShip;
	}

	// marking the tile with hit status
	public void markHit() {
		status = HIT;
	}

	// marking the tile with miss status
	public void markMissed() {
		status = MISSED;
	}

	// change status of tile to has a ship
	public void addShip() {
		hasShip = true;
	}

}
