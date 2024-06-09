package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counterfraud.R;
import com.example.counterfraud.adapter.ReportAdapter;
import com.example.counterfraud.bean.Report;
import com.example.counterfraud.util.KeyBoardUtil;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的举报
 */
public class ReportActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private ReportAdapter reportAdapter;
    private List<Report> reportList;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        rvList = findViewById(R.id.rv_list);
        llEmpty = findViewById(R.id.ll_empty);
        etQuery = findViewById(R.id.et_query);
        ivSearch = findViewById(R.id.iv_search);
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(v);//隐藏软键盘
                    loadData();//加载数据
                    return true;
                }
                return false;
            }
        });
        initView();
    }

    private void initView() {
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        reportAdapter = new ReportAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(reportAdapter);
        loadData();//加载数据
        reportAdapter.setItemListener(new ReportAdapter.ItemListener() {
            @Override
            public void ItemClick(Report report) {
                Intent intent = new Intent(myActivity,AddReportActivity.class);
                intent.putExtra("report",report);
                startActivityForResult(intent, 100);
            }

            @Override
            public void Delete(Integer id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要删除该数据吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String sql = "delete from report where id="+id;
                        db.execSQL(sql);
                        db.close();
                        Toast.makeText(myActivity, "删除成功", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                });
                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }


        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        String contentStr = etQuery.getText().toString();//获取搜索内容
        reportList = new ArrayList<>();
        String sql = "select * from report where userId =  "+userId;
        if (!"".equals(contentStr)){
            sql+=" and number like ?";
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,!"".equals(contentStr)?new String[]{"%"+contentStr+"%"}:null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer userId = cursor.getInt(1);
                String number = cursor.getString(2);
                String name = cursor.getString(3);
                String sex = cursor.getString(4);
                String phone = cursor.getString(5);
                String address = cursor.getString(6);
                String reportTime = cursor.getString(7);
                String crimeTime = cursor.getString(8);
                String crimeAddress = cursor.getString(9);
                String content = cursor.getString(10);
                Report report = new Report(dbId, userId,number,name,sex,phone, address,reportTime,crimeTime,crimeAddress ,content);
                reportList.add(report);
            }
        }
        if (reportList != null && reportList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            reportAdapter.addItem(reportList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
    public void back(View view) {
        finish();
    }
}
