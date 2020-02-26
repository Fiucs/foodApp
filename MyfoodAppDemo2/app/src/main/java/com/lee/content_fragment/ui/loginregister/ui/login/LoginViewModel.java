package com.lee.content_fragment.ui.loginregister.ui.login;

import android.app.Application;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.repository.javabean.MsgInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import okhttp3.Response;

public class LoginViewModel extends AndroidViewModel {

    private  String URL="";
    private static ObjectMapper mapper = new ObjectMapper();

    MutableLiveData<MsgInfo> msgInfo;//登录状态
    LoginAsyncTask loginAsyncTask=null;
    public LoginViewModel(@NonNull Application application) {
        super(application);

//        this.msgInfo.setValue(new MsgInfo(false, "未登录"));//默认未登录
    }

    public MutableLiveData<MsgInfo> getStatus() {
        return msgInfo;
    }

    public LoginAsyncTask loginMethod()
    {
        loginAsyncTask=new LoginAsyncTask();
        System.out.println(loginAsyncTask);
        return loginAsyncTask;

    }

    //获取登录或注册的返回结果
    public interface AsyncResponse {
        void onDataReceivedSuccess(MsgInfo msgInfo);
//        void onDataReceivedFailed();
    }

     public class LoginAsyncTask extends AsyncTask<String,Void,MsgInfo>
    {

        public AsyncResponse asyncResponse;

        public void setOnAsyncResponse(AsyncResponse asyncResponse)
        {
            this.asyncResponse = asyncResponse;
        }

        @Override
        protected MsgInfo doInBackground(String... strings) {

            MsgInfo msgInfo = okhttpLogin(strings);
            return msgInfo;
        }
        @Override
        protected void onPostExecute(MsgInfo msgInfo) {
            super.onPostExecute(msgInfo);
            asyncResponse.onDataReceivedSuccess(msgInfo);//结果回传给接口


        }
    }



    public static MsgInfo okhttpLogin(String...strings)
    {
        try {
//            String URL = "http://192.168.1.7:8081/login";
            String URL = "http://192.168.1.7:8081"+strings[2];
            Response response = OkHttpUtils
                    .post()
                    .url(URL)
                    .addHeader("connection", "close")
                    .addParams("userName", String.valueOf(strings[0]))
                    .addParams("password", String.valueOf(strings[1]))
                    .build()
                    .connTimeOut(5000)
                    .readTimeOut(5000)
                    .execute();

            String string = response.body().string();
            System.out.println("sadsadsa"+string);
            MsgInfo    msgInfo = mapper.readValue(string, MsgInfo.class);
            return  msgInfo;

        } catch (IOException e) {
            e.printStackTrace();

            return  new MsgInfo(false,"_error");
        }
    }




}
