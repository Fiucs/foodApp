package com.utils;

import com.foodweb.pojo.FoodsAll;
import com.foodweb.service.NormalSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SpirngRedisDdemo1 {

    @Autowired
    private static RedisTemplate redisTemplate;

    @Autowired
    private static NormalSearch normalSearch;
//
//    @Test
//    public void setValue(){
//        redisTemplate.boundValueOps("name").set("leezz");
//    }
//
//    @Test
//    public void getValue()
//    {
//        String name =(String) redisTemplate.boundValueOps("name").get();
//
//        System.out.println(name);
//    }
//    @Test
//    public void deleteVallue()
//
//    {
//        redisTemplate.delete("name");
//    }


    public static void main(String[] args) {

        SpirngRedisDdemo1 spirngRedisDdemo1 = new SpirngRedisDdemo1();
        spirngRedisDdemo1.savefid_ingredients();
        System.out.println("-----------");


    }

    public void savefid_ingredients()
    {

        List<FoodsAll> list = normalSearch.searchAllfidIngredients();
        System.out.println(list.size());


    }

}
