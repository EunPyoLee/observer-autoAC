import impls.ClockTempRecorder;
import impls.RoomTempPublisher;
import interfaces.ITempPublisher;

public class Main {
    public static void main(String []args){
        ITempPublisher recorder = new ClockTempRecorder(3000);
        ITempPublisher roomTempPubliserPush = new RoomTempPublisher(recorder, true, 10000);
        ITempPublisher roomTempPublisherPull = new RoomTempPublisher(recorder, false, 15000);
    }
}
