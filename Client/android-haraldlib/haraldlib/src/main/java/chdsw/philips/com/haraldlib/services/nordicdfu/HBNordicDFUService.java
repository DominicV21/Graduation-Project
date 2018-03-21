/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.nordicdfu;

import android.app.Activity;

import no.nordicsemi.android.dfu.DfuBaseService;

/**
 * Instance of the abstract nordic DFU base service with no modifications
 */
public class HBNordicDFUService extends DfuBaseService {
    @Override
    protected Class<? extends Activity> getNotificationTarget() {
        return null;
    }
}
