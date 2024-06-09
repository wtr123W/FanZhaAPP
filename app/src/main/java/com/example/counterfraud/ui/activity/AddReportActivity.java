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
import com.example.counterfraud.bean.Report;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 添加修改页面
 */
public class AddReportActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private EditText et_name;
    private EditText et_sex;
    private EditText et_phone;
    private EditText et_address;
    private EditText et_crimeAddress;
    private EditText et_crimeTime;
    private EditText et_content;
    private Report report;
    private Integer userId;
    private SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        setContentView(R.layout.activity_report_add);
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        et_name = findViewById(R.id.et_name);
        et_sex = findViewById(R.id.et_sex);
        et_address = findViewById(R.id.et_address);
        et_crimeAddress = findViewById(R.id.et_crimeAddress);
        et_crimeTime = findViewById(R.id.et_crimeTime);
        et_phone = findViewById(R.id.et_phone);
        et_content = findViewById(R.id.et_content);
        initView();
    }

    private void initView() {
        report = (Report) getIntent().getSerializableExtra("report");
        if (report != null) {
            et_name.setText(report.getPhone());
            et_sex.setText(report.getSex());
            et_address.setText(report.getAddress());
            et_crimeAddress.setText(report.getCrimeAddress());
            et_crimeTime.setText(report.getCrimeTime());
            et_phone.setText(report.getPhone());
            et_content.setText(report.getContent());
        }
    }

    public void save(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String name = et_name.getText().toString();
        String sex = et_sex.getText().toString();
        String address = et_address.getText().toString();
        String crimeAddress = et_crimeAddress.getText().toString();
        String crimeTime = et_crimeTime.getText().toString();
        String phone = et_phone.getText().toString();
        String content = et_content.getText().toString();
        if ("".equals(name)) {
            Toast.makeText(myActivity, "姓名不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(sex)) {
            Toast.makeText(myActivity, "性别不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(address)) {
            Toast.makeText(myActivity, "联系地址不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(crimeAddress)) {
            Toast.makeText(myActivity, "案发地址不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(crimeTime)) {
            Toast.makeText(myActivity, "案发时间能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(phone)) {
            Toast.makeText(myActivity, "手机号不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(content)) {
            Toast.makeText(myActivity, "内容不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (report == null) {//新增
            String insertSql = "insert into report(userId,number,name,sex,phone,address,reportTime,crimeTime,crimeAddress,content) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            db.execSQL(insertSql, new Object[]{userId,"J"+System.currentTimeMillis(),name,sex,phone,address,sf.format(new Date())
                    ,crimeTime,crimeAddress,content});
        } else {//修改
            String updateSql = "update report set name=?,sex=?,phone=?,address=?,crimeTime=?,crimeAddress=?,content=? where id =?";
            db.execSQL(updateSql, new Object[]{name,sex,phone, address,crimeTime,crimeAddress,content,report.getId()});
        }
        Toast.makeText(myActivity, "提交成功", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
        db.close();
    }

    public void back(View view){
        finish();
    }
}

