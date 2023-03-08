package scheduleGen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Activity {
    private String name;
    private String timeStart;
    private String timeEnd;
    private String day;

    public Activity(String name, String timeStart, String timeEnd, String day) {
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getDay() {
        return day;
    }
}

class VerificationEngine {
    public static boolean checkOverlap(Activity activity1, Activity activity2) {
        if (!activity1.getDay().equals(activity2.getDay())) {
            return false;
        }

        String start1 = activity1.getTimeStart();
        String end1 = activity1.getTimeEnd();
        String start2 = activity2.getTimeStart();
        String end2 = activity2.getTimeEnd();

        if ((start1.compareTo(end2) <= 0) && (end1.compareTo(start2) >= 0)) {
            return true;
        } else {
            return false;
        }
    }
}

public class ScheduleGenerator {
    public static void main(String[] args) {
        ArrayList<Activity> activities = new ArrayList<Activity>();
        Scanner scanner = new Scanner(System.in);

        // Get class times
        System.out.println("Enter class times for Monday to Friday:");
        for (int i = 1; i <= 5; i++) {
            String day = getDay(1);
            while (true) {
                System.out.print("Class name for " + day + " (or enter \"done\" to move on): ");
                String name = scanner.nextLine();
                if (name.equals("done")) {
                    break;
                }
                System.out.print("Start time (in format HH:MM): ");
                String startTime = scanner.nextLine();
                System.out.print("End time (in format HH:MM): ");
                String endTime = scanner.nextLine();
                Activity activity = new Activity(name, startTime, endTime, day);
                activities.add(activity);
                
                String filename = "my_activities.txt";
                writeActivitiesToFile(activities, filename);
            }
        }

        // Get meal times --- only takes in one meal time and applies it to Each of the days
        System.out.println("\nEnter meal times:");
        System.out.print("Start time (in format HH:MM): ");
        String mealStartTime = scanner.nextLine();
        System.out.print("End time (in format HH:MM): ");
        String mealEndTime = scanner.nextLine();
        Activity mealActivity = new Activity("Meal", mealStartTime, mealEndTime, "Monday-Friday");
        activities.add(mealActivity);

        // Get bed times --- currently only takes in one time range. 
        //Produces an error when given time range where start time is greater than end time.
        System.out.println("\nEnter bed times:");
        System.out.print("Start time (in format HH:MM): ");
        String bedStartTime = scanner.nextLine();
        System.out.print("End time (in format HH:MM): ");
        String bedEndTime = scanner.nextLine();
        Activity bedActivity = new Activity("Bed", bedStartTime, bedEndTime, "Monday-Friday");
        activities.add(bedActivity);

        // Check for overlapping times
        boolean isOverlap = false;
        for (int i = 0; i < activities.size(); i++) {
            for (int j = i + 1; j < activities.size(); j++) {
                if (VerificationEngine.checkOverlap(activities.get(i), activities.get(j))) {
                    isOverlap = true;
                    System.out.println("Error: Overlapping");
                }}}}
    //currently not working
	private static void writeActivitiesToFile(ArrayList<Activity> activities, String filename) {
		
		
	}
	//functions
	private static String getDay(int i) {
		
		return null;
	}}
    