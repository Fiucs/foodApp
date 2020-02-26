//package com.lee.myfoodappdemo2;
//
//import android.inputmethodservice.Keyboard;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lee.repository.javabean.FoodsBean;
//import com.lee.repository.javabean.Rows;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import com.zhy.http.okhttp.callback.StringCallback;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONStringer;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Call;
//import okhttp3.Response;
//
//import static org.junit.Assert.*;
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//public class ExampleUnitTest {
//
//    ObjectMapper mapper = new ObjectMapper();
//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//        System.out.println("asasssssssss");
//    }
//
//    /**
//     * 同步
//     * @throws IOException
//     * @throws JSONException
//     */
//    @Test
//    public void getMultiFoodsInfo() throws IOException, JSONException {
//        String url = "http://localhost:8081/app/searchBySystemKey";
//        System.out.println(111111);
//        Response response = OkHttpUtils
//
//                .get()
//                .url(url)
//                .build()
//                .execute();
//
//        List<FoodsBean> list=new ArrayList<>();
//       Rows rows = mapper.readValue(response.body().string(), Rows.class);
//
//        System.out.println(rows.getTotal());
//        System.out.println(rows.getCurrentPage());
//        System.out.println(rows.getFoods());
//        for (FoodsBean foodsBean :rows.getFoods()) {
//            System.out.println(foodsBean.getFoodname());
//        }
//    }
//    /**
//     * 异步
//     *
//     */
//
//    @Test
//    public void testSync() throws InterruptedException {
////        String url = "http://localhost:8081/app/searchByName";
//        String url = "http://localhost:8081/app/searchBySystemKey";
//        System.out.println(111111);
//        final String[] result = new String[1];
//        OkHttpUtils
//                .post()
//                .url(url)
//                .addParams("currentPage","4")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                                e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        result[0] =response;
//
//                    }
//                });
//        Thread.sleep(2000);
//        try {
//            Rows rows = mapper.readValue(result[0], Rows.class);
//            for (FoodsBean foodsBean :rows.getFoods()) {
//                System.out.println(foodsBean.getFoodname());
//            }
//
//        } catch (JsonProcessingException e) {
//            System.out.println("为空");
//            e.printStackTrace();
//        }
//
//    }
//}