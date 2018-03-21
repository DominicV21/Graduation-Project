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
#include "pams.h"

/***************************************
*        Global Variables
***************************************/


uint8 calculateNumberOfMessages(double sizeOfData)
{
    return ceil((sizeOfData - 1)/(PAM_ATT_MTU_SIZE - 4));
}


uint8 checkMaxMessageSizeReached(uint8 currentSize, uint8 sizeToAdd)
{
    if(currentSize + sizeToAdd > PAM_MAX_MESSAGE_DATA_SIZE)
    {
        return  1;
    }
    else 
        return 0;
}

void setMessageHeader(uint8 messageBuffer[][PAM_MAX_MESSAGE_DATA_SIZE], uint8 messageCount)
{
    messageBuffer[0][0] = 0x80 + messageCount - 1;
    
    for(int i = 1; i < messageCount; i++)
    {
        messageBuffer[i][0] = (messageCount - 1) - i;
    }
}

void addToBuffer(uint8 *messageBuffer, uint8 *MessageSize, uint8 *Message, uint8 *messageCount, uint8 *byteCount)
{
    memcpy(messageBuffer, Message, *byteCount);
    memcpy(MessageSize, byteCount, 1);
    *byteCount = 1;
    *messageCount += 1;
}


void ble_SendNotification(CYBLE_GATT_DB_ATTR_HANDLE_T characteristic, uint8* data, uint16 length)
{
    CYBLE_API_RESULT_T t;
    CYBLE_GATTS_HANDLE_VALUE_NTF_T notification;
    notification.attrHandle = characteristic;
    notification.value.val  = data;
    notification.value.len  = length;
    if ((t = CyBle_GattsNotification(cyBle_connHandle, &notification)) != CYBLE_ERROR_OK) 
    {
        DBG_PRINTF("ERROR sending notification %i\r\n", t);
    }    
    else
    {
        DBG_PRINTF("sending notification Size = %i\r\n", length);
    }
}

void ble_SendIndication(CYBLE_GATT_DB_ATTR_HANDLE_T characteristic, uint8* data, uint16 length)
{
    while(CyBle_IsStackIdle() == CYBLE_ERROR_STACK_BUSY || CyBle_GattGetBusStatus() == CYBLE_STACK_STATE_BUSY)
    {
        /* Wait for BLE Stack to comple current operation */
        CyBle_ProcessEvents();
    }
    CYBLE_API_RESULT_T t;
    CYBLE_GATTS_HANDLE_VALUE_IND_T indication;
    indication.value.val  = data;
    indication.value.len  = length;
    indication.attrHandle = characteristic;
    
    if ((t = CyBle_GattsIndication(cyBle_connHandle, &indication)) != CYBLE_ERROR_OK) 
    {
        DBG_PRINTF("ERROR sending indication %i\r\n", t);
    }    
    else
    {
        DBG_PRINTF("sending indication Size = %i\r\n", length);
    }
    
}

void CyBle_Set32ByPtr(uint8 ptr[], uint32 value)
{
    ptr[0u] = (uint8) value;
    ptr[1u] = (uint8) (value >> 8u);
    ptr[2u] = (uint8) (value >> 16u);
    ptr[3u] = (uint8) (value >> 24u);
}

/*
void GetUint24(uint32 *u32, uint8 u24Ptr[])
{
    *u32 = ((uint32) ((uint32) u24Ptr[0u]) | ((uint32) (((uint32) u24Ptr[1u]) << 8u)) |
                                               ((uint32) (((uint32) u24Ptr[2u]) << 16u)));
}

*/

/* [] END OF FILE */