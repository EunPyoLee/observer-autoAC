package common.type;

// Parameter structure to pass down subject's states to observers
public class StateParam {
    private double temperature; // in celcius
    // More field can be added if the functionality gets upgraded and handle different facdtors like humid and e.t.c
    public StateParam(double temperature){
        this.temperature = temperature;
    }
    public double getTemperature() {
        return this.temperature;
    }
}
