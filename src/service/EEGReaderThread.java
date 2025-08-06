package service;

import model.EEGData;

import java.io.*;

public class EEGReaderThread implements Runnable {
    private File file;
    private EEGData eegData;
    private OnReadCompleteListener listener;

    public interface OnReadCompleteListener {
        void onReadComplete(EEGData data);
        void onError(Exception e);
    }

    public EEGReaderThread(File file, OnReadCompleteListener listener) {
        this.file = file;
        this.listener = listener;
        this.eegData = new EEGData();
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            while (true) {
                for (int channel = 1; channel <= 32; channel++) {
                    short rawShort = dis.readShort();
                    eegData.addSampleToChannel(channel, rawShort);
                }
            }
        } catch (EOFException e) {
            listener.onReadComplete(eegData);
        } catch (Exception ex) {
            listener.onError(ex);
        }
    }
}
