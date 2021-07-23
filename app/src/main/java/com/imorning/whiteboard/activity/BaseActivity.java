package com.imorning.whiteboard.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.guanpy.library.EventBus;
import com.imorning.whiteboard.R;
import com.imorning.whiteboard.utils.ToastUtils;

public class BaseActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;

    private long mBackPressedTime;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
    }

    protected void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == PERMISSION_REQUEST_CODE) {
                        if (!Environment.isExternalStorageManager()) {
                            showMessage(getString(R.string.no_permission));
                        }
                    }
                }).launch(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));

            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                showMessage(getString(R.string.no_permission));
            }
        }
    }


    protected void showMessage(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.registerAnnotatedReceiver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregisterAnnotatedReceiver(this);
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
        //System.exit(0);
        finish();
    }
}