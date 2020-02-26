package com.lee.content_fragment.ui.Type;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lee.myInterface.OnRecyclerItemClickListener;
import com.lee.myadapter.PageListHomeAdapter;
import com.lee.myadapter.PageListTypeAdapter;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.myviewmodel.MyviewModel01;
import com.lee.repository.javabean.FoodsClass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class typeMenuFragment extends Fragment {

    private TypeMenuViewModel mViewModel;
    RecyclerView recyclerView;
    PageListHomeAdapter pageListHomeAdapter;
    PageListTypeAdapter pageListTypeAdapter;
    MyviewModel01 myviewModel01;
    View parentView;
    GridLayoutManager gridLayoutManager;//网格布局
    final static int width= BaseDataModel.getWidth();//宽
    final static int height= BaseDataModel.getHeight();//高

    Fragment fragment;
    Toolbar toobar;

    public static typeMenuFragment newInstance() {
        return new typeMenuFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.type_menu_fragment, container, false);

        ActionBar actionBar = BaseDataModel.getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true); //设置返回键可用


        fragment=getParentFragment();
        recyclerView = view.findViewById(R.id.typeRecycleview);

        gridLayoutManager=new GridLayoutManager(getContext(),4);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //设置每隔item 所占的单元格

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {

                System.out.println("position-----------:"+i);
                if(i==0 || i==18 || i==29 || i==35 ||i==48 || i== 63 || i==74 || i==77)
                {
                    return 4;
                }
                else
                    return 1;

            }
        });


        recyclerView.setLayoutManager(gridLayoutManager);
        //绑定数据
//        Fragment fragment = getParentFragment();//获取父fragment


        pageListTypeAdapter=new PageListTypeAdapter(width,height,inflater,container);

        myviewModel01= ViewModelProviders.of(this).get(MyviewModel01.class);

//        myviewModel01.setAppUri("/app/searchBySystemKey");
        myviewModel01.setAppUri("/app/getFoodClassMenu");
        myviewModel01.setTransflag(1);


        LiveData<PagedList<FoodsClass>> liveData = myviewModel01.getmLiveData();
        liveData.observe(this, new Observer<PagedList<FoodsClass>>() {
            @Override
            public void onChanged(PagedList<FoodsClass> foodsClasses) {
//                /调用的submit方法就是PagedListAdapter中的方法。它来对数据进行更新显示
                pageListTypeAdapter.submitList(foodsClasses);
            }
        });

        recyclerView.setAdapter(pageListTypeAdapter);


        pageListTypeAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onclickItem(int position, View view) {
                TextView view1 = view.findViewById(R.id.foodId);//获取分类
                String foodClass =(String) view1.getText();
                System.out.println("foodclass:"+foodClass);
                //点击进入分类展示界面
                Bundle bundle = new Bundle();
                bundle.putString("foodClass",foodClass);

                NavHostFragment.findNavController(fragment).navigate(R.id.action_type_menu_fragment_to_typeListFragment,bundle);
            }
        });


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TypeMenuViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onDestroyView() {
        System.out.println("TypeMenuFragment----------------------");

        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                System.out.println("sadsadasdasdasd");
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
