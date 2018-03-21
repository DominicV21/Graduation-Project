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

#ifndef PAMCONTROLPOINT_H__
#define PAMCONTROLPOINT_H__
    
PAM_SESSIONDESCRIPTOR_VALUE_T mySessions[MAX_SESSIONS];
PAM_SESSIONDESCRIPTOR_VALUE_T mySubSessions[MAX_SESSIONS];
PAM_SESSIONDESCRIPTOR_VALUE_T myDeletedSessions[MAX_SESSIONS];
uint16 sessionCount;
uint16 subsessionCount;
uint16 totalSessionCount;
uint16 totalSubSessionCount;
uint16 totalDeletedSessions;
int controlpointIndicate;

void updateControlPoint(uint8 opCode, uint8 param);
void add_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, PAM_SESSIONDESCRIPTOR_VALUE_T a, uint16* totalcount);
uint8 delete_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16 *totalcount, uint16 toDelete);
void delete_sub_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16 *totalcount, uint16 toDeleteID);
CYBIT stop_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16* totalcount);
void stop_sub_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16* totalcount);
PAM_SESSIONDESCRIPTOR_VALUE_T buildNewSession(uint16 sessionID, uint16 subSessionID);


typedef enum
{
    GENERAL_ACTIVITY_INSTANTANEOUS = 0x00,
    GENERAL_ACTIVITY_SUMMARY = 0x01,
    CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS = 0x02,
    CARDIORESPIRATORY_ACTIVITY_SUMMARY = 0x03,
    STEP_COUNTER_ACTIVITY_SUMMARY = 0x04,
    SLEEP_ACTIVITY_INSTANTANEOUS = 0x05,
    SLEEP_ACTIVITY_SUMMARY = 0x06
    
}PAM_CONTROL_POINT_DATA_REQUEST;

typedef enum
{
    SUCCES = 0x00,
    NO_SESSION_WITH_ID = 0x01,
    SESSION_STILL_RUNNING = 0x02,
    NO_SESSIONS = 0x03
    
}PAM_CONTROL_POINT_DELETE_SESSION_RESULT;
    
#endif /* PAMCONTROLPOINT_H__ */
/* [] END OF FILE */
