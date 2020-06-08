package com.cm.learn.words.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cm.learn.words.R;
import com.cm.learn.words.database.DBOpenHelper;
import com.cm.learn.words.fragments.FragmentInput;
import com.cm.learn.words.fragments.FragmentOnline;
import com.cm.learn.words.fragments.FragmentRecite;
import com.cm.learn.words.fragments.FragmentSelf;
import com.cm.learn.words.receiver.NetWorkChangeReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 首个activity，包括四个按钮
 * activity需要在manifest.xml中注册
 * View.OnClickListener 使用: 1、要么直接在按钮setOnClickListener中new  2、要么activity直接实现，setOnClickListener中传this
 * FrameLayout（框架布局）
 */
public class FirstActivity extends AppCompatActivity implements View.OnClickListener{
    private static FirstActivity _instance;
    TextView wel_name,wel_week;
    Button button_input,button_recite,button_self,button_online;
    FragmentInput fragmentInput;
    FragmentRecite fragmentRecite;
    FragmentSelf fragmentSelf;
    FragmentOnline fragmentOnline;
    DBOpenHelper dbOpenHelper;

    private IntentFilter intentFilter;
    private NetWorkChangeReceiver netWorkChangeReceiver;

    public static FirstActivity getInstance(){
        if(_instance == null){
            _instance = new FirstActivity();
        }
        return _instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充布局
        setContentView(R.layout.activity_frist);
        /**
         *  网络变化监听（接收指定的系统广播，网络状态变化时，系统会自动发出此条广播）
         */
        //广播动态注册
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        registerReceiver(netWorkChangeReceiver,intentFilter);//注册广播

        //ui组件初始化
        button_input = findViewById(R.id.first_btn_input); //录入按钮
        button_recite = findViewById(R.id.first_btn_recite);  //背诵按钮
        button_self = findViewById(R.id.first_btn_self);   //个人中心按钮
        button_online = findViewById(R.id.first_btn_onlineTranslation);   //在线翻译按钮
        wel_name = findViewById(R.id.wel_name);  //默认欢迎语句
        wel_week = findViewById(R.id.wel_week);  //默认周几

        fragmentInput = new FragmentInput();
        fragmentRecite = new FragmentRecite();
        fragmentSelf = new FragmentSelf();
        fragmentOnline = new FragmentOnline();

        dbOpenHelper = new DBOpenHelper(FirstActivity.this,"tb_dict",null,1);

        //获取从MainActivity通过intent - bundle存储的用户名
        Bundle buddle = getIntent().getExtras();
        //欢迎条 填充用户的名字,周几
        if(buddle!=null){
            wel_name.setText(buddle.getString("name"));
        }
        wel_week.setText(getWeek());

        //点击录入按钮，跳转录入界面
        button_input.setOnClickListener(this);
        //点击在线翻译按钮，跳转在线翻译界面
        button_online.setOnClickListener(this);
        //点击个人按钮，跳转个人中心界面
        button_self.setOnClickListener(this);
        //点击背诵按钮，跳转背诵界面
        button_recite.setOnClickListener(this);


        //默认显示录入界面
        replace(R.id.first_fl,fragmentInput);
        setText(button_input);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注意：取消广播
        unregisterReceiver(netWorkChangeReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //对应按钮 进行跳转及其他处理
            case R.id.first_btn_input:
                replace(R.id.first_fl,fragmentInput);
                setText(button_input);
                break;
            case R.id.first_btn_recite:
                replace(R.id.first_fl,fragmentRecite);
                setText(button_recite);
                break;
            case R.id.first_btn_onlineTranslation:
                replace(R.id.first_fl,fragmentOnline);
                setText(button_online);
                break;
            case R.id.first_btn_self:
                replace(R.id.first_fl,fragmentSelf);
                setText(button_self);
        }
    }
    /**
     * id   展示的视图的id
     * fragment     需要显示的fragment对象
     */
    private void replace(int id,Fragment fragment){
        //获取FragmentManager,开启事务，替换(展示的视图，需要显示的fragment对象)，提交
        getFragmentManager().beginTransaction().replace(id,fragment).commitAllowingStateLoss();
    }

    //控制按钮状态
    private void setText(Button btn) {
        List<Button> buttonList=new ArrayList<>();
        if (buttonList.size()==0){
            buttonList.add(button_input);
            buttonList.add(button_online);
            buttonList.add(button_recite);
            buttonList.add(button_self);
        }
        for (int i = 0; i <buttonList.size() ; i++) {
//            buttonList.get(i).setEnabled(true); //激活按钮，可点击，触碰
            buttonList.get(i).setTextSize(13);  //字体变回原来大小
        }
//        btn.setEnabled(false);  //不可点击，变灰
        btn.setTextSize(16);    //当前按钮字体变大
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    //点击系统返回键退出时，给个提醒
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("exit_words1",keyCode+"");
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog dialog = new AlertDialog.Builder(FirstActivity.this)
                    .setTitle("真滴不再背哈儿了嘛?") //直接用标题询问，不设置message
                    .setIcon(R.mipmap.dog1)
                    .setPositiveButton("不想背了", new DialogInterface.OnClickListener() { //确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {    //PositiveButton 确定按钮
                            FirstActivity.this.finish();
                        }
                    })
                    .setNegativeButton("再背哈儿", null)
                    .show();
            //设置 取消/确定按钮 颜色
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setTextColor(Color.BLACK);

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.GRAY);    //确定（不想背了）灰色
            //用权重（weight）将 取消/确定 按钮居中显示，而不是都显示在右边（默认都在右边）,getLayoutParams()通过 negativeButton/positiveButton 获取都一样
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
            layoutParams.weight = 10;
            positiveButton.setLayoutParams(layoutParams);
            negativeButton.setLayoutParams(layoutParams);


        }
        return super.onKeyDown(keyCode, event);
    }
    private String getWeek(){
        int w = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (w){
            case 1:
                week = "星期天";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }


}
