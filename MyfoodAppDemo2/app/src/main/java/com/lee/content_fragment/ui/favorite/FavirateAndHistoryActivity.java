package com.lee.content_fragment.ui.favorite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lee.myfoodappdemo2.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class FavirateAndHistoryActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favirate_and_history);
        //任务栏透明
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
            layoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);

        }
        toolbar=findViewById(R.id.toolbar5);
         tabLayout=findViewById(R.id.tabLayout2);
         viewPager2=findViewById(R.id.viewpager_hisfa);
        toolbar.setTitle("返回");

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        int red=(int)(Math.random()*256);
        int green=(int)(Math.random()*256);
        int blue=(int)(Math.random()*256);

        toolbar.setBackgroundColor(Color.rgb(red, green, blue));
        //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用


        SharedPreferences account = getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS );
        String password = account.getString("password", "_null");
        System.out.println("home_buttom+password："+password);

//        viewPager2.setOffscreenPageLimit(1);
//
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position==1)
                {
                    FavoriteAndHistoryFragment favoriteAndHistoryFragment= new FavoriteAndHistoryFragment();
                    favoriteAndHistoryFragment.setFlag(1,"aaaa");//收藏 传入用户名
                    return favoriteAndHistoryFragment;
                }
                else
                {
                    FavoriteAndHistoryFragment favoriteAndHistoryFragment=new FavoriteAndHistoryFragment();
                    favoriteAndHistoryFragment.setFlag(0,"aaaa");//历史

                    return favoriteAndHistoryFragment;
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
//        viewPager2.setCurrentItem(1,false);//设置默认的 界面显示
//

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_history_black_50dp);
                    tab.setText("浏览历史");

                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    tab.setText("我的收藏");

                    break;
            }
        });
        tabLayoutMediator.attach();//运行




    }
}
