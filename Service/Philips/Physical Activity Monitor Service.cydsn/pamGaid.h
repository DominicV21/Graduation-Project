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

#ifndef PAMGAID_H__
#define PAMGAID_H__

#include "common.h"   
    
/***************************************
*          Constants
***************************************/
#define NORMAL_WALKING_ENERGY_EXPENDITURE_PER_HOUR_PRESENT      0u
#define INTENSITY_ENERGY_EXPENDITURE_PER_HOUR_PRESENT           1u
#define TOTAL_ENERGY_EXPENDITURE_PER_HOUR_PRESENT               2u
#define FAT_BURNED_PER_HOUR_PRESENT                             3u
#define METABOLIC_EQUIVALENT_PRESENT                            4u
#define SPEED_PRESENT                                           5u
#define MOTION_CADENCE_PRESENT                                  6u
#define ELEVATION_PRESENT                                       7u
#define ACTIVITY_COUNT_PER_MINUTE_PRESENT                       8u
#define ACTIVITY_LEVEL_PRESENT                                  9u
#define ACTIVITY_TYPE_PRESENT                                   10u

#define ALWAYS_PRESENT_GAID_FIELD_LENGTH                             16u

/***************************************
*       Data Struct Definition
***************************************/

CYBLE_CYPACKED typedef struct
{
    uint8  Header;
    uint8  Flags[3u];
    uint16 SessionID;
    uint16 SubSessionID;
    uint32 RelativeTimestamp;
    uint32 SequenceNumber;
    uint16 NormalWalkingEnergyExpenditurePerHour;
    uint16 IntensityEnergyExpenditurePerHour;
    uint16 TotalEnergyExpenditurePerHour;
    uint16 FatBurnedPerHour;
    uint8  MetabolicEquivalent;
    uint16 Speed;
    uint16 MotionCadence;
    int8   Elevation[3u];
    uint16 ActivityCountPerMinute;
    uint16 ActivityLevel;
    uint16 ActivityType;
    
}CYBLE_CYPACKED_ATTR PAM_GAID_VALUE_T;

/***************************************
*        Function Prototypes
***************************************/
void UpdateGaid(void);
uint8 getPresentGaidFields(uint8 *PresentData);

PAM_GAID_VALUE_T GaiData;

int generalinstantaneousdataNotify;

#endif /* PAMGAID_H__ */

/* [] END OF FILE */