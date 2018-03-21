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

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.HBConnectionListener;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryListener;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class BatteryLevelFragment extends Fragment {
    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;

    private Button getBatteryLevelButton;
    private TextView batteryLevelTextView;

    private HBBatteryListener hbBatteryListener = new HBBatteryListener() {
        @Override
        public void onBatteryLevel(String deviceAddress, int batteryLevel) {
            batteryLevelTextView.setText(batteryLevel + "%");
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
        View rootView = inflater.inflate(R.layout.fragment_battery_level, container, false);

        getBatteryLevelButton = (Button) rootView.findViewById(R.id.getBatteryLevelButton);
        getBatteryLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getBatteryLevel(deviceAddress);
            }
        });

        batteryLevelTextView = (TextView) rootView.findViewById(R.id.batteryLevelTextView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addBatteryServiceListener(this.deviceAddress, this.hbBatteryListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeBatteryServiceListener(this.deviceAddress);
    }
}
