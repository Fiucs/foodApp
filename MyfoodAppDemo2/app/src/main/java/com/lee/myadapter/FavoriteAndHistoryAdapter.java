package com.lee.myadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lee.myInterface.OnCollectOnClickListener;
import com.lee.myInterface.OnRecycleProGressBarOnClickListener;
import com.lee.myInterface.OnRecyclerItemClickListener;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.repository.javabean.FoodsBean;

import java.text.SimpleDateFormat;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class FavoriteAndHistoryAdapter extends PagedListAdapter<FoodsBean, FavoriteAndHistoryAdapter.MyviewHolder>{



    private Context mcontext;
    private int width;//宽
    private int height;//高
    private LayoutInflater inflater;//布局管理器
    private ViewGroup container;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;//item点击事件 接口

    public OnRecycleProGressBarOnClickListener proGressBarOnClickListener;//progressbar

    public OnCollectOnClickListener collectOnClickListener;//收藏点击

    MutableLiveData<Boolean> netWork;

    private static final int LOADING_VIEW=0;//加载中
    private static final int NETERRO_VIEW=1;//网络错误
    private static final int FINISH_VIEW=3;//加载完成

    private static final int ITEM_VIEW=4;//加载视图

    private boolean initFlag=true;

    private int flag;//收藏/历史

    public boolean isInitFlag() {
        return initFlag;
    }

    public void setOnRecyclerItemCli1ckListener(OnRecyclerItemClickListener onRecyclerItemClick) {
        this.onRecyclerItemClickListener= onRecyclerItemClick;//暴露接口 回调
    }

    public void setProGressBarOnClickListener(OnRecycleProGressBarOnClickListener proGressBarOnClickListener) {
        this.proGressBarOnClickListener = proGressBarOnClickListener;
    }

    public void setCollectOnClickListener(OnCollectOnClickListener collectOnClickListener) {
        this.collectOnClickListener = collectOnClickListener;
    }

    public FavoriteAndHistoryAdapter(MutableLiveData<Boolean> netWork, int width, int height , LayoutInflater inflater, ViewGroup container,int flag01) {

        super(new DiffUtil.ItemCallback<FoodsBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull FoodsBean oldItem, @NonNull FoodsBean newItem) {
                return oldItem.getFoodid().equals(newItem.getFoodid());
            }

            @Override
            public boolean areContentsTheSame(@NonNull FoodsBean oldItem, @NonNull FoodsBean newItem) {
                return oldItem.getFoodid().equals(newItem.getFoodid());
            }
        });
        this.width=width;
        this.height=height;
        this.inflater=inflater;
        this.container=container;
        this.netWork=netWork;
        this.flag=flag01;

    }

    /**
     * 设置 元素宽高
     * @param parent
     * @param viewType
     * @return
     */
    View viewitem,viewfoot;
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        System.out.println("oncreateViweType:"+viewType);
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        boolean isNetWorkOk = BaseDataModel.isIsNetWorkOk();//网络状况
        View view;
//        Log.d("网络状态：",isNetWorkOk+"");



        if(viewType==ITEM_VIEW)
        {
                viewitem=inflater.inflate(R.layout.card_view03,parent,false);
                Log.d("width",String.valueOf(width/2));
                Log.d("height",String.valueOf(height));

            view=viewitem;

        }
        else
        {
            viewfoot=inflater.inflate(R.layout.foot_view,parent,false);
            view=viewfoot;
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            layoutParams.setFullSpan(true);
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            ImageView imageView = view.findViewById(R.id.imageViewpro);
            TextView textviewNet = view.findViewById(R.id.textViewnet);
            imageView.setVisibility(View.INVISIBLE);
            textviewNet.setVisibility(View.INVISIBLE);


        }


        return new MyviewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        boolean value = BaseDataModel.isIsNetWorkOk();
        Log.d("我进来了","onblind");
        System.out.println("onviewhoder"+position);

      if (getItemViewType(position)==ITEM_VIEW)
        {
            showItems(holder,position);
            Log.d("我进来了","onblind正在加载条目");

        }
      else
      {

          if(value)//联网
          {
              holder.networkText.setText("加载中");
              //再判断 页
              if(position==BaseDataModel.getTotals() && position>=0 )
              {
                  holder.progressBar.setVisibility(View.INVISIBLE);
                  holder.networkText.setText("--没有更多了--");
              }
              holder.networkText.setVisibility(View.VISIBLE);


          }
          else
          {

              System.out.println("网络连接错误,请联网再试"+":::position"+position);
              holder.progressBar.setVisibility(View.INVISIBLE);
              holder.networkText.setText("网络连接错误,请联网再试");
              holder.networkText.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                         if(proGressBarOnClickListener!=null)
                        {
                            System.out.println("重新刷新被点击了");
                            proGressBarOnClickListener.onclickprogressBar(position,holder.itemView);
                        }
                  }
              });

              holder.networkText.setVisibility(View.VISIBLE);

          }
      }


    }

//    返回条目加载 加载更多效果
    @Override
    public int getItemCount() {

        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {

        System.out.println("viewType Position:"+position);
        if(position==getItemCount()-1 )
        {
                return LOADING_VIEW;//加载footview
        }
        else
        {
            return ITEM_VIEW;//加载条目
        }



    }

    public void showItems(MyviewHolder holder, int position )
    {


        FoodsBean foodsBean=getItem(position);//获取当前位置的bean
        int randomH = new Random().nextInt(80);
        ViewGroup.LayoutParams cardViewLayoutParams=holder.cardView.getLayoutParams();
//        imaegeParams.width=(width/2-2);
        cardViewLayoutParams.height= (height/5)+10;
        holder.cardView.setLayoutParams(cardViewLayoutParams);//重新设置 carview高度

        //设置图片大小
        ViewGroup.LayoutParams imageViewParams=holder.foodimage.getLayoutParams();
        imageViewParams.height=height/5;
        imageViewParams.width=(width/2)-60;
        holder.foodimage.setLayoutParams(imageViewParams);

        //设置文本大小
        ViewGroup.LayoutParams textParams=holder.foodname.getLayoutParams();

        textParams.height=125;
        holder.foodname.setLayoutParams(textParams);

        //设置时间
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        if (foodsBean.getDerive_time()!=null)
        {

            String format = dateFormat.format(foodsBean.getDerive_time());
            Log.e("日期：",format);
            format=format.replace("00:00","");
            holder.textViewDate.setText(format);
        }

        //设置收藏点击事件

        if (flag==1)
        {
            holder.collection.setImageResource(R.drawable.ic_favorite_red_24dp);
            holder.collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("收藏事件","点击了接口回调前");
                    if(collectOnClickListener!=null)
                    {

                        Log.e("收藏事件","点击了");
                        collectOnClickListener.onclickcollect(position,holder.itemView);
                    }
                }
            });
        }else
        {
            holder.collection.setVisibility(View.GONE);
        }



        //设置foodId 到隐藏区域
        holder.foodId.setText(foodsBean.getFoodid());

        //设置点击
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onRecyclerItemClickListener!=null)
                {
                    System.out.println("我被点击了");
                    onRecyclerItemClickListener.onclickItem(position,holder.itemView);
                }
            }
        });


        try {
            String foodname = foodsBean.getFoodname();
            if (foodname.length()>15)
            {
                foodname = foodname.replace(foodname.substring(15), "...");//超过字数隐藏
            }

            holder.foodname.setText(foodname);
            //FIXME 获取上下文
            Glide.with(holder.itemView).load(foodsBean.getFoodpic()).
                    apply(RequestOptions.bitmapTransform(new RoundedCorners(30))).
//                    override(width/2-10,height/3).
        into(holder.foodimage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    //完成viewHolder  组件绑定
    static class MyviewHolder extends RecyclerView.ViewHolder{
        ImageView foodimage,collection;
        TextView foodname,foodId,textViewDate;
        CardView cardView;

        ProgressBar progressBar;
        TextView networkText;
        ImageView imageViewpro;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
             foodimage = itemView.findViewById(R.id.imageView1);
             foodname=itemView.findViewById(R.id.textView1);
             cardView=itemView.findViewById(R.id.page_home_cardview);
             foodId=itemView.findViewById(R.id.foodId);
             progressBar=itemView.findViewById(R.id.progressBar);
             networkText=itemView.findViewById(R.id.textViewnet);
             imageViewpro=itemView.findViewById(R.id.imageViewpro);
             textViewDate=itemView.findViewById(R.id.textViewdate);
             collection=itemView.findViewById(R.id.imageView3);



        }
    }


}
