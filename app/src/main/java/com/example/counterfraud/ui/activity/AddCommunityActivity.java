package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.counterfraud.R;
import com.example.counterfraud.bean.CommunityVo;
import com.example.counterfraud.util.GlideEngine;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 添加修改页面
 */
public class AddCommunityActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private EditText etTitle;//标题
    private ImageView ivPhoto;//图片
    private EditText etContent;//内容
    private String imagePath;//图片地址
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private CommunityVo community;
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        setContentView(R.layout.activity_community_add);
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        etTitle = findViewById(R.id.title);
        ivPhoto = findViewById(R.id.iv_photo);
        etContent = findViewById(R.id.content);
        //选择图片
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectClick();
            }
        });
    }

    private void initView() {
        community = (CommunityVo) getIntent().getSerializableExtra("community");
        if (community != null) {
            etTitle.setText(community.getTitle());
            etContent.setText(community.getContent());
            imagePath = community.getImg();
            Glide.with(myActivity)
                    .asBitmap()
                    .load(community.getImg())
                    .error(R.drawable.ic_add)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivPhoto);
        }
    }

    public void save(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if ("".equals(title)) {
            Toast.makeText(myActivity, "标题不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(imagePath)) {
            Toast.makeText(myActivity, "图片不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(content)) {
            Toast.makeText(myActivity, "感受不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (community == null) {//新增
            String insertSql = "insert into community(userId,title,img,content,date) values(?,?,?,?,?)";
            db.execSQL(insertSql, new Object[]{userId, title, imagePath, content,sf.format(new Date())});
        } else {//修改
            String updateSql = "update community set userId=?,title=?,img=?,content=? where id =?";
            db.execSQL(updateSql, new Object[]{userId, title, imagePath, content, community.getId()});
        }
        Toast.makeText(myActivity, "保存成功", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
        db.close();
    }

    /**
     * 选择图片
     */
    private void selectClick() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//只选择图片
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE )// 多选 or 单选
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        for (int i = 0; i < result.size(); i++) {
                            // onResult Callback
                            LocalMedia media = result.get(i);
                            String path;
                            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                            boolean compressPath = media.isCompressed() || (media.isCut() && media.isCompressed());
                            // 裁剪过
                            boolean isCutPath = media.isCut() && !media.isCompressed();

                            if (isCutPath) {
                                path = media.getCutPath();
                            } else if (compressPath) {
                                path = media.getCompressPath();
                            } else if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                                // AndroidQ特有path
                                path = media.getAndroidQToPath();
                            } else if (!TextUtils.isEmpty(media.getRealPath())) {
                                // 原图
                                path = media.getRealPath();
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    path = PictureFileUtils.getPath(myActivity, Uri.parse(media.getPath()));
                                } else {
                                    path = media.getPath();
                                }
                            }
                            imagePath = path;
                            Glide.with(myActivity).load(imagePath).into(ivPhoto);
                        }
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }
    public void back(View view){
        finish();
    }
}

