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

#include "pamControlPoint.h"

void updateControlPoint(uint8 opCode, uint8 param)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    uint8 myIndication[2] = { opCode, param };
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&myIndication;
    tempHandle.value.len = sizeof(myIndication);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT_CHAR_HANDLE, myIndication, sizeof(myIndication));
}

CYBIT stop_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16* totalcount)
{
    CYBIT isSessionStopped = NO;
    
    for(uint8 i = 0; i < *totalcount; i++)
    {
        if(CHECK_BIT(p[i].Flags, DESCRIBES_SESSION_FLAG)
        && CHECK_BIT(p[i].Flags, SESSION_SUB_SESSION_RUNNING_FLAG))
        {
            TOGGLE_BIT(p[i].Flags, SESSION_SUB_SESSION_RUNNING_FLAG);
            isSessionStopped = YES;
        }
    }
    
    return isSessionStopped;
}

void stop_sub_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16* totalcount)
{
    //Stop previous sub-session
    if(!CHECK_BIT(p[*totalcount - 1].Flags, DESCRIBES_SESSION_FLAG))
    {
        TOGGLE_BIT(p[*totalcount - 1].Flags, SESSION_SUB_SESSION_RUNNING_FLAG);
    }
}


void add_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, PAM_SESSIONDESCRIPTOR_VALUE_T a, uint16* totalcount)
{
    if(totalcount > 0)
    {
        if(CHECK_BIT(a.Flags, DESCRIBES_SESSION_FLAG))
        {
            stop_session(p, totalcount);
        }
        stop_sub_session(p, totalcount);
    }
    
    if ( *totalcount < MAX_SESSIONS )
    {
       p[*totalcount] = a;
       *totalcount += 1;
    }
}

uint8 delete_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16 *totalcount, uint16 toDeleteID)
{
    uint8 deletedStatus;
    CYBIT FoundSession = NO;
    CYBIT FoundRunningSession = NO;
    if(*totalcount > 0)
    {
        for(int i = 0; i < *totalcount; i++)
        {
            if(p[i].SessionID == toDeleteID)
            {
                FoundSession = YES;
                if(!CHECK_BIT(p[i].Flags, SESSION_SUB_SESSION_RUNNING_FLAG))
                {
                    FoundRunningSession = YES;
                    deletedStatus = SUCCES;
                    DBG_PRINTF("Deleted session with \r\n SessionID: %i \r\n Sub-SessionID: %i \r\n", p[i].SessionID, p[i].SubSessionID);
                            
                    //set Deleted flag
                    TOGGLE_BIT(p[i].Flags, DELETED_SESSION_FLAG);
                    memcpy(&myDeletedSessions[i + totalDeletedSessions], &p[i], sizeof(p[i]));
                    totalDeletedSessions++;
                    send_Deleted_Sessions = YES;
                    
                        
                    for (int x = i; x < *totalcount; x++)
                    {
                        p[x] = p[x + 1];
                    }
                    *totalcount -= 1;
                }
                else 
                {
                    FoundSession = NO;
                    FoundRunningSession = YES;
                }
            }
        }
        if (!FoundRunningSession)
        {
            deletedStatus = NO_SESSION_WITH_ID;
            return deletedStatus;
        }
        if (!FoundSession)
        {
            deletedStatus = SESSION_STILL_RUNNING;
        }
    }
    else
    {
        deletedStatus = NO_SESSIONS;
    }
    return deletedStatus;
}

void delete_sub_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16 *totalcount, uint16 toDeleteID)
{
    for(int i = 0; i < *totalcount; i++)
    {
        if(p[i].SessionID == toDeleteID)
        {
            DBG_PRINTF("Deleted Sub-Session with \r\n SessionID: %i \r\n Sub-SessionID: %i \r\n", p[i].SessionID, p[i].SubSessionID);
                    
            //set Deleted flag
            TOGGLE_BIT(p[i].Flags, DELETED_SESSION_FLAG);
            //memcpy(&myDeletedSessions[totalDeletedSessions], &p[i], sizeof(p[i]));
            //totalDeletedSessions++;
            //send_Deleted_Sessions = YES;
                
            for (int x = i; x < *totalcount; x++)
            {
                p[x] = p[x + 1];
            }
            i--;
            *totalcount -= 1;
        }
    }
}

PAM_SESSIONDESCRIPTOR_VALUE_T buildNewSession(uint16 sessionID, uint16 subSessionID)
{
    PAM_SESSIONDESCRIPTOR_VALUE_T session;
    
    session.SessionID = sessionID;
    session.SubSessionID = subSessionID;
    if(subSessionID == 0)
    {
        session.Flags = 0x03;
        session.SessionStartBaseTime = 0x12345678;
        session.SessionStartTimeOffset = 0x2222;
        session.SessionEndBaseTime = 0x87654321;
        session.SessionEndTimeOffset = 0x4444;
        session.SubSessionStartBaseTime = 0x12345678;
        session.SubSessionStartTimeOffset = 0x4444;
        session.SubSessionEndBaseTime = 87654321;
        session.SubSessionEndTimeOffset = 5555;
        session.PredominantActivityType = 0xAA;
    }
    else
    {
        session.Flags = 0x02;
        session.SessionStartBaseTime = 0x789ABCDE;
        session.SessionStartTimeOffset = 0x6666;
        session.SessionEndBaseTime = 0xEDCBA987;
        session.SessionEndTimeOffset = 0x7777;
        session.SubSessionStartBaseTime = 0x789ABCDE;
        session.SubSessionStartTimeOffset = 0x8888;
        session.SubSessionEndBaseTime = 0xEDCBA987;
        session.SubSessionEndTimeOffset = 0x9999;
        session.PredominantActivityType = 0xAA;
    }
    
    return session;
}

/* [] END OF FILE */
