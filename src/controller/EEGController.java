package controller;

import model.Sample;
import service.EEGReaderThread;
import service.EEGWriterThread;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EEGController {
    private File selectedFile;
    private AtomicBoolean readerFinished = new AtomicBoolean(false);

    public File setSelectedFile(File file) {
        this.selectedFile = file;
        return (file);
    }

    public void startProcessing() {
        if (selectedFile == null) {
            System.out.println("No file selected.");
            return;
        }

        ConcurrentHashMap<Integer, BlockingQueue<Sample>> channelQueues = new ConcurrentHashMap<>();
        for (int i = 1; i <= 32; i++) {
            channelQueues.put(i, new LinkedBlockingQueue<>());
        }

        for (int i = 1; i <= 32; i++) {
            Thread writer = new Thread(new EEGWriterThread(i, channelQueues.get(i), readerFinished));
            writer.start();
        }
        Thread reader = new Thread(new EEGReaderThread(selectedFile, channelQueues, readerFinished));
        reader.start();
    }
}