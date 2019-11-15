package com.jack.carebaby.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.utils.WebViewFragmentUtil;

public class HospitalAskActivity extends AppCompatActivity {
    
    private WebView ask_webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_ask);
        ask_webview=findViewById(R.id.ask_webview);

        url="https://m.120ask.com/list/erke/";
        ask_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(ask_webview);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.ask_menu,menu);

        return true;
    }

    //监听菜单点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.ask1:
                url="https://m.120ask.com/";
                ask_webview.loadUrl(url);
                Toast.makeText(this, "加载问答源1...", Toast.LENGTH_LONG).show();


                break;

            case R.id.ask2:

                url="http://gjk120.com/";
                ask_webview.loadUrl(url);
                Toast.makeText(this, "加载问答源2...", Toast.LENGTH_LONG).show();

                break;

            case R.id.ask3:

                url="https://community.kankanyisheng.com/#/index/questions";
                ask_webview.loadUrl(url);
                Toast.makeText(this, "加载问答源3...", Toast.LENGTH_LONG).show();

                break;   

            case R.id.ask4:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
