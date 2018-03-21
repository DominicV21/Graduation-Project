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

#define ALWAYS_PRESENT_SCASD_FIELD_LENGTH           14u

/***************************************
*       Data Struct Definition
***************************************/

CYBLE_CYPACKED typedef struct
{
    uint8   Header;
    uint8	Flags;
    uint16	SessionID;
    uint16	SubSessionID;
    uint32	RelativeTimestamp;
    uint32	SequenceNumber;
    uint8	NormalWalkingSteps[3];
    uint8	IntensitySteps[3];
    uint8	FloorSteps[3];
    uint8	Distance[3];
    uint8	WornDuration[3];
    
}CYBLE_CYPACKED_ATTR PAM_SCASD_VALUE_T;

/***************************************
*        Function Prototypes
***************************************/
void UpdateScasd(void);
uint8 getPresentScasdFields(uint8 *PresentData);

PAM_SCASD_VALUE_T ScasData;

int stepcounteractivitysummarydataIndicate;

/* [] END OF FILE */
