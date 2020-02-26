package com.lee.myfoodappdemo2;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lee.myviewmodel.BaseDataModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);

        BaseDataModel.setWindowManager(this.getWindowManager());//传宽高 获取

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
            layoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);

        }

        //隐藏默认actionbar
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }

        //获取toolbar
        toolbar = findViewById(R.id.toolbar2);
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可

        //用toolbar替换actionbar
        setSupportActionBar(toolbar);
        BaseDataModel.setActionBar01(getSupportActionBar());
        //底部导航 与navhostFragment
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        NavController navController= Navigation.findNavController(this,R.id.fragment);


        AppBarConfiguration configuration=new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();

        //设置controller  底部导航栏 与 nav_host
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);


    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController controller=Navigation.findNavController(this,R.id.fragment);
//
//        return controller.navigateUp();
//    }


}
