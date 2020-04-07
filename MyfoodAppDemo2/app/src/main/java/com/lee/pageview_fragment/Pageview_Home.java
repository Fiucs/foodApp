package com.lee.pageview_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.content_fragment.ui.content01.Content01Activity;
import com.lee.myInterface.OnRecycleProGressBarOnClickListener;
import com.lee.myInterface.OnRecyclerItemClickListener;
import com.lee.myadapter.PageListHomeAdapter;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.myviewmodel.MyviewModel01;
import com.lee.repository.javabean.FoodsBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;



public class Pageview_Home extends Fragment {

    LiveData<PagedList<FoodsBean>> liveData=null;
     PageviewHomeViewModel mViewModel;
     RecyclerView recyclerView;
     PageListHomeAdapter pageListHomeAdapter;
     MyviewModel01 myviewModel01=null;
     View parentView;

     final static int width= BaseDataModel.getWidth();//宽
     final static int height= BaseDataModel.getHeight();//高

    View view;

    public Pageview_Home(){
    }
    public static Pageview_Home newInstance() {
        return new Pageview_Home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //设置布局

            view = inflater.inflate(R.layout.pageview__home_fragment, container, false);

            recyclerView = view.findViewById(R.id.recycleView01);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            //绑定数据
            Fragment fragment = getParentFragment();//获取父fragment


            myviewModel01 = ViewModelProviders.of(this).get(MyviewModel01.class);
                System.out.println("11111111111111111111111111111");


            myviewModel01.setAppUri("/app/getRandomList");
            myviewModel01.setTransflag(0);

            MutableLiveData<Boolean> isNetWork = myviewModel01.getIsNetWork();
            if (liveData == null) {
                liveData = myviewModel01.getmLiveData();
                System.out.println("2222222222222222222222222222222");
            }

            pageListHomeAdapter = new PageListHomeAdapter(isNetWork, width, height, inflater, container);//设置数据
            boolean isNetWorkOk = BaseDataModel.isIsNetWorkOk();
            liveData.observe(this, new Observer<PagedList<FoodsBean>>() {
                @Override
                public void onChanged(PagedList<FoodsBean> foodsBeans) {
//                /调用的submit方法就是PagedListAdapter中的方法。它来对数据进行更新显示
                    if (foodsBeans.size()>0)
                    pageListHomeAdapter.submitList(foodsBeans);
                    else
                        pageListHomeAdapter.notifyDataSetChanged();

                    if (BaseDataModel.getTotals()<3)
                        pageListHomeAdapter.notifyDataSetChanged();

                }

            });

            //观察网络状态
            final boolean[] flag = {true};
            isNetWork.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {

                    System.out.println("网络状态aa：" + aBoolean);
                    if (!aBoolean && BaseDataModel.getFirstFllsh()==0) {
                        pageListHomeAdapter.notifyDataSetChanged();
                        System.out.println("obversr onchangewa");
                    }


                }
            });


            pageListHomeAdapter.setProGressBarOnClickListener(new OnRecycleProGressBarOnClickListener() {

                @Override
                public void onclickprogressBar(int position, View view) {

                    if(BaseDataModel.isIsNetWorkOk())
                    {
                        myviewModel01.changeDada();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"网络连接断开，请检查网络",Toast.LENGTH_SHORT).show();
                    }

                    ProgressBar progressBar = view.findViewById(R.id.progressBar);
//                    progressBar.setVisibility(View.VISIBLE);
                    System.out.println("重新刷新被点击了...." + "hhhhhh");
                    if (BaseDataModel.isIsNetWorkOk()) {
                        pageListHomeAdapter.notifyDataSetChanged();
                        BaseDataModel.setFirstFllsh(0);//重新设置成初次断网
                    }
                    BaseDataModel.setFirstFllsh(2);//代表不是初次断网

                    progressBar.setVisibility(View.INVISIBLE);

                }
            });

            pageListHomeAdapter.setOnRecyclerItemCli1ckListener(new OnRecyclerItemClickListener() {
                @Override
                public void onclickItem(int position, View view) {
                    //点击itemView 时 完成的动作
                    TextView foodIdView = view.findViewById(R.id.foodId);
                    String foodid = foodIdView.getText().toString();
                    Log.d("position", String.valueOf(position));
                    Log.d("foodId", foodid);
                    Bundle bundle = new Bundle();
                    bundle.putString("foodId", foodid);

                    //导航跳转
//                NavHostFragment.findNavController(fragment).navigate(R.id.action_homepageFragment_to_content01Fragment,bundle);

                    //新建activity
                    Intent intent = new Intent(getActivity(), Content01Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);//传递数据


                }
            });

            recyclerView.setAdapter(pageListHomeAdapter);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view1;
//        if(myviewModel01==null)
//        {}
            myviewModel01= ViewModelProviders.of(this).get(MyviewModel01.class);
            System.out.println("1-------------------------------------s");




        // TODO: Use the ViewModel
    }



}
