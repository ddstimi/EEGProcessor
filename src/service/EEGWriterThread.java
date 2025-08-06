package service;

import model.Sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class EEGWriterThread implements Runnable {
    private int channelId;
    private BlockingQueue<Sample> queue;
    private List<Sample> window = new ArrayList<>();
    private BufferedWriter writer;
    private AtomicBoolean readerFinished;
    public EEGWriterThread(int channelId, BlockingQueue<Sample> queue, AtomicBoolean readerFinished){
        this.channelId = channelId;
        this.queue = queue;
        try {
            writer = new BufferedWriter(new FileWriter("channel" + channelId + ".csv"));
            writer.write("Sample,Average\n");
            this.readerFinished = readerFinished;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Sample sample = queue.poll(100, TimeUnit.MILLISECONDS);
                if (sample == null) {
                    if (readerFinished.get() && queue.isEmpty()) {
                        break;
                    }
                    continue;
                }

                window.add(sample);

                if (window.size() > 3) {
                    window.remove(0);
                }

                if (window.size() == 3) {
                    double avg = (window.get(0).value + window.get(1).value + window.get(2).value) / 3.0;
                    writer.write(
                            window.get(0).index + "-" + window.get(1).index + "-" + window.get(2).index
                                    + "," + avg + "\n"
                    );
                    writer.flush();
                }

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
