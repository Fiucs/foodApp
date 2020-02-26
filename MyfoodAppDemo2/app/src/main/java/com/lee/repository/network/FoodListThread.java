package com.lee.repository.network;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.repository.javabean.FoodsBean;
import com.lee.repository.javabean.FoodsClass;
import com.lee.repository.javabean.Rows;
import com.lee.repository.javabean.Rows01;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.PositionalDataSource;
import okhttp3.Response;

public class FoodListThread implements Runnable {
    private int currentPage;
    private int pageSize;
    private PositionalDataSource.LoadRangeCallback<FoodsBean> callback02;
    private PositionalDataSource.LoadInitialCallback<FoodsBean> callback01;
    private String URL;
    private static ObjectMapper mapper = new ObjectMapper();
    private List list;
    private boolean isInit;
    private int position;
    private int transflag;
    MutableLiveData<Boolean> isNetWork;//网络状态

    public static void changeData()
    {
        System.out.println("threadList被点击了");
    }

    public FoodListThread(List list,int currentPage, int pageSize,String url,int transflag,MutableLiveData<Boolean> net) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;

        this.isInit=(boolean)list.get(0);//初始化标志

        this.position =(Integer)list.get(1) ;
        this.callback01= (PositionalDataSource.LoadInitialCallback<FoodsBean>) list.get(2);
        this.callback02= (PositionalDataSource.LoadRangeCallback<FoodsBean>) list.get(3);
        this.URL = url;

        this.transflag=transflag;
        this.isNetWork=net;
    }

    @Override
    public void run() {

        Response response;
        int total =0;
        boolean flag = true;
        int reconnect=5;
        while (reconnect>0) {

            try {
                response = OkHttpUtils
                        .post()
                        .url(URL)
                        .addHeader("connection", "close")
                        .addParams("currentPage", String.valueOf(currentPage))
                        .addParams("pageSize", String.valueOf(pageSize))
                        .build()
                        .connTimeOut(5000)
                        .readTimeOut(5000)
                        .execute();
                String body = response.body().string();
                Log.d("tg",body);


                List foods=null;
                int totalpages;
                long totalColumn;//总条数
                //foodsbean
                if (transflag==0)
                {
                   Rows rows = mapper.readValue(body, Rows.class);
                     foods =(List<FoodsBean>) rows.getFoods();
                    totalpages= rows.getTotalpages();//总页数
                    totalColumn = rows.getTotal();

                }
                else
                {
                    Rows01 rows = mapper.readValue(body, Rows01.class);
                    foods=(List<FoodsClass>) rows.getFoods();
                    totalpages= rows.getTotalpages();//总页数
                    totalColumn=rows.getTotal();//总数
                }


               total =(int)totalColumn;
                if (isInit)
                {
                    BaseDataModel.setTotals((int) totalColumn);
                    callback01.onResult(foods,position);

                }
                else {
                    if (currentPage <= totalpages)
                    {
                        BaseDataModel.setTotals((int) totalColumn);
                        callback02.onResult(foods);
                    }else
                    {
                        callback02.onResult(new ArrayList<>());
                    }


                }

                flag = false;
                BaseDataModel.setIsNetWorkOk(true);//设置为联网 状态
//                isNetWork.setValue(true);
                System.out.println("联网状态：");
                break;//正常退出

            } catch (IOException e) {
                e.printStackTrace();
                flag = true;
                BaseDataModel.setIsNetWorkOk(false);//设置为 断网状态
                System.out.println("断网状态：");
//                isNetWork.setValue(false);
//                isNetWork.postValue(false);
            }

            reconnect=reconnect-1;
        }


        if(reconnect==0)
        {
            isNetWork.postValue(false);//断网
        }
        else
        {
            isNetWork.postValue(true);//联网
        }

//        isNetWork.postValue(true);
        System.out.println("总数：：：thread--"+total);


    }
}
