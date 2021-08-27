package interfaces;

import common.type.StateParam;

public interface ITempSubscriber {
    public void updateTemperature(StateParam sp, Object selfObj) throws Exception;
    public boolean isPush(); //true - push mode | false - pull mode
    public boolean unRegister();
}
