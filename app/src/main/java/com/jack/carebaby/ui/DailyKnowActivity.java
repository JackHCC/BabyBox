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

public class DailyKnowActivity extends AppCompatActivity {

    private WebView know_webview;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_know);

        know_webview=findViewById(R.id.know_webview);

        url="http://www.jiankang123.net/";
        know_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(know_webview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.know_menu,menu);

        return true;
    }

    //监听菜单点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.know1:
                url="http://dtxinshenghuo.com/";
                know_webview.loadUrl(url);
                Toast.makeText(this, "加载生活百科...", Toast.LENGTH_LONG).show();


                break;

            case R.id.know2:

                url="http://www.8585.online/";
                know_webview.loadUrl(url);
                Toast.makeText(this, "加载生活技巧...", Toast.LENGTH_LONG).show();

                break;

            case R.id.know3:
                url="http://www.bkzx.cn/";
                know_webview.loadUrl(url);
                Toast.makeText(this, "加载百科在线...", Toast.LENGTH_LONG).show();

                break;

            case R.id.know4:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
