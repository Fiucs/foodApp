package com.utils;

import com.foodweb.pojo.FoodSimInfo;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateSimValues {
    private  static  Properties pro=null;
   private static StopRecognition fillter =null;


    static {
//        InputStream ins = Test01.class.getClassLoader().getResourceAsStream("words.txt");//字节流无法读中文
        String path = CalculateSimValues.class.getClassLoader().getResource("words.txt").getPath();
         pro= new Properties();
        fillter = new StopRecognition();
        fillter.insertStopNatures("ad");//副词
        fillter.insertStopNatures("s");//过滤处动词
        fillter.insertStopNatures("a");//形容词
        fillter.insertStopNatures("m");//数词
        fillter.insertStopNatures("mq");//数量词
        fillter.insertStopNatures("q");//量词
        fillter.insertStopNatures("v");//动词
        fillter.insertStopNatures("vd");//副动词
        fillter.insertStopNatures("vn","ude1","ude2","ude3","usuo","udeng","uyy" ,
                "udh","uls","uzhi","e","y","o","ns");//动名词

        fillter.insertStopWords("大勺","小勺","大匙","小匙","一小勺","一大勺","一小","一大","吸水性","个人","没关系"
        ,"照片","不过","多少","真得","天使","手工","韩国","nr2");//添加停用词


        fillter.insertStopRegexes("^\\(w+\\)$");
        System.out.println(path);


        try {
            pro.load(new FileReader(path));//加载 近义词替换


        } catch (IOException e) {

        }

    }


//    public static void main(String[] args) {
//
////        Result parse = BaseAnalysis.parse("42g:细砂糖,90g:蛋白,45g:蛋黄,45g:牛奶2,42g:牛奶1,35g:低筋粉,100g:奶油乳酪");
////        System.out.println(parse.get(0).getName());
////        String[] split = parse.toStringWithOutNature().split(",");
////        List<String> list = new ArrayList<>(Arrays.asList(split));
////        KeyWordComputer<Analysis> computer = new KeyWordComputer<>(10);
////        Collections.sort(list);
////        System.out.println(computer.computeArticleTfidf(parse.toStringWithOutNature()));
////
////        System.out.println(list);
////        System.out.println("-----------------------------------------------");
//        //42g:细砂糖,90g:蛋白,45g:蛋黄,45g:牛奶2,42g:牛奶1,35g:低筋粉,100g:奶油乳酪"
//        Result parse1 = ToAnalysis.parse("42g:细砂糖,90g:蛋白,45g:蛋黄,45g:牛奶2,42g:牛奶1,35g:低筋粉,100g:奶油乳酪");
//
//        String[] split1 = parse1.toStringWithOutNature().split(",");
//        List<String> list1 = new ArrayList<>(Arrays.asList(split1));
//        KeyWordComputer<Analysis> computer1 = new KeyWordComputer<>(20);
//        Collections.sort(list1);
//        System.out.println(computer1.computeArticleTfidf(parse1.toStringWithOutNature()));
//
//        System.out.println("------------------------------");
////        String str11="42g:细砂糖,90g:蛋白,45g:蛋黄,45g:牛奶2,42g:牛奶1,35g:低筋粉,100g:奶油乳酪";
//        String str11="一块:五花肉,适量:香菇，木耳，黄花菜,2个:鸡蛋,适量:虾仁,适量:香葱，姜片，淀粉,适量:玉米油，料酒，生抽,适量:老抽，盐，糖，高汤粉";
//        String str = "中种面团,140g:高粉,50g:牛奶,40g:全蛋液,10g:细砂糖,2g:酵母,主面团,20g:高粉,40g:低粉,40g:细砂糖,2g:盐,7g:奶粉,35g:牛奶,30g:黄油,椰蓉馅,30g:黄油,25g:细砂糖,30g:鸡蛋液,30g:牛奶,60g:椰蓉";
//        String str2="3张:豆腐皮,2个:鸡蛋";
//        String str3="适量:豆腐,2个:鸡蛋";
//        String str4="2克:白糖,60克:香油,7克:料酒,7克:姜,3克:味精,20克:酱油,8克:食盐,200克:水,3克:碱,250克:猪肉,100克:面肥,400克:高筋面粉";
//        String str5="适量:水,5克:植物油,3克:香油,5克:料酒,5克:姜,5克:葱,3克:味精,3克:酱油,5克:食盐,5克:花生油,100克:黑木耳,100克:粉条（干）,5克:酵母(干),100克:虾米,250克:小麦面粉,150克:猪肉,250克:苔菜";
//        String str6="猪肉（肥瘦搭配）,开洋(ssss),千张皮（萨达萨达）,葱,姜,生抽,白糖,盐,料酒";
//        str6=moveOthersWords(str6);
//
//        Result parse = BaseAnalysis.parse(str11).recognition(fillter);
//
//        System.out.println(parse);
//
//
//
//
////        String str1 = "([\\u4e00-\\u9fa5]+)";
////        Matcher matcher= Pattern.compile(str1).matcher(str);
//
////        while(matcher.find()){
////
////            String group = matcher.group(1);
////            System.out.println(group);
////        }
//
////        System.out.println("-----------------");
////        String[] split2 = str.split(",");
////        for (int i = 0; i < split2.length; i++) {
////            String[] split3 = split2[i].split(":");
////            if(split3.length>=2)
////            {
////                System.out.println(split3[1]);
////            }
////            else
////                System.out.println(split3[0]);
////        }
////        transfer(str,str11);
//        ArrayList<FoodSimInfo> list = new ArrayList<>();
//        list.add(new FoodSimInfo("2",str11));
//        list.add(new FoodSimInfo("3",str));
//        list.add(new FoodSimInfo("4",str2));
//        list.add(new FoodSimInfo("5",str3));
//        list.add(new FoodSimInfo("6",str4));
//        list.add(new FoodSimInfo("7",str5));
//        list.add(new FoodSimInfo("8",str6));
////        List<FoodSimInfo> transfer1 = transfer(list);//其他用户参考样本
//
////        list.clear();
////        list.add(new FoodSimInfo(1,str2));
////        List<FoodSimInfo> transfer = transfer(list);//user样本
//
//
//        //getVectors(transfer1.get(0),transfer.get(0));
//        ArrayList<Map.Entry<Integer, Double>> sortList = getTenthRes(new FoodSimInfo("1", str2), list);
//
//        for (Map.Entry<Integer, Double> entry : sortList) {
//            System.out.println(entry.getKey()+": "+entry.getValue());
//        }
//
//        System.out.println();
//
//        System.out.println(moveOthersWords(str6));
//
//    }

    public static String moveOthersWords(String str)
    {
        String reg="\\(.*?\\)|（.*?）";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        //System.out.println(matcher.replaceAll(""));
        return matcher.replaceAll("");
    }

    /*接受一定数量的数据 和主数据
    并将数据 按相似度排序
     */
    public static ArrayList<Map.Entry<String, Double>> getTenthRes(FoodSimInfo u1, List<FoodSimInfo> others ){
        ArrayList<FoodSimInfo> userInfos = new ArrayList<>(Arrays.asList(u1));
        List<FoodSimInfo> transferUser=transfer(userInfos);//得到用户的分词结果
        List<FoodSimInfo> transferOthers = transfer(others);//得到分词结果

        TreeMap<String, Double> sortMap = new TreeMap<>();

        //用户 ----其他用户 两两计算 得到相似值
        for (FoodSimInfo other : transferOthers) {

            TreeMap<String, Double> map = getVectors(transferUser.get(0), other);//存有 Oid 与相似度x的值
            sortMap.put(other.getfoodid(),map.get(other.getfoodid()));//保存到map中 用于排序
        }

        ArrayList<Map.Entry<String, Double>> sortList = new ArrayList<>(sortMap.entrySet());


        Collections.sort(sortList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {

                double v = o2.getValue() - o1.getValue();


                if(v>0.00001)
                {
                    System.out.println("差值:="+v);
                    return 1;
                }
                 if(v<0)
                {
                    return -1;
                }

                    return 0;

            }
        });


        return sortList;

    }


    /**
     *
     * 计算map1  与 list<map>
     *     转为他们的向量值
     *     并且计算出相似度
     *
     *
     * @return*/

    public static  TreeMap<String, Double> getVectors(FoodSimInfo u1, FoodSimInfo u2){
        Map<String, Integer> divres2 = new HashMap<>(u2.getDivres());//开辟空间 保存零时值 避免 u1空间的值被改变
        Map<String, Integer> divres1 = new HashMap<>(u1.getDivres());//开辟空间 保存零时值
        Map<String,Integer> tmpdiv=new HashMap<>(u1.getDivres());//开辟空间 保存零时值
        System.out.println(divres1);
        System.out.println(divres2);

        //得到 主用户的向量值
        for (String s : divres2.keySet()) {
            if(divres1.get(s)==null)
            {
                divres1.put(s,0);
            }

        }

        //
        //得到 次用户的向量值
        System.out.println(tmpdiv);
        for (String s : tmpdiv.keySet()) {
            if(divres2.get(s)==null)
            {
                divres2.put(s,0);
            }

        }

        System.out.println("主菜谱关键词-向量"+": "+divres1);
        System.out.println("副菜谱关键词-向量"+": "+divres2);

        System.out.println("计算相似度：");

        double fenzi=0;
        double fenmuA=0;
        double fenmuB=0;
        for (String s : divres1.keySet()) {
            //求分子、
            fenzi+=divres1.get(s)*divres2.get(s);

            fenmuA+=Math.pow(divres1.get(s),2);
            fenmuB+=Math.pow(divres2.get(s),2);

            //求分母
        }

        double x = fenzi / (Math.sqrt(fenmuA * fenmuB));
        x=Double.isNaN(x)?0.0:x;
        //保留16位小数
        DecimalFormat decimal = new DecimalFormat("#0.000000000000");
        x = Double.parseDouble(decimal.format(x));
        System.out.println(u1.getfoodid()+":"+u2.getfoodid()+" ="+x);
        TreeMap<String, Double> map = new TreeMap<>();//map 不能按value排序
        map.put(u2.getfoodid(),x);//将目标菜谱id 和结果相似度保存到 主食物的map、··集合中。下一步将主菜谱-目标菜谱的id 保存到数据库中。
//        数据库格式中     majorFoodId  minorFoodId similarValue

        return map;


    }





    /**
     * 菜的向量的list<map> 分词结果 第一个为
     *
     */


    /**
     * 重载形式  c参数为list
     * @param listmap
     * @return
     */
    //

    public static List<FoodSimInfo> transfer(List<FoodSimInfo> listmap){
        KeyWordComputer<Analysis> keyWordComputer = new KeyWordComputer<>(20);//取前30个出现频率最高的词汇
        List<FoodSimInfo> list=new ArrayList<>();
        for (FoodSimInfo s : listmap) {

            Result parse = BaseAnalysis.parse(s.getingredients()).recognition(fillter);//启用 停用词配置
            List<Keyword> keywords = keyWordComputer.computeArticleTfidf(parse.toStringWithOutNature());
            TreeMap<String, Integer> map = new TreeMap<>();//设置map 保存分词结果
            for (Keyword keyword : keywords) {

                String s1 = pro.getProperty(keyword.getName());
              //  System.out.println(s1);
                if(s1!=null)
                    map.put(s1,1);
                //同义词替换
                else
                    map.put(keyword.getName(),1);

            }
            //得到结果
            FoodSimInfo info = new FoodSimInfo(s.getfoodid(),map);//保存 字符
            list.add(info);

        }

        System.out.println(list);
        return list;


    }


    public static void transfer(String s1,String s2) {

        String str1 = "([\\u4e00-\\u9fa5]+)";
        Pattern compile = Pattern.compile(str1);
        Matcher matcher1 = compile.matcher(s1);
        Matcher matcher2 = compile.matcher(s2);
        List<Matcher> list = Arrays.asList(matcher1, matcher2);//得到list集合
        HashMap<String, Integer> map1 = new HashMap<>();//保存 s1的 词频
        HashMap<String, Integer> map2 = new HashMap<>();//保存 s2的·词频
        //得到 s1的分词
        int count=0;
        for (Matcher matcher : list) {

            while (matcher.find())//采用 正则表达式
            {
                if(count==0)
                map1.put(matcher.group(1),0);
                else
                    map2.put(matcher.group(1),0);
            }
            count++;

    }

        System.out.println("111111111111111");
        System.out.println(map1);
        System.out.println("222222222222222");
        System.out.println(map2);




    }






}
