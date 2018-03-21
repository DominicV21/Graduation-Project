package chdsw.philips.com.haraldtestapp;

import java.util.UUID;

import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryServiceHandler;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeServiceHandler;
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationServiceHandler;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerServiceHandler;
import chdsw.philips.com.haraldlib.services.heartrate.HBHeartRateServiceHandler;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUServiceHandler;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleServiceHandler;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.HBPhysicalActivityMonitorServiceHandler;

public enum ServiceDefinition {
    HealthThermometerService(HBHealthThermometerServiceHandler.SERVICE_UUID, R.string.health_thermometer_service),
    CurrentTimeService(HBCurrentTimeServiceHandler.SERVICE_UUID, R.string.current_time_service),
    NordicOtaService(HBNordicDFUServiceHandler.SERVICE_UUID, R.string.nordic_ota_service),
    BatteryLevelService(HBBatteryServiceHandler.SERVICE_UUID, R.string.battery_level_service),
    DeviceInformationService(HBDeviceInformationServiceHandler.SERVICE_UUID, R.string.device_information_service),
    WeightScaleService(HBWeightScaleServiceHandler.SERVICE_UUID, R.string.weight_scale_service),
    HeartRateService(HBHeartRateServiceHandler.SERVICE_UUID, R.string.heart_rate_service),
    PhysicalActivityMonitorService(HBPhysicalActivityMonitorServiceHandler.SERVICE_UUID, R.string.physical_activity_monitor_service);


    private UUID serviceUUID;
    private int resourceId;

    ServiceDefinition(UUID serviceUUID, int resourceId) {
        this.serviceUUID = serviceUUID;
        this.resourceId = resourceId;
    }

    public UUID getUUID() {
        return serviceUUID;
    }

    public int getResourceId() {
        return resourceId;
    }

    public static ServiceDefinition fromValue(UUID serviceUUID) {
        for(ServiceDefinition type : values()) {
            if(type.getUUID() == serviceUUID)
                return type;
        }
        return null;
    }
}