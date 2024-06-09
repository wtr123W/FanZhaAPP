package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.counterfraud.R;
import com.example.counterfraud.adapter.CommentAdapter;
import com.example.counterfraud.bean.CommentVo;
import com.example.counterfraud.bean.CommunityVo;
import com.example.counterfraud.bean.Praise;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 明细信息
 */
public class CommunityDetailActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity mActivity;
    private ImageView ivImg;
    private TextView tvTitle;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvContent;
    private EditText etContent;
    private TextView tvLike;
    private ImageView ivLike;
    private ImageView ivLikeCheck;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Integer userId;
    private SQLiteDatabase db;
    private CommunityVo community;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private CommentAdapter commentAdapter;
    private int praiseCount = 0;//点赞数
    private int collectCount = 0;//收藏数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_community_detail);
        helper = new MySqliteOpenHelper(this);
        ivImg = findViewById(R.id.img);
        tvTitle = findViewById(R.id.title);
        tvName = findViewById(R.id.name);
        tvDate = findViewById(R.id.date);
        tvContent = findViewById(R.id.content);
        etContent = findViewById(R.id.et_content);
        tvLike = findViewById(R.id.tv_like);
        ivLike = findViewById(R.id.iv_like);
        ivLikeCheck = findViewById(R.id.iv_like_check);
        llEmpty = findViewById(R.id.ll_empty);
        rvList = findViewById(R.id.rv_list);
        userId = (Integer) SPUtils.get(mActivity, SPUtils.USER_ID, 0);
        community = (CommunityVo) getIntent().getSerializableExtra("community");
        tvTitle.setText(community.getTitle());
        tvName.setText(String.format("发布人：%s",community.getName()));
        tvDate.setText(community.getDate());
        tvContent.setText(community.getContent());
        Glide.with(mActivity)
                .asBitmap()
                .skipMemoryCache(true)
                .load(community.getImg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImg);
        db = helper.getWritableDatabase();

        if (userId > 0){
            //查询点赞状态
            String selectPraiseSql = "select * from praise where articleId=" + community.getId() +" and userId=" + userId;
            Praise praise = null;
            Cursor cursorPraise = db.rawQuery(selectPraiseSql, null);
            if (cursorPraise != null && cursorPraise.getColumnCount() > 0) {
                while (cursorPraise.moveToNext()) {
                    Integer dbId = cursorPraise.getInt(0);
                    Integer dbArticleId = cursorPraise.getInt(1);
                    Integer dbUserId = cursorPraise.getInt(2);
                    praise = new Praise(dbId, dbArticleId, dbUserId);
                }
            }
            ivLike.setVisibility(praise == null ? View.VISIBLE : View.GONE);
            ivLikeCheck.setVisibility(praise == null ? View.GONE : View.VISIBLE);
        }

        //查看点赞数量
        String selectPraiseCountSql = "select count(*) from praise where articleId=" + community.getId();

        Cursor cursorPraiseCount = db.rawQuery(selectPraiseCountSql, null);
        if (cursorPraiseCount != null && cursorPraiseCount.getColumnCount() > 0) {
            while (cursorPraiseCount.moveToNext()) {
                praiseCount = cursorPraiseCount.getInt(0);
            }
        }
        tvLike.setText(String.valueOf(praiseCount));

        //查询评论列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter();
        rvList.setAdapter(commentAdapter);
        commentAdapter.setItemListener(new CommentAdapter.ItemListener() {
            @Override
            public void ItemClick(CommentVo comment) {

            }

            @Override
            public void Delete(CommentVo comment) {
                if (userId.intValue() == comment.getUserId().intValue()){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                    dialog.setMessage("确认要删除该评论吗");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = helper.getWritableDatabase();
                            if (db.isOpen()) {
                                db.execSQL("delete from comment where id = " + comment.getId());
                            }
                            Toast.makeText(mActivity, "删除成功", Toast.LENGTH_LONG).show();
                            getCommentList();
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
            }
        });
        getCommentList();
        //点赞
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId == 0) {////未登录,跳转到登录页面
                    Toast.makeText(mActivity, "请登录后操作", Toast.LENGTH_SHORT).show();
                }else {//已经登录
                    String insertSql = "insert into praise(articleId,userId) values(?,?)";
                    db.execSQL(insertSql, new Object[]{community.getId(), userId});
                    Toast.makeText(mActivity, "点赞成功", Toast.LENGTH_SHORT).show();
                    tvLike.setText(String.valueOf(++praiseCount));
                    ivLike.setVisibility(View.GONE);
                    ivLikeCheck.setVisibility(View.VISIBLE);
                }

            }
        });
        //取消点赞
        ivLikeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId == 0) {////未登录,跳转到登录页面
                    Toast.makeText(mActivity, "请登录后操作", Toast.LENGTH_SHORT).show();
                }else {//已经登录
                    String deleteSql = "delete from praise where articleId=? and userId =? ";
                    db.execSQL(deleteSql, new Object[]{community.getId(), userId});
                    Toast.makeText(mActivity, "取消成功", Toast.LENGTH_SHORT).show();
                    tvLike.setText(String.valueOf(--praiseCount));
                    ivLike.setVisibility(View.VISIBLE);
                    ivLikeCheck.setVisibility(View.GONE);
                }

            }
        });
    }

    /**
     * 获取评论列表
     */
    private void getCommentList() {
        String commentSql = "select c.*,u.name,u.photo from comment c,user u where c.userId = u.id and c.articleId=" + community.getId();
        List<CommentVo> list = new ArrayList<>();
        Cursor cursor1 = db.rawQuery(commentSql, null);
        if (cursor1 != null && cursor1.getColumnCount() > 0) {
            while (cursor1.moveToNext()) {
                Integer dbId = cursor1.getInt(0);
                Integer dbArticleId = cursor1.getInt(1);
                Integer dbUserId = cursor1.getInt(2);
                String dbContent = cursor1.getString(3);
                String dbDate = cursor1.getString(4);
                String nickName = cursor1.getString(5);
                String photo = cursor1.getString(6);
                CommentVo comment = new CommentVo(dbId, dbArticleId, dbUserId, dbContent, dbDate, nickName,photo);
                list.add(comment);
            }
        }
        if (list.size() > 0) {
            commentAdapter.addItem(list);
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    //发表
    public void publish(View view) {
        if (userId == 0) {////未登录,跳转到登录页面
            Toast.makeText(mActivity, "请登录后操作", Toast.LENGTH_SHORT).show();
        }else {//已经登录
            String content = etContent.getText().toString();
            if ("".equals(content)) {
                Toast.makeText(mActivity, "请输入评论内容", Toast.LENGTH_SHORT).show();
                return;
            }
            String insertSql = "insert into comment(articleId,userId,content,date) values(?,?,?,?)";
            db.execSQL(insertSql, new Object[]{community.getId(), userId, content, sf.format(new Date())});
            Toast.makeText(mActivity, "发表成功", Toast.LENGTH_SHORT).show();
            etContent.setText("");
            getCommentList();
        }
    }
    public void back(View view) {
        finish();
    }
}
