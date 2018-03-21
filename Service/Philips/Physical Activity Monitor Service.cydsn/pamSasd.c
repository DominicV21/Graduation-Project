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

#include "pamSasd.h"

void UpdateSasd(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_SLEEP_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&SasData;
    tempHandle.value.len = sizeof(SasData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(sleepactivitysummarydataIndicate)
    {
        uint8 PresentData[sizeof(SasData)];
        
        uint8 presentDataLength = getPresentSasdFields(PresentData);
        DBG_PRINTF("SLEEP ACTIVITY SUMMARY DATA presentdatalength = %i\r\n", presentDataLength);
        uint8 N = calculateNumberOfMessages(presentDataLength);
        DBG_PRINTF("SLEEP ACTIVITY SUMMARY DATA N = %i\r\n", N);
        
        uint8 byteCount = 1u;//Starts at 1 because 0 is the header field
        
        PresentData[byteCount] = SasData.Flags[0];
        PresentData[byteCount + 1] = SasData.Flags[1];
        PresentData[byteCount + 2] = SasData.Flags[2];
        byteCount += sizeof(SasData.Flags);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], SasData.SessionID);
        byteCount += sizeof(SasData.SessionID);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], SasData.SubSessionID);
        byteCount += sizeof(SasData.SubSessionID);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], SasData.RelativeTimestamp);
        byteCount += sizeof(SasData.RelativeTimestamp);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], SasData.SequenceNumber);
        byteCount += sizeof(SasData.SequenceNumber);
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
            ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_SLEEP_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE, Message, MessageSize);
        }
    }
}


uint8 getPresentSasdFields(uint8 *PresentData)
{
    uint8 length = ALWAYS_PRESENT_SASD_FIELD_LENGTH;

    if(CHECK_BIT(SasData.Flags[0], 1/*TOTAL SLEEP TIME PRESENT*/) &&
       CHECK_BIT(pamFeature, TOTAL_SLEEP_TIME_SUPPORTED))
    {
        PresentData[length] = SasData.TotalSleepTime[0];
        PresentData[length+1] = SasData.TotalSleepTime[1];
        PresentData[length+2] = SasData.TotalSleepTime[2];
        length += sizeof(SasData.TotalSleepTime);
    }
    if(CHECK_BIT(SasData.Flags[0], 1/*TOTAL WAKE TIME PRESENT*/) &&
       CHECK_BIT(pamFeature, TOTAL_WAKE_TIME_SUPPORTED))
    {
        PresentData[length] = SasData.TotalWakeTime[0];
        PresentData[length+1] = SasData.TotalWakeTime[1];
        PresentData[length+2] = SasData.TotalWakeTime[2];
        length += sizeof(SasData.TotalWakeTime);
    }
    if(CHECK_BIT(SasData.Flags[0], 1/*TOTAL BED TIME PRESENT*/) &&
       CHECK_BIT(pamFeature, TOTAL_BED_TIME_SUPPORTED))
    {
        PresentData[length] = SasData.TotalBedTime[0];
        PresentData[length+1] = SasData.TotalBedTime[1];
        PresentData[length+2] = SasData.TotalBedTime[2];
        length += sizeof(SasData.TotalBedTime);
    }
    if(CHECK_BIT(SasData.Flags[0], 1/*NR OF AWAKENINGS PRESENT*/) &&
       CHECK_BIT(pamFeature, NUMBER_OF_AWAKENINGS_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], SasData.NumberOfAwakenings);
        length += sizeof(SasData.NumberOfAwakenings);
    }
    if(CHECK_BIT(SasData.Flags[0], 1/*SLEEP LATENCY PRESENT*/) &&
       CHECK_BIT(pamFeature, SLEEP_LATENCY_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], SasData.SleepLatency);
        length += sizeof(SasData.SleepLatency);
    }
    if(CHECK_BIT(SasData.Flags[0], 1/*SLEEP EDDICIENCY PRESENT*/) &&
       CHECK_BIT(pamFeature, SLEEP_EFFICIENCY_SUPPORTED))
    {
        PresentData[length] = SasData.SleepEfficiency;
        length += sizeof(SasData.SleepEfficiency);
    }
    if(CHECK_BIT(SasData.Flags[0], 1/*SNOOZE TIME PRESENT*/) &&
       CHECK_BIT(pamFeature, SNOOZE_TIME_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], SasData.SnoozeTime);
        length += sizeof(SasData.SnoozeTime);
    }
    if(CHECK_BIT(SasData.Flags[0], 1/*NROF TOSSNTURN EVENTS PRESENT*/) &&
       CHECK_BIT(pamFeature, NUMBER_OF_TOSS_N_TURN_EVENTS_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], SasData.NumberOfTossNturnEvents);
        length += sizeof(SasData.NumberOfTossNturnEvents);
    }
    if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*TIME OF AWEKENING AFTER ALARM PRESENT*/) &&
       CHECK_BIT(pamFeature, TIME_OF_AWAKENING_AFTER_ALARM_SUPPORTED))
    {
        PresentData[length] = SasData.TimeOfAwakeningAfterAlarm[0];
        PresentData[length+1] = SasData.TimeOfAwakeningAfterAlarm[1];
        PresentData[length+2] = SasData.TimeOfAwakeningAfterAlarm[2];
        length += sizeof(SasData.TimeOfAwakeningAfterAlarm);
    }
    if(CHECK_BIT(pamFeature, VISIBLE_LIGHT_LEVEL_SUPPORTED))
    {
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*MinimumVisibleLightLevel PRESENT*/))
        {
            PresentData[length] = SasData.MinimumVisibleLightLevel[0];
            PresentData[length+1] = SasData.MinimumVisibleLightLevel[1];
            PresentData[length+2] = SasData.MinimumVisibleLightLevel[2];
            length += sizeof(SasData.MinimumVisibleLightLevel);
        }
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*MAXIMUMVisibleLightLevel PRESENT*/))
        {
            PresentData[length] = SasData.MaximumVisibleLightLevel[0];
            PresentData[length+1] = SasData.MaximumVisibleLightLevel[1];
            PresentData[length+2] = SasData.MaximumVisibleLightLevel[2];
            length += sizeof(SasData.MaximumVisibleLightLevel);
        }
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*AVERAGEVisibleLightLevel PRESENT*/))
        {
            PresentData[length] = SasData.AverageVisibleLightLevel[0];
            PresentData[length+1] = SasData.AverageVisibleLightLevel[1];
            PresentData[length+2] = SasData.AverageVisibleLightLevel[2];
            length += sizeof(SasData.AverageVisibleLightLevel);
        }
    }
    if(CHECK_BIT(pamFeature, UV_LIGHT_LEVEL_SUPPORTED))
    {
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*Minimumuvlight PRESENT*/))
        {
            PresentData[length] = SasData.MinimumUVLightLevel[0];
            PresentData[length+1] = SasData.MinimumUVLightLevel[1];
            PresentData[length+2] = SasData.MinimumUVLightLevel[2];
            length += sizeof(SasData.MinimumUVLightLevel);
        }
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*MAXIMUMuvlight PRESENT*/))
        {
            PresentData[length] = SasData.MaximumUVLightLevel[0];
            PresentData[length+1] = SasData.MaximumUVLightLevel[1];
            PresentData[length+2] = SasData.MaximumUVLightLevel[2];
            length += sizeof(SasData.MaximumUVLightLevel);
        }
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*AVERAGEuvlight PRESENT*/))
        {
            PresentData[length] = SasData.AverageUVLightLevel[0];
            PresentData[length+1] = SasData.AverageUVLightLevel[1];
            PresentData[length+2] = SasData.AverageUVLightLevel[2];
            length += sizeof(SasData.AverageUVLightLevel);
        }
    }
    if(CHECK_BIT(pamFeature, IR_LIGHT_LEVEL_SUPPORTED))
    {
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*MinimumIRlight PRESENT*/))
        {
            PresentData[length] = SasData.MinimumIRLightLevel[0];
            PresentData[length+1] = SasData.MinimumIRLightLevel[1];
            PresentData[length+2] = SasData.MinimumIRLightLevel[2];
            length += sizeof(SasData.MinimumIRLightLevel);
        }
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*MAXIMUMIRlight PRESENT*/))
        {
            PresentData[length] = SasData.MaximumIRLightLevel[0];
            PresentData[length+1] = SasData.MaximumIRLightLevel[1];
            PresentData[length+2] = SasData.MaximumIRLightLevel[2];
            length += sizeof(SasData.MaximumIRLightLevel);
        }
        if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*AVERAGEIRlight PRESENT*/))
        {
            PresentData[length] = SasData.AverageIRLightLevel[0];
            PresentData[length+1] = SasData.AverageIRLightLevel[1];
            PresentData[length+2] = SasData.AverageIRLightLevel[2];
            length += sizeof(SasData.AverageIRLightLevel);
        }
    }
    if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*AVERAGE SLEEPING HEART RATE*/) &&
       CHECK_BIT(pamFeature, SLEEPING_HEART_RATE_SUPPORTED))
    {
        PresentData[length] = SasData.AverageSleepingHeartRate;
        length += sizeof(SasData.AverageSleepingHeartRate);
    }
    if(CHECK_BIT(SasData.Flags[1], 8 - ONE_BYTE/*MinimumIRlight PRESENT*/) &&
       CHECK_BIT(pamFeature, WORN_DURATION_SUPPORTED))
    {
        PresentData[length] = SasData.WornDuration[0];
        PresentData[length+1] = SasData.WornDuration[1];
        PresentData[length+2] = SasData.WornDuration[2];
        length += sizeof(SasData.WornDuration);
    }
   
    return length;
}

/* [] END OF FILE */
