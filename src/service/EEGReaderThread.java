package service;

import java.io.*;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import model.Sample;

public class EEGReaderThread implements Runnable {
    private File file;
    private Map<Integer, BlockingQueue<Sample>> channelQueues;

    private AtomicBoolean readerFinished;
    private final AtomicBoolean paused;
    private final AtomicBoolean stopped;
    public EEGReaderThread(File file, Map<Integer, BlockingQueue<Sample>> channelQueues, AtomicBoolean readerFinished,AtomicBoolean paused,AtomicBoolean stopped){
        this.file = file;
        this.channelQueues = channelQueues;
        this.readerFinished = readerFinished;
        this.paused=paused;
        this.stopped=stopped;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            int[] sampleIndexes = new int[33];
            while (!stopped.get()) {
                if (paused.get()) {
                    Thread.sleep(100);
                    continue;
                }

                    for (int channel = 1; channel <= 32; channel++) {
                        if (stopped.get()) break;
                        int low = dis.readUnsignedByte();
                        int high = dis.readUnsignedByte();
                        short rawShort = (short) ((low & 0xFF) | ((high & 0xFF) << 8));

                        sampleIndexes[channel]++;
                        Sample sample = new Sample(sampleIndexes[channel], rawShort);

                        channelQueues.get(channel).put(sample);
                    }

            }



        } catch (EOFException e) {
        //System.out.println("Finished reading the file.");
        readerFinished.set(true);
    } catch (Exception ex) {
            System.err.println("Error while reading EEG file:");
            ex.printStackTrace();
        }
    }


}
