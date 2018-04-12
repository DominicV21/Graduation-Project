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

#define ALWAYS_PRESENT_CRAID_FIELD_LENGTH           15u

/***************************************
*       Data Struct Definition
***************************************/

CYBLE_CYPACKED typedef struct
{
    uint8	Header;
    uint16	Flags;
    uint16	SessionID;
    uint16	SubSessionID;
    uint32	RelativeTimestamp;
    uint32	SequenceNumber;
    uint8	VO2Max;
    uint8	HeartRate;
    uint16	PulseInterBeatInterval;
    uint8	RestingHeartRate;
    uint16	HeartRateVariability;
    uint8	RespirationRate;
    uint8	RestingRespirationRate;
    
}CYBLE_CYPACKED_ATTR PAM_CRAID_VALUE_T;



/***************************************
*        Function Prototypes
***************************************/
void UpdateCraid(void);
uint8 getPresentCraidFields(uint8 *PresentData);

PAM_CRAID_VALUE_T CraiData;

int cardiorespiratoryactivityinstantaneousdataNotify;

/* [] END OF FILE */
