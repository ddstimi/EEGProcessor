package gui;

import controller.EEGController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class ControlPanel {
    private JPanel panel;
    private JButton openButton;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JFileChooser fileChooser;
    private Color valid = new Color(12, 145, 23, 200);
    private Color original = new Color(119,176,170);

    private final EEGController controller;

    public ControlPanel() {
        this.controller = new EEGController();
        fileChooser = new JFileChooser();
        startButton.setEnabled(false);

        openButton.addActionListener(this::handleOpenButton);
        startButton.addActionListener(this::handleStartButton);

    }

    private void handleOpenButton(ActionEvent e) {
        if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.setSelectedFile(file);
            openButton.setBackground(valid);
        }
        if (openButton.getBackground()==valid) startButton.setEnabled(true);

    }

    private void handleStartButton(ActionEvent e){
        try {
            startButton.setEnabled(false);
            openButton.setBackground(original);
            controller.startProcessing();
        } catch (Exception exception){
            System.out.println("Error: " + exception);
        }
    }

    public JPanel getMainPanel() {
        return panel;
    }
}
