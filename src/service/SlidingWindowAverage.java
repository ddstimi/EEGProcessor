package service;

import model.EEGData;

import java.util.ArrayList;
import java.util.List;
public class SlidingWindowAverage {
    public List<List<Double>> computeSlidingAverage(EEGData data, int windowSize) {
        List<List<Double>> result = new ArrayList<>();
        int channelCount = data.getChannelCount();

        for (int ch = 1; ch <= channelCount; ch++) {
            List<Integer> samples = data.getChannelSamples(ch);
            List<Double> averages = new ArrayList<>();
            int n = samples.size();

            for (int i = 0; i <= n - windowSize; i++) {
                double sum = 0;
                for (int j = i; j < i + windowSize; j++) {
                    sum += samples.get(j);
                }
                averages.add(sum / windowSize);
            }
            result.add(averages);
        }
        return result;
    }

}