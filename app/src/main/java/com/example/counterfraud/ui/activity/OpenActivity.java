package com.example.counterfraud.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.counterfraud.R;
import com.example.counterfraud.util.MPermissionUtils;
import com.example.counterfraud.util.MySqliteOpenHelper;
import com.example.counterfraud.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 欢迎页
 */
public class OpenActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Button in;
    private Boolean isFirst;
    private Integer userId;
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    //权限组（读写权限）
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ANSWER_PHONE_CALLS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        helper = new MySqliteOpenHelper(this);
        in = findViewById(R.id.in);
        userId = (Integer) SPUtils.get(OpenActivity.this, SPUtils.USER_ID,0);
        isFirst = (Boolean) SPUtils.get(OpenActivity.this,SPUtils.IF_FIRST,true);
        //请求权限
        MPermissionUtils.requestPermissionsResult(this,
                REQUEST_EXTERNAL_STORAGE,
                PERMISSIONS_STORAGE,
                new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        CountDownTimer timer = new CountDownTimer(5000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                in.setText(millisUntilFinished / 1000 + "秒");
                            }
                            public void onFinish() {
                                finishView();
                            }
                        };
                        timer.start();
                        in.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finishView();
                                timer.cancel();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied() {//拒绝的话
                        finish();//退出
                    }
                });


    }

    private void finishView(){
        if (isFirst){//第一次进来  初始化本地数据

            SQLiteDatabase db = helper.getWritableDatabase();
            SPUtils.put(OpenActivity.this,SPUtils.IF_FIRST,false);//第一次
            //初始化数据
            //获取json数据
            String rewardJson = "";
            String rewardJsonLine;
            //assets文件夹下db.json文件的路径->打开db.json文件
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(OpenActivity.this.getAssets().open("db.json")));
                while (true) {
                    if (!((rewardJsonLine = bufferedReader.readLine()) != null)) break;
                    rewardJson += rewardJsonLine;
                }
                JSONObject jsonObject = new JSONObject(rewardJson);
                JSONArray videoList = jsonObject.getJSONArray("video");
                JSONArray newsList = jsonObject.getJSONArray("news");
                JSONArray knowledgeList = jsonObject.getJSONArray("knowledge");
                JSONArray blacklistList = jsonObject.getJSONArray("blacklist");
                for (int i = 0, length = videoList.length(); i < length; i++) {//初始化
                    JSONObject o = videoList.getJSONObject(i);
                    String title = o.getString("title");
                    String coverUrl = o.getString("coverUrl");
                    String playUrl = o.getString("playUrl");
                    String insertSql = "insert into video(title,coverUrl,playUrl) values(?,?,?)";
                    db.execSQL(insertSql,new Object[]{title,coverUrl,playUrl});
                }
                for (int i = 0, length = newsList.length(); i < length; i++) {//初始化
                    JSONObject o = newsList.getJSONObject(i);
                    String typeId = o.getString("typeId");
                    String title = o.getString("title");
                    String img = o.getString("img");
                    String content = o.getString("content");
                    String date = o.getString("date");
                    String insertSql = "insert into news(typeId,title,img,content,date) values(?,?,?,?,?)";
                    db.execSQL(insertSql,new Object[]{typeId,title,img,content,date});
                }
                for (int i = 0, length = knowledgeList.length(); i < length; i++) {//初始化
                    JSONObject o = knowledgeList.getJSONObject(i);
                    String typeId = o.getString("typeId");
                    String title = o.getString("title");
                    String img = o.getString("img");
                    String content = o.getString("content");
                    String date = o.getString("date");
                    String insertSql = "insert into knowledge(typeId,title,img,content,date) values(?,?,?,?,?)";
                    db.execSQL(insertSql,new Object[]{typeId,title,img,content,date});
                }
                for (int i = 0, length = blacklistList.length(); i < length; i++) {//初始化
                    JSONObject o = blacklistList.getJSONObject(i);
                    Integer userId = o.getInt("userId");
                    String phone = o.getString("phone");
                    String status = o.getString("status");
                    String insertSql = "insert into blacklist(userId,phone,status) values(?,?,?)";
                    db.execSQL(insertSql,new Object[]{userId,phone,status});
                }
                db.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        //两秒后跳转到主页面
        Intent intent = new Intent();
        if (userId > 0) {//已登录
            intent.setClass(OpenActivity.this, MainActivity.class);
        }else {
            intent.setClass(OpenActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }




    //权限请求的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {

    }

}