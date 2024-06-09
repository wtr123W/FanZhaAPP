package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.counterfraud.R;
import com.example.counterfraud.bean.User;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

/**
 * 重置密码
 */
public class PasswordActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity activity;
    private EditText etNewPassword;
    private EditText et_sure_password;
    private EditText et_old_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity =this;
        setContentView(R.layout.activity_password);
        helper = new MySqliteOpenHelper(activity);
        etNewPassword = findViewById(R.id.et_new_password);
        et_sure_password = findViewById(R.id.et_sure_password);
        et_old_password = findViewById(R.id.et_old_password);
    }

    //保存信息
    public void save(View v){
        String oldPassword = et_old_password.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String surePassword = et_sure_password.getText().toString();
        if ("".equals(oldPassword)){//密码为空
            Toast.makeText(activity,"旧密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(newPassword)){//密码为空
            Toast.makeText(activity,"新密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (!surePassword.equals(newPassword)){//密码不一致
            Toast.makeText(activity,"密码不一致", Toast.LENGTH_LONG).show();
            return;
        }
        Integer userId = (Integer) SPUtils.get(PasswordActivity.this,SPUtils.USER_ID,0);

        User mUser = null;
        //通过账号查询是否存在
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
        if (oldPassword.equals(mUser.getPassword())){
            db.execSQL("update user set password = ? where id = ?", new Object[]{newPassword,userId});
            Toast.makeText(PasswordActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(PasswordActivity.this, "旧密码错误", Toast.LENGTH_SHORT).show();
        }

    }

    public void back(View view){
        finish();
    }
}
