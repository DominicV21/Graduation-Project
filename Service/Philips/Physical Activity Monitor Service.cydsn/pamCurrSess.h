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

#ifndef PAMCURRSESS_H__
#define PAMCURRSESS_H__
    
#include "common.h"
    
 #define CURRENT_SESSION_SUB_SESSION_RUNNING_FLAG            0u

CYBLE_CYPACKED typedef struct
{
    uint8 Flags;
    uint16 SessionID;
    uint32 SessionStartBaseTime;
    int16  SessionStartTimeOffset;
    uint16 SubSessionID;
    uint32 SubSessionStartBaseTime;
    int16  SubSessionStartTimeOffset;
    uint16 PredominantActivityType;
    
}CYBLE_CYPACKED_ATTR PAM_CURRSESSION_VALUE_T;

void UpdateCurrSession(PAM_SESSIONDESCRIPTOR_VALUE_T *session);
PAM_CURRSESSION_VALUE_T CurrSession;
int currentsessionIndicate;

/* [] END OF FILE */

#endif /* PAMCURRSESS_H__*/