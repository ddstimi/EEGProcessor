package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ControlPanel {
    private JPanel panel;
    private JButton openButton;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JFileChooser fileChooser;

    public ControlPanel() {
        fileChooser = new JFileChooser();
        openButton.addActionListener(this::handleOpenButton);
    }

    private void handleOpenButton(ActionEvent e) {
        if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
            System.out.println("Selected: " + fileChooser.getSelectedFile());
        }
    }

    public JPanel getMainPanel() {
        return panel;
    }
}