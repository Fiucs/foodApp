package com.lee.pageview_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lee.myfoodappdemo2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class OnlieSearch_Fragment extends Fragment {

    private OnlieSearchViewModel mViewModel;

    private WebView webView;
    public static OnlieSearch_Fragment newInstance() {

        return new OnlieSearch_Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.onlie_search__fragment, container, false);

        webView=view.findViewById(R.id.webview_search);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);//支持缩放

        webView.loadUrl("http://www.xiachufang.com/search/?keyword=%E6%93%8D");
        webView.setWebViewClient(new webViewClient());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OnlieSearchViewModel.class);
        // TODO: Use the ViewModel
    }





//    @Override
//    //设置回退
//    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        }
//        getActivity().finish();
//        //结束退出程序
//        return false;
//    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
