package Schedule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleParser {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy");
    private static final int FREE_TIME_LINE_LENGTH = 4;
    private static final String FREE_TIME_PREFIX = "Free Time: ";

    public static void main(String[] args) throws Exception {
        // Read the file and parse the schedule
        List<ScheduleEntry> scheduleEntries = parseScheduleFile("weeklySchedule.txt");

        // Print the schedule in a calendar layout
        printScheduleInCalendar(scheduleEntries);
    }

    private static List<ScheduleEntry> parseScheduleFile(String filePath) throws Exception {
        List<ScheduleEntry> scheduleEntries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentDayOfWeek = null;
            LocalDate currentDate = null;

            while ((line = br.readLine()) != null) {
                if (line.matches("\\d+")) {
                    // Skip line numbers
                    continue;
                } else if (line.matches("\\w+:")) {
                    // Parse day of week
                    currentDayOfWeek = line.replace(":", "");
                    currentDate = LocalDate.now().with(DayOfWeek.valueOf(currentDayOfWeek.toUpperCase()));
                } else if (line.matches("\\d+.*")) {
                    // Parse schedule entry
                    String[] parts = line.split("\\s+");
                    int index = Integer.parseInt(parts[0]);
                    String activity = parts[1];
                    LocalTime startTime = LocalTime.parse(parts[2], timeFormatter);
                    LocalTime endTime = parts.length > 3 && !parts[3].equals("—")
                            ? LocalTime.parse(parts[3], timeFormatter)
                            : null;
                    LocalDate date = currentDate;
                    if (parts.length > 4) {
                        String timeSpecifier = parts[4];
                        if (timeSpecifier.equals("nextday")) {
                            date = currentDate.plusDays(1);
                        }
                    }
                    ScheduleEntry entry = new ScheduleEntry(index, activity, startTime, endTime, date);
                    scheduleEntries.add(entry);
                } else if (line.startsWith(FREE_TIME_PREFIX)) {
                    // Parse free time entry
                    String[] parts = line.split(":");
                    if (parts.length == FREE_TIME_LINE_LENGTH) {
                        LocalTime startTime = LocalTime.parse(parts[1].substring(0, 5), timeFormatter);
                        LocalTime endTime = LocalTime.parse(parts[1].substring(6), timeFormatter);
                        scheduleEntries.add(new ScheduleEntry(-1, "Free Time", startTime, endTime, currentDate));
                    }
                }
            }
        }

        return scheduleEntries;
    }

    private static void printScheduleInCalendar(List<ScheduleEntry> scheduleEntries) {
        // Group schedule entries by date
        List<List<ScheduleEntry>> entriesByDate = groupEntriesByDate(scheduleEntries);

        // Print schedule for each date in a calendar layout
        for (List<ScheduleEntry> entries : entriesByDate) {
            LocalDate date = entries.get(0).getDate();

            System.out.println(dateFormatter.format(date));
            System.out.println("+---------+---------+---------+---------+---------+---------+---------+");
            System.out.println("|  Time   | Monday  | Tuesday | Wednesday | Thursday | Friday  |  Saturday/Sunday");
            System.out.println("+---------+---------+---------+---------+---------+---------+---------+");

            // Print schedule entries for each hour of the day
            for (int hour = 0; hour < 24; hour++) {
                LocalTime startTime = LocalTime.of(hour, 0);
                LocalTime endTime = LocalTime.of(hour, 59);

                System.out.printf("| %s |", formatTimeRange(startTime, endTime));

                for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        // Weekend days are combined into one column
                        System.out.print("         |");
                    } else {
                        List<ScheduleEntry> entriesForDayOfWeek = getEntriesForDayOfWeek(entries, dayOfWeek);
                        String cellContent = "";
                        for (ScheduleEntry entry : entriesForDayOfWeek) {
                            if (entry.getStart().compareTo(endTime) <= 0
                                    && (entry.getEnd() == null || entry.getEnd().compareTo(startTime) >= 0)) {
                                // Entry overlaps with this hour of the day
                                if (!cellContent.isEmpty()) {
                                    cellContent += "\n";
                                }
                                cellContent += entry.getActivity();
                            }
                        }
                        if (cellContent.isEmpty()) {
                            cellContent = "         ";
                        }
                        System.out.printf(" %s |", cellContent);
                    }
                }

                System.out.println();
                System.out.println("+---------+---------+---------+---------+---------+---------+---------+");
            }

            System.out.println();
        }
    }

    private static List<List<ScheduleEntry>> groupEntriesByDate(List<ScheduleEntry> scheduleEntries) {
        List<List<ScheduleEntry>> entriesByDate = new ArrayList<>();
        LocalDate currentDate = null;
        List<ScheduleEntry> currentEntries = null;

        for (ScheduleEntry entry : scheduleEntries) {
            if (currentDate == null || !entry.getDate().isEqual(currentDate)) {
                if (currentEntries != null) {
                    entriesByDate.add(currentEntries);
                }
                currentDate = entry.getDate();
                currentEntries = new ArrayList<>();
            }
            currentEntries.add(entry);
        }

        if (currentEntries != null) {
            entriesByDate.add(currentEntries);
        }

        return entriesByDate;
    }

    private static List<ScheduleEntry> getEntriesForDayOfWeek(List<ScheduleEntry> entries, DayOfWeek dayOfWeek) {
        List<ScheduleEntry> entriesForDayOfWeek = new ArrayList<>();
        for (ScheduleEntry entry : entries) {
            if (entry.getDate().getDayOfWeek() == dayOfWeek) {
                entriesForDayOfWeek.add(entry);
            }
        }
        return entriesForDayOfWeek;
    }

    private static String formatTimeRange(LocalTime startTime, LocalTime endTime) {
        return String.format("%02d:%02d - %02d:%02d", startTime.getHour(), startTime.getMinute(), endTime.getHour(),
                endTime.getMinute());
    }

    private static class ScheduleEntry {
        private final int index;
        private final String activity;
        private final LocalTime start;
        private final LocalTime end;
        private final LocalDate date;

        public ScheduleEntry(int index, String activity, LocalTime start, LocalTime end, LocalDate date) {
            this.index = index;
            this.activity = activity;
            this.start = start;
            this.end = end;
            this.date = date;
        }

        public int getIndex() {
            return index;
        }

        public String getActivity() {
            return activity;
        }

        public LocalTime getStart() {
            return start;
        }

        public LocalTime getEnd() {
            return end;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}
