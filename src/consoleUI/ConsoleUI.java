package consoleUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import besttimes.BestTimes;
import besttimes.PlayerTimeDatabaseLoader;
import core.Field;
import core.Field.Direction;
import fileloader.FieldFileLoader;

public class ConsoleUI {

	private int rowCount = 2;
	private int columnCount = 2;

	private long time;
	private Field field;
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private BestTimes bestTimes;
	private FieldFileLoader fileLoader;
	private PlayerTimeDatabaseLoader databaseLoader;

	/**
	 * Construct consoleUI, loads best times from database and field from file,
	 * starts new game
	 */
	public ConsoleUI() {
		databaseLoader = new PlayerTimeDatabaseLoader();
		fileLoader = new FieldFileLoader();
		if (databaseLoader.load() != null) {
			this.bestTimes = databaseLoader.load();
		} else {
			this.bestTimes = new BestTimes();
		}

		if (fileLoader.load() != null) {
			this.field = fileLoader.load();
			startNewGame(field);
		} else {
			this.field = new Field(rowCount, columnCount);
			startNewGame(field);
		}
	}

	/**
	 * user input
	 * 
	 * @return
	 */
	private String readLine() {
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Starts new game on field
	 * 
	 * @param field
	 */
	private void startNewGame(Field field) {
		this.field = field;
		newGameStarted(field);

	}

	/**
	 * Communicates with user in console, stops game if field is solved, adds
	 * best times
	 * 
	 * @param field
	 */
	private void newGameStarted(Field field) {
		time = System.currentTimeMillis();
		update();
		do {
			processInput(false);
			update();
		} while (!field.checkGameState());
		savePlayerTime();
		createNewField();
		do {
			processInput(true);
		} while (true);
	}
	
	/**
	 * Saves playerTime and prints best times
	 */
	private void savePlayerTime(){
		time = (System.currentTimeMillis() - time) / 1000;
		System.out.println("YOU WON! Please enter your name");
		String name = readLine();
		bestTimes.addPlayerTime(name, (int) time);
		try {
			databaseLoader.save(bestTimes);
		} catch (Exception e) {
		}
		System.out.println(bestTimes.toString());
	}
	
	/**
	 * Creates new game field
	 */
	private void createNewField() {
		this.field = new Field(rowCount, columnCount);
	}

	/**
	 * Updates game field into console
	 */
	public void update() {
		System.out.println(field.toString());
	}

	/**
	 * Communicates with user in console, prints instructions and handle inputs
	 * 
	 * @param won
	 */
	private void processInput(boolean won) {
		System.out.println("Exit – Exit game");
		System.out.println("New - Start new game");
		if (!won) {
			System.out.println("W (up), S (down), A (left), D (right) - to move");
			checkInput(readLine(), false);
		} else {
			checkInput(readLine(), true);
		}
		System.out.println("-----------------------------------------------");

	}
	
	/**
	 * Calls method handleinput and catches exceptions
	 * @param input
	 * @param won
	 */
	private void checkInput(String input, boolean won) {
		try {
			handleInput(input, won);
		} catch (WrongFormatException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Checks if input form user is valid, executes move methods asked by user,
	 * starts new game and exit game
	 * 
	 * @param input
	 * @param won
	 * @throws WrongFormatException
	 */
	private void handleInput(String input, boolean won) throws WrongFormatException {
		String x = input.toUpperCase();
		if (!won) {
			switch (x) {
			case "W":
			case "UP":
				field.move(Direction.UP, true);
				break;
			case "S":
			case "DOWN":
				field.move(Direction.DOWN, true);
				break;
			case "A":
			case "LEFT":
				field.move(Direction.LEFT, true);
				break;
			case "D":
			case "RIGHT":
				field.move(Direction.RIGHT, true);
				break;
			case "EXIT":
				System.out.println();
				System.out.print("Game was closed");
				try {
					fileLoader.save(field);
					System.out.println(" and saved");
				} catch (IOException e) {
					System.err.println("There is some problem, game wasn't saved!");
				}
				System.exit(0);
				break;
			case "NEW":
				this.field = new Field(rowCount, columnCount);
				startNewGame(field);
				break;
			default:
				throw new WrongFormatException("Wrong format!");
			}
		} else {
			switch (x) {
			case "EXIT":
				System.out.println("Game was closed");
				System.exit(0);
				break;
			case "NEW":
				this.field = new Field(rowCount, columnCount);
				startNewGame(field);
				break;
			default:
				throw new WrongFormatException("Wrong format!");
			}
		}
	}

}
