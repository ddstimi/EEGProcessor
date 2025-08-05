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
    private JLabel fileNameLabel;
    private JLabel statusLabel;
    private JFileChooser fileChooser;
    private Color valid = new Color(27, 206, 42, 200);
    private Color validLabel = new Color(57, 238, 72, 226);
    private Color original = new Color(100,147,142);
    private Color label = new Color(180, 243, 235);


    private final EEGController controller;

    public ControlPanel() {
        this.controller = new EEGController();
        fileChooser = new JFileChooser();
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        openButton.addActionListener(this::handleOpenButton);
        startButton.addActionListener(this::handleStartButton);

    }

    private void handleOpenButton(ActionEvent e) {
        if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileNameLabel.setForeground(label);
            fileNameLabel.setText("Selected file: "+controller.setSelectedFile(file).getName());
            openButton.setBackground(valid);
        }
        if (openButton.getBackground()==valid){
            startButton.setEnabled(true);
        }

    }

    private void handleStartButton(ActionEvent e){
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        try {
            statusLabel.setForeground(label);
            statusLabel.setText("Processing file...");
            startButton.setEnabled(false);
            openButton.setBackground(original);
            statusLabel.setText("Read " + controller.startProcessing().getSampleCount() + " samples.");
            statusLabel.setForeground(validLabel);
        } catch (Exception exception){
            statusLabel.setText("Error: " + exception);
            statusLabel.setForeground(Color.red);

        }
    }

    public JPanel getMainPanel() {
        return panel;
    }
}
