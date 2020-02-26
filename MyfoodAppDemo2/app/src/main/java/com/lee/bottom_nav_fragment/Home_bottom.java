package com.lee.bottom_nav_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lee.content_fragment.ui.loginfragment.LoginrRecFragment;
import com.lee.myadapter.MyFragmentStateAdapter;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.pageTranseformer.ScaleTransformer;
import com.lee.pageview_fragment.Pageview_Home;
import com.lee.pageview_fragment.Pageview_Recommend;
import com.lee.pageview_fragment.SearchPageFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;


public class Home_bottom extends Fragment {

    private HomeBottomViewModel mViewModel;

    private Pageview_Home[] pageview_homes=new Pageview_Home[3];
    private Pageview_Recommend pageview_recommend=null;
    public static Home_bottom newInstance() {
        return new Home_bottom();
    }
    List<Fragment> fragments=null;
    MyFragmentStateAdapter fragmentStateAdapter=null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.home_bottom_fragment, container, false);
        TabLayout tabLayout=inflate.findViewById(R.id.tabLayout);
        ViewPager2 viewPager2=inflate.findViewById(R.id.viewpager3);

        SharedPreferences account = getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS );
        String password = account.getString("password", "_null");
        System.out.println("home_buttom+password："+password);
            fragments = new ArrayList<>();

            if (password.equals("_null")) {
                fragments.add(new LoginrRecFragment());
            } else {
                if (pageview_recommend == null) {
                    fragments.add(new Pageview_Recommend());
                }
                fragments.add(pageview_recommend);

            }
            fragments.add(new Pageview_Home());
            fragments.add(new SearchPageFragment());

            fragmentStateAdapter = new MyFragmentStateAdapter(getActivity(),fragments );
    viewPager2.setPageTransformer(new ScaleTransformer());

        //设置界面名字 在 tablayout中

        viewPager2.setOffscreenPageLimit(1);

        viewPager2.setAdapter(fragmentStateAdapter);
        viewPager2.setCurrentItem(1,false);//设置默认的 界面显示

        new TabLayoutMediator(tabLayout,viewPager2,(tab,position)-> {
            switch (position){
                case 0:
                    tab.setText("我的推荐");
                    break;
                case 1:
                    tab.setText("随便看看");

                    break;
                case 2:
                    tab.setText("菜谱查询");
                    break;

            }
        }).attach();//运行
        
        BaseDataModel.setFragmentStateAdapter(fragmentStateAdapter);
        BaseDataModel.setFragments(fragments);
        BaseDataModel.setViewPager2(viewPager2);

        return inflate;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeBottomViewModel.class);
        // TODO: Use the ViewModel

    }




}
