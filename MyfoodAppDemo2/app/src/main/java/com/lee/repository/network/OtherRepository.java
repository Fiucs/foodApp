package com.lee.repository.network;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.repository.javabean.MsgInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import okhttp3.Response;

public class OtherRepository {

    private ObjectMapper objectMapper;
    private UpdateFavo updateFavo;

    public OtherRepository() {
        objectMapper=new ObjectMapper();
    }

    public UpdateFavo getUpdateFavo() {

        return new UpdateFavo();//获取新实例 防止thread 只创建一次
    }

    /**
     * 回调 改变收藏状态
     */
    public  interface FavoriteCallback{
        public void updateFavorite(MsgInfo msgInfo);
    }
    public  class UpdateFavo extends AsyncTask<String,Void,MsgInfo>
    {
        FavoriteCallback favoriteCallback;

        public void setFavoriteCallback(FavoriteCallback favoriteCallback) {
            this.favoriteCallback = favoriteCallback;
        }

        @Override
        public MsgInfo doInBackground(String...strings) {

            MsgInfo msgInfo = updateFavo(strings[0]);
            return msgInfo;
        }

        @Override
        protected void onPostExecute(MsgInfo msgInfo) {
            favoriteCallback.updateFavorite(msgInfo);//回调 根据msgInfo操作
        }
    }
    //检测网络是否ok
    public  MsgInfo updateFavo(String url)
    {

        objectMapper=new ObjectMapper();
        try {
            String URL="http://192.168.1.7:8081";
            URL+=url;
            Response response = OkHttpUtils
                    .post()
                    .url(URL)
                    .addHeader("connection", "close")
                    .build()
                    .connTimeOut(1000)
                    .readTimeOut(1000)
                    .execute();
            System.out.println("检查网络状况：有网");
            Log.e("检查网络状况","true");
            String res = response.body().string();
            Log.e("检查网络状况",res);
            MsgInfo msgInfo = objectMapper.readValue(res, MsgInfo.class);

            return msgInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("检查网络状况","false");

            return new MsgInfo(false,"_error");//网络错误
        }
    }
}
