package com.example.counterfraud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.counterfraud.R;
import com.example.counterfraud.bean.CommentVo;
import com.example.counterfraud.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentVo> list =new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_comment_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CommentVo comment = list.get(i);
        if (comment != null) {
            Glide.with(mActivity)
                    .asBitmap()
                    .load(comment.getPhoto())
                    .apply(headerRO.error(R.drawable.ic_default_man))
                    .into(viewHolder.img);
            viewHolder.tvName.setText(comment.getName());
            viewHolder.tvDate.setText(comment.getDate());
            viewHolder.tvContent.setText(comment.getContent());
            Integer userId = (Integer) SPUtils.get(mActivity,SPUtils.USER_ID,0);
            if (userId.intValue() == comment.getUserId().intValue()){//自己数据可以删除
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mItemListener!=null){
                            mItemListener.Delete(comment);
                        }
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<CommentVo> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }

    public interface ItemListener{
        void ItemClick(CommentVo comment);
        void Delete(CommentVo comment);
    }
}
