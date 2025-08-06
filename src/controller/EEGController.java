package controller;

import model.Sample;
import service.EEGReaderThread;
import service.EEGWriterThread;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EEGController {
    private File selectedFile;
    private AtomicBoolean readerFinished = new AtomicBoolean(false);

    public File setSelectedFile(File file) {
        this.selectedFile = file;
        return (file);
    }
    public String getSelectedFile() {
        return (this.selectedFile.getName());
    }

    public void startProcessing() {
        if (selectedFile == null) {
            System.out.println("No file selected.");
            return;
        }
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName=getSelectedFile();
        String outputDir = "output_files/" +fileName+"_"+ timestamp;
        try {
            Files.createDirectories(Path.of(outputDir));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ConcurrentHashMap<Integer, BlockingQueue<Sample>> channelQueues = new ConcurrentHashMap<>();
        for (int i = 1; i <= 32; i++) {
            channelQueues.put(i, new LinkedBlockingQueue<>());
        }

        for (int i = 1; i <= 32; i++) {
            Thread writer = new Thread(new EEGWriterThread(i, channelQueues.get(i), readerFinished, outputDir));            writer.start();
        }
        Thread reader = new Thread(new EEGReaderThread(selectedFile, channelQueues, readerFinished));
        reader.start();
    }
}