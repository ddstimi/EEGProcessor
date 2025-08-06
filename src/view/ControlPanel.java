package view;

import controller.EEGController;
import interfaces.ProcessingListener;
import model.EEGData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

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
    private boolean isPaused = false;
    public ControlPanel() {
        this.controller = new EEGController();

        controller.setProcessingListener(new ProcessingListener() {
            @Override
            public void onProcessingStarted() {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Processing file...");
                    statusLabel.setForeground(label);
                });
            }

            @Override
            public void onProcessingFinished() {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("File processed successfully!");
                    statusLabel.setForeground(validLabel);
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(false);
                });
            }

            @Override
            public void onProcessingError(Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Error: " + e.getMessage());
                    statusLabel.setForeground(Color.red);
                });
            }
        });

        fileChooser = new JFileChooser();
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        openButton.addActionListener(this::handleOpenButton);
        startButton.addActionListener(this::handleStartButton);
        pauseButton.addActionListener(this::handlePauseButton);

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

            statusLabel.setText("Processing file...");
            statusLabel.setForeground(label);
            startButton.setEnabled(false);
            openButton.setBackground(original);
            controller.startProcessing();
            statusLabel.setForeground(validLabel);
            statusLabel.setText("File processed succesfully!");
        } catch (Exception exception){
            statusLabel.setText("Error: " + exception);
            System.out.println("Error:"+exception);
            statusLabel.setForeground(Color.red);

        }
    }
    private void handlePauseButton(ActionEvent e) {
        if (!isPaused) {
            controller.pauseProcessing();
            pauseButton.setText("Resume");
            statusLabel.setText("Processing paused");
            statusLabel.setForeground(Color.ORANGE);
            isPaused = true;
        } else {
            controller.resumeProcessing();
            pauseButton.setText("Pause");
            statusLabel.setText("Processing resumed");
            statusLabel.setForeground(new Color(57, 238, 72, 226)); // validLabel
            isPaused = false;
        }
    }

    public JPanel getMainPanel() {
        return panel;
    }
}
