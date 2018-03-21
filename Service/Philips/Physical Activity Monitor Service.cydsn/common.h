/*******************************************************************************
* File Name: debug.h
*
* Version 1.0
*
* Description:
*  Contains the function prototypes and constants available to the example
*  project.
*
********************************************************************************
* Copyright 2016, Cypress Semiconductor Corporation.  All rights reserved.
* You may use this file only in accordance with the license, terms, conditions,
* disclaimers, and limitations in the end user license agreement accompanying
* the software package with which this file was provided.
*******************************************************************************/


#ifndef COMMON_H
#define COMMON_H

#include "project.h"    
#include "pams.h"
#include "pamFeatures.h"  
#include "pamSessDescr.h"
#include <math.h>  
#include <stdio.h>    

    
/***************************************
*       Data Struct Definition
***************************************/

//hoi
/***************************************
* Conditional Compilation Parameters
***************************************/
#define YES                                 (1u)
#define NO                                  (0u)

#define ENABLED                             (1u)
#define DISABLED                            (0u)

#define DEBUG_UART_ENABLED                  (ENABLED)

/***************************************
*        API Constants
***************************************/
#define LED_ON                              (0u)
#define LED_OFF                             (1u)
    
/***************************************
*        Constants
***************************************/
    
#define MAX_SESSIONS                         (25u)

/***************************************
*        Function Prototypes
***************************************/

void InitializePhisicalActivityMonitor(void);
void HandleLeds(void);
void PhysicalActivityMonitorProfileHandler(void);
void SimulateValues(uint16 *SessionID, uint16 *SubSessionID);

void PrintState(void);
void PrintApiResult(void);

extern CYBLE_API_RESULT_T apiResult;

    
/***************************************
*        Macros
***************************************/
#if (DEBUG_UART_ENABLED == ENABLED)
    #define DBG_PRINTF(...)             (printf(__VA_ARGS__))
#else
    #define DBG_PRINTF(...)
#endif /* (DEBUG_UART_ENABLED == ENABLED) */

#define PACK_U16(loByte, hiByte)            ((uint16) (((uint16)loByte) | (((uint16) hiByte) << BYTE_SHIFT)))
#define PACK_U24(loByte, miByte ,hiByte)    ((uint16) (((uint16)loByte) | (((uint16) hiByte) << BYTE_SHIFT)))

/***************************************
*        Enums
***************************************/
typedef enum
{
    ENQUIRE_SESSIONS = 0x01,
    ENQUIRE_SUB_SESSIONS = 0x02,
    GET_ENDED_SESSION_DATA = 0x03,
    START_SESSION_SUB_SESSION = 0x04,
    STOP_SESSION = 0x05,
    DELETE_SESSION = 0x06
    
}PAM_CONTROL_POINT_WRITE_REQUEST;

#endif

/* [] END OF FILE */
