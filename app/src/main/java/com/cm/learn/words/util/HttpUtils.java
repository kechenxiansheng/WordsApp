package com.cm.learn.words.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求工具类
 */
public class HttpUtils {

    public static void sendHttpRequestGET(final String _url, final HttpCallBackListener callBackListener){
        //耗时任务新建子线程执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(_url);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000); //连接超时毫秒数
                    connection.setReadTimeout(8000);    //读取超时毫秒数
                    //获取返回内容
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    //回调解析结果
                    if(callBackListener!=null){
                        callBackListener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //回调异常结果
                    if(callBackListener!=null){
                        callBackListener.onError(e);
                    }
                } finally {
                    //关闭流和http连接
                    try {
                        if(reader!=null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).run();
    }

    /** 用于回调 http 的请求结果*/
    interface HttpCallBackListener{
        void onFinish(String response);    //请求结束
        void onError(Exception e);     //请求中出现异常
    }
}
