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
#include "pamGaid.h"
#include "pamGasd.h"
#include "pamCraid.h"
#include "pamCrasd.h"
#include "pamScasd.h"
#include "pamSaid.h"
#include "pamSasd.h"
#include "pamControlPoint.h"
#include "pamCurrSess.h"

/***************************************
*        Global Variables
***************************************/
CYBLE_API_RESULT_T      apiResult;
uint8                   isButtonPressed = NO;


CY_ISR ( isr_timer_Handler )
{
    //this interupt happends every second

    
    TCPWM_ClearInterrupt(TCPWM_INTR_MASK_TC);
}

CY_ISR( Pin_SW2_Handler )
{
    DBG_PRINTF("BUTTON PRESS \r\n");

    CraiData.RelativeTimestamp++;
    UpdateCraid();
    
    
    CrasData.RelativeTimestamp++;
    UpdateCrasd();
    
    
    GaiData.RelativeTimestamp++;
    GaiData.SessionID = 0xABCD;
    UpdateGaid();
    
    GasData.RelativeTimestamp++;
    GasData.SessionID = 0xABCD;
    UpdateGasd();
    
    
    
    
    SaiData.RelativeTimestamp++;
    UpdateSaid();
    
    
    SasData.RelativeTimestamp++;
    UpdateSasd();
    
    
    ScasData.RelativeTimestamp++;
    UpdateScasd();
    
    
    Pin_SW2_ClearInterrupt();
}

void BleEventHandler(uint32 event, void* eventParam)
{
    if(0u != eventParam)
    {
        /* This dummy operation is to avoid warning about unused eventParam */
    }
    
    CYBLE_GATTS_WRITE_REQ_PARAM_T *wrReqParam;
    
    switch(event)
    {
        case CYBLE_EVT_STACK_ON:
        case CYBLE_EVT_GAP_DEVICE_DISCONNECTED:
        {
            CyBle_GappStartAdvertisement(CYBLE_ADVERTISING_FAST);
            DBG_PRINTF("Start Advertisement with addr: ");
            CYBLE_GAP_BD_ADDR_T localAddr;
            localAddr.type = 0u;
            CyBle_GetDeviceAddress(&localAddr);
            for(uint8 i = CYBLE_GAP_BD_ADDR_SIZE; i > 0u; i--)
            {
                DBG_PRINTF("%2.2x", localAddr.bdAddr[i-1]);
            }
            DBG_PRINTF("\r\n");
            DBG_PRINTF("CYBLE_EVT_GAP_DEVICE_DISCONNECTED \r\n");
            HandleLeds();
           
            generalinstantaneousdataNotify = DISABLED;
            generalsummarydataIndicate = DISABLED;
            cardiorespiratoryactivityinstantaneousdataNotify = DISABLED;
            cardiorespiratoryactivitysummarydataIndicate = DISABLED;
            stepcounteractivitysummarydataIndicate = DISABLED;
            sleepactivityinstantaneousdataNotify = DISABLED;
            sleepactivitysummarydataIndicate = DISABLED;
            controlpointIndicate = DISABLED;
            currentsessionIndicate = DISABLED;
            sessionDescriptorIndicate = DISABLED;
            break;
        }
        case CYBLE_EVT_GAP_DEVICE_CONNECTED:
        {
            /* BLE link is established */
            DBG_PRINTF("CYBLE_EVT_GAP_DEVICE_CONNECTED \r\n");
            HandleLeds();
            break;
        }
        case CYBLE_EVT_GAPP_ADVERTISEMENT_START_STOP:
        {
            HandleLeds();
            DBG_PRINTF("CYBLE_EVT_GAPP_ADVERTISEMENT_START_STOP \r\n");
            if(CyBle_GetState() == CYBLE_STATE_DISCONNECTED)
            {
                /* Advertisement event timed out, go to low power
                * mode (Stop mode) and wait for device reset
                * event to wake up the device again */
                CySysPmSetWakeupPolarity(CY_PM_STOP_WAKEUP_ACTIVE_HIGH);
                CySysPmStop();
            }
            break;
        }
        case CYBLE_EVT_GATTS_HANDLE_VALUE_CNF:
        {
            DBG_PRINTF("indication confirmation \r\n");
            break;
        }
        case CYBLE_EVT_GAP_PASSKEY_DISPLAY_REQUEST:
        {
            DBG_PRINTF("\r\n");
            DBG_PRINTF("CYBLE_EVT_GAP_PASSKEY_DISPLAY_REQUEST. Passkey is: %lu.\r\n",
                *(uint32 *)eventParam);
            break;
        }
        case CYBLE_EVT_GAP_ENCRYPT_CHANGE:
        {
            DBG_PRINTF("CYBLE_EVT_GAP_ENCRYPT_CHANGE: %d \r\n", *(uint8 *)eventParam);
            break;
        }
        case CYBLE_EVT_GATT_CONNECT_IND:
        {
            DBG_PRINTF("CYBLE_EVT_GATT_CONNECT_IND: attId %x, bdHandle %x \r\n", 
                ((CYBLE_CONN_HANDLE_T *)eventParam)->attId, ((CYBLE_CONN_HANDLE_T *)eventParam)->bdHandle);
            break;
        }
        case CYBLE_EVT_GAP_AUTH_COMPLETE:
        {
            DBG_PRINTF("CYBLE_EVT_GAP_AUTH_COMPLETE: security:%x, bonding:%x, ekeySize:%x, authErr %x \r\n",
                        ((CYBLE_GAP_AUTH_INFO_T *)eventParam)->security,
                        ((CYBLE_GAP_AUTH_INFO_T *)eventParam)->bonding, 
                        ((CYBLE_GAP_AUTH_INFO_T *)eventParam)->ekeySize, 
                        ((CYBLE_GAP_AUTH_INFO_T *)eventParam)->authErr);
            break;
        }
        case CYBLE_EVT_GATTS_WRITE_REQ:
        {
            wrReqParam = (CYBLE_GATTS_WRITE_REQ_PARAM_T *) eventParam;
            //CCCD Region
            {
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_INSTANTANEOUS_DATA_GENERALACTIVITYINSTANTANEOUSDATACCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    generalinstantaneousdataNotify = wrReqParam->handleValPair.value.val[0] & 0x01;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop GENERAL ACTIVITY INSTANTANEOUS DATA Notifications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_SUMMARY_DATA_GENERALACTIVITYSUMMARYDATACCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    generalsummarydataIndicate = wrReqParam->handleValPair.value.val[0] & 0x02;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop GENERAL ACTIVITY SUMMARY DATA Indications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS_DATA_CARDIORESPIRATORYAVTIVITYINSTANTANEOUSDATACCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    cardiorespiratoryactivityinstantaneousdataNotify = wrReqParam->handleValPair.value.val[0] & 0x01;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop CARDIORESPIRATORY ACTIVITY INSTANTANEOUS DATA Notifications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_CARDIORESPIRATORY_ACTIVITY_SUMMARY_DATA_CARDIORESPIRATORYACTIVITYSUMMARYDATACCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    cardiorespiratoryactivitysummarydataIndicate = wrReqParam->handleValPair.value.val[0] & 0x02;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop CARDIORESPIRATORY ACTIVITY SUMMARY DATA Indications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_STEP_COUNTER_ACTIVITY_SUMMARY_DATA_STEPCOUNTERACTIVITYSUMMARYDATACCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    stepcounteractivitysummarydataIndicate = wrReqParam->handleValPair.value.val[0] & 0x02;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop STEP COUNTER ACTIVITY SUMMARY DATA Indications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_SLEEP_ACTIVITY_INSTANTANEOUS_DATA_SLEEPACTIVITYINSTANTANEOUSDATACCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    sleepactivityinstantaneousdataNotify = wrReqParam->handleValPair.value.val[0] & 0x01;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop SLEEP ACTIVITY INSTANTANEOUS DATA Notifications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_SLEEP_ACTIVITY_SUMMARY_DATA_SLEEPACTIVITYSUMMARYDATACCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    sleepactivitysummarydataIndicate = wrReqParam->handleValPair.value.val[0] & 0x02;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop SLEEP ACTIVITY SUMMARY DATA Indications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT_PHYSICALACTIVITYMONITORCONTROLPOINTCCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    controlpointIndicate = wrReqParam->handleValPair.value.val[0] & 0x02;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop CONTROL POINT Indications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_CURRENT_SESSION_CURRENTSESSIONCCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    currentsessionIndicate = wrReqParam->handleValPair.value.val[0] & 0x02;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop CURRENT SESSION Indications \r\n");
                }
                
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_SESSION_DESCRIPTOR_SESSIONDESCRIPTORCCCD_DESC_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    sessionDescriptorIndicate = wrReqParam->handleValPair.value.val[0] & 0x02;
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                    DBG_PRINTF("Start/stop SESSION DESCRIPTOR Indications \r\n");
                }
            }
            //User control point write handler region
            {
                if(wrReqParam->handleValPair.attrHandle == CYBLE_PHYSICAL_ACTIVITY_MONITOR_PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT_CHAR_HANDLE)
                {
                    CyBle_GattsWriteAttributeValue(&wrReqParam->handleValPair, 0, &cyBle_connHandle, CYBLE_GATT_DB_PEER_INITIATED);
                    CYBLE_GATTS_ERR_PARAM_T err;
                    err.attrHandle = wrReqParam->handleValPair.attrHandle;
                    err.opcode = CYBLE_GATT_WRITE_REQ;
                    
                    DBG_PRINTF("Control point write request with: %i \r\n", wrReqParam->handleValPair.value.val[0]);
                    
                    switch(wrReqParam->handleValPair.value.val[0])
                    {
                        case ENQUIRE_SESSIONS:
                        {
                            DBG_PRINTF("Enquire Sessions \r\n");
                            if(!controlpointIndicate || !sessionDescriptorIndicate)
                            {
                                DBG_PRINTF("Client not subscribed to control point or session descriptor indications \r\n");
                                err.errorCode = CYBLE_GATT_ERR_CCCD_IMPROPERLY_CONFIGURED;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                                
                            }
                            else if(totalSessionCount > 0)
                            {
                                Enquire_Sessions_Requested = YES;
                            }
                            else
                            {
                                DBG_PRINTF("There are no sessions in the database \r\n");
                                err.errorCode = ERR_NO_SESSIONS_AVAILABLE;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            break;
                        }
                        case ENQUIRE_SUB_SESSIONS:
                        {
                            uint16 subSessionIDBuffer = 0;
                            if(wrReqParam->handleValPair.value.val[2] == 0) { subSessionIDBuffer = wrReqParam->handleValPair.value.val[1]; }
                                else if(wrReqParam->handleValPair.value.val[1] == 0) { subSessionIDBuffer = wrReqParam->handleValPair.value.val[2] * 255; }
                                else { subSessionIDBuffer = wrReqParam->handleValPair.value.val[2] * 256 + wrReqParam->handleValPair.value.val[1]; }
                                
                            DBG_PRINTF("Enquire Sub-Sessions with with session ID: %i \r\n", subSessionIDBuffer);
                            
                            if(!controlpointIndicate || !sessionDescriptorIndicate)
                            {
                                DBG_PRINTF("Client not subscribed to control point or session descriptor indications \r\n");
                                err.errorCode = CYBLE_GATT_ERR_CCCD_IMPROPERLY_CONFIGURED;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            else if(totalSessionCount == 0)
                            {
                                DBG_PRINTF("There are no sessions in the database \r\n");
                                err.errorCode = ERR_NO_SESSIONS_AVAILABLE;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            else 
                            {
                                Enquire_Sub_Sessions_Requested_For_ID = subSessionIDBuffer;
                                
                                CYBIT foundSubSession = NO;
                                for(int i = 0; i < totalSubSessionCount; i++)
                                {
                                    if(Enquire_Sub_Sessions_Requested_For_ID == mySubSessions[i].SessionID)
                                    {
                                        foundSubSession = YES;
                                    }
                                }
                                if(!foundSubSession)
                                {
                                    Enquire_Sub_Sessions_Requested_For_ID = 0;
                                    DBG_PRINTF("There are no Sessions with this ID thus no sub sessions can be enquired \r\n");
                                    err.errorCode = ERR_INVALID_SESSION_ID;
                                    (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                    return;
                                }
                            }
                            break;
                        }
                        case GET_ENDED_SESSION_DATA:
                        {
                        if(!controlpointIndicate)
                        {
                            DBG_PRINTF("Client not subscribed to control point indications \r\n");
                            err.errorCode = CYBLE_GATT_ERR_CCCD_IMPROPERLY_CONFIGURED;
                            (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                            return;
                        }
                        else if(totalSessionCount == 0)
                        {
                            DBG_PRINTF("There are no sessions in the database \r\n");
                            err.errorCode = ERR_NO_SESSIONS_AVAILABLE;
                            (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                        }
                        else if(wrReqParam->handleValPair.value.val[5] > 6)
                        {
                            DBG_PRINTF("OPCODE not supported \r\n");
                            err.errorCode = CYBLE_GATT_ERR_OP_CODE_NOT_SUPPORTED;
                            CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                            return;
                        }
                        else
                        {
                            CYBIT foundSession = NO;
                            CYBIT foundRunningSession = NO;
                            if(wrReqParam->handleValPair.value.val[2] == 0) { Get_Ended_Data_SessionID = wrReqParam->handleValPair.value.val[1]; }
                            else if(wrReqParam->handleValPair.value.val[1] == 0) { Get_Ended_Data_SessionID = wrReqParam->handleValPair.value.val[2] * 255; }
                            else { Get_Ended_Data_SessionID = wrReqParam->handleValPair.value.val[2] * 256 + wrReqParam->handleValPair.value.val[1]; }
                            
                            if(wrReqParam->handleValPair.value.val[4] == 0) { Get_Ended_Data_Sub_SessionID = wrReqParam->handleValPair.value.val[3]; }
                            else if(wrReqParam->handleValPair.value.val[3] == 0) { Get_Ended_Data_Sub_SessionID = wrReqParam->handleValPair.value.val[4] * 255; }
                            else { Get_Ended_Data_Sub_SessionID = wrReqParam->handleValPair.value.val[4] * 256 + wrReqParam->handleValPair.value.val[3]; }
                            
                            Get_Ended_Data_Value = wrReqParam->handleValPair.value.val[5];
                            
                            for(int i = 0; i < totalSessionCount; i++)
                            {
                                if(Get_Ended_Data_SessionID == mySessions[i].SessionID)
                                {
                                    foundSession = YES;
                                    if(CHECK_BIT(mySessions[i].Flags, SESSION_SUB_SESSION_RUNNING_FLAG))
                                    {
                                        foundSession = NO;
                                        foundRunningSession = YES;
                                    }
                                }
                            }
                            
                            if(foundRunningSession)
                            {
                                DBG_PRINTF("This session is still running: %i \r\n", Get_Ended_Data_SessionID);
                                Get_Ended_Data_SessionID = 0;
                                Get_Ended_Data_Sub_SessionID = 0;
                                err.errorCode = ERR_SESSION_STILL_RUNNING;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            else if(!foundSession)
                            {
                                DBG_PRINTF("There are no Sessions with this ID: %i \r\n", Get_Ended_Data_SessionID);
                                Get_Ended_Data_SessionID = 0;
                                Get_Ended_Data_Sub_SessionID = 0;
                                err.errorCode = ERR_INVALID_SESSION_ID;
                                CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            
                            CYBIT foundSubSession = NO;
                            for(int i = 0; i < totalSubSessionCount; i++)
                            {
                                if(Get_Ended_Data_Sub_SessionID == mySubSessions[i].SubSessionID
                                && Get_Ended_Data_SessionID == mySubSessions[i].SessionID)
                                {
                                    foundSubSession = YES;
                                }
                            }
                            
                            if(Get_Ended_Data_Sub_SessionID == 0xFFFF)
                            {
                                foundSubSession = YES;
                            }
                            
                            if(!foundSubSession)
                            {
                                DBG_PRINTF("There are no Sub-Sessions with this ID: %i \r\n", Get_Ended_Data_Sub_SessionID);
                                Get_Ended_Data_SessionID = 0;
                                Get_Ended_Data_Sub_SessionID = 0;
                                err.errorCode = ERR_INVALID_SUB_SESSION_ID;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            else
                            {
                                DBG_PRINTF("Get Ended Session Data\r\n");
                                DBG_PRINTF("SessionID: %i \r\n", Get_Ended_Data_SessionID);
                                DBG_PRINTF("Sub-SessionID: %i \r\n", Get_Ended_Data_Sub_SessionID);
                                DBG_PRINTF("Characteristic %i (0 = GAI, 1 = GAS, 2 = CRAI, 3 = CRAS, 4 = SCAS, 5 = SAI, 6 = SAS) \r\n", wrReqParam->handleValPair.value.val[5]); 
                            }
                        }
                        break;
                    }
                        case START_SESSION_SUB_SESSION:
                        {
                        if(wrReqParam->handleValPair.value.val[1] == 0x00)
                        {
                            sessionCount++;
                            add_session(mySessions, buildNewSession(sessionCount, 0), &totalSessionCount);
                            subsessionCount = 1;
                            add_session(mySubSessions, buildNewSession(totalSessionCount, subsessionCount), &totalSubSessionCount);
                            UpdateCurrSession(&mySubSessions[totalSubSessionCount - 1]);
                            DBG_PRINTF("Start Session with session ID: %i\r\n Total sessions: %i \r\n Total SUB sessions: %i \r\n", sessionCount, totalSessionCount, totalSubSessionCount);
                        }
                        else if (wrReqParam->handleValPair.value.val[1] == 0x01)
                        {
                            if(totalSessionCount > 0)
                            {
                                subsessionCount++;
                                add_session(mySubSessions, buildNewSession(totalSessionCount, subsessionCount), &totalSubSessionCount);
                                UpdateCurrSession(&mySubSessions[totalSubSessionCount - 1]);
                                DBG_PRINTF("Start Sub-Session with sub session ID: %i into sessionID: %i\r\n", subsessionCount, sessionCount);
                                DBG_PRINTF("Total sessions: %i \r\n Total SUB sessions: %i \r\n", totalSessionCount, totalSubSessionCount);
                            }
                            else
                            {
                                DBG_PRINTF("Cannot start new Sub-Session need to start a session first\r\n");
                                err.errorCode = ERR_NO_SESSION_RUNNING;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                        }
                        else
                        {
                            DBG_PRINTF("Unknown Command\r\n Specify '0400' to start a session and '0401' to start sub-session \r\n");
                            err.errorCode = ERR_UNDEFINED_VALUE;
                            (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                            return;
                        }
                        break;
                    }
                        case STOP_SESSION:
                        {
                        DBG_PRINTF("Stop Session\r\n");
                        if (stop_session(mySessions, &totalSessionCount))
                        {
                            stop_sub_session(mySubSessions, &totalSubSessionCount);
                            UpdateCurrSession(&mySubSessions[totalSubSessionCount - 1]);
                        }
                        else
                        {
                            DBG_PRINTF("Can't stop session, No running session\r\n");
                            err.errorCode = ERR_NOTHING_TO_STOP;
                            (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                            return;
                        }
                        break;
                    }
                        case DELETE_SESSION:
                        {
                        uint16 sessionIDBuffer = 0;
                        if(wrReqParam->handleValPair.value.val[2] == 0) { sessionIDBuffer = wrReqParam->handleValPair.value.val[1]; }
                            else if(wrReqParam->handleValPair.value.val[1] == 0) { sessionIDBuffer = wrReqParam->handleValPair.value.val[2] * 255; }
                            else { sessionIDBuffer = wrReqParam->handleValPair.value.val[2] * 256 + wrReqParam->handleValPair.value.val[1]; }
                        
                        DBG_PRINTF("Delete Session with parameter: %i requested\r\n", sessionIDBuffer);
                        uint16 toDeleteID = sessionIDBuffer;
                        uint8 deleteStatus = delete_session(mySessions, &totalSessionCount, toDeleteID);
                        switch(deleteStatus)
                        {
                            case(SUCCES):
                            {
                                DBG_PRINTF("Deleted Session and sub sessions \r\n");
                                delete_sub_session(mySubSessions, &totalSubSessionCount, toDeleteID);
                                break;
                            }
                            case NO_SESSION_WITH_ID:
                            {
                                DBG_PRINTF("There are no sessions with: \r\n SessionID: %i \r\n", toDeleteID);
                                err.errorCode = ERR_INVALID_SESSION_ID;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            case SESSION_STILL_RUNNING:
                            {
                                DBG_PRINTF("This session is still running: \r\n SessionID: %i \r\n", toDeleteID);
                                err.errorCode = ERR_SESSION_STILL_RUNNING;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            case NO_SESSIONS:
                            {
                                DBG_PRINTF("There are no sessions\r\n");
                                err.errorCode = ERR_NO_SESSIONS_AVAILABLE;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                            }
                        }
                        break;
                    }
                        case SET_AVERAGE_ACTIVITY_TYPE:
                        {
                            DBG_PRINTF("Set Average Activity Type Requested\r\n");
                            if(!controlpointIndicate)
                            {
                                DBG_PRINTF("Client not subscribed to control point indications \r\n");
                                err.errorCode = CYBLE_GATT_ERR_CCCD_IMPROPERLY_CONFIGURED;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                return;
                            }
                            else if(totalSessionCount == 0)
                            {
                                DBG_PRINTF("There are no sessions in the database \r\n");
                                err.errorCode = ERR_NO_SESSIONS_AVAILABLE;
                                (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                            }
                            
                            int scope = wrReqParam->handleValPair.value.val[1];
                            uint8 UserDefinedActivityType = wrReqParam->handleValPair.value.val[2];
                            
                            switch(scope)
                            {
                                case CURRENT_SUB_SESSION:
                                {
                                    UserDefinedActivityTypeAllSubSessions[CurrSession.SessionID][CurrSession.SubSessionID] = UserDefinedActivityType;
                                    GasData.AverageActivityType = ((UserDefinedActivityTypeAllSubSessions[CurrSession.SessionID][CurrSession.SubSessionID] & 0xFF) << 8) | (GasData.AverageActivityType & 0xFF);
                                    break;
                                }
                                case ALL_SUB_SESSIONS_IN_SESSION:
                                {
                                    for(int i = 0; i < totalSubSessionCount; i++)
                                    {
                                        if(mySubSessions[i].SessionID == CurrSession.SessionID)
                                        {
                                            UserDefinedActivityTypeAllSubSessions[CurrSession.SessionID][mySubSessions[i].SubSessionID] = UserDefinedActivityType;
                                            DBG_PRINTF("Value Set for: \r\n Sess ID: %i \r\n Sub Ses ID: %i \r\n Value(HEX): %x \r\n", CurrSession.SessionID, mySubSessions[i].SubSessionID, UserDefinedActivityType);
                                        }
                                    }  
                                    break;
                                }
                                default:
                                {
                                    DBG_PRINTF("Invalid Parameter\r\n");
                                    err.errorCode = ERR_UNDEFINED_VALUE;
                                    (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                                    return;
                                }
                            }
                            break;
                        }
                        default:
                        {
                            DBG_PRINTF("Unknown Command\r\n");
                            err.errorCode = CYBLE_GATT_ERR_OP_CODE_NOT_SUPPORTED;
                            (void)CyBle_GattsErrorRsp(cyBle_connHandle, &err);
                            return;
                        }
                    }
                    CyBle_GattsWriteRsp(cyBle_connHandle);
                }
            }
            
        case CYBLE_EVT_GATTS_READ_CHAR_VAL_ACCESS_REQ:
            //DBG_PRINTF("READ REQUEST\r\n");
            break;
        
        default:
            break;
        }
    }
}

int main(void)
{
    CyGlobalIntEnable; /* Enable global interrupts. */
    
    if(apiResult != CYBLE_ERROR_OK)
    {
        /* BLE stack initialization failed Check configuration */
        CYASSERT(0);
    }

    /* Place your initialization/startup code here (e.g. MyInst_Start()) */

    Pin_SW2_Int_StartEx( Pin_SW2_Handler );
    TCPWM_Start();
    isr_timer_StartEx( isr_timer_Handler );
    
    CYBIT procedureFailed = FALSE;
    
    apiResult = CyBle_Start(BleEventHandler);
    
    InitializePhisicalActivityMonitor();
    
    for(;;)
    {
        /* Place your application code here. */
        CyBle_ProcessEvents();
        
        if(CyBle_GetState() == CYBLE_STATE_CONNECTED)
        {
        #if (DEBUG_UART_ENABLED == ENABLED)
            if((cyBle_pendingFlashWrite != 0u) &&
               ((UART_DEB_SpiUartGetTxBufferSize() + UART_DEB_GET_TX_FIFO_SR_VALID) == 0u))
        #else
            if(cyBle_pendingFlashWrite != 0u)
        #endif /* (DEBUG_UART_ENABLED == ENABLED) */
            {
                /* Store bounding data to flash only when all debug information has been sent */
                apiResult = CyBle_StoreBondingData(0u);
            }
            else
            {
                /* nothing else */ 
            }
        }
        
        if(Enquire_Sessions_Requested)
        {
            for(int i = 0; i < totalSessionCount; i++)
            {
                procedureFailed = UpdateSessionDescriptor(mySessions[i]);
            }
            if(procedureFailed) { updateControlPointEnquireSucces(0xFF, 0xFF); }
            else                { updateControlPointEnquireSucces(0xFC, totalSessionCount); }
            
            Enquire_Sessions_Requested = NO;
            procedureFailed = FALSE;
            
        }
        
        if(Enquire_Sub_Sessions_Requested_For_ID > 0)
        {
            uint16 count = 0;
            for(int i = 0; i < totalSubSessionCount; i++)
            {
                if(Enquire_Sub_Sessions_Requested_For_ID == mySubSessions[i].SessionID)
                {
                    procedureFailed = UpdateSessionDescriptor(mySubSessions[i]);
                    count++;
                }
            }
            
            if(procedureFailed) { updateControlPointEnquireSucces(0xFE, 0xFF); }
            else                { updateControlPointEnquireSucces(0xFB, count); }
            
            Enquire_Sub_Sessions_Requested_For_ID = NO;
            procedureFailed = FALSE;
        }
        
        if(send_Deleted_Sessions)
        {
            for(int i = 0; i < totalDeletedSessions; i++)
            {
                UpdateSessionDescriptor(myDeletedSessions[i]);
                myDeletedSessions[i] = myDeletedSessions[i + 1];
                totalDeletedSessions -= 1;
            }
            send_Deleted_Sessions = NO;
        }
        
        if(Get_Ended_Data_SessionID > 0)
        {
            uint32 count = 0;
            if(Get_Ended_Data_Sub_SessionID == 0xFFFF)
            {
                for(int i = 0; i < totalSubSessionCount; i++)
                {
                    if(Get_Ended_Data_SessionID == mySubSessions[i].SessionID)
                    {
                        uint16 subSessionID = mySubSessions[i].SubSessionID;
                        count += SimulateValues(&Get_Ended_Data_SessionID, &subSessionID);
                        
                    }
                }
            }
            else
            {
                count += SimulateValues(&Get_Ended_Data_SessionID, &Get_Ended_Data_Sub_SessionID);
            }
            
            if(procedureFailed) { updateControlPointEnquireSucces(0xFD, 0xFF); }
            else                { updateControlPointGetEndedDataSucces(0xFA, count); }
            
            
            Get_Ended_Data_SessionID = 0;
            Get_Ended_Data_Sub_SessionID = 0;
        }
        
        CyBle_EnterLPM(CYBLE_BLESS_DEEPSLEEP);
    }
}

uint32 SimulateValues(uint16* SessionID, uint16* SubSessionID)
{
    //GaiData
    {
        GaiData.SessionID = *SessionID;
        GaiData.SubSessionID = *SubSessionID;
        GaiData.ActivityCountPerMinute = *SessionID * 10 + *SubSessionID;
        GaiData.ActivityLevel = *SessionID * 10 + *SubSessionID;
        GaiData.ActivityType = *SessionID * 10 + *SubSessionID;
        GaiData.Elevation[0] = *SessionID * 10 + *SubSessionID;
        GaiData.Elevation[1] = *SessionID * 10 + *SubSessionID;
        GaiData.Elevation[2] = *SessionID * 10 + *SubSessionID;
        GaiData.FatBurnedPerHour = *SessionID * 10 + *SubSessionID;
        GaiData.IntensityEnergyExpenditurePerHour = *SessionID * 10 + *SubSessionID;
        GaiData.MetabolicEquivalent = *SessionID * 10 + *SubSessionID;
        GaiData.MotionCadence = *SessionID * 10 + *SubSessionID;
        GaiData.NormalWalkingEnergyExpenditurePerHour = *SessionID * 10 + *SubSessionID;
        GaiData.RelativeTimestamp = *SessionID * 10 + *SubSessionID;
        GaiData.SequenceNumber = *SessionID * 10 + *SubSessionID;
        GaiData.Speed = *SessionID * 10 + *SubSessionID;
        GaiData.TotalEnergyExpenditurePerHour = *SessionID * 10 + *SubSessionID;
    }
    //CraiData
    {
        CraiData.SessionID = *SessionID;
        CraiData.SubSessionID = *SubSessionID;
        CraiData.HeartRate = *SessionID * 10 + *SubSessionID;
        CraiData.HeartRateVariability = *SessionID * 10 + *SubSessionID;
        CraiData.PulseInterBeatInterval = *SessionID * 10 + *SubSessionID;
        CraiData.RelativeTimestamp = *SessionID * 10 + *SubSessionID;
        CraiData.RespirationRate = *SessionID * 10 + *SubSessionID;
        CraiData.RestingHeartRate = *SessionID * 10 + *SubSessionID;
        CraiData.RestingRespirationRate = *SessionID * 10 + *SubSessionID;
        CraiData.SequenceNumber = *SessionID * 10 + *SubSessionID;
        CraiData.VO2Max = *SessionID * 10 + *SubSessionID;
    }
    //SaiData
    {
        SaiData.SessionID = *SessionID;
        SaiData.SubSessionID = *SubSessionID;
        SaiData.IRLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SaiData.IRLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SaiData.IRLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SaiData.RelativeTimestamp = *SessionID * 10 + *SubSessionID;
        SaiData.SequenceNumber = *SessionID * 10 + *SubSessionID;
        SaiData.SleepingHeartRate = *SessionID * 10 + *SubSessionID;
        SaiData.SleepStage = *SessionID * 10 + *SubSessionID;
        SaiData.UVLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SaiData.UVLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SaiData.UVLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SaiData.VisibleLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SaiData.VisibleLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SaiData.VisibleLightLevel[2] = *SessionID * 10 + *SubSessionID;
    }
    //GasData
    {
        GasData.SessionID = *SessionID;
        GasData.SubSessionID = *SubSessionID;
        GasData.ActivityCount = *SessionID * 10 + *SubSessionID;
        GasData.AverageActivityLevel = *SessionID * 10 + *SubSessionID;
        GasData.AverageActivityType = ((UserDefinedActivityTypeAllSubSessions[*SessionID][*SubSessionID] & 0xFF) << 8) | (GasData.AverageActivityType & 0xFF);
        GasData.AverageMetabolicEquivalent = *SessionID * 10 + *SubSessionID;
        GasData.AverageMotionCadence = *SessionID * 10 + *SubSessionID;
        GasData.AverageSpeed = *SessionID * 10 + *SubSessionID;
        GasData.Distance[0] = *SessionID * 10 + *SubSessionID;
        GasData.Distance[1] = *SessionID * 10 + *SubSessionID;
        GasData.Distance[2] = *SessionID * 10 + *SubSessionID;
        GasData.DurationOfIntensityWalkingEpisodes[0] = *SessionID * 10 + *SubSessionID;
        GasData.DurationOfIntensityWalkingEpisodes[1] = *SessionID * 10 + *SubSessionID;
        GasData.DurationOfIntensityWalkingEpisodes[2] = *SessionID * 10 + *SubSessionID;
        GasData.DurationOfNormalWalkingEpisodes[0] = *SessionID * 10 + *SubSessionID;
        GasData.DurationOfNormalWalkingEpisodes[1] = *SessionID * 10 + *SubSessionID;
        GasData.DurationOfNormalWalkingEpisodes[2] = *SessionID * 10 + *SubSessionID;
        GasData.FatBurned = *SessionID * 10 + *SubSessionID;
        GasData.Floors = *SessionID * 10 + *SubSessionID;
        GasData.IntensityEnergyExpenditure = *SessionID * 10 + *SubSessionID;
        GasData.MaximumActivityLevel = *SessionID * 10 + *SubSessionID;
        GasData.MaximumMetabolicEquivalent = *SessionID * 10 + *SubSessionID;
        GasData.MaximumMotionCadence = *SessionID * 10 + *SubSessionID;
        GasData.MaximumSpeed = *SessionID * 10 + *SubSessionID;
        GasData.MinimumActivityLevel = *SessionID * 10 + *SubSessionID;
        GasData.MinimumMetabolicEquivalent = *SessionID * 10 + *SubSessionID;
        GasData.MinimumMotionCadence = *SessionID * 10 + *SubSessionID;
        GasData.MinimumSpeed = *SessionID * 10 + *SubSessionID;
        GasData.NegativeElevationGain[0] = *SessionID * 10 + *SubSessionID;
        GasData.NegativeElevationGain[1] = *SessionID * 10 + *SubSessionID;
        GasData.NegativeElevationGain[2] = *SessionID * 10 + *SubSessionID;
        GasData.NormalWalkingEnergyExpenditure = *SessionID * 10 + *SubSessionID;
        GasData.PositiveElevationGain[0] = *SessionID * 10 + *SubSessionID;
        GasData.PositiveElevationGain[1] = *SessionID * 10 + *SubSessionID;
        GasData.PositiveElevationGain[2] = *SessionID * 10 + *SubSessionID;
        GasData.RelativeTimestamp = *SessionID * 10 + *SubSessionID;
        GasData.SequenceNumber = *SessionID * 10 + *SubSessionID;
        GasData.TotalEnergyExpenditure = *SessionID * 10 + *SubSessionID;
        GasData.WornDuration[0] = *SessionID * 10 + *SubSessionID;
        GasData.WornDuration[1] = *SessionID * 10 + *SubSessionID;
        GasData.WornDuration[2] = *SessionID * 10 + *SubSessionID;
    }
    //CrasData
    {
        CrasData.SessionID = *SessionID;
        CrasData.SubSessionID = *SubSessionID;
        CrasData.AverageHeartRate = *SessionID * 10 + *SubSessionID;
        CrasData.AverageHeartRateVariability = *SessionID * 10 + *SubSessionID;
        CrasData.AveragePulseInterbeatInterval = *SessionID * 10 + *SubSessionID;
        CrasData.AverageRespirationRate = *SessionID * 10 + *SubSessionID;
        CrasData.AverageRestingHeartRate = *SessionID * 10 + *SubSessionID;
        CrasData.AverageRestingRespirationRate = *SessionID * 10 + *SubSessionID;
        CrasData.AverageVO2Max = *SessionID * 10 + *SubSessionID;
        CrasData.MaximumHeartRate = *SessionID * 10 + *SubSessionID;
        CrasData.MaximumHeartRateVariability = *SessionID * 10 + *SubSessionID;
        CrasData.MaximumPulseInterbeatInterval = *SessionID * 10 + *SubSessionID;
        CrasData.MaximumRespirationRate = *SessionID * 10 + *SubSessionID;
        CrasData.MaximumRestingHeartRate = *SessionID * 10 + *SubSessionID;
        CrasData.MaximumRestingRespirationRate = *SessionID * 10 + *SubSessionID;
        CrasData.MaximumVO2Max = *SessionID * 10 + *SubSessionID;
        CrasData.MinimumHeartRate = *SessionID * 10 + *SubSessionID;
        CrasData.MinimumHeartRateVariability = *SessionID * 10 + *SubSessionID;
        CrasData.MinimumPulseInterbeatInterval = *SessionID * 10 + *SubSessionID;
        CrasData.MinimumRespirationRate = *SessionID * 10 + *SubSessionID;
        CrasData.MinimumRestingHeartRate = *SessionID * 10 + *SubSessionID;
        CrasData.MinimumRestingRespirationRate = *SessionID * 10 + *SubSessionID;
        CrasData.MinimumVO2Max = *SessionID * 10 + *SubSessionID;
        CrasData.RelativeTimestamp = *SessionID * 10 + *SubSessionID;
        CrasData.SequenceNumber = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone1[0] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone1[1] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone1[2] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone2[0] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone2[1] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone2[2] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone3[0] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone3[1] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone3[2] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone4[0] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone4[1] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone4[2] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone5[0] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone5[1] = *SessionID * 10 + *SubSessionID;
        CrasData.TimeInHeartRateZone5[2] = *SessionID * 10 + *SubSessionID;
        CrasData.WornDuration[0] = *SessionID * 10 + *SubSessionID;
        CrasData.WornDuration[1] = *SessionID * 10 + *SubSessionID;
        CrasData.WornDuration[2] = *SessionID * 10 + *SubSessionID;
    }
    //ScasData
    {
        ScasData.SessionID = *SessionID;
        ScasData.SubSessionID = *SubSessionID;
        ScasData.Distance[0] = *SessionID * 10 + *SubSessionID;
        ScasData.Distance[1] = *SessionID * 10 + *SubSessionID;
        ScasData.Distance[2] = *SessionID * 10 + *SubSessionID;
        ScasData.FloorSteps[0] = *SessionID * 10 + *SubSessionID;
        ScasData.FloorSteps[1] = *SessionID * 10 + *SubSessionID;
        ScasData.FloorSteps[2] = *SessionID * 10 + *SubSessionID;
        ScasData.IntensitySteps[0] = *SessionID * 10 + *SubSessionID;
        ScasData.IntensitySteps[1] = *SessionID * 10 + *SubSessionID;
        ScasData.IntensitySteps[2] = *SessionID * 10 + *SubSessionID;
        ScasData.NormalWalkingSteps[0] = *SessionID * 10 + *SubSessionID;
        ScasData.NormalWalkingSteps[1] = *SessionID * 10 + *SubSessionID;
        ScasData.NormalWalkingSteps[2] = *SessionID * 10 + *SubSessionID;
        ScasData.RelativeTimestamp = *SessionID * 10 + *SubSessionID;
        ScasData.SequenceNumber = *SessionID * 10 + *SubSessionID;
        ScasData.WornDuration[0] = *SessionID * 10 + *SubSessionID;
        ScasData.WornDuration[1] = *SessionID * 10 + *SubSessionID;
        ScasData.WornDuration[2] = *SessionID * 10 + *SubSessionID;
    }
    //SasData
    {
        SasData.AverageIRLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.AverageIRLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.AverageIRLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.AverageSleepingHeartRate = *SessionID * 10 + *SubSessionID;
        SasData.AverageUVLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.AverageUVLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.AverageUVLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.AverageVisibleLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.AverageVisibleLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.AverageVisibleLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumIRLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumIRLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumIRLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumUVLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumUVLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumUVLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumVisibleLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumVisibleLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.MaximumVisibleLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumIRLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumIRLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumIRLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumUVLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumUVLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumUVLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumVisibleLightLevel[0] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumVisibleLightLevel[1] = *SessionID * 10 + *SubSessionID;
        SasData.MinimumVisibleLightLevel[2] = *SessionID * 10 + *SubSessionID;
        SasData.NumberOfAwakenings = *SessionID * 10 + *SubSessionID;
        SasData.NumberOfTossNturnEvents = *SessionID * 10 + *SubSessionID;
        SasData.RelativeTimestamp = *SessionID * 10 + *SubSessionID;
        SasData.SequenceNumber = *SessionID * 10 + *SubSessionID;
        SasData.SessionID = *SessionID * 10 + *SubSessionID;
        SasData.SleepEfficiency = *SessionID * 10 + *SubSessionID;
        SasData.SleepLatency = *SessionID * 10 + *SubSessionID;
        SasData.SnoozeTime = *SessionID * 10 + *SubSessionID;
        SasData.SubSessionID = *SessionID * 10 + *SubSessionID;
        SasData.TimeOfAwakeningAfterAlarm[0] = *SessionID * 10 + *SubSessionID;
        SasData.TimeOfAwakeningAfterAlarm[1] = *SessionID * 10 + *SubSessionID;
        SasData.TimeOfAwakeningAfterAlarm[2] = *SessionID * 10 + *SubSessionID;
        SasData.TotalBedTime[0] = *SessionID * 10 + *SubSessionID;
        SasData.TotalBedTime[1] = *SessionID * 10 + *SubSessionID;
        SasData.TotalBedTime[2] = *SessionID * 10 + *SubSessionID;
        SasData.TotalSleepTime[0] = *SessionID * 10 + *SubSessionID;
        SasData.TotalSleepTime[1] = *SessionID * 10 + *SubSessionID;
        SasData.TotalSleepTime[2] = *SessionID * 10 + *SubSessionID;
        SasData.TotalWakeTime[0] = *SessionID * 10 + *SubSessionID;
        SasData.TotalWakeTime[1] = *SessionID * 10 + *SubSessionID;
        SasData.TotalWakeTime[2] = *SessionID * 10 + *SubSessionID;
        SasData.WornDuration[0] = *SessionID * 10 + *SubSessionID;
        SasData.WornDuration[1] = *SessionID * 10 + *SubSessionID;
        SasData.WornDuration[2] = *SessionID * 10 + *SubSessionID;
    }
    
    uint32 count = 0;
    
    switch(Get_Ended_Data_Value)
    {
        case GENERAL_ACTIVITY_INSTANTANEOUS:
        {
            UpdateGaid();
            count++;
            break;
        }
        case GENERAL_ACTIVITY_SUMMARY:
        {
            UpdateGasd();
            count++;
            break;
        }
        case CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS:
        {
            UpdateCraid();
            count++;
            break;
        }
        case CARDIORESPIRATORY_ACTIVITY_SUMMARY:
        {
            UpdateCrasd();
            count++;
            break;
        }
        case STEP_COUNTER_ACTIVITY_SUMMARY:
        {
            UpdateScasd();
            count++;
            break;
        }
        case SLEEP_ACTIVITY_INSTANTANEOUS:
        {
            UpdateSaid();
            count++;
            break;
        }
        case SLEEP_ACTIVITY_SUMMARY:
        {
            UpdateSasd();
            count++;
            break;
        }
    }
    return count;
}

void InitializePhisicalActivityMonitor()
{
    UART_DEB_Start();
    
    sessionCount = 0;
    subsessionCount = 0;
    totalSessionCount = 0;
    totalSubSessionCount = 0;
    totalDeletedSessions = 0;
    
    //init features
    //pamFeature = getChosenFeatures();
    
    pamFeature = 0xFFFFFFFFFFFFFFFF;
    UpdateFeatureList();
    
        
    //init GAI Data
    GaiData.Flags[0] = 0x02;
    GaiData.Flags[1] = 0x02;
    GaiData.Flags[2] = 0x02;
    UpdateGaid();
   
    //init GAS Data
    GasData.Flags = 0xFFFFFFFF;
    GasData.AverageActivityType = 0xAA;
    UpdateGasd();

    //init CRAI Data
    //DO NOT CHANGE THE FLAGS BEFORE THE SPECIFICATION FLAGS HAVE BEEN DEFINED AND IMPLEMENTED IN:
    // SERVER -> pamCraid.h & pamCraid.c
    // CLIENT -> HBPhysicalActivityMonitorCraid.java
    // IF YOU CHANGE FLAGS YOU MIGHT GET AN ARRAY OUT OF BOUNDS EXCEPTION
    CraiData.Flags = 0xFFFF;
    UpdateCraid();
    
    //init CRAS Data
    //DO NOT CHANGE THE FLAGS BEFORE THE SPECIFICATION FLAGS HAVE BEEN DEFINED AND IMPLEMENTED IN:
    // SERVER -> pamCrasd.h & pamCrasd.c
    // CLIENT -> HBPhysicalActivityMonitorCrasd.java
    // IF YOU CHANGE FLAGS YOU MIGHT GET AN ARRAY OUT OF BOUNDS EXCEPTION
    CrasData.Flags = 0xFFFFFFFF;
    UpdateCrasd();
    
    //init SCAS Data
    //DO NOT CHANGE THE FLAGS BEFORE THE SPECIFICATION FLAGS HAVE BEEN DEFINED AND IMPLEMENTED IN:
    // SERVER -> pamScasd.h & pamScasd.c
    // CLIENT -> HBPhysicalActivityMonitorScasd.java
    // IF YOU CHANGE FLAGS YOU MIGHT GET AN ARRAY OUT OF BOUNDS EXCEPTION
    ScasData.Flags = 0xFF;
    UpdateScasd();
    
    //init SAI Data
    //DO NOT CHANGE THE FLAGS BEFORE THE SPECIFICATION FLAGS HAVE BEEN DEFINED AND IMPLEMENTED IN:
    // SERVER -> pamSaid.h & pamSaid.c
    // CLIENT -> HBPhysicalActivityMonitorSaid.java
    // IF YOU CHANGE FLAGS YOU MIGHT GET AN ARRAY OUT OF BOUNDS EXCEPTION
    SaiData.Flags = 0xFFFF;
    UpdateSaid();
    
    //init SAS Data
    //DO NOT CHANGE THE FLAGS BEFORE THE SPECIFICATION FLAGS HAVE BEEN DEFINED AND IMPLEMENTED IN:
    // SERVER -> pamSasd.h & pamSasd.c
    // CLIENT -> HBPhysicalActivityMonitorSasd.java
    // IF YOU CHANGE FLAGS YOU MIGHT GET AN ARRAY OUT OF BOUNDS EXCEPTION
    SasData.Flags[0] = 0xFF;
    SasData.Flags[1] = 0xFF;
    SasData.Flags[2] = 0xFF;
    UpdateSasd();
}

void HandleLeds()
{
    if(CyBle_GetState() == CYBLE_STATE_DISCONNECTED)
    {
        pwm_Stop();
        connected_LED_Write(LED_OFF);
        disconnected_LED_Write(LED_ON);
    }
    else if(CyBle_GetState() == CYBLE_STATE_ADVERTISING)
    {
        connected_LED_Write(LED_OFF);
        disconnected_LED_Write(LED_OFF);
        pwm_Start();
    }
    else if(CyBle_GetState() == CYBLE_STATE_CONNECTED)
    {
        pwm_Stop();
        disconnected_LED_Write(LED_OFF);
        connected_LED_Write(LED_ON);
    }
}

/* [] END OF FILE */
