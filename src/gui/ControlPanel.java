package gui;

import controller.EEGController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ControlPanel {
    private JPanel panel;
    private JButton openButton;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JFileChooser fileChooser;

    private final EEGController controller;

    public ControlPanel() {
        this.controller = new EEGController();
        fileChooser = new JFileChooser();

        openButton.addActionListener(this::handleOpenButton);
    }

    private void handleOpenButton(ActionEvent e) {
        if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.setSelectedFile(file);
        }
    }

    public JPanel getMainPanel() {
        return panel;
    }
}
