package com.lee.myadapter;

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
import com.lee.myInterface.OnRecycleProGressBarOnClickListener;
import com.lee.myInterface.OnRecyclerItemClickListener;
import com.lee.myfoodappdemo2.R;
import com.lee.myviewmodel.BaseDataModel;
import com.lee.repository.javabean.FoodsBean;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class LocalSearchListHomeAdapter extends PagedListAdapter<FoodsBean, LocalSearchListHomeAdapter.MyviewHolder>{



    private Context mcontext;
    private int width;//宽
    private int height;//高
    private LayoutInflater inflater;//布局管理器
    private ViewGroup container;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;//item点击事件 接口

    public OnRecycleProGressBarOnClickListener proGressBarOnClickListener;//progressbar
    MutableLiveData<Boolean> netWork;

    private static final int LOADING_VIEW=0;//加载中
    private static final int NETERRO_VIEW=1;//网络错误
    private static final int FINISH_VIEW=3;//加载完成

    private static final int ITEM_VIEW=4;//加载视图

    private boolean initFlag=true;


    public boolean isInitFlag() {
        return initFlag;
    }

    public void setOnRecyclerItemCli1ckListener(OnRecyclerItemClickListener onRecyclerItemClick) {
        this.onRecyclerItemClickListener= onRecyclerItemClick;//暴露接口 回调
    }

    public void setProGressBarOnClickListener(OnRecycleProGressBarOnClickListener proGressBarOnClickListener) {
        this.proGressBarOnClickListener = proGressBarOnClickListener;
    }

    public LocalSearchListHomeAdapter(MutableLiveData<Boolean> netWork, int width, int height , LayoutInflater inflater, ViewGroup container) {

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
                viewitem=inflater.inflate(R.layout.card_view02,parent,false);//此处改变 cardView布局
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
              if(position==BaseDataModel.getTotals() )
              {
                  holder.progressBar.setVisibility(View.INVISIBLE);
                  holder.networkText.setText("--没有更多了--");
              }
              holder.networkText.setVisibility(View.VISIBLE);
//              holder.progressBar.setVisibility(View.VISIBLE);

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

        ViewGroup.LayoutParams cardViewLayoutParams=holder.cardView.getLayoutParams();
//        imaegeParams.width=(width/2-2);
        cardViewLayoutParams.height= (height/5)+10;
        holder.cardView.setLayoutParams(cardViewLayoutParams);//重新设置 carview高度

        //设置图片大小
        ViewGroup.LayoutParams imageViewParams=holder.foodimage.getLayoutParams();
        imageViewParams.height=height/5;
        imageViewParams.width=(width/2);
        holder.foodimage.setLayoutParams(imageViewParams);

        //设置文本大小
        ViewGroup.LayoutParams textParams=holder.foodname.getLayoutParams();

        textParams.height=125;
        holder.foodname.setLayoutParams(textParams);


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
            String introduce ="简介:\n"+foodsBean.getIntroduce();
            if (foodname.length()>12)
            {
                foodname = foodname.replace(foodname.substring(12), "...");//超过字数隐藏
            }
            if (introduce.length()>30)
            {
                introduce=introduce.replace(introduce.substring(30),"...");
            }

            holder.foodname.setText(foodname);
            holder.introduce.setText(introduce);
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
        ImageView foodimage;
        TextView foodname,foodId,introduce;
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
             introduce=itemView.findViewById(R.id.textView3_introduce);
        }
    }


}
