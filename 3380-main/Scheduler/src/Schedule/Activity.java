package Schedule;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String name;
    private String timeStart;
    private String timeEnd;
    private String comments;

    public Activity(String name, String timeStart, String timeEnd, String comments, boolean isWakeUp) {
        this.name = name;
        this.timeStart = isWakeUp ? "0:00am" : timeStart;
        this.timeEnd = timeEnd;
        this.comments = comments;
    }

    public String getName(){
        return name;
    }

    public LocalTime getStart(){
        return parseTime(timeStart);
    }

    public LocalTime getEnd(){
        return parseTime(timeEnd);
    }
     // Added setEndTime method
     public void setEndTime(String newEndTime) {
        this.timeEnd = newEndTime;
    }


    private LocalTime parseTime(String time) {
        int hour = 0;
        int min = 0;
        int n = time.length();
        String[] a = {time.substring(0, n - 2), time.substring(n - 2, n)};
        String[] s = a[0].split(":");

        hour = Integer.parseInt(s[0]) % 12;
        if (a[1].equalsIgnoreCase("pm")) {
            hour += 12;
        }
        min = Integer.parseInt(s[1]);

        return LocalTime.of(hour, min);
    }

    public String comments(){
        return comments;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
        int n = name.length();
        int m = timeStart.length();
        int k = timeEnd.length();
        String w1 = "";
        for(int i = 0; i < 25-n; i++) {
            w1 += " ";
        }
        String w2 = "";
        for(int i = 0; i < 9-m; i++) {
            w2 += " ";
        }
        String w3 = "";
        for(int i = 0; i < 9-k; i++) {
            w3 += " ";
        }
        return name + w1 + getStart().format(formatter) + w2 + " - " + getEnd().format(formatter) + w3 + comments;
    }

    public LocalTime unscheduled() {
        int hour = 0;
        int min = 0;
        
        return LocalTime.of(hour, min);
    }
}