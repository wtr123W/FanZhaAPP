package com.example.counterfraud.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.counterfraud.R;
import com.example.counterfraud.bean.Blacklist;
import com.example.counterfraud.bean.User;
import com.example.counterfraud.ui.fragment.ForumFragment;
import com.example.counterfraud.ui.fragment.HomeFragment;
import com.example.counterfraud.ui.fragment.NewsFragment;
import com.example.counterfraud.ui.fragment.UserFragment;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {
    private Activity myActivity;
    private TextView tvTitle;
    private LinearLayout llContent;
    private RadioButton rbHome;
    private RadioButton rbNews;
    private RadioButton rbShare;
    private RadioButton rbUser;
    private Fragment[] fragments = new Fragment[]{null, null, null, null};//存放Fragment
    private List<Blacklist> blacklistList;
    User mUser = null;
    private Integer userId;
    MySqliteOpenHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_main);
        helper = new MySqliteOpenHelper(myActivity);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llContent = (LinearLayout) findViewById(R.id.ll_main_content);
        rbHome = (RadioButton) findViewById(R.id.rb_main_home);
        rbNews = (RadioButton) findViewById(R.id.rb_main_news);
        rbShare = (RadioButton) findViewById(R.id.rb_main_share);
        rbUser = (RadioButton) findViewById(R.id.rb_main_user);
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        initView();
        setViewListener();
        loadData();
    }

    private void setViewListener() {

        rbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("首页");
                switchFragment(0);
            }
        });
        rbNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("骗局曝光");
                switchFragment(1);
            }
        });
        rbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("论坛");
                switchFragment(2);
            }
        });
        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("我的");
                switchFragment(3);
            }
        });
    }

    private void initView() {
        //设置导航栏图标样式
        Drawable iconHome = getResources().getDrawable(R.drawable.selector_main_rb_home);//设置主页图标样式
        iconHome.setBounds(0, 0, 68, 68);//设置图标边距 大小
        rbHome.setCompoundDrawables(null, iconHome, null, null);//设置图标位置
        rbHome.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconNews = getResources().getDrawable(R.drawable.selector_main_rb_news);//设置主页图标样式
        iconNews.setBounds(0, 0, 68, 68);//设置图标边距 大小
        rbNews.setCompoundDrawables(null, iconNews, null, null);//设置图标位置
        rbNews.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconShare = getResources().getDrawable(R.drawable.selector_main_rb_share);//设置主页图标样式
        iconShare.setBounds(0, 0, 68, 68);//设置图标边距 大小
        rbShare.setCompoundDrawables(null, iconShare, null, null);//设置图标位置
        rbShare.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconUser = getResources().getDrawable(R.drawable.selector_main_rb_user);//设置主页图标样式
        iconUser.setBounds(0, 0, 68, 68);//设置图标边距 大小
        rbUser.setCompoundDrawables(null, iconUser, null, null);//设置图标位置
        rbUser.setCompoundDrawablePadding(5);//设置文字与图片的间距
        switchFragment(0);
        rbHome.setChecked(true);
    }

    /**
     * 方法 - 切换Fragment
     *
     * @param fragmentIndex 要显示Fragment的索引
     */
    private void switchFragment(int fragmentIndex) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //2、开启fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //懒加载 - 如果需要显示的Fragment为null，就new。并添加到Fragment事务中
        if (fragments[fragmentIndex] == null) {
            switch (fragmentIndex) {
                case 0:
                    fragments[fragmentIndex] = new HomeFragment();
                    break;
                case 1:
                    fragments[fragmentIndex] = new NewsFragment();
                    break;
                case 2:
                    fragments[fragmentIndex] = new ForumFragment();
                    break;
                case 3:
                    fragments[fragmentIndex] = new UserFragment();
                    break;
            }

            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex]);
        }

        //隐藏其他的Fragment
        for (int i = 0; i < fragments.length; i++) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i]);
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex]);

        //5、提交事务
        transaction.commit();
    }

    /**
     * 双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }

        return false;
    }

    private long time = 0;

    public void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(myActivity, "再点击一次退出应用程序", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }


    private void loadData() {
        String sql = "select * from user where id =  " + userId;
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
                mUser = new User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone, dbRoom, dbPhoto, isEnable);
            }
        }
        loadBlacklistData();
    }


    private void loadBlacklistData() {
        blacklistList = new ArrayList<>();
        String sql = "select * from blacklist where userId =  " + userId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer userId = cursor.getInt(1);
                String phone = cursor.getString(2);
                Integer status = cursor.getInt(3);
                Blacklist blacklist = new Blacklist(dbId, userId, phone, status);
                blacklistList.add(blacklist);
            }
        }
        initTelephonyManager();
    }


    //初始化监听
    private void initTelephonyManager() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        // 电话响铃
                        if (mUser.getIsEnable().intValue() == 1) {//拦截开启
                            for (Blacklist blacklist : blacklistList) {//查询号码是否在黑名单里面
                                if (incomingNumber.equals(blacklist.getPhone())) {
                                    endCall();
                                }
                            }
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        // 电话接通

                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // 电话挂断

                        break;
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void endCall() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (telecomManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager.endCall();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
