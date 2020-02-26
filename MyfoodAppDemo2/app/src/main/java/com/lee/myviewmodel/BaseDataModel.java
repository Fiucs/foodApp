package com.lee.myviewmodel;

import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.lee.myadapter.MyFragmentStateAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import okhttp3.Response;

public class BaseDataModel {

    private static int width;
    private static int height;
    private static boolean isNetWorkOk=true;//网络状况 是否ok 默认ok

    private static ActionBar actionBar;//分类
    private static ActionBar actionBar01;//我的

    private static int totals=-1;//总条目数 初始值为为获取
    private  static MyFragmentStateAdapter fragmentStateAdapter;//vIewpager视图管理器
    private static List<Fragment> fragments;//fragment列表
    private static ViewPager2 viewPager2;
    private static int firstFllsh=0;//是否是初次断网

    private static ChekNetWork chekNetWork;

    private static String deviceId;//设备号

    public static void setDeviceId(String deviceId) {
        BaseDataModel.deviceId = deviceId;
    }

    public static String getDeviceId() {
        return deviceId;
    }

    public static int getFirstFllsh() {
        return firstFllsh;
    }

    public static void setFirstFllsh(int firstFllsh) {
        firstFllsh = firstFllsh;
    }

    public static ViewPager2 getViewPager2() {
        return viewPager2;
    }

    public static void setViewPager2(ViewPager2 viewPager2) {
        BaseDataModel.viewPager2 = viewPager2;
    }

    public static List<Fragment> getFragments() {
        return fragments;
    }

    public static void setFragments(List<Fragment> fragments) {
        BaseDataModel.fragments = fragments;
    }

    public static MyFragmentStateAdapter getFragmentStateAdapter() {
        return fragmentStateAdapter;
    }

    public static void setFragmentStateAdapter(MyFragmentStateAdapter fragmentStateAdapter) {
        BaseDataModel.fragmentStateAdapter = fragmentStateAdapter;
    }

    public static int getTotals() {
        return totals;
    }

    public static void setTotals(int totals) {
        BaseDataModel.totals = totals;
    }

    public static ActionBar getActionBar01() {
        return actionBar01;
    }

    public static void setActionBar01(ActionBar actionBar01) {
        BaseDataModel.actionBar01 = actionBar01;
    }

    public static void setActionBar(ActionBar ab)
    {
        actionBar=ab;
    }
    public static ActionBar getActionBar()
    {
        return actionBar;
    }

    public static void setWindowManager(WindowManager manager) {

        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width=point.x;
        height=point.y;

    }

    public static boolean isIsNetWorkOk() {
        return isNetWorkOk;
    }

    public static void setIsNetWorkOk(boolean isNetWorkOk) {
        BaseDataModel.isNetWorkOk = isNetWorkOk;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static ChekNetWork getChekNetWork() {
        chekNetWork=new ChekNetWork();
        return chekNetWork;
    }

    /**
     * 回调 检测网络是否ok
     */
public  interface NetWorkChekProvider{
        public void checkNetWorkProvider(Boolean b);
    }
  public static class ChekNetWork extends AsyncTask<Void,Void,Boolean>
    {
        NetWorkChekProvider netWorkChekProvider;

        public void setNetWorkChekProvider(NetWorkChekProvider netWorkChekProvider) {
            this.netWorkChekProvider = netWorkChekProvider;
        }

        @Override
        public Boolean doInBackground(Void... voids) {

            return chekNetWork();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            netWorkChekProvider.checkNetWorkProvider(aBoolean);
        }
    }
//检测网络是否ok
    public static boolean chekNetWork()
    {

        try {
            String URL = "http://192.168.1.7:8081/";
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("检查网络状况","false");

            return false;
        }
    }

    public static void main(String[] args) {



        try {
            String URL = "http://192.168.1.7:8081/";
            Response response = OkHttpUtils
                    .post()
                    .url(URL)
                    .addHeader("connection", "close")
                    .build()
                    .connTimeOut(1000)
                    .readTimeOut(1000)
                    .execute();
            String cookie = response.header("cookie");

        } catch (IOException e) {
            e.printStackTrace();


        }


    }



}
