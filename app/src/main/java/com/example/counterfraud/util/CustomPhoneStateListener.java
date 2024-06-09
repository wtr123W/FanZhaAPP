package com.example.counterfraud.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.counterfraud.bean.Blacklist;
import com.example.counterfraud.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 来去电监听
 */
public class CustomPhoneStateListener extends PhoneStateListener {

    private Context mContext;

    private List<Blacklist> blacklistList;
    User mUser = null;
    private Integer userId;
    MySqliteOpenHelper helper = null;
    public CustomPhoneStateListener(Context context) {
        mContext = context;
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        loadData();
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:      // 电话挂断
                break;
            case TelephonyManager.CALL_STATE_RINGING:   // 电话响铃
                // 电话响铃
                if (mUser.getIsEnable().intValue()==1){//拦截开启
                    for (Blacklist blacklist : blacklistList) {//查询号码是否在黑名单里面
                        if (incomingNumber.equals(blacklist.getPhone())){
                            stopCall();
                        }
                    }
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:   // 来电接通 或者 去电，去电接通  但是没法区分
                break;
        }
    }

    //挂断电话
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopCall() {
        TelecomManager tm = (TelecomManager) mContext.getSystemService(Context.TELECOM_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 100);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    tm.endCall();
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                tm.endCall();
            }
        }
    }

    private void loadData() {
        userId = (Integer) SPUtils.get(mContext, SPUtils.USER_ID, 0);
        helper = new MySqliteOpenHelper(mContext);
        String sql = "select * from user where id =  "+userId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String dbAccount = cursor.getString(1);
                String dbPassword = cursor.getString(2);
                String dbName = cursor.getString(3);
                String dbSex = cursor.getString(4);
                String dbPhone = cursor.getString(5);
                String dbRoom = cursor.getString(6);
                String dbPhoto = cursor.getString(7);
                Integer isEnable = cursor.getInt(8);
                mUser = new User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone, dbRoom, dbPhoto,isEnable);
            }
        }
        loadBlacklistData();
    }

    private void loadBlacklistData() {
        blacklistList = new ArrayList<>();
        String sql = "select * from blacklist where userId =  "+userId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer userId = cursor.getInt(1);
                String phone = cursor.getString(2);
                Integer status = cursor.getInt(3);
                Blacklist blacklist = new Blacklist(dbId, userId, phone , status);
                blacklistList.add(blacklist);
            }
        }
    }


}