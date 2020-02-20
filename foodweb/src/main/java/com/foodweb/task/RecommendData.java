package com.foodweb.task;

import com.foodweb.pojo.Recommend;
import com.foodweb.service.NormalSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Set;


public class RecommendData {


    private NormalSearch normalSearch;

    public void setNormalSearch(NormalSearch normalSearch) {
        this.normalSearch = normalSearch;
    }

    private Set<String> stringSet;//待更新的推荐数据

    private String uuid;//待更新的用户uuid

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    //更新 recommend 表
    public synchronized void insertIntoRecommend(String uuid, Map<String,String> map)
    {

        if (map.size()==0)
            return;
        for (String s : map.keySet()) {

            Recommend recommend = new Recommend(uuid, s, new Date());
            recommend.setRecommend_value(map.get(s));
            int i = normalSearch.insertIntoRecommend(recommend);//插入数据到表
            System.out.println(i+":"+recommend);

        }

    }

    //更新 userrecord表记录
    public synchronized void updateUserRecordStatus(int id)
    {

        int i = normalSearch.updateUserRecordStatus(id);

    }

}
