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

#include "pamSessDescr.h"


void UpdateSessionDescriptor(PAM_SESSIONDESCRIPTOR_VALUE_T session)
{
    CYBLE_GATTS_HANDLE_VALUE_IND_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_SESSION_DESCRIPTOR_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&session;
    tempHandle.value.len = sizeof(session);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(sessionDescriptorIndicate == 2)
    {
        uint8 byteCount = 0u;
        
        uint8  Message[PAM_MAX_MESSAGE_DATA_SIZE];
        
        Message[byteCount++] = session.Flags;
        CyBle_Set16ByPtr(&Message[byteCount], session.SessionID);
        byteCount += 2u;
        
        if(CHECK_BIT(session.Flags, DESCRIBES_SESSION_FLAG))
        {
            //if first flag is set to 1 it is a "Session"
                //include session start base time && session start time offset else exclude
            CyBle_Set32ByPtr(&Message[byteCount], session.SessionStartBaseTime);
            byteCount += 4u;
            CyBle_Set16ByPtr(&Message[byteCount], session.SessionStartTimeOffset);
            byteCount += 2u;
            
            if(!CHECK_BIT(session.Flags, SESSION_SUB_SESSION_RUNNING_FLAG))
            {
                //if second flag is set to 0 it is NOT "Running"
                    //include corresponding end time fields
                CyBle_Set32ByPtr(&Message[byteCount], session.SessionEndBaseTime);
                byteCount += 4u;
                CyBle_Set32ByPtr(&Message[byteCount], session.SessionEndTimeOffset);
                byteCount += 2u;
            }
            //if second flag is set to 1 it is "Running"
                //Do not include extra fields
        }
        else
        {
            //if first flag is set to 0 it is a "Sub-Session"
                //include subsessionID && subsession start base time && subsession start time offset
            CyBle_Set16ByPtr(&Message[byteCount], session.SubSessionID);
            byteCount += 2u;
            CyBle_Set32ByPtr(&Message[byteCount], session.SubSessionStartBaseTime);
            byteCount += 4u;
            CyBle_Set16ByPtr(&Message[byteCount], session.SubSessionStartTimeOffset);
            byteCount += 2u;
            
            if(!CHECK_BIT(session.Flags, SESSION_SUB_SESSION_RUNNING_FLAG))
            {
                //if second flag is set to 0 it is NOT "Running"
                    //include corresponding end time fields
                CyBle_Set32ByPtr(&Message[byteCount], session.SubSessionEndBaseTime);
                byteCount += 4u;
                CyBle_Set16ByPtr(&Message[byteCount], session.SubSessionEndTimeOffset);
                byteCount += 2u;
            }
            //if second flag is set to 1 it is "Running"
                //Do not include extra fields
        }
        
        CyBle_Set16ByPtr(&Message[byteCount], session.PredominantActivityType);
        byteCount += 2u;
        
        //if third flag is set to 1 it is a "DELETED" session
        //if third flag is set to 0 it is NOT "DELETED" session
    
        DBG_PRINTF("SESSION DESCRIPTOR SIZE = %i \r\n", byteCount);
        ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_SESSION_DESCRIPTOR_CHAR_HANDLE, Message, byteCount);
    }
}

/* [] END OF FILE */
