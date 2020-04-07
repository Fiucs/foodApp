package com.lee.myfoodappdemo2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.lee.content_fragment.ui.loginregister.ui.login.LoginViewModel;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.repository.javabean.MsgInfo;
import com.lee.utils.DES;

import java.io.InputStream;
import java.util.Properties;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_splash);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {

                    SharedPreferences account =getSharedPreferences("account", Context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
                    SharedPreferences.Editor edit = account.edit();//编辑器
                    String password = account.getString("password","_null");//s1为返回值密码
                    String username = account.getString("username", "_null");
                    edit.remove("password");
                    edit.apply();
                    @SuppressLint("HardwareIds") String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    String id = androidID + Build.SERIAL;
                    Log.e("设备ID:",id);
                    BaseDataModel.setDeviceId(id);//设置设备号

                    //获取基本地址
                    InputStream inputStream = getResources().getAssets().open("baseUrl.properties");
                    Properties pro = new Properties();
                    pro.load(inputStream);
                    String baseUrl = pro.getProperty("localBaseUrl");
                    BaseDataModel.setBaseUrl(baseUrl);
                    Log.e("基本地址:",baseUrl);

                    if(!password.equals("_null"))//则登录E/设备ID:: 9e26a499477c7b6de60b4ce
                    {
//                        此处利用okhttp
                        password = DES.getDESOri(password, username);
                        MsgInfo msgInfo = LoginViewModel.okhttpLogin(username, password);
                        if(msgInfo.isSuccess())
                        {
//                            登录成功//写入
                            edit.putString("userStatus","ok");
                            edit.apply();
                        }

                    }
                    else
                    {

                        //代表没有密码 即 用户非登录
                        edit.putString("userStatus","bad");
                        edit.apply();
                    }
                    int count=3;
                    boolean b=false;
                    while (count-->0)
                    {
                        b = BaseDataModel.chekNetWork();
                    }

                    BaseDataModel.setIsNetWorkOk(b);
                    System.out.println("splash的网络"+b);;

                    sleep(500);//作其他耗时工作
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
