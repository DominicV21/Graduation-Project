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
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.HBPhysicalActivityMonitorListener;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBControlPointOperation;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorControlPoint;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSessDescriptor;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;
import static java.security.AccessController.getContext;

public class PhysicalActivityMonitor extends Fragment{
    private Context context;
    private HBCentral hbCentral;
    private String deviceAddress;
    private Button getPamFeatureButton;
    private TextView pamFeatureTextView;

    //region GaidTextViews
    private TextView pamGaidFlagsTextView;
    private TextView pamGaidSessionIDTextView;
    private TextView pamGaidSubSessionIDTextView;
    private TextView pamGaidRelativeTimestampTextView;
    private TextView pamGaidSequenceNumberTextView;
    private TextView pamGaidNormalWalkingTextView;
    private TextView pamGaidIntensityWalkingTextView;
    private TextView pamGaidTotalEnergyTextView;
    private TextView pamGaidFatBurnedTextView;
    private TextView pamGaidMetabolicTextView;
    private TextView pamGaidSpeedTextView;
    private TextView pamGaidMotionTextView;
    private TextView pamGaidElevationTextView;
    private TextView pamGaidActivityCountTextView;
    private TextView pamGaidActivityLevelTextView;
    private TextView pamGaidActivityTypeTextView;
    //endregion
    //region GasdTextViews
    //endregion
    //region CraidTextViews
    //endregion
    //region CrasdTextViews
    //endregion
    //region ScasdTextViews
    //endregion
    //region SaidTextViews
    //endregion
    //region SasdTextViews
    //endregion
    //region Control Point TextViews
    private Button pamControlPointEnquireSessionsWriteButton;
    private Button pamControlPointEnquireSubSessionsWriteButton;
    private Button pamControlPointGetEndedSessionDataWriteButton;
    private Button pamControlPointStartSessionWriteButton;
    private Button pamControlPointStartSubSessionWriteButton;
    private Button pamControlPointStopSessionWriteButton;
    private Button pamControlPointDeleteSessionWriteButton;
    private EditText pamControlPointEnquireSubSessionID;
    private EditText pamControlPointGetEndedSessionID;
    private EditText pamControlPointGetEndedSubSessionID;
    private EditText pamControlPointGetEndedData;
    private EditText pamControlPointDeleteSessionID;
    //endregion
    //region CurrSessionTextViews
    //endregion
    //region Session Descriptor TextViews
    private TextView pamSDFlagsTextView;
    private TextView pamSDSessionIDTextView;
    private TextView pamSDSessionStartBaseTimeTextView;
    private TextView pamSDSessionStartTimeOffsetTextView;
    private TextView pamSDSessionEndBaseTimeTextView;
    private TextView pamSDSessionEndTimeOffsetTextView;
    private TextView pamSDSubSessionIDTextView;
    private TextView pamSDSubSessionStartBaseTimeTextView;
    private TextView pamSDSubSessionStartTimeOffsetTextView;
    private TextView pamSDSubSessionEndBaseTimeTextView;
    private TextView pamSDSubSessionEndTimeOffsetTextView;
    private TextView pamSDPredominantActivityTypeTextView;
    //endregion





    private HBPhysicalActivityMonitorListener hbPhysicalActivityMonitorListener = new HBPhysicalActivityMonitorListener() {

        @Override
        public void onPamFeature(String deviceAddress, HBPhysicalActivityMonitorFeature pamFeature) {
            pamFeatureTextView.setText(Long.toHexString(pamFeature.getFlags()));
        }

        @Override
        public void onPamGaid(String deviceAddress, HBPhysicalActivityMonitorGaid pamGaid) {
            byte[] myFlags = pamGaid.getFlags();
            pamGaidFlagsTextView.setText("Flags: " + (Integer.toBinaryString(myFlags[0] & 0xFF) + Integer.toBinaryString(myFlags[1] & 0xFF) + Integer.toBinaryString(myFlags[2] & 0xFF)).replace(' ', '0'));
            pamGaidSessionIDTextView.setText("Session ID: " + Integer.toString(pamGaid.getSessionID()));
            pamGaidSubSessionIDTextView.setText("Sub-Session ID: " + Integer.toString(pamGaid.getSubSessionID()));
            pamGaidRelativeTimestampTextView.setText("Relative Time Stamp: " + Integer.toString(pamGaid.getRelativeTimestamp()));
            pamGaidSequenceNumberTextView.setText("Sequence Number: " + Integer.toString(pamGaid.getSequenceNumber()));
            pamGaidNormalWalkingTextView.setText("Normal Walking Energy Expenditure Per Hour: " + Integer.toString(pamGaid.getNormalWalkingEnergyExpenditurePerHour()).replace(' ','0'));
            pamGaidIntensityWalkingTextView.setText("Intensity Walking Energy Expenditure Per Hour: " + Integer.toString(pamGaid.getIntensityEnergyExpenditurePerHour()));
            pamGaidTotalEnergyTextView.setText("Total Energy Expenditure Per Hour: " + Integer.toString(pamGaid.getTotalEnergyExpenditurePerHour()));
            pamGaidFatBurnedTextView.setText("Fat Burned Per Hour: " + Integer.toString(pamGaid.getFatBurnedPerHour()));
            pamGaidMetabolicTextView.setText("Metabolic Equivalent: " + Integer.toString(pamGaid.getMetabolicEquivalent()));
            pamGaidSpeedTextView.setText("Speed: " + Integer.toString(pamGaid.getSpeed()));
            pamGaidMotionTextView.setText("Motion Cadence: " + Integer.toString(pamGaid.getMotionCadence()));
            pamGaidElevationTextView.setText("Elevation: " + Integer.toString(pamGaid.getElevation()));
            pamGaidActivityCountTextView.setText("Activity Count Per Minute: " + Integer.toString(pamGaid.getActivityCountPerMinute()));
            pamGaidActivityLevelTextView.setText("Activity Level: " + Integer.toString(pamGaid.getActivityLevel()));
            pamGaidActivityTypeTextView.setText("Activity Type: " + Integer.toString(pamGaid.getActivityType()));
        }

        @Override
        public void onPamSessDescriptor(String deviceAddress, HBPhysicalActivityMonitorSessDescriptor pamSessDescriptor) {
            pamSDFlagsTextView.setText("Flags: " + (Integer.toBinaryString(pamSessDescriptor.getFlags())));
            pamSDSessionIDTextView.setText("Session ID " + Integer.toString(pamSessDescriptor.getSessionID()));
            //pamSDSessionStartBaseTimeTextView.setText();
            //pamSDSessionStartTimeOffsetTextView.setText();
            //pamSDSessionEndBaseTimeTextView.setText();
            //pamSDSessionEndTimeOffsetTextView.setText();
            pamSDSubSessionIDTextView.setText("Sub Session ID " + Integer.toString(pamSessDescriptor.getSubSessionID()));
            //pamSDSubSessionStartBaseTimeTextView.setText();
            //pamSDSubSessionStartTimeOffsetTextView.setText();
            //pamSDSubSessionEndBaseTimeTextView.setText();
            //pamSDSubSessionEndTimeOffsetTextView.setText();
            //pamSDPredominantActivityTypeTextView.setText();
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
        View rootView = inflater.inflate(R.layout.fragment_physycal_activity_monitor, container, false);

        getPamFeatureButton = (Button) rootView.findViewById(R.id.getPamFeatureButton);
        getPamFeatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                hbCentral.getPhysicalActivityMonitorFeature(deviceAddress);
            }
        });

        pamFeatureTextView = (TextView) rootView.findViewById(R.id.pamFeatureTextView);

        //region Gaid
        pamGaidFlagsTextView = (TextView) rootView.findViewById(R.id.pamGaidFlagsTextView);
        pamGaidSessionIDTextView = (TextView) rootView.findViewById(R.id.pamGaidSessionIDTextView);
        pamGaidSubSessionIDTextView = (TextView) rootView.findViewById(R.id.pamGaidSubSessionIDTextView);
        pamGaidRelativeTimestampTextView = (TextView) rootView.findViewById(R.id.pamGaidRelativeTimestampTextView);
        pamGaidSequenceNumberTextView = (TextView) rootView.findViewById(R.id.pamGaidSequenceNumberTextView);
        pamGaidNormalWalkingTextView = (TextView) rootView.findViewById(R.id.pamGaidNormalWalkingTextView);
        pamGaidIntensityWalkingTextView = (TextView) rootView.findViewById(R.id.pamGaidIntensityWalkingTextView);
        pamGaidTotalEnergyTextView = (TextView) rootView.findViewById(R.id.pamGaidTotalEnergyTextView);
        pamGaidFatBurnedTextView = (TextView) rootView.findViewById(R.id.pamGaidFatBurnedTextView);
        pamGaidMetabolicTextView = (TextView) rootView.findViewById(R.id.pamGaidMetabolicTextView);
        pamGaidSpeedTextView = (TextView) rootView.findViewById(R.id.pamGaidSpeedTextView);
        pamGaidMotionTextView = (TextView) rootView.findViewById(R.id.pamGaidMotionTextView);
        pamGaidElevationTextView = (TextView) rootView.findViewById(R.id.pamGaidElevationTextView);
        pamGaidActivityCountTextView = (TextView) rootView.findViewById(R.id.pamGaidActivityCountTextView);
        pamGaidActivityLevelTextView = (TextView) rootView.findViewById(R.id.pamGaidActivityLevelTextView);
        pamGaidActivityTypeTextView = (TextView) rootView.findViewById(R.id.pamGaidActivityTypeTextView);
        //endregion

        //region Control Point
        pamControlPointEnquireSessionsWriteButton = (Button) rootView.findViewById((R.id.pamCPEnquireSessions));
        pamControlPointEnquireSessionsWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.EnquireSessions));
            }
        });
        pamControlPointEnquireSubSessionsWriteButton = (Button) rootView.findViewById((R.id.pamCPEnquireSubSessions));
        pamControlPointEnquireSubSessionsWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pamControlPointEnquireSubSessionID.getText().toString().equals("")) {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.EnquireSubSessions, 0));
                }
                else
                {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.EnquireSubSessions, Integer.parseInt(pamControlPointEnquireSubSessionID.getText().toString())));
                }
            }
        });
        pamControlPointGetEndedSessionDataWriteButton = (Button) rootView.findViewById((R.id.pamCPGetEndedSessionData));
        pamControlPointGetEndedSessionDataWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pamControlPointGetEndedSessionID.getText().toString().equals("")
                || pamControlPointGetEndedSubSessionID.getText().toString().equals("")
                || pamControlPointGetEndedData.getText().toString().equals("")) {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.GetEndedSessionData, 0,0,0));
                }
                else
                {
                    int mySessionID = Integer.parseInt(pamControlPointGetEndedSessionID.getText().toString());
                    int mySubSessionID = Integer.parseInt(pamControlPointGetEndedSubSessionID.getText().toString());
                    int swappedSessionID = 0;
                    int swappedSubSessionID = 0;

                    swappedSessionID = ((byte)(mySessionID & 0xFF) << 8) + (byte)((mySessionID >> 8) & 0xFF);
                    swappedSubSessionID = ((byte)(mySubSessionID & 0xFF) << 8) + (byte)((mySessionID >> 8) & 0xFF);

                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.GetEndedSessionData,
                            swappedSessionID,
                            swappedSubSessionID,
                            Integer.parseInt(pamControlPointGetEndedData.getText().toString())));
                }
            }
        });
        pamControlPointStartSessionWriteButton = (Button) rootView.findViewById((R.id.pamCPStartSession));
        pamControlPointStartSessionWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.StartSessionSubSession, 0));
            }
        });
        pamControlPointStartSubSessionWriteButton = (Button) rootView.findViewById((R.id.pamCPStartSubSession));
        pamControlPointStartSubSessionWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.StartSessionSubSession, 1));
            }
        });
        pamControlPointStopSessionWriteButton = (Button) rootView.findViewById((R.id.pamCPStopSession));
        pamControlPointStopSessionWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.StopSession));
            }
        });
        pamControlPointDeleteSessionWriteButton = (Button) rootView.findViewById((R.id.pamCPDeleteSession));
        pamControlPointDeleteSessionWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pamControlPointDeleteSessionID.getText().toString().equals("")) {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.DeleteSession, 0));
                }
                else
                {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.DeleteSession, Integer.parseInt(pamControlPointDeleteSessionID.getText().toString())));
                }
            }
        });

        pamControlPointEnquireSubSessionID = (EditText) rootView.findViewById(R.id.pamCPGetSubSessSubSessionID);
        pamControlPointGetEndedSessionID = (EditText) rootView.findViewById(R.id.pamCPGetEndedSessionID);
        pamControlPointGetEndedSubSessionID = (EditText) rootView.findViewById(R.id.pamCPGetEndedSubSessionID);
        pamControlPointGetEndedData = (EditText) rootView.findViewById(R.id.pamCPGetEndedData);
        pamControlPointDeleteSessionID = (EditText) rootView.findViewById(R.id.pamCPDeleteSessionID);
        //endregion

        //region SessDescriptor
        pamSDFlagsTextView                      = (TextView)rootView.findViewById(R.id.pamSDFlagsTextView                    );
        pamSDSessionIDTextView                  = (TextView)rootView.findViewById(R.id.pamSDSessionIDTextView                );
        pamSDSessionStartBaseTimeTextView       = (TextView)rootView.findViewById(R.id.pamSDSessionStartBaseTimeTextView     );
        pamSDSessionStartTimeOffsetTextView     = (TextView)rootView.findViewById(R.id.pamSDSessionStartTimeOffsetTextView   );
        pamSDSessionEndBaseTimeTextView         = (TextView)rootView.findViewById(R.id.pamSDSessionEndBaseTimeTextView       );
        pamSDSessionEndTimeOffsetTextView       = (TextView)rootView.findViewById(R.id.pamSDSessionEndTimeOffsetTextView     );
        pamSDSubSessionIDTextView               = (TextView)rootView.findViewById(R.id.pamSDSubSessionIDTextView             );
        pamSDSubSessionStartBaseTimeTextView    = (TextView)rootView.findViewById(R.id.pamSDSubSessionStartBaseTimeTextView  );
        pamSDSubSessionStartTimeOffsetTextView  = (TextView)rootView.findViewById(R.id.pamSDSubSessionStartTimeOffsetTextView);
        pamSDSubSessionEndBaseTimeTextView      = (TextView)rootView.findViewById(R.id.pamSDSubSessionEndBaseTimeTextView    );
        pamSDSubSessionEndTimeOffsetTextView    = (TextView)rootView.findViewById(R.id.pamSDSubSessionEndTimeOffsetTextView  );
        pamSDPredominantActivityTypeTextView    = (TextView)rootView.findViewById(R.id.pamSDPredominantActivityTypeTextView  );
        //endregion



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.hbCentral.addPhysicalActivityMonitorServiceListener(deviceAddress, this.hbPhysicalActivityMonitorListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.hbCentral.removePhysicalActivityMonitorServiceListener(deviceAddress);
    }
}
