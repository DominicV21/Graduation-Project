package chdsw.philips.com.haraldtestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleListener;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightMeasurement;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightScaleFeature;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class WeightScaleFragment extends Fragment {
    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;

    private HBWeightScaleListener hbWeightScaleListener = new HBWeightScaleListener() {

        @Override
        public void onWeightScaleFeature(String deviceAddress, HBWeightScaleFeature weightScaleFeature) {
            Log.e("HARALD", "onWeightScaleFeature ");
        }

        @Override
        public void onWeightMeasurement(String deviceAddress, HBWeightMeasurement weightMeasurement) {
            Log.e("HARALD", "onWeightMeasurement ");
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
        View rootView = inflater.inflate(R.layout.fragment_weight_scale, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addWeightScaleServiceListener(deviceAddress, this.hbWeightScaleListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeWeightScaleServiceListener(deviceAddress);
    }
}