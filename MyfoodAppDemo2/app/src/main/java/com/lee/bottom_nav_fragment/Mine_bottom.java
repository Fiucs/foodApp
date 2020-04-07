package com.lee.bottom_nav_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lee.content_fragment.ui.loginregister.ui.login.LoginActivity;
import com.lee.content_fragment.ui.loginregister.ui.register.MyRegisterActivity;
import com.lee.myadapter.MyFragmentStateAdapter;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.pageview_fragment.Pageview_Recommend;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;


public class Mine_bottom extends Fragment {

    private MineBottomViewModel mViewModel;

    public static Mine_bottom newInstance() {
        return new Mine_bottom();
    }

    Button button_regester,button_login;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mine_bottom_fragment, container, false);

        SharedPreferences account =getActivity().getSharedPreferences("account", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);

        String password = account.getString("password","_null");//s1为返回值密码

        if(!password.equals("_null") && BaseDataModel.isIsNetWorkOk())
        {
//            登陆了
            String username = account.getString("username", "_null");//未找到名字
            String usernameId = account.getString("usernameId", "_null");//未找到名字
            Bundle bundle = new Bundle();
            bundle.putString("username",username);
            bundle.putString("usernameId",usernameId);
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_mIneFragment_to_mine_logineddFragment,bundle);
            System.out.println("用户名："+username+"  密码："+password);

        }

        //注册
        button_regester=view.findViewById(R.id.register_button);
        button_regester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent=new Intent(getActivity(), MyRegisterActivity.class);
                startActivityForResult(intent,1);//
//                失败返回2
                //注册成功 自动登录 即返回code2
            }
        });

        //登录
        button_login=view.findViewById(R.id.login_button);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent,1);//可接受返回的code确定使用哪种布局
//                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_mIneFragment_to_collectionFragment);
                //登录成功 返回code2
                //失败  返回3
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MineBottomViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2)///成功 跳转到我的页面
        {
            Bundle bundle =data.getBundleExtra("userInfo");
            System.out.println(bundle.getString("usernameId"));
//            getActivity().onBackPressed();
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_mIneFragment_to_mine_logineddFragment,bundle);
            MyFragmentStateAdapter fragmentStateAdapter = BaseDataModel.getFragmentStateAdapter();
            List<Fragment> fragments = BaseDataModel.getFragments();
            fragments.remove(0);
            fragmentStateAdapter.notifyDataSetChanged();
            fragmentStateAdapter.notifyItemRemoved(0);

            fragments.add(0,new Pageview_Recommend());
            fragmentStateAdapter.createFragment(0);

            fragmentStateAdapter.notifyDataSetChanged();//重新设置 我的推荐界面
            ViewPager2 viewPager2 = BaseDataModel.getViewPager2();
            viewPager2.setAdapter(fragmentStateAdapter);



        }

    }


}
