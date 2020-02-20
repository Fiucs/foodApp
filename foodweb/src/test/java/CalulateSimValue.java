import com.foodweb.javabean.Rows;
import com.foodweb.javabean.review.CommentDetailBean;
import com.foodweb.javabean.review.ReplyDetailBean;
import com.foodweb.mapper.FoodsMapper;
import com.foodweb.pojo.*;
import com.foodweb.service.NormalSearch;
import com.foodweb.service.imp.NormalSearchImp;
import com.foodweb.task.RecommendData;
import com.foodweb.task.RecommendRunnable;
import com.github.binarywang.java.emoji.EmojiConverter;
import com.utils.CalculateSimValues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:myapplication.xml"})
@Transactional
public class CalulateSimValue {



    @Autowired
    private NormalSearch normalSearch;
    @Autowired
    private FoodsMapper foodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 测试  基本数据
     */

    @Test
    public void test01(){
        String str11="一块:五花肉,适量:香菇，木耳，黄花菜,2个:鸡蛋,适量:虾仁,适量:香葱，姜片，淀粉,适量:玉米油，料酒，生抽,适量:老抽，盐，糖，高汤粉";
        String str = "中种面团,140g:高粉,50g:牛奶,40g:全蛋液,10g:细砂糖,2g:酵母,主面团,20g:高粉,40g:低粉,40g:细砂糖,2g:盐,7g:奶粉,35g:牛奶,30g:黄油,椰蓉馅,30g:黄油,25g:细砂糖,30g:鸡蛋液,30g:牛奶,60g:椰蓉";
        String str2="3张:豆腐皮,2个:鸡蛋";
        String str3="适量:豆腐,2个:鸡蛋";
        String str4="2克:白糖,60克:香油,7克:料酒,7克:姜,3克:味精,20克:酱油,8克:食盐,200克:水,3克:碱,250克:猪肉,100克:面肥,400克:高筋面粉";
        String str5="适量:水,5克:植物油,3克:香油,5克:料酒,5克:姜,5克:葱,3克:味精,3克:酱油,5克:食盐,5克:花生油,100克:黑木耳,100克:粉条（干）,5克:酵母(干),100克:虾米,250克:小麦面粉,150克:猪肉,250克:苔菜";
        String str6="猪肉（肥瘦搭配）,开洋(ssss),千张皮（萨达萨达）,葱,姜,生抽,白糖,盐,料酒";
        str6= CalculateSimValues.moveOthersWords(str6);

        ArrayList<FoodSimInfo> list = new ArrayList<>();
        list.add(new FoodSimInfo("2",str11));
        list.add(new FoodSimInfo("3",str));
        list.add(new FoodSimInfo("4",str2));
        list.add(new FoodSimInfo("5",str3));
        list.add(new FoodSimInfo("6",str4));
        list.add(new FoodSimInfo("7",str5));
        list.add(new FoodSimInfo("8",str6));

        ArrayList<Map.Entry<String, Double>> sortList = CalculateSimValues.getTenthRes(new FoodSimInfo("1", str2), list);

        for (Map.Entry<String, Double> entry : sortList) {
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
    }

    /**
     *  测试  数据库数据
     */

    @Test
    @Rollback(value = false)
    public void test02(){
        List<FoodSimInfo> simInfoList=new ArrayList<>();

        Map<String,Set<String>> map=new HashMap<>();

        System.out.println(111111111);




//        List<FoodsAll> list = normalSearch.searchByclass01("%面%", 1, 2000);
//
//        for (FoodsAll foodsAll : list) {
//            FoodSimInfo simInfo = new FoodSimInfo(foodsAll.getFoodid(), foodsAll.getIngredients());
//            if(!simInfoList.contains(simInfo)) {
//                simInfoList.add(simInfo);
//                System.out.println(foodsAll);
//            }
//        }
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
//        FoodSimInfo simInfo01 = new FoodSimInfo(list.get(0).getFoodid(), list.get(0).getIngredients());

//        if(simInfoList.contains(simInfo01))
//        {
//            System.out.println("foodid相同了");
//        }

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


        for (String s : stringSet) {

            Recommend recommend = new Recommend(uuid, s, new Date());
            int i = normalSearch.insertIntoRecommend(recommend);//插入数据到表
            System.out.println(i+":"+recommend);
        }

        System.out.println("结果数量为:"+stringSet.size());



//
//        for (Map.Entry<String, Double> tenthRe : tenthRes) {
//            System.out.println(tenthRe.getKey()+" "+tenthRe.getValue());
//
//        }
//        simInfoList.clear();



    }

    //uuid
    //生成用户唯一 uid标识
    @Test
    public void generalUUID()
    {
//        List list=new ArrayList<String>();
//        //生成 用户唯一id
//        for (int i=0;i<10000;i++)
//        {
//            String uuid = UUID.randomUUID().toString().trim().replace("-","");
//
//            if(!list.contains(uuid))
//            {
//                list.add(uuid);
//            }
//
////        }
//
//        System.out.println(list.size());

        String uuid = UUID.randomUUID().toString().trim().replace("-","");
        System.out.println(uuid);
    }

    /**
     * 获取用户历史记录
     */
    @Test
    public void getUserReocrds()
    {

        String uuid="f8afb838bf8f405eb01284810cf348e8";
        List<UserRecord> userReoords = foodsMapper.getUserReoords(uuid);

        for (UserRecord userReoord : userReoords) {
            System.out.println(userReoord);
        }

    }

    /**
     * 查询出所有fid  ingredients
     */
    @Test
    public void  searchAllFidIngredients()
    {
        List<FoodsAll> list = normalSearch.searchAllfidIngredients();

        for (FoodsAll foodsAll : list) {

            System.out.println(foodsAll.getFoodid());
            //redis中存入  fid--inredients数据
            redisTemplate.boundHashOps("fid_ingredients").put(foodsAll.getFoodid(),foodsAll.getIngredients());
            //redis中存入 fid--smallclass数据
            redisTemplate.boundHashOps("fid_smallclass").put(foodsAll.getFoodid(),foodsAll.getSmallclass());
            //redis中存入 fid-bigclass数据
            redisTemplate.boundHashOps("fid_bigclass").put(foodsAll.getFoodid(),foodsAll.getBigclass());

        }


        System.out.println(list.size());
    }

    @Test
    public void getAllFidIngre()
    {
        List<FoodSimInfo> simInfoList=new ArrayList<>();


        String foodid =(String) redisTemplate.boundHashOps("fid_ingredients").get("157694375928");
        String foodsmallclass=(String)redisTemplate.boundHashOps("fid_smallclass").get("157694375928");
        String foodbigclass=(String)redisTemplate.boundHashOps("fid_bigclass").get("157694375928");
        System.out.println(foodid+"小分类:"+foodsmallclass+" 大分类:"+foodbigclass);
        System.out.println("--------------------");



        Set<String> fidSet = redisTemplate.boundHashOps("fid_ingredients").keys();

        for (String fid : fidSet) {

            String ingredients = (String) redisTemplate.boundHashOps("fid_ingredients").get(fid);
            System.out.println(fid+":::::"+ingredients);
            FoodSimInfo simInfo = new FoodSimInfo(fid,ingredients);
            simInfoList.add(simInfo);

        }
        System.out.println(fidSet.size());


    }

    /**
     * 用户记录查询
     */
    @Rollback(value = false)
    @Test
    public void testRunnable()
    {

//        foodsMapper.updateUserRecordStatus(2);

        List<UserRecord> userReoords = foodsMapper.getUserReoords("asd");
        System.out.println(userReoords);

        Recommend recommend = new Recommend("52b986aa88214a3787eb7a9f316cd624","157513087209",new Date());

        int i = foodsMapper.insertIntoRecommend(recommend);
        System.out.println("影响的行数："+i);

    }
    /**
     * 用户表查询
     */

    @Test
    public void testRegister()
    {

        System.out.println("----------注册------");
        MsgInfo msgInfo = normalSearch.register("aaaa", "123456");
        MsgInfo msgInfo01 = normalSearch.register("asd", "123456");
        System.out.println(msgInfo);
        System.out.println(msgInfo01);

        System.out.println("-------登录------------");
        MsgInfo msgInfo1 = normalSearch.login("aaaa", "sadas");
        MsgInfo msgInfo2 = normalSearch.login("asd", "sadas");
        MsgInfo msgInfo3 = normalSearch.login("asd", "12331");
        System.out.println(msgInfo1);
        System.out.println(msgInfo2);
        System.out.println(msgInfo3);


    }
    /**
     * 随机下啊表
     */
    @Test
    public void randomTest()
    {
        int count=10;
        List<Integer> listSub=new ArrayList<>();
        while (count>0)
        {
            int sub =(int) (Math.random() * (10));


            if(!listSub.contains(sub))
            {
                listSub.add(sub);
                System.out.println(sub);
                count--;
            }


        }

        System.out.println(listSub);
        System.out.println("-----------------------");

        FoodsAll foodsAll = foodsMapper.searchPartBeanByFoodId("157548553722");
        System.out.println(foodsAll);
        System.out.println("-----------------------");
//        List<FoodsAll> list = normalSearch.searchFromRecommendTb("52b986aa88214a3787eb7a9f316cd623", 100);
//        for (FoodsAll all : list) {
//            System.out.println(all);
//        }
//
//        System.out.println(list.size());
//
//        NormalSearchImp.initAppliation(new ServletContext() {
//            @Override
//            public String getContextPath() {
//                return null;
//            }
//
//            @Override
//            public ServletContext getContext(String s) {
//                return null;
//            }
//
//            @Override
//            public int getMajorVersion() {
//                return 0;
//            }
//
//            @Override
//            public int getMinorVersion() {
//                return 0;
//            }
//
//            @Override
//            public String getMimeType(String s) {
//                return null;
//            }
//
//            @Override
//            public Set getResourcePaths(String s) {
//                return null;
//            }
//
//            @Override
//            public URL getResource(String s) throws MalformedURLException {
//                return null;
//            }
//
//            @Override
//            public InputStream getResourceAsStream(String s) {
//                return null;
//            }
//
//            @Override
//            public RequestDispatcher getRequestDispatcher(String s) {
//                return null;
//            }
//
//            @Override
//            public RequestDispatcher getNamedDispatcher(String s) {
//                return null;
//            }
//
//            @Override
//            public Servlet getServlet(String s) throws ServletException {
//                return null;
//            }
//
//            @Override
//            public Enumeration getServlets() {
//                return null;
//            }
//
//            @Override
//            public Enumeration getServletNames() {
//                return null;
//            }
//
//            @Override
//            public void log(String s) {
//
//            }
//
//            @Override
//            public void log(Exception e, String s) {
//
//            }
//
//            @Override
//            public void log(String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public String getRealPath(String s) {
//                return null;
//            }
//
//            @Override
//            public String getServerInfo() {
//                return null;
//            }
//
//            @Override
//            public String getInitParameter(String s) {
//                return null;
//            }
//
//            @Override
//            public Enumeration getInitParameterNames() {
//                return null;
//            }
//
//            @Override
//            public Object getAttribute(String s) {
//                return null;
//            }
//
//            @Override
//            public Enumeration getAttributeNames() {
//                return null;
//            }
//
//            @Override
//            public void setAttribute(String s, Object o) {
//
//            }
//
//            @Override
//            public void removeAttribute(String s) {
//
//            }
//
//            @Override
//            public String getServletContextName() {
//                return null;
//            }
//        });
//
//        ServletContext application = NormalSearchImp.getApplication();
//        System.out.println(application);


        HttpServletRequest request = null;

        System.out.println("-------------");

        FoodsAll foods01 = foodsMapper.searchPartBeanById(15);
        System.out.println(foods01);

        System.out.println("-------------");
        int count1 = foodsMapper.seachCountOfFoods();
        System.out.println("总记录数："+count1);

        System.out.println("随便看看");

        List<FoodsAll> randomResult = normalSearch.getRandomResult(20, null);
        for (FoodsAll all : randomResult) {
            System.out.println(all);
        }


        System.out.println("------------------------------");

        List<FoodsAll> list = normalSearch.searchFromRecommendTb("52b986aa88214a3787eb7a9f316cd623", 100, null);

        for (FoodsAll all : list) {
            System.out.println(all);
        }
        System.out.println("大小:"+list.size());


    }
    /**
     * 查询所有分类
     */
    @Test

    public void findAllFoodClassTest()
    {

//        Rows rows = normalSearch.findAllFoodClassInfo(null);
//
//        List<FoodClass> foods =(List<FoodClass>) rows.getFoods();
//        for (FoodClass food : foods) {
//            System.out.println(food);
//        }

        normalSearch.searchByclass(null,"面",1,15,1,null);

    }
    /**
     * 评论回复评论测试
     */
    @Test
    @Rollback(value = false)
    public void testReview()
    {
        ReplyDetailBean detailBean = new ReplyDetailBean();
        detailBean.setNickName("王五");
        detailBean.setContent("hahahahas🙃hhaha");
        detailBean.setCommentId(1);
        detailBean.setCreateDate(new Date());
        System.out.println(detailBean);
//        foodsMapper.inserIntoReply(detailBean);
        MsgInfo msgInfo = normalSearch.updateReplys(null, detailBean);
        System.out.println(msgInfo);
        int id = detailBean.getId();


        System.out.println("主键ID:"+id);

        System.out.println("-------------------------");
        System.out.println("-----通过CommentId查找reply------");
        List<ReplyDetailBean> beanList = foodsMapper.searchReplyByCommentId(1);
        System.out.println(beanList);
    }
    @Test
    @Rollback(value = false)
    public void testReview01()
    {
        EmojiConverter instance = EmojiConverter.getInstance();
        CommentDetailBean commentDetailBean = new CommentDetailBean();
        commentDetailBean.setNickName("爱爱爱爱啊");
        String s = instance.toAlias("你是谁😂");
        commentDetailBean.setContent(s);
        commentDetailBean.setFoodid("11111sdsadsad");
//        commentDetailBean.setReplyId(); //需要插入回复后才有id 对应 CommentID
        commentDetailBean.setCreateDate(new Date());


//        String conten="你是谁😘😘";
//        String ss="\uD83D\uDE02";
//        EmojiConverter instance = EmojiConverter.getInstance();
//        String s = instance.toAlias(conten);
//        String s1 = instance.toUnicode(s);
//        System.out.println(s1);

        System.out.println("---------------------------------");
        List<CommentDetailBean> commentDetailBeans = foodsMapper.searchCommentBeanByFoodId("11111sdsadsad");
        String s1 = commentDetailBeans.get(0).getContent();
        System.out.println(instance.toUnicode(s1));
    }

    /**
     * 查询评论
     */
    @Test
    public void testReview02() throws UnsupportedEncodingException {
////        List<CommentDetailBean> commentsByFoodId = normalSearch.getCommentsByFoodId(null, "11111sdsadsad", 1, 3);
//        for (CommentDetailBean commentDetailBean : commentsByFoodId) {
//            System.out.println(commentDetailBean);
//
//        }
        EmojiConverter instance = EmojiConverter.getInstance();
        String s = instance.toAlias("撒旦😂");
        String s1 = instance.toHtml("撒旦😂");
        String s2 = instance.toUnicode("撒旦😂");
        System.out.println(s);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println("--------------URLencoder----------------");

        String s4 = URLEncoder.encode("撒旦😂", "utf-8");
        System.out.println(s4);

        String s3 = Base64.getEncoder().encodeToString("撒旦😘".getBytes());
        System.out.println(s3);


    }
/**
 * 插入评论
 *
 */
@Test
    @Rollback(value = false)
    public void ReviewUpdateTest()
{

    CommentDetailBean bean = new CommentDetailBean();
    bean.setFoodid("123321");
    bean.setContent("啊啊啊啊😋");
    bean.setCreateDate(new Date());
    MsgInfo msgInfo = normalSearch.updateComments(null, bean);
    System.out.println(msgInfo);


}

    /**
     * 收藏相关
     */
    @Test
@Rollback(value = false)
    public void collectFoods()
{
    Favarites favarites01 = new Favarites();
    favarites01.setUser_id("f8afb838bf8f405eb01284810cf348e8");
    favarites01.setFood_id("157520557898");
    favarites01.setFavorite("1");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    Rows rows = normalSearch.searchHistory("f8afb838bf8f405eb01284810cf348e8", 1, 3);
    System.out.println(rows.getTotal());
    for (FoodsAll food : (List<FoodsAll>)rows.getFoods()) {
        System.out.println(food);
        System.out.println(dateFormat.format(food.getDerive_time()));
    }

    //查询收藏

    Rows rows1 = normalSearch.searchFavorites("f8afb838bf8f405eb01284810cf348e8", 1, 3);
    System.out.println(rows1.getTotal());
    for (FoodsAll food : (List<FoodsAll>)rows1.getFoods()) {
        System.out.println(food);
        System.out.println(dateFormat.format(food.getDerive_time()));
    }


    //修改收藏
//重新收藏
    MsgInfo msgInfo = normalSearch.updateFavorites(favarites01);
    System.out.println(msgInfo);
    //取消收藏
//    favarites01.setFavorite("0");
//    MsgInfo msgInfo1 = normalSearch.updateFavorites(favarites01);
//    System.out.println(msgInfo1);


}

}
