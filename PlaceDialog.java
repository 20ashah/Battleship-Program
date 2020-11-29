import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import BreezySwing.GBDialog;
/*
 This class is the dialog box for the user to place their ships. Once
 the user finishes placing their ships this dialog box will close and 
 return to the main GUI 
*/

public class PlaceDialog extends GBDialog {

	// declare GUI components
	private JCheckBox[][] playerButton = new JCheckBox[8][8];
	private JButton finish;
	private JButton destroyer;
	private JButton carrier;
	private JButton battleship;
	private JButton cruiser;
	private GameLogic gameLogic;

	// user and computer ships
	protected Ship userDest = new Ship(2);
	protected Ship userCruis = new Ship(3);
	protected Ship userBatt = new Ship(4);
	protected Ship userCarr = new Ship(5);
	protected Ship compDest = new Ship(2);
	protected Ship compCruis = new Ship(3);
	protected Ship compBatt = new Ship(4);
	protected Ship compCarr = new Ship(5);

	// constructor for opening dialog box and placing components
	public PlaceDialog(JFrame parent, GameLogic g) {
		super(parent);
		// add the board for user to place ships on
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				playerButton[i][j] = addCheckBox("", i + 1, j + 1, 1, 1);
				playerButton[i][j].setVisible(true);
			}
		}

		finish = addButton("Finish", 10, 9, 1, 1);
		destroyer = addButton("Add destroyer(2 spaces)", 10, 9, 1, 1);
		cruiser = addButton("Add cruiser(3 spaces)", 10, 9, 1, 1);
		battleship = addButton("Add battleship(4 spaces)", 10, 9, 1, 1);
		carrier = addButton("Add carrier(5 spaces)", 10, 9, 1, 1);
		destroyer.setVisible(true);
		cruiser.setVisible(false);
		battleship.setVisible(false);
		carrier.setVisible(false);
		finish.setVisible(false);
		gameLogic = g;

		// setting characteristics for GUI
		setTitle("Place Ships");
		setDlgCloseIndicator("Cancel");
		setSize(400, 500);
		setVisible(true);

	}

	// method for performing actions when buttons are clicked
	public void buttonClicked(JButton button) {
		if (destroyer == button) { // if user clicks the destroyer button
			if (isValidPlacement(2)) { // check if user ship is valid
				// make new ship button visible if ship is valid
				destroyer.setVisible(false);
				cruiser.setVisible(true);
				battleship.setVisible(false);
				carrier.setVisible(false);
				userDest = retrieveShip(2);
				// add and mark ship
				gameLogic.addUserShip(userDest);
				markShip();
			} else { // throw error if invalid
				messageBox("Invalid Ship Placement!");
			}

		}
		if (cruiser == button) { // if user clicks the cruiser button
			if (isValidPlacement(3)) { // check if user ship is valid
				// make new ship buttons visible if ship is valid
				destroyer.setVisible(false);
				cruiser.setVisible(false);
				battleship.setVisible(true);
				carrier.setVisible(false);
				userCruis = retrieveShip(3);
				// add and mark ship
				gameLogic.addUserShip(userCruis);
				markShip();
			} else { // throw error if invalid
				messageBox("Invalid Ship Placement!");
			}
		}
		if (battleship == button) { // if user clicks the battleship button
			if (isValidPlacement(4)) { // check if user ship is valid
				// make new ship buttons visible if ship is valid
				destroyer.setVisible(false);
				cruiser.setVisible(false);
				battleship.setVisible(false);
				carrier.setVisible(true);
				userBatt = retrieveShip(4);
				// add and mark ship
				gameLogic.addUserShip(userBatt);
				markShip();
			} else { // throw error if invalid
				messageBox("Invalid Ship Placement!");
			}
		}
		if (carrier == button) { // if user clicks the carrier button
			if (isValidPlacement(5)) { // check if user ship is valid
				destroyer.setVisible(false);
				cruiser.setVisible(false);
				battleship.setVisible(false);
				carrier.setVisible(false);
				finish.setVisible(true);
				userCarr = retrieveShip(5);
				// add and mark ship
				gameLogic.addUserShip(userCarr);
				markShip();
			} else { // throw error if invalid
				messageBox("Invalid Ship Placement!");
			}
		}
		if (finish == button) { // if user is done adding ships
			// generate and add all computer ships
			compDest = gameLogic.generateShip(2);
			gameLogic.addCPUShip(compDest);
			compCruis = gameLogic.generateShip(3);
			gameLogic.addCPUShip(compCruis);
			compBatt = gameLogic.generateShip(4);
			gameLogic.addCPUShip(compBatt);
			compCarr = gameLogic.generateShip(5);
			gameLogic.addCPUShip(compCarr);
			dispose(); // close dialog box
		}
	}

	// method for retrieving ship from the board
	public Ship retrieveShip(int length) {
		String dd = "";
		int startr = 0;
		int startc = 0;
		// loop through the whole board of buttons
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// finds the first selected box from board
				if (playerButton[i][j].isSelected()) {
					// set the start coordinates
					startr = i;
					startc = j;
					// if there are boxes selected to the right set direction to right
					if (j + 1 != 8 && playerButton[i][j + 1].isSelected()) {
						dd = "RIGHT";
						// if there are boxes selected to the left set direction to left
					} else if (j - 1 != -1 && playerButton[i][j - 1].isSelected()) {
						dd = "LEFT";
						// if there are boxes selected down set direction to down
					} else if (i + 1 != 8 && playerButton[i + 1][j].isSelected()) {
						dd = "DOWN";
						// if there are boxes selected up set direction to up
					} else if (i - 1 != -1 && playerButton[i - 1][j].isSelected()) {
						dd = "UP";
					}
					break;
				}
			}
		}
		// create and return ship with these characteristics
		return new Ship(length, dd, new Point(startr, startc));
	}

	// checking if the user's placement of ships is valid
	public boolean isValidPlacement(int length) {
		int count = 0; // number of selected boxes
		int row = 0; // starting row
		int col = 0; // starting column
		// loop through the board
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (playerButton[r][c].isSelected()) {
					// set the row and column of start
					row = r;
					col = c;
					count++; // increment number of shots
				}
			}
		}

		if (count == length) { // number of shots has to equal length
			// loop through the rest of selected boxes
			for (int i = 1; i < length; i++) {
				// checking if all the selected boxes are in a line
				if ((row - i < 0 || !playerButton[row - i][col].isSelected())
						&& (row + i > 7 || !playerButton[row + i][col].isSelected())
						&& (col + i > 7 || !playerButton[row][col + i].isSelected())
						&& (col - i < 0 || !playerButton[row][col - i].isSelected())) {
					return false;
				}
			}
		} else {
			return false;
		}

		// checking if boat doesn't touch any other boat
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (playerButton[r][c].isSelected()) {
					// if boat touches another boat above it
					if (r - 1 >= 0 && gameLogic.getUserBoard().getBoard()[r - 1][c].hasShip()
							&& gameLogic.getUserBoard().getBoard()[r - 1][c].getLength() != length) {
						// ship of different length above it
						return false;
					}
					// if boat touches another boat below it
					if (r + 1 < 8 && gameLogic.getUserBoard().getBoard()[r + 1][c].hasShip()
							&& gameLogic.getUserBoard().getBoard()[r + 1][c].getLength() != length) {
						// ship of different length below it
						return false;
					}
					// if boat touches another boat to the left of it
					if (c - 1 >= 0 && gameLogic.getUserBoard().getBoard()[r][c - 1].hasShip()
							&& gameLogic.getUserBoard().getBoard()[r][c - 1].getLength() != length) {
						// ship of different length to the left of it
						return false;
					}
					// if boat touches another boat to the right of it
					if (c + 1 < 8 && gameLogic.getUserBoard().getBoard()[r][c + 1].hasShip()
							&& gameLogic.getUserBoard().getBoard()[r][c + 1].getLength() != length) {
						// ship of different length to the right of it
						return false;
					}
				}
			}
		}
		return true;
	}

	// marking a ship on the grid of buttons
	public void markShip() {
		// looping through the entire button array
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (playerButton[i][j].isSelected()) { // if button is selected
					// remove selection on the button and make it black
					playerButton[i][j].setSelected(false);
					playerButton[i][j].setEnabled(false);
					playerButton[i][j].setBackground(Color.BLACK);
				}
			}
		}
	}

}
