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

#ifndef PAMGASD_H__
#define PAMGASD_H__

#include "common.h"
    
/***************************************
*          Constants
***************************************/
#define NORMAL_WALKING_ENERGY_EXPENDITURE_PRESENT	    0u
#define INTENSITY_ENERGY_EXPENDITURE_PRESENT	        1u
#define TOTAL_ENERGY_EXPENDITURE_PRESENT	            2u
#define FAT_BURNED_PRESENT	                            3u
#define MINIMUM_METABOLIC_EQUIVALENT_PRESENT	        4u
#define MAXIMUM_METABOLIC_EQUIVALENT_PRESENT	        5u
#define AVERAGE_METABOLIC_EQUIVALENT_PRESENT	        6u
#define DISTANCE_PRESENT	                            7u
#define MINIMUM_SPEED_PRESENT	                        8u
#define MAXIMUM_SPEED_PRESENT	                        9u
#define AVERAGE_SPEED_PRESENT	                        10u
#define DURATION_OF_NORMAL_WALKING_EPISODES_PRESENT	    11u
#define DURATION_OF_INTENSITY_WALKING_EPISODES_PRESENT	12u
#define MINIMUM_MOTION_CADENCE_PRESENT	                13u
#define MAXIMUM_MOTION_CADENCE_PRESENT	                14u
#define AVERAGE_MOTION_CADENCE_PRESENT	                15u
#define FLOORS_PRESENT	                                16u
#define POSITIVE_ELEVATION_GAIN_PRESENT	                17u
#define NEGATIVE_ELEVATION_GAIN_PRESENT	                18u
#define ACTIVITY_COUNT_PRESENT	                        19u
#define MINIMUM_ACTIVITY_LEVEL_PRESENT	                20u
#define MAXIMUM_ACTIVITY_LEVEL_PRESENT	                21u
#define AVERAGE_ACTIVITY_LEVEL_PRESENT	                22u
#define AVERAGE_ACTIVITY_TYPE_PRESENT	                23u
#define WORN_DURATION_PRESENT	                        24u

#define ALWAYS_PRESENT_GASD_FIELD_LENGTH                     17u

/***************************************
*       Data Struct Definition
***************************************/
CYBLE_CYPACKED typedef struct
{
    uint8 Header;
    uint32 Flags;
    uint16 SessionID;
    uint16 SubSessionID;
    uint32 RelativeTimestamp;
    uint32 SequenceNumber;
    uint32 NormalWalkingEnergyExpenditure;
    uint32 IntensityEnergyExpenditure;
    uint32 TotalEnergyExpenditure;
    uint16 FatBurned;
    uint8 MinimumMetabolicEquivalent;
    uint8 MaximumMetabolicEquivalent;
    uint8 AverageMetabolicEquivalent;
    uint8 Distance[3];
    uint16 MinimumSpeed;
    uint16 MaximumSpeed;
    uint16 AverageSpeed;
    uint8 DurationOfNormalWalkingEpisodes[3];
    uint8 DurationOfIntensityWalkingEpisodes[3];
    uint16 MinimumMotionCadence;
    uint16 MaximumMotionCadence;
    uint16 AverageMotionCadence;
    int8 Floors;
    uint8 PositiveElevationGain[3];
    uint8 NegativeElevationGain[3];
    uint32 ActivityCount;
    uint16 MinimumActivityLevel;
    uint16 MaximumActivityLevel;
    uint16 AverageActivityLevel;
    uint16 AverageActivityType;
    uint8 WornDuration[3];
    
}CYBLE_CYPACKED_ATTR PAM_GASD_VALUE_T;


/***************************************
*        Function Prototypes
***************************************/
void UpdateGasd(void);
uint8 getPresentGasdFields(uint8 *PresentData);

PAM_GASD_VALUE_T GasData;

int generalsummarydataIndicate;

#endif /* PAMGAID_H__ */

#define PAMGAID_H__

/* [] END OF FILE */
