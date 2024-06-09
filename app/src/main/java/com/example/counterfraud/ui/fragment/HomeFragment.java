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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.counterfraud.R;
import com.example.counterfraud.adapter.NewsAdapter;
import com.example.counterfraud.bean.News;
import com.example.counterfraud.enums.NewsTypeEnum;
import com.example.counterfraud.ui.activity.AddReportActivity;
import com.example.counterfraud.ui.activity.InterceptActivity;
import com.example.counterfraud.ui.activity.NewsDetailActivity;
import com.example.counterfraud.ui.activity.VerifyActivity;
import com.example.counterfraud.ui.activity.VideoActivity;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */

public class HomeFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private LinearLayout ll_video;
    private LinearLayout ll_report;
    private LinearLayout ll_intercept;
    private LinearLayout ll_verify;
    private Banner mBanner;//轮播顶部
    private RecyclerView rvNewsList;
    private NewsAdapter mNewsAdapter;
    private List<News> newsList;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        helper = new MySqliteOpenHelper(myActivity);
        ll_video = view.findViewById(R.id.ll_video);
        ll_report = view.findViewById(R.id.ll_report);
        ll_intercept = view.findViewById(R.id.ll_intercept);
        ll_verify = view.findViewById(R.id.ll_verify);
        mBanner = view.findViewById(R.id.banner);
        rvNewsList = view.findViewById(R.id.rv_list);
        //获取控件
        initView();
        initNewsView();
        return view;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        //图片资源
        int[] imageResourceID = new int[]{R.drawable.i_a, R.drawable.i_b, R.drawable.i_c};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            //设置图片加载器，通过Glide加载图片
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(myActivity).load(path).into(imageView);
                }
            });
            //设置轮播的动画效果,里面有很多种特效,可以到GitHub上查看文档。
            mBanner.setImages(imgeList);//设置图片资源
            //设置指示器位置（即图片下面的那个小圆点）
            mBanner.setDelayTime(3000);//设置轮播时间3秒切换下一图
            mBanner.start();//开始进行banner渲染
        }
        ll_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, VideoActivity.class);
                startActivity(intent);
            }
        });

        ll_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, AddReportActivity.class);
                startActivity(intent);
            }
        });

        ll_intercept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, InterceptActivity.class);
                startActivity(intent);

            }
        });

        ll_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, VerifyActivity.class);
                startActivity(intent);
            }
        });
    }

    //初始化动态
    private void initNewsView() {
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
        loadNewsData();
    }


    //加载动态数据
    private void loadNewsData() {
        newsList = new ArrayList<>();
        String sql = "select * from news where typeId != ? order by date desc";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(NewsTypeEnum.A0.getCode())});
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

        mNewsAdapter.addItem(newsList);
    }
}
