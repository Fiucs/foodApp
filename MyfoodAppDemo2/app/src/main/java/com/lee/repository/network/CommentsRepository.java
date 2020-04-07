package com.lee.repository.network;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.repository.javabean.CommentBean;
import com.lee.repository.javabean.CommentDetailBean;
import com.lee.repository.javabean.MsgInfo;
import com.lee.repository.javabean.ReplyDetailBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import okhttp3.Response;

public class CommentsRepository {

    private static int currentPage = 1;//当前页码
    private static int pageSize = 3;//当前页大小
    CommentBean commentBean;
    GetComments getComments;
//    private String baseUrl="http://192.168.1.7:8081";
    private String baseUrl="";


    public CommentsRepository() {
        baseUrl=BaseDataModel.getBaseUrl();
        pageSize = 3;
    }

    public interface CommentsCallBack {
        public void getCommentsCallBack(CommentBean commentBean);
    }

    public class GetComments extends AsyncTask<String, Void, CommentBean> {
        CommentsCallBack commentsCallBack;

        public void setGetCommentsCallBack(CommentsCallBack getCommentsCallBack) {
            this.commentsCallBack = getCommentsCallBack;
        }

        @Override
        protected CommentBean doInBackground(String... args) {

            CommentBean commentBean = okhttpGetComments(args[0], args[1]);
            return commentBean;
        }

        @Override
        protected void onPostExecute(CommentBean commentBean) {
            super.onPostExecute(commentBean);
            commentsCallBack.getCommentsCallBack(commentBean);//回调

        }
    }

    //添加回复 评论

    public interface UpDateReultCallBack {
        public void upDateResult(MsgInfo msgInfo);
    }
    public class UpdateCmRply extends AsyncTask<Object,Void,MsgInfo>
    {
        UpDateReultCallBack upDateReultCallBack;

        public void setUpDateReultCallBack(UpDateReultCallBack upDateReultCallBack) {
            this.upDateReultCallBack = upDateReultCallBack;
        }

        @Override
        protected MsgInfo doInBackground(Object... objects) {

            MsgInfo msgInfo=null;
            if (objects[0] instanceof CommentDetailBean)
            {
                //是评论
                CommentDetailBean commentDetailBean=(CommentDetailBean)objects[0];
                 msgInfo = okhttpUpdateViews(commentDetailBean, null);
            }
            else
            {
                ReplyDetailBean replyDetailBean = (ReplyDetailBean) objects[1];
                 msgInfo=okhttpUpdateViews(null,replyDetailBean);
            }
            return msgInfo;
        }

        @Override
        protected void onPostExecute(MsgInfo msgInfo) {
            super.onPostExecute(msgInfo);
            upDateReultCallBack.upDateResult(msgInfo);

        }
    }




    public MsgInfo okhttpUpdateViews(CommentDetailBean commentDetailBean, ReplyDetailBean replyDetailBean)
    {
        MsgInfo msgInfo;
        if (commentDetailBean!=null)
        {
            //是评论
            System.out.println("正在添加评论");
            String content = commentDetailBean.getContent();
            String foodid = commentDetailBean.getFoodid();
            String nickName = commentDetailBean.getNickName();
            String URL=baseUrl+"/app/updateComments?nickName="+nickName+"&foodid="+foodid+"&content="+content;
            msgInfo=updateCmtRply(URL);

        }
        else
        {
//            是回复
            System.out.println("正在添加回复");
            int commentId = replyDetailBean.getCommentId();
            String content = replyDetailBean.getContent();
            String nickName = replyDetailBean.getNickName();
            String URL=baseUrl+"/app/updateReplys?nickName="+nickName+"&commentId="+commentId+"&content="+content;
            msgInfo=updateCmtRply(URL);
        }

        return msgInfo;
    }


    public MsgInfo updateCmtRply(String url)
    {

        Response response;
        try {
            response = OkHttpUtils
                    .post()
                    .url(url)
                    .addHeader("connection", "close")
                    .build()
                    .connTimeOut(3000)
                    .readTimeOut(3000)
                    .execute();
            String res = response.body().string();
            Gson gson = new Gson();
            MsgInfo msgInfo = gson.fromJson(res, MsgInfo.class);

            System.out.println("添加评论处的结果");
            System.out.println(msgInfo);
            BaseDataModel.setIsNetWorkOk(true);//设置为联网网 状态

            return msgInfo;
        } catch (IOException e) {

            BaseDataModel.setIsNetWorkOk(false);//设置为断网 状态
            e.printStackTrace();
        }
            return null;
    }





    public CommentBean okhttpGetComments(String foodId, String currentPage) {

        System.out.println("初始化评论执行了");
        String URL = baseUrl+"/app/getCommentsByFoodId";
        Response response;
        try {
            response = OkHttpUtils
                    .post()
                    .url(URL)
                    .addHeader("connection", "close")
                    .addParams("currentPage", String.valueOf(currentPage))
                    .addParams("pageSize", String.valueOf(pageSize))
                    .addParams("foodId", "11111")//此处 应该调用 foodId  ps:此处为了方便
//                    .addParams("foodId", foodId)
                    .build()
                    .connTimeOut(3000)
                    .readTimeOut(3000)
                    .execute();
            String res = response.body().string();
            System.out.println(res);
            ObjectMapper mapper = new ObjectMapper();
//            Gson gson = new Gson();
//            CommentBean commentBean = gson.fromJson(res, CommentBean.class);
             commentBean = mapper.readValue(res, CommentBean.class);
//            List<CommentDetailBean> commentList = commentBean.getData().getList();
            System.out.println("------------------------------------------------");
            System.out.println(commentBean);
            BaseDataModel.setIsNetWorkOk(true);//设置为联网网 状态

            return commentBean;
        } catch (IOException e) {

            BaseDataModel.setIsNetWorkOk(false);//设置为断网 状态
            e.printStackTrace();
        }

        return null;
    }

}
