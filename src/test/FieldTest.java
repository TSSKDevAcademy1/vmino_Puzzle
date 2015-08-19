package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.Field;
import core.Field.Direction;
import core.Field.GameState;

public class FieldTest {
	
	int rowCount = 3;
	
	int columnCount = 3;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createFieldGameState() {
		Field field = new Field(rowCount, columnCount);
		assertEquals(GameState.UNSOLVED, field.getGameState());
	}
	
	@Test
	public void moveUp() {
		Field field = new Field(rowCount, columnCount);
		for (int i=0; i<rowCount*columnCount; i++){
			field.move(Direction.UP);
		}
	}
	
	@Test
	public void moveDown() {
		Field field = new Field(rowCount, columnCount);
		for (int i=0; i<rowCount*columnCount; i++){
			field.move(Direction.DOWN);
		}
	}
	
	@Test
	public void moveLeft() {
		Field field = new Field(rowCount, columnCount);
		for (int i=0; i<rowCount*columnCount; i++){
			field.move(Direction.LEFT);
		}
	}
	
	@Test
	public void moveRight() {
		Field field = new Field(rowCount, columnCount);
		for (int i=0; i<rowCount*columnCount; i++){
			field.move(Direction.RIGHT);
		}
	}
	
}
