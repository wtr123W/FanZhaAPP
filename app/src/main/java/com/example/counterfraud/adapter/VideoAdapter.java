package com.example.counterfraud.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.component.PrepareView;
import com.example.counterfraud.R;
import com.example.counterfraud.bean.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Video> datas=new ArrayList<>();

    public void addItem(List<Video> datas) {
        //如果是加载第一页，需要先清空数据列表
        this.datas.clear();
        if (datas!=null){
            //添加数据
            this.datas.addAll(datas);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }

    private OnItemChildClickListener mOnItemChildClickListener;

    private OnItemClickListener mOnItemClickListener;
    private OnItemClickListener mItemListener;
    public void setItemListener(OnItemClickListener itemListener){
        this.mItemListener = itemListener;
    }


    public VideoAdapter(Context context) {
        this.mContext = context;
    }

    public VideoAdapter(Context context, List<Video> datas) {
        this.mContext = context;
        this.datas = datas;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ViewHolder vh = (ViewHolder) holder;
        Video videoEntity = datas.get(position);
        vh.tvTitle.setText(videoEntity.getTitle());
        Picasso.with(mContext)
                .load(videoEntity.getCoverUrl())
                .into(vh.mThumb);
        vh.mPosition = position;
    }

    @Override
    public int getItemCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        public ImageView mThumb;
        public Button btnUpdate;
        public Button btnDelete;
        public PrepareView mPrepareView;
        public FrameLayout mPlayerContainer;
        public int mPosition;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            mPlayerContainer = view.findViewById(R.id.player_container);
            mPrepareView = view.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(com.dueeeke.videocontroller.R.id.thumb);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                view.setOnClickListener(this);
            }
            //通过tag将ViewHolder和itemView绑定
            view.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }
        }
    }


    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
