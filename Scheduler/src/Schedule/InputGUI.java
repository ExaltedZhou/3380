

package schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InputGUI {

    public static void GUI (){
        SwingUtilities.invokeLater(() -> {
            int numClasses = promptForNumberOfClasses();
            createAndShowGUI(numClasses);
        });    
    }
    
    private static int promptForNumberOfClasses() {
        String input = JOptionPane.showInputDialog("Enter the number of Activities:");
        return Integer.parseInt(input);
    }

    private static void createAndShowGUI(int numActivity) {
        JFrame frame = new JFrame("Activity Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(numActivity * 5 + 1, 2, 3, 4));
        
        JLabel[] dayLabels = new JLabel[numActivity];
        JTextField[] dayNames = new JTextField[numActivity];
        JLabel[] activityLabels = new JLabel[numActivity];
        JTextField[] activityNames = new JTextField[numActivity];
        JLabel[] startTimeLabels = new JLabel[numActivity];
        JTextField[] startTimes = new JTextField[numActivity];
        JLabel[] endTimeLabels = new JLabel[numActivity];
        JTextField[] endTimes = new JTextField[numActivity];
        JLabel[] activityTypeLabels = new JLabel[numActivity];
        JTextField[] activityTypes = new JTextField[numActivity];

        for (int i = 0; i < numActivity; i++) {
            
            dayLabels[i] = new JLabel("Activity " + (i + 1) + " Day:");
            dayNames[i] = new JTextField(10);
            panel.add(dayLabels[i]);
            panel.add(dayNames[i]);

            activityLabels[i] = new JLabel("Activity " + (i + 1) + " Name:");
            activityNames[i] = new JTextField(10);
            panel.add(activityLabels[i]);
            panel.add(activityNames[i]);

            startTimeLabels[i] = new JLabel("Activity " + (i + 1) + " Start Time:");
            startTimes[i] = new JTextField(10);
            panel.add(startTimeLabels[i]);
            panel.add(startTimes[i]);

            endTimeLabels[i] = new JLabel("Activity " + (i + 1) + " End Time:");
            endTimes[i] = new JTextField(10);
            panel.add(endTimeLabels[i]);
            panel.add(endTimes[i]);
            
            activityTypeLabels[i] = new JLabel("Activity " + (i + 1) + " Type:");
            activityTypes[i] = new JTextField(10);
            panel.add(activityTypeLabels[i]);
            panel.add(activityTypes[i]);
        }

        JButton submitButton = new JButton("Submit");
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File outputFile = new File("input.txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    for (int i = 0; i < numActivity; i++) {
                        String classInfo = dayNames[i].getText() + ", " + activityNames[i].getText() + ", " + startTimes[i].getText() + ", " + endTimes[i].getText() + ", " + activityTypes[i].getText();
                        writer.write(classInfo);
                        writer.newLine();
                    }

                    frame.dispose();
                    JOptionPane.showMessageDialog(null, "Entries Saved");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(new JLabel());
        panel.add(submitButton);

        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
