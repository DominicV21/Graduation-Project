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

#ifndef PAMSESSDESCR_H__
#define PAMSESSDESCR_H__
    
#include "common.h"
    
#define DESCRIBES_SESSION_FLAG                      0u
#define SESSION_SUB_SESSION_RUNNING_FLAG            1u
#define DELETED_SESSION_FLAG                        2u

CYBLE_CYPACKED typedef struct
{
    uint8 Flags;
    uint16 SessionID;
    uint32 SessionStartBaseTime;
    int16  SessionStartTimeOffset;
    uint32 SessionEndBaseTime;
    int16  SessionEndTimeOffset;
    uint16 SubSessionID;
    uint32 SubSessionStartBaseTime;
    int16  SubSessionStartTimeOffset;
    uint32 SubSessionEndBaseTime;
    int16  SubSessionEndTimeOffset;
    
} CYBLE_CYPACKED_ATTR PAM_SESSIONDESCRIPTOR_VALUE_T;

CYBIT UpdateSessionDescriptor(PAM_SESSIONDESCRIPTOR_VALUE_T session);
uint8 sessionDescriptorIndicate;

#endif /* PAMSESSDESCR_H__ */

/* [] END OF FILE */