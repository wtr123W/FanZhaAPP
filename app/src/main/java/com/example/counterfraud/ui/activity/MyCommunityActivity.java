package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counterfraud.R;
import com.example.counterfraud.adapter.CommunityAdapter;
import com.example.counterfraud.bean.CommunityVo;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分享
 */
public class MyCommunityActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvBrowseList;
    private CommunityAdapter communityAdapter;
    private List<CommunityVo> communityList;
    private FloatingActionButton btnAdd;
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        rvBrowseList = findViewById(R.id.rv_list);
        llEmpty = findViewById(R.id.ll_empty);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, AddCommunityActivity.class);
                startActivityForResult(intent, 100);
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
        rvBrowseList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        communityAdapter = new CommunityAdapter();
        //=2.3、设置recyclerView的适配器
        rvBrowseList.setAdapter(communityAdapter);
        loadData();//加载数据
        communityAdapter.setItemListener(new CommunityAdapter.ItemListener() {
            @Override
            public void ItemClick(CommunityVo community) {
                Intent intent = new Intent(myActivity, AddCommunityActivity.class);
                intent.putExtra("community", community);
                startActivityForResult(intent, 100);
            }

            @Override
            public void Delete(CommunityVo community) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要删除该数据吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String sql = "delete from community where id="+community.getId();
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
        communityList = new ArrayList<>();
        CommunityVo community = null;
        String sql = "select c.*,u.name,u.photo from community c,user u where c.userId = u.id and u.id="+userId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer dbUserId = cursor.getInt(1);
                String title = cursor.getString(2);
                String img = cursor.getString(3);
                String content = cursor.getString(4);
                String date = cursor.getString(5);
                String name = cursor.getString(6);
                String photo = cursor.getString(7);
                community = new CommunityVo(dbId, dbUserId, title, img, content, date, name,photo);
                communityList.add(community);
            }
        }
        Collections.reverse(communityList);
        if (communityList != null && communityList.size() > 0) {
            rvBrowseList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            communityAdapter.addItem(communityList);
        } else {
            rvBrowseList.setVisibility(View.GONE);
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
