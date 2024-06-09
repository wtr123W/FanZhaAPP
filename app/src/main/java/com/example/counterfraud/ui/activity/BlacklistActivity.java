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
import com.example.counterfraud.adapter.BlacklistAdapter;
import com.example.counterfraud.bean.Blacklist;
import com.example.counterfraud.util.KeyBoardUtil;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉黑号码
 */
public class BlacklistActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private BlacklistAdapter blacklistAdapter;
    private List<Blacklist> blacklistList;
    private FloatingActionButton btnAdd;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        rvList = findViewById(R.id.rv_list);
        llEmpty = findViewById(R.id.ll_empty);
        etQuery = findViewById(R.id.et_query);
        ivSearch = findViewById(R.id.iv_search);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, AddBlacklistActivity.class);
                startActivityForResult(intent, 100);
            }
        });
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
        blacklistAdapter = new BlacklistAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(blacklistAdapter);
        loadData();//加载数据
        blacklistAdapter.setItemListener(new BlacklistAdapter.ItemListener() {
            @Override
            public void ItemClick(Blacklist blacklist) {
                Intent intent = new Intent(myActivity,AddBlacklistActivity.class);
                intent.putExtra("blacklist",blacklist);
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
                        String sql = "delete from blacklist where id="+id;
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
        blacklistList = new ArrayList<>();
        String sql = "select * from blacklist where userId =  "+userId;
        if (!"".equals(contentStr)){
            sql+=" and phone like ?";
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,!"".equals(contentStr)?new String[]{"%"+contentStr+"%"}:null);
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
        if (blacklistList != null && blacklistList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            blacklistAdapter.addItem(blacklistList);
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
