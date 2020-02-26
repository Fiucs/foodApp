package com.lee.bottom_nav_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.content_fragment.ui.Type.TypeActivity;
import com.lee.myfoodappdemo2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class Type_bottom extends Fragment {

    private TypeBottomViewModel mViewModel;
    Fragment fragment;

    public static Type_bottom newInstance() {
        return new Type_bottom();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.type_bottom_fragment, container, false);

        Intent intent = new Intent(getActivity(), TypeActivity.class);
        startActivityForResult(intent,1);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TypeBottomViewModel.class);

        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        fragment = getParentFragment();
        System.out.println("resuluCode:"+resultCode);

        Bundle bundle = new Bundle();
        getActivity().getSupportFragmentManager().popBackStack();//返回
//            跳转到首页操作
//            NavHostFragment.findNavController(fragment).navigate(R.id.action_typingFragment_to_homepageFragment);


    }
}
