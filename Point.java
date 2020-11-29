/*
 This class models a point for a position
 on the board. It contains a row and a column
 coordinate to correspond with a point on the board
*/

public class Point {

	// declare instance variables
	private int row; // row coordinate
	private int col; // column coordinate

	// constructor for creating a point object
	public Point(int r, int c) {
		this.row = r;
		this.col = c;
	}

	// returning the row coordinate
	public int getRow() {
		return row;
	}

	// returning the column coordinate
	public int getCol() {
		return col;
	}

}
