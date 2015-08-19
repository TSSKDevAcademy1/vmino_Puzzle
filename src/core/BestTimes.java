package core;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;


public class BestTimes implements Iterable<BestTimes.PlayerTime> {

	private List<PlayerTime> playerTimes = new ArrayList<PlayerTime>();

	@Override
	public Iterator<PlayerTime> iterator() {
		return playerTimes.iterator();
	}
	
	public void addPlayerTime(String name, int time) {
		PlayerTime player = new PlayerTime(name, time);
		playerTimes.add(player);
		
		Collections.sort(playerTimes);
	}
	
	public PlayerTime getPlayerTime(int i){
		return playerTimes.get(i);
	}
	
	public String toString() {
		Formatter f = new Formatter();
		Iterator<PlayerTime> it = playerTimes.iterator();
		f.format("%-10s %-5s", "Name", "Time");
		while (it.hasNext()) {
			PlayerTime pt = (PlayerTime) it.next();
			f.format("%-10s %-5s %n", pt.name, pt.time);
		}
		return f.toString();
	}
	
	public int getCount() {
		return playerTimes.size();
	}
	
	public static class PlayerTime implements Comparable<PlayerTime> {

		private final String name;
		private final int time;

		public PlayerTime(String name, int time) {
			this.name = name;
			this.time = time;
		}

		public String getName() {
			return this.name;
		}

		public int getTime() {
			return this.time;
		}

		public int compareTo(PlayerTime o) {
			if (this.time < o.time) {
				return -1;
			} else if (this.time == o.time) {
				return 0;
			} else {
				return 1;
			}
		}

	}
}
