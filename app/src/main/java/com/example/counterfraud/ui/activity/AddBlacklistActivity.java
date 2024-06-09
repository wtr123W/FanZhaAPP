package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.counterfraud.R;
import com.example.counterfraud.bean.Blacklist;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

/**
 * 添加修改页面
 */
public class AddBlacklistActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private EditText etPhone;//标题
    private Blacklist blacklist;
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        setContentView(R.layout.activity_blacklist_add);
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        etPhone = findViewById(R.id.et_phone);
        initView();
    }

    private void initView() {
        blacklist = (Blacklist) getIntent().getSerializableExtra("blacklist");
        if (blacklist != null) {
            etPhone.setText(blacklist.getPhone());
        }
    }

    public void save(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String phone = etPhone.getText().toString();
        if ("".equals(phone)) {
            Toast.makeText(myActivity, "电话号码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (blacklist == null) {//新增
            String insertSql = "insert into blacklist(userId,phone) values(?,?)";
            db.execSQL(insertSql, new Object[]{userId, phone});
        } else {//修改
            String updateSql = "update blacklist set userId=?,phone=? where id =?";
            db.execSQL(updateSql, new Object[]{userId, phone, blacklist.getId()});
        }
        Toast.makeText(myActivity, "保存成功", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
        db.close();
    }

    public void back(View view){
        finish();
    }
}

