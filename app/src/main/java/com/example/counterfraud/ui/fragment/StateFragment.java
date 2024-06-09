package com.example.counterfraud.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counterfraud.R;
import com.example.counterfraud.adapter.KnowledgeAdapter;
import com.example.counterfraud.adapter.NewsAdapter;
import com.example.counterfraud.bean.Knowledge;
import com.example.counterfraud.bean.News;
import com.example.counterfraud.ui.activity.KnowledgeDetailActivity;
import com.example.counterfraud.ui.activity.NewsDetailActivity;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class StateFragment extends Fragment {
    private Activity myActivity;//上下文
    private int stateId;
    MySqliteOpenHelper helper = null;

    private TabLayout tabNewsTitle;
    private RecyclerView rvNewsList;
    private NewsAdapter mNewsAdapter;
    private String[] newsState = {"0","1","2","3"};
    private String[] newsTitle = {"推荐", "各地动态","反诈知识","时事热点"};
    private String newsTypeId = "0";
    private List<News> newsList;
    private LinearLayout llNewsEmpty;

    private TabLayout tabKnowledgeTitle;
    private RecyclerView rvKnowledgeList;
    private KnowledgeAdapter knowledgeAdapter;
    private String[] knowledgeState = {"0","1","2","3"};
    private String[] knowledgeTitle = {"推荐", "刷单返利诈骗","虚假投资理财诈骗","虚假网络贷款诈骗"};
    private String knowledgeTypeId = "0";
    private List<Knowledge> knowledgeList;
    private LinearLayout llKnowledgeEmpty;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity=(Activity) context;//设置上下文
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        //获取传递的参数 orderStateId
        Bundle bundle=getArguments();
        stateId = bundle.getInt("stateId");
        helper = new MySqliteOpenHelper(myActivity);
        if (stateId==0){//动态
            view=inflater.inflate(R.layout.fragment_news_data,container,false);
            tabNewsTitle = view.findViewById(R.id.tab_title);
            llNewsEmpty = view.findViewById(R.id.ll_empty);
            rvNewsList = view.findViewById(R.id.rv_list);
            initNewsView();
            loadNewsData();//加载
        } else if (stateId==1){//科普
            view=inflater.inflate(R.layout.fragment_knowledge_data,container,false);
            tabKnowledgeTitle = view.findViewById(R.id.tab_title);
            llKnowledgeEmpty = view.findViewById(R.id.ll_empty);
            rvKnowledgeList = view.findViewById(R.id.rv_list);
            initKnowledgeView();
            loadKnowledgeData();//加载
        }
        return view;
    }

    //初始化动态
    private void initNewsView() {
        tabNewsTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tablayout距离上下左右的距离
        //为TabLayout添加tab名称
        for (int i=0;i<newsTitle.length;i++){
            tabNewsTitle.addTab(tabNewsTitle.newTab().setText(newsTitle[i]));
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvNewsList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mNewsAdapter=new NewsAdapter();
        //=2.3、设置recyclerView的适配器
        rvNewsList.setAdapter(mNewsAdapter);
        mNewsAdapter.setItemListener(new NewsAdapter.ItemListener() {
            @Override
            public void ItemClick(News news) {
                Intent intent = new Intent(myActivity, NewsDetailActivity.class);
                intent.putExtra("news",news);
                startActivity(intent);
            }
        });
        tabNewsTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                newsTypeId = newsState[tab.getPosition()];
                loadNewsData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    //初始化科普
    private void initKnowledgeView() {
        tabKnowledgeTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tablayout距离上下左右的距离
        //为TabLayout添加tab名称
        for (int i=0;i<newsTitle.length;i++){
            tabKnowledgeTitle.addTab(tabKnowledgeTitle.newTab().setText(knowledgeTitle[i]));
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvKnowledgeList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        knowledgeAdapter=new KnowledgeAdapter();
        //=2.3、设置recyclerView的适配器
        rvKnowledgeList.setAdapter(knowledgeAdapter);
        knowledgeAdapter.setItemListener(new KnowledgeAdapter.ItemListener() {
            @Override
            public void ItemClick(Knowledge knowledge) {
                Intent intent = new Intent(myActivity, KnowledgeDetailActivity.class);
                intent.putExtra("knowledge",knowledge);
                startActivity(intent);
            }

            @Override
            public void DeleteClick(Knowledge knowledge) {

            }
        });
        tabKnowledgeTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                knowledgeTypeId = knowledgeState[tab.getPosition()];
                loadKnowledgeData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //加载动态数据
    private void loadNewsData() {
        newsList = new ArrayList<>();
        String sql = "select * from news where typeId = ?";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{newsTypeId});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer typeId = cursor.getInt(1);
                String title = cursor.getString(2);
                String img = cursor.getString(3);
                String content = cursor.getString(4);
                String date = cursor.getString(5);
                News news = new News(dbId,typeId, title,img,content,date);
                newsList.add(news);
            }
        }
        db.close();
        if (newsList != null && newsList.size() > 0) {
            rvNewsList.setVisibility(View.VISIBLE);
            llNewsEmpty.setVisibility(View.GONE);
            mNewsAdapter.addItem(newsList);
        } else {
            rvNewsList.setVisibility(View.GONE);
            llNewsEmpty.setVisibility(View.VISIBLE);
        }
    }

    //加载科普数据
    private void loadKnowledgeData() {
        knowledgeList = new ArrayList<>();
        String sql = "select * from knowledge where typeId = ?";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{knowledgeTypeId});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer typeId = cursor.getInt(1);
                String title = cursor.getString(2);
                String img = cursor.getString(3);
                String content = cursor.getString(4);
                String date = cursor.getString(5);
                Knowledge knowledge = new Knowledge(dbId,typeId, title,img,content,date);
                knowledgeList.add(knowledge);
            }
        }
        db.close();
        if (knowledgeList != null && knowledgeList.size() > 0) {
            rvKnowledgeList.setVisibility(View.VISIBLE);
            llKnowledgeEmpty.setVisibility(View.GONE);
            knowledgeAdapter.addItem(knowledgeList);
        } else {
            rvKnowledgeList.setVisibility(View.GONE);
            llKnowledgeEmpty.setVisibility(View.VISIBLE);
        }
    }

}
