package com.lee.content_fragment.ui.loginregister.ui.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.content_fragment.ui.loginregister.ui.login.LoginViewModel;
import com.lee.myfoodappdemo2.R;
import com.lee.repository.javabean.MsgInfo;
import com.lee.utils.DES;

import androidx.appcompat.app.AppCompatActivity;

public class MyRegisterActivity extends AppCompatActivity {

    TextView tips;
    EditText username,password,confirmpassword;
    ImageView imageView_back;
    Button button_register;
    ProgressBar progressBar_wait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register);
        username=findViewById(R.id.editText_username);
        password=findViewById(R.id.editText2_password);
        confirmpassword=findViewById(R.id.editText3_confimpassword);
        imageView_back=findViewById(R.id.imageView3_back);
        button_register=findViewById(R.id.button_back);
        tips=findViewById(R.id.textView_tips);
        progressBar_wait=findViewById(R.id.progressBar2_wait);


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = password.getText().toString();
                String s1 = confirmpassword.getText().toString();
                String str_username = username.getText().toString();
                if (str_username.length()==0)
                    tips.setText("用户名为空,请重新输入");
                else {
                    if (s.length()>0 && s1.length()>0)
                    {
                        //提交注册信息
                        if (s.equals(s1))
                        {
                            button_register.setVisibility(View.INVISIBLE);
                            progressBar_wait.setVisibility(View.VISIBLE);
                            register(str_username,s);

                        }
                        else tips.setText("两次密码不一致请重新输入");
                    }
                    else
                    {
                        tips.setText("密码或确认密码为空请重新输入");
                    }
                }


            }
        });


        //图片返回键
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("username","null");
                setResult(3,intent);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        //手机键回退
        Intent intent = new Intent();
        intent.putExtra("username","null");
        setResult(3);//登陆失败
        finish();
    }
    public void register(String username,String password)
    {

        LoginViewModel loginViewModel = new LoginViewModel(getApplication());
        LoginViewModel.LoginAsyncTask registAsynck = loginViewModel.loginMethod();
        registAsynck.execute(username,password,"/register");//线程执行
        registAsynck.setOnAsyncResponse(new LoginViewModel.AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(MsgInfo msgInfo) {
                //此处返回注册的结果
                if (msgInfo.isSuccess())
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("username",username);
                    bundle.putString("usernameId",msgInfo.getMsg());

                    intent.putExtra("userInfo",bundle);
                    //成功
                    String desPassword = null;//密码加密
                    try {
                        desPassword = DES.getDES(password, username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferences account = getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS );
                    SharedPreferences.Editor edit = account.edit();
                    edit.putString("username",username);
                    edit.putString("password",desPassword );//加密密码
                    edit.putString("usernameId",msgInfo.getMsg() );//用户id

                    edit.remove("username");
                    edit.remove("password");
                    edit.remove("usernameId");//即时删除 方便测试



                    System.out.println("用户名："+username);
                    System.out.println("密码："+password);
                    edit.apply();
                    setResult(2,intent);//成功返回code2
                    Toast.makeText(getApplication(),"注册成功，正在返回",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    if (msgInfo.getMsg().equals("_error"))
                    {
                        Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        tips.setText(msgInfo.getMsg());
                    }

                    button_register.setVisibility(View.VISIBLE);
                    progressBar_wait.setVisibility(View.INVISIBLE);


                }
            }
        });




    }







}
