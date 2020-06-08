package com.cm.learn.words.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cm.learn.words.R;
import com.cm.learn.words.database.DBOpenHelper;
import com.cm.learn.words.util.ToastUtil;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 *  录入单词界面
 *  Fragment 分段，碎片
 *  Fragment 可以理解为也是activity，用于分割开一个activity的量，方便后续操作。必须依赖activity使用，不能单独使用
 */

public class FragmentInput extends Fragment {
    Button button;
    EditText editText_word,editText_translate;
    DBOpenHelper dbOpenHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input,null);
        dbOpenHelper = new DBOpenHelper(getActivity(),"tb_dict",null,1);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //获取输入单词的 EditText 对象
        editText_word = view.findViewById(R.id.input_et_words);
        //获取输入翻译的 EditText 对象
        editText_translate = view.findViewById(R.id.input_et_translate);
        //获取保存按钮
        button = view.findViewById(R.id.input_btn);

        //点击保存按钮
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String word = editText_word.getText().toString();   //录入的单词
                    String translate = editText_translate.getText().toString();     //录入的翻译
                    if(word.endsWith("ion")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(word.endsWith("ness")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(word.endsWith("ment")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(word.endsWith("ty")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(word.endsWith("acy")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(word.endsWith("ance")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(word.endsWith("ence")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(word.endsWith("ice")){
                        translate = "n. "+editText_translate.getText().toString();
                    }else if(translate.endsWith("的")){
                        translate = "adj. "+editText_translate.getText().toString();
                    }else if (translate.endsWith("地")){
                        translate = "adv. "+editText_translate.getText().toString();
                    }else{
                        translate = editText_translate.getText().toString();
                    }
                    if (word.isEmpty() || editText_translate.getText().toString().isEmpty()){
                        ToastUtil.showMsg(getActivity(),"空滴内容");
                    }else{
                        //dbOpenHelper.getReadableDatabase() 获取可读数据库句柄
                        dbOpenHelper.writeData(dbOpenHelper.getReadableDatabase(),word,translate);
                        ToastUtil.showMsg(getActivity(),"保存老");
                        editText_translate.setText("");
                        editText_word.setText("");
                        editText_word.requestFocus();   //单词录入框重新获取焦点

                        editText_word.setHeight(WRAP_CONTENT);
                }

            }
        });
    }
}
