import java.awt.Color;
/*
 Arjun Shah
 6/12/19
 E Block
 Program Description: This program codes the game of Battleship. In this game,
 the user places their ships and the computer randomly generates its own ships.
 From there, the user and computer guess where the other person's ships are and 
 once all the ships have been sunk the other person wins
*/

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import BreezySwing.GBFrame;

public class BattleshipGUI extends GBFrame {

	// declare GUI components
	private JCheckBox[][] playersButton = new JCheckBox[8][8]; // player board
	private JCheckBox[][] cpuButton = new JCheckBox[8][8]; // computer board
	private JLabel splitter; // divider line
	private JLabel player;
	private JButton placing;
	private JButton shoot; // button to shoot

	private GameLogic gameLogic;
	private PlaceDialog p;

	// default constructor for adding GUI components to window
	public BattleshipGUI() {
		player = addLabel("You", 10, 1, 1, 1);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				playersButton[i][j] = addCheckBox("", i + 11, j + 1, 1, 1);
			}
		}
		splitter = addLabel("CPU", 1, 1, 1, 1);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				cpuButton[i][j] = addCheckBox("", i + 2, j + 1, 1, 1);
			}
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				playersButton[i][j].setVisible(false);
				cpuButton[i][j].setVisible(false);
			}
		}

		placing = addButton("Click to set up your board", 1, 1, 1, 1);
		shoot = addButton("Shoot", 20, 11, 1, 1);

		// setting restrictions on components
		player.setVisible(false);
		splitter.setVisible(false);
		shoot.setVisible(false);

		gameLogic = new GameLogic();
		// rules of the game
		messageBox(
				"Rules for Battleship:\n" + "- You will place your ships (lengths 2,3,4,5) on your board to start\n"
						+ "- No two ships may touch each other!\n"
						+ "- You will start by guessing any location to shoot a missile\n"
						+ "- Once a ship is sunk, you will be notified\n"
						+ "- Whoever sinks all of the other person's ships first wins!\n\n" + "			Good Luck!!!",
				500, 250);

	}

	// method for performing actions when buttons are clicked
	public void buttonClicked(JButton button) {
		if (button == placing) { // if user clicks the placing ships button
			p = new PlaceDialog(this, gameLogic); // open dialog box for placing ships

			// make the board visible when dialog box closes
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					playersButton[i][j].setVisible(true);
					playersButton[i][j].setEnabled(false);
					cpuButton[i][j].setVisible(true);
				}
			}
			// hiding placing button and making others visible
			player.setVisible(true);
			splitter.setVisible(true);
			placing.setVisible(false);
			messageBox("Click ONE box and click the button to shoot.\nYellow = Sunk, Blue = Miss, Red = Hit", 450, 150);
			shoot.setVisible(true);

			// adding ships to the GUI check box array
			Board userBoard = gameLogic.getUserBoard();
			for (int r = 0; r < 8; r++) {
				for (int c = 0; c < 8; c++) {
					// sets all tiles with a ship to black
					if (userBoard.getBoard()[r][c].hasShip()) {
						playersButton[r][c].setBackground(Color.BLACK);
					}
				}
			}

		} else if (button == shoot) { // if the user clicks the shoot button
			if (isValidShot()) { // makes sure user's shot is valid
				for (int r = 0; r < 8; r++) {
					for (int c = 0; c < 8; c++) {
						if (cpuButton[r][c].isSelected()) { // finds the user's shot
							// disables that spot so user can't shoot again
							cpuButton[r][c].setEnabled(false);
							cpuButton[r][c].setSelected(false);
							if (gameLogic.getComputerBoard().isHit(new Point(r, c))) { // if shot is hit
								cpuButton[r][c].setBackground(Color.RED); // set tile to red
								gameLogic.incrementCPUHits(); // increment computer ship hits
								// if user hit a 2 tiled ship
								if (gameLogic.getComputerBoard().getBoard()[r][c].getLength() == 2) {
									p.compDest.incrementHits(); // increment hits on destroyer
									if (p.compDest.isSunk()) { // if user sunk ship
										messageBox("Computer Destroyer is Sunk");
										markSunked(p.compDest, cpuButton); // mark ship as sunk
									}
									// if user hit a 3 tiled ship
								} else if (gameLogic.getComputerBoard().getBoard()[r][c].getLength() == 3) {
									p.compCruis.incrementHits(); // increment hits on cruiser
									if (p.compCruis.isSunk()) { // if user sunk ship
										messageBox("Computer Cruiser is Sunk");
										markSunked(p.compCruis, cpuButton); // mark ship as sunk
									}
									// if user hit a 4 tiled ship
								} else if (gameLogic.getComputerBoard().getBoard()[r][c].getLength() == 4) {
									p.compBatt.incrementHits(); // increment hits on battleship
									if (p.compBatt.isSunk()) { // if user sunk ship
										messageBox("Computer Battleship is Sunk");
										markSunked(p.compBatt, cpuButton); // mark ship as sunk
									}
									// if user hit a 5 tiled ship
								} else if (gameLogic.getComputerBoard().getBoard()[r][c].getLength() == 5) {
									p.compCarr.incrementHits(); // increment hits on carrier
									if (p.compCarr.isSunk()) { // if user sunk ship
										messageBox("Computer Carrier is Sunk");
										markSunked(p.compCarr, cpuButton); // mark ship as sunk
									}
								}
							} else { // if shot is a miss
								cpuButton[r][c].setBackground(Color.BLUE); // mark that tile as blue
							}
						}
					}
				}
				// checking if computer has lost
				if (gameLogic.hasCPULost()) {
					messageBox("You win!!");
					shoot.setEnabled(false);
				} else { // game continues
					Point compGuess = gameLogic.generateGuess(); // computer generates a guess
					if (gameLogic.getUserBoard().isHit(compGuess)) { // if computer gets a hit
						gameLogic.addHit(compGuess); // add hit to user tracker
						playersButton[compGuess.getRow()][compGuess.getCol()].setBackground(Color.RED); // tile to red
						gameLogic.incrementUserHits(); // increment hits on user
						// if computer hits a 2 tiled ship
						if (gameLogic.getUserBoard().getBoard()[compGuess.getRow()][compGuess.getCol()]
								.getLength() == 2) {
							p.userDest.incrementHits(); // increment hits on destroyer
							if (p.userDest.isSunk()) { // if computer sunk ship
								messageBox("Your Destroyer is Sunk");
								markSunked(p.userDest, playersButton); // mark ship as sunk
								gameLogic.clearHits(); // clear tracker
							}
							// if computer hits a 3 tiled ship
						} else if (gameLogic.getUserBoard().getBoard()[compGuess.getRow()][compGuess.getCol()]
								.getLength() == 3) {
							p.userCruis.incrementHits(); // increment hits on cruiser
							if (p.userCruis.isSunk()) { // if computer sunk ship
								messageBox("Your Cruiser is Sunk");
								markSunked(p.userCruis, playersButton); // mark ship as sunk
								gameLogic.clearHits(); // clear tracker
							}
							// if computer hits a 4 tiled ship
						} else if (gameLogic.getUserBoard().getBoard()[compGuess.getRow()][compGuess.getCol()]
								.getLength() == 4) {
							p.userBatt.incrementHits(); // increment hits on battleship
							if (p.userBatt.isSunk()) { // if computer sunk ship
								messageBox("Your Battleship is Sunk");
								markSunked(p.userBatt, playersButton); // mark ship as sunk
								gameLogic.clearHits(); // clear tracker
							}
							// if computer hits a 5 tiled ship
						} else if (gameLogic.getUserBoard().getBoard()[compGuess.getRow()][compGuess.getCol()]
								.getLength() == 5) {
							p.userCarr.incrementHits(); // increments hits on carrier
							if (p.userCarr.isSunk()) { // if computer sunk ship
								messageBox("Your Carrier is Sunk");
								markSunked(p.userCarr, playersButton); // mark ship as sunk
								gameLogic.clearHits(); // clear tracker
							}
						}
					} else { // if computer misses
						playersButton[compGuess.getRow()][compGuess.getCol()].setBackground(Color.BLUE); // tile to blue
					}

					// checking if user has lost
					if (gameLogic.hasUserLost()) {
						messageBox("You lose!");
						revealComputerShips(); // show computer ships
						shoot.setEnabled(false);
					}
				}

			} else { // if user shot is invalid
				messageBox("Invalid Shot. Press ONE square to shoot.");
			}
		}
	}

	// marking sunk ship on the board
	public void markSunked(Ship s, JCheckBox[][] board) {
		// set first point of ship to yellow
		board[s.getStartPosRow()][s.getStartPosCol()].setBackground(Color.YELLOW);
		// set rest of the ship to yellow
		if (s.getDirection().equals("UP")) { // if ship is going up
			// make tiles above start yellow
			for (int i = 1; i < s.getLength(); i++) {
				board[s.getStartPosRow() - i][s.getStartPosCol()].setBackground(Color.YELLOW);
			}
		} else if (s.getDirection().equals("DOWN")) { // if ship is going down
			// make tiles below start yellow
			for (int i = 1; i < s.getLength(); i++) {
				board[s.getStartPosRow() + i][s.getStartPosCol()].setBackground(Color.YELLOW);
			}
		} else if (s.getDirection().equals("RIGHT")) { // if ship is going right
			// make tiles right of start yellow
			for (int i = 1; i < s.getLength(); i++) {
				board[s.getStartPosRow()][s.getStartPosCol() + i].setBackground(Color.YELLOW);
			}
		} else if (s.getDirection().equals("LEFT")) { // if ship is going left
			// make tiles left of start yellow
			for (int i = 1; i < s.getLength(); i++) {
				board[s.getStartPosRow()][s.getStartPosCol() - i].setBackground(Color.YELLOW);
			}
		}
	}

	// checking if the user's shot is valid
	public boolean isValidShot() {
		int count = 0; // number of buttons user clicked
		// looping through every check box
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				// increment count for every selected box
				if (cpuButton[r][c].isSelected()) {
					count++;
				}
			}
		}
		// if number of selects is 1 then it is valid
		if (count == 1) {
			return true;
		}
		return false;
	}

	// revealing all of the computer's ships
	public void revealComputerShips() {
		Square[][] compShips = gameLogic.getComputerBoard().getBoard();
		// looping through the computer button array
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				// mark every unguessed tile with a computer ship black
				if (compShips[r][c].isUnguessed() && compShips[r][c].hasShip()) {
					cpuButton[r][c].setEnabled(false);
					cpuButton[r][c].setBackground(Color.BLACK);
				}
			}
		}
	}

	// main method
	public static void main(String[] args) {
		BattleshipGUI theGUI = new BattleshipGUI(); // make new window
		theGUI.setSize(500, 500); // set size of window
		theGUI.setVisible(true); // set visibility of window
		theGUI.setTitle("Battleship"); // set title of window
	}
}
