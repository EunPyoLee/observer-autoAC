package integration;

import impls.ACTempController;
import impls.ClockTempRecorder;
import impls.RoomTempPublisher;
import interfaces.ITempPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class AutoACTest {
    @Test
    public void testACTempController(){
        ITempPublisher recorder = new ClockTempRecorder(500); // Record temperature every 0.5 seconds
        RoomTempPublisher roomTempPublisher;
        ACTempController acTempController;
        try{
            roomTempPublisher = new RoomTempPublisher(recorder, false, 10000);
            // Our roomTempPublisher is getter from the ClockTempRecorder for every 10 second;
        } catch(Exception e){
            Assertions.assertTrue(false, "Valid RoomTempPublisher was given but failed");
            return;
        }
        try{
            acTempController = new ACTempController(roomTempPublisher);
        } catch(Exception e){
            Assertions.assertTrue(false, "Valid AcTempController was given but failed");
            return;
        }

        Timer timer = new Timer();
        boolean done = false;
        timer.schedule(new TimerTask() {
            int clockCounter = 0;
            @Override
            public void run() {
                // For ideal and precise testing, synchronization is needed
                double curTemp = acTempController.getThresholdTemperature();
                boolean isPowerOn = acTempController.getPowerState();
                if(curTemp < 25){
                    Assertions.assertFalse(isPowerOn);
                } else{
                    Assertions.assertTrue(isPowerOn);
                }
                if(++clockCounter == 6){ //6 * 10. So for 1 minutes
                    timer.cancel();
                }
            }
        }, new Date(), 10000);
        while(true){
            //Spinning for test
        }
    }
}
