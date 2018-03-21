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

#include "pamGasd.h"

void UpdateGasd(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_INSTANTANEOUS_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&GasData;
    tempHandle.value.len = sizeof(GasData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(generalsummarydataIndicate)
    {
        uint8 PresentData[sizeof(GasData)];
        
        uint8 presentDataLength = getPresentGasdFields(PresentData);
        DBG_PRINTF("GENERAL ACTIVITY SUMMARY DATA presentdatalength = %i\r\n", presentDataLength);
        uint8 N = calculateNumberOfMessages(presentDataLength);
        DBG_PRINTF("GENERAL ACTIVITY SUMMARY DATA N = %i\r\n", N);
        
        uint8 byteCount = 1u; //Starts at 1 because 0 is the header field
        
        CyBle_Set32ByPtr(&PresentData[byteCount], GasData.Flags);
        byteCount += sizeof(GasData.Flags);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], GasData.SessionID);
        byteCount += sizeof(GasData.SessionID);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], GasData.SubSessionID);
        byteCount += sizeof(GasData.SubSessionID);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], GasData.RelativeTimestamp);
        byteCount += sizeof(GasData.RelativeTimestamp);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], GasData.SequenceNumber);
        byteCount += sizeof(GasData.SequenceNumber);
        //size = 17
        
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
            ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE, Message, MessageSize);
        }
    }
}

uint8 getPresentGasdFields(uint8 *PresentData)
{
    uint8 length = ALWAYS_PRESENT_GASD_FIELD_LENGTH;
    
    if(CHECK_BIT(GasData.Flags, NORMAL_WALKING_ENERGY_EXPENDITURE_PRESENT) &&
       CHECK_BIT(pamFeature, NORMAL_WALKING_ENERGY_EXPENDITURE_SUPPORTED))
    {
        CyBle_Set32ByPtr(&PresentData[length], GasData.NormalWalkingEnergyExpenditure);
        length += sizeof(GasData.NormalWalkingEnergyExpenditure);
    }
    if(CHECK_BIT(GasData.Flags, INTENSITY_ENERGY_EXPENDITURE_PRESENT) &&
       CHECK_BIT(pamFeature, INTENSITY_ENERGY_EXPENDITURE_SUPPORTED))
    {
        CyBle_Set32ByPtr(&PresentData[length], GasData.IntensityEnergyExpenditure);
        length += sizeof(GasData.IntensityEnergyExpenditure);
    }
    if(CHECK_BIT(GasData.Flags, TOTAL_ENERGY_EXPENDITURE_PRESENT) &&
       CHECK_BIT(pamFeature, TOTAL_ENERGY_EXPENDITURE_SUPPORTED))
    {
        CyBle_Set32ByPtr(&PresentData[length], GasData.TotalEnergyExpenditure);
        length += sizeof(GasData.TotalEnergyExpenditure);
    }
    if(CHECK_BIT(GasData.Flags, FAT_BURNED_PRESENT) &&
       CHECK_BIT(pamFeature, FAT_BURNED_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GasData.FatBurned);
        length += sizeof(GasData.FatBurned);
    }
    if(CHECK_BIT(pamFeature, METABOLIC_EQUIVALENT_SUPPORTED))
    {
        if(CHECK_BIT(GasData.Flags, MINIMUM_METABOLIC_EQUIVALENT_PRESENT))
        {
            PresentData[length] = GasData.MinimumMetabolicEquivalent;
            length += sizeof(GasData.MinimumMetabolicEquivalent);
        }
        if(CHECK_BIT(GasData.Flags, MAXIMUM_METABOLIC_EQUIVALENT_PRESENT))
        {
            PresentData[length] = GasData.MaximumMetabolicEquivalent;
            length += sizeof(GasData.MaximumMetabolicEquivalent);
        }
        if(CHECK_BIT(GasData.Flags, AVERAGE_METABOLIC_EQUIVALENT_PRESENT))
        {
            PresentData[length] = GasData.AverageMetabolicEquivalent;
            length += sizeof(GasData.AverageMetabolicEquivalent);
        }
    }
    if(CHECK_BIT(GasData.Flags, DISTANCE_PRESENT) &&
       CHECK_BIT(pamFeature, DISTANCE_SUPPORTED))
    {
        PresentData[length] = GasData.Distance[0];
        PresentData[length + 1] = GasData.Distance[1];
        PresentData[length + 2] = GasData.Distance[2];
        length += sizeof(GasData.Distance);
    }
    if(CHECK_BIT(pamFeature, SPEED_SUPPORTED))
    {
        if(CHECK_BIT(GasData.Flags, MINIMUM_SPEED_PRESENT))
        {
            PresentData[length] = GasData.MinimumSpeed;
            length += sizeof(GasData.MinimumSpeed);
        }
        if(CHECK_BIT(GasData.Flags, MAXIMUM_SPEED_PRESENT))
        {
            PresentData[length] = GasData.MaximumSpeed;
            length += sizeof(GasData.MaximumSpeed);
        }
        if(CHECK_BIT(GasData.Flags, AVERAGE_SPEED_PRESENT))
        {
            PresentData[length] = GasData.AverageSpeed;
            length += sizeof(GasData.AverageSpeed);
        }
    }
    if(CHECK_BIT(GasData.Flags, DURATION_OF_NORMAL_WALKING_EPISODES_PRESENT) &&
       CHECK_BIT(pamFeature, DURATION_OF_NORMAL_WALKING_EPISODES_SUPPORTED))
    {
        PresentData[length] = GasData.DurationOfNormalWalkingEpisodes[0];
        PresentData[length + 1] = GasData.DurationOfNormalWalkingEpisodes[1];
        PresentData[length + 2] = GasData.DurationOfNormalWalkingEpisodes[2];
        length += sizeof(GasData.DurationOfNormalWalkingEpisodes);
    }
    if(CHECK_BIT(GasData.Flags, DURATION_OF_INTENSITY_WALKING_EPISODES_PRESENT) &&
       CHECK_BIT(pamFeature, DURATION_OF_INTENSITY_WALKING_EPISODES_SUPPORTED))
    {
        PresentData[length] = GasData.DurationOfIntensityWalkingEpisodes[0];
        PresentData[length + 1] = GasData.DurationOfIntensityWalkingEpisodes[1];
        PresentData[length + 2] = GasData.DurationOfIntensityWalkingEpisodes[2];
        length += sizeof(GasData.DurationOfIntensityWalkingEpisodes);
    }
    if(CHECK_BIT(pamFeature, MOTION_CADENCE_SUPPORTED))
    {
        if(CHECK_BIT(GasData.Flags, MINIMUM_MOTION_CADENCE_PRESENT))
        {
            PresentData[length] = GasData.MinimumMotionCadence;
            length += sizeof(GasData.MinimumMotionCadence);
        }
        if(CHECK_BIT(GasData.Flags, MAXIMUM_MOTION_CADENCE_PRESENT))
        {
            PresentData[length] = GasData.MaximumMotionCadence;
            length += sizeof(GasData.MaximumMotionCadence);
        }
        if(CHECK_BIT(GasData.Flags, AVERAGE_MOTION_CADENCE_PRESENT))
        {
            PresentData[length] = GasData.AverageMotionCadence;
            length += sizeof(GasData.AverageMotionCadence);
        }
    }
    if(CHECK_BIT(GasData.Flags, FLOORS_PRESENT) &&
       CHECK_BIT(pamFeature, FLOORS_SUPPORTED))
    {
        PresentData[length] = GasData.Floors;
        length += sizeof(GasData.Floors);
    }
    if(CHECK_BIT(GasData.Flags, POSITIVE_ELEVATION_GAIN_PRESENT) &&
       CHECK_BIT(pamFeature, POSITIVE_ELEVATION_GAIN_SUPPORTED))
    {
        PresentData[length] = GasData.PositiveElevationGain[0];
        PresentData[length + 1] = GasData.PositiveElevationGain[1];
        PresentData[length + 2] = GasData.PositiveElevationGain[2];
        length += sizeof(GasData.PositiveElevationGain);
    }
    if(CHECK_BIT(GasData.Flags, NEGATIVE_ELEVATION_GAIN_PRESENT) &&
       CHECK_BIT(pamFeature, NEGATIVE_ELEVATION_GAIN_SUPPORTED))
    {
        PresentData[length] = GasData.NegativeElevationGain[0];
        PresentData[length + 1] = GasData.NegativeElevationGain[1];
        PresentData[length + 2] = GasData.NegativeElevationGain[2];
        length += sizeof(GasData.NegativeElevationGain);
    }
    if(CHECK_BIT(GasData.Flags, ACTIVITY_COUNT_PRESENT) &&
       CHECK_BIT(pamFeature, ACTIVITY_COUNT_SUPPORTED))
    {
        CyBle_Set32ByPtr(&PresentData[length], GasData.ActivityCount);
        length += sizeof(GasData.ActivityCount);
    }
    if(CHECK_BIT(pamFeature, ACTIVITY_LEVEL_SUPPORTED))
    {
        if(CHECK_BIT(GasData.Flags, MINIMUM_ACTIVITY_LEVEL_PRESENT))
        {
            PresentData[length] = GasData.MinimumActivityLevel;
            length += sizeof(GasData.MinimumActivityLevel);
        }
        if(CHECK_BIT(GasData.Flags, MAXIMUM_ACTIVITY_LEVEL_PRESENT))
        {
            PresentData[length] = GasData.MaximumActivityLevel;
            length += sizeof(GasData.MaximumActivityLevel);
        }
        if(CHECK_BIT(GasData.Flags, AVERAGE_ACTIVITY_LEVEL_PRESENT))
        {
            PresentData[length] = GasData.AverageActivityLevel;
            length += sizeof(GasData.AverageActivityLevel);
        }
    }
    if(CHECK_BIT(GasData.Flags, AVERAGE_ACTIVITY_TYPE_PRESENT) &&
       CHECK_BIT(pamFeature, ACTIVITY_TYPE_SUPPORTED))
    {
        CyBle_Set16ByPtr(&PresentData[length], GasData.AverageActivityType);
        length += sizeof(GasData.AverageActivityType);
    }
    if(CHECK_BIT(GasData.Flags, WORN_DURATION_PRESENT) &&
       CHECK_BIT(pamFeature, WORN_DURATION_SUPPORTED))
    {
        PresentData[length] = GasData.WornDuration[0];
        PresentData[length + 1] = GasData.WornDuration[1];
        PresentData[length + 2] = GasData.WornDuration[2];
        length += sizeof(GasData.WornDuration);
    }
    
    return length;
}

//old protocol//
    /*void UpdateGasd2(void)
{
    CYBLE_GATTS_HANDLE_VALUE_IND_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&GasData;
    tempHandle.value.len = sizeof(GasData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(generalsummarydataIndicate == 2)
    {
        uint8 byteCount = 0u;
        uint8 messageCount = 0;
        
        uint8  Message[PAM_MAX_MESSAGE_DATA_SIZE];
        uint8  MessageBuffer[20][PAM_MAX_MESSAGE_DATA_SIZE];
        
        uint8  MessageSize[20];
        
        Message[byteCount++] = GasData.Header;
        
        
        if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.Flags)))
        {
            addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
        }
        CyBle_Set32ByPtr(&Message[byteCount], GasData.Flags);
        byteCount += 4u;
        
        if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.SessionID)))
        {
            addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
        }
        CyBle_Set16ByPtr(&Message[byteCount], GasData.SessionID);
        byteCount += 2u;
        
        if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.SubSessionID)))
        {
            addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
        }
        CyBle_Set32ByPtr(&Message[byteCount], GasData.SubSessionID);
        byteCount += 2u;
        
        if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.RelativeTimestamp)))
        {
            addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
        }
        CyBle_Set32ByPtr(&Message[byteCount], GasData.RelativeTimestamp);
        byteCount += 4u;
        
        if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.SequenceNumber)))
        {
            addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
        }
        CyBle_Set32ByPtr(&Message[byteCount], GasData.SequenceNumber);
        byteCount += 4u;
       ///<- Use this to test more notifications (put the PAM_MAX_MESSAGE_SIZE to lower value)
        
        CyBle_Set32ByPtr(&Message[byteCount], GasData.Flags);
        byteCount += 4u;
        
        CyBle_Set16ByPtr(&Message[byteCount], GasData.SessionID);
        byteCount += 2u;
        
        CyBle_Set16ByPtr(&Message[byteCount], GasData.SubSessionID);
        byteCount += 2u;
        
        CyBle_Set32ByPtr(&Message[byteCount], GasData.RelativeTimestamp);
        byteCount += 4u;
        
        CyBle_Set32ByPtr(&Message[byteCount], GasData.SequenceNumber);
        byteCount += 4u;
        
        if(CHECK_BIT(pamFeature, NORMAL_WALKING_ENERGY_EXPENDITURE_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.NormalWalkingEnergyExpenditure)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set32ByPtr(&Message[byteCount], GasData.NormalWalkingEnergyExpenditure);
            byteCount += 4u;
        }
        if(CHECK_BIT(pamFeature, INTENSITY_ENERGY_EXPENDITURE_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.IntensityEnergyExpenditure)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set32ByPtr(&Message[byteCount], GasData.IntensityEnergyExpenditure);
            byteCount += 4u;
        }
        if(CHECK_BIT(pamFeature, TOTAL_ENERGY_EXPENDITURE_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.TotalEnergyExpenditure)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set32ByPtr(&Message[byteCount], GasData.TotalEnergyExpenditure);
            byteCount += 4u;
        }
        if(CHECK_BIT(pamFeature, FAT_BURNED_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.FatBurned)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.FatBurned);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, METABOLIC_EQUIVALENT_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MinimumMetabolicEquivalent)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.MinimumMetabolicEquivalent;
            
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MaximumMetabolicEquivalent)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.MaximumMetabolicEquivalent;
            
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.AverageMetabolicEquivalent)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.AverageMetabolicEquivalent;
        }
        if(CHECK_BIT(pamFeature, DISTANCE_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.Distance)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.Distance;
            Message[byteCount++] = GasData.Distance[1];
            Message[byteCount++] = GasData.Distance[2];
        }
        if(CHECK_BIT(pamFeature, SPEED_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MinimumSpeed)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MaximumSpeed)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.AverageSpeed)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
        }
        if(CHECK_BIT(pamFeature, DURATION_OF_NORMAL_WALKING_EPISODES_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.DurationOfNormalWalkingEpisodes)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.DurationOfNormalWalkingEpisodes;
            Message[byteCount++] = GasData.DurationOfNormalWalkingEpisodes[1];
            Message[byteCount++] = GasData.DurationOfNormalWalkingEpisodes[2];
        }
        if(CHECK_BIT(pamFeature, DURATION_OF_INTENSITY_WALKING_EPISODES_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.DurationOfIntensityWalkingEpisodes)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.DurationOfIntensityWalkingEpisodes;
            Message[byteCount++] = GasData.DurationOfIntensityWalkingEpisodes[1];
            Message[byteCount++] = GasData.DurationOfIntensityWalkingEpisodes[2];
        }
        if(CHECK_BIT(pamFeature, MOTION_CADENCE_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MinimumMotionCadence)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.MinimumMotionCadence);
            byteCount += 2u;
            
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MaximumMotionCadence)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.MaximumMotionCadence);
            byteCount += 2u;
            
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.AverageMotionCadence)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.AverageMotionCadence);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, FLOORS_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.Floors)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.Floors;
        }
        if(CHECK_BIT(pamFeature, ELEVATION_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.PositiveElevationGain)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.PositiveElevationGain;
            Message[byteCount++] = GasData.PositiveElevationGain[1];
            Message[byteCount++] = GasData.PositiveElevationGain[2];
            
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.NegativeElevationGain)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount++] = GasData.NegativeElevationGain;
            Message[byteCount++] = GasData.NegativeElevationGain[1];
            Message[byteCount++] = GasData.NegativeElevationGain[2];
        }
        if(CHECK_BIT(pamFeature, ACTIVITY_COUNT_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.ActivityCount)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set32ByPtr(&Message[byteCount], GasData.ActivityCount);
            byteCount += 4u;
        }
        if(CHECK_BIT(pamFeature, ACTIVITY_LEVEL_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MinimumActivityLevel)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.MinimumActivityLevel);
            byteCount += 2u;
            
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.MaximumActivityLevel)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.MaximumActivityLevel);
            byteCount += 2u;
            
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.AverageActivityLevel)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.AverageActivityLevel);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, ACTIVITY_TYPE_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.AverageActivityType)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            CyBle_Set16ByPtr(&Message[byteCount], GasData.AverageActivityType);
            byteCount += 2u;
        }
        if(CHECK_BIT(pamFeature, WORN_DURATION_SUPPORTED))
        {
            if(checkMaxMessageSizeReached(byteCount, sizeof(GasData.WornDuration)))
            {
                addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);
            }
            Message[byteCount] = GasData.WornDuration;
            Message[byteCount] = GasData.WornDuration[1];
            Message[byteCount] = GasData.WornDuration[2];
        }
        
        addToBuffer(MessageBuffer[messageCount], &MessageSize[messageCount], Message, &messageCount, &byteCount);

        setMessageHeader(MessageBuffer, messageCount);
        
        if(CyBle_GattGetBusStatus() == CYBLE_STACK_STATE_FREE)
        {
            for(int i = 0; i < messageCount; i++)
            {
                ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_GENERAL_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE, MessageBuffer[i], MessageSize[i]);
            }
        }
    }
}
*/
////////////////

/* [] END OF FILE */
