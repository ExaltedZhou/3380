package Schedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class Block {
	private String name;
	private List <Activity> block;
	private List<FreeTime> freeTimeBlocks;
	
	public List<SuggestedActivity> suggestActivitiesForFreeTime(FreeTime freeTime){
		List<SuggestedActivity> suggestedActivities = new ArrayList<>();
		
		List<SuggestedActivity> potentialActivities = Arrays.asList(
				new SuggestedActivity("Exercise", 60),
				new SuggestedActivity("Friend Time", 120),
				new SuggestedActivity("Study Time", 120)
				);
		
		long freeTimeDuration = Duration.between(freeTime.getStartTime(), freeTime.getEndTime()).toMinutes();
		
		for (SuggestedActivity activity : potentialActivities) {
			if(activity.getDuration() <= freeTimeDuration) {
				suggestedActivities.add(activity);
			}
		}
		
		return suggestedActivities;
		
	}
	
	public void printFreeTimeWithSuggestions() {
		for (FreeTime ft : freeTimeBlocks) {
			System.out.println(ft);
			List<SuggestedActivity> suggestions = suggestActivitiesForFreeTime(ft);
			System.out.println("SuggestedActivities:");
			for (SuggestedActivity suggestion : suggestions) {
				System.out.println(" - " + suggestion);
			}
		}
	}
	
	public Block(String name) {
		this.name = name;
		block = new ArrayList<Activity>();
		freeTimeBlocks = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void insert(Activity a) {
		block.add(a);
		updateFreeTimeBlocks();
	}
	
	public void sortedBlock() {
		Comparator<Activity> c = Comparator.comparing(Activity::getStart);
		Collections.sort(block,c);
	}
	
	public List <Activity> getBlock() {
		return block;
	}
	
	public void removeActivity(Activity a) {
		block.remove(a);
		updateFreeTimeBlocks();
	}
	//
	private void updateFreeTimeBlocks() {
		sortedBlock();
		freeTimeBlocks.clear();
		LocalTime prevActivityEndTime = LocalTime.MIN;
		
		for (Activity activity : block) {
			LocalTime currentActivityStartTime = activity.getStart();
			
			if (prevActivityEndTime.isBefore(currentActivityStartTime)) {
				freeTimeBlocks.add(new FreeTime(prevActivityEndTime, currentActivityStartTime));
			}
			prevActivityEndTime = activity.getEnd();
		}
		if (prevActivityEndTime.isBefore(LocalTime.MAX)) {
			freeTimeBlocks.add(new FreeTime(prevActivityEndTime, LocalTime.MAX));
		}
	}
	//
	public List<FreeTime> getFreeTimeBlocks(){
		return freeTimeBlocks;
	}
	
	public String toString() {
		String b = "";
		for(Activity a:block) {
			b += a.toString()+"\n";
		}
		for (FreeTime ft : freeTimeBlocks) {
			b += ft.toString() + "\n";
		}
		return b;
	}
}
