package com.cm.learn.words.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLiteOpenHelper 数据创建位置：/data/data/应用程序名/databases中
 * table_dictionary
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    final String Create_Table_dict="create table tb_dict (_id integer primary key autoincrement,word,translate)";
    final String Create_Table_user="create table tb_user (_id integer primary key autoincrement,name)";

    public DBOpenHelper(Context context, String table_name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, table_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建表
        sqLiteDatabase.execSQL(Create_Table_dict);
        sqLiteDatabase.execSQL(Create_Table_user);
    }

    @Override   //当版本变化时系统会调用该回调方法
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("delete from tb_dict");
        sqLiteDatabase.execSQL("update sqlite_sequence SET seq = 0 where name ='tb_dict'");
        sqLiteDatabase.execSQL("delete from tb_user");
        sqLiteDatabase.execSQL("update sqlite_sequence SET seq = 0 where name ='tb_user'");
    }

    //存储添加的单词
    public void writeData(SQLiteDatabase sqLiteDatabase,String word,String translate){
        ContentValues values = new ContentValues();
        values.put("word",word);
        values.put("translate",translate);
        sqLiteDatabase.insert("tb_dict",null,values);//保存单词
    }

    //删除单词
    public void deleteData(SQLiteDatabase sqLiteDatabase,String word,String translate){
        ContentValues values = new ContentValues();
        values.put("word",word);
        values.put("translate",translate);
        String[] wordsArr = {word};
        sqLiteDatabase.delete("tb_dict","word=?",wordsArr);//保存单词
    }
    //存储登陆的名称
    public void writeName(SQLiteDatabase sqLiteDatabase,String name){
        Log.d("write_name",name);
        ContentValues values = new ContentValues();
        values.put("name",name);
        sqLiteDatabase.insert("tb_user",null,values);//保存名字
    }
    //修改名称
    public void updateName(SQLiteDatabase sqLiteDatabase,String c_name,String new_name){
        Log.d("current_name",c_name);
        Log.d("update_name",new_name);
        ContentValues values = new ContentValues();
        values.put("name",new_name);
        String[] nameArr = {c_name};
        //参数说明：表名，当前数据，条件（需要修改的字段），需要修改的字段的当前值
        sqLiteDatabase.update("tb_user",values,"name=?",nameArr);//数据库更新
    }
}
