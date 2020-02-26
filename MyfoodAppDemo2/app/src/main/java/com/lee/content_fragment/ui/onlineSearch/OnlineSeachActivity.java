package com.lee.content_fragment.ui.onlineSearch;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lee.myfoodappdemo2.R;

import androidx.appcompat.app.AppCompatActivity;

public class OnlineSeachActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_seach);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
            layoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);

        }

        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);//支持缩放
        settings.setDomStorageEnabled(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        settings.setLoadWithOverviewMode(true);

//        webView.loadUrl("https://m.xiachufang.com/");
        String keywords = getIntent().getStringExtra("keyWord");
        webView.loadUrl("https://m.meishij.net/search.php?q="+keywords);
        webView.setWebViewClient(new webViewClient());


    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();
        //结束退出程序
        return false;
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Otherwise allow the OS to handle things like tel, mailto, etc.
           webView.loadUrl(url);
            return true;
        }
    }
}


