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

#define ALWAYS_PRESENT_CRASD_FIELD_LENGTH           17u

/***************************************
*       Data Struct Definition
***************************************/

CYBLE_CYPACKED typedef struct
{
    uint8	Header;
    uint32	Flags;
    uint16	SessionID;
    uint16	SubSessionID;
    uint32	RelativeTimestamp;
    uint32	SequenceNumber;
    uint8	TimeInHeartRateZone1[3];
    uint8	TimeInHeartRateZone2[3];
    uint8	TimeInHeartRateZone3[3];
    uint8	TimeInHeartRateZone4[3];
    uint8	TimeInHeartRateZone5[3];
    uint8	MinimumVO2Max;
    uint8	MaximumVO2Max;
    uint8	AverageVO2Max;
    uint8	MinimumHeartRate;
    uint8	MaximumHeartRate;
    uint8	AverageHeartRate;
    uint16	MinimumPulseInterbeatInterval;
    uint16	MaximumPulseInterbeatInterval;
    uint16	AveragePulseInterbeatInterval;
    uint8	MinimumRestingHeartRate;
    uint8	MaximumRestingHeartRate;
    uint8	AverageRestingHeartRate;
    uint16	MinimumHeartRateVariability;
    uint16	MaximumHeartRateVariability;
    uint16	AverageHeartRateVariability;
    uint8	MinimumRespirationRate;
    uint8	MaximumRespirationRate;
    uint8	AverageRespirationRate;
    uint8	MinimumRestingRespirationRate;
    uint8	MaximumRestingRespirationRate;
    uint8	AverageRestingRespirationRate;
    uint8	WornDuration[3];

}CYBLE_CYPACKED_ATTR PAM_CRASD_VALUE_T;

/***************************************
*        Function Prototypes
***************************************/
void UpdateCrasd(void);
uint8 getPresentCrasdFields(uint8 *PresentData);

PAM_CRASD_VALUE_T CrasData;

int cardiorespiratoryactivitysummarydataIndicate;

/* [] END OF FILE */