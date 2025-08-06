package service;

import model.Sample;

import java.io.*;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EEGReaderThread implements Runnable {
    private File file;
    private Map<Integer, BlockingQueue<Sample>> channelQueues;

    private AtomicBoolean readerFinished;

    public EEGReaderThread(File file, Map<Integer, BlockingQueue<Sample>> channelQueues, AtomicBoolean readerFinished){
        this.file = file;
        this.channelQueues = channelQueues;
        this.readerFinished = readerFinished;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            int[] sampleIndexes = new int[33];
            while (true) {
                for (int channel = 1; channel <= 32; channel++) {
                    int low = dis.readUnsignedByte();
                    int high = dis.readUnsignedByte();
                    short rawShort = (short) ((high << 8) | low);

                    sampleIndexes[channel]++;
                    Sample sample = new Sample(sampleIndexes[channel], rawShort);

                    channelQueues.get(channel).put(sample);
                }
            }



        } catch (EOFException e) {
        System.out.println("Finished reading the file.");
        readerFinished.set(true);
    }
 catch (Exception ex) {
            System.err.println("Error while reading EEG file:");
            ex.printStackTrace();
        }
    }

}
