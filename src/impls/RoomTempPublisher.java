package impls;

import common.exceptions.InvalidRequest;
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
    public RoomTempPublisher(ITempPublisher recorder, boolean isPush, long clockTimeInMS) throws Exception {
        if(recorder == null){
            throw new InvalidRequest("Invalid Request: null subject was provided");
        } else if(!(recorder instanceof IGetModeTempPublisher)){
            throw new InvalidRequest("Invalid Request: Trying to subscribe wrong instance");
        }
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

    public boolean removeObserver(ITempSubscriber tempObserver){
        for(int i = 0; i < subscribers.size(); ++i){
            if(subscribers.get(i) == tempObserver){
                subscribers.remove(i);
                return true;
            }
        }
        return false;
    }

    // Unregister itself from its observing subject
    @Override
    public boolean unRegister() {
        return recorder.removeObserver(this);
    }

    // Our subscriber from AC part to this subject will be pullers pulling every 30 seconds
    public void publish(){
        System.out.println(this.temperature); // for Testing
        for(ITempSubscriber s : subscribers){
            try {
                if(s.isPush()){
                    s.updateTemperature(new StateParam(temperature), this);
                } else{
                    s.updateTemperature(null, this);
                }
            } catch(Exception e){
                System.out.println(e.toString());
            }
        }
    }

    public void updateTemperature(StateParam sp, Object publisherObj) throws Exception {
        if(!isPush){// pull mode so sp is null
            if(publisherObj == null){
                throw new InvalidRequest("Invalid Request: Pull Model should get non nil publisher object");
            }
            if(publisherObj instanceof IGetModeTempPublisher){
                this.temperature = ((IGetModeTempPublisher) publisherObj).getTemperature();
            } else {
                throw new InvalidRequest("Invalid Request: wrong instanceof IGetModeTempPublisher");
            }
        } else { // Push mode so StateParm should not be nill
            if(sp == null){
                throw new InvalidRequest("Invalid Request: Push Model should get non nill state packet");
            }
            this.temperature = sp.getTemperature();
        }
    }

    public double getTemperature(){
        return temperature;
    }

    public boolean isPush() {
        return isPush;
    }

    public void printTemp(){
        System.out.println(this.temperature);
    }
}
