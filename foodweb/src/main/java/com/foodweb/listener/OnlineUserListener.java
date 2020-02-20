package com.foodweb.listener;

import com.foodweb.controller.LoginController;
import com.foodweb.service.imp.NormalSearchImp;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class OnlineUserListener implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("会话被创建了");
        HttpSession session = httpSessionEvent.getSession();
        //获取了application
        ServletContext application = session.getServletContext();
        NormalSearchImp.initAppliation(application);//初始化application

        ArrayList<String> list = new ArrayList<>();
        application.setAttribute("onlineUserList",list);//初始化名单


    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        HttpSession session = httpSessionEvent.getSession();//获得session

        ServletContext application = session.getServletContext();
        List onlineUser =(List) application.getAttribute("onlineUserList");


        String user_id=(String)session.getAttribute("user_id");//获取用户id

        onlineUser.remove(user_id);//从在线名单中移除 uuid
        LoginController.removeOnleInfo(user_id);//从在线名单.txt中删除 用户

        System.out.println("用户:"+user_id+"超时退出");

    }
}
