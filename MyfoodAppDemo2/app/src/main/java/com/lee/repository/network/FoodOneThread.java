package com.lee.repository.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.repository.javabean.FoodsBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import okhttp3.Response;

public class FoodOneThread implements Runnable {

    private String foodId;
    private boolean flag=true;//默认有网络
//    private String URL="http://192.168.43.64:8081/app/getOneFoodBean";
    private String URL="http://192.168.1.7:8081/app/getOneFoodBean";
    private static ObjectMapper mapper = new ObjectMapper();
    private static FoodsBean foodsBean;
    public FoodOneThread(String foodId)
    {
        this.foodId=foodId;
    }
    public static FoodsBean getFoodBean()
    {
        return foodsBean;
    }
    /**
     * 获取一个bean
     */
    @Override
    public void run() {

        Response response;

            try {
                response = OkHttpUtils
                        .post()
                        .url(URL)
                        .addHeader("connection", "close")
                        .addParams("foodId", String.valueOf(foodId))
                        .build()
                        .connTimeOut(5000)
                        .readTimeOut(5000)
                        .execute();
                String res=response.body().string();
                foodsBean = mapper.readValue(res, FoodsBean.class);

                BaseDataModel.setIsNetWorkOk(true);//设置为联网网 状态

            } catch (IOException e) {

                BaseDataModel.setIsNetWorkOk(false);//设置为断网 状态
                e.printStackTrace();
            }

    }
}
