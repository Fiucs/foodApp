package com.lee.repository.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by moos on 2018/4/20.
 */

public class CommentBean implements Serializable {
    private int code;
    private String message;
    private Data data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public class Data {

        private int total;//所有评论数
        private int currentPage;//当前页码
        private int totalpages;//所有的页数
        private List<CommentDetailBean> list;//从网络获取  currentPageSize,pageSize
        
        public void setTotal(int total) {
            this.total = total;
        }
        public int getTotal() {
            return total;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalpages() {
            return totalpages;
        }

        public void setTotalpages(int totalpages) {
            this.totalpages = totalpages;
        }

        public void setList(List<CommentDetailBean> list) {
            this.list = list;
        }
        public List<CommentDetailBean> getList() {
            return list;
        }

    }

}
