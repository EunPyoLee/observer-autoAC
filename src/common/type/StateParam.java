package common.type;

// Parameter structure to pass down subject's states to observers
public class StateParam {
    private float temperature; // in celcius
    // More field can be added if the functionality gets upgraded and handle different facdtors like humid and e.t.c
    public StateParam(int temperature){
        this.temperature = temperature;
    }
    public int getTemperature() {
        return this.temperature;
    }
}
