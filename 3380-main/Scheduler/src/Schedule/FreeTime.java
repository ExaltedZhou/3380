package Schedule;


import java.time.*;

public class FreeTime {
	private LocalTime startTime;
	private LocalTime endTime;
	
	public FreeTime(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public String toString() {
		return "Free Time: " + startTime + "-" + endTime;
	}
}