package chdsw.philips.com.haraldtestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeListener;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBReferenceTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBSTDOffset;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class CurrentTimeFragment extends Fragment {
    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;

    private TextView dateTimeTextView;
    private TextView dayOfWeekTextView;
    private TextView fractions256TextView;
    private TextView adjustReasonTextView;
    private Button getCurrentTimeButton;
    private Button setCurrentTimeButton;

    private TextView timeZoneTextView;
    private TextView stdOffsetTextView;
    private Button getLocalTimeInformationButton;
    private Button setLocalTimeInformationButton;

    private TextView timeSourceTextView;
    private TextView accuracyTextView;
    private TextView daysSinceUpdateTextView;
    private TextView hoursSinceUpdateTextView;
    private Button getReferenceTimeInformationButton;

    private HBCurrentTimeListener hbCurrentTimeListener = new HBCurrentTimeListener() {
        @Override
        public void onCurrentTime(String deviceAddress, HBCurrentTime currentTime) {
            setCurrentTime(currentTime);
        }

        @Override
        public void onLocalTimeInformation(String deviceAddress, HBLocalTimeInformation localTimeInformation) {
            setLocalTimeInformation(localTimeInformation);
        }

        @Override
        public void onReferenceTimeInformation(String deviceAddress, HBReferenceTimeInformation referenceTimeInformation) {
            setReferenceTimeInformation(referenceTimeInformation);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getContext();
        this.hbCentral = HBCentralInstance.getInstance(this.context).getHBCentral();
        this.deviceAddress = getArguments().getString(DEVICE_ADDRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_time, container, false);

        getCurrentTimeButton = (Button) rootView.findViewById(R.id.getCurrentTimeButton);
        getCurrentTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getCurrentTime(deviceAddress);
            }
        });

        setCurrentTimeButton = (Button) rootView.findViewById(R.id.setCurrentTimeButton);
        setCurrentTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HBCurrentTime currentTime = new HBCurrentTime();
                currentTime.setDateTime(new Date());
                currentTime.setAdjustReason(0x00000001);
                hbCentral.setCurrentTime(deviceAddress, currentTime);
            }
        });

        dateTimeTextView = (TextView) rootView.findViewById(R.id.dateTimeTextView);
        dayOfWeekTextView = (TextView) rootView.findViewById(R.id.dayOfWeekTextView);
        fractions256TextView = (TextView) rootView.findViewById(R.id.fractions256TextView);
        adjustReasonTextView = (TextView) rootView.findViewById(R.id.adjustReasonTextView);

        getLocalTimeInformationButton = (Button) rootView.findViewById(R.id.getLocalTimeInformationButton);
        getLocalTimeInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getLocalTimeInformation(deviceAddress);
            }
        });

        setLocalTimeInformationButton = (Button) rootView.findViewById(R.id.setLocalTimeInformationButton);
        setLocalTimeInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HBLocalTimeInformation localTimeInformation = new HBLocalTimeInformation();
                localTimeInformation.setTimeZone(0);
                localTimeInformation.setSTDOffset(HBSTDOffset.DaylightTime);
                hbCentral.setLocalTimeInformation(deviceAddress, localTimeInformation);
            }
        });

        timeZoneTextView = (TextView) rootView.findViewById(R.id.timeZoneTextView);
        stdOffsetTextView = (TextView) rootView.findViewById(R.id.stdOffsetTextView);

        getReferenceTimeInformationButton = (Button) rootView.findViewById(R.id.getReferenceTimeInformationButton);
        getReferenceTimeInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getReferenceTimeInformation(deviceAddress);
            }
        });

        timeSourceTextView = (TextView) rootView.findViewById(R.id.timeSourceTextView);
        accuracyTextView = (TextView) rootView.findViewById(R.id.accuracyTextView);
        daysSinceUpdateTextView = (TextView) rootView.findViewById(R.id.daysSinceUpdateTextView);
        hoursSinceUpdateTextView = (TextView) rootView.findViewById(R.id.hoursSinceUpdateTextView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addCurrentTimeServiceListener(this.deviceAddress, this.hbCurrentTimeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeCurrentTimeServiceListener(this.deviceAddress);
    }

    private void setCurrentTime(HBCurrentTime currentTime) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        dateTimeTextView.setText(dateFormat.format(currentTime.getDateTime()));
        dayOfWeekTextView.setText(String.valueOf(currentTime.getDayOfWeek()));
        fractions256TextView.setText(String.valueOf(currentTime.getFractions256()));
        adjustReasonTextView.setText(String.valueOf(currentTime.getAdjustReason()));
    }

    private void setLocalTimeInformation(HBLocalTimeInformation localTimeInformation) {
        timeZoneTextView.setText(String.valueOf(localTimeInformation.getTimeZone()));
        stdOffsetTextView.setText(localTimeInformation.getSTDOffset().toString());
    }

    private void setReferenceTimeInformation(HBReferenceTimeInformation referenceTimeInformation) {
        timeSourceTextView.setText(referenceTimeInformation.getTimeSource().toString());
        accuracyTextView.setText(String.valueOf(referenceTimeInformation.getAccuracy()));
        daysSinceUpdateTextView.setText(String.valueOf(referenceTimeInformation.getDaysSinceUpdate()));
        hoursSinceUpdateTextView.setText(String.valueOf(referenceTimeInformation.getHoursSinceUpdate()));
    }
}