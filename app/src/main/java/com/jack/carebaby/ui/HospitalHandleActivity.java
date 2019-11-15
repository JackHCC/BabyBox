package com.jack.carebaby.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BasePage;
import com.jack.carebaby.utils.WebViewFragmentUtil;

public class HospitalHandleActivity extends BasePage {

    private WebView handle_webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_handle);

       handle_webview=findViewById(R.id.handle_webview);

        url="http://wisdom.120.net/m";
       handle_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(handle_webview);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.handle_menu,menu);

        return true;
    }

    //监听菜单点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.handle1:
                url="http://wapjbk.39.net/";
               handle_webview.loadUrl(url);
                Toast.makeText(this, "加载诊断源1...", Toast.LENGTH_LONG).show();


                break;

            case R.id.handle2:

                url="https://mjbk.familydoctor.com.cn/conditions_57.html";
               handle_webview.loadUrl(url);
                Toast.makeText(this, "加载诊断源2...", Toast.LENGTH_LONG).show();

                break;

            case R.id.handle4:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
