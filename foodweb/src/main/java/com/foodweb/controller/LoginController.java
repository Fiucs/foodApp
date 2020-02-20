package com.foodweb.controller;

import com.foodweb.pojo.MsgInfo;
import com.foodweb.service.NormalSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController

public class LoginController {
    private static String path=null;

    @Autowired
    private NormalSearch normalSearch;

    static {
        //获取路径
         path = LoginController.class.getClassLoader().getResource("onlineUser.txt").getPath();
    }


    /**
     * 注册
     * @param request
     * @param userName
     * @param password
     */
    @RequestMapping("/register")
    public MsgInfo register(HttpServletRequest request,String userName,String password)
    {
//注册
        MsgInfo msgInfo = normalSearch.register(userName, password);

        return msgInfo;

    }


    /**
     * 登录
     */
    @RequestMapping("/login")
    public MsgInfo login(HttpServletRequest request,String userName,String password)
    {
        HttpSession session = request.getSession();

        ServletContext application = session.getServletContext();

        MsgInfo msgInfo = normalSearch.login(userName, password);
        if(msgInfo.isSuccess())
        {
            //true则登陆成功 将用户userid加入session
            session.setAttribute("user_id",msgInfo.getMsg());

            System.out.println("login:userid"+msgInfo.getMsg());
            //注意将用户id加入名单而不是用户名

            List<String> onlineUserList = (List<String>) application.getAttribute("onlineUserList");

            List<String> listName = readAllOnlineInfo();
            if(!onlineUserList.contains(msgInfo.getMsg()) && !listName.contains(msgInfo.getMsg()) )
            {
                onlineUserList.add(msgInfo.getMsg());//从app中添加 长登录的用户


                writeOnlineInfo(msgInfo.getMsg());//加入在线名单
            }
            System.out.println("用户："+msgInfo.getMsg()+" 添加成功");

        }

        return msgInfo;




    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,String userName)
    {
        request.getSession().invalidate();//退出清除session
        removeOnleInfo(userName);

    }

    public static List<String> readAllOnlineInfo()
    {

        ArrayList<String> listName=null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
             listName= new ArrayList<>();
            String s="";
            while ((s=reader.readLine())!=null)
            {
                listName.add(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listName;

    }


    public static void writeOnlineInfo(String userid)
    {

        FileWriter fileWriter=null;
        try {
            fileWriter= new FileWriter(path,true);
            fileWriter.write(userid);
            fileWriter.write("\n");
            fileWriter.flush();
            System.out.println(path);
            System.out.println("写入成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void removeOnleInfo(String userid)
    {
        BufferedReader bufferedReader=null;
        BufferedWriter bufferedWriter=null;
        try {
            bufferedReader =new BufferedReader(new FileReader(path));//输入流


            String s;
            String tmp="";
            while ((s=bufferedReader.readLine())!=null)
            {
                tmp+=s+"\n";//获取所有记录
            }

            System.out.println(userid);
            System.out.println(tmp);
            String s1 = tmp.replace(userid + "\n", "");
//读完了再 打开输出流 不然记录会清空
            bufferedWriter =new BufferedWriter(new FileWriter(path));//输出流
            bufferedWriter.write(s1);
            bufferedWriter.flush();

            System.out.println(s1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }

}
