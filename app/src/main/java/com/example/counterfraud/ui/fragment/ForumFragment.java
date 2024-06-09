package com.example.counterfraud.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counterfraud.R;
import com.example.counterfraud.adapter.CommunityAdapter;
import com.example.counterfraud.bean.CommunityVo;
import com.example.counterfraud.ui.activity.AddCommunityActivity;
import com.example.counterfraud.ui.activity.CommunityDetailActivity;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.RecyclerViewSpaces;
import com.example.counterfraud.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 论坛
 */
public class ForumFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private FloatingActionButton btnAdd;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private CommunityAdapter communityAdapter;
    private List<CommunityVo> communityList;
    private Integer userId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        helper = new MySqliteOpenHelper(myActivity);
        rvList = view.findViewById(R.id.rv_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        etQuery = view.findViewById(R.id.et_query);
        ivSearch = view.findViewById(R.id.iv_search);
        btnAdd = view.findViewById(R.id.btn_add);
        //获取控件
        initView();
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
                    loadData();//加载数据
                    return true;
                }
                return false;
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, AddCommunityActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        return view;
    }


    /**
     * 初始化页面
     */
    private void initView() {
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        btnAdd.setVisibility(userId > 0 ? View.VISIBLE : View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        HashMap<String, Integer> mapSpaces = new HashMap<>();//间距
        mapSpaces.put(RecyclerViewSpaces.TOP_DECORATION, 20);//上间距
        mapSpaces.put(RecyclerViewSpaces.BOTTOM_DECORATION, 20);//下间距
        mapSpaces.put(RecyclerViewSpaces.LEFT_DECORATION, 20);//左间距
        mapSpaces.put(RecyclerViewSpaces.RIGHT_DECORATION, 20);//右间距
        rvList.addItemDecoration(new RecyclerViewSpaces(mapSpaces));//设置间距
        //==2、实例化适配器
        //=2.1、初始化适配器
        communityAdapter = new CommunityAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(communityAdapter);
        loadData();
        communityAdapter.setItemListener(new CommunityAdapter.ItemListener() {
            @Override
            public void ItemClick(CommunityVo community) {
                Intent intent = new Intent(myActivity, CommunityDetailActivity.class);
                intent.putExtra("community", community);
                startActivity(intent);
            }

            @Override
            public void Delete(CommunityVo community) {

            }
        });
    }

    private void loadData() {
        String contentStr = etQuery.getText().toString();//获取搜索内容
        communityList = new ArrayList<>();
        CommunityVo community = null;
        String sql = "select c.*,u.name,u.photo from community c,user u where c.userId = u.id";
        if (!"".equals(contentStr)) {
            sql += " and title like ?";
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, !"".equals(contentStr) ? new String[]{"%" + contentStr + "%"} : null);
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
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            communityAdapter.addItem(communityList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData();//加载数据
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
