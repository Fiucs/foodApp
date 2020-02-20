package com.foodweb.task;

import com.foodweb.mapper.FoodsMapper;
import com.foodweb.pojo.FoodSimInfo;
import com.foodweb.pojo.UserRecord;
import com.foodweb.service.NormalSearch;
import com.utils.CalculateSimValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;


public class RecommendRunnable implements Runnable {


    public void setNormalSearch(NormalSearch normalSearch) {
        this.normalSearch = normalSearch;
    }

    private NormalSearch normalSearch;



    public void setFoodsMapper(FoodsMapper foodsMapper) {
        this.foodsMapper = foodsMapper;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private FoodsMapper foodsMapper;



    private RedisTemplate redisTemplate;

    private String uuid;//用户id

    private RecommendData recommendData;

    public void setRecommendData(RecommendData recommendData) {
        this.recommendData = recommendData;
    }
    private  List<FoodSimInfo> simInfoList;

    public void setSimInfoList(List<FoodSimInfo> simInfoList) {
        this.simInfoList = simInfoList;
    }

//设置用户id
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+"正在运行");


        //保存fid   -------和 recomend_value
        Map<String, String> map=new HashMap<>();
//        String uuid="f8afb838bf8f405eb01284810cf348e8";//从session中获得uuid 这里这只是 测试
        System.out.println("uuid:"+uuid);
        List<UserRecord> userReoords = foodsMapper.getUserReoords(uuid);//根据uuid查询出用户的浏览历史数据

        if (userReoords.size()==0)
            return;
        //set集合用于保存一个用户的结果
        Set<String> stringSet = new LinkedHashSet<>();

        //遍历历史数据
        for (UserRecord userReoord : userReoords) {

            String food_id = userReoord.getFood_id();//获得foodId数据
            //根据foodId 获得ingredients数据 从redis存缓中和获取
            String ingredients =(String) redisTemplate.boundHashOps("fid_ingredients").get(food_id);

            //获得 用户的 ingredients 时源
            FoodSimInfo simInfo01 = new FoodSimInfo(food_id,ingredients);
//            计算 用户浏览的一条记录-----------对应的所有相似值
            ArrayList<Map.Entry<String, Double>> tenthRes = CalculateSimValues.getTenthRes(simInfo01, simInfoList);

            System.out.println("相似度大于0的计算结果");

            int count=30;//每条记录的相似结果 的条数 定义为30条

            for (Map.Entry<String, Double> tenthRe : tenthRes) {
                if(tenthRe.getValue()>0 && count>=0)
                {
//                    stringSet.add(tenthRe.getKey());//添加 fid到set集合中
                    //添加到map集合中
                    if(map.get(tenthRe.getKey())==null)
                    {
                        map.put(tenthRe.getKey(),userReoord.getFood_id()+"/"+tenthRe.getKey()+":"+tenthRe.getValue());
                        count--;
                    }


//                    System.out.println(tenthRe.getKey()+" "+tenthRe.getValue());

                }
            }


            //更新user_records status表的status
            recommendData.updateUserRecordStatus(userReoord.getId());
//            foodsMapper.updateUserRecordStatus(userReoord.getId());

        }
        System.out.println(map);

        //计算 —---更新数据库表
        recommendData.insertIntoRecommend(uuid,map);







    }

//    //线程保护
//    private synchronized int add(int count )
//    {
//        return count+1;
//    }


}
