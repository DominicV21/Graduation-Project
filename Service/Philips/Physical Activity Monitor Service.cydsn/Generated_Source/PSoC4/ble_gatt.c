/***************************************************************************//**
* \file CYBLE_gatt.c
* \version 3.30
* 
* \brief
*  This file contains the source code for the GATT API of the BLE Component.
* 
********************************************************************************
* \copyright
* Copyright 2014-2016, Cypress Semiconductor Corporation.  All rights reserved.
* You may use this file only in accordance with the license, terms, conditions,
* disclaimers, and limitations in the end user license agreement accompanying
* the software package with which this file was provided.
*******************************************************************************/


#include "ble_eventHandler.h"


/***************************************
* Global variables
***************************************/

CYBLE_STATE_T cyBle_state;

#if ((CYBLE_MODE_PROFILE) && (CYBLE_BONDING_REQUIREMENT == CYBLE_BONDING_YES))
    
#if(CYBLE_MODE_PROFILE)
    #if defined(__ARMCC_VERSION)
        CY_ALIGN(CYDEV_FLS_ROW_SIZE) const CY_BLE_FLASH_STORAGE cyBle_flashStorage CY_SECTION(".cy_checksum_exclude") =
    #elif defined (__GNUC__)
        const CY_BLE_FLASH_STORAGE cyBle_flashStorage CY_SECTION(".cy_checksum_exclude")
            CY_ALIGN(CYDEV_FLS_ROW_SIZE) =
    #elif defined (__ICCARM__)
        #pragma data_alignment=CY_FLASH_SIZEOF_ROW
        #pragma location=".cy_checksum_exclude"
        const CY_BLE_FLASH_STORAGE cyBle_flashStorage =
    #endif  /* (__ARMCC_VERSION) */
    {
        { 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u }, 
        {{
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
        },
        {
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
        },
        {
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
        },
        {
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
        },
        {
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
            0x00u, 0x00u,
        }}, 
        0x16u, /* CYBLE_GATT_DB_CCCD_COUNT */ 
        0x05u, /* CYBLE_GAP_MAX_BONDED_DEVICE */ 
    };
#endif /* (CYBLE_MODE_PROFILE) */

#endif  /* (CYBLE_MODE_PROFILE) && (CYBLE_BONDING_REQUIREMENT == CYBLE_BONDING_YES) */

#if(CYBLE_GATT_ROLE_SERVER)
    
    const CYBLE_GATTS_T cyBle_gatts =
{
    0x000Cu,    /* Handle of the GATT service */
    0x000Eu,    /* Handle of the Service Changed characteristic */
    0x000Fu,    /* Handle of the Client Characteristic Configuration descriptor */
};
    
    static uint8 cyBle_attValues[0x02D4u] = {
    /* Device Name */
    (uint8)'P', (uint8)'A', (uint8)'M', (uint8)'S',

    /* Appearance */
    0xC1u, 0x00u,

    /* Peripheral Preferred Connection Parameters */
    0x06u, 0x00u, 0x28u, 0x00u, 0x00u, 0x00u, 0xE8u, 0x03u,

    /* Central Address Resolution */
    0x00u,

    /* Resolvable Private Address Only */
    0x00u,

    /* Service Changed */
    0x00u, 0x00u, 0x00u, 0x00u,

    /* Physical Activity Monitor Feature */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'P', (uint8)'A', (uint8)'M', (uint8)' ', (uint8)'F', (uint8)'e', (uint8)'a', (uint8)'t', (uint8)'u',
    (uint8)'r', (uint8)'e', (uint8)'l', (uint8)'i', (uint8)'s', (uint8)'t',

    /* General Activity Instantaneous Data */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'G', (uint8)'e', (uint8)'n', (uint8)'e', (uint8)'r', (uint8)'a', (uint8)'l', (uint8)' ', (uint8)'A',
    (uint8)'c', (uint8)'t', (uint8)'i', (uint8)'v', (uint8)'i', (uint8)'t', (uint8)'y', (uint8)' ', (uint8)'I',
    (uint8)'n', (uint8)'s', (uint8)'t', (uint8)'a', (uint8)'n', (uint8)'t', (uint8)'a', (uint8)'n', (uint8)'e',
    (uint8)'o', (uint8)'u', (uint8)'s', (uint8)' ', (uint8)'D', (uint8)'a', (uint8)'t', (uint8)'a',

    /* General Activity Summary Data */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'G', (uint8)'e', (uint8)'n', (uint8)'e', (uint8)'r', (uint8)'a', (uint8)'l', (uint8)' ', (uint8)'A',
    (uint8)'c', (uint8)'t', (uint8)'i', (uint8)'v', (uint8)'i', (uint8)'t', (uint8)'y', (uint8)' ', (uint8)'S',
    (uint8)'u', (uint8)'m', (uint8)'m', (uint8)'a', (uint8)'r', (uint8)'y', (uint8)' ', (uint8)'D', (uint8)'a',
    (uint8)'t', (uint8)'a',

    /* CardioRespiratory Activity Instantaneous Data */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'C', (uint8)'a', (uint8)'r', (uint8)'d', (uint8)'i', (uint8)'o', (uint8)'R', (uint8)'e', (uint8)'s',
    (uint8)'p', (uint8)'i', (uint8)'r', (uint8)'a', (uint8)'t', (uint8)'o', (uint8)'r', (uint8)'y', (uint8)' ',
    (uint8)'I', (uint8)'n', (uint8)'s', (uint8)'t', (uint8)'a', (uint8)'n', (uint8)'t', (uint8)'a', (uint8)'n',
    (uint8)'e', (uint8)'o', (uint8)'u', (uint8)'s', (uint8)' ', (uint8)'D', (uint8)'a', (uint8)'t', (uint8)'a',

    /* CardioRespiratory Activity Summary Data */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'C', (uint8)'a', (uint8)'r', (uint8)'d', (uint8)'i', (uint8)'o', (uint8)'R', (uint8)'e', (uint8)'s',
    (uint8)'p', (uint8)'i', (uint8)'r', (uint8)'a', (uint8)'t', (uint8)'o', (uint8)'r', (uint8)'y', (uint8)' ',
    (uint8)'A', (uint8)'c', (uint8)'t', (uint8)'i', (uint8)'v', (uint8)'i', (uint8)'t', (uint8)'y', (uint8)' ',
    (uint8)'S', (uint8)'u', (uint8)'m', (uint8)'m', (uint8)'a', (uint8)'r', (uint8)'y', (uint8)' ', (uint8)'D',
    (uint8)'a', (uint8)'t', (uint8)'a',

    /* Step Counter Activity Summary Data */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'S', (uint8)'t', (uint8)'e', (uint8)'p', (uint8)' ', (uint8)'C', (uint8)'o', (uint8)'u', (uint8)'n',
    (uint8)'t', (uint8)'e', (uint8)'r', (uint8)' ', (uint8)'A', (uint8)'c', (uint8)'t', (uint8)'i', (uint8)'v',
    (uint8)'i', (uint8)'t', (uint8)'y', (uint8)' ', (uint8)'S', (uint8)'u', (uint8)'m', (uint8)'m', (uint8)'a',
    (uint8)'r', (uint8)'y', (uint8)' ', (uint8)'D', (uint8)'a', (uint8)'t', (uint8)'a',

    /* Sleep Activity Instantaneous Data */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'S', (uint8)'l', (uint8)'e', (uint8)'e', (uint8)'p', (uint8)' ', (uint8)'A', (uint8)'c', (uint8)'t',
    (uint8)'i', (uint8)'v', (uint8)'i', (uint8)'t', (uint8)'y', (uint8)' ', (uint8)'I', (uint8)'n', (uint8)'s',
    (uint8)'t', (uint8)'a', (uint8)'n', (uint8)'t', (uint8)'a', (uint8)'n', (uint8)'e', (uint8)'o', (uint8)'u',
    (uint8)'s', (uint8)' ', (uint8)'D', (uint8)'a', (uint8)'t', (uint8)'a',

    /* Sleep Activity Summary Data */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'S', (uint8)'l', (uint8)'e', (uint8)'e', (uint8)'p', (uint8)' ', (uint8)'A', (uint8)'c', (uint8)'t',
    (uint8)'i', (uint8)'v', (uint8)'i', (uint8)'t', (uint8)'y', (uint8)' ', (uint8)'S', (uint8)'u', (uint8)'m',
    (uint8)'m', (uint8)'a', (uint8)'r', (uint8)'y', (uint8)' ', (uint8)'D', (uint8)'a', (uint8)'t', (uint8)'a',

    /* Physical Activity Monitor Control Point */
    0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'P', (uint8)'h', (uint8)'y', (uint8)'s', (uint8)'i', (uint8)'c', (uint8)'a', (uint8)'l', (uint8)' ',
    (uint8)'A', (uint8)'c', (uint8)'t', (uint8)'i', (uint8)'v', (uint8)'i', (uint8)'t', (uint8)'y', (uint8)' ',
    (uint8)'M', (uint8)'o', (uint8)'n', (uint8)'i', (uint8)'t', (uint8)'o', (uint8)'r', (uint8)' ', (uint8)'C',
    (uint8)'o', (uint8)'n', (uint8)'t', (uint8)'r', (uint8)'o', (uint8)'l', (uint8)' ', (uint8)'P', (uint8)'o',
    (uint8)'i', (uint8)'n', (uint8)'t',

    /* Current Session */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'C', (uint8)'u', (uint8)'r', (uint8)'r', (uint8)'e', (uint8)'n', (uint8)'t', (uint8)' ', (uint8)'S',
    (uint8)'e', (uint8)'s', (uint8)'s', (uint8)'i', (uint8)'o', (uint8)'n',

    /* Session Descriptor */
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,

    /* Characteristic User Description */
    (uint8)'S', (uint8)'e', (uint8)'s', (uint8)'s', (uint8)'i', (uint8)'o', (uint8)'n', (uint8)' ', (uint8)'D',
    (uint8)'e', (uint8)'s', (uint8)'c', (uint8)'r', (uint8)'i', (uint8)'p', (uint8)'t', (uint8)'o', (uint8)'r',

};
#if(CYBLE_GATT_DB_CCCD_COUNT != 0u)
uint8 cyBle_attValuesCCCD[CYBLE_GATT_DB_CCCD_COUNT];
#endif /* CYBLE_GATT_DB_CCCD_COUNT != 0u */

const uint8 cyBle_attUuid128[][16u] = {
    /* connectionFeedback */
    { 0x00u, 0x00u, 0x3Eu, 0xB6u, 0xFCu, 0x96u, 0xB7u, 0x9Cu, 0x8Au, 0x4Fu, 0x38u, 0xEAu, 0x9Bu, 0x18u, 0x98u, 0xB5u },
    /* Physical Activity Monitor Feature */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x11u, 0x7Fu, 0x00u, 0x00u },
    /* General Activity Instantaneous Data */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x12u, 0x7Fu, 0x00u, 0x00u },
    /* General Activity Summary Data */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x13u, 0x7Fu, 0x00u, 0x00u },
    /* CardioRespiratory Activity Instantaneous Data */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x14u, 0x7Fu, 0x00u, 0x00u },
    /* CardioRespiratory Activity Summary Data */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x15u, 0x7Fu, 0x00u, 0x00u },
    /* Step Counter Activity Summary Data */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x16u, 0x7Fu, 0x00u, 0x00u },
    /* Sleep Activity Instantaneous Data */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x17u, 0x7Fu, 0x00u, 0x00u },
    /* Sleep Activity Summary Data */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x18u, 0x7Fu, 0x00u, 0x00u },
    /* Physical Activity Monitor Control Point */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x1Eu, 0x7Fu, 0x00u, 0x00u },
    /* Current Session */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x1Fu, 0x7Fu, 0x00u, 0x00u },
    /* Session Descriptor */
    { 0xFBu, 0x34u, 0x9Bu, 0x5Fu, 0x80u, 0x00u, 0x00u, 0x80u, 0x00u, 0x10u, 0x00u, 0x00u, 0x20u, 0x7Fu, 0x00u, 0x00u },
};

CYBLE_GATTS_ATT_GEN_VAL_LEN_T cyBle_attValuesLen[CYBLE_GATT_DB_ATT_VAL_COUNT] = {
    { 0x0004u, (void *)&cyBle_attValues[0] }, /* Device Name */
    { 0x0002u, (void *)&cyBle_attValues[4] }, /* Appearance */
    { 0x0008u, (void *)&cyBle_attValues[6] }, /* Peripheral Preferred Connection Parameters */
    { 0x0001u, (void *)&cyBle_attValues[14] }, /* Central Address Resolution */
    { 0x0001u, (void *)&cyBle_attValues[15] }, /* Resolvable Private Address Only */
    { 0x0004u, (void *)&cyBle_attValues[16] }, /* Service Changed */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[0] }, /* Client Characteristic Configuration */
    { 0x0010u, (void *)&cyBle_attUuid128[0] }, /* connectionFeedback UUID */
    { 0x0010u, (void *)&cyBle_attUuid128[1] }, /* Physical Activity Monitor Feature UUID */
    { 0x0008u, (void *)&cyBle_attValues[20] }, /* Physical Activity Monitor Feature */
    { 0x000Fu, (void *)&cyBle_attValues[28] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[2] }, /* General Activity Instantaneous Data UUID */
    { 0x0026u, (void *)&cyBle_attValues[43] }, /* General Activity Instantaneous Data */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[2] }, /* GeneralActivityInstantaneousDataCCCD */
    { 0x0023u, (void *)&cyBle_attValues[81] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[3] }, /* General Activity Summary Data UUID */
    { 0x004Du, (void *)&cyBle_attValues[116] }, /* General Activity Summary Data */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[4] }, /* GeneralActivitySummaryDataCCCD */
    { 0x001Du, (void *)&cyBle_attValues[193] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[4] }, /* CardioRespiratory Activity Instantaneous Data UUID */
    { 0x0018u, (void *)&cyBle_attValues[222] }, /* CardioRespiratory Activity Instantaneous Data */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[6] }, /* CardioRespiratoryAvtivityInstantaneousDataCCCD */
    { 0x0024u, (void *)&cyBle_attValues[246] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[5] }, /* CardioRespiratory Activity Summary Data UUID */
    { 0x003Cu, (void *)&cyBle_attValues[282] }, /* CardioRespiratory Activity Summary Data */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[8] }, /* CardioRespiratoryActivitySummaryDataCCCD */
    { 0x0027u, (void *)&cyBle_attValues[342] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[6] }, /* Step Counter Activity Summary Data UUID */
    { 0x001Du, (void *)&cyBle_attValues[381] }, /* Step Counter Activity Summary Data */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[10] }, /* StepCounterActivitySummaryDataCCCD */
    { 0x0022u, (void *)&cyBle_attValues[410] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[7] }, /* Sleep Activity Instantaneous Data UUID */
    { 0x001Au, (void *)&cyBle_attValues[444] }, /* Sleep Activity Instantaneous Data */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[12] }, /* SleepActivityInstantaneousDataCCCD */
    { 0x0021u, (void *)&cyBle_attValues[470] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[8] }, /* Sleep Activity Summary Data UUID */
    { 0x0044u, (void *)&cyBle_attValues[503] }, /* Sleep Activity Summary Data */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[14] }, /* SleepActivitySummaryDataCCCD */
    { 0x001Bu, (void *)&cyBle_attValues[571] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[9] }, /* Physical Activity Monitor Control Point UUID */
    { 0x0004u, (void *)&cyBle_attValues[598] }, /* Physical Activity Monitor Control Point */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[16] }, /* PhysicalActivityMonitorControlPointCCCD */
    { 0x0027u, (void *)&cyBle_attValues[602] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[10] }, /* Current Session UUID */
    { 0x0013u, (void *)&cyBle_attValues[641] }, /* Current Session */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[18] }, /* CurrentSessionCCCD */
    { 0x000Fu, (void *)&cyBle_attValues[660] }, /* Characteristic User Description */
    { 0x0010u, (void *)&cyBle_attUuid128[11] }, /* Session Descriptor UUID */
    { 0x001Fu, (void *)&cyBle_attValues[675] }, /* Session Descriptor */
    { 0x0002u, (void *)&cyBle_attValuesCCCD[20] }, /* SessionDescriptorCCCD */
    { 0x0012u, (void *)&cyBle_attValues[706] }, /* Characteristic User Description */
};

const CYBLE_GATTS_DB_T cyBle_gattDB[0x3Cu] = {
    { 0x0001u, 0x2800u /* Primary service                     */, 0x00000001u /*        */, 0x000Bu, {{0x1800u, NULL}}                           },
    { 0x0002u, 0x2803u /* Characteristic                      */, 0x00020001u /* rd     */, 0x0003u, {{0x2A00u, NULL}}                           },
    { 0x0003u, 0x2A00u /* Device Name                         */, 0x01020001u /* rd     */, 0x0003u, {{0x0004u, (void *)&cyBle_attValuesLen[0]}} },
    { 0x0004u, 0x2803u /* Characteristic                      */, 0x00020001u /* rd     */, 0x0005u, {{0x2A01u, NULL}}                           },
    { 0x0005u, 0x2A01u /* Appearance                          */, 0x01020001u /* rd     */, 0x0005u, {{0x0002u, (void *)&cyBle_attValuesLen[1]}} },
    { 0x0006u, 0x2803u /* Characteristic                      */, 0x00020001u /* rd     */, 0x0007u, {{0x2A04u, NULL}}                           },
    { 0x0007u, 0x2A04u /* Peripheral Preferred Connection Par */, 0x01020001u /* rd     */, 0x0007u, {{0x0008u, (void *)&cyBle_attValuesLen[2]}} },
    { 0x0008u, 0x2803u /* Characteristic                      */, 0x00020001u /* rd     */, 0x0009u, {{0x2AA6u, NULL}}                           },
    { 0x0009u, 0x2AA6u /* Central Address Resolution          */, 0x01020001u /* rd     */, 0x0009u, {{0x0001u, (void *)&cyBle_attValuesLen[3]}} },
    { 0x000Au, 0x2803u /* Characteristic                      */, 0x00020001u /* rd     */, 0x000Bu, {{0x2AC9u, NULL}}                           },
    { 0x000Bu, 0x2AC9u /* Resolvable Private Address Only     */, 0x01020001u /* rd     */, 0x000Bu, {{0x0001u, (void *)&cyBle_attValuesLen[4]}} },
    { 0x000Cu, 0x2800u /* Primary service                     */, 0x00000001u /*        */, 0x000Fu, {{0x1801u, NULL}}                           },
    { 0x000Du, 0x2803u /* Characteristic                      */, 0x00200001u /* ind    */, 0x000Fu, {{0x2A05u, NULL}}                           },
    { 0x000Eu, 0x2A05u /* Service Changed                     */, 0x01200000u /* ind    */, 0x000Fu, {{0x0004u, (void *)&cyBle_attValuesLen[5]}} },
    { 0x000Fu, 0x2902u /* Client Characteristic Configuration */, 0x010A0301u /* rd,wr  */, 0x000Fu, {{0x0002u, (void *)&cyBle_attValuesLen[6]}} },
    { 0x0010u, 0x2800u /* Primary service                     */, 0x08000001u /*        */, 0x0010u, {{0x0010u, (void *)&cyBle_attValuesLen[7]}} },
    { 0x0011u, 0x2800u /* Primary service                     */, 0x00000001u /*        */, 0x003Cu, {{0x7f10u, NULL}}                           },
    { 0x0012u, 0x2803u /* Characteristic                      */, 0x00020001u /* rd     */, 0x0014u, {{0x0010u, (void *)&cyBle_attValuesLen[8]}} },
    { 0x0013u, 0x7F11u /* Physical Activity Monitor Feature   */, 0x09020003u /* rd     */, 0x0014u, {{0x0008u, (void *)&cyBle_attValuesLen[9]}} },
    { 0x0014u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0014u, {{0x000Fu, (void *)&cyBle_attValuesLen[10]}} },
    { 0x0015u, 0x2803u /* Characteristic                      */, 0x00120001u /* rd,ntf */, 0x0018u, {{0x0010u, (void *)&cyBle_attValuesLen[11]}} },
    { 0x0016u, 0x7F12u /* General Activity Instantaneous Data */, 0x09120003u /* rd,ntf */, 0x0018u, {{0x0026u, (void *)&cyBle_attValuesLen[12]}} },
    { 0x0017u, 0x2902u /* GeneralActivityInstantaneousDataCCC */, 0x010A0301u /* rd,wr  */, 0x0017u, {{0x0002u, (void *)&cyBle_attValuesLen[13]}} },
    { 0x0018u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0018u, {{0x0023u, (void *)&cyBle_attValuesLen[14]}} },
    { 0x0019u, 0x2803u /* Characteristic                      */, 0x00220001u /* rd,ind */, 0x001Cu, {{0x0010u, (void *)&cyBle_attValuesLen[15]}} },
    { 0x001Au, 0x7F13u /* General Activity Summary Data       */, 0x09220003u /* rd,ind */, 0x001Cu, {{0x004Du, (void *)&cyBle_attValuesLen[16]}} },
    { 0x001Bu, 0x2902u /* GeneralActivitySummaryDataCCCD      */, 0x010A0301u /* rd,wr  */, 0x001Bu, {{0x0002u, (void *)&cyBle_attValuesLen[17]}} },
    { 0x001Cu, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x001Cu, {{0x001Du, (void *)&cyBle_attValuesLen[18]}} },
    { 0x001Du, 0x2803u /* Characteristic                      */, 0x00120001u /* rd,ntf */, 0x0020u, {{0x0010u, (void *)&cyBle_attValuesLen[19]}} },
    { 0x001Eu, 0x7F14u /* CardioRespiratory Activity Instanta */, 0x09120003u /* rd,ntf */, 0x0020u, {{0x0018u, (void *)&cyBle_attValuesLen[20]}} },
    { 0x001Fu, 0x2902u /* CardioRespiratoryAvtivityInstantane */, 0x010A0301u /* rd,wr  */, 0x001Fu, {{0x0002u, (void *)&cyBle_attValuesLen[21]}} },
    { 0x0020u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0020u, {{0x0024u, (void *)&cyBle_attValuesLen[22]}} },
    { 0x0021u, 0x2803u /* Characteristic                      */, 0x00220001u /* rd,ind */, 0x0024u, {{0x0010u, (void *)&cyBle_attValuesLen[23]}} },
    { 0x0022u, 0x7F15u /* CardioRespiratory Activity Summary  */, 0x09220003u /* rd,ind */, 0x0024u, {{0x003Cu, (void *)&cyBle_attValuesLen[24]}} },
    { 0x0023u, 0x2902u /* CardioRespiratoryActivitySummaryDat */, 0x010A0301u /* rd,wr  */, 0x0023u, {{0x0002u, (void *)&cyBle_attValuesLen[25]}} },
    { 0x0024u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0024u, {{0x0027u, (void *)&cyBle_attValuesLen[26]}} },
    { 0x0025u, 0x2803u /* Characteristic                      */, 0x00220001u /* rd,ind */, 0x0028u, {{0x0010u, (void *)&cyBle_attValuesLen[27]}} },
    { 0x0026u, 0x7F16u /* Step Counter Activity Summary Data  */, 0x09220003u /* rd,ind */, 0x0028u, {{0x001Du, (void *)&cyBle_attValuesLen[28]}} },
    { 0x0027u, 0x2902u /* StepCounterActivitySummaryDataCCCD  */, 0x010A0301u /* rd,wr  */, 0x0027u, {{0x0002u, (void *)&cyBle_attValuesLen[29]}} },
    { 0x0028u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0028u, {{0x0022u, (void *)&cyBle_attValuesLen[30]}} },
    { 0x0029u, 0x2803u /* Characteristic                      */, 0x00120001u /* rd,ntf */, 0x002Cu, {{0x0010u, (void *)&cyBle_attValuesLen[31]}} },
    { 0x002Au, 0x7F17u /* Sleep Activity Instantaneous Data   */, 0x09120003u /* rd,ntf */, 0x002Cu, {{0x001Au, (void *)&cyBle_attValuesLen[32]}} },
    { 0x002Bu, 0x2902u /* SleepActivityInstantaneousDataCCCD  */, 0x010A0301u /* rd,wr  */, 0x002Bu, {{0x0002u, (void *)&cyBle_attValuesLen[33]}} },
    { 0x002Cu, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x002Cu, {{0x0021u, (void *)&cyBle_attValuesLen[34]}} },
    { 0x002Du, 0x2803u /* Characteristic                      */, 0x00220001u /* rd,ind */, 0x0030u, {{0x0010u, (void *)&cyBle_attValuesLen[35]}} },
    { 0x002Eu, 0x7F18u /* Sleep Activity Summary Data         */, 0x09220003u /* rd,ind */, 0x0030u, {{0x0044u, (void *)&cyBle_attValuesLen[36]}} },
    { 0x002Fu, 0x2902u /* SleepActivitySummaryDataCCCD        */, 0x010A0301u /* rd,wr  */, 0x002Fu, {{0x0002u, (void *)&cyBle_attValuesLen[37]}} },
    { 0x0030u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0030u, {{0x001Bu, (void *)&cyBle_attValuesLen[38]}} },
    { 0x0031u, 0x2803u /* Characteristic                      */, 0x00280001u /* wr,ind */, 0x0034u, {{0x0010u, (void *)&cyBle_attValuesLen[39]}} },
    { 0x0032u, 0x7F1Eu /* Physical Activity Monitor Control P */, 0x09280300u /* wr,ind */, 0x0034u, {{0x0004u, (void *)&cyBle_attValuesLen[40]}} },
    { 0x0033u, 0x2902u /* PhysicalActivityMonitorControlPoint */, 0x010A0301u /* rd,wr  */, 0x0033u, {{0x0002u, (void *)&cyBle_attValuesLen[41]}} },
    { 0x0034u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0034u, {{0x0027u, (void *)&cyBle_attValuesLen[42]}} },
    { 0x0035u, 0x2803u /* Characteristic                      */, 0x00220001u /* rd,ind */, 0x0038u, {{0x0010u, (void *)&cyBle_attValuesLen[43]}} },
    { 0x0036u, 0x7F1Fu /* Current Session                     */, 0x09220003u /* rd,ind */, 0x0038u, {{0x0013u, (void *)&cyBle_attValuesLen[44]}} },
    { 0x0037u, 0x2902u /* CurrentSessionCCCD                  */, 0x010A0301u /* rd,wr  */, 0x0037u, {{0x0002u, (void *)&cyBle_attValuesLen[45]}} },
    { 0x0038u, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x0038u, {{0x000Fu, (void *)&cyBle_attValuesLen[46]}} },
    { 0x0039u, 0x2803u /* Characteristic                      */, 0x00200001u /* ind    */, 0x003Cu, {{0x0010u, (void *)&cyBle_attValuesLen[47]}} },
    { 0x003Au, 0x7F20u /* Session Descriptor                  */, 0x09200000u /* ind    */, 0x003Cu, {{0x001Fu, (void *)&cyBle_attValuesLen[48]}} },
    { 0x003Bu, 0x2902u /* SessionDescriptorCCCD               */, 0x010A0301u /* rd,wr  */, 0x003Bu, {{0x0002u, (void *)&cyBle_attValuesLen[49]}} },
    { 0x003Cu, 0x2901u /* Characteristic User Description     */, 0x01020001u /* rd     */, 0x003Cu, {{0x0012u, (void *)&cyBle_attValuesLen[50]}} },
};


#endif /* (CYBLE_GATT_ROLE_SERVER) */

#if(CYBLE_GATT_ROLE_CLIENT)
    
CYBLE_CLIENT_STATE_T cyBle_clientState;
CYBLE_GATTC_T cyBle_gattc;
CYBLE_GATT_ATTR_HANDLE_RANGE_T cyBle_gattcDiscoveryRange;
    
#endif /* (CYBLE_GATT_ROLE_CLIENT) */


#if(CYBLE_GATT_ROLE_SERVER)

/****************************************************************************** 
* Function Name: CyBle_GattsReInitGattDb
***************************************************************************//**
* 
*  Reinitializes the GATT database.
* 
*  \return
*  CYBLE_API_RESULT_T: An API result states if the API succeeded or failed with
*  error codes:

*  Errors codes                          | Description
*  ------------                          | -----------
*  CYBLE_ERROR_OK						 | GATT database was reinitialized successfully.
*  CYBLE_ERROR_INVALID_STATE             | If the function is called in any state except CYBLE_STATE_DISCONNECTED.
*  CYBLE_ERROR_INVALID_PARAMETER         | If the Database has zero entries or is a NULL pointer.
* 
******************************************************************************/
CYBLE_API_RESULT_T CyBle_GattsReInitGattDb(void)
{
    CYBLE_API_RESULT_T apiResult;
    
    if(CyBle_GetState() == CYBLE_STATE_DISCONNECTED)
    {
        apiResult = CyBle_GattsDbRegister(cyBle_gattDB, CYBLE_GATT_DB_INDEX_COUNT, CYBLE_GATT_DB_MAX_VALUE_LEN);
    }
    else
    {
        apiResult = CYBLE_ERROR_INVALID_STATE;
    }
    
    return(apiResult);
}


/****************************************************************************** 
* Function Name: CyBle_GattsWriteEventHandler
***************************************************************************//**
* 
*  Handles the Write Request Event for GATT service.
* 
*  \param eventParam: The pointer to the data structure specified by the event.
* 
*  \return
*  CYBLE_GATT_ERR_CODE_T: An API result returns one of the following status 
*  values.

*  Errors codes                          | Description
*  --------------------                  | -----------
*  CYBLE_GATT_ERR_NONE                   | Write is successful.
* 
******************************************************************************/
CYBLE_GATT_ERR_CODE_T CyBle_GattsWriteEventHandler(CYBLE_GATTS_WRITE_REQ_PARAM_T *eventParam)
{
    CYBLE_GATT_ERR_CODE_T gattErr = CYBLE_GATT_ERR_NONE;
    
    /* Client Characteristic Configuration descriptor write request */
    if(eventParam->handleValPair.attrHandle == cyBle_gatts.cccdHandle)
    {
        /* Store value to database */
        gattErr = CyBle_GattsWriteAttributeValue(&eventParam->handleValPair, 0u, 
                        &eventParam->connHandle, CYBLE_GATT_DB_PEER_INITIATED);
        
        if(gattErr == CYBLE_GATT_ERR_NONE)
        {
            if(CYBLE_IS_INDICATION_ENABLED_IN_PTR(eventParam->handleValPair.value.val))
            {
                CyBle_ApplCallback((uint32)CYBLE_EVT_GATTS_INDICATION_ENABLED, eventParam);
            }
            else
            {
                CyBle_ApplCallback((uint32)CYBLE_EVT_GATTS_INDICATION_DISABLED, eventParam);
            }
        }
        cyBle_eventHandlerFlag &= (uint8)~CYBLE_CALLBACK;
    }
    return (gattErr);
}


#endif /* (CYBLE_GATT_ROLE_SERVER) */

#if(CYBLE_GATT_ROLE_CLIENT)


/****************************************************************************** 
* Function Name: CyBle_GattcStartDiscovery
***************************************************************************//**
* 
*  Starts the automatic server discovery process. Two events may be generated 
*  after calling this function - CYBLE_EVT_GATTC_DISCOVERY_COMPLETE or 
*  CYBLE_EVT_GATTC_ERROR_RSP. The CYBLE_EVT_GATTC_DISCOVERY_COMPLETE event is 
*  generated when the remote device was successfully discovered. The
*  CYBLE_EVT_GATTC_ERROR_RSP is generated if the device discovery is failed.
* 
*  \param connHandle: The handle which consists of the device ID and ATT connection ID.
* 
* \return
*	CYBLE_API_RESULT_T : Return value indicates if the function succeeded or
*                        failed. Following are the possible error codes.
*
*   <table>	
*   <tr>
*	  <th>Errors codes</th>
*	  <th>Description</th>
*	</tr>
*	<tr>
*	  <td>CYBLE_ERROR_OK</td>
*	  <td>On successful operation</td>
*	</tr>
*	<tr>
*	  <td>CYBLE_ERROR_INVALID_PARAMETER</td>
*	  <td>'connHandle' value does not represent any existing entry.</td>
*	</tr>
*	<tr>
*	  <td>CYBLE_ERROR_INVALID_OPERATION</td>
*	  <td>The operation is not permitted</td>
*	</tr>
*   <tr>
*	  <td>CYBLE_ERROR_MEMORY_ALLOCATION_FAILED</td>
*	  <td>Memory allocation failed</td>
*	</tr>
*   <tr>
*	  <td>CYBLE_ERROR_INVALID_STATE</td>
*	  <td>If the function is called in any state except connected or discovered</td>
*	</tr>
*   </table>
* 
******************************************************************************/
CYBLE_API_RESULT_T CyBle_GattcStartDiscovery(CYBLE_CONN_HANDLE_T connHandle)
{
    uint8 j;
    CYBLE_API_RESULT_T apiResult;
    
    if((CyBle_GetState() != CYBLE_STATE_CONNECTED) || 
       ((CyBle_GetClientState() != CYBLE_CLIENT_STATE_CONNECTED) && 
        (CyBle_GetClientState() != CYBLE_CLIENT_STATE_DISCOVERED))) 
    {
        apiResult = CYBLE_ERROR_INVALID_STATE;
    }
    else
    {
        /* Clean old discovery information */
        for(j = 0u; j < (uint8) CYBLE_SRVI_COUNT; j++)
        {
            (void)memset(&cyBle_serverInfo[j].range, 0, sizeof(cyBle_serverInfo[0].range));
        }

        cyBle_connHandle = connHandle;
        cyBle_gattcDiscoveryRange.startHandle = CYBLE_GATT_ATTR_HANDLE_START_RANGE;
        cyBle_gattcDiscoveryRange.endHandle = CYBLE_GATT_ATTR_HANDLE_END_RANGE;
        
        CyBle_ServiceInit();
        
        apiResult = CyBle_GattcDiscoverAllPrimaryServices(connHandle);

        if(apiResult == CYBLE_ERROR_OK)
        {
            CyBle_SetClientState(CYBLE_CLIENT_STATE_SRVC_DISCOVERING);
            cyBle_eventHandlerFlag |= CYBLE_AUTO_DISCOVERY;
        }
    }
    
    return (apiResult);
}


/****************************************************************************** 
* Function Name: CyBle_GattcStartPartialDiscovery
***************************************************************************//**
* 
*  Starts the automatic server discovery process as per the range provided
*  on a GATT Server to which it is connected. This API could be used for 
*  partial server discovery after indication received to the Service Changed
*  Characteristic Value. Two events may be generated 
*  after calling this function - CYBLE_EVT_GATTC_DISCOVERY_COMPLETE or 
*  CYBLE_EVT_GATTC_ERROR_RSP. The CYBLE_EVT_GATTC_DISCOVERY_COMPLETE event is 
*  generated when the remote device was successfully discovered. The
*  CYBLE_EVT_GATTC_ERROR_RSP is generated if the device discovery is failed.
* 
*  \param connHandle: The handle which consists of the device ID and ATT connection ID.
*  \param startHandle: Start of affected attribute handle range.
*  \param endHandle: End of affected attribute handle range.
* 
*  \return
*	CYBLE_API_RESULT_T : Return value indicates if the function succeeded or
*                        failed. Following are the possible error codes.
*
*   <table>	
*   <tr>
*	  <th>Errors codes</th>
*	  <th>Description</th>
*	</tr>
*	<tr>
*	  <td>CYBLE_ERROR_OK</td>
*	  <td>On successful operation</td>
*	</tr>
*	<tr>
*	  <td>CYBLE_ERROR_INVALID_PARAMETER</td>
*	  <td>'connHandle' value does not represent any existing entry.</td>
*	</tr>
*	<tr>
*	  <td>CYBLE_ERROR_INVALID_OPERATION</td>
*	  <td>The operation is not permitted</td>
*	</tr>
*   <tr>
*	  <td>CYBLE_ERROR_MEMORY_ALLOCATION_FAILED</td>
*	  <td>Memory allocation failed</td>
*	</tr>
*   <tr>
*	  <td>CYBLE_ERROR_INVALID_STATE</td>
*	  <td>If the function is called in any state except connected or discovered</td>
*	</tr>
*   </table>
* 
******************************************************************************/
CYBLE_API_RESULT_T CyBle_GattcStartPartialDiscovery(CYBLE_CONN_HANDLE_T connHandle,
                        CYBLE_GATT_DB_ATTR_HANDLE_T startHandle, CYBLE_GATT_DB_ATTR_HANDLE_T endHandle)
{
    uint8 j;
    CYBLE_API_RESULT_T apiResult;
    
    if((CyBle_GetState() != CYBLE_STATE_CONNECTED) || 
       ((CyBle_GetClientState() != CYBLE_CLIENT_STATE_CONNECTED) && 
        (CyBle_GetClientState() != CYBLE_CLIENT_STATE_DISCOVERED))) 
    {
        apiResult = CYBLE_ERROR_INVALID_STATE;
    }
    else
    {
        /* Clean old discovery information of affected attribute range */
        for(j = 0u; j < (uint8) CYBLE_SRVI_COUNT; j++)
        {
            if((cyBle_serverInfo[j].range.startHandle >= startHandle) &&
               (cyBle_serverInfo[j].range.startHandle <= endHandle))
            {
                (void)memset(&cyBle_serverInfo[j].range, 0, sizeof(cyBle_serverInfo[0].range));
            }
        }

        cyBle_connHandle = connHandle;
        cyBle_gattcDiscoveryRange.startHandle = startHandle;
        cyBle_gattcDiscoveryRange.endHandle = endHandle;

        CyBle_ServiceInit();

        apiResult = CyBle_GattcDiscoverPrimaryServices(connHandle, &cyBle_gattcDiscoveryRange);

        if(apiResult == CYBLE_ERROR_OK)
        {
            CyBle_SetClientState(CYBLE_CLIENT_STATE_SRVC_DISCOVERING);
            cyBle_eventHandlerFlag |= CYBLE_AUTO_DISCOVERY;
        }
    }
    
    return (apiResult);
}


/******************************************************************************
* Function Name: CyBle_GattcDiscoverCharacteristicsEventHandler
***************************************************************************//**
* 
*  This function is called on receiving a "CYBLE_EVT_GATTC_READ_BY_TYPE_RSP"
*  event. Based on the service UUID, an appropriate data structure is populated
*  using the data received as part of the callback.
* 
*  \param *discCharInfo: The pointer to a characteristic information structure.
* 
* \return
*  None
* 
******************************************************************************/
void CyBle_GattcDiscoverCharacteristicsEventHandler(CYBLE_DISC_CHAR_INFO_T *discCharInfo)
{
    if(discCharInfo->uuid.uuid16 == CYBLE_UUID_CHAR_SERVICE_CHANGED)
    {
        CyBle_CheckStoreCharHandle(cyBle_gattc.serviceChanged);
    }
}


/******************************************************************************
* Function Name: CyBle_GattcDiscoverCharDescriptorsEventHandler
***************************************************************************//**
* 
*  This function is called on receiving a "CYBLE_EVT_GATTC_FIND_INFO_RSP" event.
*  Based on the descriptor UUID, an appropriate data structure is populated 
*  using the data received as part of the callback.
* 
*  \param *discDescrInfo: The pointer to a descriptor information structure.
*  \param discoveryService: The index of the service instance
* 
* \return
*  None
* 
******************************************************************************/
void CyBle_GattcDiscoverCharDescriptorsEventHandler(CYBLE_DISC_DESCR_INFO_T *discDescrInfo)
{
    if(discDescrInfo->uuid.uuid16 == CYBLE_UUID_CHAR_CLIENT_CONFIG)
    {
        CyBle_CheckStoreCharDescrHandle(cyBle_gattc.cccdHandle);
    }
}


/******************************************************************************
* Function Name: CyBle_GattcIndicationEventHandler
***************************************************************************//**
* 
*  Handles the Indication Event.
* 
*  \param *eventParam: The pointer to the data structure specified by the event.
* 
* \return
*  None.
* 
******************************************************************************/
void CyBle_GattcIndicationEventHandler(CYBLE_GATTC_HANDLE_VALUE_IND_PARAM_T *eventParam)
{
    if(cyBle_gattc.serviceChanged.valueHandle == eventParam->handleValPair.attrHandle)
    {
        CyBle_ApplCallback((uint32)CYBLE_EVT_GATTC_INDICATION, eventParam);
        cyBle_eventHandlerFlag &= (uint8)~CYBLE_CALLBACK;
    }
}


#endif /* (CYBLE_GATT_ROLE_CLIENT) */


/* [] END OF FILE */
