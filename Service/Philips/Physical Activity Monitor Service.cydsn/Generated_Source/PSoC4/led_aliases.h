/*******************************************************************************
* File Name: led.h  
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

#if !defined(CY_PINS_led_ALIASES_H) /* Pins led_ALIASES_H */
#define CY_PINS_led_ALIASES_H

#include "cytypes.h"
#include "cyfitter.h"
#include "cypins.h"


/***************************************
*              Constants        
***************************************/
#define led_0			(led__0__PC)
#define led_0_PS		(led__0__PS)
#define led_0_PC		(led__0__PC)
#define led_0_DR		(led__0__DR)
#define led_0_SHIFT	(led__0__SHIFT)
#define led_0_INTR	((uint16)((uint16)0x0003u << (led__0__SHIFT*2u)))

#define led_1			(led__1__PC)
#define led_1_PS		(led__1__PS)
#define led_1_PC		(led__1__PC)
#define led_1_DR		(led__1__DR)
#define led_1_SHIFT	(led__1__SHIFT)
#define led_1_INTR	((uint16)((uint16)0x0003u << (led__1__SHIFT*2u)))

#define led_2			(led__2__PC)
#define led_2_PS		(led__2__PS)
#define led_2_PC		(led__2__PC)
#define led_2_DR		(led__2__DR)
#define led_2_SHIFT	(led__2__SHIFT)
#define led_2_INTR	((uint16)((uint16)0x0003u << (led__2__SHIFT*2u)))

#define led_INTR_ALL	 ((uint16)(led_0_INTR| led_1_INTR| led_2_INTR))
#define led_red			(led__red__PC)
#define led_red_PS		(led__red__PS)
#define led_red_PC		(led__red__PC)
#define led_red_DR		(led__red__DR)
#define led_red_SHIFT	(led__red__SHIFT)
#define led_red_INTR	((uint16)((uint16)0x0003u << (led__0__SHIFT*2u)))

#define led_green			(led__green__PC)
#define led_green_PS		(led__green__PS)
#define led_green_PC		(led__green__PC)
#define led_green_DR		(led__green__DR)
#define led_green_SHIFT	(led__green__SHIFT)
#define led_green_INTR	((uint16)((uint16)0x0003u << (led__1__SHIFT*2u)))

#define led_blue			(led__blue__PC)
#define led_blue_PS		(led__blue__PS)
#define led_blue_PC		(led__blue__PC)
#define led_blue_DR		(led__blue__DR)
#define led_blue_SHIFT	(led__blue__SHIFT)
#define led_blue_INTR	((uint16)((uint16)0x0003u << (led__2__SHIFT*2u)))


#endif /* End Pins led_ALIASES_H */


/* [] END OF FILE */
