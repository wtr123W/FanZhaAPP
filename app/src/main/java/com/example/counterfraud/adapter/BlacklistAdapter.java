package com.example.counterfraud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counterfraud.R;
import com.example.counterfraud.bean.Blacklist;

import java.util.ArrayList;
import java.util.List;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.ViewHolder> {
    private List<Blacklist> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_blacklist_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Blacklist blacklist = list.get(i);
        if (blacklist != null) {
            viewHolder.phone.setText(blacklist.getPhone());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(blacklist);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.Delete(blacklist.getId());
                    }

                    return false;
                }
            });
        }
    }

    public void addItem(List<Blacklist> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd != null) {
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.phone);
        }
    }

    public interface ItemListener{
        void ItemClick(Blacklist blacklist);
        void Delete(Integer id);
    }
}
