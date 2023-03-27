package Schedule;

import java.util.*;
import java.io.*;

public class ScheduleGenerator {
	private static ReadLine r;
	
	public static void main(String[] args) throws FileNotFoundException {
		String[] wd = {"Mon", "Tue", "Wed", "Thu", "Fri", "M-F"};
		List<Block> b = new ArrayList<Block>();
		for(int i = 0; i < 5; i++) {
			b.add(new Block(wd[i]));
		}
		r = new ReadLine(b);
		Scanner s = new Scanner(System.in);
		if(args.length==0) {
			System.out.println("instruction, enter day, name, time start, time end, comments in order");
			System.out.println("seperated by commas");
			System.out.println("Enter day as first 3 letter of weekdays");
			System.out.println("Time has to be hour:minuteAM/PM");
			System.out.println("or enter done to move on");
			String line = "";
			while(!line.equalsIgnoreCase("done")) {
				System.out.print("Enter your activity: ");
				line = s.nextLine().trim();
				if(line.equalsIgnoreCase("done")) 
					break;
				r.readLine(line);
			}
		}
		else if(args.length==1) {
			FileReader sFile = new FileReader(args[0]);
			Scanner sc = new Scanner(sFile);
			int lineNum = 1;
			while(sc.hasNextLine()) {
				String line = sc.nextLine().trim();
				System.out.println("Reading line " + lineNum);
				r.readLine(line);
				lineNum++;
			}
			sc.close();
		}
		s.close();
		ScheduleMap sch = new ScheduleMap();
		for(int i = 0; i < 5; i++) {
			if(!b.get(i).getBlock().isEmpty()) {
				b.get(i).sortedBlock();
				sch.insertSchedule(i, b.get(i));
			}
		}
		
		
		
		System.out.println(sch.toString());
		PrintStream out = new PrintStream(new File("weeklySchedule.txt"));
		System.setOut(out);
		System.out.println(sch.toString());
		for (Block block : b) {
            if (!block.getBlock().isEmpty()) {
                System.out.println("Free time with suggestions for " + block.getName() + ":");
                block.printFreeTimeWithSuggestions();
                System.out.println();
            }
        }
    }
}



