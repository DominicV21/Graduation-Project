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

#include "pamSaid.h"

void UpdateSaid(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_SLEEP_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&SaiData;
    tempHandle.value.len = sizeof(SaiData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(sleepactivityinstantaneousdataNotify)
    {
        uint8 PresentData[sizeof(SaiData)];
        
        uint8 presentDataLength = getPresentSaidFields(PresentData);
        DBG_PRINTF("SLEEP ACTIVITY INSTANTANEOUS DATA presentdatalength = %i\r\n", presentDataLength);
        uint8 N = calculateNumberOfMessages(presentDataLength);
        DBG_PRINTF("SLEEP ACTIVITY INSTANTANEOUS DATA N = %i\r\n", N);
        
        uint8 byteCount = 1u;//Starts at 1 because 0 is the header field
        
        CyBle_Set16ByPtr(&PresentData[byteCount], SaiData.Flags);
        byteCount += sizeof(SaiData.Flags);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], SaiData.SessionID);
        byteCount += sizeof(SaiData.SessionID);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], SaiData.SubSessionID);
        byteCount += sizeof(SaiData.SubSessionID);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], SaiData.RelativeTimestamp);
        byteCount += sizeof(SaiData.RelativeTimestamp);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], SaiData.SequenceNumber);
        byteCount += sizeof(SaiData.SequenceNumber);
        //size = 15
        
        char* my_struct_in_bytes = (char*)(&PresentData);
        
        uint8 Message[PAM_MAX_MESSAGE_DATA_SIZE + HEADER_SIZE];
        uint8 MessageSize = 0;
        
        
        for(uint8 x = 0; x < N; x++)
        {
            if(x == 0)
            {
                Message[0] = 0x80 + N - 1;
            }
            else
            {
                Message[0] = 0x00 + N - 1 - x;
            }
            
            
            for(uint8 i = 1; i < PAM_MAX_MESSAGE_DATA_SIZE + HEADER_SIZE; i++)
            {
                if((x * PAM_MAX_MESSAGE_DATA_SIZE + i) < presentDataLength)
                {
                    Message[i] = my_struct_in_bytes[i + x * PAM_MAX_MESSAGE_DATA_SIZE];
                    MessageSize = i + HEADER_SIZE;
                }
            }
            ble_SendNotification(CYBLE_PHYSICAL_ACTIVITY_MONITOR_SLEEP_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE, Message, MessageSize);
        }
    }
}


uint8 getPresentSaidFields(uint8 *PresentData)
{
    uint8 length = ALWAYS_PRESENT_SAID_FIELD_LENGTH;
    
    if(CHECK_BIT(SaiData.Flags, 1/*VISIBLE LIGHT LEVEL PRESENT*/) &&
       CHECK_BIT(pamFeature, VISIBLE_LIGHT_LEVEL_SUPPORTED))
    {
        PresentData[length] = SaiData.VisibleLightLevel[0];
        PresentData[length+1] = SaiData.VisibleLightLevel[1];
        PresentData[length+2] = SaiData.VisibleLightLevel[2];
        length += sizeof(SaiData.VisibleLightLevel);
    }
    if(CHECK_BIT(SaiData.Flags, 1/*UV LIGHT LEVEL PRESENT*/) &&
       CHECK_BIT(pamFeature, UV_LIGHT_LEVEL_SUPPORTED))
    {
        PresentData[length] = SaiData.UVLightLevel[0];
        PresentData[length+1] = SaiData.UVLightLevel[1];
        PresentData[length+2] = SaiData.UVLightLevel[2];
        length += sizeof(SaiData.UVLightLevel);
    }
    if(CHECK_BIT(SaiData.Flags, 1/*IR LIGHT LEVEL PRESENT*/) &&
       CHECK_BIT(pamFeature, IR_LIGHT_LEVEL_SUPPORTED))
    {
        PresentData[length] = SaiData.IRLightLevel[0];
        PresentData[length+1] = SaiData.IRLightLevel[1];
        PresentData[length+2] = SaiData.IRLightLevel[2];
        length += sizeof(SaiData.IRLightLevel);
    }
    if(CHECK_BIT(SaiData.Flags, 1/*SLEEP STAGE PRESENT*/) &&
       CHECK_BIT(pamFeature, SLEEP_STAGE_SUPPORTED))
    {
        PresentData[length] = SaiData.SleepStage;
        length += sizeof(SaiData.SleepStage);
    }
    if(CHECK_BIT(SaiData.Flags, 1/*SLEEPING HEART RATE PRESENT*/) &&
       CHECK_BIT(pamFeature, SLEEPING_HEART_RATE_SUPPORTED))
    {
        PresentData[length] = SaiData.SleepingHeartRate;
        length += sizeof(SaiData.SleepingHeartRate);
    }
    
    return length;
}

/* [] END OF FILE */
