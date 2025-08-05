package controller;

import model.EEGData;

import java.io.*;

public class EEGController {
    private File selectedFile;
    private EEGData eegData;

    public void setSelectedFile(File file) {
        this.selectedFile = file;
        System.out.println("Selected file: " + file.getAbsolutePath());
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void startProcessing() {
        if (selectedFile == null) {
            System.out.println("No file selected. Cannot start processing.");
            return;
        }

        eegData = new EEGData();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(selectedFile))) {
            while (dis.available() > 0) {
                int sample = dis.readShort();
                eegData.addSample(sample);
            }

            System.out.println("Read " + eegData.getSampleCount() + " samples.");

        } catch (IOException e) {
            System.out.println("Error reading EEG file.");
            e.printStackTrace();
        }
    }
}
