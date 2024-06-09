package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.counterfraud.R;
import com.example.counterfraud.bean.Blacklist;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

/**
 * 核实身份
 */
public class VerifyActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity activity;
    private EditText etPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity =this;
        setContentView(R.layout.activity_verify);
        helper = new MySqliteOpenHelper(activity);
        etPhone = findViewById(R.id.et_phone);
    }

    public void find(View v){
        String phone = etPhone.getText().toString();
        if ("".equals(phone)){//电话号码不能为空
            Toast.makeText(activity,"电话号码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        Integer userId = (Integer) SPUtils.get(VerifyActivity.this,SPUtils.USER_ID,0);

        Blacklist blacklist = null;
        //通过账号查询是否存在
        String sql = "select * from blacklist where phone = ? and userId ="+userId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{phone});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer dbUserId = cursor.getInt(1);
                String dbPhone = cursor.getString(2);
                Integer status = cursor.getInt(3);
                blacklist = new Blacklist(dbId, dbUserId, dbPhone , status);
            }
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(blacklist!=null?"该电话号码为高危诈骗电话":"该电话号码安全");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void back(View view){
        finish();
    }
}
