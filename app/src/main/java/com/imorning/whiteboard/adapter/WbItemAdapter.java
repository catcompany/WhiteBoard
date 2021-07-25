package com.imorning.whiteboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.imorning.whiteboard.R;
import com.imorning.whiteboard.bean.FileListData;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class WbItemAdapter extends RecyclerView.Adapter<WbItemAdapter.ItemViewHolder> {

    private final List<FileListData> fileListDataList;
    private final LayoutInflater layoutInflater;

    //声明自定义的监听接口
    private onItemClickListener onItemClickListener;
    private onItemLongClickListener onItemLongClickListener;

    public WbItemAdapter(Context context, List<FileListData> fileListDataList) {
        this.fileListDataList = fileListDataList;
        layoutInflater = LayoutInflater.from(context);
    }

    //提供set方法供Activity或Fragment调用
    public void setOnItemClickListener(onItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.wb_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        FileListData fileListData = fileListDataList.get(position);
        holder.textView.setText(fileListData.getTitle());
    }

    @Override
    public int getItemCount() {
        return fileListDataList == null ? 0 : fileListDataList.size();
    }

    public interface onItemClickListener {
        void onItemClick(int Position, List<FileListData> fileListDataList);
    }

    public interface onItemLongClickListener {
        void onItemLongClick(int Position, List<FileListData> fileListDataList);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatTextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.tv_wb_name);

            //将监听传递给自定义接口
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition(), fileListDataList);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(getAdapterPosition(), fileListDataList);
                        return true;
                    }
                    return false;
                }
            });

        }
    }

}