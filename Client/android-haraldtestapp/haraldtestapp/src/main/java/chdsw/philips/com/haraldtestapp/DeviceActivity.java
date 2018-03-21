/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldtestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.HBDeviceConnectionListener;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryServiceHandler;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeServiceHandler;
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationServiceHandler;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerServiceHandler;
import chdsw.philips.com.haraldlib.services.heartrate.HBHeartRateServiceHandler;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUServiceHandler;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.HBPhysicalActivityMonitorServiceHandler;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleServiceHandler;

public class DeviceActivity extends AppCompatActivity {
    public static final String DEVICE_ADDRESS = "device_address";

    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;
    private Button disconnectDeviceButton;
    private TextView deviceAddressTextView;
    private ServicesPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private HBDeviceConnectionListener hbDeviceConnectionListener = new HBDeviceConnectionListener() {

        @Override
        public void disconnected(String deviceAddress) {
            if (DeviceActivity.this.deviceAddress.equals(deviceAddress)) {
                Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        public void onReadWriteError(String deviceAddress) {
            Toast.makeText(context, "onReadWriteError", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = getApplicationContext();
        this.hbCentral = HBCentralInstance.getInstance(this.context).getHBCentral();
        this.deviceAddress = getIntent().getStringExtra(DEVICE_ADDRESS);

        this.disconnectDeviceButton = (Button) findViewById(R.id.disconnectDeviceButton);
        this.disconnectDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.disconnect(DeviceActivity.this.deviceAddress);
                DeviceActivity.this.finish();
            }
        });

        this.deviceAddressTextView = (TextView) findViewById(R.id.deviceAddressTextView);
        this.deviceAddressTextView.setText(DeviceActivity.this.deviceAddress);

        mViewPager = (ViewPager) findViewById(R.id.device_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mSectionsPagerAdapter = new ServicesPagerAdapter(getSupportFragmentManager(), tabLayout);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        List<UUID> services = this.hbCentral.getDeviceSupportedServices(this.deviceAddress);

        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ADDRESS, deviceAddress);

        for (UUID service : services) {
            Fragment fragment = null;
            String name = "";

            if (service.equals(HBHealthThermometerServiceHandler.SERVICE_UUID)) {
                fragment = new HealthThermometerFragment();
                name = getResources().getString(R.string.health_thermometer_service);
            }
            if (service.equals(HBCurrentTimeServiceHandler.SERVICE_UUID)) {
                fragment = new CurrentTimeFragment();
                name = getResources().getString(R.string.current_time_service);
            }
            if (service.equals(HBNordicDFUServiceHandler.SERVICE_UUID)) {
                fragment = new NordicOTAFragment();
                name = getResources().getString(R.string.nordic_ota_service);
            }
            if (service.equals(HBBatteryServiceHandler.SERVICE_UUID)) {
                fragment = new BatteryLevelFragment();
                name = getResources().getString(R.string.battery_level_service);
            }
            if (service.equals(HBDeviceInformationServiceHandler.SERVICE_UUID)) {
                fragment = new DeviceInformationFragment();
                name = getResources().getString(R.string.device_information_service);
            }
            if (service.equals(HBWeightScaleServiceHandler.SERVICE_UUID)) {
                fragment = new WeightScaleFragment();
                name = getResources().getString(R.string.weight_scale_service);
            }
            if (service.equals(HBHeartRateServiceHandler.SERVICE_UUID)) {
                fragment = new HeartRateFragment();
                name = getResources().getString(R.string.heart_rate_service);
            }
            if (service.equals(HBPhysicalActivityMonitorServiceHandler.SERVICE_UUID)) {
                fragment = new PhysicalActivityMonitor();
                name = getResources().getString(R.string.physical_activity_monitor_service);
            }

            if (fragment != null) {
                fragment.setArguments(bundle);
                mSectionsPagerAdapter.addFragment(fragment, name);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addDeviceConnectionListener(this.deviceAddress, this.hbDeviceConnectionListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeDeviceConnectionListener(this.deviceAddress);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hbCentral.disconnect(DeviceActivity.this.deviceAddress);
    }
}
