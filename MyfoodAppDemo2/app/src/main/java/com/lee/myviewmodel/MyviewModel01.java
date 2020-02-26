package com.lee.myviewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.repository.javabean.FoodsBean;
import com.lee.repository.network.FoodListThread;
import com.lee.repository.network.PageDataSourceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

public class MyviewModel01 extends AndroidViewModel {
    //学校wifi
//    private String URL="http://192.168.1.101:8081/app/searchBySystemKey";
//    手机wifi
//    private String URL="http://192.168.43.64:8081/app/searchBySystemKey";
    //家中wifi
    private String BaseURL="http://192.168.1.7:8081";
    private String URL;
    //每页需要加载的数量
    private  static  final  int PAGE_SIZE=3;
    //起始页 1
    private  static  final  int FIRST_PAGE=1;
    //当前 页码数
    private  int currentPage=FIRST_PAGE;
    //列表数据
//    private LiveData<PagedList<FoodsBean>> mLiveData;
    private LiveData mLiveData=null;
    //用于保存 网络返回的信息类

    MutableLiveData<Boolean> isNetWork;

    PositionalDataSource<FoodsBean>  positionalDataSource=null;
    private LiveData<Integer> total;//总数

    private Map<String,String> reultInfo;
    ObjectMapper mapper = new ObjectMapper();

    private Context context;

    Thread thread2;
    Thread thread1;
    //基本地址
    private String appUri;
//    其他参数
/////
    //设置转换bean的类型 0 foodsbean ,1 food class
    private int transflag;

    public void setAppUri(String appUri) {
        this.appUri = appUri;
        URL=BaseURL+appUri;
    }


    public void setTransflag(int transflag) {
        this.transflag = transflag;

    }

     MyviewModel01 myviewModel01;
    public MyviewModel01(@NonNull Application application) {

        super(application);
        this.context=application.getApplicationContext();
        if(isNetWork==null)
        {
            isNetWork=new MutableLiveData<>();
            isNetWork.setValue(true);
        }


    }

    public void setmLiveData(LiveData mLiveData) {
        currentPage=FIRST_PAGE;

        this.mLiveData = mLiveData;
    }

    public void setPositionalDataSource(PositionalDataSource<FoodsBean> positionalDataSource) {
        this.positionalDataSource = positionalDataSource;
    }

    //获取 livedata
    public LiveData getmLiveData() {
        initPageList();//初始化PageList
        return mLiveData;
    }

    public  void changeDada()
    {
        System.out.println("我执行了啊啊changeData");
        if(!thread1.isAlive())
        {
           thread1=new Thread(thread1);
           thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("线程咋横在运行："+thread1.getName());
        }

//        我被调用了


    }

    //获取 livedate network


    public MutableLiveData<Boolean> getIsNetWork() {
        return isNetWork;
    }

    public LiveData<Integer> getTotal() {
        return total;
    }

    /**
     * 初始化PAgeList
     */
    private void initPageList() {

        //构建LiveData

        if(positionalDataSource==null )
        {
            positionalDataSource=new PositionalDataSource<FoodsBean>() {

                List<FoodsBean> list=new ArrayList<>();

                /**
                 *
                 * @param params 包含当前加载的位置position、下一页加载的长度count
                 * @param callback 将数据回调给UI界面使用callback.onResult
                 */
                @Override
                public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<FoodsBean> callback) {

                    //计算 下一次数据 加载位置
                    final int position=computeInitialLoadPosition(params,PAGE_SIZE);
                    ArrayList<Object> argsList = new ArrayList<>();
                    argsList.add(true);//添加 初始化标志
                    argsList.add(position);//添加初始化未知
                    argsList.add(callback);//添加初始化 callback
                    argsList.add(null);//添加 loadcallback
                     thread1=new Thread(new FoodListThread(argsList,currentPage,PAGE_SIZE,URL,transflag,isNetWork));//开启子线程 放置断网重连 无法刷新的bug
                    thread1.start();
                    System.out.println("数据初始化了");
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //recyclerView第一次加载时我们调用OkHttp进行数据的加载
                    //add后面都是post请求所需要的字段及值，根据请求数据的不同有所改变


                }

                /**
                 * 当用户滑动recyclerView到下一屏的时候自动调用，这里我们自动加载下一页的数据
                 * @param params
                 * @param callback
                 */
                @Override
                public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<FoodsBean> callback) {
                    //
                    System.out.println("我进来了");
                    //每次加载到分页条目时页数变为下一页
                    Log.d("sendMSG","发生了请求");
                    currentPage++;
                    ArrayList<Object> argsList = new ArrayList<>();
                    argsList.add(false);//添加 初始化标志
                    argsList.add(0);//添加初始化 position
                    argsList.add(null);//添加 callback
                    argsList.add(callback);//添加初始化 loadcallback

                     thread1=new Thread(new FoodListThread(argsList,currentPage,PAGE_SIZE,URL,transflag,isNetWork));//开启子线程 放置断网重连 无法刷新的bug
                    thread1.start();
                    System.out.println("数据继续加载了");
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("我出来了");
//                ArrayList<FoodsBean> list = new ArrayList<>();
                    //callback.onResult(list);

                }
            };
        }
        if(mLiveData==null && isNetWork.getValue())
        {
            mLiveData=new LivePagedListBuilder(new PageDataSourceFactory(positionalDataSource),
                    new PagedList.Config.Builder().setPageSize(1)
                            .setPrefetchDistance(1)
                            .setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(2)
                            .build()).build();
        }



    }

    //自定义 callBack



}


