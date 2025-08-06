package controller;

import model.EEGData;

import java.io.*;
import java.util.List;

import service.EEGReaderThread;
import service.SlidingWindowAverage;

public class EEGController {
    private File selectedFile;
    private EEGData eegData;
    private SlidingWindowAverage processor = new SlidingWindowAverage();



    public File setSelectedFile(File file) {
        this.selectedFile = file;
        return(file);
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void startProcessing() {
        if (selectedFile == null) {
            System.out.println("No file selected. Cannot start processing.");
            return;
        }

        EEGReaderThread reader = new EEGReaderThread(selectedFile, new EEGReaderThread.OnReadCompleteListener() {
            @Override
            public void onReadComplete(EEGData data) {
                eegData = data;

                System.out.println("Reading complete. Calculating averages...");
                List<List<Double>> slidingAverages = processor.computeSlidingAverage(eegData, 3);

                List<Double> channel4 = slidingAverages.get(3);
                for (int i = 0; i < Math.min(10, channel4.size()); i++) {
                    System.out.println("CH4 - Sample " + (i+1) + " avg: " + channel4.get(i+1));
                }
            }

            @Override
            public void onError(Exception e) {
                System.err.println("Error while reading EEG file:");
                e.printStackTrace();
            }
        });

        Thread readerThread = new Thread(reader);
        readerThread.start();
    }
}


