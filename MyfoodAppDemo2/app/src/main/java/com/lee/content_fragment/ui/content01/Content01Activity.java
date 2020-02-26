package com.lee.content_fragment.ui.content01;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.lee.myfoodappdemo2.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Content01Activity extends AppCompatActivity {

    FragmentManager fragmentManager;//视图 管理器
    FragmentTransaction transaction;//逻辑管理器
    Toolbar toolbar;//自定义工具栏


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_content01);



        //设置透明度

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
//        {
//            WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
//            layoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
//
//        }
//获得toobar
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("菜谱详情");
        setSupportActionBar(toolbar);

        int red=(int)(Math.random()*256);
        int green=(int)(Math.random()*256);
        int blue=(int)(Math.random()*256);

        toolbar.setBackgroundColor(Color.rgb(red, green, blue));
                //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String foodId = (String) bundle.getString("foodId");


//添加一个fragment实例
        fragmentManager=getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();

//add 将fragment对象添加到antiviy
        Content01Fragment content01Fragment = new Content01Fragment();
        content01Fragment.setArguments(bundle);//设置参数
        transaction.add(R.id.fragmentct,content01Fragment);
        //调用commit方法使其生效

        transaction.commit();


    }

    @Override
    protected void onDestroy() {
        int red=(int)(Math.random()*256);
        int green=(int)(Math.random()*256);
        int blue=(int)(Math.random()*256);

        toolbar.setBackgroundColor(Color.rgb(red, green, blue));
        super.onDestroy();
    }

    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                this.finish();//结束该activity
//                return true;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//
//    }
}
