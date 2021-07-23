package com.imorning.whiteboard.activity;


import android.os.Bundle;

import androidx.annotation.Nullable;

import com.imorning.whiteboard.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }
}
