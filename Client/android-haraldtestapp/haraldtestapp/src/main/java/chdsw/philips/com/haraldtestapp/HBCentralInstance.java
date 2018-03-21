package chdsw.philips.com.haraldtestapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import chdsw.philips.com.haraldlib.HBCentral;

public class HBCentralInstance {
    private static HBCentralInstance instance;
    private HBCentral hbCentral;

    private HBCentralInstance(Context context) {
        this.hbCentral = new HBCentral(context, new Handler(Looper.getMainLooper()));
    }

    public static HBCentralInstance getInstance(Context context) {
        if (instance == null) {
            instance = new HBCentralInstance(context);
        }
        return instance;
    }

    public HBCentral getHBCentral() {
        return hbCentral;
    }

}
