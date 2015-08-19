package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FieldFileLoader {
	
	private final String SAVE_FILE = "puzzle.bin";
	
	public void save(Field field) throws IOException {
		try (ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(SAVE_FILE));) {
			s.writeObject(field);
		}
	}

	public Field load() {
		File file = new File(SAVE_FILE);
		Field field;
		if (file.exists()) {
			try (ObjectInputStream s = new ObjectInputStream(new FileInputStream(file));) {
				field = (Field) s.readObject();
				return field;
			} catch (FileNotFoundException e) {
				System.err.println("File not found");
				return null;
			} catch (IOException e) {
				System.err.println("IO Exception");
				return null;
			} catch (ClassNotFoundException e) {
				System.err.println("Class not found exception");
				return null;
			}
		} else {
			return null;
		}
	}
}
