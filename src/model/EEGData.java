package model;

import java.util.ArrayList;
import java.util.List;

public class EEGData {
    private final List<Integer> samples = new ArrayList<>();

    public void addSample(int sample) {
        samples.add(sample);
    }

    public int getSampleCount() {
        return samples.size();
    }

    public List<Integer> getSamples() {
        return samples;
    }
}
