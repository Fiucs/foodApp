package com.foodweb.service.imp;

import com.foodweb.javabean.Rows;
import com.foodweb.javabean.review.CommentBean;
import com.foodweb.javabean.review.CommentDetailBean;
import com.foodweb.javabean.review.ReplyDetailBean;
import com.foodweb.mapper.FoodsMapper;
import com.foodweb.pojo.*;
import com.foodweb.service.NormalSearch;
import com.github.binarywang.java.emoji.EmojiConverter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.utils.FormatBean;
import com.utils.MethodStringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional
public class NormalSearchImp implements NormalSearch {

    @Autowired
    private FoodsMapper foodsMapper;
//整个应用
    private static ServletContext application = null;
    //初始化 application
    public static void initAppliation(ServletContext application01)
    {
        if(application==null)
        {
            application=application01;
        }
    }
//得到application
    public static ServletContext getApplication()
    {
        return application;
    }


    /**
     * 关键字查询
     * @param name
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @Override
    public Rows searchByName(String name, int currentPage, int pageSize, HttpServletRequest request) {

        PageHelper.startPage(currentPage,pageSize);

        System.out.println(name);
        Page<FoodsAll> res=(Page<FoodsAll>) foodsMapper.searchByName(name);
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        List<FoodsAll> foodsAllList = res.getResult();
        List<FoodsAll> result = FormatBean.forMatListBean(request, url, uri, foodsAllList);


        return result==null?new Rows():new Rows(res.getTotal(),res.getPageNum(),res.getPages(),result);
    }

    /**
     * 不查询 方法相关的
     * @param nameKey
     * @param index
     * @param request
     * @return
     */
    @Override
    public Rows searchByclass(String nameKey,String smallClass,Integer currentPage,Integer pageSize ,int index,HttpServletRequest request) {

        currentPage=currentPage==null?1:currentPage;
        pageSize=pageSize==null?10:pageSize;
        PageHelper.startPage(currentPage,pageSize);

        Page<FoodsAll> res=(Page<FoodsAll>) foodsMapper.searchByclass(nameKey,smallClass,1);

        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();

        List<FoodsAll> result1 = res.getResult();

        System.out.println(result1);

        List<FoodsAll> result = FormatBean.forMatListBean(request, url, uri, result1);

//        String user_id =(String) request.getSession().getAttribute("user_id");
//        System.out.println("user_id："+user_id);

        return result==null?new Rows():new Rows(res.getTotal(),res.getPageNum(),res.getPages(),result);
    }

    /**
     * 按照foodid查找菜谱 数据
     * 即显示一个foodBean
     * @param foodId

     * @param request
     * @return
     */
    @Override
    public FoodsAll searchByFoodId(String foodId, HttpServletRequest request) {

        FoodsAll food = foodsMapper.searchByFoodId(foodId);
        if (food==null)
            return new FoodsAll();
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        FoodsAll forMatOnetBean = FormatBean.forMatOnetBean(request, url, uri, food);

        return forMatOnetBean;
    }

    /**
     * 测试 类型的数据
     * @param nameKey
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public List<FoodsAll> searchByclass01(String nameKey, Integer currentPage, Integer pageSize) {

        PageHelper.startPage(currentPage,pageSize);

        Page<FoodsAll> res=(Page<FoodsAll>)foodsMapper.searchByclass(nameKey, "",1);//是否分类
        System.out.println(res.getResult());
        System.out.println(res.getTotal());
        return res.getResult();
    }


    /**
     * 查询所有 fid-ingreidents
     */
    @Override
    public List<FoodsAll> searchAllfidIngredients()
    {

        return foodsMapper.searchAllfidIngredients();
    }


    /**
     * 插入数据
     */


    @Override
    public int insertIntoRecommend(Recommend recommend) {

        int i=foodsMapper.insertIntoRecommend(recommend);

        return i;


    }

    /**
     * 更新 user_record的 被计算的status 0 未用于计算  1 用于了计算
     * @param id
     * @return
     */
    @Override
    public int updateUserRecordStatus(int id) {
        int i = foodsMapper.updateUserRecordStatus(id);
        return i;
    }

    /**
     * 添加访问记录到 user_receords
     *
     */

    @Override
    public int insertIntoUserRecords(String foodId, String userId) {

        //根据food_id查找分类
        FoodsAll bean = foodsMapper.searchPartBeanByFoodId(foodId);
        UserRecord userRecord = new UserRecord();
        userRecord.setFood_id(foodId);
        userRecord.setUser_id(userId);
        userRecord.setFood_bigclass(bean.getBigclass());
        userRecord.setFood_smallclass(bean.getSmallclass());
        userRecord.setDerive_time(new Date());
        userRecord.setStatus("0");
        int i = foodsMapper.addUserRecords(userRecord);
        System.out.println(i);
        return i;
    }

    /**
     * 注册
     * @param userName
     * @param userPassword
     * @return
     */
    @Override
    public MsgInfo register(String userName, String userPassword) {


        User user = foodsMapper.findUserByUserName(userName);

        if(user==null)
        {
            //可以注册

            //获得用户唯一标识
            String uuid = UUID.randomUUID().toString().trim().replace("-","");

            int i = foodsMapper.insertIntoUser(uuid, userName, userPassword);
            return i>0?new MsgInfo(true,uuid):new MsgInfo(false,"注册发生错误");

        }
        else
        {
            return new MsgInfo(false,"用户名已被使用");
            //用户名被占用
        }



    }

    /**
     * 登录
     * @param userName
     * @param userPassword
     * @return
     */
    @Override
    public MsgInfo login(String userName, String userPassword) {


        User user = foodsMapper.findUserByUserName(userName);

        if(user!=null)
        {

            if(user.getUser_name().equals(userName) && user.getUser_password().equals(userPassword))
            {
//                登录成功
                return new MsgInfo(true,user.getUser_id());
            }

        }
        return new MsgInfo(false,"用户名或密码错误");

    }

    /**
     * 查询所有分类信息
     */

    @Override
    public Rows findAllFoodClassInfo(HttpServletRequest request)
    {
        List<FoodClass> foodClassList = foodsMapper.findAllFoodClassInfo();

        List<FoodClass> newList=new ArrayList<>();//保存 gridLayout的排列结果

//重新排列
        for (int i = 0; i < foodClassList.size(); i++) {

            if(i==0 || i==17 || i==27 || i==32 ||i==44 || i== 58 || i==68 || i==70) {
                FoodClass foodClass = new FoodClass();
                foodClass.setBigclass(foodClassList.get(i).getBigclass());
                foodClass.setId(i+100);
                newList.add(foodClass);//添加分类
                newList.add(foodClassList.get(i));//
            }
            else
            {
                newList.add(foodClassList.get(i));//正常添加
            }


        }

        if(request!=null)
        {
            String uri = request.getRequestURI();
            String url = request.getRequestURL().toString();
            for (FoodClass aClass : newList) {
                FormatBean.formatFoodClassBean(aClass,url,uri);
            }

        }
        System.out.println(newList);

        return new Rows(newList.size(),1,1,newList);

    }



    /**
     * 根据 uuid 查询用户的推荐结果
     * @param uuid
     * @return
     */
    @Override
    public List<FoodsAll> searchFromRecommendTb(String uuid,int pageSize,HttpServletRequest request)
    {

        //从recommend表中获得 推荐的uuid集合
        List<Recommend> recommendByUUid = foodsMapper.findRecommendByUUid(uuid);
        //下标集合 防止重复
        List<Integer> listSub=new ArrayList<>();
        //保存结果
        List<FoodsAll> foodsAllList=new ArrayList<>();
        int sizeRecommend=2;//系统推荐的值

        int count=pageSize<=sizeRecommend?pageSize:sizeRecommend;//注意pageSize应当小于recommendByUUid.size()

        while (count>0)
        {
            int sub =(int) (Math.random() * (recommendByUUid.size()));

            if(!listSub.contains(sub))
            {
                listSub.add(sub);//加入 下标集合防重复
                FoodsAll bean = foodsMapper.searchPartBeanByFoodId(recommendByUUid.get(sub).getFood_id());

                if(request!=null)
                {
                    String uri = request.getRequestURI();
                    String url = request.getRequestURL().toString();
                    bean = FormatBean.forMatOnetBean(request, url, uri, bean);//转换 图片网络地址
                }


                foodsAllList.add(bean);//计入list 结果集

                System.out.println(sub);
                count--;
            }

        }

        if(pageSize>sizeRecommend)
        {
            List<FoodsAll> randomResult = getRandomResult(pageSize - sizeRecommend, request);
            foodsAllList.addAll(randomResult);
        }
        System.out.println("----用户推荐----------------------");
        System.out.println(foodsAllList.size());
        return foodsAllList;
    }

    /**
     * 随便看看
     */
    public List<FoodsAll> getRandomResult(int pageSize,HttpServletRequest request)
    {


        //总记录数
        int size = foodsMapper.seachCountOfFoods();
        int count=pageSize;//获得的条数
        //下标集合 防止重复
        List<Integer> listSub=new ArrayList<>();
        //保存结果
        List<FoodsAll> foodsAllList=new ArrayList<>();
        while (count>0)
        {

            int sub=(int)(Math.random()*size);
            if(!listSub.contains(sub))
            {
                listSub.add(sub);
                FoodsAll bean = foodsMapper.searchPartBeanById(sub);

                if(request!=null)
                {
                    String uri = request.getRequestURI();
                    String url = request.getRequestURL().toString();
                    bean = FormatBean.forMatOnetBean(request, url, uri, bean);//转换 图片网络地址
                }

                foodsAllList.add(bean);//计入list 结果集


                count--;
            }


        }
        System.out.println("---------随便看看----------------------------");
        System.out.println(foodsAllList);

        return  foodsAllList;

    }

    //查找所有
    public int getTotal()
    {
        return foodsMapper.seachCountOfFoods();
    }


    //根据 foodID


    @Override
    public CommentBean.Data getCommentsByFoodId(HttpServletRequest request, String foodId, Integer currentPage, Integer pageSize) {

        PageHelper.startPage(currentPage,pageSize);


        Page<CommentDetailBean> page=(Page<CommentDetailBean>) foodsMapper.searchCommentBeanByFoodId(foodId);
        List<CommentDetailBean> result = page.getResult();

        for (CommentDetailBean bean : result) {
            bean.setContent(transferEmjoyCode(bean.getContent()));
            List<ReplyDetailBean> replyDetailBeans = foodsMapper.searchReplyByCommentId(bean.getReplyId());

            for (ReplyDetailBean detailBean : replyDetailBeans) {
                detailBean.setContent(transferEmjoyCode(detailBean.getContent()));
            }
            bean.setReplyList(replyDetailBeans);
            bean.setReplyTotal(replyDetailBeans.size());

        }

        CommentBean.Data data = new CommentBean().new Data();
        data.setCurrentPage(currentPage);
        data.setTotal((int) page.getTotal());
        data.setList(result);
        data.setTotalpages(page.getPages());
        return data;
    }

    //解码 Emjoy 成 unicode
    public String transferEmjoyCode(String s)
    {
        EmojiConverter instance = EmojiConverter.getInstance();

        return instance.toUnicode(s);
    }

    //编码 Emjoy 成 Alias 存入数据库
    public String encodeEmoji(String s)
    {
        EmojiConverter instance = EmojiConverter.getInstance();
        return instance.toAlias(s);
    }


    //更新评论列表


    @Override
    public MsgInfo updateComments(HttpServletRequest request, CommentDetailBean commentDetailBean) {

        commentDetailBean.setContent(encodeEmoji(commentDetailBean.getContent()));
        commentDetailBean.setCreateDate(new Date());
        int i = foodsMapper.insertIntoCommentBean(commentDetailBean);
        int id = commentDetailBean.getId();

        int i1 = foodsMapper.updateReplyId(id);
        MsgInfo msgInfo=i1==0?new MsgInfo(false,"评论失败"):new MsgInfo(true,"评论成功");

        return msgInfo;
    }

    //更新回复


    @Override
    public MsgInfo updateReplys(HttpServletRequest request, ReplyDetailBean replyDetailBean) {
        replyDetailBean.setContent(encodeEmoji(replyDetailBean.getContent()));
        replyDetailBean.setCreateDate(new Date());
        int i = foodsMapper.inserIntoReply(replyDetailBean);
        MsgInfo msgInfo=i==0?new MsgInfo(false,"回复失败"):new MsgInfo(true,"回复成功");

        return msgInfo;
    }


    //添加收藏 根据 user_id foodid Favarites
    @Override
    public MsgInfo updateFavorites(Favarites favarites)
    {
        int flag=1;//代表初始为添加
        MsgInfo msgInfo =null;
        User user = foodsMapper.findUserByUserName(favarites.getUser_id());//获得用户实体
        favarites.setUser_id(user.getUser_id());
        if (favarites.getFavorite().equals("1"))//添加新的或重新收藏
        {
            //添加收藏
            //获得以前的收藏数据
            List<Favarites> oldRes = foodsMapper.searchFavoriteByUerId(favarites.getUser_id());
            for (Favarites oldRe : oldRes) {

                if (oldRe.getFood_id().equals(favarites.getFood_id()))
                {
                    System.out.println("修改搜藏水水水水水水");
                    //代表原来有收藏
                    //只需修改即可
                    favarites.setFavorite("1");
                    favarites.setDerive_time(new Date());
                    int i = foodsMapper.changeFavoriteByUserId(favarites);//修改状态为 收藏
                    flag=0;//修改了
                    msgInfo=i==0?new MsgInfo(false,"重新收藏失败"):new MsgInfo(true,"重新收藏成功");

                }
            }

            if (flag==1)
            {
                //添加
                System.out.println("添加成滚滚滚滚滚滚");
                favarites.setDerive_time(new Date());
                favarites.setFavorite("1");
                int i = foodsMapper.insertIntoFavorite(favarites);
                msgInfo=i==0?new MsgInfo(false,"收藏失败"):new MsgInfo(true,"收藏成功");
            }


        }else
        {
            //取消收藏 根据 food_id,user_id Favarites
            //取消收藏
            favarites.setFavorite("0");
            favarites.setDerive_time(null);
            int i = foodsMapper.changeFavoriteByUserId(favarites);
            msgInfo=i==0?new MsgInfo(false,"取消收藏失败"):new MsgInfo(true,"取消收藏成功");

        }

        return msgInfo;

    }


    //查询收藏 根据 user_id
    @Override
    public Rows searchFavorites(String user_id,Integer currentPage, Integer pageSize,HttpServletRequest request)
    {
        User user = foodsMapper.findUserByUserName(user_id);//获得用户实体

        PageHelper.startPage(currentPage,pageSize);

        Page<Favarites> favarites =(Page<Favarites>) foodsMapper.searchFavoriteByUerId(user.getUser_id());//获得收藏记录
        List<Favarites> favaritesResult = favarites.getResult();
        ArrayList<FoodsAll> foodsAlls = new ArrayList<>();

        for (Favarites favarites1 : favaritesResult) {
            if (favarites1.getFavorite().equals("1"))
            {
                FoodsAll bean = foodsMapper.searchPartBeanByFoodId(favarites1.getFood_id());
                bean.setDerive_time(favarites1.getDerive_time());
                foodsAlls.add(bean);//添加到bean
            }

        }
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();

        int total = foodsMapper.countFavoriteByUerId(user.getUser_id());//获得收藏数
        List<FoodsAll> result = FormatBean.forMatListBean(request, url, uri, foodsAlls);
        System.out.println(result);
        return new Rows(total,currentPage,favarites.getPages(),result);

    }

    //查询收藏单体


    @Override
    public MsgInfo findCollectionStatus(Favarites favarites) {
        MsgInfo msgInfo=null;

        User user = foodsMapper.findUserByUserName(favarites.getUser_id());
        favarites.setUser_id(user.getUser_id());

        List<Favarites> favaritesList = foodsMapper.findCollectionStatus(favarites);
        if (favaritesList.size()>0)
        {
            //代表已经被收藏
            msgInfo=new MsgInfo(true,"已被收藏");
        }else
            msgInfo=new MsgInfo(false,"未被收藏");

        return msgInfo;
    }

    //查询用户访问的历史记录
    @Override
    public Rows searchHistory(String user_id,Integer currentPage, Integer pageSize,HttpServletRequest request)
    {
        User user = foodsMapper.findUserByUserName(user_id);//获得用户实体
        PageHelper.startPage(currentPage,pageSize);
        Page<UserRecord> userRecords =(Page<UserRecord>) foodsMapper.getUserReoords02(user.getUser_id());//获得收藏记录
        List<UserRecord> recordsResult = userRecords.getResult();

        ArrayList<FoodsAll> foodsAlls = new ArrayList<>();

        for (UserRecord userRecord : recordsResult) {
            FoodsAll bean = foodsMapper.searchPartBeanByFoodId(userRecord.getFood_id());
            bean.setDerive_time(userRecord.getDerive_time());
            foodsAlls.add(bean);//添加到bean
        }

        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();


        List<FoodsAll> result = FormatBean.forMatListBean(request, url, uri, foodsAlls);
        System.out.println(result);
        return new Rows((int)userRecords.getTotal(),currentPage,userRecords.getPages(),result);


    }







}
