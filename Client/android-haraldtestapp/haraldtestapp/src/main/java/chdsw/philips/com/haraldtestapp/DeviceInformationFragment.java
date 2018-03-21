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
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationListener;
import chdsw.philips.com.haraldlib.services.deviceinformation.object.HBPnPID;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

public class DeviceInformationFragment extends Fragment {
    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;

    private Button getManufacturerNameButton;
    private TextView manufacturerNameTextView;

    private Button getModelNumberValueButton;
    private TextView modelNumberValueTextView;

    private Button getSerialNumberButton;
    private TextView serialNumberTextView;

    private Button getHardwareRevisionButton;
    private TextView hardwareRevisionTextView;

    private Button getFirmwareRevisionButton;
    private TextView firmwareRevisionTextView;

    private Button getSoftwareRevisionButton;
    private TextView softwareRevisionTextView;

    private Button getSystemIDButton;
    private TextView systemIDTextView;

    private Button getRegulatoryCertificationDataListButton;
    private TextView regulatoryCertificationDataListTextView;

    private Button getPnPIDButton;
    private TextView vendorIDSourceTextView;
    private TextView vendorIDTextView;
    private TextView productIDTextView;
    private TextView productVersionTextView;

    private HBDeviceInformationListener hbDeviceInformationListener = new HBDeviceInformationListener() {

        @Override
        public void onManufacturerName(String deviceAddress, String manufacturerName) {
            manufacturerNameTextView.setText(manufacturerName);
        }

        @Override
        public void onModelNumber(String deviceAddress, String modelNumber) {
            modelNumberValueTextView.setText(modelNumber);
        }

        @Override
        public void onSerialNumber(String deviceAddress, String serialNumber) {
            serialNumberTextView.setText(serialNumber);
        }

        @Override
        public void onHardwareRevision(String deviceAddress, String hardwareRevision) {
            hardwareRevisionTextView.setText(hardwareRevision);
        }

        @Override
        public void onFirmwareRevision(String deviceAddress, String firmwareRevision) {
            firmwareRevisionTextView.setText(firmwareRevision);
        }

        @Override
        public void onSoftwareRevision(String deviceAddress, String softwareRevision) {
            softwareRevisionTextView.setText(softwareRevision);
        }

        @Override
        public void onSystemID(String deviceAddress, byte[] systemID) {
            systemIDTextView.setText(getHex(systemID));
        }

        @Override
        public void onRegulatoryCertificationDataList(String deviceAddress, byte[] regulatoryCertificationDataList) {
            regulatoryCertificationDataListTextView.setText(getHex(regulatoryCertificationDataList));
        }

        @Override
        public void onPnPID(String deviceAddress, HBPnPID pnpID) {
            vendorIDSourceTextView.setText(String.valueOf(pnpID.getVendorIDSource()));
            vendorIDTextView.setText(String.valueOf(pnpID.getVendorID()));
            productIDTextView.setText(String.valueOf(pnpID.getProductID()));
            productVersionTextView.setText(String.valueOf(pnpID.getProductVersion()));
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
        View rootView = inflater.inflate(R.layout.fragment_device_information, container, false);

        getManufacturerNameButton = (Button) rootView.findViewById(R.id.getManufacturerNameButton);
        getManufacturerNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getManufacturerName(deviceAddress);
            }
        });
        manufacturerNameTextView = (TextView) rootView.findViewById(R.id.manufacturerNameTextView);

        getModelNumberValueButton = (Button) rootView.findViewById(R.id.getModelNumberValueButton);
        getModelNumberValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getModelNumber(deviceAddress);
            }
        });
        modelNumberValueTextView = (TextView) rootView.findViewById(R.id.modelNumberValueTextView);

        getSerialNumberButton = (Button) rootView.findViewById(R.id.getSerialNumberButton);
        getSerialNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getSerialNumber(deviceAddress);
            }
        });
        serialNumberTextView = (TextView) rootView.findViewById(R.id.serialNumberTextView);

        getHardwareRevisionButton = (Button) rootView.findViewById(R.id.getHardwareRevisionButton);
        getHardwareRevisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getHardwareRevision(deviceAddress);
            }
        });
        hardwareRevisionTextView = (TextView) rootView.findViewById(R.id.hardwareRevisionTextView);

        getFirmwareRevisionButton = (Button) rootView.findViewById(R.id.getFirmwareRevisionButton);
        getFirmwareRevisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getFirmwareRevision(deviceAddress);
            }
        });
        firmwareRevisionTextView = (TextView) rootView.findViewById(R.id.firmwareRevisionTextView);

        getSoftwareRevisionButton = (Button) rootView.findViewById(R.id.getSoftwareRevisionButton);
        getSoftwareRevisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getSoftwareRevision(deviceAddress);
            }
        });
        softwareRevisionTextView = (TextView) rootView.findViewById(R.id.softwareRevisionTextView);

        getSystemIDButton = (Button) rootView.findViewById(R.id.getSystemIDButton);
        getSystemIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getSystemID(deviceAddress);
            }
        });
        systemIDTextView = (TextView) rootView.findViewById(R.id.systemIDTextView);

        getRegulatoryCertificationDataListButton = (Button) rootView.findViewById(R.id.getRegulatoryCertificationDataListButton);
        getRegulatoryCertificationDataListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getRegulatoryCertificationDataList(deviceAddress);
            }
        });
        regulatoryCertificationDataListTextView = (TextView) rootView.findViewById(R.id.regulatoryCertificationDataListTextView);

        getPnPIDButton = (Button) rootView.findViewById(R.id.getPnPIDButton);
        getPnPIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbCentral.getPnPID(deviceAddress);
            }
        });
        vendorIDSourceTextView = (TextView) rootView.findViewById(R.id.vendorIDSourceTextView);
        vendorIDTextView = (TextView) rootView.findViewById(R.id.vendorIDTextView);
        productIDTextView = (TextView) rootView.findViewById(R.id.productIDTextView);
        productVersionTextView = (TextView) rootView.findViewById(R.id.productVersionTextView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addDeviceInformationServiceListener(this.deviceAddress, this.hbDeviceInformationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removeDeviceInformationServiceListener(this.deviceAddress);
    }

    public static String getHex(byte[] value) {
        if (value == null) {
            return null;
        }
        final String HEXES = "0123456789ABCDEF";
        final StringBuilder hex = new StringBuilder();
        for (final byte b : value) {
            if (hex.length() != 0)
                hex.append("-");
            else
                hex.append("(0x) ");
            hex.append(HEXES.charAt((b & 0xF0) >> 4));
            hex.append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
}
