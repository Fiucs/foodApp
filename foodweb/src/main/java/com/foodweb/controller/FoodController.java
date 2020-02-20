package com.foodweb.controller;

import com.foodweb.javabean.Rows;
import com.foodweb.javabean.review.CommentBean;
import com.foodweb.javabean.review.CommentDetailBean;
import com.foodweb.javabean.review.ReplyDetailBean;
import com.foodweb.pojo.Favarites;
import com.foodweb.pojo.FoodsAll;
import com.foodweb.pojo.MsgInfo;
import com.foodweb.service.NormalSearch;


import com.github.binarywang.java.emoji.EmojiConverter;
import com.sun.rowset.internal.Row;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/app")
public class FoodController {
    @Autowired
    private NormalSearch normalSearch;
    @RequestMapping("/searchByName")
    @ResponseBody
    public Rows searchByname(String nameLike, Integer currentPage, Integer pageSize, HttpServletRequest request){

        if (nameLike==null || nameLike.length()==0 || currentPage==null || pageSize==null)
            return new Rows();
        String string="%"+nameLike+"%";
        Rows rows = normalSearch.searchByName(string, currentPage, pageSize,request);

        return rows;

    }

    /**
     * 根据 小分类查询
     * @param smallClass
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("/searchBySmallClass")
    public Rows searchBySmallClass(String smallClass,Integer currentPage,Integer pageSize, HttpServletRequest request)
    {
        Rows rows = normalSearch.searchByclass("",smallClass, currentPage, pageSize, 1, request);
        return rows;
    }

    /**
     * 获取 分类菜单
     * @param
     * @param
     * @param request
     * @return
     */
    @RequestMapping("/getFoodClassMenu")
    public Rows getFoodClassMenu(HttpServletRequest request)
    {
        Rows rows = normalSearch.findAllFoodClassInfo(request);
        return rows;
    }



    @RequestMapping("/searchBySystemKey")
    public Rows searchByNone(Integer currentPage,Integer pageSize, HttpServletRequest request)
    {
        //此处时用户推荐页面的 关键词 来自于系统计算值 （推荐值）
        //比如
        String key="面";
        String string="%"+key+"%";
        Rows rows = normalSearch.searchByclass(string,"",currentPage, pageSize,1,request);

        return rows;

    }

    @RequestMapping("/getOneFoodBean")
    public FoodsAll getOneFoodBean(String foodId,HttpServletRequest request)
    {

        String user_id=(String) request.getSession().getAttribute("user_id");

        System.out.println("getOneFoodBean:+"+user_id);
        if(user_id!=null && user_id.length()>0)
        {
//            添加到用户历史记录 User_id  ,foodId
            int i = normalSearch.insertIntoUserRecords(foodId, user_id);
            System.out.println("用户："+user_id + "记录添加状态:"+i);
        }


        return normalSearch.searchByFoodId(foodId,request);

    }

    /**
     * 系统推荐
     * 根据用户是否登录 session中是否有数据
     */

    @RequestMapping("/getRecommendList")
    public Rows getRecommendList(HttpServletRequest request,Integer pageSize,Integer currentPage)
    {

        HttpSession session = request.getSession();
        String user_id =(String) session.getAttribute("user_id");

        if(user_id!=null && user_id.length()>0)
        {
            System.out.println("推荐看看");
            //登陆了采用 推荐list
            List<FoodsAll> list = normalSearch.searchFromRecommendTb(user_id, pageSize, request);

            return new Rows(list.size(),currentPage,currentPage+1,list);

        }
        else
        {
            System.out.println("随便看看");
            //没登陆 则采用随便看看
            List<FoodsAll> randomResult = normalSearch.getRandomResult(pageSize, request);
            return new Rows(randomResult.size(),currentPage,currentPage+1,randomResult);
        }


    }

    @RequestMapping("/getRandomList")
    public Rows getRandomList(HttpServletRequest request ,Integer pageSize,Integer currentPage)
    {
        List<FoodsAll> randomResult = normalSearch.getRandomResult(pageSize, request);


        return new Rows(normalSearch.getTotal(),currentPage,100,randomResult);
    }

    /**
     * 根据foodid获得评论
     */
    @RequestMapping("/getCommentsByFoodId")
    public CommentBean getCommentsByFoodId(HttpServletRequest request, String foodId, Integer currentPage, Integer pageSize) {


        CommentBean commentBean = new CommentBean();
        CommentBean.Data commentsData = normalSearch.getCommentsByFoodId(request, foodId, currentPage, pageSize);
        commentBean.setData(commentsData);
        commentBean.setCode(1);
        commentBean.setMessage("ok");
        return commentBean;

    }

    @RequestMapping("/updateReplys")
    public MsgInfo updateComments (HttpServletRequest request ,ReplyDetailBean replyDetailBean)
    {

        MsgInfo msgInfo = normalSearch.updateReplys(request, replyDetailBean);
        return msgInfo;
    }

    @RequestMapping("/updateComments")
    public MsgInfo updateComments(HttpServletRequest request, CommentDetailBean commentDetailBean)
    {
        MsgInfo msgInfo = normalSearch.updateComments(request, commentDetailBean);
        return msgInfo;
    }


    /**
     * 添加/修改收藏
     */
    @RequestMapping("/updateFavorites")
    public MsgInfo updateFavorites(Favarites favarite)
    {

        MsgInfo msgInfo = normalSearch.updateFavorites(favarite);
        return msgInfo;

    }

    /**
     * 查询收藏列表
     */
    @RequestMapping("/searchFavoritesByUserName")
    public Rows searchFavoritesByUserId(HttpServletRequest request,Integer pageSize,Integer currentPage,String userName)
    {
        currentPage=currentPage==null||currentPage<=0?1:currentPage;
        pageSize=pageSize==null||pageSize<=0?3:pageSize;
        Rows rows = normalSearch.searchFavorites(userName, currentPage, pageSize,request);
        return rows;
    }
/**
 * 查询收藏单体
 */

    @RequestMapping("/findCollectionStatus")
    public MsgInfo findCollectionStatus(HttpServletRequest request,Favarites favarites)
    {
        MsgInfo collectionStatus = normalSearch.findCollectionStatus(favarites);
        return collectionStatus;
    }




    /**
     * 查询历史记录
     */
    @RequestMapping("/searchHistoryByUserName")
    public Rows searchHistoryByUserName(HttpServletRequest request,Integer pageSize,Integer currentPage,String userName)
    {
        currentPage=currentPage==null||currentPage<=0?1:currentPage;
        pageSize=pageSize==null||pageSize<=0?3:pageSize;
        Rows rows = normalSearch.searchHistory(userName, currentPage, pageSize,request);
        return rows;
    }



}
