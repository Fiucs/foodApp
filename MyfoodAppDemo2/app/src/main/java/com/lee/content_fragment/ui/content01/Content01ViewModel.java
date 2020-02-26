package com.lee.content_fragment.ui.content01;

import com.lee.content_fragment.ui.content01.view.CommentExpandableListView;
import com.lee.myadapter.CommentExpandAdapter;
import com.lee.repository.javabean.CommentBean;
import com.lee.repository.javabean.CommentDetailBean;
import com.lee.repository.network.CommentsRepository;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class Content01ViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    CommentsRepository.GetComments getComments;
    CommentsRepository.UpdateCmRply updateCmRply;
    int currentPage=1;
    int totalPages=1;

    public Content01ViewModel() {
        //初始化
         getComments = new CommentsRepository().new GetComments();
         updateCmRply=new CommentsRepository().new UpdateCmRply();

    }

    public CommentsRepository.UpdateCmRply getUpdateCmRply() {
        updateCmRply=new CommentsRepository().new UpdateCmRply();
        return updateCmRply;
    }

    public CommentsRepository.GetComments getGetComments() {
        getComments=new CommentsRepository().new GetComments();
        return getComments;
    }
    public void getCommentDetailBeanList(String foodId, int currentPage, CommentExpandAdapter adapter, CommentExpandableListView expandableListView) {

         getComments = getGetComments();
            getComments.execute(foodId, String.valueOf(currentPage));

        this.getComments.setGetCommentsCallBack(new CommentsRepository.CommentsCallBack() {
            @Override
            public void getCommentsCallBack(CommentBean commentBean) {
                //此处获得数据进行回调
                List<CommentDetailBean> list = commentBean.getData().getList();
                if (list.size()>0)
                    for (CommentDetailBean commentDetailBean : list) {
                        adapter.addTheCommentData(commentDetailBean,expandableListView);//加载评论
                    }
            }
        });

    }


}
