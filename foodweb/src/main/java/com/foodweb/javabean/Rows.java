package com.foodweb.javabean;

import com.foodweb.pojo.FoodsAll;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Rows implements Serializable {
    private long total;

    private int currentPage;
    private int totalpages;
    private List foods;
    private Date driveTime;


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Rows(long total, int currentPage,int totalpages, List foods) {
        this.total = total;
        this.foods = foods;
        this.currentPage = currentPage;
        this.totalpages=totalpages;
    }

    public Rows() {
    }


    public Date getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(Date driveTime) {
        this.driveTime = driveTime;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getFoods() {
        return foods;
    }

    public void setFoods(List foods) {
        this.foods = foods;
    }

    public int getTotalpages() {
        return totalpages;
    }

    public void setTotalpages(int totalpages) {
        this.totalpages = totalpages;
    }

    @Override
    public String toString() {
        return "Rows{" +
                "total=" + total +
                ", foods=" + foods +
                '}';
    }
}
