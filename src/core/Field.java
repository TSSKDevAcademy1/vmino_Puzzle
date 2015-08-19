package core;

import java.io.Serializable;
import java.util.Formatter;
import java.util.Random;

public class Field implements Serializable {

	private Tile[][] tiles;
	private final int rowCount;
	private final int columnCount;
	private int numberCount;
	private int[] numbers;
	private GameState gameState;

	public enum GameState {
		SOLVED, UNSOLVED
	}

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	/**
	 * Constructor for game field, builds new field and generates numbers
	 * 
	 * @param rowCount
	 * @param columnCount
	 */
	public Field(int rowCount, int columnCount) {
		super();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.tiles = new Tile[rowCount][columnCount];
		generate();
		gameState = GameState.UNSOLVED;
	}

	/**
	 * Generates numbers into field
	 */
	private void generate() {
		setNumbers();
		int randomNumber = 0;
		int maxNumber = rowCount * columnCount;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				randomNumber = getNumber();
				if (randomNumber == maxNumber) {
					tiles[row][column] = new Space();
				} else {
					tiles[row][column] = new Stone(randomNumber);
				}
			}
		}
	}

	/**
	 * Sets numbers into numbers array
	 */
	private void setNumbers() {
		this.numberCount = (rowCount * columnCount); // - 1;
		this.numbers = new int[this.numberCount];
		for (int i = 0; i < numbers.length; i++) {
			this.numbers[i] = i + 1;
		}
	}

	/**
	 * Returns number from numbers array and deletes this number form array
	 * 
	 * @return
	 */
	private int getNumber() {
		int[] numbers = this.numbers;
		Random random = new Random();
		int index = 0;
		int chosenNumber = 0;
		int chosenIndex = random.nextInt(this.numberCount);
		for (int i = 0; i < this.numberCount; i++) {
			if ((i) == chosenIndex) {
				chosenNumber = numbers[i];
				index = i;
			}
		}
		for (int i = index; i < this.numberCount - 1; i++) {
			numbers[i] = numbers[i + 1];
		}
		this.numberCount--;
		return chosenNumber;
	}

	/**
	 * Finds space in field and returns as Tile
	 * 
	 * @return
	 */
	public Tile getSpace() { // viem, toto by trebalo opravit 
		Tile tile = null;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				if (this.tiles[row][column] instanceof Space) {
					tile = this.tiles[row][column];
					return tile;
				}
			}
		}
		return null;
	}

	/**
	 * Returns tile at entered row and column
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Tile getTile(int row, int column) {
		return tiles[row][column];
	}

	/**
	 * Returns row of inserted Tile
	 * 
	 * @param tile
	 * @return
	 */
	public int getTileRow(Tile tile) { // viem, toto by trebalo opravit 
		int tileRow = 0;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				if (this.tiles[row][column].equals(tile)) {
					tileRow = row;
				}
			}
		}
		return tileRow;
	}

	/**
	 * Returns column of inserted Tile
	 * 
	 * @param tile
	 * @return
	 */
	public int getTileColumn(Tile tile) { // viem, toto by trebalo opravit 
		int tileColumn = 0;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				if (this.tiles[row][column].equals(tile)) {
					tileColumn = column;
				}
			}
		}
		return tileColumn;
	}

	/**
	 * Swaps two tiles in field
	 * 
	 * @param tile1
	 * @param tile2
	 */
	private void swapTiles(Tile tile1, Tile tile2) {
		Tile helpfulTile = this.tiles[getTileRow(tile1)][getTileColumn(tile1)];
		this.tiles[getTileRow(tile1)][getTileColumn(tile1)] = this.tiles[getTileRow(tile2)][getTileColumn(tile2)];
		this.tiles[getTileRow(tile2)][getTileColumn(tile2)] = helpfulTile;
	}

	/**
	 * Swaps two tiles in field based on direction enum
	 * 
	 * @param direction
	 */
	public void move(Direction direction) {
		Tile tile1 = getSpace();
		int row = getTileRow(tile1);
		int column = getTileColumn(tile1);
		Tile tile2 = null;
		if (direction == Direction.UP) {
			if (row + 1 < rowCount) {
				tile2 = tiles[row + 1][column];
				swapTiles(tile1, tile2);
			} else {
				System.err.println("You can't move here");
			}
		}
		if (direction == Direction.DOWN) {
			if (row - 1 >= 0) {
				tile2 = tiles[row - 1][column];
				swapTiles(tile2, tile1);
			} else {
				System.err.println("You can't move here");
			}
		}
		if (direction == Direction.LEFT) {
			if (column + 1 < columnCount) {
				tile2 = tiles[row][column + 1];
				swapTiles(tile1, tile2);
			} else {
				System.err.println("You can't move here");
			}
		}
		if (direction == Direction.RIGHT) {
			if (column - 1 >= 0) {
				tile2 = tiles[row][column - 1];
				swapTiles(tile2, tile1);
			} else {
				System.err.println("You can't move here");
			}
		}
	}

	/**
	 * Returns state of game
	 * 
	 * @return
	 */
	public GameState getGameState() {
		return this.gameState;
	}

	/**
	 * if game is solved, changed game state to solved
	 */
	public void checkGameState() {
		int number1 = 0;
		int number2 = 0;
		Tile currentTile = null;
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				currentTile = tiles[row][column];
				if (currentTile instanceof Stone) {
					number2 = currentTile.getValue();
					if (number2 > number1) {
						number1 = number2;
					} else {
						return;
					}
				}
			}
		}
		if (currentTile instanceof Space) {
			gameState = GameState.SOLVED;
		}
	}

	/**
	 * Returns field as string
	 */
	@Override
	public String toString() {
		Formatter f = new Formatter();
		for (int row = 0; row < this.rowCount; row++) {
			for (int column = 0; column < this.columnCount; column++) {
				f.format("%3s", tiles[row][column].toString());
			}
			f.format("%n");
		}
		return f.toString();
	}

}
