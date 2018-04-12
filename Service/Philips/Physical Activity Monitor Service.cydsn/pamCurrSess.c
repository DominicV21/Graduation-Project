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

#include "pamCurrSess.h"

void UpdateCurrSession(PAM_SESSIONDESCRIPTOR_VALUE_T *session)
{
    CYBLE_GATTS_HANDLE_VALUE_IND_T tempHandle;
    
    if(CHECK_BIT(session->Flags, SESSION_SUB_SESSION_RUNNING_FLAG))
    {
        SET_BIT(CurrSession.Flags, CURRENT_SESSION_SUB_SESSION_RUNNING_FLAG);
    }
    else
    {
        UNSET_BIT(CurrSession.Flags, CURRENT_SESSION_SUB_SESSION_RUNNING_FLAG);
    }
    
    
    
    CurrSession.SessionID = session->SessionID;
    CurrSession.SessionStartBaseTime = session->SessionStartBaseTime;
    CurrSession.SessionStartTimeOffset = session->SessionStartTimeOffset;
    CurrSession.SubSessionID = session->SubSessionID;
    CurrSession.SubSessionStartBaseTime = session->SessionStartBaseTime;
    CurrSession.SubSessionStartTimeOffset = session->SessionStartTimeOffset;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_CURRENT_SESSION_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&CurrSession;
    tempHandle.value.len = sizeof(CurrSession);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(CyBle_GattGetBusStatus() == CYBLE_STACK_STATE_FREE && currentsessionIndicate == 2)
    {
        DBG_PRINTF("CURRENT SESSION SIZE = %i \r\n", sizeof(CurrSession));
        ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_CURRENT_SESSION_CHAR_HANDLE, (uint8 *)&CurrSession, sizeof(CurrSession));
    }
}

/* [] END OF FILE */
