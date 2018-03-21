package chdsw.philips.com.haraldlib.services.devicetime.object;

public class HBDeviceTimeStatus {
    private boolean timeFault;
    private boolean utcAligned;
    private boolean qualifiedLocalTimeSynchronized;
    private boolean proposeTimeUpdateRequest;
    private boolean userTimeManuallySet;
    private boolean timeLastSetByClient;
    private boolean epochYear2000;

    private static final int TIMEFAULT_POSITION = (0x01);
    private static final int UTC_ALIGNED_POSITION = (0x02);
    private static final int QUALIFIED_LOCAL_TIME_SYNCHRONIZED_POSITION = (0x04);
    private static final int PROPOSE_TIME_UPDATE_REQUEST_POSITION = (0x08);
    private static final int USERTIME_MANUALLY_SET_POSITION = (0x10);
    private static final int TIME_LAST_SET_BY_CLIENT_POSITION = (0x20);
    private static final int EPOCH_YEAR_2000_POSITION = (0x40);

    public HBDeviceTimeStatus(int flags) {
        timeFault = ((flags & TIMEFAULT_POSITION) == TIMEFAULT_POSITION);
        utcAligned = ((flags & UTC_ALIGNED_POSITION) == UTC_ALIGNED_POSITION);
        qualifiedLocalTimeSynchronized = ((flags & QUALIFIED_LOCAL_TIME_SYNCHRONIZED_POSITION) == QUALIFIED_LOCAL_TIME_SYNCHRONIZED_POSITION);
        proposeTimeUpdateRequest = ((flags & PROPOSE_TIME_UPDATE_REQUEST_POSITION) == PROPOSE_TIME_UPDATE_REQUEST_POSITION);
        userTimeManuallySet = ((flags & USERTIME_MANUALLY_SET_POSITION) == USERTIME_MANUALLY_SET_POSITION);
        timeLastSetByClient = ((flags & TIME_LAST_SET_BY_CLIENT_POSITION) == TIME_LAST_SET_BY_CLIENT_POSITION);
        epochYear2000 = ((flags & EPOCH_YEAR_2000_POSITION) == EPOCH_YEAR_2000_POSITION);
    }

    public boolean isTimeFault() {
        return timeFault;
    }

    public boolean isUtcAligned() {
        return utcAligned;
    }

    public boolean isQualifiedLocalTimeSynchronized() {
        return qualifiedLocalTimeSynchronized;
    }

    public boolean isProposeTimeUpdateRequest() {
        return proposeTimeUpdateRequest;
    }

    public boolean isUserTimeManuallySet() {
        return userTimeManuallySet;
    }

    public boolean isTimeLastSetByClient() {
        return timeLastSetByClient;
    }

    public boolean isEpochYear2000() {
        return epochYear2000;
    }
}
