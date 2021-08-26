package interfaces;

public interface ITempPublisher {
    public boolean register(ITempSubscriber tempObserver);
    public void removeObserver(ITempSubscriber tempObserver);
    public void publish();
}
