package com.imorning.whiteboard.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.imorning.whiteboard.R;

public class UiActivity extends BaseActivity {

    private final String[] data =
            {"This a test activity",
                    "This a test activity",
                    "This a test activity",
                    "This a test activity",
                    "This a test activity"};//假数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_layout);
        ListView listView = (ListView) findViewById(R.id.test_lv);//在视图中找到ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);//新建并配置ArrayAapeter
        listView.setAdapter(adapter);

    }
}