package com.twopole.app.base;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twopole.app.R;

public class WebViewActivity extends BaseOnHeaderActivity {
    WebView mwebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mwebView = (WebView) findViewById(R.id.webView);

        initHeader();
        setMyTitle("正在加载中，请稍等...");

        mwebView.setBackgroundColor(0);//先设置背景色为transparent
        mwebView.setBackgroundResource(R.drawable.webview_loading);//然后设置背景图片
        mwebView.loadUrl(getIntent().getStringExtra("loadUrl"));
        WebSettings wSet = mwebView.getSettings();
        wSet.setJavaScriptEnabled(true);
        mwebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                ((TextView)findViewById(R.id.setting_head_title)).setText("加载完成");
                return true;
            }
        });
    }


}
