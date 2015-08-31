package besttimes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class PlayerTimeDatabaseLoader {

	public static final String URL = "jdbc:mysql://localhost/puzzle";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	
	public void save(BestTimes bestTimes) {
		String SET = "SET SQL_SAFE_UPDATES = 0";
		String DELETE = "DELETE FROM besttimes";
		String QUERY = "INSERT INTO besttimes (id, name, time) VALUES (?, ?, ?)";
		int count = bestTimes.getCount();
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement del = con.createStatement();
				PreparedStatement stmt = con.prepareStatement(QUERY);) {
			del.execute(SET);
			del.executeUpdate(DELETE);
			for (int i = 0; i < count; i++) {
				stmt.setInt(1, i);
				stmt.setString(2, bestTimes.getPlayerTime(i).getName());
				stmt.setInt(3, bestTimes.getPlayerTime(i).getTime());
				stmt.executeUpdate();
			}
		} catch (MySQLSyntaxErrorException e){ // toto a import treba otesotvat
			createTable();
			save(bestTimes);
		} catch (SQLException e) {
			System.err.println("Something is wrong, your time was not saved");
			return;
		}
	}
	
	public BestTimes load() {
		String QUERY = "SELECT id, name, time FROM besttimes";
		BestTimes bestTimes = new BestTimes();
		String name;
		int time;
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = con.createStatement();) {
			ResultSet rs = stmt.executeQuery(QUERY);
			while (rs.next()) {
				name = rs.getString(2);
				time = rs.getInt(3);
				bestTimes.addPlayerTime(name, time);
			}
		} catch (SQLException e) {
			System.err.println("Something is wrong, best times were not loaded");
			return null;
		}

		return bestTimes;
	}
	
	public void createTable() {
		String QUERY = "CREATE TABLE besttimes (id INT PRIMARY KEY, name VARCHAR(32) NOT NULL, time INT NOT NULL)";
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = con.createStatement();) {
			stmt.executeUpdate(QUERY);
		} catch (SQLException e) {
			System.err.println("SQL Exception");
		}
	}
}
