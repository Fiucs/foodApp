package com.lee.myadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lee.myInterface.OnRecyclerItemClickListener;
import com.lee.myfoodappdemo2.R;
import com.lee.repository.javabean.FoodsClass;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


public class PageListTypeAdapter extends PagedListAdapter<FoodsClass, PageListTypeAdapter.MyviewHolder>{



    private Context mcontext;
    private int width;//宽
    private int height;//高
    private LayoutInflater inflater;//布局管理器
    private ViewGroup container;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;//item点击事件 接口

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClick) {
        this.onRecyclerItemClickListener= onRecyclerItemClick;//暴露接口 回调
    }

    public PageListTypeAdapter(int width, int height , LayoutInflater inflater, ViewGroup container) {

        super(new DiffUtil.ItemCallback<FoodsClass>() {
            @Override
            public boolean areItemsTheSame(@NonNull FoodsClass oldItem, @NonNull FoodsClass newItem) {
                return oldItem.getId()==newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull FoodsClass oldItem, @NonNull FoodsClass newItem) {
                return oldItem.getSmallclass().equals(newItem.getSmallclass());
            }
        });
        this.width=width;
        this.height=height;
        this.inflater=inflater;
        this.container=container;

    }
/**
 * 样式设置
 * 根据位置改变样式
 *
 */

    @Override
    public int getItemViewType(int i) {

        if (i==0 || i==18 || i==29 || i==35 ||i==48 || i== 63 || i==74 || i==77)
        {
            return 0;//标题样式
        }
        else
            return 1;//普通样式

    }

    /**
     * 设置 元素宽高
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
//        View view=inflater.inflate(R.layout.card_view,parent,false);
        View holder=null;
        Log.d("width",String.valueOf(width/2));
        Log.d("height",String.valueOf(height));

        if(viewType==0)
        {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            holder = inflater.inflate(R.layout.type_view, parent, false);
            return new MyviewHolder(holder,0);
        }
        else
        {

            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            holder = inflater.inflate(R.layout.card_view, parent, false);
            return new MyviewHolder(holder,1);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

//        FoodsBean foodsBean=getItem(position);//获取当前位置的bean
        FoodsClass foodsClass = getItem(position);
        if (holder.holderItemType==1)
        {
            normalItem(holder,position);
        }
        else
        {
            //显示标题
            holder.typeTile.setText(foodsClass.getBigclass());
        }


    }

    /**
     * 正常item样式
     * @param holder
     * @param position
     */

    public void normalItem(MyviewHolder holder,int position)
    {
        FoodsClass foodsClass=getItem(position);//获取当前位置的bean
        int randomH = new Random().nextInt(80);
        ViewGroup.LayoutParams cardViewLayoutParams=holder.cardView.getLayoutParams();
//        imaegeParams.width=(width/2-2);
//        cardViewLayoutParams.height= (height/4)+(120)+randomH;
//        holder.cardView.setLayoutParams(cardViewLayoutParams);//重新设置 carview高度

        //设置图片大小
        ViewGroup.LayoutParams imageViewParams=holder.foodimage.getLayoutParams();
        imageViewParams.height=(width-4*6)/4;
//        imageViewParams.width=(width/2)-60;
        holder.foodimage.setLayoutParams(imageViewParams);


        //设置文本大小
        ViewGroup.LayoutParams textParams=holder.foodname.getLayoutParams();
        textParams.height=0;
        holder.foodname.setLayoutParams(textParams);


        //设置foodId 到隐藏区域
        holder.foodsmalclass.setText(foodsClass.getSmallclass());

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

            //FIXME 获取上下文
            Glide.with(holder.itemView).load(foodsClass.getSmallclass_pic()).
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
        TextView foodname,foodsmalclass;
        CardView cardView;

        int holderItemType;

        TextView typeTile;
        public MyviewHolder(@NonNull View itemView,int itemType) {
            super(itemView);
            holderItemType=itemType;//设置样式
            if (itemType==1)
            {
                foodimage = itemView.findViewById(R.id.imageView1);
                foodname=itemView.findViewById(R.id.textView1);
                cardView=itemView.findViewById(R.id.page_home_cardview);
                foodsmalclass=itemView.findViewById(R.id.foodId);

            }
         else
            {
                typeTile=itemView.findViewById(R.id.title_textView);

            }


        }
    }


}
