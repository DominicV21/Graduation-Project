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

#include "common.h"

/***************************************
*          Constants
***************************************/

//Flags field needs to be specified in specification

#define ALWAYS_PRESENT_SASD_FIELD_LENGTH           16u

/***************************************
*       Data Struct Definition
***************************************/

CYBLE_CYPACKED typedef struct
{
    uint8	Header;
    uint8	Flags[3];
    uint16	SessionID;
    uint16	SubSessionID;
    uint32	RelativeTimestamp;
    uint32	SequenceNumber;
    uint8	TotalSleepTime[3];
    uint8	TotalWakeTime[3];
    uint8	TotalBedTime[3];
    uint16	NumberOfAwakenings;
    uint16	SleepLatency;
    uint8	SleepEfficiency;
    uint16	SnoozeTime;
    uint16	NumberOfTossNturnEvents;
    uint8	TimeOfAwakeningAfterAlarm[3];
    uint8	MinimumVisibleLightLevel[3];
    uint8	MaximumVisibleLightLevel[3];
    uint8	AverageVisibleLightLevel[3];
    uint8	MinimumUVLightLevel[3];
    uint8	MaximumUVLightLevel[3];
    uint8	AverageUVLightLevel[3];
    uint8	MinimumIRLightLevel[3];
    uint8	MaximumIRLightLevel[3];
    uint8	AverageIRLightLevel[3];
    uint8	AverageSleepingHeartRate;
    uint8	WornDuration[3];

}CYBLE_CYPACKED_ATTR PAM_SASD_VALUE_T;

/***************************************
*        Function Prototypes
***************************************/
void UpdateSasd(void);
uint8 getPresentSasdFields(uint8 *PresentData);

PAM_SASD_VALUE_T SasData;

int sleepactivitysummarydataIndicate;

/* [] END OF FILE */
