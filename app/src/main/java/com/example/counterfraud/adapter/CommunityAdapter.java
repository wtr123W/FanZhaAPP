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
import com.example.counterfraud.bean.CommunityVo;

import java.util.ArrayList;
import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private List<CommunityVo> list =new ArrayList<>();
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
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_communtiy_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CommunityVo community = list.get(i);
        if (community != null) {
            viewHolder.tvTitle.setText(community.getTitle());
            viewHolder.tvName.setText(community.getName());
            viewHolder.tvDate.setText(community.getDate());
            Glide.with(mActivity)
                    .asBitmap()
                    .load(community.getImg())
                    .error(R.drawable.ic_error)
                    .into(viewHolder.ivImage);
            Glide.with(mActivity)
                    .asBitmap()
                    .load(community.getPhoto())
                    .apply(headerRO.error(R.drawable.ic_default_man))
                    .into(viewHolder.ivPhoto);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(community);
                    }
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemListener.Delete(community);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<CommunityVo> listAdd) {
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
        private ImageView ivImage;
        private ImageView ivPhoto;
        private TextView tvTitle;
        private TextView tvName;
        private TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.img);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvTitle = itemView.findViewById(R.id.title);
            tvName = itemView.findViewById(R.id.name);
            tvDate = itemView.findViewById(R.id.date);
        }
    }

    public interface ItemListener{
        void ItemClick(CommunityVo community);
        void Delete(CommunityVo community);
    }
}
