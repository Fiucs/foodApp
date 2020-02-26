package com.lee.content_fragment.ui.loginfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import androidx.viewpager2.widget.ViewPager2;

public class LoginrRecFragment extends Fragment {

    private LoginrRecViewModel mViewModel;
    List<Fragment> fragments;

    Button button_regester,button_login;
    ProgressBar progressBar;
    public static LoginrRecFragment newInstance() {
        return new LoginrRecFragment();
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mine_bottom_fragment, container, false);

        //注册
        button_regester=view.findViewById(R.id.register_button);
        //登录
        button_login=view.findViewById(R.id.login_button);
        //
        progressBar=view.findViewById(R.id.progressBar2);
        button_regester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                button_login.setVisibility(View.INVISIBLE);
                button_regester.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                BaseDataModel.ChekNetWork chekNetWork = BaseDataModel.getChekNetWork();
                chekNetWork.execute();
                chekNetWork.setNetWorkChekProvider(new BaseDataModel.NetWorkChekProvider() {
                    @Override
                    public void checkNetWorkProvider(Boolean b) {
                        if(b)
                        {
                            Intent intent=new Intent(getActivity(), MyRegisterActivity.class);
                            startActivityForResult(intent,1);
//                失败返回2
                            //注册成功 自动登录 即返回code2
                        }else
                        {
                            Toast.makeText(getContext(),"网络异常请重试！",Toast.LENGTH_SHORT).show();
                        }
                        button_login.setVisibility(View.VISIBLE);
                        button_regester.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("我被点击了");
                button_login.setVisibility(View.INVISIBLE);
                button_regester.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                BaseDataModel.ChekNetWork chekNetWork = BaseDataModel.getChekNetWork();
                chekNetWork.execute();
                chekNetWork.setNetWorkChekProvider(new BaseDataModel.NetWorkChekProvider() {
                    @Override
                    public void checkNetWorkProvider(Boolean b) {
                        if (b)
                        {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivityForResult(intent,1);//可接受返回的code确定使用哪种布局
//                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_mIneFragment_to_collectionFragment);
                            //登录成功 返回code2
                            //失败  返回3
                        }else
                        {
                            Toast.makeText(getContext(),"网络异常请重试！",Toast.LENGTH_SHORT).show();
                        }
                        button_login.setVisibility(View.VISIBLE);
                        button_regester.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });




            }
        });


        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginrRecViewModel.class);
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

//            NavHostFragment.findNavController(getParentFragment().getParentFragment()).navigate(R.id.action_loginrRecFragment_to_pageview_Home,bundle);
            MyFragmentStateAdapter fragmentStateAdapter = BaseDataModel.getFragmentStateAdapter();
            List<Fragment> fragments = BaseDataModel.getFragments();
            fragments.remove(0);
            fragmentStateAdapter.notifyDataSetChanged();
            fragmentStateAdapter.notifyItemRemoved(0);

            fragments.add(0,new Pageview_Recommend());
            fragmentStateAdapter.createFragment(0);
            fragmentStateAdapter.notifyItemChanged(0);
            fragmentStateAdapter.notifyDataSetChanged();
            ViewPager2 viewPager2 = BaseDataModel.getViewPager2();
            viewPager2.setAdapter(fragmentStateAdapter);


        }

    }
}
