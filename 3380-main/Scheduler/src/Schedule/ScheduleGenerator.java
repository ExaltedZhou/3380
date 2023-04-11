package Schedule;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import javax.swing.*;

public class ScheduleGenerator {
    private static ReadLine r;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        File inputFile = new File("input.txt");

        CountDownLatch latch = new CountDownLatch(1);
        InputGUI.GUI(inputFile, latch);

        latch.await();

        String[] wd = {"Mon", "Tue", "Wed", "Thu", "Fri", "M-F"};
        List<Block> b = new ArrayList<Block>();
        for (int i = 0; i < 5; i++) {
            b.add(new Block(wd[i]));
        }

        r = new ReadLine(b);

        Scanner sc = new Scanner(inputFile);
        int lineNum = 1;
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            r.readLine(line);
            lineNum++;
        }
        sc.close();

        ScheduleMap sch = new ScheduleMap();
        for (int i = 0; i < 5; i++) {
            if (!b.get(i).getBlock().isEmpty()) {
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
	
        // Display the schedule grid
		SwingUtilities.invokeLater(() -> {
			try {
				JFrame scheduleFrame = ScheduleGrid.createScheduleFrame();
				scheduleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				scheduleFrame.pack();
				scheduleFrame.setLocationRelativeTo(null);
				scheduleFrame.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
    }
}
