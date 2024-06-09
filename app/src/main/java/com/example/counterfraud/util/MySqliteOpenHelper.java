package com.example.counterfraud.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * description :数据库管理类,负责管理数据库的创建、升级工作
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper {
    //数据库名字
    public static final String DB_NAME = "counterfraud.db";

    //数据库版本
    public static final int DB_VERSION = 1;
    private Context context;

    public MySqliteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    /**
     * 在数据库首次创建的时候调用，创建表以及可以进行一些表数据的初始化
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        //_id为主键并且自增长一般命名为_id

        String userSql = "create table user(id integer primary key autoincrement,account, password,name,sex, phone,room,photo,isEnable)";
        String messageSql = "create table message(id integer primary key autoincrement,userId,content,date)";
        String communitySql = "create table community(id integer primary key autoincrement,userId,title,img,content,date)";//论坛
        String praiseSql = "create table praise(id integer primary key autoincrement,articleId,userId)";//点赞
        String commentSql = "create table comment(id integer primary key autoincrement,articleId,userId,content,date)";//评论
        String newsSql = "create table news(id integer primary key autoincrement,typeId,title,img,content,date)";//动态
        String knowledgeSql = "create table knowledge(id integer primary key autoincrement,typeId,title,img,content,date)";//科普知识
        String videoSql = "create table video(id integer primary key autoincrement,title, coverUrl,playUrl)";//视频
        String blacklistSql = "create table blacklist(id integer primary key autoincrement,userId,phone,status)";//黑名单
        String reportSql = "create table report (id integer primary key autoincrement,userId,number,name,sex,phone,address,reportTime,crimeTime,crimeAddress,content)";//报案
        db.execSQL(userSql);
        db.execSQL(messageSql);
        db.execSQL(communitySql);
        db.execSQL(praiseSql);
        db.execSQL(commentSql);
        db.execSQL(newsSql);
        db.execSQL(knowledgeSql);
        db.execSQL(videoSql);
        db.execSQL(blacklistSql);
        db.execSQL(reportSql);

        //初始化管理员数据
        String insertSql = "insert into user(account,password,name,sex,phone,room,photo,isEnable) values(?,?,?,?,?,?,?,?)";
        db.execSQL(insertSql, new Object[]{"admin", "123", "管理员", "男", "17620885471",  "广东省", "", 0});
    }

    /**
     * 数据库升级的时候回调该方法，在数据库版本号DB_VERSION升级的时候才会调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //给表添加一个字段
        //db.execSQL("alter table person add age integer");
    }

    /**
     * 数据库打开的时候回调该方法
     *
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}

