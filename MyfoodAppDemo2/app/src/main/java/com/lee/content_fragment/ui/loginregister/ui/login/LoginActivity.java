package com.lee.content_fragment.ui.loginregister.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.myfoodappdemo2.R;
import com.lee.repository.javabean.MsgInfo;
import com.lee.utils.DES;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    EditText username,password;
    Button button_login;
    ImageView imageView_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        imageView_back=findViewById(R.id.imageView_back);
        button_login=findViewById(R.id.login);

         loginViewModel = new LoginViewModel(getApplication());

        password.addTextChangedListener(new MyTextWatcher(button_login));
        username.addTextChangedListener(new MyTextWatcher(button_login));

        //登录状态
//        loginViewModel.getStatus().observe(this, new Observer<MsgInfo>() {
//            @Override
//            public void onChanged(MsgInfo msgInfo) {
//                if(msgInfo.isSuccess())
//                {
//                    Intent intent = new Intent();
//                    intent.putExtra("username",msgInfo.getMsg());
//                    setResult(2,intent);
//                    finish();
//                }
//
//            }
//        });



        //按钮回退

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

    private static ObjectMapper mapper = new ObjectMapper();
    //登录
    public void loginOnclick(View view) {

        button_login.setEnabled(false);
        LoginViewModel.LoginAsyncTask loginAsyncTask = loginViewModel.loginMethod();
        loginAsyncTask.execute(username.getText().toString(),password.getText().toString(),"/login");
        loginAsyncTask.setOnAsyncResponse(new LoginViewModel.AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(MsgInfo msgInfo) {
                if(msgInfo.isSuccess())
                {
                    HashMap<String,String> map = new HashMap<>();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("username",username.getText().toString());
                    bundle.putString("usernameId",msgInfo.getMsg());

                    intent.putExtra("userInfo",bundle);
                    try {
                        //用户名 是加密要是
                        String desPassword = DES.getDES(password.getText().toString(), username.getText().toString());//密码加密
                        SharedPreferences account = getSharedPreferences("account", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS );
                        SharedPreferences.Editor edit = account.edit();
                        edit.putString("username", LoginActivity.this.username.getText().toString());
                        edit.putString("password",desPassword );
                        edit.putString("usernameId",msgInfo.getMsg() );
                        System.out.println("用户名："+username);
                        System.out.println("密码："+password);
                        edit.remove("username");
                        edit.remove("password");
//                        edit.remove("usernameId");
                        edit.apply();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    setResult(2,intent);
                     Toast.makeText(getApplication(),"登录成功",Toast.LENGTH_SHORT).show();
                     finish();
                }
                else
                {
                    if(msgInfo.getMsg().equals("_error"))
                    {
                        Toast.makeText(getApplication(),"登录超时，网络错误",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplication(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                    button_login.setEnabled(true);

                }
            }
        });



//       String URL = "http://192.168.1.7:8081/login";
//        final MsgInfo[] msgInfo1 = {null};
//        final int[] flag = {0};
//
//
//        OkHttpUtils
//                .post()
//                .url(URL)
//                .addHeader("connection", "close")
//                .addParams("userName", String.valueOf(username))
//                .addParams("password", String.valueOf(password))
//                .build()
//                .connTimeOut(5000)
//                .readTimeOut(5000)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Toast.makeText(getApplication(),"登录失败",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//
//                        MsgInfo msgInfo = null;
//                        try {
//                            msgInfo = mapper.readValue(response, MsgInfo.class);
//
//                            if(msgInfo.isSuccess())
//                            {
//                                Intent intent = new Intent();
//                                intent.putExtra("username",msgInfo.getMsg());
//                                setResult(2,intent);
//                                Toast.makeText(getApplication(),"登录成功",Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                            else
//                            {
//
//                                Toast.makeText(getApplication(),"登录成功",Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        } catch (JsonProcessingException e) {
//                            Toast.makeText(getApplication(),"登录失败",Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//
//                    }
//                });

    }

    class MyTextWatcher implements TextWatcher{

        Button button;
        MyTextWatcher(Button button_login)
        {
            button=button_login;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (username.length()>0 && password.length()>0)
            {
                button_login.setEnabled(true);
            }
            else
            {
                button_login.setEnabled(false);
            }
        }
    }


}
