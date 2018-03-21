package chdsw.philips.com.haraldlib.services.devicetime.object;

public enum HBTimeSource {
    Unknown(0),
    NetworkTimeProtocol(1),
    GPS(2),
    RadioTimeSignal(3),
    Manual(4),
    AtomicClock(5),
    CellularNetwork(6);

    HBTimeSource(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBTimeSource fromValue(int value) {
        for(HBTimeSource type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
