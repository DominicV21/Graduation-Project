/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldtestapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.HBConnectionListener;
import chdsw.philips.com.haraldlib.HBScanRecord;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class ConnectActivity extends AppCompatActivity {
    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    private Context context;
    private HBCentral hbCentral;
    private Button connectServiceButton;
    private ListView servicesListView;
    private ListView availableDevicesListView;
    private BluetoothAdapter BTAdapter;
    private Set<BluetoothDevice> pairedDevices;

    private HBConnectionListener hbConnectionListener = new HBConnectionListener() {

        @Override
        public void connected(String deviceAddress, UUID primaryService) {
            Intent intent = new Intent(context, DeviceActivity.class);
            intent.putExtra(DEVICE_ADDRESS, deviceAddress);
            startActivity(intent);
            finish();
        }

        @Override
        public void connectFailed(String deviceAddress, UUID primaryService) {
            Toast.makeText(context, "Connect failed", Toast.LENGTH_LONG).show();
            connectServiceButton.setEnabled(true);
        }

        @Override
        public boolean discoveredDeviceIsCorrect(String deviceAddress, UUID primaryService, HBScanRecord scanRecord) {
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        //Phone does not support Bluetooth so let the user know and exit.
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 1);
        }

        availableDevicesListView = (ListView) findViewById(R.id.availableDevicesListView);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = getApplicationContext();
        this.hbCentral = HBCentralInstance.getInstance(this.context).getHBCentral();

        List<UUID> supportedServices = this.hbCentral.getSupportedServices();
        List<ServiceDefinition> serviceDefinitions = new ArrayList<>();
        for (UUID supportedService : supportedServices) {
            ServiceDefinition definition = ServiceDefinition.fromValue(supportedService);
            if (definition != null) {
                serviceDefinitions.add(definition);
            }
        }

        CustomAdapter<ServiceDefinition> adapter = new CustomAdapter<>(serviceDefinitions, android.R.layout.simple_list_item_1, new CustomAdapter.Callbacks<ServiceDefinition>() {
            private int[] viewIds = new int[]{android.R.id.text1};

            @Override
            public int[] getViewIds() {
                return viewIds;
            }

            @Override
            public void setupView(ServiceDefinition item, Map<Integer, View> viewDescriptors) {
                if (item != null) {
                    ((TextView) viewDescriptors.get(android.R.id.text1)).setText(item.getResourceId());
                }
            }
        }, this);

        servicesListView = (ListView) findViewById(R.id.servicesListView);
        servicesListView.setAdapter(adapter);
        servicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectServiceButton.setEnabled(true);
            }
        });

        connectServiceButton = (Button) findViewById(R.id.connectServiceButton);
        connectServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectServiceButton.setEnabled(false);
                ServiceDefinition selectedService = (ServiceDefinition) servicesListView.getAdapter().getItem(servicesListView.getCheckedItemPosition());

                connect(selectedService.getUUID());
            }
        });
        connectServiceButton.setEnabled(false);
    }

    public void list(View v)
    {
        pairedDevices = BTAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        availableDevicesListView.setAdapter(adapter);
    }

    ///////////
    public void on(View v){
        if (!BTAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v){
        BTAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }

    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }
    /////////////////////////

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_FINE_LOCATION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ServiceDefinition selectedService = (ServiceDefinition)servicesListView.getAdapter().getItem(servicesListView.getCheckedItemPosition());
                connect(selectedService.getUUID());
            }
        }
    }

    private void connect(UUID serviceUUID) {
        if(hasPermissions()) {
            hbCentral.connect(serviceUUID, hbConnectionListener);
        }
    }

    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, ACCESS_FINE_LOCATION_REQUEST);
                return false;
            }
        }

        return true;
    }
}
