package com.imorning.whiteboard.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.imorning.whiteboard.R;
import com.imorning.whiteboard.databinding.ActivityMainBinding;
import com.imorning.whiteboard.utils.FileUtil;
import com.imorning.whiteboard.utils.OperationUtils;
import com.imorning.whiteboard.utils.StoreUtil;
import com.imorning.whiteboard.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ArrayList<String> filenames;
    ArrayList<String> filePaths;
    private ActivityMainBinding binding;
    private WbAdapter mWbAdapter;
    private long mBackPressedTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadData();
        initView();
    }

    private void loadData() {
        File folder = new File(StoreUtil.getWbPath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File[] files = folder.listFiles();
        if (files.length > 0) {
            filenames = new ArrayList<String>();
            filePaths = new ArrayList<String>();
            for (File f : files) {
                filenames.add(FileUtil.getFileName(f));
                filePaths.add(f.getAbsolutePath());
            }
        }

    }

    private void initView() {
        mWbAdapter = new WbAdapter();
        binding.lvWb.setAdapter(mWbAdapter);
        binding.ivWbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperationUtils.getInstance().initDrawPointList();
                navi2Page(WhiteBoardActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final long mCurrentTime = System.currentTimeMillis();
        if (mCurrentTime - this.mBackPressedTime > 1000) {
            ToastUtils.showToast(this, R.string.app_logout);
            this.mBackPressedTime = mCurrentTime;
            return;
        }
        super.onBackPressed();
        System.exit(0);
    }

    private class WbAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return filenames != null ? filenames.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            WbViewHolder holder = null;
            if (convertView != null) {
                holder = (WbViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.wb_item, null);
                if (convertView != null) {
                    convertView.setTag(
                            holder = new WbViewHolder(convertView)
                    );
                }
                if (holder != null) {
                    holder.nWbName.setText(filenames.get(position));
                    convertView.setOnClickListener(v -> {
                        StoreUtil.readWhiteBoardPoints(filePaths.get(position));
                        navi2Page(WhiteBoardActivity.class);
                    });
                }
            }
            return convertView;
        }
    }

    private final class WbViewHolder {
        final TextView nWbName;

        public WbViewHolder(final View view) {
            this.nWbName = view.findViewById(R.id.tv_wb_name);
        }
    }
}
