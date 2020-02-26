package com.lee.content_fragment.ui.Type;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.lee.content_fragment.ui.content01.Content01Activity;
import com.lee.myInterface.OnRecyclerItemClickListener;
import com.lee.myadapter.PageListHomeAdapter;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.myviewmodel.MyviewModel01;
import com.lee.repository.javabean.FoodsBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class TypeListFragment extends Fragment {

    RecyclerView recyclerView;
    PageListHomeAdapter pageListHomeAdapter;
    MyviewModel01 myviewModel01;
    View parentView;
    TextView header_text;

    final static int width= BaseDataModel.getWidth();//宽
    final static int height= BaseDataModel.getHeight();//高


    ActionBar actionBar;
    private TypeListViewModel mViewModel;
    TextView type_name;
    String keyWord=null;
    String foodClass=null;


    android.widget.SearchView mSearchView;
//    SearchView mSearchView;
    public static TypeListFragment newInstance() {
        return new TypeListFragment();
    }

    public String getWords()
    {
        Bundle bundle = getArguments();


        if ((keyWord=bundle.getString("keyWord"))!=null)
        {

            return  "/app/searchByName?nameLike="+keyWord;
        }
        else
        {
            foodClass = bundle.getString("foodClass");
            return "/app/searchBySmallClass?smallClass="+foodClass;
        }


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.type_list_fragment, container, false);



        header_text=view.findViewById(R.id.head_text);
        actionBar = BaseDataModel.getActionBar();
        actionBar.setSubtitle(keyWord);//标题77

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true); //设置返回键可用


        //设置布局
        recyclerView = view.findViewById(R.id.text_recycleview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //绑定数据
        Fragment fragment = getParentFragment();//获取父fragment


        final String[] s = {""};
        myviewModel01= ViewModelProviders.of(this).get(MyviewModel01.class);
        myviewModel01.setAppUri(getWords());
        myviewModel01.setTransflag(0);
        LiveData<PagedList<FoodsBean>> liveData = myviewModel01.getmLiveData();
        pageListHomeAdapter=new PageListHomeAdapter(myviewModel01.getIsNetWork(),width,height,inflater,container);
        liveData.observe(this, new Observer<PagedList<FoodsBean>>() {
            @Override
            public void onChanged(PagedList<FoodsBean> foodsBeans) {
//                /调用的submit方法就是PagedListAdapter中的方法。它来对数据进行更新显示
                pageListHomeAdapter.submitList(foodsBeans);
                s[0] ="总共查询到："+String.valueOf(BaseDataModel.getTotals())+"条记录";
                header_text.setText(s[0]);
            }
        });



        pageListHomeAdapter.setOnRecyclerItemCli1ckListener(new OnRecyclerItemClickListener() {
            @Override
            public void onclickItem(int position,View view) {
                //点击itemView 时 完成的动作
                TextView foodIdView = view.findViewById(R.id.foodId);
                String foodid = foodIdView.getText().toString();
                Log.d("position",String.valueOf(position));
                Log.d("foodId",foodid);
                Bundle bundle = new Bundle();
                bundle.putString("foodId",foodid);

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
        mViewModel = ViewModelProviders.of(this).get(TypeListViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onDestroyView() {
        System.out.println("TypelistFragment----------------------");
        actionBar.setSubtitle("");
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchView = menu.findItem(R.id.app_bar_search);
        SearchView mSearchView =(SearchView) searchView.getActionView();//获得searchview

        //获取搜索文本框
        AutoCompleteTextView mSearchEdit =  mSearchView.findViewById(R.id.search_src_text);
        //获取搜索框关闭
        ImageView closeButton = mSearchView.findViewById(R.id.search_close_btn);

        mSearchView.setIconifiedByDefault(false);

        mSearchView.setMaxWidth((width/100)*60);

        mSearchView.setQueryHint("请输入关键词");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //文字提交的时候哦回调，newText是最后提交搜索的文字
                System.out.println("提交搜索的文字:"+query);
                actionBar.setTitle(" 关键词 " );
                actionBar.setSubtitle(  "  "+query);

                myviewModel01.setAppUri("/app/searchByName?nameLike="+query);
                myviewModel01.setTransflag(0);

//                recyclerView.removeAllViews();//清空视图
                myviewModel01.setmLiveData(null);//清空数据
                myviewModel01.setPositionalDataSource(null);//清空 数据发送器
                LiveData<PagedList<FoodsBean>> liveData = myviewModel01.getmLiveData();
                liveData.observe(getActivity(), new Observer<PagedList<FoodsBean>>() {
                    @Override
                    public void onChanged(PagedList<FoodsBean> foodsBeans) {
//                /调用的submit方法就是PagedListAdapter中的方法。它来对数据进行更新显示

                        pageListHomeAdapter.submitList(foodsBeans);

                    }
                });
                mSearchView.setQuery("",false);
                mSearchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //在文字改变的时候回调，query是改变之后的文字

                System.out.println("改变之后的文字:"+newText
                );
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if (keyWord!=null)
       {
           switch (item.getItemId())
           {
               case android.R.id.home:
                   System.out.println("sadsadasdasdasd");
                   getActivity().onBackPressed();
                   return true;
           }

       }
        return super.onOptionsItemSelected(item);
    }



}
