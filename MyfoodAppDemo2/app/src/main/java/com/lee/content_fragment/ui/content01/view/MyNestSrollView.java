package com.lee.content_fragment.ui.content01.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

public class MyNestSrollView extends NestedScrollView {
    public MyNestSrollView(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        System.out.println(l+" "+t+"  "+oldl+"  "+oldt);

        if(getScrollY() == getChildAt(0).getMeasuredHeight()- getMeasuredHeight()){
            //滑动到了底部,注意如果有padding设置还需要减去一个padding的数值
            System.out.println("滑动到地步了");
        }
    }


}
