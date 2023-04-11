package Schedule;

import java.util.*;

public class ReadLine {
    private List<Block> b;
    String[] wd = {"Mon", "Tue", "Wed", "Thu", "Fri", "M-F"};
    private VerificationEngine ve;
    private Activity a;
    public ReadLine(List<Block> b) {
        this.b=b;
        b=new ArrayList<Block>();
        for(int i = 0; i < 5; i++) {
            b.add(new Block(wd[i]));
        }
    }
    public void readLine(String s) {
        ve = new VerificationEngine();
        ve.checkFormat(s);
        String[] lp = s.split(",");
        String activityDay = lp[0].trim();
        String activityName = lp[1].trim();
        String activityStart = lp[2].trim();
        String activityEnd = lp[3].trim();
        String activityComments = lp[4].trim();
        a = new Activity(activityName, activityStart, activityEnd, activityComments, false);

        boolean overlap = false;
        boolean[] ovlap = new boolean[5];
        if (activityDay.equalsIgnoreCase(wd[5])) {
            for (int j = 0; j < 5; j++) {
                ovlap[j] = ve.checkOverblock(a, b.get(j));
                if (ovlap[j])
                    System.out.println("overlap with " + wd[j] + " schedule.");
                b.get(j).insert(a);

                // Change 1: Splitting bed into two activities and adjusting end time
                if (activityName.equalsIgnoreCase("bed")) {
                    a.setEndTime("11:59pm");
                    Activity wakeUp = new Activity("wake up", "", activityEnd, "", true);
                    int nextDayIndex = (j + 1) % 5;
                    b.get(nextDayIndex).insert(wakeUp);
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                if (activityDay.equalsIgnoreCase(wd[i])) {
                    overlap = ve.checkOverblock(a, b.get(i));
                    if (!overlap)
                        b.get(i).insert(a);
                    else
                        System.out.println("overlap");

                    // Change 2: Splitting bed into two activities and adjusting end time
                    if (activityName.equalsIgnoreCase("bed")) {
                        a.setEndTime("11:59pm");
                        Activity wakeUp = new Activity("wake up", "", activityEnd, "", true);
                        int nextDayIndex = (i + 1) % 5;
                        b.get(nextDayIndex).insert(wakeUp);
                    }
                }
            }
        }
    }
}