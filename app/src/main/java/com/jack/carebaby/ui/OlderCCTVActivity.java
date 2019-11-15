package com.jack.carebaby.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.jack.carebaby.R;
import com.jack.carebaby.utils.WebViewFragmentUtil;

public class OlderCCTVActivity extends AppCompatActivity {

    private WebView CCTV_webview;
    private String url;
    private FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_cctv);

        CCTV_webview=findViewById(R.id.CCTV_webview);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        url="http://tv.cctv.com/live/m/?spm=C28340.P4oOHksoF7nA.EexWRG5WBDFS.2";
        CCTV_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(CCTV_webview);


    }
}
