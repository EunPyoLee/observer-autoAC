package interfaces;

import common.type.StateParam;

public interface ITempSubscriber {
    public void updateTemperature(StateParam sp, ITempPublisher selfObj);
    public boolean isPush(); //true - push mode | false - pull mode
}
