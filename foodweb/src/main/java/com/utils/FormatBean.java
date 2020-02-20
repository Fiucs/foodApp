package com.utils;

import com.foodweb.javabean.Rows;
import com.foodweb.pojo.FoodClass;
import com.foodweb.pojo.FoodsAll;
import com.github.pagehelper.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormatBean {

    /**
     * 转换 List菜bean数据
     * @param request
     * @param url
     * @param uri
     * @param res
     * @return
     */

    public static List<FoodsAll> forMatListBean(HttpServletRequest request, String url, String uri, List<FoodsAll> res){

        List<FoodsAll> result=res;
        System.out.println("------------------");

        try {

            System.out.println(request);
            for (FoodsAll food : result) {
                System.out.println(food);
                forMatFoodPic(food,url,uri);//转换 foodpic链接格式
                if (food.getMethods()==null)
                    continue;
                forMatFoodMethod(food,url,uri);//转换  method格式

            }
        }
        catch (Exception e)
        {
            System.out.println("未找到");
            return null;

        }

        return result;

    }

    /**
     * 转换一个 菜bean
     * @param request
     * @param url
     * @param uri
     * @param food
     * @return
     */
    public static FoodsAll forMatOnetBean(HttpServletRequest request, String url, String uri, FoodsAll food)
    {

        try {

            forMatFoodPic(food,url,uri);//转换 foodpic链接格式
            forMatFoodMethod(food,url,uri);//转换  method格式
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("转换失败");

        }

        return food;

    }






    /**
     * /转换 foodpic链接格式
     * @param food
     * @param url
     * @param uri
     */

    public static void forMatFoodPic(FoodsAll food,String url,String uri){

        if(food.getFoodpic()==null || food.getFoodpic().length()==0)
            return;//没有图片则不转换
        String s1 = food.getFoodpic().substring(food.getFoodpic().lastIndexOf("/") +1);
        food.setFoodpic(MethodStringUtils.getRealImageUrl(food.getFoodid(),url,uri,s1));


    }

    /**
     * 转换  method格式
     * @param food
     * @param url
     * @param uri
     */
    public static void forMatFoodMethod(FoodsAll food,String url,String uri){

        Map<String, List<String>> methods = food.getMethods();
        if(methods==null || methods.size()<=0)
            return;//没有method 则不转换
        List<String> list = methods.get("imgUrl");
        System.out.println(list);
        ArrayList<String> newList = new ArrayList<>();
        for (String s : list) {
            String substring = s.substring(s.lastIndexOf("/")+1);
            newList.add(MethodStringUtils.getRealImageUrl(food.getFoodid(),url,uri,substring));
        }
        methods.put("imgUrl",newList);

    }

    public static FoodClass formatFoodClassBean(FoodClass foodClass,String url,String uri)
    {

        try {
            String smallclass = foodClass.getSmallclass();
            String baseuri = "/imgs/imgclass";
            if(smallclass!=null)
            {
                String res = url.replace(uri, baseuri) + "/" + smallclass + ".PNG";
                foodClass.setSmallclass_pic(res);
            }
            return foodClass;

        } catch (Exception e) {
            System.out.println("此为大分类 没有小分类");
            e.printStackTrace();
            return foodClass;//不进行任何操作
        }

    }


}
