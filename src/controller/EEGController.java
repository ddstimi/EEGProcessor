package controller;

import model.EEGData;

import java.io.*;
import java.util.List;

import service.ProcessorService;

public class EEGController {
    private File selectedFile;
    private EEGData eegData;
    private ProcessorService processor = new ProcessorService();


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
            try {
                while (true) {
                    for (int channel = 1; channel <= 32; channel++) {
                        short rawShort = dis.readShort();
                        eegData.addSampleToChannel(channel, rawShort);
                    }
                }
            } catch (EOFException e) {
                System.out.println("Ooops.");}
            List<List<Double>> slidingAverages = processor.computeSlidingAverage(eegData, 3);


            List<Double> channel4Averages = slidingAverages.get(4);

            System.out.println("Sliding averages for Channel 4 (first 10):");
            for (int i = 0; i < channel4Averages.size(); i++) {
                System.out.println("Sample " + (i + 1) + " average: " + channel4Averages.get(i));
            }
            return eegData;

        } catch (IOException e) {
            System.out.println("Error reading EEG file.");
            e.printStackTrace();
        }
        return null;
    }

}
