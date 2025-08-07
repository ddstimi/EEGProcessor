package controller;

import interfaces.ProcessingListener;
import model.Sample;
import service.EEGReaderThread;
import service.EEGWriterThread;

import javax.swing.*;
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
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private ProcessingListener listener;
    private AtomicBoolean stopped = new AtomicBoolean(false);


    public File setSelectedFile(File file) {
        this.selectedFile = file;
        return (file);
    }
    public String getSelectedFile() {
        return (this.selectedFile.getName());
    }

    public void setProcessingListener(ProcessingListener listener) {
        this.listener = listener;
    }

    public void startProcessing() {
        if (selectedFile == null) {
            if (listener != null) listener.onProcessingError(new IllegalStateException("No file selected"));
            return;
        }

        if (listener != null) listener.onProcessingStarted();
        if (selectedFile == null) {
            //System.out.println("No file selected.");
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
            Thread writer = new Thread(new EEGWriterThread(i, channelQueues.get(i), readerFinished, outputDir, paused,stopped));
            writer.start();
        }
        stopped.set(false);
        Thread reader = new Thread(() -> {
            try {
                new EEGReaderThread(selectedFile, channelQueues, readerFinished, paused,stopped).run();
                if (!stopped.get() && listener != null) {
                    SwingUtilities.invokeLater(listener::onProcessingFinished);
                }
            } catch (Exception e) {
                if (listener != null) {
                    SwingUtilities.invokeLater(() -> listener.onProcessingError(e));
                }
            }
        });
        reader.start();
    }

    public void pauseProcessing() {
        paused.set(true);
    }

    public void resumeProcessing() {
        paused.set(false);
        stopped.set(false);
    }
    public void stopProcessing() {
        stopped.set(true);
    }


}