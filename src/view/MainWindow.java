package view;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("EEG Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ControlPanel controlPanel = new ControlPanel();
        add(controlPanel.getMainPanel());

        pack();
        setLocationRelativeTo(null);
    }
}