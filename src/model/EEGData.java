package model;

import java.util.ArrayList;
import java.util.List;

public class EEGData {
    private final List<List<Integer>> channels;

    public EEGData() {
        channels = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            channels.add(new ArrayList<>());
        }
    }

    public void addSampleToChannel(int channelIndex, int sample) {
        if (channelIndex < 1 || channelIndex > 32) {
            throw new IllegalArgumentException("Invalid channel index: " + channelIndex);
        }
        channels.get(channelIndex - 1).add(sample);
    }


    public List<Integer> getChannelSamples(int channelIndex) {
        if (channelIndex < 1 || channelIndex > 32) {
            throw new IllegalArgumentException("Invalid channel index: " + channelIndex);
        }
        return channels.get(channelIndex - 1);
    }

    public int getChannelCount() {
        return channels.size();
    }

    public int getSampleCountPerChannel() {
        return channels.get(0).size();
    }
}
