package interfaces;

public interface ITempPublisher {
    public boolean register(ITempSubscriber tempObserver);
    public boolean removeObserver(ITempSubscriber tempObserver);
    public void publish();
}
