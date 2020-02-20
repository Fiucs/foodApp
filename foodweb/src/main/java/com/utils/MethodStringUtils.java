package com.utils;

import java.util.*;

public class MethodStringUtils {

    public static void main(String[] args) {

        String words="第1步:辣白菜切小块，豆腐切块，海带切几刀，五花肉切片(^_^) imageURL:../img/unknown.png\n" +
                "第2步:锅里热少许油，放入五花肉，小火煎至肉出油，变得金黄。然后倒入辣白菜和汁继续翻炒一会出香味(^_^) imageURL:../img/unknown.png\n" +
                "第3步:倒入淘米水，烧开后加入海带和豆腐转中火继续炖10-15分钟(^_^) imageURL:../img/unknown.png\n" +
                "第4步:这时另起一锅煮面，顺便把小油菜焯烫一下捞出浸凉。面煮至六七成熟捞出过凉后，放入泡菜锅里，拌匀继续煮5分钟，加入小油菜，倒入生抽调味即可出锅(^_^) imageURL:../img/unknown.png";
//        String[] split = words.split("\n");
//        System.out.println(split.length);
//        System.out.println(Arrays.toString(split));
//
//
//        String word="第1步:辣白菜切小块，豆腐切块，海带切几刀，五花肉切片(^_^) imageURL:../img/unknown.png";
//        String[] strings = word.split("\\(\\^_\\^\\)");
//
//        System.out.println(strings[0]);
//        System.out.println(strings[1].trim());
//        url  http://localhost:8081/aaaa/bbbbb
//          uri   /aaaa/bbbbb

//        #2014圣诞节#Mini圣诞糖霜饼干-可爱笑脸圣诞树step9-157685324819.png
//        foodid
        Map<String, List<String>> stringListMap = splitOneWord(words);
        System.out.println(stringListMap);
//        String realImageUrl = getRealImageUrl("157685324819", "http://localhost:8081/imgs/#2014圣诞节#Mini圣诞糖霜饼干-可爱笑脸圣诞树step9-157685324819.png", "/imgs/#2014圣诞节#Mini圣诞糖霜饼干-可爱笑脸圣诞树step9-157685324819.png");
//        String realImageUrl = getRealImageUrl("157524365279", "http://localhost:8081/imgs/【发面糖饼】-157524365279.png", "/imgs/【发面糖饼】-157524365279.png");
        String realImageUrl = getRealImageUrl("157524365279", "http://localhost:8081/img/aaaa", "/img/aaaa","【发面糖饼】-157524365279.png");
        System.out.println(realImageUrl);


    }


    public static String getRealImageUrl(String foodid,String url,String uri,String imgname)
    {
        if(imgname.contains("unknown.png"))
        {
            return url.replace(uri,"/imgs/unknown.png");
        }

        String []orderIds={
                "157526515481","157535419142","157537504526","157539210082",
                "157540821791","157542422197","157544018821","157545617291",
                "157547226172","157549780829","157566375006","157574390147",
                "157578868917","157581941798","157586151433","157586151433",
                "157597232224","157601360851","157607479141","157613268507",
                "157620119786","157625692867","157631729703","157638138785",
                "157645627822","157649212368","157656099740","157661633728",
                "157667206006","157671533267","157678209412","157684928053",
                "157689327115","157694447696"

        };
        String baseuri="/imgs/img";
        long fid=Long.parseLong(foodid);
        for (int i=0;i<orderIds.length;i++)
        {

            if(fid<=Long.parseLong(orderIds[i]))
            {
                int firstPos=i/4+1;//取得imgXX 第一个位置
                int secondPos=i%4+1;//取得第二个位置
                baseuri=baseuri+firstPos+secondPos;
                break;//退出循环
            }

        }

        String res = url.replace(uri, baseuri) + "/"+imgname;

        return res;




    }

    //字符串 转集合类型

    public static Map<String,List<String>> splitOneWord(String words){

        class Imgurl0{
            private Imgurl0() {
            }

            private String getImgUrl(String string){
                String[] strings = string.split("imageURL:");
                return strings[1];
            }
        }

        Imgurl0 imgurl0 = new Imgurl0();
        String[] strings = words.split("\n");//得到每一行数据
        Map<String, List<String>> map=new HashMap<String, List<String>>();
        ArrayList<String> stepName = new ArrayList<>();
        ArrayList<String> imgUrl = new ArrayList<>();

        for (String string : strings) {

            String[] strings1 = string.split("\\(\\^_\\^\\)");
            if(strings1.length>=2) {
                System.out.println(Arrays.toString(strings1));
                stepName.add(strings1[0].trim());
                System.out.println(strings1[0]);
                imgUrl.add(imgurl0.getImgUrl(strings1[1].trim()).trim().replace("#", ""));
            }

        }
        map.put("stepname",stepName);
        map.put("imgUrl",imgUrl);

        return map;
    }


}
