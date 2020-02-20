package com.foodweb.task;

import com.foodweb.controller.LoginController;
import com.foodweb.mapper.FoodsMapper;
import com.foodweb.pojo.FoodSimInfo;
import com.foodweb.pojo.Recommend;
import com.foodweb.pojo.UserRecord;
import com.foodweb.service.NormalSearch;
import com.foodweb.service.imp.NormalSearchImp;
import com.utils.CalculateSimValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class RecommendTask {
    @Autowired
    private NormalSearch normalSearch;
    @Autowired
    private FoodsMapper foodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private String userid;
    private static String path=null;
    private static ServletContext application=null;

    private  List<FoodSimInfo> simInfoList=null;
    static {
        //获取路径
        path = LoginController.class.getClassLoader().getResource("onlineUser.txt").getPath();
        application= NormalSearchImp.getApplication();


    }



    //每5分钟执行一次 更新一个用户的推荐表 已经登陆的用户
    @Scheduled(cron = "0 0/5 * * * ?" )//秒/间隔 分 时 日
    public void task()
    {

        System.out.println("定时任务执行了:");
        List<String> uidList= getOnlineUid();

//        if(application==null)
//        {
//            //没有用户在线
//            return;
//        }

        if(uidList.size()<=0)
        {
            return;//不满足条件直接退出

        }

        System.out.println(uidList);
//        获取 所有 的fid--ingre的值
        if(simInfoList==null)
        {
            simInfoList = getAllIngredientsFoodId();//初始化foodid_ingre的数据
        }

        System.out.println("-------------------------");
//使用多线程完成 推荐表的更新(包括计算值+写入数据表)  uidList

        RecommendData recommendData = new RecommendData();
        recommendData.setNormalSearch(normalSearch);//设置nomalsearch
        List<RecommendRunnable> runnableList=new ArrayList<>();//保存多线程的 runnable实现方法
        for (int i=0;i<uidList.size();i++)
        {
            RecommendRunnable runnable = new RecommendRunnable();
            runnable.setRecommendData(recommendData);//设置 更新操作相关对象
            runnable.setUuid(uidList.get(i));//设置 uuid
            runnable.setSimInfoList(simInfoList);//设置 fid -ingredients计算数据
            runnable.setFoodsMapper(foodsMapper);//设置 foodMapper
            runnable.setRedisTemplate(redisTemplate);//设置redisTemplate
            runnable.setNormalSearch(normalSearch);//设置 normalsearch
            //FIXME 子线程无法进行bean注入 这里采用传参操作
            runnableList.add(runnable);

        }

        for (int i = 0; i < runnableList.size(); i++) {
            new Thread(runnableList.get(i),"线程00000000000000000000000XX"+i).start();//启动线程 依次

        }


    }


    public  List<FoodSimInfo>  getAllIngredientsFoodId()
    {

        List<FoodSimInfo> simInfoList=new ArrayList<>();

        Map<String, Set<String>> map=new HashMap<>();

        System.out.println(111111111);


        //获取所有 fid ----ingred数据用于计算
        Set<String> fidSet = redisTemplate.boundHashOps("fid_ingredients").keys();

        for (String fid : fidSet) {

            String ingredients = (String) redisTemplate.boundHashOps("fid_ingredients").get(fid);
            System.out.println(fid+":::::"+ingredients);
            FoodSimInfo simInfo = new FoodSimInfo(fid,ingredients);
            simInfoList.add(simInfo);

        }
        System.out.println(fidSet.size());

        return simInfoList;
    }


    public List<String> getOnlineUid()
    {
        BufferedReader bufferedReader=null;
        String s;
        List<String> uidList=new ArrayList<>();

        try {
            bufferedReader=new BufferedReader(new FileReader(path));
            while ((s=bufferedReader.readLine())!=null)
            {
                if(s.length()>0)
                {
                    uidList.add(s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uidList;


    }

    public void recommendGeneal(List<String> uuidList){
        List<FoodSimInfo> simInfoList=new ArrayList<>();

        Map<String, Set<String>> map=new HashMap<>();

        System.out.println(111111111);



        //获取所有 fid ----ingred数据用于计算
        Set<String> fidSet = redisTemplate.boundHashOps("fid_ingredients").keys();

        for (String fid : fidSet) {

            String ingredients = (String) redisTemplate.boundHashOps("fid_ingredients").get(fid);
            System.out.println(fid+":::::"+ingredients);
            FoodSimInfo simInfo = new FoodSimInfo(fid,ingredients);
            simInfoList.add(simInfo);

        }
        System.out.println(fidSet.size());
        System.out.println("-------------------------");

        String uuid="f8afb838bf8f405eb01284810cf348e8";//从session中获得uuid 这里这只是 测试
        List<UserRecord> userReoords = foodsMapper.getUserReoords(uuid);//根据uuid查询出用户的浏览历史数据
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
                    stringSet.add(tenthRe.getKey());//添加 fid到set集合中
                    count--;
//                    System.out.println(tenthRe.getKey()+" "+tenthRe.getValue());

                }
            }
            map.put(uuid,stringSet);

        }

//此处应该  考虑线程的并发
        for (String s : stringSet) {
            Recommend recommend = new Recommend(uuid, s, new Date());
            int i = normalSearch.insertIntoRecommend(recommend);//插入数据到表
            System.out.println(i+":"+recommend);
        }

        System.out.println("结果数量为:"+stringSet.size());


    }






}
