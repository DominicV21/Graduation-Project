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

#include "pamScasd.h"

void UpdateScasd(void)
{
    CYBLE_GATTS_HANDLE_VALUE_NTF_T tempHandle;
    
    tempHandle.attrHandle = CYBLE_PHYSICAL_ACTIVITY_MONITOR_STEP_COUNTER_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE;
    tempHandle.value.val = (uint8 *)&ScasData;
    tempHandle.value.len = sizeof(ScasData);
    CyBle_GattsWriteAttributeValue(&tempHandle, 0, &cyBle_connHandle, 0);
    
    if(stepcounteractivitysummarydataIndicate)
    {
        uint8 PresentData[sizeof(ScasData)];
        
        uint8 presentDataLength = getPresentScasdFields(PresentData);
        DBG_PRINTF("STEP COUNTER ACTIVITY SUMMARY DATA presentdatalength = %i\r\n", presentDataLength);
        uint8 N = calculateNumberOfMessages(presentDataLength);
        DBG_PRINTF("STEP COUNTER ACTIVITY SUMMARY DATA N = %i\r\n", N);
        
        uint8 byteCount = 1u;//Starts at 1 because 0 is the header field
        
        PresentData[byteCount] = ScasData.Flags;
        byteCount += sizeof(ScasData.Flags);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], ScasData.SessionID);
        byteCount += sizeof(ScasData.SessionID);
        
        CyBle_Set16ByPtr(&PresentData[byteCount], ScasData.SubSessionID);
        byteCount += sizeof(ScasData.SubSessionID);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], ScasData.RelativeTimestamp);
        byteCount += sizeof(ScasData.RelativeTimestamp);
        
        CyBle_Set32ByPtr(&PresentData[byteCount], ScasData.SequenceNumber);
        byteCount += sizeof(ScasData.SequenceNumber);
        //size = 14
        
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
            ble_SendIndication(CYBLE_PHYSICAL_ACTIVITY_MONITOR_STEP_COUNTER_ACTIVITY_SUMMARY_DATA_CHAR_HANDLE, Message, MessageSize);
        }
    }
}


uint8 getPresentScasdFields(uint8 *PresentData)
{
    uint8 length = ALWAYS_PRESENT_SCASD_FIELD_LENGTH;
    
    if(CHECK_BIT(ScasData.Flags, 1/*NORMAL WALKING STEPS PRESENT*/) &&
       CHECK_BIT(pamFeature, NORMAL_WALKING_STEPS_SUPPORTED))
    {
        PresentData[length] = ScasData.NormalWalkingSteps[0];
        PresentData[length+1] = ScasData.NormalWalkingSteps[1];
        PresentData[length+2] = ScasData.NormalWalkingSteps[2];
        length += sizeof(ScasData.NormalWalkingSteps);
    }
    if(CHECK_BIT(ScasData.Flags, 1/*INTENSITY STEPS PRESENT*/) &&
       CHECK_BIT(pamFeature, INTENSITY_STEPS_SUPPORTED))
    {
        PresentData[length] = ScasData.IntensitySteps[0];
        PresentData[length+1] = ScasData.IntensitySteps[1];
        PresentData[length+2] = ScasData.IntensitySteps[2];
        length += sizeof(ScasData.IntensitySteps);
    }
    if(CHECK_BIT(ScasData.Flags, 1/*FLOOR STEPS PRESENT*/) &&
       CHECK_BIT(pamFeature, FLOOR_STEPS_SUPPORTED))
    {
        PresentData[length] = ScasData.FloorSteps[0];
        PresentData[length+1] = ScasData.FloorSteps[1];
        PresentData[length+2] = ScasData.FloorSteps[2];
        length += sizeof(ScasData.FloorSteps);
    }
    if(CHECK_BIT(ScasData.Flags, 1/*Distance PRESENT*/) &&
       CHECK_BIT(pamFeature, DISTANCE_SUPPORTED))
    {
        PresentData[length] = ScasData.Distance[0];
        PresentData[length+1] = ScasData.Distance[1];
        PresentData[length+2] = ScasData.Distance[2];
        length += sizeof(ScasData.Distance);
    }
    if(CHECK_BIT(ScasData.Flags, 1/*WORN DURATION PRESENT*/) &&
       CHECK_BIT(pamFeature, WORN_DURATION_SUPPORTED))
    {
        PresentData[length] = ScasData.WornDuration[0];
        PresentData[length+1] = ScasData.WornDuration[1];
        PresentData[length+2] = ScasData.WornDuration[2];
        length += sizeof(ScasData.WornDuration);
    }
    
    return length;
}

/* [] END OF FILE */
