package com.cm.learn.words.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cm.learn.words.activity.ExplainActivity;
import com.cm.learn.words.R;
import com.cm.learn.words.activity.UpdateNameActivity;
import com.cm.learn.words.activity.VersionDescriptionActivity;
import com.cm.learn.words.activity.WordsBaseActivity;

/**
 * 个人中心
 */

public class FragmentSelf extends Fragment {

    TextView textView_wordsbase,textView_explain,textView_version,textView_update,textView_tel;
    @Nullable
    @Override   //创建个人中心的view对象
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self,null);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView_wordsbase = view.findViewById(R.id.self_tv_words); //单词库按钮
        textView_update = view.findViewById(R.id.self_tv_update); //改名按钮
        textView_explain = view.findViewById(R.id.self_tv_explain); //软件说明按钮
        textView_version = view.findViewById(R.id.self_tv_version); //版本更新说明按钮
        textView_tel = view.findViewById(R.id.take_phone); //拨打电话按钮
        //点击单词库
        textView_wordsbase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WordsBaseActivity.class);
                startActivity(intent);
            }
        });
        //点击改名
        textView_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateNameActivity.class);
                startActivity(intent);
                //监听回调状态
                UpdateNameActivity.setCallback(new UpdateNameActivity.UpdateCallback() {
                    @Override
                    public void result(Boolean bo) {
                        Log.d("update_callback",bo+"");
                        if(bo){
                            getActivity().finish();
                        }
                    }
                });
            }
        });
        //点击软件说明
        textView_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExplainActivity.class);
                startActivity(intent);
            }
        });
        //点击版本说明
        textView_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VersionDescriptionActivity.class);
                startActivity(intent);
            }
        });
        //直呼你哥提建议
        textView_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);   //设置动作，此时表示显示数据（显示拨号界面）---- 也可设置为直接拨打电话（Intent.ACTION_CALL）,需要添加权限
                intent.setData(Uri.parse("tel:10086"));   //设置数据，用Uri指定电话
                startActivity(intent);

            }
        });
    }

}
