package com.jack.carebaby.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BasePage;
import com.jack.carebaby.utils.WebViewFragmentUtil;

public class OlderFMActivity extends BasePage {

    private WebView FM_webview;
    private String url;

    private Button button_1;
    private Button button_2;
    private Button button_3;
    private Button button_4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_fm);

        button_1=findViewById(R.id.button_1);
        button_2=findViewById(R.id.button_2);
        button_3=findViewById(R.id.button_3);
        button_4=findViewById(R.id.button_4);


        FM_webview=findViewById(R.id.FM_webview);


        url="https://m.tingtingfm.com/";
        FM_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(FM_webview);



        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url="https://m.tingtingfm.com/";
                FM_webview.loadUrl(url);
                //Toast.makeText(, "加载听听FM...", Toast.LENGTH_LONG).show();


            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url="https://m.ximalaya.com/";
                FM_webview.loadUrl(url);

            }
        });

        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url="http://m.lrts.me/";
                FM_webview.loadUrl(url);

            }
        });

        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


    }
}
