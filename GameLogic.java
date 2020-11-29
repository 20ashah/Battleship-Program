import java.util.ArrayList;
import java.util.Random;
/*
 This class contains the logic of the game. It contains methods for the computer
 to generate guesses and ships in addition to the methods for coding the logic
 of battleship 
*/

public class GameLogic {

	// declare instance variables
	private Board computerBoard; // computer board of ships
	private Board userBoard; // user board of ships
	private int numUserHits; // number of hits the user ships have taken
	private int numCPUHits; // number of hits the computer ships have taken
	private ArrayList<Point> hits; // current hits that the computer has
	private Random r = new Random(); // random object for generating computer guesses and ships
	private String[] directions = { "LEFT", "RIGHT", "UP", "DOWN" }; // different possible directions

	// default constructor for initializing scores and boards
	public GameLogic() {
		numUserHits = 0;
		numCPUHits = 0;
		computerBoard = new Board();
		userBoard = new Board();
		hits = new ArrayList<Point>();
	}

	// adding a computer hit to the array of hits
	public void addHit(Point p) {
		hits.add(p);
	}

	// clearing the array of hits
	public void clearHits() {
		hits.clear();
	}

	// getting the computer board of ships
	public Board getComputerBoard() {
		return computerBoard;
	}

	// getting the user board of ships
	public Board getUserBoard() {
		return userBoard;
	}

	// adding a ship to the user board of ships
	public void addUserShip(Ship s) {
		userBoard.addShip(s);
	}

	// adding a ship to the computer board of ships
	public void addCPUShip(Ship s) {
		computerBoard.addShip(s);
	}

	// generating a computer guess point
	public Point generateGuess() {
		if (hits.size() == 0) { // if computer has no prior hits
			Point p = generatePoint(); // generate random point
			// continue generating random point until an unguessed one comes up
			while (!userBoard.getBoard()[p.getRow()][p.getCol()].isUnguessed()) {
				p = generatePoint();
			}
			return p;
		} else { // if computer has prior hits
			if (hits.size() == 1) { // if computer has only one prior hit
				Point p = hits.get(0); // first computer hit
				// checking the tiles around the first hit and guessing them if unguessed
				if (p.getRow() + 1 < 8 && userBoard.getBoard()[p.getRow() + 1][p.getCol()].isUnguessed()) {
					return new Point(p.getRow() + 1, p.getCol());
				} else if (p.getCol() + 1 < 8 && userBoard.getBoard()[p.getRow()][p.getCol() + 1].isUnguessed()) {
					return new Point(p.getRow(), p.getCol() + 1);
				} else if (p.getRow() - 1 >= 0 && userBoard.getBoard()[p.getRow() - 1][p.getCol()].isUnguessed()) {
					return new Point(p.getRow() - 1, p.getCol());
				} else if (p.getCol() - 1 >= 0 && userBoard.getBoard()[p.getRow()][p.getCol() - 1].isUnguessed()) {
					return new Point(p.getRow(), p.getCol() - 1);
				}
			} else { // if computer has two or more prior hits
				for (int i = 0; i < hits.size(); i++) {
					if (hits.get(0).getRow() == hits.get(1).getRow()) { // if the ship is horizontal
						// guess point on either side of the horizontal ship
						if (hits.get(getMinColIndex()).getCol() - 1 >= 0
								&& userBoard.getBoard()[hits.get(getMinColIndex()).getRow()][hits.get(getMinColIndex())
										.getCol() - 1].isUnguessed()) { // guess point on left side of ship
							return new Point(hits.get(getMinColIndex()).getRow(),
									hits.get(getMinColIndex()).getCol() - 1);
						} else if (hits.get(getMaxColIndex()).getCol() + 1 < 8
								&& userBoard.getBoard()[hits.get(getMaxColIndex()).getRow()][hits.get(getMaxColIndex())
										.getCol() + 1].isUnguessed()) { // guess point on right side of ship
							return new Point(hits.get(getMaxColIndex()).getRow(),
									hits.get(getMaxColIndex()).getCol() + 1);
						}
					} else { // if the ship is vertical
						// guess point on either side of the vertical ship
						if (hits.get(getMinRowIndex()).getRow() - 1 >= 0
								&& userBoard.getBoard()[hits.get(getMinRowIndex()).getRow() - 1][hits
										.get(getMinRowIndex()).getCol()].isUnguessed()) { // guess point on top of ship
							return new Point(hits.get(getMinRowIndex()).getRow() - 1,
									hits.get(getMinRowIndex()).getCol());
						} else if (hits.get(getMaxRowIndex()).getRow() + 1 < 8
								&& userBoard.getBoard()[hits.get(getMaxRowIndex()).getRow() + 1][hits
										.get(getMaxRowIndex()).getCol()].isUnguessed()) { // guess point below ship
							return new Point(hits.get(getMaxRowIndex()).getRow() + 1,
									hits.get(getMaxRowIndex()).getCol());
						}
					}
				}
			}

		}
		// in case there is an error print a random point
		Point p = generatePoint();
		while (!userBoard.getBoard()[p.getRow()][p.getCol()].isUnguessed()) {
			p = generatePoint();
		}
		return p;
	}

	// generate a random computer ship of a given length
	public Ship generateShip(int l) {
		Ship s = new Ship(l);
		// keep generating ship if the ship is invalid
		do {
			Point starting = generatePoint(); // generate random starting position
			String direction = generateDirection(); // generating direction
			s = new Ship(l, direction, starting); // create new ship
		} while (!isValidShip(s));
		return s;
	}

	// helper method for getting the minimum row index in hit array
	private int getMinRowIndex() {
		int minRow = hits.get(0).getRow(); // set minimum to first value
		int index = 0; // minimum index set to 0
		// loop through the hits array
		for (int i = 1; i < hits.size(); i++) {
			// if there is a hit lower than minimum
			if (hits.get(i).getRow() < minRow) {
				// set new minimum and index
				minRow = hits.get(i).getRow();
				index = i;
			}
		}
		return index;
	}

	// helper method for getting the maximum row index in hit array
	private int getMaxRowIndex() {
		int maxRow = hits.get(0).getRow(); // set maximum to first value
		int index = 0; // maximum index set to 0
		// loop through the hits array
		for (int i = 1; i < hits.size(); i++) {
			// if there is a hit higher than maximum
			if (hits.get(i).getRow() > maxRow) {
				// set new maximum and index
				maxRow = hits.get(i).getRow();
				index = i;
			}
		}
		return index;
	}

	// helper method for getting the minimum column index in hit array
	private int getMinColIndex() {
		int minCol = hits.get(0).getCol(); // set minimum to first value
		int index = 0; // minimum index set to 0
		for (int i = 1; i < hits.size(); i++) {
			// if there is a hit lower than minimum
			if (hits.get(i).getCol() < minCol) {
				// set new minimum and index
				minCol = hits.get(i).getCol();
				index = i;
			}
		}
		return index;
	}

	// helper method for getting the maximum column index in hit array
	private int getMaxColIndex() {
		int maxCol = hits.get(0).getCol(); // set maximum to first value
		int index = 0; // maximum index set to 0
		for (int i = 1; i < hits.size(); i++) {
			// if there is a hit higher than maximum
			if (hits.get(i).getCol() > maxCol) {
				// set new maximum and index
				maxCol = hits.get(i).getCol();
				index = i;
			}
		}
		return index;
	}

	// method for checking whether a ship is valid or not
	public boolean isValidShip(Ship s) {
		for (int i = 1; i < s.getLength(); i++) {
			if (s.getDirection().equals("UP")) { // if ship is facing up
				if (s.getStartPosRow() - i >= 0) {
					// make sure the tiles up from start are empty
					int rid = s.getStartPosRow() - i;
					int cid = s.getStartPosCol();
					Square sq = computerBoard.getBoard()[rid][cid];
					if (sq.hasShip()) {
						return false;
					}
				} else {
					return false;
				}
			} else if (s.getDirection().equals("DOWN")) { // if ship is facing down
				if (s.getStartPosRow() + i < 8) {
					// make sure the tiles down from start are empty
					int rid = s.getStartPosRow() + i;
					int cid = s.getStartPosCol();
					Square sq = computerBoard.getBoard()[rid][cid];
					if (sq.hasShip()) {
						return false;
					}
				} else {
					return false;
				}
			} else if (s.getDirection().equals("RIGHT")) { // if ship is facing right
				if (s.getStartPosCol() + i < 8) {
					// make sure the tiles right from start are empty
					int rid = s.getStartPosRow();
					int cid = s.getStartPosCol() + i;
					Square sq = computerBoard.getBoard()[rid][cid];
					if (sq.hasShip()) {
						return false;
					}
				} else {
					return false;
				}
			} else { // if ship is facing left
				if (s.getStartPosCol() - i >= 0) {
					// make sure the tiles left from start are empty
					int rid = s.getStartPosRow();
					int cid = s.getStartPosCol() - i;
					Square sq = computerBoard.getBoard()[rid][cid];
					if (sq.hasShip()) {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		// checking if the ship is touching other ships
		int r = s.getStartPosRow(); // starting row of ship
		int c = s.getStartPosCol(); // starting column of ship
		// checking every tile around the ship
		for (int i = 0; i < s.getLength(); i++) {
			// checking above the tile for another ship
			if (r - 1 >= 0 && computerBoard.getBoard()[r - 1][c].hasShip()
					&& computerBoard.getBoard()[r - 1][c].getLength() != s.getLength()) {
				return false;
			}
			// checking right of the tile for another ship
			if (c + 1 < 8 && computerBoard.getBoard()[r][c + 1].hasShip()
					&& computerBoard.getBoard()[r][c + 1].getLength() != s.getLength()) {
				return false;
			}
			// checking below the tile for another ship
			if (r + 1 < 8 && computerBoard.getBoard()[r + 1][c].hasShip()
					&& computerBoard.getBoard()[r + 1][c].getLength() != s.getLength()) {
				return false;
			}
			// checking left of the tile for another ship
			if (c - 1 >= 0 && computerBoard.getBoard()[r][c - 1].hasShip()
					&& computerBoard.getBoard()[r][c - 1].getLength() != s.getLength()) {
				return false;
			}
			// updating the tile
			if (s.getDirection().equals("UP")) { // move up
				r -= 1;
			} else if (s.getDirection().equals("DOWN")) { // move down
				r += 1;
			} else if (s.getDirection().equals("RIGHT")) { // move right
				c += 1;
			} else { // move left
				c -= 1;
			}
		}

		return true;
	}

	// for generating a random point
	private Point generatePoint() {
		// generating new row and column 0-7
		int row = r.nextInt(8);
		int col = r.nextInt(8);
		return new Point(row, col);
	}

	// for generating a random direction
	private String generateDirection() {
		// generating random value in direction array
		int number = r.nextInt(4);
		return directions[number];

	}

	// incrementing hits on user ships
	public void incrementUserHits() {
		numUserHits++;
	}

	// incrementing hits on computer ships
	public void incrementCPUHits() {
		numCPUHits++;
	}

	// checking whether the user has lost
	public boolean hasUserLost() {
		// if all the user ships have been hit
		if (numUserHits == 14) {
			return true;
		}
		return false;
	}

	// checking whether the computer has lost
	public boolean hasCPULost() {
		// if all the computer ships have been hit
		if (numCPUHits == 14) {
			return true;
		}
		return false;
	}

}
