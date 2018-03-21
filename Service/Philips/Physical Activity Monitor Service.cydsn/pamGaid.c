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

#include "pamGaid.h"

void UpdateGaid(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&GaiData;
    tempHandle.value.len = sizeof(GaiData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(generalinstantaneousdataNotify)
    {
        uint8 PresentData[sizeof(GaiData)];
        
        uint8 presentDataLength = getPresentGaidFields(PresentData);
        DBG_PRINTF("GENERAL ACTIVITY INSTANTANEOUS DATA presentdatalength = %i\r\n", presentDataLength);
        uint8 N = calculateNumberOfMessages(presentDataLength);
        DBG_PRINTF("GENERAL ACTIVITY INSTANTANEOUS DATA N = %i\r\n", N);
        
        uint8 byteCount = 1u;//Starts at 1 because 0 is the header field
        
        PresentData[byteCount++] = GaiData.Flags[0];
        PresentData[byteCount++] = GaiData.Flags[1];
        PresentData[byteCount++] = GaiData.Flags[2];
        
        CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.SessionID);
        byteCount += sizeof(GaiData.SessionID);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.SubSessionID);
        byteCount += sizeof(GaiData.SubSessionID);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], GaiData.RelativeTimestamp);
        byteCount += sizeof(GaiData.RelativeTimestamp);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], GaiData.SequenceNumber);
        byteCount += sizeof(GaiData.SequenceNumber);
        //size = 16
        
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
            ble_SendNotification(CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE, Message, MessageSize);
        }
    }
}


uint8 getPresentGaidFields(uint8 *PresentData)
{
    uint8 length = ALWAYS_PRESENT_GAID_FIELD_LENGTH;
    
    if(CHECK_BIT(GaiData.Flags[0], NORMAL_WALKING_ENERGY_EXPENDITURE_PER_HOUR_PRESENT) &&
       CHECK_BIT(pamFeature, NORMAL_WALKING_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.NormalWalkingEnergyExpenditurePerHour);
        length += sizeof(GaiData.NormalWalkingEnergyExpenditurePerHour);
    }
    if(CHECK_BIT(GaiData.Flags[0], INTENSITY_ENERGY_EXPENDITURE_PER_HOUR_PRESENT) &&
       CHECK_BIT(pamFeature, INTENSITY_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.IntensityEnergyExpenditurePerHour);
        length += sizeof(GaiData.IntensityEnergyExpenditurePerHour);
    }
    if(CHECK_BIT(GaiData.Flags[0], TOTAL_ENERGY_EXPENDITURE_PER_HOUR_PRESENT) &&
       CHECK_BIT(pamFeature, TOTAL_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.TotalEnergyExpenditurePerHour);
        length += sizeof(GaiData.TotalEnergyExpenditurePerHour);
    }
    if(CHECK_BIT(GaiData.Flags[0], FAT_BURNED_PER_HOUR_PRESENT) &&
       CHECK_BIT(pamFeature, FAT_BURNED_PER_HOUR_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.FatBurnedPerHour);
        length += sizeof(GaiData.FatBurnedPerHour);
    }
    if(CHECK_BIT(GaiData.Flags[0], METABOLIC_EQUIVALENT_PRESENT) &&
       CHECK_BIT(pamFeature, METABOLIC_EQUIVALENT_SUPPORTED))
    {
        PresentData[length] = GaiData.MetabolicEquivalent;
        length += sizeof(GaiData.MetabolicEquivalent);
    }
    if(CHECK_BIT(GaiData.Flags[0], SPEED_PRESENT) &&
       CHECK_BIT(pamFeature, SPEED_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.Speed);
        length += sizeof(GaiData.Speed);
    }
    if(CHECK_BIT(GaiData.Flags[0], MOTION_CADENCE_PRESENT) &&
       CHECK_BIT(pamFeature, MOTION_CADENCE_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.MotionCadence);
        length += sizeof(GaiData.MotionCadence);
    }
    if(CHECK_BIT(GaiData.Flags[0], ELEVATION_PRESENT) &&
       CHECK_BIT(pamFeature, ELEVATION_SUPPORTED))
    {
        PresentData[length] = GaiData.Elevation[0];
        PresentData[length + 1] = GaiData.Elevation[1];
        PresentData[length + 2] = GaiData.Elevation[2];
        length += sizeof(GaiData.Elevation);
    }
    if(CHECK_BIT(GaiData.Flags[1], ACTIVITY_COUNT_PER_MINUTE_PRESENT - ONE_BYTE) &&
       CHECK_BIT(pamFeature, ACTIVITY_COUNT_PER_MINUTE_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.ActivityCountPerMinute);
        length += sizeof(GaiData.ActivityCountPerMinute);
    }
    if(CHECK_BIT(GaiData.Flags[1], ACTIVITY_LEVEL_PRESENT - ONE_BYTE) &&
       CHECK_BIT(pamFeature, ACTIVITY_LEVEL_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.ActivityLevel);
        length += sizeof(GaiData.ActivityLevel);
    }
    if(CHECK_BIT(GaiData.Flags[1], ACTIVITY_TYPE_PRESENT - ONE_BYTE) &&
       CHECK_BIT(pamFeature, ACTIVITY_TYPE_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GaiData.ActivityType);
        length += sizeof(GaiData.ActivityType);
    }
    
    return length;
}

/*
void UpdateGaid2(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&GaiData;
    tempHandle.value.len = sizeof(GaiData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    //buffer contains messages FIFO
    //First build the messages and save them in the buffer
    //send the messages from the fifo buffer
    
    if(generalinstantaneousdataNotify)
    {
        uint8 byteCount = 0u;
        uint8 messageCount = 0;
        
        uint8  PresentData[PAM_MAX_MESSAGE_DATA_SIZE];
        uint8  PresentDataBuffer[10][PAM_MAX_MESSAGE_DATA_SIZE];
        
        uint8  PresentDataSize[10];
        
        PresentData[byteCount++] = GaiData.Header;
        
        PresentData[byteCount++] = GaiData.Flags[0];
        PresentData[byteCount++] = GaiData.Flags[1];
        PresentData[byteCount++] = GaiData.Flags[2];
        
        CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.SessionID);
        byteCount += 2u;
        
        CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.SubSessionID);
        byteCount += 2u;
        
        CyBle_Set32ByPtr(&PresentData[byteCount], GaiData.RelativeTimestamp);
        byteCount += 4u;
        //12
        
        CyBle_Set32ByPtr(&PresentData[byteCount], GaiData.SequenceNumber);
        byteCount += 4u;
        //16
        
        
        if(CHECK_BIT(pamFeature, NORMAL_WALKING_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.NormalWalkingEnergyExpenditurePerHour)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.NormalWalkingEnergyExpenditurePerHour);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, INTENSITY_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.IntensityEnergyExpenditurePerHour)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.IntensityEnergyExpenditurePerHour);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, TOTAL_ENERGY_EXPENDITURE_PER_HOUR_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.TotalEnergyExpenditurePerHour)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.TotalEnergyExpenditurePerHour);
            byteCount += 2u;            
        }
        if(CHECK_BIT(pamFeature, FAT_BURNED_PER_HOUR_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.FatBurnedPerHour)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.FatBurnedPerHour);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, METABOLIC_EQUIVALENT_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.MetabolicEquivalent)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            PresentData[byteCount++] = GaiData.MetabolicEquivalent;
        }
        if(CHECK_BIT(pamFeature, SPEED_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.Speed)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.Speed);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, MOTION_CADENCE_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.MotionCadence)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.MotionCadence);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, ELEVATION_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.Elevation)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            PresentData[byteCount++] = GaiData.Elevation[0];
            PresentData[byteCount++] = GaiData.Elevation[1];
            PresentData[byteCount++] = GaiData.Elevation[2];
        }
        if(CHECK_BIT(pamFeature, ACTIVITY_COUNT_PER_MINUTE_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.ActivityCountPerMinute)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.ActivityCountPerMinute);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, ACTIVITY_LEVEL_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.ActivityLevel)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.ActivityLevel);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, ACTIVITY_TYPE_SUPPORTED))
        {
            if(checkMaxPresentDataSizeReached(byteCount, sizeof(GaiData.ActivityType)))
            {
                addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&PresentData[byteCount], GaiData.ActivityType);
            byteCount += 2u;
        }
        
        addToBuffer(PresentDataBuffer[messageCount], &PresentDataSize[messageCount], PresentData, &messageCount, &byteCount);

        setPresentDataHeader(PresentDataBuffer, messageCount);
        
        if(CyBle_GattGetBusStatus() == CYBLE_STACK_STATE_FREE)
        {
            for(int i = 0; i < messageCount; i++)
            {
                ble_SendNotification(CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE, PresentDataBuffer[i], PresentDataSize[i]);
                DBG_PRINTF("Sizeof message: %i \r\n", PresentDataSize[i]);
            }
        }
    }
}
*/
/* [] END OF FILE */
