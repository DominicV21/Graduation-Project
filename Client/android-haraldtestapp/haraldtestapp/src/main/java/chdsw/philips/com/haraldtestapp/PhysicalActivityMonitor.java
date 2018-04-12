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


import java.math.BigInteger;

import chdsw.philips.com.haraldlib.HBCentral;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.HBPhysicalActivityMonitorListener;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBControlPointOperation;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorControlPoint;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCraid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCrasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorScasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCurrSess;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSessDescriptor;

import static chdsw.philips.com.haraldtestapp.DeviceActivity.DEVICE_ADDRESS;

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
    private TextView pamGasdFlagsTextView;
    private TextView pamGasdSessionIDTextView;
    private TextView pamGasdSubSessionIDTextView;
    private TextView pamGasdRelativeTimestampTextView;
    private TextView pamGasdSequenceNumberTextView;
    private TextView pamGasdNormalWalkingEnergyExpenditureTextView;
    private TextView pamGasdIntensityEnergyExpenditureTextView;
    private TextView pamGasdTotalEnergyExpenditureTextView;
    private TextView pamGasdFatBurnedTextView;
    private TextView pamGasdMetabolicEquivalentTextView;
    private TextView pamGasdDistanceTextView;
    private TextView pamGasdSpeedTextView;
    private TextView pamGasdDurationNormalEpisodesTextView;
    private TextView pamGasdDurationIntensityEpisodesTextView;
    private TextView pamGasdMotionCadenceTextView;
    private TextView pamGasdFloorsTextView;
    private TextView pamGasdPosElevationTextView;
    private TextView pamGasdNegElevationTextView;
    private TextView pamGasdActivityCountTextView;
    private TextView pamGasdActivityLevelTextView;
    private TextView pamGasdActivityTypeTextView;
    private TextView pamGasdWornDurationTextView;
    //endregion
    //region CraidTextViews
    private TextView pamCraidFlagsTextView;
    private TextView pamCraidSessionIDTextView;
    private TextView pamCraidSubSessionIDTextView;
    private TextView pamCraidRelativeTimestampTextView;
    private TextView pamCraidSequenceNumberTextView;
    private TextView pamCraidVO2MAXTextView;
    private TextView pamCraidHeartRateTextView;
    private TextView pamCraidPulseInterBeatIntervalTextView;
    private TextView pamCraidRestingHeartRateTextView;
    private TextView pamCraidHeartRateVariabilityTextView;
    private TextView pamCraidRespirationRateTextView;
    private TextView pamCraidRestingRespirationRateTextView;
    //endregion
    //region CrasdTextViews
    private TextView pamCrasdFlagsTextView;
    private TextView pamCrasdSessionIDTextView;
    private TextView pamCrasdSubSessionIDTextView;
    private TextView pamCrasdRelativeTimestampTextView;
    private TextView pamCrasdSequenceNumberTextView;
    private TextView pamCrasdTimeInHeartRateZoneTextView;
    private TextView pamCrasdVO2MaxTextView;
    private TextView pamCrasdHeartRateTextView;
    private TextView pamCrasdPulseInterbeatIntervalTextView;
    private TextView pamCrasdRestingHeartRateTextView;
    private TextView pamCrasdHeartRateVariabilityTextView;
    private TextView pamCrasdRespirationRateTextView;
    private TextView pamCrasdRestingRespirationRateTextView;
    private TextView pamCrasdWornDurationTextView;
    //endregion
    //region ScasdTextViews
    private TextView pamScasdFlagsTextView;
    private TextView pamScasdSessionIDTextView;
    private TextView pamScasdSubSessionIDTextView;
    private TextView pamScasdRelativeTimestampTextView;
    private TextView pamScasdSequenceNumberTextView;
    private TextView pamScasdNormalWalkingStepsTextView;
    private TextView pamScasdIntensityStepsTextView;
    private TextView pamScasdFloorStepsTextView;
    private TextView pamScasdDistanceTextView;
    private TextView pamScasdWornDurationTextView;
    //endregion
    //region SaidTextViews
    private TextView pamSaidFlagsTextView;
    private TextView pamSaidSessionIDTextView;
    private TextView pamSaidSubSessionIDTextView;
    private TextView pamSaidRelativeTimestampTextView;
    private TextView pamSaidSequenceNumberTextView;
    private TextView pamSaidVisibleLightLevelTextView;
    private TextView pamSaidUVLightLevelTextView;
    private TextView pamSaidIRLightLevelTextView;
    private TextView pamSaidSleepStageTextView;
    private TextView pamSaidSleepingHeartRateTextView;
    //endregion
    //region SasdTextViews
    private TextView pamSasdFlagsTextView;
    private TextView pamSasdSessionIDTextView;
    private TextView pamSasdSubSessionIDTextView;
    private TextView pamSasdRelativeTimestampTextView;
    private TextView pamSasdSequenceNumberTextView;
    private TextView pamSasdTotalSleepTime;
    private TextView pamSasdTotalWakeTime;
    private TextView pamSasdTotalBedTime;
    private TextView pamSasdNumberOfAwakenings;
    private TextView pamSasdSleepLatency;
    private TextView pamSasdSleepEfficiency;
    private TextView pamSasdSnoozeTime;
    private TextView pamSasdNumberOfTossNTurnEvents;
    private TextView pamSasdTimeOfAwakeningAfterAlarm;
    private TextView pamSasdVisibleLightLevelTextView;
    private TextView pamSasdUVLightLevelTextView;
    private TextView pamSasdIRLightLevelTextView;
    private TextView pamSasdAverageSleepingHeartRate;
    private TextView pamSasdWornDuration;
    //endregion
    //region Control Point
    private Button pamControlPointEnquireSessionsWriteButton;
    private Button pamControlPointEnquireSubSessionsWriteButton;
    private Button pamControlPointGetEndedSessionDataWriteButton;
    private Button pamControlPointStartSessionWriteButton;
    private Button pamControlPointStartSubSessionWriteButton;
    private Button pamControlPointStopSessionWriteButton;
    private Button pamControlPointDeleteSessionWriteButton;
    private Button pamControlPointSetAverageActivityTypeCurrentWriteButton;
    private Button pamControlPointSetAverageActivityTypeAllWriteButton;
    private EditText pamControlPointEnquireSubSessionID;
    private EditText pamControlPointGetEndedSessionID;
    private EditText pamControlPointGetEndedSubSessionID;
    private EditText pamControlPointGetEndedData;
    private EditText pamControlPointDeleteSessionID;
    private EditText pamControlPointSetActivityTypeValue;
    private TextView pamCPResponse;
    private TextView pamCPValue;
    //endregion
    //region CurrSessionTextViews
    private TextView pamCSFlags;
    private TextView pamCSSessionID;
    private TextView pamCSSessionStartBaseTime;
    private TextView pamCSSessionStartTimeOffset;
    private TextView pamCSSubSessionID;
    private TextView pamCSSubSessionStartBaseTime;
    private TextView pamCSSubSessionStartTimeOffset;
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
    //endregion





    private HBPhysicalActivityMonitorListener hbPhysicalActivityMonitorListener = new HBPhysicalActivityMonitorListener() {

        @Override
        public void onPamFeature(String deviceAddress, HBPhysicalActivityMonitorFeature pamFeature) {
            pamFeatureTextView.setText(Long.toHexString(pamFeature.getFlags()));
        }

        @Override
        public void onPamGaid(String deviceAddress, HBPhysicalActivityMonitorGaid pamGaid)
        {

            pamGaidFlagsTextView.setText(String.format("Flags: %024d", new BigInteger(Long.toBinaryString(pamGaid.getFlags()))));
            pamGaidSessionIDTextView.setText("Session ID: " + Integer.toHexString(pamGaid.getSessionID()));
            pamGaidSubSessionIDTextView.setText("Sub-Session ID: " + Integer.toHexString(pamGaid.getSubSessionID()));
            pamGaidRelativeTimestampTextView.setText("Relative Time Stamp: " + Long.toHexString(pamGaid.getRelativeTimestamp()));
            pamGaidSequenceNumberTextView.setText("Sequence Number: " + Long.toHexString(pamGaid.getSequenceNumber()));
            pamGaidNormalWalkingTextView.setText("Normal Walking Energy Expenditure Per Hour: " + Integer.toHexString(pamGaid.getNormalWalkingEnergyExpenditurePerHour()));
            pamGaidIntensityWalkingTextView.setText("Intensity Walking Energy Expenditure Per Hour: " + Integer.toHexString(pamGaid.getIntensityEnergyExpenditurePerHour()));
            pamGaidTotalEnergyTextView.setText("Total Energy Expenditure Per Hour: " + Integer.toHexString(pamGaid.getTotalEnergyExpenditurePerHour()));
            pamGaidFatBurnedTextView.setText("Fat Burned Per Hour: " + Integer.toHexString(pamGaid.getFatBurnedPerHour()));
            pamGaidMetabolicTextView.setText("Metabolic Equivalent: " + Integer.toHexString(pamGaid.getMetabolicEquivalent()));
            pamGaidSpeedTextView.setText("Speed: " + Integer.toHexString(pamGaid.getSpeed()));
            pamGaidMotionTextView.setText("Motion Cadence: " + Integer.toHexString(pamGaid.getMotionCadence()));
            pamGaidElevationTextView.setText("Elevation: " + Integer.toHexString(pamGaid.getElevation()));
            pamGaidActivityCountTextView.setText("Activity Count Per Minute: " + Integer.toHexString(pamGaid.getActivityCountPerMinute()));
            pamGaidActivityLevelTextView.setText("Activity Level: " + Integer.toHexString(pamGaid.getActivityLevel()));
            pamGaidActivityTypeTextView.setText("Activity Type: " + Integer.toHexString(pamGaid.getActivityType()));
        }

        @Override
        public void onPamGasd(String deviceAddress, HBPhysicalActivityMonitorGasd pamGasd)
        {
            pamGasdFlagsTextView.setText(String.format("Flags: %032d", new BigInteger(Long.toBinaryString(pamGasd.getFlags()))));
            pamGasdSessionIDTextView.setText("SessionID " + Integer.toHexString(pamGasd.getSessionID()));
            pamGasdSubSessionIDTextView.setText("SubSession ID " + Integer.toHexString(pamGasd.getSubSessionID()));
            pamGasdRelativeTimestampTextView.setText("Relative Timestamp " + Long.toHexString((pamGasd.getRelativeTimestamp())));
            pamGasdSequenceNumberTextView.setText("Sequence Number " + Long.toHexString(pamGasd.getSequenceNumber()));
            pamGasdNormalWalkingEnergyExpenditureTextView.setText("Normal Walking Energy Expenditure " + Long.toHexString(pamGasd.getNormalWalkingEnergyExpenditure()));
            pamGasdIntensityEnergyExpenditureTextView.setText("Intensity Energy Expenditure " + Long.toHexString(pamGasd.getIntensityEnergyExpenditure()));
            pamGasdTotalEnergyExpenditureTextView.setText("Total Energy Expenditure " + Long.toHexString(pamGasd.getTotalEnergyExpenditure()));
            pamGasdFatBurnedTextView.setText("Fat Burned " + Integer.toHexString(pamGasd.getFatBurned()));
            pamGasdMetabolicEquivalentTextView.setText("Metabolic exquivalent " + Integer.toHexString(pamGasd.getMinimumMetabolicEquivalent()) + " / "
                                                                                + Integer.toHexString(pamGasd.getMaximumMetabolicEquivalent()) + " / "
                                                                                + Integer.toHexString(pamGasd.getAverageMetabolicEquivalent()));
            pamGasdDistanceTextView.setText("Distance " + Long.toHexString(pamGasd.getDistance()));
            pamGasdSpeedTextView.setText("Speed "   + Integer.toHexString(pamGasd.getMinimumSpeed()) + " / "
                                                    + Integer.toHexString(pamGasd.getMaximumSpeed()) + " / "
                                                    + Integer.toHexString(pamGasd.getAverageSpeed()));
            pamGasdDurationNormalEpisodesTextView.setText("Duration of normal walking episodes " + Long.toHexString(pamGasd.getDurationOfNormalWalkingEpisodes()));
            pamGasdDurationIntensityEpisodesTextView.setText("Duration of intensity walking episodes " + Long.toHexString(pamGasd.getDurationOfIntensityWalkingEpisodes()));
            pamGasdMotionCadenceTextView.setText("Motion Cadence "  + Integer.toHexString(pamGasd.getMinimumMotionCadence()) + " / "
                                                                    + Integer.toHexString(pamGasd.getMaximumMotionCadence()) + " / "
                                                                    + Integer.toHexString(pamGasd.getAverageMotionCadence()));
            pamGasdFloorsTextView.setText("Floors " + Integer.toHexString(pamGasd.getFloors()));
            pamGasdPosElevationTextView.setText("Positive Elevatin " + Long.toHexString(pamGasd.getPositiveElevationGain()));
            pamGasdNegElevationTextView.setText("Negative Elevation " + Long.toHexString(pamGasd.getNegativeElevationGain()));
            pamGasdActivityCountTextView.setText("Activity Count " + Long.toHexString(pamGasd.getActivityCount()));
            pamGasdActivityLevelTextView.setText("Activity Level "  + Integer.toHexString(pamGasd.getMinimumActivityLevel()) + " / "
                                                                    + Integer.toHexString(pamGasd.getMaximumActivityLevel()) + " / "
                                                                    + Integer.toHexString(pamGasd.getAverageActivityLevel()));
            pamGasdActivityTypeTextView.setText("Activity Type " + Integer.toHexString(pamGasd.getAverageActivityType()));
            pamGasdWornDurationTextView.setText("Worn Duration " + Long.toHexString(pamGasd.getWornDuration()));

        }

        @Override
        public void onPamCraid(String deviceAddress, HBPhysicalActivityMonitorCraid pamCraid)
        {
            pamCraidFlagsTextView                 .setText(String.format("Flags: %016d", new BigInteger(Long.toBinaryString(pamCraid.getFlags()))));
            pamCraidSessionIDTextView             .setText("SessionID " + (Integer.toHexString(pamCraid.getSessionID())));
            pamCraidSubSessionIDTextView          .setText("SubSessionID " + (Integer.toHexString(pamCraid.getSubSessionID())));
            pamCraidRelativeTimestampTextView     .setText("Relative Timestamp " + (Long.toHexString(pamCraid.getRelativeTimestamp())));
            pamCraidSequenceNumberTextView        .setText("Sequence Number " + (Long.toHexString(pamCraid.getSequenceNumber())));
            pamCraidVO2MAXTextView                .setText("VO2MAX " + (Integer.toHexString(pamCraid.getVO2Max())));
            pamCraidHeartRateTextView             .setText("Heart Rate " + (Integer.toHexString(pamCraid.getHeartRate())));
            pamCraidPulseInterBeatIntervalTextView.setText("PulseInterBeatInterval " + (Integer.toHexString(pamCraid.getPulseInterBeatInterval())));
            pamCraidRestingHeartRateTextView      .setText("RestingHeartRate " + (Integer.toHexString(pamCraid.getRestingHeartRate())));
            pamCraidHeartRateVariabilityTextView  .setText("HeartRateVariability " + (Integer.toHexString(pamCraid.getHeartRateVariability())));
            pamCraidRespirationRateTextView       .setText("RespirationRate " + (Integer.toHexString(pamCraid.getRespirationRate())));
            pamCraidRestingRespirationRateTextView.setText("RestingRespirationRate " + (Integer.toHexString(pamCraid.getRestingRespirationRate())));
        }

        @Override
        public void onPamCrasd(String deviceAddress, HBPhysicalActivityMonitorCrasd pamCrasd)
        {
            pamCrasdFlagsTextView.setText(String.format("Flags: %032d", new BigInteger(Long.toBinaryString(pamCrasd.getFlags()))));
            pamCrasdSessionIDTextView.setText("SessionID " + Integer.toHexString(pamCrasd.getSessionID()));
            pamCrasdSubSessionIDTextView.setText("SubSession ID " + Integer.toHexString(pamCrasd.getSubSessionID()));
            pamCrasdRelativeTimestampTextView.setText("Relative Timestamp " + Long.toHexString((pamCrasd.getRelativeTimestamp())));
            pamCrasdSequenceNumberTextView.setText("Sequence Number " + Long.toHexString(pamCrasd.getSequenceNumber()));
            pamCrasdTimeInHeartRateZoneTextView.setText("Time in heartrate zone "
                            + Long.toHexString(pamCrasd.getTimeInHeartRateZone1()) + " / "
                            + Long.toHexString(pamCrasd.getTimeInHeartRateZone2()) + " / "
                            + Long.toHexString(pamCrasd.getTimeInHeartRateZone3()) + " / "
                            + Long.toHexString(pamCrasd.getTimeInHeartRateZone4()) + " / "
                            + Long.toHexString(pamCrasd.getTimeInHeartRateZone5()));
            pamCrasdVO2MaxTextView.setText("VO2MAX "
                    + Integer.toHexString(pamCrasd.getMinimumVO2Max()) + " / "
                + Integer.toHexString(pamCrasd.getMaximumVO2Max()) + " / "
                + Integer.toHexString(pamCrasd.getAverageVO2Max()));
            pamCrasdHeartRateTextView.setText("HeartRate "
                    +Integer.toHexString(pamCrasd.getMinimumHeartRate()) + " / "
                +Integer.toHexString(pamCrasd.getMaximumHeartRate()) + " / "
                +Integer.toHexString(pamCrasd.getAverageHeartRate()));
            pamCrasdPulseInterbeatIntervalTextView.setText("PulseInterbeatInterval "
                    +Integer.toHexString(pamCrasd.getMinimumPulseInterbeatInterval()) + " / "
                +Integer.toHexString(pamCrasd.getMaximumPulseInterbeatInterval()) + " / "
                +Integer.toHexString(pamCrasd.getAveragePulseInterbeatInterval()));
            pamCrasdRestingHeartRateTextView.setText("Resting Heartrate "
                    +Integer.toHexString(pamCrasd.getMinimumRestingHeartRate()) + " / "
                +Integer.toHexString(pamCrasd.getMaximumRestingHeartRate()) + " / "
                +Integer.toHexString(pamCrasd.getAverageRestingHeartRate()));
            pamCrasdHeartRateVariabilityTextView.setText("HeartRate Variability "
                    +Integer.toHexString(pamCrasd.getMinimumHeartRateVariability()) + " / "
                +Integer.toHexString(pamCrasd.getMaximumHeartRateVariability()) + " / "
                +Integer.toHexString(pamCrasd.getAverageHeartRateVariability()));
            pamCrasdRespirationRateTextView.setText("RespirationRate "
                    +Integer.toHexString(pamCrasd.getMinimumRespirationRate()) + " / "
                +Integer.toHexString(pamCrasd.getMaximumRespirationRate()) + " / "
                +Integer.toHexString(pamCrasd.getAverageRespirationRate()));
            pamCrasdRestingRespirationRateTextView.setText("RestingRespiration Rate "
                    +Integer.toHexString(pamCrasd.getMinimumRestingRespirationRate()) + " / "
                +Integer.toHexString(pamCrasd.getMaximumRestingRespirationRate()) + " / "
                +Integer.toHexString(pamCrasd.getAverageRestingRespirationRate()));
            pamCrasdWornDurationTextView.setText("Worn Duration " + Long.toHexString(pamCrasd.getWornDuration()));
        }

        @Override
        public void onPamScasd(String deviceAddress, HBPhysicalActivityMonitorScasd pamScasd)
        {
            pamScasdFlagsTextView.setText(String.format("Flags: %08d", new BigInteger(Long.toBinaryString(pamScasd.getFlags()))));
            pamScasdSessionIDTextView.setText("SessionID " + (Integer.toHexString(pamScasd.getSessionID())));
            pamScasdSubSessionIDTextView.setText("Sub Session ID " + (Integer.toHexString(pamScasd.getSubSessionID())));
            pamScasdRelativeTimestampTextView.setText("Relative Timestamp " + (Long.toHexString(pamScasd.getRelativeTimestamp())));
            pamScasdSequenceNumberTextView.setText("Sequence Number " + (Long.toHexString(pamScasd.getSequenceNumber())));
            pamScasdNormalWalkingStepsTextView.setText("Normal Walking Steps " + (Long.toHexString(pamScasd.getNormalWalkingSteps())));
            pamScasdIntensityStepsTextView.setText("Intensity Steps " + (Long.toHexString(pamScasd.getIntensitySteps())));
            pamScasdFloorStepsTextView.setText("Floor Steps " + (Long.toHexString(pamScasd.getFloorSteps())));
            pamScasdDistanceTextView.setText("Distance " + (Long.toHexString(pamScasd.getDistance())));
            pamScasdWornDurationTextView.setText("Worn Duration " + (Long.toHexString(pamScasd.getWornDuration())));
        }

        @Override
        public void onPamSaid(String deviceAddress, HBPhysicalActivityMonitorSaid pamSaid)
        {
            pamSaidFlagsTextView.setText(String.format("Flags: %016d", new BigInteger(Long.toBinaryString(pamSaid.getFlags()))));
            pamSaidSessionIDTextView.setText("SessionID " + (Integer.toHexString(pamSaid.getSessionID())));
            pamSaidSubSessionIDTextView.setText("Sub Session ID " + (Integer.toHexString(pamSaid.getSubSessionID())));
            pamSaidRelativeTimestampTextView.setText("Relative Timestamp " + (Long.toHexString(pamSaid.getRelativeTimestamp())));
            pamSaidSequenceNumberTextView.setText("Sequence Number " + (Long.toHexString(pamSaid.getSequenceNumber())));
            pamSaidVisibleLightLevelTextView.setText("Visible Light Level " + (Long.toHexString(pamSaid.getVisibleLightLevel())));
            pamSaidUVLightLevelTextView.setText("UV Light Level " + (Long.toHexString(pamSaid.getUVLightLevel())));
            pamSaidIRLightLevelTextView.setText("IR Light Level " + (Long.toHexString(pamSaid.getIRLightLevel())));
            pamSaidSleepStageTextView.setText("Sleep Stage " + (Long.toHexString(pamSaid.getSleepStage())));
            pamSaidSleepingHeartRateTextView.setText("Sleeping Heart Rate " + (Long.toHexString(pamSaid.getSleepingHeartRate())));
        }

        @Override
        public void onPamSasd(String deviceAddress, HBPhysicalActivityMonitorSasd pamSasd)
        {
            pamSasdFlagsTextView            .setText(String.format("Flags: %024d", new BigInteger(Long.toBinaryString(pamSasd.getFlags()))));
            pamSasdSessionIDTextView        .setText("SessionID " + (Integer.toHexString(pamSasd.getSessionID())));
            pamSasdSubSessionIDTextView     .setText("SubSession ID " + (Integer.toHexString(pamSasd.getSubSessionID())));
            pamSasdRelativeTimestampTextView.setText("Relative Timestamp " + (Long.toHexString(pamSasd.getRelativeTimestamp())));
            pamSasdSequenceNumberTextView   .setText("Sequence Number " + (Long.toHexString(pamSasd.getSequenceNumber())));
            pamSasdTotalSleepTime           .setText("Total Sleep Time " + (Long.toHexString(pamSasd.getTotalSleepTime())));
            pamSasdTotalWakeTime            .setText("Total Wake Time " + (Long.toHexString(pamSasd.getTotalWakeTime())));
            pamSasdTotalBedTime             .setText("Total Bed Time " + (Long.toHexString(pamSasd.getTotalBedTime())));
            pamSasdNumberOfAwakenings       .setText("Number Of Awakenings " + (Integer.toHexString(pamSasd.getNumberOfAwakenings())));
            pamSasdSleepLatency             .setText("Sleep Latency " + (Integer.toHexString(pamSasd.getSleepLatency())));
            pamSasdSleepEfficiency          .setText("Sleep Efficiency " + (Integer.toHexString(pamSasd.getSleepEfficiency())));
            pamSasdSnoozeTime               .setText("SnoozeTime " + (Integer.toHexString(pamSasd.getSnoozeTime())));
            pamSasdNumberOfTossNTurnEvents  .setText("Number Of Toss & Turn Events " + (Integer.toHexString(pamSasd.getNumberOfTossNturnEvents())));
            pamSasdTimeOfAwakeningAfterAlarm.setText("Time Of Awakening After Alarm " + (Long.toHexString(pamSasd.getTimeOfAwakeningAfterAlarm())));
            pamSasdVisibleLightLevelTextView.setText("VisibleLightLevel "
                    +Long.toHexString(pamSasd.getMinimumVisibleLightLevel()) + " / "
                    +Long.toHexString(pamSasd.getMaximumVisibleLightLevel()) + " / "
                    +Long.toHexString(pamSasd.getAverageVisibleLightLevel()));
            pamSasdUVLightLevelTextView.setText("UV Light Level "
                    +Long.toHexString(pamSasd.getMinimumUVLightLevel()) + " / "
                    +Long.toHexString(pamSasd.getMaximumUVLightLevel()) + " / "
                    +Long.toHexString(pamSasd.getAverageUVLightLevel()));
            pamSasdIRLightLevelTextView.setText("IR Light Level "
                    +Long.toHexString(pamSasd.getMinimumIRLightLevel()) + " / "
                    +Long.toHexString(pamSasd.getMaximumIRLightLevel()) + " / "
                    +Long.toHexString(pamSasd.getAverageIRLightLevel()));
            pamSasdAverageSleepingHeartRate .setText("Average Sleeping Heart Rate " + (Integer.toHexString(pamSasd.getAverageSleepingHeartRate())));
            pamSasdWornDuration             .setText("Worn Duration " + (Long.toHexString(pamSasd.getWornDuration())));
        }

        @Override
        public void onPamCP(String deviceAddress, HBPhysicalActivityMonitorControlPoint pamCP)
        {
            pamCPResponse   .setText("" + String.format("%02X", pamCP.getResponse()));
            pamCPValue      .setText("" + Integer.toString(pamCP.getValue()));
        }

        @Override
        public void onPamCurrSess(String deviceAddress, HBPhysicalActivityMonitorCurrSess pamCurrSess)
        {
            pamCSFlags                    .setText(String.format("Flags: %08d", new BigInteger(Long.toBinaryString(pamCurrSess.getFlags()))));
            pamCSSessionID                .setText("SessionID " + Integer.toHexString(pamCurrSess.getSessionID()));
            pamCSSessionStartBaseTime     .setText("Session Start Base Time" + Long.toHexString(pamCurrSess.getSessionStartBaseTime()));
            pamCSSessionStartTimeOffset   .setText("Session Start Time Offset " + Integer.toHexString(pamCurrSess.getSessionStartTimeOffset()));
            pamCSSubSessionID             .setText("Sub Session ID " + Integer.toHexString(pamCurrSess.getSubSessionID()));
            pamCSSubSessionStartBaseTime  .setText("Sub Session Start Base Time " + Long.toHexString(pamCurrSess.getSubSessionStartBaseTime()));
            pamCSSubSessionStartTimeOffset.setText("SubSession Start Time Offset " + Integer.toHexString(pamCurrSess.getSubSessionStartTimeOffset()));
        }

        @Override
        public void onPamSessDescriptor(String deviceAddress, HBPhysicalActivityMonitorSessDescriptor pamSessDescriptor) {
            pamSDFlagsTextView.setText(String.format("Flags: %08d", new BigInteger(Long.toBinaryString(pamSessDescriptor.getFlags()))));
            pamSDSessionIDTextView.setText("Session ID " + Integer.toHexString(pamSessDescriptor.getSessionID()));
            pamSDSessionStartBaseTimeTextView.setText("Session Start Base Time " + Long.toHexString(pamSessDescriptor.getSessionStartBaseTime()));
            pamSDSessionStartTimeOffsetTextView.setText("Session Start Time Offset " + Integer.toHexString(pamSessDescriptor.getSessionStartTimeOffset()));
            pamSDSessionEndBaseTimeTextView.setText("Session End Base Time " + Long.toHexString((pamSessDescriptor.getSessionEndBaseTime())));
            pamSDSessionEndTimeOffsetTextView.setText("Session End Time Offset " + Integer.toHexString(pamSessDescriptor.getSessionEndTimeOffset()));
            pamSDSubSessionIDTextView.setText("Sub Session ID " + Integer.toHexString(pamSessDescriptor.getSubSessionID()));
            pamSDSubSessionStartBaseTimeTextView.setText("Sub Session Start Base Time " + Long.toHexString(pamSessDescriptor.getSubSessionStartBaseTime()));
            pamSDSubSessionStartTimeOffsetTextView.setText("Sub Session Start Time Offset " + Integer.toHexString(pamSessDescriptor.getSubSessionStartTimeOffset()));
            pamSDSubSessionEndBaseTimeTextView.setText("Sub Session End Base Time " + Long.toHexString(pamSessDescriptor.getSubSessionEndBaseTime()));
            pamSDSubSessionEndTimeOffsetTextView.setText("Sub Session End Time Offset " + Integer.toHexString(pamSessDescriptor.getSubSessionEndTimeOffset()));
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

        //region Features
        getPamFeatureButton = (Button) rootView.findViewById(R.id.getPamFeatureButton);
        getPamFeatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                hbCentral.getPhysicalActivityMonitorFeature(deviceAddress);
            }
        });

        pamFeatureTextView = (TextView) rootView.findViewById(R.id.pamFeatureTextView);
        //endregion

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

        //region Gasd
        pamGasdFlagsTextView = (TextView) rootView.findViewById(R.id.pamGasdFlagsTextView);
        pamGasdSessionIDTextView                        = (TextView) rootView.findViewById(R.id.pamGasdSessionIDTextView                     );
        pamGasdSubSessionIDTextView                     = (TextView) rootView.findViewById(R.id.pamGasdSubSessionIDTextView                  );
        pamGasdRelativeTimestampTextView                = (TextView) rootView.findViewById(R.id.pamGasdRelativeTimestampTextView             );
        pamGasdSequenceNumberTextView                   = (TextView) rootView.findViewById(R.id.pamGasdSequenceNumberTextView                );
        pamGasdNormalWalkingEnergyExpenditureTextView   = (TextView) rootView.findViewById(R.id.pamGasdNormalWalkingTextView);
        pamGasdIntensityEnergyExpenditureTextView       = (TextView) rootView.findViewById(R.id.pamGasdIntensityWalkingTextView    );
        pamGasdTotalEnergyExpenditureTextView           = (TextView) rootView.findViewById(R.id.pamGasdTotalEnergyTextView        );
        pamGasdFatBurnedTextView                        = (TextView) rootView.findViewById(R.id.pamGasdFatBurnedTextView                     );
        pamGasdMetabolicEquivalentTextView              = (TextView) rootView.findViewById(R.id.pamGasdMetabolicTextView           );
        pamGasdDistanceTextView                         = (TextView) rootView.findViewById(R.id.pamGasdDistanceTextView                      );
        pamGasdSpeedTextView                            = (TextView) rootView.findViewById(R.id.pamGasdSpeedTextView                         );
        pamGasdDurationNormalEpisodesTextView           = (TextView) rootView.findViewById(R.id.pamGasdDurationNormalEpisodesTextView        );
        pamGasdDurationIntensityEpisodesTextView        = (TextView) rootView.findViewById(R.id.pamGasdDurationIntensityEpisodesTextView     );
        pamGasdMotionCadenceTextView                    = (TextView) rootView.findViewById(R.id.pamGasdMotionTextView                 );
        pamGasdFloorsTextView                           = (TextView) rootView.findViewById(R.id.pamGasdFloorsTextView                        );
        pamGasdPosElevationTextView                     = (TextView) rootView.findViewById(R.id.pamGasdPosElevationTextView                  );
        pamGasdNegElevationTextView                     = (TextView) rootView.findViewById(R.id.pamGasdNegElevationTextView                 );
        pamGasdActivityCountTextView                    = (TextView) rootView.findViewById(R.id.pamGasdActivityCountTextView                 );
        pamGasdActivityLevelTextView                    = (TextView) rootView.findViewById(R.id.pamGasdActivityLevelTextView                 );
        pamGasdActivityTypeTextView                     = (TextView) rootView.findViewById(R.id.pamGasdActivityTypeTextView                  );
        pamGasdWornDurationTextView                     = (TextView) rootView.findViewById(R.id.pamGasdWornDurationTextView                  );
        //endregion

        //region Craid
        pamCraidFlagsTextView = (TextView) rootView.findViewById(R.id.pamCraidFlagsTextView);
        pamCraidSessionIDTextView                        = (TextView) rootView.findViewById(R.id.pamCraidSessionIDTextView                     );
        pamCraidSubSessionIDTextView                     = (TextView) rootView.findViewById(R.id.pamCraidSubSessionIDTextView                  );
        pamCraidRelativeTimestampTextView                = (TextView) rootView.findViewById(R.id.pamCraidRelativeTimestampTextView             );
        pamCraidSequenceNumberTextView                   = (TextView) rootView.findViewById(R.id.pamCraidSequenceNumberTextView                );
        pamCraidVO2MAXTextView  = (TextView) rootView.findViewById(R.id.pamCraidVO2MAXTextView);
        pamCraidHeartRateTextView  = (TextView) rootView.findViewById(R.id.pamCraidHeartRateTextView);
        pamCraidPulseInterBeatIntervalTextView  = (TextView) rootView.findViewById(R.id.pamCraidPulseInterBeatIntervalTextView);
        pamCraidRestingHeartRateTextView  = (TextView) rootView.findViewById(R.id.pamCraidRestingHeartRateTextView);
        pamCraidHeartRateVariabilityTextView  = (TextView) rootView.findViewById(R.id.pamCraidHeartRateVariabilityTextView);
        pamCraidRespirationRateTextView  = (TextView) rootView.findViewById(R.id.pamCraidRespirationRateTextView);
        pamCraidRestingRespirationRateTextView  = (TextView) rootView.findViewById(R.id.pamCraidRestingRespirationRateTextView);
        //endregion

        //region Crasd
        pamCrasdFlagsTextView                  = (TextView) rootView.findViewById(R.id.pamCrasdFlagsTextView);
        pamCrasdSessionIDTextView              = (TextView) rootView.findViewById(R.id.pamCrasdSessionIDTextView);
        pamCrasdSubSessionIDTextView           = (TextView) rootView.findViewById(R.id.pamCrasdSubSessionIDTextView);
        pamCrasdRelativeTimestampTextView      = (TextView) rootView.findViewById(R.id.pamCrasdRelativeTimestampTextView);
        pamCrasdSequenceNumberTextView         = (TextView) rootView.findViewById(R.id.pamCrasdSequenceNumberTextView);
        pamCrasdTimeInHeartRateZoneTextView    = (TextView) rootView.findViewById(R.id.pamCrasdTimeInHeartRateZoneTextView);
        pamCrasdVO2MaxTextView                 = (TextView) rootView.findViewById(R.id.pamCrasdVO2MAXTextView);
        pamCrasdHeartRateTextView              = (TextView) rootView.findViewById(R.id.pamCrasdHeartRateTextView);
        pamCrasdPulseInterbeatIntervalTextView = (TextView) rootView.findViewById(R.id.pamCrasdPulseInterbeatIntervalTextView);
        pamCrasdRestingHeartRateTextView       = (TextView) rootView.findViewById(R.id.pamCrasdRestingHeartRateTextView);
        pamCrasdHeartRateVariabilityTextView   = (TextView) rootView.findViewById(R.id.pamCrasdHeartRateVariabilityTextView);
        pamCrasdRespirationRateTextView        = (TextView) rootView.findViewById(R.id.pamCrasdRespirationRateTextView);
        pamCrasdRestingRespirationRateTextView = (TextView) rootView.findViewById(R.id.pamCrasdRestingRespirationRateTextView);
        pamCrasdWornDurationTextView           = (TextView) rootView.findViewById(R.id.pamCrasdWornDurationTextView);
        //endregion

        //region Scasd
        pamScasdFlagsTextView               = (TextView) rootView.findViewById(R.id.pamScasdFlagsTextView);
        pamScasdSessionIDTextView           = (TextView) rootView.findViewById(R.id.pamScasdSessionIDTextView);
        pamScasdSubSessionIDTextView        = (TextView) rootView.findViewById(R.id.pamScasdSubSessionIDTextView);
        pamScasdRelativeTimestampTextView   = (TextView) rootView.findViewById(R.id.pamScasdRelativeTimestampTextView);
        pamScasdSequenceNumberTextView      = (TextView) rootView.findViewById(R.id.pamScasdSequenceNumberTextView);
        pamScasdNormalWalkingStepsTextView  = (TextView) rootView.findViewById(R.id.pamScasdNormalWalkingStepsTextView);
        pamScasdIntensityStepsTextView      = (TextView) rootView.findViewById(R.id.pamScasdIntensityStepsTextView);
        pamScasdFloorStepsTextView          = (TextView) rootView.findViewById(R.id.pamScasdFloorStepsTextView);
        pamScasdDistanceTextView            = (TextView) rootView.findViewById(R.id.pamScasdDistanceTextView);
        pamScasdWornDurationTextView        = (TextView) rootView.findViewById(R.id.pamScasdWornDurationTextView);
        //endregion

        //region Said
        pamSaidFlagsTextView               = (TextView) rootView.findViewById(R.id.pamSaidFlagsTextView);
        pamSaidSessionIDTextView           = (TextView) rootView.findViewById(R.id.pamSaidSessionIDTextView);
        pamSaidSubSessionIDTextView        = (TextView) rootView.findViewById(R.id.pamSaidSubSessionIDTextView);
        pamSaidRelativeTimestampTextView   = (TextView) rootView.findViewById(R.id.pamSaidRelativeTimestampTextView);
        pamSaidSequenceNumberTextView      = (TextView) rootView.findViewById(R.id.pamSaidSequenceNumberTextView);
        pamSaidVisibleLightLevelTextView   = (TextView) rootView.findViewById(R.id.pamSaidVisibleLightLevelTextView);
        pamSaidUVLightLevelTextView        = (TextView) rootView.findViewById(R.id.pamSaidUVLightLevelTextView);
        pamSaidIRLightLevelTextView        = (TextView) rootView.findViewById(R.id.pamSaidIRLightLevelTextView);
        pamSaidSleepStageTextView          = (TextView) rootView.findViewById(R.id.pamSaidSleepStageTextView);
        pamSaidSleepingHeartRateTextView   = (TextView) rootView.findViewById(R.id.pamSaidSleepingHeartRateTextView);
        //endregion

        //region Sasd
        pamSasdFlagsTextView               = (TextView) rootView.findViewById(R.id.pamSasdFlagsTextView);
        pamSasdSessionIDTextView           = (TextView) rootView.findViewById(R.id.pamSasdSessionIDTextView);
        pamSasdSubSessionIDTextView        = (TextView) rootView.findViewById(R.id.pamSasdSubSessionIDTextView);
        pamSasdRelativeTimestampTextView   = (TextView) rootView.findViewById(R.id.pamSasdRelativeTimestampTextView);
        pamSasdSequenceNumberTextView      = (TextView) rootView.findViewById(R.id.pamSasdSequenceNumberTextView);
        pamSasdTotalSleepTime           = (TextView) rootView.findViewById(R.id.pamSasdTotalSleepTimeTextView);
        pamSasdTotalWakeTime            = (TextView) rootView.findViewById(R.id.pamSasdTotalWakeTimeTextView);
        pamSasdTotalBedTime             = (TextView) rootView.findViewById(R.id.pamSasdTotalBedTimeTextView);
        pamSasdNumberOfAwakenings       = (TextView) rootView.findViewById(R.id.pamSasdNumberOfAwekeningsTextView);
        pamSasdSleepLatency             = (TextView) rootView.findViewById(R.id.pamSasdSleepLatencyTextView);
        pamSasdSleepEfficiency          = (TextView) rootView.findViewById(R.id.pamSasdSleepEfficiencyTextView);
        pamSasdSnoozeTime               = (TextView) rootView.findViewById(R.id.pamSasdSnoozeTimeTextView);
        pamSasdNumberOfTossNTurnEvents  = (TextView) rootView.findViewById(R.id.pamSasdNumberofTossNTurnEventsTextView);
        pamSasdTimeOfAwakeningAfterAlarm= (TextView) rootView.findViewById(R.id.pamSasdTimeOfAwakeningAfterAlarmTextView);
        pamSasdVisibleLightLevelTextView= (TextView) rootView.findViewById(R.id.pamSasdVisibleLightLevelTextView);
        pamSasdUVLightLevelTextView     = (TextView) rootView.findViewById(R.id.pamSasdUVLightLevelTextView);
        pamSasdIRLightLevelTextView     = (TextView) rootView.findViewById(R.id.pamSasdIRLightLevelTextView);
        pamSasdAverageSleepingHeartRate = (TextView) rootView.findViewById(R.id.pamSasdSleepingHeartRateTextView);
        pamSasdWornDuration             = (TextView) rootView.findViewById(R.id.pamSasdWornDurationTextView);
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

                    swappedSessionID = ((byte)(mySessionID & 0xFF) << 8) | (byte)((mySessionID >> 8) & 0xFF);
                    swappedSubSessionID = ((byte)(mySubSessionID & 0xFF) << 8) | (byte)((mySubSessionID >> 8) & 0xFF);

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

        pamControlPointSetAverageActivityTypeCurrentWriteButton = (Button) rootView.findViewById((R.id.pamCPSetAverageActivityTypeCurrent));
        pamControlPointSetAverageActivityTypeCurrentWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pamControlPointSetActivityTypeValue.getText().toString().equals("")) {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.SetAverageActivityType, 0, 0));
                }
                else
                {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.SetAverageActivityType, 0, Integer.parseInt(pamControlPointSetActivityTypeValue.getText().toString())));
                }
            }
        });
        pamControlPointSetAverageActivityTypeAllWriteButton = (Button) rootView.findViewById((R.id.pamCPSetAverageActivityTypeAll));
        pamControlPointSetAverageActivityTypeAllWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pamControlPointSetActivityTypeValue.getText().toString().equals("")) {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.SetAverageActivityType, 1,0));
                }
                else
                {
                    hbCentral.writeToPamCP(deviceAddress, new HBPhysicalActivityMonitorControlPoint(HBControlPointOperation.SetAverageActivityType, 1, Integer.parseInt(pamControlPointSetActivityTypeValue.getText().toString())));
                }
            }
        });

        pamCPResponse = (TextView) rootView.findViewById(R.id.pamCPResponse);
        pamCPValue    = (TextView) rootView.findViewById(R.id.pamCPRValue);

        pamControlPointEnquireSubSessionID = (EditText) rootView.findViewById(R.id.pamCPGetSubSessionsSessionID);
        pamControlPointGetEndedSessionID = (EditText) rootView.findViewById(R.id.pamCPGetEndedSessionID);
        pamControlPointGetEndedSubSessionID = (EditText) rootView.findViewById(R.id.pamCPGetEndedSubSessionID);
        pamControlPointGetEndedData = (EditText) rootView.findViewById(R.id.pamCPGetEndedData);
        pamControlPointDeleteSessionID = (EditText) rootView.findViewById(R.id.pamCPDeleteSessionID);
        pamControlPointSetActivityTypeValue = (EditText) rootView.findViewById(R.id.pamCPUserDefinedActivityType);
        //endregion

        //region CurrSess
        pamCSFlags                     = (TextView)rootView.findViewById(R.id.pamCSFlagsTextView);
        pamCSSessionID                 = (TextView)rootView.findViewById(R.id.pamCSSessionIDTextView);
        pamCSSessionStartBaseTime      = (TextView)rootView.findViewById(R.id.pamCSSessionStartBaseTimeTextView);
        pamCSSessionStartTimeOffset    = (TextView)rootView.findViewById(R.id.pamCSSessionStartTimeOffsetTextView);
        pamCSSubSessionID              = (TextView)rootView.findViewById(R.id.pamCSSubSessionIDTextView);
        pamCSSubSessionStartBaseTime   = (TextView)rootView.findViewById(R.id.pamCSSubSessionStartBaseTimeTextView);
        pamCSSubSessionStartTimeOffset = (TextView)rootView.findViewById(R.id.pamCSSubSessionStartTimeOffsetTextView);
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
