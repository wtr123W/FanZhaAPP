package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.example.counterfraud.R;
import com.example.counterfraud.adapter.VideoAdapter;
import com.example.counterfraud.bean.Video;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 反诈视频
 */
public class VideoActivity extends AppCompatActivity implements VideoAdapter.OnItemChildClickListener {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty1;
    private RecyclerView rvList1;
    private List<Video> videoEntityList = new ArrayList<>();
    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;
    private VideoAdapter videoAdapter;
    private EditText etQuery;
    private ImageView ivSearch;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity =this;
        setContentView(R.layout.activity_video);
        helper = new MySqliteOpenHelper(myActivity);
        rvList1 = findViewById(R.id.rv_list);
        llEmpty1 = findViewById(R.id.ll_empty);
        etQuery = findViewById(R.id.et_query);
        ivSearch = findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseVideoView();
                initVideoView();
            }
        });
        //获取控件
        initVideoView();
    }


    //加载数据
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(myActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList1.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(myActivity);
        videoAdapter.setOnItemChildClickListener(this);
        rvList1.setAdapter(videoAdapter);
        rvList1.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });
        videoAdapter.setItemListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        getVideoList();
    }



    //初始化播放器
    protected void initVideoView() {
        mVideoView = new VideoView(myActivity);
        mVideoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    // Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(myActivity);
        mErrorView = new ErrorView(myActivity);
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(myActivity);
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(myActivity);
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(myActivity));
        mController.addControlComponent(new GestureView(myActivity));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);

        initData();
    }

    /**
     * 开始播放
     *
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        Video video = videoEntityList.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(video.getPlayUrl());
        mTitleView.setTitle(video.getTitle());
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list");
        mVideoView.start();
        mCurPos = position;

    }

    private void getVideoList() {
        String content = etQuery.getText().toString();
        videoEntityList = new ArrayList<>();
        Video video = null;
        String sql = "select * from video";
        if (!"".equals(content)) {
            sql += " where title like ?";
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,!"".equals(content) ? new String[]{"%"+content+"%"} : null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String title = cursor.getString(1);
                String coverUrl = cursor.getString(2);
                String playUrl = cursor.getString(3);
                video = new Video(dbId, title,coverUrl, playUrl);
                videoEntityList.add(video);
            }
        }
        if (videoEntityList != null && videoEntityList.size() > 0) {
            rvList1.setVisibility(View.VISIBLE);
            llEmpty1.setVisibility(View.GONE);
            videoAdapter.addItem(videoEntityList);
        } else {
            rvList1.setVisibility(View.GONE);
            llEmpty1.setVisibility(View.VISIBLE);
        }
    }

    //释放
    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (myActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }

    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoList();
        resume();
    }
    /**
     * PrepareView被点击
     */
    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }
    public void back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseVideoView();
    }
}
