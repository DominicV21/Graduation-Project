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

#include "pamCrasd.h"

void UpdateCrasd(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_CARDIORESPIRATORY_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&CrasData;
    tempHandle.value.len = sizeof(CrasData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(cardiorespiratoryactivitysummarydataIndicate)
    {
        uint8 PresentData[sizeof(CrasData)];
        
        uint8 presentDataLength = getPresentCrasdFields(PresentData);
        DBG_PRINTF("CARDIORESPIRATORY ACTIVITY SUMMARY DATA presentdatalength = %i\r\n", presentDataLength);
        uint8 N = calculateNumberOfMessages(presentDataLength);
        DBG_PRINTF("CARDIORESPIRATORY ACTIVITY SUMMARY DATA N = %i\r\n", N);
        
        uint8 byteCount = 1u;//Starts at 1 because 0 is the header field
        
        CyBle_Set16ByPtr(&PresentData[byteCount], CrasData.Flags);
        byteCount += sizeof(CrasData.Flags);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], CrasData.SessionID);
        byteCount += sizeof(CrasData.SessionID);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], CrasData.SubSessionID);
        byteCount += sizeof(CrasData.SubSessionID);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], CrasData.RelativeTimestamp);
        byteCount += sizeof(CrasData.RelativeTimestamp);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], CrasData.SequenceNumber);
        byteCount += sizeof(CrasData.SequenceNumber);
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
            ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_CARDIORESPIRATORY_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE, Message, MessageSize);
        }
    }
}


uint8 getPresentCrasdFields(uint8 *PresentData)
{
    uint8 length = ALWAYS_PRESENT_CRAID_FIELD_LENGTH;
    
    if(CHECK_BIT(CrasData.Flags, 1/*TIMEZONE1 PRESENT*/) &&
       CHECK_BIT(pamFeature, TIME_IN_HEART_RATE_ZONE1_SUPPORTED))
    {
        PresentData[length] = CrasData.TimeInHeartRateZone1[0];
        PresentData[length+1] = CrasData.TimeInHeartRateZone1[1];
        PresentData[length+2] = CrasData.TimeInHeartRateZone1[2];
        length += sizeof(CrasData.TimeInHeartRateZone1);
    }
    if(CHECK_BIT(CrasData.Flags, 1/*TIMEZONE2 PRESENT*/) &&
       CHECK_BIT(pamFeature, TIME_IN_HEART_RATE_ZONE2_SUPPORTED))
    {
        PresentData[length] = CrasData.TimeInHeartRateZone2[0];
        PresentData[length+1] = CrasData.TimeInHeartRateZone2[1];
        PresentData[length+2] = CrasData.TimeInHeartRateZone2[2];
        length += sizeof(CrasData.TimeInHeartRateZone2);
    }
    if(CHECK_BIT(CrasData.Flags, 1/*TIMEZONE3 PRESENT*/) &&
       CHECK_BIT(pamFeature, TIME_IN_HEART_RATE_ZONE3_SUPPORTED))
    {
        PresentData[length] = CrasData.TimeInHeartRateZone3[0];
        PresentData[length+1] = CrasData.TimeInHeartRateZone3[1];
        PresentData[length+2] = CrasData.TimeInHeartRateZone3[2];
        length += sizeof(CrasData.TimeInHeartRateZone3);
    }
    if(CHECK_BIT(CrasData.Flags, 1/*TIMEZONE4 PRESENT*/) &&
       CHECK_BIT(pamFeature, TIME_IN_HEART_RATE_ZONE4_SUPPORTED))
    {
        PresentData[length] = CrasData.TimeInHeartRateZone4[0];
        PresentData[length+1] = CrasData.TimeInHeartRateZone4[1];
        PresentData[length+2] = CrasData.TimeInHeartRateZone4[2];
        length += sizeof(CrasData.TimeInHeartRateZone4);
    }
    if(CHECK_BIT(CrasData.Flags, 1/*TIMEZONE5 PRESENT*/) &&
       CHECK_BIT(pamFeature, TIME_IN_HEART_RATE_ZONE5_SUPPORTED))
    {
        PresentData[length] = CrasData.TimeInHeartRateZone5[0];
        PresentData[length+1] = CrasData.TimeInHeartRateZone5[1];
        PresentData[length+2] = CrasData.TimeInHeartRateZone5[2];
        length += sizeof(CrasData.TimeInHeartRateZone5);
    }
    if(CHECK_BIT(pamFeature, VO2_MAX_SUPPORTED))
    {
        if(CHECK_BIT(CrasData.Flags, 1/*MIN VO2MAX PRESENT*/))
        {
            PresentData[length] = CrasData.MaximumVO2Max;
            length += sizeof(CrasData.MaximumVO2Max);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*MAX VO2MAX PRESENT*/))
        {
            PresentData[length] = CrasData.MaximumVO2Max;
            length += sizeof(CrasData.MaximumVO2Max);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*AVG VO2MAX PRESENT*/))
        {
            PresentData[length] = CrasData.AverageVO2Max;
            length += sizeof(CrasData.AverageVO2Max);
        }
    }
    if(CHECK_BIT(pamFeature, HEART_RATE_SUPPORTED))
    {
        if(CHECK_BIT(CrasData.Flags, 1/*MIN HEARTRATE PRESENT*/))
        {
            PresentData[length] = CrasData.MinimumHeartRate;
            length += sizeof(CrasData.MinimumHeartRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*MAX HEARTRATE PRESENT*/))
        {
            PresentData[length] = CrasData.MaximumHeartRate;
            length += sizeof(CrasData.MaximumHeartRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*AVG HEARTRATE PRESENT*/))
        {
            PresentData[length] = CrasData.AverageHeartRate;
            length += sizeof(CrasData.AverageHeartRate);
        }
    }
    if(CHECK_BIT(pamFeature, PULSE_INTER_BEAT_INTERVAL_SUPPORTED))
    {
        if(CHECK_BIT(CrasData.Flags, 1/*MIN PULSE INTERBEAT INTERVAL PRESENT*/))
        {
            CyBle_Set16ByPtr(&PresentData[length], CrasData.MinimumPulseInterbeatInterval);
            length += sizeof(CrasData.MinimumPulseInterbeatInterval);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*MAX PULSE INTERBEAT INTERVAL PRESENT*/))
        {
            CyBle_Set16ByPtr(&PresentData[length], CrasData.MaximumPulseInterbeatInterval);
            length += sizeof(CrasData.MaximumPulseInterbeatInterval);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*AVG PULSE INTERBEAT INTERVAL PRESENT*/))
        {
            CyBle_Set16ByPtr(&PresentData[length], CrasData.AveragePulseInterbeatInterval);
            length += sizeof(CrasData.AveragePulseInterbeatInterval);
        }
    }
    if(CHECK_BIT(pamFeature, RESTING_HEART_RATE_SUPPORTED))
    {
        if(CHECK_BIT(CrasData.Flags, 1/*MIN REST HEART RATE PRESENT*/))
        {
            PresentData[length] = CrasData.MinimumRestingHeartRate;
            length += sizeof(CrasData.MinimumRestingHeartRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*MAX REST HEART RATE PRESENT*/))
        {
            PresentData[length] = CrasData.MaximumRestingHeartRate;
            length += sizeof(CrasData.MaximumRestingHeartRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*AVG REST HEART RATE PRESENT*/))
        {
            PresentData[length] = CrasData.AverageRestingHeartRate;
            length += sizeof(CrasData.AverageRestingHeartRate);
        }
    }
    if(CHECK_BIT(pamFeature, HEART_RATE_VARIABILITY_SUPPORTED))
    {
        if(CHECK_BIT(CrasData.Flags, 1/*MIN HEART RATE VARIABILITY PRESENT*/))
        {
            CyBle_Set16ByPtr(&PresentData[length], CrasData.MinimumHeartRateVariability);
            length += sizeof(CrasData.MinimumHeartRateVariability);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*MAX HEART RATE VARIABILITY PRESENT*/))
        {
            CyBle_Set16ByPtr(&PresentData[length], CrasData.MaximumHeartRateVariability);
            length += sizeof(CrasData.MaximumHeartRateVariability);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*AVG HEART RATE VARIABILITY PRESENT*/))
        {
            CyBle_Set16ByPtr(&PresentData[length], CrasData.AverageHeartRateVariability);
            length += sizeof(CrasData.AverageHeartRateVariability);
        }
    }
    if(CHECK_BIT(pamFeature, RESPIRATION_RATE_SUPPORTED))
    {
        if(CHECK_BIT(CrasData.Flags, 1/*MIN RESPIRATION RATE PRESENT*/))
        {
            PresentData[length] = CrasData.MinimumRespirationRate;
            length += sizeof(CrasData.MinimumRespirationRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*MAX RESPIRATION RATE PRESENT*/))
        {
            PresentData[length] = CrasData.MaximumRespirationRate;
            length += sizeof(CrasData.MaximumRespirationRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*AVG RESPIRATION RATE PRESENT*/))
        {
            PresentData[length] = CrasData.AverageRespirationRate;
            length += sizeof(CrasData.AverageRespirationRate);
        }
    }
    if(CHECK_BIT(pamFeature, RESTING_RESPIRATION_RATE_SUPPORTED))
    {
        if(CHECK_BIT(CrasData.Flags, 1/*MIN RESTING RESPIRATION RATE PRESENT*/))
        {
            PresentData[length] = CrasData.MinimumRestingRespirationRate;
            length += sizeof(CrasData.MinimumRestingRespirationRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*MAX RESTING RESPIRATION RATE PRESENT*/))
        {
            PresentData[length] = CrasData.MaximumRestingRespirationRate;
            length += sizeof(CrasData.MaximumRestingRespirationRate);
        }
        if(CHECK_BIT(CrasData.Flags, 1/*AVG RESTING RESPIRATION RATE PRESENT*/))
        {
            PresentData[length] = CrasData.AverageRestingRespirationRate;
            length += sizeof(CrasData.AverageRestingRespirationRate);
        }
    }
    if(CHECK_BIT(CrasData.Flags, 1/*WORN DURATION PRESENT*/) &&
       CHECK_BIT(pamFeature, WORN_DURATION_SUPPORTED))
    {
        PresentData[length] = CrasData.WornDuration[0];
        PresentData[length + 1] = CrasData.WornDuration[1];
        PresentData[length + 2] = CrasData.WornDuration[2];
        length += sizeof(CrasData.WornDuration);
    }
    
    return length;
}

/* [] END OF FILE */
