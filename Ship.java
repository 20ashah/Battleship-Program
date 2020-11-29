/*
 This class models a ship object that contains a length, 
 direction, starting position, and number of hits on it.
 This class contains methods to set and get the characteristics 
 of a ship object
*/

public class Ship {

	// declare private instance variables
	private int length; // length of the ship
	private String direction; // direction the ship is going
	private Point startingPos; // where the ship starts
	private int numHits; // number of hits that the ship has taken

	// constructor for making a ship
	public Ship(int l) {
		length = l;
		numHits = 0;
		direction = "";
		startingPos = new Point(-1, -1);
	}

	// constructor for making ship with all characteristics
	public Ship(int l, String d, Point s) {
		length = l;
		direction = d;
		startingPos = s;
		numHits = 0;
	}

	// returns the length of the ship
	public int getLength() {
		return length;
	}

	// returns the direction of the ship
	public String getDirection() {
		return direction;
	}

	// returns the starting row of ship
	public int getStartPosRow() {
		return startingPos.getRow();
	}

	// returns the starting column of ship
	public int getStartPosCol() {
		return startingPos.getCol();
	}

	// incrementing the number of hits onto the ship
	public void incrementHits() {
		numHits++;
	}

	// checks if the ship is sunk or not
	public boolean isSunk() {
		// if all spots are hit then ship is sunk
		if (numHits == length) {
			return true;
		}
		return false;
	}

}
