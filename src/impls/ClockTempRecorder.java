package impls;

import interfaces.ITempRecorder;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ClockTempRecorder implements ITempRecorder {
    private long clockTime; // in millisecond
    private double curTemp; // in celcius
    private Timer timer;
    public ClockTempRecorder(long clockTimeInMS){
        this.clockTime = clockTimeInMS;
        run();
    }

    private void run(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                recordTemperature();
                System.out.println(curTemp);
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
        curTemp = rand;
    }

    public double getTemperature(){
        return this.curTemp;
    }

}
