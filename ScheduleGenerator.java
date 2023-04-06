package scheduleGenertor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Activity {
    private final String name;
    private final int startTime;
    private final int endTime;
    private final int dayOfWeek;

    public Activity(String name, int startTime, int endTime, int dayOfWeek) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    public String getName() {
        return name;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
}

class VerificationEngine {
    public static boolean checkOverlap(Activity activity1, Activity activity2) {
        if (activity1.getDayOfWeek() != activity2.getDayOfWeek()) {
            return false;
        }

        int start1 = activity1.getStartTime();
        int end1 = activity1.getEndTime();
        int start2 = activity2.getStartTime();
        int end2 = activity2.getEndTime();

        return (start1 <= end2) && (end1 >= start2);
    }
}

public class ScheduleGenerator {
    public static void main(String[] args) throws IOException {
//         try( //this reads the txt file but need to change the inputGUI.java to add the amount of how many classes user put in txt file for this for-loop.
// 		        Scanner inFile = new Scanner(new FileReader("inputFile.txt")))
// 		        {
// 		            int totalOps = inFile.nextInt();
// 		            for (int i=0; i<totalOps; i++)
// 		            {
// 		                if(inFile.hasNext("(Meal,)"))
// 		                {
		                  
// 		                }
// 		                else if(inFile.hasNext("(Bed,)"))
// 		                {
		                   
// 		                }
//                         else {method(inFIle.hasNext())}
		               
// 		            }

// 		}
        Scanner scanner = new Scanner(System.in);

        // Get class times
        Map<Integer, List<Activity>> activitiesByDay = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            String day = getDay(i);
            List<Activity> activities = new ArrayList<>();
            while (true) {
                System.out.print("Class name for " + day + " (or enter \"done\" to move on): ");
                String name = scanner.nextLine();
                if (name.equals("done")) {
                    break;
                }
                System.out.print("Start time (in format HHMM): ");
                int startTime = scanner.nextInt();
                System.out.print("End time (in format HHMM): ");
                int endTime = scanner.nextInt();
                scanner.nextLine();
                Activity activity = new Activity(name, startTime, endTime, i);
                activities.add(activity);
            }
            activitiesByDay.put(i, activities);
        }

        // Get meal times
        System.out.println("\nEnter meal times:");
        System.out.print("Start time (in format HHMM): ");
        int mealStartTime = scanner.nextInt();
        System.out.print("End time (in format HHMM): ");
        int mealEndTime = scanner.nextInt();
        scanner.nextLine();
        Activity mealActivity = new Activity("Meal", mealStartTime, mealEndTime, 0);
        for (int i = 1; i <= 5; i++) {
            List<Activity> activities = activitiesByDay.get(i);
            activities.add(mealActivity);
        }

        // Get bed times
        System.out.println("\nEnter bed times:");
        System.out.print("Start time (in format HHMM): ");
        int bedStartTime = scanner.nextInt();
        System.out.print("End time (in format HHMM): ");
        int bedEndTime = scanner.nextInt();
        scanner.nextLine();
        Activity bedActivity = new Activity("Bed", bedStartTime, bedEndTime, 0);
        List<Activity> bedActivities = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            bedActivities.add(bedActivity);
        }
        activitiesByDay.put(0, bedActivities);

        // Check for overlapping times
        boolean isOverlap = false;
        Set<Activity> allActivities = new TreeSet<>(Comparator.comparing(Activity::
        getStartTime).thenComparing(Activity::getDayOfWeek));
        for (int i = 0; i <= 5; i++) {
        List<Activity> activities = activitiesByDay.get(i);
        allActivities.addAll(activities);
        }
        for (Activity activity1 : allActivities) {
        for (Activity activity2 : allActivities) {
        if (VerificationEngine.checkOverlap(activity1, activity2) && activity1 != activity2) {
        isOverlap = true;
        System.out.println("Error: Overlapping activities - " + activity1.getName() + " and " + activity2.getName());
           }
          }
        }
        
           // Write activities to file
    String filename = "my_activities.txt";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        for (Activity activity : allActivities) {
            writer.write(activity.getName() + "," + activity.getStartTime() + "," + activity.getEndTime() + "," + activity.getDayOfWeek() + "\n");
        }
    } catch (IOException e) {
        System.out.println("Error writing to file: " + e.getMessage());
    }

    if (!isOverlap) {
        System.out.println("Schedule generated successfully!");
    }
}
    
public String method(String str) {
    if (str.charAt(str.length()-1)=='x'){
        str = str.replace(str.substring(str.length()-1), "");
        return str;
    } else{
        return str;
    }
}
    
private static String getDay(int i) {
    switch (i) {
        case 1:
            return "Monday";
        case 2:
            return "Tuesday";
        case 3:
            return "Wednesday";
        case 4:
            return "Thursday";
        case 5:
            return "Friday";
        default:
            return "";
    }
 }
}
