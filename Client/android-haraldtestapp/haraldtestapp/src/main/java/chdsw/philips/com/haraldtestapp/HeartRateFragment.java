package chdsw.philips.com.haraldtestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.services.heartrate.HBHeartRateListener;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBBodySensorLocation;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBHeartRateMeasurement;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class HeartRateFragment extends Fragment {
    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;
    private TextView heartRateTextView;
    private TextView energyExpendedTextView;
    private TextView RRIntervalTextView;
    private TextView dateTimeTextView;
    private Button getBodySensorLocationButton;
    private TextView bodySensorLocationTextView;
    private Button resetEnergyExpendedButton;

    private HBHeartRateListener hbHeartRateListener = new HBHeartRateListener() {
        @Override
        public void onHeartRateMeasurement(String deviceAddress, HBHeartRateMeasurement hbHeartRateMeasurement) {
            heartRateTextView.setText(String.valueOf(hbHeartRateMeasurement.getHeartRateMeasurementValue()));
            energyExpendedTextView.setText(String.valueOf(hbHeartRateMeasurement.getEnergyExpended()));
            RRIntervalTextView.setText(String.valueOf(hbHeartRateMeasurement.getRRInterval()));
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            dateTimeTextView.setText(dateFormat.format(new GregorianCalendar().getTime()));
        }

        @Override
        public void onBodySensorLocation(String deviceAddress, HBBodySensorLocation hbBodySensorLocation) {
            bodySensorLocationTextView.setText(hbBodySensorLocation.name());
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
        View rootView = inflater.inflate(R.layout.fragment_heart_rate, container, false);

        getBodySensorLocationButton = (Button) rootView.findViewById(R.id.getBodySensorLocationButton);
        getBodySensorLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getBodySensorLocation(deviceAddress);
            }
        });

        resetEnergyExpendedButton = (Button) rootView.findViewById(R.id.resetEnergyExpendedButton);
        resetEnergyExpendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.resetEnergyExpended(deviceAddress);
            }
        });

        heartRateTextView = (TextView) rootView.findViewById(R.id.heartRateTextView);
        energyExpendedTextView = (TextView) rootView.findViewById(R.id.energyExpendedTextView);
        RRIntervalTextView = (TextView) rootView.findViewById(R.id.RRIntervalTextView);
        dateTimeTextView = (TextView) rootView.findViewById(R.id.dateTimeTextView);
        bodySensorLocationTextView = (TextView) rootView.findViewById(R.id.bodySensorLocationTextView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addHeartRateServiceListener(deviceAddress, this.hbHeartRateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeHeartRateServiceListener(deviceAddress);
    }
}