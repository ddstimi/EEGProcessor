package interfaces;

public interface ProcessingListener {
    void onProcessingStarted();
    void onProcessingFinished();
    void onProcessingError(Exception e);
}
