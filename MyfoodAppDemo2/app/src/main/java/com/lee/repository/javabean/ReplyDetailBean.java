package com.lee.repository.javabean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by moos on 2018/4/20.
 */

public class ReplyDetailBean implements Serializable {
    private String nickName;//用户名
    private String userLogo;//用户图片
    private int id;//索引
    private int commentId;//评论ID 与 comment中的replyID对应1对多
    private String content;//评论内容
    private String status;//回复 状态
    private Date createDate;//创建日期

    public ReplyDetailBean() {
    }

    public ReplyDetailBean(String nickName, String content) {
        this.nickName = nickName;
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ReplyDetailBean{" +
                "nickName='" + nickName + '\'' +
                ", userLogo='" + userLogo + '\'' +
                ", id=" + id +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
