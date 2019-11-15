package com.jack.carebaby.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.jack.carebaby.R;
import com.jack.carebaby.utils.WebViewFragmentUtil;

public class OlderDialActivity extends AppCompatActivity {
    

    private WebView music_webview;
    private String url;
    private FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_dial);

        music_webview=findViewById(R.id.music_webview);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        url="https://jackhcc.github.io/WebMusic/";
        music_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(music_webview);


    }
}
