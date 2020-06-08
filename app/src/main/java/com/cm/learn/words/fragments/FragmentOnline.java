package com.cm.learn.words.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cm.learn.words.R;
import com.cm.learn.words.activity.FirstActivity;
import com.cm.learn.words.activity.MainActivity;
import com.cm.learn.words.baiduAPI.TransApi;
import com.cm.learn.words.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Fragment 片段
 * 片段之 在线查询
 *
 * getContext()：这个是View类中提供的方法，在继承了View的类中才可以调用，返回的是当前View运行在哪个Activity Context中。
 * getActivity(): 获得Fragment依附的Activity对象。Fragment里边的getActivity()不推荐使用原因如下：这个方法会返回当前Fragment所附加的Activity，
                当Fragment生命周期结束并销毁时，getActivity()返回的是null，所以在使用时要注意判断null或者捕获空指针异常。
 * getSystemService 监听手机的一些状态，如网络，sd卡等等。这个方法基于context,只有存在TextView控件的窗体中这个方法才会被激活，参考：https://www.cnblogs.com/gaoshen/p/5784788.html
 */

public class FragmentOnline extends Fragment {
    Button button1,button2;
    EditText textView_online;
    EditText textView_translate_result;

    String input_data;  //输入的需要查询的内容
    String APP_ID;
    String SECURITY_KEY;

    Handler handler;    //用户主次线程数据交互
    HashMap<String,String> input_map;   //输入的内容
    final String en = "en";     //语种，英文
    final String zh = "zh";     //语种，中文

    @Nullable
    @Override   //返回在线翻译的view视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online,null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO  翻译功能已屏蔽，如果需要使用，自己去百度翻译申请 appid 与 privateKey 填在对应位置处即可
        APP_ID = getResources().getString(R.string.baidu_appId);
        SECURITY_KEY = getResources().getString(R.string.baidu_privateKey);

        button1 = view.findViewById(R.id.online_query_btn1);    //英译汉按钮
        button2 = view.findViewById(R.id.online_query_btn2);    //汉译英按钮
        textView_online = view.findViewById(R.id.online_input_words); //EditText获取查询内容
        textView_translate_result = view.findViewById(R.id.online_translate_result); //翻译结果显示的view

        //点击英译汉
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndInput();
                //TODO  翻译功能已屏蔽，如果需要使用，自己去百度翻译申请 appid 与 privateKey 填在对应位置处即可
                Toast.makeText(getActivity(),"翻译功能已屏蔽，如果需要使用，自己去百度翻译申请 appid 与 privateKey 填在对应位置处即可",Toast.LENGTH_LONG).show();
                if(checkNetwork()){
                    newThreadRun(en,zh);
                }else {
                    ToastUtil.showMsg(getActivity(),"兄dei，网络没打开哈");
                }

            }
        });
        //点击汉译英
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndInput();
                //TODO  翻译功能已屏蔽，如果需要使用，自己去百度翻译申请 appid 与 privateKey 填在对应位置处即可
                Toast.makeText(getActivity(),"翻译功能已屏蔽，如果需要使用，自己去百度翻译申请 appid 与 privateKey 填在对应位置处即可",Toast.LENGTH_LONG).show();
                if(checkNetwork()){
                    newThreadRun(zh,en);
                }else {
                    ToastUtil.showMsg(getActivity(),"兄dei，网络没打开哈");
                }
            }
        });
    }
    /** 判断是否有可用网络 */
    private boolean checkNetwork(){
            ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null){
                //getAllNetworkInfo 已被弃用
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
//            if (android.os.Build.VERSION.SDK_INT >= 21) {
//                Network[] networks = connectivity.getAllNetworks();
//                for (Network mNetwork : networks) {
//                    NetworkInfo networkInfo = connectivity.getNetworkInfo(mNetwork);
//                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
//                        return true;
//                    }
//                }
//            }
            return false;
    }
    private void checkAndInput(){
        //获取需要查询的内容
        input_data = textView_online.getText().toString();
        System.out.println(input_data.contains("\n"));
        Log.d("input_data1：" ,input_data);

        if(input_data.isEmpty()){
            ToastUtil.showMsg(getActivity(),"空的哈！");
            return;
        }
        //将输入的多个查询内容 进行有序拼接
        if(input_data.contains("\n")){
            input_map = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            String[] input_data_arr = input_data.split("\n");
            for (int i = 1;i <= input_data_arr.length;i++) {
                sb.append(i).append("、").append(input_data_arr[i-1]).append("\n");
            }
            String str1 = sb.toString();
            input_data = str1.substring(0,str1.length()-1);
            Log.d("input_data2：" ,input_data);
        }
        //通过 handler 传递信息，填充原文及翻译
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                TreeMap<String,String> map = (TreeMap) msg.obj;
                StringBuilder sb = new StringBuilder();

                //将一组原文与对应译文拼接保存在 list
                for (Map.Entry<String,String> data:map.entrySet()) {
                    sb.append("\n")
                      .append("原文:").append(data.getKey()).append("\n")
                      .append("译文:").append(data.getValue().replaceFirst(",","、")).append("\n");
                }
                Log.d("res_sb:", sb.toString());
                //显示结果
                textView_translate_result.setText(sb.toString());
                return false;
            }
        });
    }
    //访问百度翻译接口需单独线程，否则android在主线程中报错
    private void newThreadRun(final String from, final String to){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //调用百度api在线查询
                final TransApi transApi = new TransApi(APP_ID, SECURITY_KEY);
                final String need_query_data = input_data;
                Log.d("need_query_data:", need_query_data);
                String response_result = transApi.getTransResult(need_query_data, from, to);
                Log.d("response_result:", response_result);
                //response_result:: {"from":"en","to":"zh","trans_result":[{"src":"one","dst":"一"},{"src":"two","dst":"二"}]}
                if (response_result.contains("error_code")) {
                    Log.d("return error：", response_result);
                    return;
                }
                try {
                    //解析为json
                    JSONObject jsonObject = new JSONObject(response_result);
                    //response_result:: {"from":"en","to":"zh","trans_result":[{"src":"one","dst":"一"},{"src":"two","dst":"二"}]}
                    Log.d("result_json:", jsonObject.toString());
                    //trans_result 返回的是一个json数组
                    JSONArray trans_result_list = jsonObject.getJSONArray("trans_result");
                    Log.d("trans_result_list:", trans_result_list.toString());
                    //继续解析,将所有结果存入map
                    TreeMap<String,String> res_map = new TreeMap<>();
                    JSONObject trans_result;
                    if(trans_result_list.length() !=0){
                        for (int i = 0 ;i<trans_result_list.length();i++) {
                            trans_result = trans_result_list.getJSONObject(i);
                            res_map.put(trans_result.getString("src"),trans_result.getString("dst"));
                        }
                        Log.d("res_map:", res_map.toString());
                    }
                    //通过handler与message在子线程与主线程间传递信息
                    if(!res_map.isEmpty()){
                        Message message = new Message();
                        message.obj = res_map;
                        handler.sendMessage(message);
                    }
                    Log.d("data_query:", "end，res_map:"+res_map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start(); //注意start
    }
}
