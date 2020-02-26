package com.lee.repository.demo;


import android.content.Intent;
import android.graphics.DashPathEffect;

import com.lee.repository.dao.Test01;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class TestData {
    private LiveData<PagedList<Test01>> dataList;

//    public DataSource.Factory<Integer, Test01> getDataList() {
//        Test01 test01 = new Test01("xxxd", "欢天喜地");
//        Test01 test02 = new Test01("小弟弟", "天翻地第");
//        Test01 test03 = new Test01("的思想", "经过公司");
//        Test01 test04 = new Test01("来看看撒", "扣扣飒飒的");
//        List<Test01> list=new ArrayList<>();
//        list.add(test01);
//        list.add(test02);
//        list.add(test03);
//        list.add(test04);
//
//        return dataList;
//    }
}
