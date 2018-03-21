package chdsw.philips.com.haraldlib.services.devicetime.object;

public enum HBEventLogType {
    TimeFault(0),
    TimeUpdate(1),
    UserTimeChange(2),
    MaxRTCDriftLimitReached(3),
    DTParameterChanged(4),
    ConsolidatedLogEntryCounter(5),
    ConsolidatedLogEntryCounterLSB(6),
    ConsolidatedLogEntryCounterMSB(7);

    HBEventLogType(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBEventLogType fromValue(int value) {
        for(HBEventLogType type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
