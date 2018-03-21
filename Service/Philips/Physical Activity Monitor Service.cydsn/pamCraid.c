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

#include "pamCraid.h"

void UpdateCraid(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&CraiData;
    tempHandle.value.len = sizeof(CraiData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(cardiorespiratoryactivityinstantaneousdataNotify)
    {
        uint8 PresentData[sizeof(CraiData)];
        
        uint8 presentDataLength = getPresentCraidFields(PresentData);
        DBG_PRINTF("CARDIORESPIRATOTY ACTIVITY INSTANTANEOUS DATA presentdatalength = %i\r\n", presentDataLength);
        uint8 N = calculateNumberOfMessages(presentDataLength);
        DBG_PRINTF("CARDIORESPIRATOTY ACTIVITY INSTANTANEOUS DATA N = %i\r\n", N);
        
        uint8 byteCount = 1u;//Starts at 1 because 0 is the header field
        
        CyBle_Set16ByPtr(&PresentData[byteCount], CraiData.Flags);
        byteCount += sizeof(CraiData.Flags);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], CraiData.SessionID);
        byteCount += sizeof(CraiData.SessionID);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], CraiData.SubSessionID);
        byteCount += sizeof(CraiData.SubSessionID);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], CraiData.RelativeTimestamp);
        byteCount += sizeof(CraiData.RelativeTimestamp);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], CraiData.SequenceNumber);
        byteCount += sizeof(CraiData.SequenceNumber);
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
            ble_SendNotification(CYBLE_PHYSICAL_ACTIVITY_MONITOR_CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE, Message, MessageSize);
        }
    }
}


uint8 getPresentCraidFields(uint8 *PresentData)
{
    uint8 length = ALWAYS_PRESENT_CRAID_FIELD_LENGTH;
    
    if(CHECK_BIT(CraiData.Flags, 1/*VO2MAX PRESENT*/) &&
       CHECK_BIT(pamFeature, VO2_MAX_SUPPORTED))
    {
        PresentData[length] = CraiData.VO2Max;
        length += sizeof(CraiData.VO2Max);
    }
    if(CHECK_BIT(CraiData.Flags, 1/*Heart rate Present*/) &&
       CHECK_BIT(pamFeature, HEART_RATE_SUPPORTED))
    {
        PresentData[length] = CraiData.HeartRate;
        length += sizeof(CraiData.HeartRate);
    }
    if(CHECK_BIT(CraiData.Flags, 1/*Pulse Inter Beat Interval present*/) &&
       CHECK_BIT(pamFeature, PULSE_INTER_BEAT_INTERVAL_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], CraiData.PulseInterBeatInterval);
        length += sizeof(CraiData.PulseInterBeatInterval);
    }
    if(CHECK_BIT(CraiData.Flags, 1/*Resting heart rate present*/) &&
       CHECK_BIT(pamFeature, RESTING_HEART_RATE_SUPPORTED))
    {
        PresentData[length] = CraiData.RestingHeartRate;
        length += sizeof(CraiData.RestingHeartRate);
    }
    if(CHECK_BIT(CraiData.Flags, 1/*Heart Rate Variability*/) &&
       CHECK_BIT(pamFeature, HEART_RATE_VARIABILITY_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], CraiData.HeartRateVariability);
        length += sizeof(CraiData.HeartRateVariability);
    }
    if(CHECK_BIT(CraiData.Flags, 1/*Respiration rate present*/) &&
       CHECK_BIT(pamFeature, RESPIRATION_RATE_SUPPORTED))
    {
        PresentData[length] = CraiData.RespirationRate;
        length += sizeof(CraiData.RespirationRate);
    }
    if(CHECK_BIT(CraiData.Flags, 1/*Resting Respiration rate*/) &&
       CHECK_BIT(pamFeature, RESTING_RESPIRATION_RATE_SUPPORTED))
    {
        PresentData[length] = CraiData.RestingRespirationRate;
        length += sizeof(CraiData.RestingRespirationRate);
    }
    
    return length;
}

/* [] END OF FILE */
