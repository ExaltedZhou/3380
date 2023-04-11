package Schedule;


public class SuggestedActivity {
	private String name;
	private int duration;
	
	
	public SuggestedActivity(String name, int duration) {
		this.name = name;
		this.duration = duration;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public String toString() {
		return name + " (" + duration + " minute)";
	}
}