package chdsw.philips.com.haraldtestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerListener;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureMeasurement;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureType;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class HealthThermometerFragment extends Fragment {
    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;
    private TextView temperatureTextView;
    private TextView temperatureUnitTextView;
    private TextView temperatureTimestampTextView;
    private Button getTemperatureTypeButton;
    private TextView temperatureTypeTextView;
    private Button getMeasurementIntervalButton;
    private TextView measurementIntervalTextView;
    private Button setMeasurementIntervalButton;
    private EditText measurementIntervalEditText;

    private HBHealthThermometerListener hbHealthThermometerListener = new HBHealthThermometerListener() {
        @Override
        public void onTemperatureMeasurement(String deviceAddress, HBTemperatureMeasurement measurementTemperature) {
            setTemperature(measurementTemperature);
        }

        @Override
        public void onTemperatureType(String deviceAddress, HBTemperatureType type) {
            temperatureTypeTextView.setText(String.valueOf(type));
        }

        @Override
        public void onIntermediateTemperature(String deviceAddress, HBTemperatureMeasurement intermediateTemperature) {
            setTemperature(intermediateTemperature);
        }

        @Override
        public void onMeasurementInterval(String deviceAddress, int interval) {
            measurementIntervalTextView.setText(String.valueOf(interval));
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
        View rootView = inflater.inflate(R.layout.fragment_health_thermometer, container, false);

        getMeasurementIntervalButton = (Button) rootView.findViewById(R.id.getMeasurementIntervalButton);
        getMeasurementIntervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getMeasurementInterval(deviceAddress);
            }
        });

        setMeasurementIntervalButton = (Button) rootView.findViewById(R.id.setMeasurementIntervalButton);
        setMeasurementIntervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence value = measurementIntervalEditText.getText();
                if(value != null && !value.toString().isEmpty()) {
                    hbCentral.setMeasurementInterval(deviceAddress, Integer.parseInt(value.toString()));
                }
            }
        });

        getTemperatureTypeButton = (Button) rootView.findViewById(R.id.getTemperatureTypeButton);
        getTemperatureTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getTemperatureType(deviceAddress);
            }
        });

        temperatureTextView = (TextView) rootView.findViewById(R.id.temperatureTextView);
        temperatureUnitTextView = (TextView) rootView.findViewById(R.id.temperatureUnitTextView);
        temperatureTimestampTextView = (TextView) rootView.findViewById(R.id.temperatureTimestampTextView);
        temperatureTypeTextView = (TextView) rootView.findViewById(R.id.temperatureTypeTextView);
        measurementIntervalTextView = (TextView) rootView.findViewById(R.id.measurementIntervalTextView);
        measurementIntervalEditText = (EditText) rootView.findViewById(R.id.measurementIntervalEditText);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addHealthThermometerServiceListener(deviceAddress, this.hbHealthThermometerListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeHealthThermometerServiceListener(deviceAddress);
    }

    private void setTemperature(HBTemperatureMeasurement temperature) {
        temperatureTextView.setText(String.valueOf(temperature.getTemperatureValue()));
        temperatureUnitTextView.setText(String.valueOf(temperature.getUnit().toString()));
        if(temperature.getTimestamp() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            temperatureTimestampTextView.setText(dateFormat.format(temperature.getTimestamp()));
        }
    }
}