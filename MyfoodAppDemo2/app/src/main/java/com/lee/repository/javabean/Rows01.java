package com.lee.repository.javabean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Rows01 implements Serializable {
    private long total;

    private int currentPage;
    private int totalpages;
    private List<FoodsClass> foods;
    private Date driveTime;//时间戳

    public Rows01() {
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

    public List<FoodsClass> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodsClass> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "Rows01{" +
                "total=" + total +
                ", currentPage=" + currentPage +
                ", totalpages=" + totalpages +
                ", foods=" + foods +
                '}';
    }
}
