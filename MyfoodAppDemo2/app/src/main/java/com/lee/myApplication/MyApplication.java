package com.lee.myApplication;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//持久化 cookie
        CookieJarImpl cookieJar =new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build();
        OkHttpUtils.initClient(okHttpClient);
        System.out.println("cookie持久化执行了+"+cookieJar.getCookieStore().getCookies());


    }
}
