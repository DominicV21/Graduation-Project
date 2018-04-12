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

#ifndef PAMS_H__
#define PAMS_H__

#include "common.h"
    
   
/***************************************
*          Constants
***************************************/
#define TRUE    (1u)
#define FALSE   (0u)    
     
#define PAM_ATT_MTU_SIZE                    (23u)
#define PAM_MAX_MESSAGE_DATA_SIZE           (PAM_ATT_MTU_SIZE - 4u)
    
#define ONE_BYTE                            (8u)
#define TWO_BYTES_SHIFT                     (16u)
#define THREE_BYTES_SHIFT                   (24u)
#define FOUR_BYTES_SHIFT                    (32u)
#define FIVE_BYTES_SHIFT                    (40u)
#define SIX_BYTES_SHIFT                     (48u)
#define SEVEN_BYTES_SHIFT                   (56u)

#define HEADER_SIZE                         (1u)


#define CHECK_BIT(var,pos) ((var) & (1ull<<(pos)))
#define TOGGLE_BIT(var,pos) ((var) ^= (1ull<<(pos)))
#define SET_BIT(var, pos) ((var) |= (1ull<<(pos)))
#define UNSET_BIT(var, pos) ((var) &= ~(1ull << (pos)))

void ble_SendNotification(CYBLE_GATT_DB_ATTR_HANDLE_T characteristic, uint8* data, uint16 length);
void ble_SendIndication(CYBLE_GATT_DB_ATTR_HANDLE_T characteristic, uint8* data, uint16 length);

void CyBle_Set24ByPtr(uint8 ptr[], uint32 value);
void CyBle_Set32ByPtr(uint8 ptr[], uint32 value);

uint8 calculateNumberOfMessages(double sizeOfData);
uint8 checkMaxMessageSizeReached(uint8 currentSize, uint8 sizeToAdd);
void setMessageHeader(uint8 messageBuffer[][PAM_MAX_MESSAGE_DATA_SIZE], uint8 messageCount);
void addToBuffer(uint8 *messageBuffer, uint8 *MessageSize, uint8 *Message, uint8 *messageCount, uint8 *byteCount);

uint16 Enquire_Sessions_Requested;
uint16 Enquire_Sub_Sessions_Requested_For_ID;
uint16 Get_Ended_Data_SessionID;
uint16 Get_Ended_Data_Sub_SessionID;
uint8 Get_Ended_Data_Value;
uint8 send_Deleted_Sessions;
CYBIT Update_GASD_With_New_Activity;
uint8 UserDefinedActivityTypeAllSubSessions[25][25];

#endif /* PAMS_H__*/