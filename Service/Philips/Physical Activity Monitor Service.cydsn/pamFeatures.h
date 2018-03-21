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

#include "project.h"

/***************************************
*          Constants
***************************************/
#define PAM_FLAGS_COUNT         (64u)  


#define MULTIPLE_USERS_SUPPORTED                                0ull
#define DEVICE_WORN_SUPPORTED                                   1ull
#define NORMAL_WALKING_ENERGY_EXPENDITURE_SUPPORTED             2ull
#define NORMAL_WALKING_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED    3ull
#define INTENSITY_ENERGY_EXPENDITURE_SUPPORTED                  4ull
#define INTENSITY_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED         5ull
#define TOTAL_ENERGY_EXPENDITURE_SUPPORTED                      6ull
#define TOTAL_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED             7ull
#define FAT_BURNED_SUPPORTED                                    8ull
#define FAT_BURNED_PER_HOUR_SUPPORTED                           9ull
#define METABOLIC_EQUIVALENT_SUPPORTED                          10ull
#define DISTANCE_SUPPORTED                                      11ull
#define SPEED_SUPPORTED                                         12ull    
#define DURATION_OF_NORMAL_WALKING_EPISODES_SUPPORTED           13ull    
#define DURATION_OF_INTENSITY_WALKING_EPISODES_SUPPORTED        14ull
#define MOTION_CADENCE_SUPPORTED                                15ull
#define FLOORS_SUPPORTED                                        16ull
#define POSITIVE_ELEVATION_GAIN_SUPPORTED                       17ull
#define NEGATIVE_ELEVATION_GAIN_SUPPORTED                       18ull
#define ELEVATION_SUPPORTED                                     19ull
#define ACTIVITY_COUNT_SUPPORTED                                20ull
#define ACTIVITY_COUNT_PER_MINUTE_SUPPORTED                     21ull
#define ACTIVITY_LEVEL_SUPPORTED                                22ull
#define ACTIVITY_TYPE_SUPPORTED                                 23ull
#define WORN_DURATION_SUPPORTED                                 24ull
#define TIME_IN_HEART_RATE_ZONE1_SUPPORTED                      25ull
#define TIME_IN_HEART_RATE_ZONE2_SUPPORTED                      26ull
#define TIME_IN_HEART_RATE_ZONE3_SUPPORTED                      27ull
#define TIME_IN_HEART_RATE_ZONE4_SUPPORTED                      28ull
#define TIME_IN_HEART_RATE_ZONE5_SUPPORTED                      29ull
#define VO2_MAX_SUPPORTED                                       30ull
#define HEART_RATE_SUPPORTED                                    31ull
#define PULSE_INTER_BEAT_INTERVAL_SUPPORTED                     32ull
#define RESTING_HEART_RATE_SUPPORTED                            33ull    
#define HEART_RATE_VARIABILITY_SUPPORTED                        34ull
#define RESPIRATION_RATE_SUPPORTED                              35ull
#define RESTING_RESPIRATION_RATE_SUPPORTED                      36ull
#define NORMAL_WALKING_STEPS_SUPPORTED                          37ull
#define INTENSITY_STEPS_SUPPORTED                               38ull
#define FLOOR_STEPS_SUPPORTED                                   39ull
#define TOTAL_SLEEP_TIME_SUPPORTED                              40ull    
#define TOTAL_WAKE_TIME_SUPPORTED                               41ull
#define TOTAL_BED_TIME_SUPPORTED                                42ull    
#define NUMBER_OF_AWAKENINGS_SUPPORTED                          43ull    
#define SLEEP_LATENCY_SUPPORTED                                 44ull
#define SLEEP_EFFICIENCY_SUPPORTED                              45ull
#define SNOOZE_TIME_SUPPORTED                                   46ull
#define NUMBER_OF_TOSS_N_TURN_EVENTS_SUPPORTED                  47ull
#define TIME_OF_AWAKENING_AFTER_ALARM_SUPPORTED                 48ull
#define VISIBLE_LIGHT_LEVEL_SUPPORTED                           49ull
#define UV_LIGHT_LEVEL_SUPPORTED                                50ull                              
#define IR_LIGHT_LEVEL_SUPPORTED                                51ull
#define SLEEP_STAGE_SUPPORTED                                   52ull
#define SLEEPING_HEART_RATE_SUPPORTED                           53ull
#define THREED_ACCELERATION_SUPPORTED                           54ull
#define THREED_ANGULAR_ACCELERATION_SUPPORTED                   55ull
#define THREED_MAGNETIC_FIELD_SUPPORTED                         56ull
#define ECG_WAVE_SUPPORTED                                      57ull
#define PPG_WAVE_SUPPORTED                                      58ull
#define PAMFRFU1                                                59ull
#define PAMFRFU2                                                60ull
#define PAMFRFU3                                                61ull
#define PAMFRFU4                                                62ull
#define PAMFRFU5                                                63ull


/***************************************
*        Function Prototypes
***************************************/
void UpdateFeatureList(void);
uint64 getChosenFeatures(void);

uint64 pamFeature;

/* [] END OF FILE */
