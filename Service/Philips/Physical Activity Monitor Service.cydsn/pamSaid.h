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

#define ALWAYS_PRESENT_SAID_FIELD_LENGTH           15u

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
    uint8	VisibleLightLevel[3];
    uint8	UVLightLevel[3];
    uint8	IRLightLevel[3];
    uint8	SleepStage;
    uint8	SleepingHeartRate;

}CYBLE_CYPACKED_ATTR PAM_SAID_VALUE_T;

/***************************************
*        Function Prototypes
***************************************/
void UpdateSaid(void);
uint8 getPresentSaidFields(uint8 *PresentData);

PAM_SAID_VALUE_T SaiData;

int sleepactivityinstantaneousdataNotify;

/* [] END OF FILE */
