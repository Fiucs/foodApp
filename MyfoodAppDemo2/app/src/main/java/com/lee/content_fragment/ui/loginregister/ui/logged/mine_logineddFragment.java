package com.lee.content_fragment.ui.loginregister.ui.logged;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class mine_logineddFragment extends Fragment {

    private MineLogineddViewModel mViewModel;

    public static mine_logineddFragment newInstance() {
        return new mine_logineddFragment();
    }

    ImageView imageViewUser;
    TextView textViewUsername,textViewUserID;
    Toolbar toolbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mine_loginedd_fragment, container, false);

        ActionBar actionBar = BaseDataModel.getActionBar01();//获取actionBar

        actionBar.setDisplayHomeAsUpEnabled(false);//不现实
        actionBar.setHomeButtonEnabled(false);

        textViewUsername=view.findViewById(R.id.textView_username);
        textViewUserID=view.findViewById(R.id.textView_userId);

        Bundle arguments = getArguments();
        if(arguments!=null)
        {
            String uname="用户名："+arguments.getString("username");
            String userId="ID："+arguments.getString("usernameId");
            textViewUsername.setText(uname);
            textViewUserID.setText(userId);
        }
//
//        SharedPreferences account =getActivity().getSharedPreferences("account", Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS);
//        String password = account.getString("password","_null");//s1为返回值
//        String username = account.getString("username", "_null");//未找到
//        String usernameId = account.getString("usernameId", "_null");//未找到名字
//        System.out.println("用户名："+username+"  密码："+password+" ID"+usernameId);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MineLogineddViewModel.class);
        // TODO: Use the ViewModel
    }

}
