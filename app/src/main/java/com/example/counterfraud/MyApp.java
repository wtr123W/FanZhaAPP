package com.example.counterfraud;

import android.app.Application;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.counterfraud.util.CustomPhoneStateListener;

public class MyApp extends Application {
    public static MyApp INSTANCE;
    private CustomPhoneStateListener customPhoneStateListener;
    private TelephonyManager telephonyManager;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        customPhoneStateListener = new CustomPhoneStateListener(this);
        registerPhoneStateListener();
    }


    private void registerPhoneStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_NONE);
            telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}
