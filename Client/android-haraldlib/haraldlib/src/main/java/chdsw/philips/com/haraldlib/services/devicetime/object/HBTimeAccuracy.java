package chdsw.philips.com.haraldlib.services.devicetime.object;

public enum  HBTimeAccuracy {
    CorrectValue(0),
    OverMaximumValue(254),
    Unknow(255);

    HBTimeAccuracy(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBTimeAccuracy fromValue(int value) {
        for(HBTimeAccuracy type : values()) {
            if(type.getValue() == value)
                return type;
        }
        if(value >= 0 && value <= 253) {
            return CorrectValue;
        }
        return null;
    }
}
