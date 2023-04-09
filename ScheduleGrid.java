package Schedule;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class ScheduleGrid {
    private static final String FILE_NAME = "weeklySchedule.txt";
    private static final String[] DAYS = { "mon", "tue", "wed", "thu", "fri", "sat", "sun" };

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mma");

    public static JFrame createScheduleFrame() throws IOException {
        // Create the table model and table
        DefaultTableModel model = createTableModel();
        JTable table = createTable(model);

        // Configure scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        configureScrollPane(scrollPane);

        // Set up the frame
        JFrame frame = new JFrame("Schedule Grid");
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        return frame;
    }

    private static DefaultTableModel createTableModel() throws IOException {
        String[][] data = createEmptyGridData();
        populateGridData(data);

        String[] columnNames = { "Time", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        configureTableFonts(table);
        configureTableHeader(table);
        configureTableCellRenderer(table);
        configureTableAppearance(table);
        return table;
    }

    private static void configureTableFonts(JTable table) {
        Font customFont = new Font("Verdana", Font.PLAIN, 14);
        table.setFont(customFont);
        table.getTableHeader().setFont(customFont.deriveFont(Font.BOLD, 16));
    }

    private static void configureTableHeader(JTable table) {
        table.getTableHeader().setBackground(new Color(30, 30, 30));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
    }

    private static void configureTableCellRenderer(JTable table) {
        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
            Map<String, Color> activityColors = new HashMap<>();
            Random random = new Random();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
                setComponentColor(value, row, column, c);
                return c;
            }

            private void setComponentColor(Object value, int row, int column, Component c) {
                if (value != null && !value.toString().isEmpty() && column > 0) {
                    String[] parts = value.toString().split("\\[");
                    String activity = parts[0].trim();
                    Color activityColor = activityColors.computeIfAbsent(activity,
                            k -> new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));

                    if (!activity.startsWith("Free Time") && !activity.startsWith("SuggestedActivities")) {
                        c.setBackground(activityColor);
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(50, 50, 50) : new Color(70, 70, 70));
                        c.setForeground(Color.WHITE);
                    }
                } else {
                    c.setBackground(row % 2 == 0 ? new Color(50, 50, 50) : new Color(70, 70, 70));
                    c.setForeground(Color.WHITE);
                }
            }

        });
    }

    private static void configureTableAppearance(JTable table) {
        table.setShowGrid(true);
        table.setGridColor(new Color(100, 100, 100));
        table.setIntercellSpacing(new Dimension(5, 5));
    }

    private static void configureScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(createBasicScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(createBasicScrollBarUI());
    }

    private static BasicScrollBarUI createBasicScrollBarUI() {
        return new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 80, 80);
                this.trackColor = new Color(50, 50, 50);
            }
        };
    }

    private static String[][] createEmptyGridData() {
        String[][] data = new String[48][8];
        for (int i = 0; i < 48; i++) {
            if (i % 2 == 0) {
                data[i][0] = LocalTime.of(i / 2, 0).format(TIME_FORMATTER);
            } else {
                data[i][0] = LocalTime.of(i / 2, 30).format(TIME_FORMATTER);
            }
            for (int j = 1; j < 8; j++) {
                data[i][j] = "";
            }
        }
        return data;
    }

    private static void populateGridData(String[][] data) throws IOException {
        List<String> lines = readLinesFromFile(FILE_NAME);
        String currentDay = "";
        int dayIndex = -1;

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            if (line.endsWith(":")) {
                currentDay = line.substring(0, line.length() - 1);
                dayIndex = findDayIndex(currentDay) + 1;
            } else {
                String[] parts = line.split("\\s+-\\s+");
                if (parts.length >= 2) {
                    processActivity(data, dayIndex, parts);
                }
            }
        }
    }

    private static void processActivity(String[][] data, int dayIndex, String[] parts) {
        String startTimeStr = parts[0].substring(parts[0].lastIndexOf(' ') + 1);
        String endTimeStr = parts[1].substring(0, parts[1].indexOf(' '));

        if (startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            return;
        }

        LocalTime startTime = LocalTime.parse(startTimeStr, TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse(endTimeStr, TIME_FORMATTER);

        if (endTime.isBefore(startTime)) {
            endTime = endTime.plusHours(12);
        }

        long duration = ChronoUnit.MINUTES.between(startTime, endTime);
        String activity = parts[0].substring(0, parts[0].lastIndexOf(' '));

        if (activity.startsWith("bed")) { // Handle bed time
            int startHour = startTime.getHour();
            int startMinute = startTime.getMinute();

            int rowIndex = startHour * 2 + (startMinute) / 30; // Fill in the cell for the start time
            data[rowIndex][dayIndex] = activity + " [" + startTimeStr + " - " + endTimeStr + "]";
        } else if (!activity.startsWith("Free Time") && !activity.startsWith("SuggestedActivities")) {
            int startHour = startTime.getHour();
            int startMinute = startTime.getMinute();

            for (int minute = 0; minute < duration; minute += 30) {
                int rowIndex = startHour * 2 + (startMinute + minute) / 30;
                data[rowIndex][dayIndex] = activity + " [" + startTimeStr + " - " + endTimeStr + "]";
            }
        }
    }

    private static int findDayIndex(String day) {
        String lowerCaseDay = day.toLowerCase();
        for (int i = 0; i < DAYS.length; i++) {
            if (DAYS[i].equals(lowerCaseDay)) {
                return i;
            }
        }
        return -1;
    }

    private static List<String> readLinesFromFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame frame = createScheduleFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

class BubbleTableCellRenderer extends DefaultTableCellRenderer {
    private static final DateTimeFormatter TIME_FORMATTER = null;
    Map<String, Color> activityColors = new HashMap<>();
    Random random = new Random();

    public BubbleTableCellRenderer() {
        setOpaque(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setCellAppearance(value, row, column);
        setForeground(Color.WHITE);

        return this;
    }

    private void setCellAppearance(Object value, int row, int column) {
        if (value != null && !value.toString().isEmpty() && column > 0) {
            String[] parts = value.toString().split("\\[");
            if (parts.length > 1) {
                setAppearanceForNonEmptyCell(parts[0].trim(), row, parts[1].replace("]", "").trim());
            } else {
                setEmptyCellAppearance(row);
            }
        } else {
            setEmptyCellAppearance(row);
        }
    }

    private void setAppearanceForNonEmptyCell(String activity, int row, String timeRange) {
        String[] times = timeRange.split("\\s*-\\s*");
        if (times.length == 2) {
            try {
                LocalTime startTime = LocalTime.parse(times[0], TIME_FORMATTER);
                LocalTime endTime = LocalTime.parse(times[1], TIME_FORMATTER);
                long duration = ChronoUnit.HOURS.between(startTime, endTime);
                if (duration > 0) {
                    setBackground(getActivityColor(activity));
                    setText(activity + " (" + duration + " hr" + (duration > 1 ? "s" : "") + ")");
                } else {
                    setEmptyCellAppearance(row);
                }
            } catch (DateTimeParseException e) {
                setEmptyCellAppearance(row);
            }
        } else {
            setEmptyCellAppearance(row);
        }
    }

    private void setEmptyCellAppearance(int row) {
        setBackground(row % 2 == 0 ? new Color(50, 50, 50) : new Color(70, 70, 70));
        setText("");
    }

    private Color getActivityColor(String activity) {
        Color activityColor = activityColors.get(activity);
        if (activityColor == null) {
            activityColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            activityColors.put(activity, activityColor);
        }
        return activityColor;
    }



}

