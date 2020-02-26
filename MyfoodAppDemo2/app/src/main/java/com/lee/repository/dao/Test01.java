package com.lee.repository.dao;

public class Test01 {
    private String name;//名称
    private String introduce;//介绍

    public Test01(String name, String introduce) {
        this.name = name;
        this.introduce = introduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "Test01{" +
                "name='" + name + '\'' +
                ", introduce='" + introduce + '\'' +
                '}';
    }
}
