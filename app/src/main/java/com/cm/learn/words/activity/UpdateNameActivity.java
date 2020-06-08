package com.cm.learn.words.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cm.learn.words.R;
import com.cm.learn.words.database.DBOpenHelper;
import com.cm.learn.words.util.ToastUtil;

import java.util.ArrayList;

/**
 * 改名的view
 * 需要在manifest.xml中注册
 */
public class UpdateNameActivity extends AppCompatActivity{

    DBOpenHelper dbOpenHelper;
    Button save_btn;
    TextView text_current;
    EditText editText_new;
    private static UpdateCallback callback = null;

    public static void setCallback(UpdateCallback updateCallback){
        callback = updateCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充布局
        setContentView(R.layout.activity_update_name);

        //当前名称editText,新名称editText,保存按钮
        text_current = findViewById(R.id.current_name);
        editText_new = findViewById(R.id.new_name);
        save_btn = findViewById(R.id.save_button);

        //查询名称
        ArrayList<String> lists = queryName();
        String tb_name ="";
        if(lists.size()!=0){
            tb_name = lists.get(0);
        }
        System.out.println("tb_name:"+tb_name);
        //显示当前名称
        text_current.setText(tb_name);

        final String c_name = tb_name;
        //点击保存
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取新名称
                String new_name = editText_new.getText().toString();
                System.out.println("new_name:"+new_name);
                if(new_name.isEmpty()){
                    callback.result(false);
                    ToastUtil.showMsg(UpdateNameActivity.this,"名称不能为空哈！");
                    return;
                }
                //数据库更新
                dbOpenHelper.updateName(dbOpenHelper.getReadableDatabase(), c_name,new_name);
                ToastUtil.showMsg(UpdateNameActivity.this,"名称修改成功！");
                //设置回调状态
                callback.result(true);
                //跳转至FirstActivity
                startActivity(new_name);
            }
        });


    }
    //查询名称
    private ArrayList<String> queryName(){
        //加载表数据
        dbOpenHelper = new DBOpenHelper(UpdateNameActivity.this,"tb_user",null,1);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        //查询
        Cursor cursor = database.query("tb_user",null,null,null,null,null,null);
        System.out.println("cursor_count:"+cursor.getCount());
        ArrayList<String> lists = new ArrayList<>();
        //存在数据为true
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            lists.add(name);
        }
        cursor.close();
        return lists;
    }

    private void startActivity(String name){
        Intent intent = new Intent(UpdateNameActivity.this,FirstActivity.class);
        //注意这里用户名的存储，通过bundle存储，bundle是容器，intent是传递消息的工具
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public interface UpdateCallback{
        void result(Boolean bo);
    }

}
