package impls;

import common.exceptions.InvalidRequest;
import common.exceptions.ServiceUnavailable;
import common.type.StateParam;
import interfaces.IGetModeTempPublisher;
import interfaces.ITempPublisher;
import interfaces.ITempSubscriber;

import java.util.Objects;

// Observes RoomTempPublisher
public class ACTempController implements ITempSubscriber {
    private double thresholdTemperature;
    private double roomTemperature;
    private boolean power;
    private boolean isPush;
    ITempPublisher acTempController;
    public ACTempController(ITempPublisher acTempController) throws Exception{
        // Default auto threshold is 25 celcius
        // This controller uses pull model that regularly pulls data
        power = true;
        thresholdTemperature = 25;
        this.isPush = false;
        if(acTempController == null){
            throw new InvalidRequest("Invalid Request: null subject was provided");
        } else if(!(acTempController instanceof RoomTempPublisher)){
            throw new InvalidRequest("Invalid Request: Trying to subscribe wrong instance");
        }
        this.acTempController = acTempController;
        acTempController.register(this);
    }
    public void setThresholdTemperature(double temperature){
        thresholdTemperature = temperature;
    }

    public double getThresholdTemperature(){
        return thresholdTemperature;
    }

    public boolean getPowerState(){
        return power;
    }

    @Override
    public void updateTemperature(StateParam sp, Object publisherObj) throws Exception {
        if(!isPush){// pull mode so sp is null
            if(publisherObj == null){
                throw new InvalidRequest("Invalid Request: Pull Model should get non nil publisher object");
            }
            if(publisherObj instanceof RoomTempPublisher){
                roomTemperature = ((RoomTempPublisher) publisherObj).getTemperature();
                if(roomTemperature >= thresholdTemperature && !power){
                    power = true;
                    System.out.println("Room temperature is high. Re-activate AC");
                } else if(roomTemperature < thresholdTemperature && power){
                    power = false;
                    System.out.println(("Room temperature is low. Deactivate AC"));
                }
            } else{
                throw new InvalidRequest("Invalid Request: wrong instanceof IGetModeTempPublisher");
            }
        } else { // Push mode so StateParm should not be nill
            throw new ServiceUnavailable("This model currently supports only pull model");
        }
    }

    @Override
    public boolean isPush() {
        return isPush;
    }

    @Override
    public boolean unRegister() {
        return acTempController.removeObserver(this);
    }
}
