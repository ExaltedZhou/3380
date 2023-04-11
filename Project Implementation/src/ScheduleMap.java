package Schedule;


import java.util.*;

public class ScheduleMap {
	HashMap<Integer, Block> weeklySchedule;
	public ScheduleMap() {
		weeklySchedule = new HashMap<Integer, Block>();
	}
	public void insertSchedule(int k, Block v) {
		weeklySchedule.put(k, v);		
	}
	public String toString() {
		String[] wd = {"Mon", "Tue", "Wed", "Thu", "Fri"};
		String c = "";
		for(int k:weeklySchedule.keySet()) {
			c += wd[k] + ":\n" + weeklySchedule.get(k).toString() + "\n";
		}
		return c;
	}
}