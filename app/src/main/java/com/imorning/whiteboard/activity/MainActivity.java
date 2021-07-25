package com.imorning.whiteboard.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.imorning.whiteboard.WhiteBoardApp;
import com.imorning.whiteboard.adapter.WbItemAdapter;
import com.imorning.whiteboard.bean.FileListData;
import com.imorning.whiteboard.databinding.ActivityMainBinding;
import com.imorning.whiteboard.utils.FileUtil;
import com.imorning.whiteboard.utils.OperationUtils;
import com.imorning.whiteboard.utils.StoreUtil;
import com.imorning.whiteboard.view.RecyclerView.DividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<FileListData> fileLists;
    private ActivityMainBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        initView();
    }

    private void initView() {
        binding.rvWb.setLayoutManager(new LinearLayoutManager(MainActivity.this));//这里用线性显示 类似于listview
        //binding.rvWb.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));//这里用线性宫格显示 类似于grid view
        //binding.rvWb.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        WbItemAdapter itemAdapter = new WbItemAdapter(MainActivity.this, fileLists);
        itemAdapter.setOnItemClickListener((position, fileListDataList) -> {
            //设置全局变量
            WhiteBoardApp.setFileTitle(fileListDataList.get(position).getTitle());
            WhiteBoardApp.setFilePath(fileListDataList.get(position).getFilePath());
            //读取文件中的数据并赋值
            StoreUtil.readWhiteBoardPoints(fileListDataList.get(position).getFilePath());
            Intent intent = new Intent(MainActivity.this, WhiteBoardActivity.class);
            startActivity(intent);
        });
        itemAdapter.setOnItemLongClickListener(new WbItemAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(int Position, List<FileListData> fileListDataList) {
                // TODO: 2021/7/25 Add context menu for rename,delete...
                Log.d("TAG", "onItemLongClick: " + fileListDataList.get(Position).getFilePath());
            }
        });
        binding.rvWb.setAdapter(itemAdapter);
        binding.rvWb.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        binding.fabWbAdd.setOnClickListener(v -> {
            OperationUtils.getInstance().initDrawPointList();
            Intent intent = new Intent(MainActivity.this, WhiteBoardActivity.class);
            startActivity(intent);
        });
    }

    private void loadData() {
        File folder = new File(StoreUtil.getWbPath());
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }
        final File[] files = folder.listFiles();
        if (files != null) {
            if (files.length > 0) {
                fileLists = new ArrayList<>();
                for (File f : files) {
                    fileLists.add(new FileListData(FileUtil.getFileName(f), f.getAbsolutePath()));
                }
                binding.tvListStatus.setVisibility(View.GONE);
            }
        }
    }

}