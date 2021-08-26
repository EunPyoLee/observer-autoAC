package impls;

import common.type.StateParam;
import interfaces.IGetModeTempPublisher;
import interfaces.ITempPublisher;
import interfaces.ITempSubscriber;

import java.util.*;

public class ClockTempRecorder implements ITempPublisher, IGetModeTempPublisher {
    private long clockTime; // in millisecond
    private double temperature; // in celcius
    private Timer timer;
    private List<ITempSubscriber> subscribers;
    public ClockTempRecorder(long clockTimeInMS){
        subscribers = new ArrayList<ITempSubscriber>();
        this.clockTime = clockTimeInMS;
        run();
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

    public void publish(){
        for(ITempSubscriber s : subscribers){
            if(s.isPush()){
                s.updateTemperature(new StateParam(temperature), this);
            } else{
                s.updateTemperature(null, this);
            }
        }
    }

    private void run(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                recordTemperature();
            }
        };
        timer = new Timer();
        timer.schedule(task, new Date(), clockTime);
    }

    public void recordTemperature(){
        /*
        No hardware is available for this project yet,
        so we mock this one with random value

        Assume room temperature's range is [18,32]
         */
        double rand = 18 + Math.random() * (32 - 18);
        double prev = temperature;
        temperature = rand;
        if(prev != temperature){
            publish();
        }
    }

    public double getTemperature(){
        return temperature;
    }

}
