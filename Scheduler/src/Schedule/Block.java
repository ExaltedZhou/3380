package Schedule;

import java.util.*;

public class Block {
	private String name;
	private List <Activity> block;
	public Block(String name) {
		this.name = name;
		block = new ArrayList<Activity>();
	}
	public String getName() {
		return name;
	}
	public void insert(Activity a) {
		block.add(a);
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
	}
	public String toString() {
		String b = "";
		for(Activity a:block) {
			b += a.toString()+"\n";
		}
		return b;
	}
}










