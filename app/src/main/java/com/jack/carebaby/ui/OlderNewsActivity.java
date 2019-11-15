package com.jack.carebaby.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.webkit.WebView;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BasePage;
import com.jack.carebaby.utils.WebViewFragmentUtil;

public class OlderNewsActivity extends BasePage {

    private WebView News_webview;
    private String url;
    private FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_news);
        
        News_webview=findViewById(R.id.News_webview);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        url="http://m.cctv.com/";
        News_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(News_webview);
        
    }
}
