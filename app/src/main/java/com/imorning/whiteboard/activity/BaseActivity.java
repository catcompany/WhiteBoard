package com.imorning.whiteboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.guanpy.library.EventBus;

public abstract class BaseActivity extends AppCompatActivity {


    protected void showMessage(CharSequence msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
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

    public void navi2Page(final Class<?> page) {
        this.navi2Page(page, false);
    }

    public void navi2Page(final Class<?> page, final boolean shut) {
        this.navi2Page(page, null, shut);
    }

    public void navi2Page(final Class<?> page, final Bundle data, final boolean shut) {
        final Intent intent = new Intent(this, page);
        if (null != data) {
            intent.putExtras(data);
        }
        this.startActivity(intent);
        if (shut) {
            this.finish();
        }
    }
}