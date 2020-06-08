package com.cm.learn.words.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cm.learn.words.R;
import com.cm.learn.words.bean.Word;
import com.cm.learn.words.database.DBOpenHelper;
import com.cm.learn.words.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载单词库的view
 * 需要在manifest.xml中注册
 */
public class WordsBaseActivity extends AppCompatActivity {

    TextView textView_wordsNum;
    DBOpenHelper dbOpenHelper;
    ListView listView;
    List<Map<String,Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充布局
        setContentView(R.layout.activity_words_base);

        dbOpenHelper = new DBOpenHelper(WordsBaseActivity.this,"tb_dict",null,1);
        ArrayList<Word> words = getWords();

        textView_wordsNum = findViewById(R.id.wordsNum);
        listView =  findViewById(R.id.list);

        list = new ArrayList<>();
        for (int i = 0;i < words.size() ;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",words.get(i).id+".");
            map.put("word", words.get(i).word);
            map.put("translate", words.get(i).translate);
            list.add(map);
        }

        String wordsNum = "单词库共有 "+words.size()+" 个单词！";
        textView_wordsNum.setText(wordsNum);
        /**
         * SimpleAdapter   参数说明
         * 　　context　　SimpleAdapter关联的View的上下文对象
         * 　　data　　　 一个 Map 组成的 List。在列表中的每个条目对应列表中的一行,每一个map中应该包含所有在from参数中指定的键
         * 　　resource   一个定义 列表项 的布局文件的 资源ID。布局文件将至少应包含那些在to中定义了的ID
         * 　　from       一个将被添加到Map映射上的键名
         * 　　to　　　　 将绑定数据的视图的ID,跟from参数对应,这些应该全是TextView
         */
        SimpleAdapter simpleAdapter = new SimpleAdapter(WordsBaseActivity.this,list, R.layout.list_item,
                new String[]{"id","word","translate"},new int[]{R.id.id,R.id.word,R.id.translate});

        listView.setAdapter(simpleAdapter);
        //listview点击事件监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map = list.get(position);
                ToastUtil.showMsg(WordsBaseActivity.this,map.toString());
            }
        });

    }
    //查询所有录入的单词数据
    private ArrayList<Word> getWords(){
        ArrayList<Word> words = new ArrayList<>();
        Cursor cursor = dbOpenHelper.getReadableDatabase().query("tb_dict",null,null,null,null,null,null);
        int i = 1;
        while(cursor.moveToNext()){
            Word word = new Word();
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            word.id = i;
            word.word = cursor.getString(cursor.getColumnIndex("word"));
            word.translate = cursor.getString(cursor.getColumnIndex("translate"));
            words.add(word);
            i++;
        }
        return words;
    }
}
