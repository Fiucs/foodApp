package com.foodweb.mapper;

import com.foodweb.javabean.review.CommentDetailBean;
import com.foodweb.javabean.review.ReplyDetailBean;
import com.foodweb.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FoodsMapper {
    /**
     * 模糊查询
     * @param nameLike
     * @return
     */
    public List<FoodsAll> searchByName(String nameLike);

    /**
     * 查询 id,foodid,foodname,foodpic,bigclass,smallclass
     *
     */

    /**
     * 通过食谱关键字查询
     * @param nameKey
     * @param smallclass
     * @param index
     * @return
     */
    public List<FoodsAll> searchByclass(@Param("nameKey") String nameKey,@Param("smallclass") String smallclass,@Param("index") int index);

    /**
     * 通过食谱 id查询
     * @param foodId
     * @return
     */
    public FoodsAll searchByFoodId(@Param("foodId") String foodId);


    /**
     * 查询部分 foodBean
     * 根据foodid
     */

    public FoodsAll searchPartBeanByFoodId(@Param("foodId") String foodId);

    /**
     * 查询部分 foodBean
     * 根据id
     */

    public FoodsAll searchPartBeanById(@Param("id") int id);


    /**
     * 计算总数
     * @return
     */
    public int seachCountOfFoods();



    /**
     * 通过用户id
     * 查询user_record 用户浏览记录表
     *
     */


    public List<UserRecord> getUserReoords(@Param("user_id") String user_id);


    /**
     * 将已经登录过的用户的历史记添加到历史记录
     *
     */

    public int addUserRecords(UserRecord userRecord);



    /**
     * 更新user_reocrd中被使用过的记录
     */
    public int updateUserRecordStatus(@Param("id")int id);



    /**
     * 查询所有 fid-ingreidents
     */

    public List<FoodsAll> searchAllfidIngredients();


    /**
     * 生成推荐数据
     */
    public  int  insertIntoRecommend( Recommend recommend);

    /**
     * 根据uuid查询生成的数据
     */
    public List<Recommend> findRecommendByUUid(@Param("user_id") String user_id);


    /**
     * 根据用户名查询 用户名不可重复
     */
    public User findUserByUserName(@Param("user_name") String username);


    /**
     * 插入 用户名 密码
     */

    public int insertIntoUser(@Param("user_id")String user_id,@Param("user_name") String user_name,@Param("user_password")String user_password);


    /**
     * 查询所有分类信息
     */

    public List<FoodClass> findAllFoodClassInfo();


    /**
     * 插入 一条回复评论的信息
     *
     */
    public int inserIntoReply(ReplyDetailBean replyBean);

    /**
     * 通过commentID 查找所有replyBean
     *
     */
    public List<ReplyDetailBean> searchReplyByCommentId(int commentId);

    /**
     * 插入 一条评论信息
     */
    public int insertIntoCommentBean(CommentDetailBean commentDetailBean);

    /**
     * 根据 foodId取出评论
     */
    public List<CommentDetailBean> searchCommentBeanByFoodId(String foodid);

    /**
     * 更新评论 commentId
     */

    public int updateReplyId(int id);


    /**
     * 添加收藏
     */

    public int insertIntoFavorite(Favarites favarites);
    /**
     * 查询收藏列表
     */

    public List<Favarites> searchFavoriteByUerId(String user_id);

    /**
     * 收藏的总数
     * @param user_id
     * @return
     */
    public int countFavoriteByUerId(String user_id);

    /**
     * 查询单体状态
     */
    public List<Favarites> findCollectionStatus(Favarites favarites);



    /**
     * 取消收藏 /添加收藏
     */

    public int changeFavoriteByUserId(Favarites favarites);


    /**
     * 查询 历史记录
     */

    public List<UserRecord> getUserReoords02(String user_id);
}
