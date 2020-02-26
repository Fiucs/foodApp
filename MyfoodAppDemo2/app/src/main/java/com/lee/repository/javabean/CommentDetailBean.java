package com.lee.repository.javabean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by moos on 2018/4/20.
 */

public class CommentDetailBean implements Serializable {
    private int id;//索引
    private String nickName;//名字
    private String userLogo;//头像地址
    private String content;//评论内容
    private String imgId;//图片Id
    private String foodid;//菜谱唯一id
    private int replyTotal;//回复评论的总数
    private Date createDate;//评论创建日期
    private int replyId;//评论的ID 注意此replyID为评论索引 因为
//    commentID--------replyID  是一对多关系
     private List<ReplyDetailBean> replyList;//回复 具体内容


    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public CommentDetailBean() {
    }

    public CommentDetailBean(String nickName, String content, Date createDate) {
        this.nickName = nickName;
        this.content = content;
        this.createDate = createDate;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
    public String getUserLogo() {
        return userLogo;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getImgId() {
        return imgId;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }
    public int getReplyTotal() {
        return replyTotal;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setReplyList(List<ReplyDetailBean> replyList) {
        this.replyList = replyList;
    }

    @Override
    public String toString() {
        return "CommentDetailBean{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", userLogo='" + userLogo + '\'' +
                ", content='" + content + '\'' +
                ", imgId='" + imgId + '\'' +
                ", foodid='" + foodid + '\'' +
                ", replyTotal=" + replyTotal +
                ", createDate='" + createDate + '\'' +
                ", replyId=" + replyId +
                ", replyList=" + replyList +
                '}';
    }

    public List<ReplyDetailBean> getReplyList() {

        return replyList;
    }
}
