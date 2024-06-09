package com.example.counterfraud.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.counterfraud.R;
import com.example.counterfraud.bean.User;
import com.example.counterfraud.ui.activity.AboutActivity;
import com.example.counterfraud.ui.activity.BlacklistActivity;
import com.example.counterfraud.ui.activity.LoginActivity;
import com.example.counterfraud.ui.activity.MyCommunityActivity;
import com.example.counterfraud.ui.activity.PasswordActivity;
import com.example.counterfraud.ui.activity.PersonActivity;
import com.example.counterfraud.ui.activity.ReportActivity;
import com.example.counterfraud.ui.activity.UserMessageActivity;
import com.example.counterfraud.util.GlideEngine;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.List;


/**
 * 我的
 */
public class UserFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity mActivity;
    private ImageView ivPhoto;
    private TextView tvNickName;
    private LinearLayout llPerson;
    private LinearLayout llSecurity;
    private LinearLayout llReport;
    private LinearLayout llBlacklist;
    private LinearLayout llShare;
    private LinearLayout llHelp;
    private LinearLayout llAbout;
    private Button btnLogout;
    private String imagePath = "";
    private User mUser = null;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    private Integer userId;
    private Integer userType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        helper = new MySqliteOpenHelper(mActivity);
        ivPhoto = view.findViewById(R.id.iv_photo);
        tvNickName = view.findViewById(R.id.tv_nickName);
        llPerson = view.findViewById(R.id.person);
        llSecurity = view.findViewById(R.id.security);
        llReport = view.findViewById(R.id.report);
        llBlacklist = view.findViewById(R.id.blacklist);
        llShare = view.findViewById(R.id.share);
        llHelp = view.findViewById(R.id.help);
        llAbout = view.findViewById(R.id.about);
        btnLogout = view.findViewById(R.id.logout);
        initData();
        initView();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userId = (Integer) SPUtils.get(mActivity,SPUtils.USER_ID,0);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from user where id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String dbAccount = cursor.getString(1);
                String dbPassword = cursor.getString(2);
                String dbName = cursor.getString(3);
                String dbSex = cursor.getString(4);
                String dbPhone = cursor.getString(5);
                String dbRoom = cursor.getString(6);
                String dbPhoto = cursor.getString(7);
                Integer isEnable = cursor.getInt(8);
                mUser = new User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone, dbRoom, dbPhoto,isEnable);
            }
        }
        db.close();
        tvNickName.setText(mUser.getName());
        Glide.with(mActivity)
                .load(mUser.getPhoto())
                .apply(headerRO.error("男".equals(mUser.getSex()) ? R.drawable.ic_default_man : R.drawable.ic_default_woman))
                .into(ivPhoto);
    }

    private void initView() {
        //从相册中选择头像
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClick();
            }
        });
        //个人信息
        llPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, PersonActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
        //账号安全
        llSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, PasswordActivity.class);
                startActivity(intent);
            }
        });
        //我的报案
        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, ReportActivity.class);
                startActivity(intent);
            }
        });
        //我的分享
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, MyCommunityActivity.class);
                startActivity(intent);
            }
        });
        //拉黑号码
        llBlacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, BlacklistActivity.class);
                startActivity(intent);
            }
        });
        //帮助反馈
        llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转页面
                Intent intent;
                intent = new Intent(mActivity, UserMessageActivity.class);
                startActivity(intent);

            }
        });
        //退出登录
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.remove(mActivity,SPUtils.USER_ID);
                startActivity(new Intent(mActivity, LoginActivity.class));
                mActivity.finish();
            }
        });
        //关于
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转页面
                Intent intent = new Intent(mActivity, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 选择图片
     */
    private void selectClick() {
        SQLiteDatabase db = helper.getWritableDatabase();
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
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
                                    path = PictureFileUtils.getPath(mActivity, Uri.parse(media.getPath()));
                                } else {
                                    path = media.getPath();
                                }
                            }
                            imagePath = path;
                        }
                        Glide.with(mActivity)
                                .load(imagePath)
                                .apply(headerRO.error("男".equals(mUser.getSex()) ? R.drawable.ic_default_man : R.drawable.ic_default_woman))
                                .into(ivPhoto);
                        db.execSQL("update user set photo = ? where id = ?", new Object[]{imagePath,mUser.getId()});
                        Toast.makeText(mActivity,"更新成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }

}
