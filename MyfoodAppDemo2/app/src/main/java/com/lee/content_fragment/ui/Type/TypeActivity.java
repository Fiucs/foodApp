package com.lee.content_fragment.ui.Type;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;

public class TypeActivity extends AppCompatActivity {



    GridLayoutManager gridLayoutManager;//网格布局
    final static int width= BaseDataModel.getWidth();//宽
    final static int height= BaseDataModel.getHeight();//高
    Toolbar toolbar;

    FragmentTransaction transaction;//事务管理
    FragmentManager fragmentManager;//视图 管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        //任务栏透明
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
            layoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);

        }
        toolbar=findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        int red=(int)(Math.random()*256);
        int green=(int)(Math.random()*256);
        int blue=(int)(Math.random()*256);

        toolbar.setBackgroundColor(Color.rgb(red, green, blue));
        //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用


        //底部导航 与navhostFragment
        NavController navController= Navigation.findNavController(this,R.id.fragment4);

        NavigationUI.setupActionBarWithNavController(this,navController);
//        Navigation.findNavController(findViewById(R.id.fragment4)).navigate(R.id.type_menu_fragment);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        BaseDataModel.setActionBar(actionBar);

    }

    @Override
    protected void onDestroy() {
        int red=(int)(Math.random()*256);
        int green=(int)(Math.random()*256);
        int blue=(int)(Math.random()*256);

        toolbar.setBackgroundColor(Color.rgb(red, green, blue));
        setResult(2);//返回标志
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController controller=Navigation.findNavController(this,R.id.fragment4);
        return controller.navigateUp();
    }

//    @Override
//    public void onBackPressed() {
//
//        setResult(2);
//        finish();
//    }
}
