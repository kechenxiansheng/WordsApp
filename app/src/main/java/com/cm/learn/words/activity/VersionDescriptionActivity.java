package com.cm.learn.words.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cm.learn.words.R;

/**
 * 加载版本说明的view
 * 需要在manifest.xml中注册
 * （1）simple_list_item_1（单行显示），此布局中只有一个TextView，是Android内置的布局文件，id为：android.R.id.text1；
 * （2）simple_list_item_2和two_line_list_item（双行显示），都有两个TextView：android.R.id.text1和android.R.id.text2，不同之处在于，前者两行字是不一样大的，而后者两行字一样大小。
 */
public class VersionDescriptionActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充布局  ArrayAdapter requires the resource ID to be a TextView
        setContentView(R.layout.activity_version);
        //通过arrayAdapter设置listView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.app_version));
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
}
