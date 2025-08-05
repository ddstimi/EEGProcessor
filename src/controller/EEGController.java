package controller;

import model.EEGData;

import java.io.*;

public class EEGController {
    private File selectedFile;
    private EEGData eegData;

    public File setSelectedFile(File file) {
        this.selectedFile = file;
        return(file);
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public EEGData startProcessing() {
        if (selectedFile == null) {
            System.out.println("No file selected. Cannot start processing.");
            return null;
        }

        eegData = new EEGData();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(selectedFile))) {
            while (dis.available() > 0) {
                int sample = dis.readShort();
                eegData.addSample(sample);
            }

            return eegData;

        } catch (IOException e) {
            System.out.println("Error reading EEG file.");
            e.printStackTrace();
        }
        return null;
    }
}
