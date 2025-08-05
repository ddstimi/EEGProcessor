package controller;

import java.io.File;

public class EEGController {
    private File selectedFile;

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

        System.out.println("Starting EEG processing for: " + selectedFile.getName());
    }
}
