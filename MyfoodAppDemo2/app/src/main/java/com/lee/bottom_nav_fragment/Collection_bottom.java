package com.lee.bottom_nav_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.content_fragment.ui.favorite.FavirateAndHistoryActivity;
import com.lee.myfoodappdemo2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class Collection_bottom extends Fragment {

    private CollectionBottomViewModel mViewModel;

    public static Collection_bottom newInstance() {
        return new Collection_bottom();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.collection_bottom_fragment, container, false);

        Intent intent = new Intent(getContext(), FavirateAndHistoryActivity.class);

        startActivityForResult(intent,1);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CollectionBottomViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity().getSupportFragmentManager().popBackStack();//返回
    }
}
