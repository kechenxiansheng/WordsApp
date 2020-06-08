package com.cm.learn.words.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import com.cm.learn.words.R;
import com.cm.learn.words.database.DBOpenHelper;
import com.cm.learn.words.util.ToastUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * Fragment 分段  Recite 背诵
 */

public class FragmentRecite extends Fragment implements View.OnClickListener{

    DBOpenHelper dbOpenHelper;
    TextView tv_word;
    EditText tv_translate;
    Button button_renshi,button_burenshi,button_next;
    ArrayList<Word> wordsList;  //库中所有单词及翻译数据list

    String current_word;                //当前单词
    String current_translation;         //当前翻译
    String[] randomArr;         //当前单词与对应翻译的数组
    String current_txt;         //当前显示的字符
    int i = 0;  //第一个单词

    //创建视图
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载数据库
        dbOpenHelper = new DBOpenHelper(getActivity(),"tb_dict",null,1);
        //获取背诵view对象
        View view = inflater.inflate(R.layout.fragment_recite,null);
        return view;
    }

    //创建时显示
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //加载库中的所有数据
        wordsList = getWords();
        if(wordsList == null || wordsList.size() ==0){
            //背诵界面提示
            ToastUtil.showMsg(getActivity(),"单词库莫有数据！");
            return;
        }
        tv_word = view.findViewById(R.id.recite_tv_word);   //显示单词的view
        tv_translate = view.findViewById(R.id.recite_tv_translate);     //显示翻译的view
        button_renshi = view.findViewById(R.id.recite_btn_renshi);  //认识按钮
        button_burenshi = view.findViewById(R.id.recite_btn_burenshi);  //不认识按钮
        button_next = view.findViewById(R.id.recite_btn_next);      //下一个按钮
        setListener();

        //随机显示单词还是翻译
        tv_word.setText(getCurrent_txt(i));

    }
    //获取当前随机文本
    private String getCurrent_txt(int index){
        //初始化当前单词及数组
        current_word = wordsList.get(index).word;
        current_translation = wordsList.get(index).translate;
        randomArr = new String[]{ "w" + current_word,"t" + current_translation};

        //randomArr数组长度的随机数
        int num = new Random().nextInt(randomArr.length);
        current_txt = randomArr[num];

        if(current_txt.startsWith("t")){
            tv_translate.setHint("此处拼写单词，点提示进行比对！");
        }else {
            tv_translate.setHint("点击提示显示翻译！");
        }

        return current_txt.substring(1);
    }
    //设置点击监听
    private void setListener(){
        button_next.setOnClickListener(this);
        button_burenshi.setOnClickListener(this);
        button_renshi.setOnClickListener(this);
    }
    //查询单词库
    private ArrayList<Word> getWords(){
        ArrayList<Word> words = new ArrayList<>();
        Cursor cursor = dbOpenHelper.getReadableDatabase().query("tb_dict",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            Word word = new Word();
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            word.word = cursor.getString(cursor.getColumnIndex("word"));
            word.translate = cursor.getString(cursor.getColumnIndex("translate"));
            words.add(word);
        }
        cursor.close();
        return words;
    }

    @Override
    public void onClick(View view) {
        //比对当前view的id
        if(wordsList.isEmpty()){
            //点击按钮继续提示
            ToastUtil.showMsg(getActivity(), "单词库莫有数据！");
            return;
        }
        switch (view.getId()) {
            case R.id.recite_btn_renshi: {      //点击认识，删除单词，跳入下一个单词
                if (wordsList.size() == 0){     //如果单词库为空
                    ToastUtil.showMsg(getActivity(), "背完老！");
                } else if (wordsList.size() == i+1) {  //如果单词到最后一个
                    wordsList.remove(i);
                    randomArr = null;
                    ToastUtil.showMsg(getActivity(), "后面没得老！");
                    tv_translate.setText("");
                    tv_word.setText("");
                }else{    //如果单词不是最后一个，删除当前单词
                        wordsList.remove(i);
                        randomArr = null;
                        ToastUtil.showMsg(getActivity(), "背诵已删除!");
                        tv_word.setText("");
                        tv_translate.setText("");
                }
                dbOpenHelper.onUpgrade(dbOpenHelper.getReadableDatabase(), 0, 0);   //更新数据库
                for (Word word : wordsList) {
                    dbOpenHelper.writeData(dbOpenHelper.getReadableDatabase(), word.word, word.translate);
                }
                break;
            }
            case R.id.recite_btn_burenshi:{     //点击提示，显示翻译
                if(wordsList.size()==0){
                    ToastUtil.showMsg(getActivity(), "背完老！");
                }else{
                    //显示对应翻译
                    if(current_txt.startsWith("w")){
                        tv_translate.setText(current_translation);
                        return;
                    }
                    //显示对应单词
                    tv_translate.setTextSize(18);
                    String input_word = tv_translate.getText().toString();
                    if(input_word.isEmpty()){
                        tv_translate.setText(current_word);
                        return;
                    }
                    if(current_word.equals(input_word)){
                        tv_translate.setText(String.valueOf("正确!单词库:"+current_word));
                        tv_translate.setTextSize(16);
                    }else {
                        tv_translate.setText(String.valueOf("错了!输入:"+input_word +" 单词库:"+current_word));
                        tv_translate.setTextSize(16);
                    }

                }
                break;
            }
            case R.id.recite_btn_next:{     //点击下一个，显示下一个单词
                if (i == wordsList.size()-1){       //如果是最后一个
                    ToastUtil.showMsg(getActivity(),"最后一个老，再来遍！");
                    i = 0;
                }else{
                    i++;
                }
                randomArr = null;
                tv_word.setText(getCurrent_txt(i));
                tv_translate.setText("");
                break;
            }
        }
    }

    class Word{
        String word;
        String translate;
    }
}
