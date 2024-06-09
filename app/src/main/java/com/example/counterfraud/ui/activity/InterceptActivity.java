package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.counterfraud.R;
import com.example.counterfraud.bean.User;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;


/**
 * 拦截
 */
public class InterceptActivity extends AppCompatActivity {
    private Activity myActivity;//上下文
    MySqliteOpenHelper helper = null;
    User mUser = null;
    private Integer userId;
    private Switch enable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity =this;
        setContentView(R.layout.activity_intercept);
        helper = new MySqliteOpenHelper(myActivity);
        enable = findViewById(R.id.enable);
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);

        loadData();

        enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SQLiteDatabase db = helper.getWritableDatabase();
                String sql = "update user set isEnable =? where id =?";
                db.execSQL(sql, new Object[]{isChecked ? 1 : 0, userId});
                mUser.setIsEnable(isChecked ? 1 : 0);
            }
        });

    }



    private void loadData() {
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
                enable.setChecked(isEnable.intValue() == 1);
            }
        }
    }



    //返回
    public void back(View view){
        finish();
    }
}
