/* ========================================
 *
 * Copyright YOUR COMPANY, THE YEAR
 * All Rights Reserved
 * UNPUBLISHED, LICENSED SOFTWARE.
 *
 * CONFIDENTIAL AND PROPRIETARY INFORMATION
 * WHICH IS THE PROPERTY OF your company.
 *
 * ========================================
*/

#include "pamFeatures.h"

uint64 getChosenFeatures()
{
    uint64 chosenFeatures;
    return chosenFeatures = 1ull << MULTIPLE_USERS_SUPPORTED |
                            1ull << DEVICE_WORN_SUPPORTED |
                            1ull << NORMAL_WALKING_ENERGY_EXPENDITURE_SUPPORTED |
                            1ull << NORMAL_WALKING_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED |
                            1ull << INTENSITY_ENERGY_EXPENDITURE_SUPPORTED |
                            1ull << INTENSITY_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED |
                            1ull << TOTAL_ENERGY_EXPENDITURE_SUPPORTED |
                            1ull << TOTAL_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED |
                             
                            1ull << FAT_BURNED_SUPPORTED |
                            1ull << FAT_BURNED_PER_HOUR_SUPPORTED |
                            1ull << METABOLIC_EQUIVALENT_SUPPORTED |
                            1ull << DISTANCE_SUPPORTED |
                             
                            1ull << SPEED_SUPPORTED | 
                             
                            1ull << FLOORS_SUPPORTED |
                            1ull << POSITIVE_ELEVATION_GAIN_SUPPORTED |
                            1ull << NEGATIVE_ELEVATION_GAIN_SUPPORTED |
                            1ull << ELEVATION_SUPPORTED |
                             
                            1ull << WORN_DURATION_SUPPORTED |
                            1ull << TIME_IN_HEART_RATE_ZONE1_SUPPORTED |
                            1ull << TIME_IN_HEART_RATE_ZONE2_SUPPORTED |
                             
                            1ull << PULSE_INTER_BEAT_INTERVAL_SUPPORTED |
                            1ull << RESTING_HEART_RATE_SUPPORTED |
                             
                            1ull << TOTAL_SLEEP_TIME_SUPPORTED |
                             
                            1ull << TIME_OF_AWAKENING_AFTER_ALARM_SUPPORTED |
                            
                            1ull << THREED_MAGNETIC_FIELD_SUPPORTED   ;
}

void UpdateFeatureList(void)
{
    CYBLE_GATT_HANDLE_VALUE_PAIR_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_PHYSICAL_ACTIVITY_MONITOR_FEATURE_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&pamFeature;
    tempHandle.value.len = sizeof(pamFeature);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
}


/* [] END OF FILE */
