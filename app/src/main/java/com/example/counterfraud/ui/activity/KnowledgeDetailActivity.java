package com.example.counterfraud.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.counterfraud.R;
import com.example.counterfraud.bean.Knowledge;
import com.example.counterfraud.enums.KnowledgeTypeEnum;

import java.text.SimpleDateFormat;

/**
 * 明细信息
 */
public class KnowledgeDetailActivity extends AppCompatActivity {
    private Activity mActivity;
    private ImageView ivImg;
    private TextView tv_title;
    private TextView tvTitle;
    private TextView tvDate;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Knowledge knowledge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_knowledge_detail);
        ivImg = findViewById(R.id.img);
        tv_title= findViewById(R.id.tv_title);
        tvTitle= findViewById(R.id.title);
        tvDate = findViewById(R.id.date);
        knowledge = (Knowledge) getIntent().getSerializableExtra("knowledge");
        tvTitle.setText(knowledge.getTitle());
        tvDate.setText(knowledge.getDate());
        Glide.with(mActivity)
                .asBitmap()
                .skipMemoryCache(true)
                .load(knowledge.getContent())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImg);
        tv_title.setText(KnowledgeTypeEnum.getName(knowledge.getTypeId()));
    }

    public void back(View view){
        finish();
    }
}
