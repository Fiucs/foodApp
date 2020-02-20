package com.foodweb.service;

import com.foodweb.javabean.Rows;
import com.foodweb.javabean.review.CommentBean;
import com.foodweb.javabean.review.CommentDetailBean;
import com.foodweb.javabean.review.ReplyDetailBean;
import com.foodweb.pojo.*;
import org.apache.ibatis.annotations.Param;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NormalSearch {





    //模糊查询
    public Rows searchByName(String name, int currentPage, int pageSize, HttpServletRequest request);
    //
    /**
     * 查询 id,foodid,foodname,foodpic,bigclass,smallclass
     *
     */

    public Rows searchByclass(String nameKey,String smallClass,Integer currentPage, Integer pageSize,int index,HttpServletRequest request);

    public FoodsAll searchByFoodId(String foodId, HttpServletRequest request);


    public List<FoodsAll> searchByclass01(String nameKey, Integer currentPage, Integer pageSize);
    /**
     * 查询所有 fid-ingreidents
     */

    public List<FoodsAll> searchAllfidIngredients();

    /**
     * 插入数据到推荐表
     */
    public  int  insertIntoRecommend(Recommend recommend);


    /**
     * 更新 user-record表
     */
    public int updateUserRecordStatus(int id);

    /**
     * 添加历史记录到user_records表
     */
    public int insertIntoUserRecords(String foodId,String userId);

    /**
     * 注册
     * @param userName
     * @param userPassword
     * @return
     */
    public MsgInfo register(String userName,String userPassword);

    /**
     * 登录
     */
    public MsgInfo login(String userName, String userPassword);

    /**
     * 查询所有 分类
     * @param request
     * @return
     */
    public Rows findAllFoodClassInfo(HttpServletRequest request);

    /**
     * 查询foodbean
     * 从 recommend表中
     * @param uuid
     * @param pageSize
     * @return
     */
    public List<FoodsAll> searchFromRecommendTb(String uuid,int pageSize,HttpServletRequest request);

    /**
     * 随便看看
     * @param pageSize
     * @param request
     * @return
     */
    public List<FoodsAll> getRandomResult(int pageSize,HttpServletRequest request);


    public int getTotal();

    /**
     * 根据 foodid currentPage 获取评论
     */

    public CommentBean.Data  getCommentsByFoodId(HttpServletRequest request, String foodId, Integer currentPage, Integer pageSize);
    /**
     * 更新评论
     */

    public MsgInfo updateComments(HttpServletRequest request,CommentDetailBean commentDetailBean);

    /**
     * 更新回复
     */
    public MsgInfo updateReplys(HttpServletRequest request, ReplyDetailBean replyDetailBean);

    //查询历史
    public Rows searchHistory(String user_id,Integer currentPage, Integer pageSize,HttpServletRequest request);

    //查询收藏 根据 user_id

    public Rows searchFavorites(String user_id,Integer currentPage, Integer pageSize,HttpServletRequest request);

    //查询收藏单体

    public MsgInfo findCollectionStatus(Favarites favarites);


    //添加收藏 根据 user_id foodid Favarites

    public MsgInfo updateFavorites(Favarites favarites);



}
