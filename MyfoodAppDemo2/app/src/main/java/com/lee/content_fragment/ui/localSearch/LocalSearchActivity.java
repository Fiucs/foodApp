package com.lee.content_fragment.ui.localSearch;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LocalSearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
            layoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);

        }

        toolbar=findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        int red=(int)(Math.random()*256);
        int green=(int)(Math.random()*256);
        int blue=(int)(Math.random()*256);

        toolbar.setBackgroundColor(Color.rgb(red, green, blue));
        //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用


        //底部导航 与navhostFragment
        String keywords = getIntent().getStringExtra("keyWord");
        Bundle bundle = new Bundle();
        bundle.putString("keyWord",keywords);
        LocalSearchListFragment listFragment = new LocalSearchListFragment();
//        TypeListFragment listFragment = new TypeListFragment();
        listFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,listFragment)
                .commit();



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
}
