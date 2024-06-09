package com.example.counterfraud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.counterfraud.R;
import com.example.counterfraud.bean.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private List<Report> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_report_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Report report = list.get(i);
        if (report != null) {
            viewHolder.number.setText(String.format("报案编号:%s",report.getNumber()));
            viewHolder.name.setText(String.format("姓名:%s",report.getName()));
            viewHolder.sex.setText(String.format("性别:%s",report.getSex()));
            viewHolder.phone.setText(String.format("手机号:%s",report.getPhone()));
            viewHolder.address.setText(String.format("联系地址:%s",report.getAddress()));
            viewHolder.reportTime.setText(String.format("报案时间:%s",report.getReportTime()));
            viewHolder.crimeTime.setText(String.format("案发时间:%s",report.getCrimeTime()));
            viewHolder.crimeAddress.setText(String.format("案发地址:%s",report.getCrimeAddress()));
            viewHolder.content.setText(String.format("报案内容:%s",report.getContent()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(report);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.Delete(report.getId());
                    }

                    return false;
                }
            });
        }
    }

    public void addItem(List<Report> listAdd) {
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
        private TextView number;
        private TextView name;
        private TextView sex;
        private TextView phone;
        private TextView address;
        private TextView reportTime;
        private TextView crimeTime;
        private TextView crimeAddress;
        private TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            sex = itemView.findViewById(R.id.sex);
            phone = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.address);
            reportTime = itemView.findViewById(R.id.reportTime);
            crimeTime = itemView.findViewById(R.id.crimeTime);
            crimeAddress = itemView.findViewById(R.id.crimeAddress);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface ItemListener{
        void ItemClick(Report report);
        void Delete(Integer id);
    }
}
