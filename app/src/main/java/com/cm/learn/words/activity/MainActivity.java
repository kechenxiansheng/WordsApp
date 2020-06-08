package com.cm.learn.words.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cm.learn.words.R;
import com.cm.learn.words.database.DBOpenHelper;
import com.cm.learn.words.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * EditText
 * android 跟用户数据传输的窗户
 * 一般用作登陆界面，比如输入账号密码电话等，可以获取输入的信息
 * 也可用作监听用户输入内容，有 内容改变之前，改变，改变之后三个时机，可选择做对应其他处理
 *
 * 需要在manifest.xml中注册
 */

public class MainActivity extends AppCompatActivity {
    Button login_button;
    EditText editText;
    DBOpenHelper dbOpenHelper;
    SharedPreferences setting;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充布局
        setContentView(R.layout.activity_main);

        //通过SharedPreferences判断是否第一次启动，以xml形式存储
        setting = getSharedPreferences("isFirst", MODE_PRIVATE);  //第一个参数：指定此文件名称，第二个：指定文件操作模式（共有四种）
        Boolean user_first = setting.getBoolean("FIRST",true);  //第一个参数key，第二个参数默认value
        //第一次 启动
        if(user_first){
            setting.edit().putBoolean("FIRST", false).apply();
            Log.d("Words_App", "first start!");
            //我哥最好的弹框
            startDialog();
        }else{
            //不是第一次启动直接进入
            Log.d("Words_App", "not first start!");
            inputName();
        }
    }


    private void startDialog(){

//        WebView webView = new WebView(this);
//        webView.setWebViewClient(new WebViewClient());//防止启动系统的浏览器显示网页
//        webView.getSettings().setBuiltInZoomControls(true);//启动网页缩放功能
//        webView.getSettings().setJavaScriptEnabled(true);   //启动javaScript，网页中的链接便可点击
//        webView.loadUrl("http://www.13cd.com/law.html");


        String bksw =
                        "用户协议：\n" +
                        "提问：如何形容你哥？\n" +
                        "答：\n" +
                        "　　玉树临风！\n" +
                        "　　翩翩少年！\n" +
                        "　　气宇轩昂！\n" +
                        "　　博学多才！\n" +
                        "　　温文尔雅！\n" +
                        "　　貌比潘安！\n" +
                        "　　···\n" +
                        "　　陌上人如玉，公子世无双！\n" +
                        "总之，他是世界上最好的最帅的最温暖的人！\n";
        TextView message = new TextView(this);
        message.setText(bksw);
        message.setTextSize(16);
        message.setTextColor(Color.BLACK);
        message.getPaint().setFakeBoldText(true);
        message.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue));


        TextView title = new TextView(this);
        title.setText("请阅读用户协议并同意！");
        title.setTextSize(18);
        title.setBackgroundColor(ContextCompat.getColor(this,R.color.colorBlue));
        title.setTextColor(ContextCompat.getColor(this,R.color.colorOrange));
        title.getPaint().setFakeBoldText(true); //字体加粗
        title.setGravity(Gravity.CENTER);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(title)
                .setView(message)
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputName();
                    }
                })
                .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setting.edit().putBoolean("FIRST", true).apply();
                        MainActivity.this.finish();
                    }
                  })
                .setCancelable(false)
                .show();

//        //设置对话框高宽
//        WindowManager.LayoutParams windowManager =  Objects.requireNonNull(dialog.getWindow()).getAttributes();
////        windowManager.width = (int)(this.getResources().getDisplayMetrics().widthPixels * 1);
//        windowManager.height = (int)(this.getResources().getDisplayMetrics().heightPixels * 0.8);
//        //对话框重置属性
//        dialog.getWindow().setAttributes(windowManager);

        //确定按钮 颜色
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(this,R.color.colorBlack));
        positiveButton.setTextSize(18);
        positiveButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorOrange));
        positiveButton.getPaint().setFakeBoldText(true);
        //确定 按钮居中显示,getLayoutParams()通过 negativeButton/positiveButton 获取都一样
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.weight = 30;
        layoutParams.leftMargin = 10;
        layoutParams.setMarginStart(10);
        layoutParams.width = 0;
        positiveButton.setLayoutParams(layoutParams);

        //取消按钮 颜色
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(this,R.color.colorBlack));
        negativeButton.setTextSize(18);
        negativeButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorOrange));
        negativeButton.getPaint().setFakeBoldText(true);
        //取消 按钮居中显示
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        layoutParams2.gravity = Gravity.CENTER;
        layoutParams2.weight = 30;
        layoutParams2.width = 0;
        negativeButton.setLayoutParams(layoutParams);


    }
    //主界面开始输入名称
    private void inputName(){
        //加载表数据
        dbOpenHelper = new DBOpenHelper(MainActivity.this,"tb_user",null,1);

//        获取数据库的存储路径
//        File file = this.getDatabasePath("tb_user");
//        Log.d("dataName",file!=null?file.getAbsolutePath():"没找到！");
//        //dataName /data/user/0/com.example.administrator.words/databases/tb_user

        //查询数据
        ArrayList<String> lists = queryName();
        String tb_name ="";
        if(lists.size()!=0){
            tb_name = lists.get(0);
        }

        Log.d("Words_App","tb_name:"+tb_name);
//        ToastUtil.showMsg(MainActivity.this,tb_name.isEmpty()+"!");

        //赋值名字
        final String[] name = new String[1];
        name[0] = tb_name;
        //数据库没有存入名字，就监听用户输入的名字
        editText = findViewById(R.id.main_et_username);
        //登陆按钮
        login_button = findViewById(R.id.main_login_btn);
        //数据库没有名字，进行输入监听操作
        if(tb_name.isEmpty()){
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //点击登陆之后获取用户输入的name
                    System.out.println("why_not??");
                    String writeName = editText.getText().toString();
                    //数据库和编辑框都是空的
                    if(writeName.isEmpty()){
                        ToastUtil.showMsg(MainActivity.this,"取个名撒，黑娃要不得哦");
                        return;
                    }
                    //用户输入了名字，但是数据库没有名字时，保存名字之后在进入
                    dbOpenHelper.writeName(dbOpenHelper.getReadableDatabase(),writeName);
                    name[0] = writeName;
                    startActivity(name[0]);
                }
            });
            return;
        }
        //数据库存有名字直接进入
        startActivity(name[0]);
    }

    private void startActivity(String name){
        Intent intent = new Intent(MainActivity.this,FirstActivity.class);
        //注意这里用户名的存储，通过bundle存储，bundle是容器，intent是传递消息的工具
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
        Log.d("Words_App","start FirstActivity");
        //activity之间跳转动画
//      overridePendingTransition(R.anim.my_scale_action, R.anim.my_scale_action);
    }

    private ArrayList<String> queryName(){
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
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


    @Override
    //点击系统返回键退出时，给个提醒
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("exit_words1",keyCode+"");
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("豆愣个走了吗?") //直接用标题询问，不设置message
                    .setIcon(R.mipmap.dog1)
                    .setPositiveButton("去意已决", new DialogInterface.OnClickListener() { //确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {    //PositiveButton 确定按钮
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("手误点错老", null)
                    .show();
            //设置 取消/确定按钮 颜色
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setTextColor(Color.BLACK);

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.GRAY);    //确定（去意已决）灰色
            //用权重（weight）将 取消/确定 按钮居中显示，而不是都显示在右边（默认都在右边）,getLayoutParams()通过 negativeButton/positiveButton 获取都一样
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
            layoutParams.weight = 10;
            positiveButton.setLayoutParams(layoutParams);
            negativeButton.setLayoutParams(layoutParams);
        }
        return super.onKeyDown(keyCode, event);
    }
}

