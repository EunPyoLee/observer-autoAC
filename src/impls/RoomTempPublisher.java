package impls;

import common.type.StateParam;
import interfaces.IGetModeTempPublisher;
import interfaces.ITempPublisher;
import interfaces.ITempSubscriber;

import java.util.*;

public class RoomTempPublisher implements ITempPublisher, ITempSubscriber {
    private ITempPublisher recorder;
    private double temperature;
    private List<ITempSubscriber> subscribers;
    private boolean isPush;
    private long clockTime;
    private Timer timer;
    public RoomTempPublisher(ITempPublisher recorder, boolean isPush, long clockTimeInMS) {
        this.recorder = recorder;
        if(!recorder.register(this)){
            System.out.println("Duplicate Reigstration");
        }
        subscribers = new ArrayList<>();
        this.isPush = isPush;
        this.clockTime = clockTimeInMS;
        run();
    }

    private void run (){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                publish();
            }
        };
        timer = new Timer();
        timer.schedule(task, new Date(), clockTime);
    }

    public boolean register(ITempSubscriber tempObserver){
        for(ITempSubscriber s : subscribers){
            if(s == tempObserver){
                return false;
            }
        }
        subscribers.add(tempObserver);
        return true;
    }

    public void removeObserver(ITempSubscriber tempObserver){
        for(int i = 0; i < subscribers.size(); ++i){
            if(subscribers.get(i) == tempObserver){
                subscribers.remove(i);
                break;
            }
        }
    }

    // Our subscriber from AC part to this subject will be pullers pulling every 30 seconds
    public void publish(){
        System.out.println(this.temperature); // for Testing
        for(ITempSubscriber s : subscribers){
            if(s.isPush()){
                s.updateTemperature(new StateParam(temperature), this);
            } else{
                s.updateTemperature(null, this);
            }
        }
    }

    public void updateTemperature(StateParam sp, ITempPublisher publisherObj) {
        if(!isPush){// pull mode so sp is null
            if(publisherObj == null){
                return; // ignore this invalid publishing
            }
            if(publisherObj instanceof IGetModeTempPublisher){
                this.temperature = ((IGetModeTempPublisher) publisherObj).getTemperature();
            }
        } else { // Push mode so StateParm should not be nill
            if(sp == null){
                return; // ignore this inalid publishing
            }
            this.temperature = sp.getTemperature();
        }
    }

    public boolean isPush() {
        return isPush;
    }

    public void printTemp(){
        System.out.println(this.temperature);
    }
}
