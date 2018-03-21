/*******************************************************************************
* File Name: connected_LED.h  
* Version 2.20
*
* Description:
*  This file contains the Alias definitions for Per-Pin APIs in cypins.h. 
*  Information on using these APIs can be found in the System Reference Guide.
*
* Note:
*
********************************************************************************
* Copyright 2008-2015, Cypress Semiconductor Corporation.  All rights reserved.
* You may use this file only in accordance with the license, terms, conditions, 
* disclaimers, and limitations in the end user license agreement accompanying 
* the software package with which this file was provided.
*******************************************************************************/

#if !defined(CY_PINS_connected_LED_ALIASES_H) /* Pins connected_LED_ALIASES_H */
#define CY_PINS_connected_LED_ALIASES_H

#include "cytypes.h"
#include "cyfitter.h"
#include "cypins.h"


/***************************************
*              Constants        
***************************************/
#define connected_LED_0			(connected_LED__0__PC)
#define connected_LED_0_PS		(connected_LED__0__PS)
#define connected_LED_0_PC		(connected_LED__0__PC)
#define connected_LED_0_DR		(connected_LED__0__DR)
#define connected_LED_0_SHIFT	(connected_LED__0__SHIFT)
#define connected_LED_0_INTR	((uint16)((uint16)0x0003u << (connected_LED__0__SHIFT*2u)))

#define connected_LED_INTR_ALL	 ((uint16)(connected_LED_0_INTR))


#endif /* End Pins connected_LED_ALIASES_H */


/* [] END OF FILE */
