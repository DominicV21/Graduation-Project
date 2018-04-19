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
    
#define CP_RSP_ENQUIRE_SESSIONS_SUCCESS        (0xFC);
#define CP_RSP_ENQUIRE_SESSIONS_FAIL           (0xFF);
#define CP_RSP_ENQUIRE_SUB_SESSIONS_SUCCESS    (0xFB);
#define CP_RSP_ENQUIRE_SUB_SESSIONS_FAIL       (0xFE);
#define CP_RSP_GET_ENDED_DATA_SUCCESS          (0xFA);
#define CP_RSP_GET_ENDED_DATA_FAIL             (0xFD);
    
#define ERR_OP_CODE_NOT_SUPPORTED           0x80;
#define ERR_INVALID_SESSION_ID              0x81;
#define ERR_INVALID_SUB_SESSION_ID          0x82;
#define ERR_SESSION_STILL_RUNNING           0x83;
#define ERR_NO_DATA_AVAILABLE               0x84;
#define ERR_NO_SESSIONS_AVAILABLE           0x85;
#define ERR_INVALID_TYPE                    0x86;
#define ERR_NO_SESSION_RUNNING              0x87;
#define ERR_NOTHING_TO_STOP                 0x88;
#define ERR_UNDEFINED_VALUE                 0x89;
#define ERR_OPERATION_FAILED                0x90;


    
PAM_SESSIONDESCRIPTOR_VALUE_T mySessions[MAX_SESSIONS];
PAM_SESSIONDESCRIPTOR_VALUE_T mySubSessions[MAX_SESSIONS];
PAM_SESSIONDESCRIPTOR_VALUE_T myDeletedSessions[MAX_SESSIONS];
uint16 sessionCount;
uint16 subsessionCount;
uint16 totalSessionCount;
uint16 totalSubSessionCount;
uint16 totalDeletedSessions;
int controlpointIndicate;

void updateControlPointError(uint8 opCode, uint8 param);
void updateControlPointEnquireSucces(uint8 opCode, uint16 param);
void updateControlPointGetEndedDataSucces(uint8 opCode, uint32 param);
void add_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, PAM_SESSIONDESCRIPTOR_VALUE_T a, uint16* totalcount);
uint8 delete_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16 *totalcount, uint16 toDelete);
void delete_sub_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16 *totalcount, uint16 toDeleteID);
CYBIT stop_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16* totalcount);
void stop_sub_session(PAM_SESSIONDESCRIPTOR_VALUE_T *p, uint16* totalcount);
PAM_SESSIONDESCRIPTOR_VALUE_T buildNewSession(uint16 sessionID, uint16 subSessionID);

CYBLE_CYPACKED typedef struct
{
    uint8 opCode;
    uint8 parameter; 
    
}CYBLE_CYPACKED_ATTR PAM_CP_ERROR_RSP_T;

CYBLE_CYPACKED typedef struct
{
    uint8 opCode;
    uint16 parameter; 
    
}CYBLE_CYPACKED_ATTR PAM_CP_ENQUIRE_SUCCES_RSP_T;

CYBLE_CYPACKED typedef struct
{
    uint8 opCode;
    uint8 parameter[3]; 
    
}CYBLE_CYPACKED_ATTR PAM_CP_GETENDED_SUCCES_RSP_T;



typedef enum
{
    ENQUIRE_SESSIONS = 0x01,
    ENQUIRE_SUB_SESSIONS = 0x02,
    GET_ENDED_SESSION_DATA = 0x03,
    START_SESSION_SUB_SESSION = 0x04,
    STOP_SESSION = 0x05,
    DELETE_SESSION = 0x06,
    SET_AVERAGE_ACTIVITY_TYPE = 0x07
    
}PAM_CONTROL_POINT_WRITE_REQUEST;

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

typedef enum
{
    CURRENT_SUB_SESSION = 0x00,
    ALL_SUB_SESSIONS_IN_SESSION = 0x01
    
}PAM_CONTROL_POINT_SET_AVERAGE_ACTIVITY_TYPE_REQUEST;
    
#endif /* PAMCONTROLPOINT_H__ */
/* [] END OF FILE */
