package chdsw.philips.com.haraldlib.services.devicetime.object;

public class HBDeviceTimeFeature {
    private boolean e2eCrcFlag;
    private boolean timeChangeLoggingFlag;
    private boolean baseTimeSecondFractionsSupportedFlag;
    private boolean timeOrDateDisplayedToUserFlag;
    private boolean displayedFormatsSupportedFlag;
    private boolean displayedFormatsChangeableFlag;
    private boolean userAllowedToSetTimeDateFlag;
    private boolean authorizationSupportedFlag;
    private boolean rtcDriftTrackingFlag;

    private static final int E2ECRC_POSITION = 0x01;
    private static final int TIME_CHANGE_LOGGING_POSITION = 0x02;
    private static final int BASE_TIME_SECOND_FRACTIONS_POSITION = 0x04;
    private static final int TIME_OR_DATE_DISPLAYED_TO_USER_POSITION = 0x08;
    private static final int DISPLAYED_FORMATS_SUPPORTED_POSITION = 0x10;
    private static final int DISPLAYED_FORMATS_CHANGEABLE_POSITION = 0x20;
    private static final int USER_ALLOWED_TO_SET_TIME_DATE_POSITION = 0x40;
    private static final int AUTHORIZATION_SUPPORTED_POSITION = 0x80;
    private static final int RTC_DRIFT_TRACKING_POSITION = 0x100;

    public HBDeviceTimeFeature(int flags) {
        e2eCrcFlag = ((flags & E2ECRC_POSITION) == E2ECRC_POSITION);
        timeChangeLoggingFlag = ((flags & TIME_CHANGE_LOGGING_POSITION) == TIME_CHANGE_LOGGING_POSITION);
        baseTimeSecondFractionsSupportedFlag = ((flags & BASE_TIME_SECOND_FRACTIONS_POSITION) == BASE_TIME_SECOND_FRACTIONS_POSITION);
        timeOrDateDisplayedToUserFlag = ((flags & TIME_OR_DATE_DISPLAYED_TO_USER_POSITION) == TIME_OR_DATE_DISPLAYED_TO_USER_POSITION);
        displayedFormatsSupportedFlag = ((flags & DISPLAYED_FORMATS_SUPPORTED_POSITION) == DISPLAYED_FORMATS_SUPPORTED_POSITION);
        displayedFormatsChangeableFlag = ((flags & DISPLAYED_FORMATS_CHANGEABLE_POSITION) == DISPLAYED_FORMATS_CHANGEABLE_POSITION);
        userAllowedToSetTimeDateFlag = ((flags & USER_ALLOWED_TO_SET_TIME_DATE_POSITION) == USER_ALLOWED_TO_SET_TIME_DATE_POSITION);
        authorizationSupportedFlag = ((flags & AUTHORIZATION_SUPPORTED_POSITION) == AUTHORIZATION_SUPPORTED_POSITION);
        rtcDriftTrackingFlag = ((flags & RTC_DRIFT_TRACKING_POSITION) == RTC_DRIFT_TRACKING_POSITION);
    }

    public boolean isE2eCrcFlag() {
        return e2eCrcFlag;
    }

    public boolean isTimeChangeLoggingFlag() {
        return timeChangeLoggingFlag;
    }

    public boolean isBaseTimeSecondFractionsSupportedFlag() {
        return baseTimeSecondFractionsSupportedFlag;
    }

    public boolean isTimeOrDateDisplayedToUserFlag() {
        return timeOrDateDisplayedToUserFlag;
    }

    public boolean isDisplayedFormatsSupportedFlag() {
        return displayedFormatsSupportedFlag;
    }

    public boolean isDisplayedFormatsChangeableFlag() {
        return displayedFormatsChangeableFlag;
    }

    public boolean isUserAllowedToSetTimeDateFlag() {
        return userAllowedToSetTimeDateFlag;
    }

    public boolean isAuthorizationSupportedFlag() {
        return authorizationSupportedFlag;
    }

    public boolean isRtcDriftTrackingFlag() {
        return rtcDriftTrackingFlag;
    }
}
