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

public class DailyEatActivity extends BasePage {

    private WebView eat_webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_eat);

        eat_webview=findViewById(R.id.eat_webview);

        url="https://m.meishij.net/";
        eat_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(eat_webview);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.eat_menu,menu);

        return true;
    }

    //监听菜单点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.eat1:
                url="https://m.xinshipu.com/";
                eat_webview.loadUrl(url);
                Toast.makeText(this, "加载菜谱推荐...", Toast.LENGTH_LONG).show();


                break;

            case R.id.eat2:

                url="https://m.meishij.net/caipudaquan/";
                eat_webview.loadUrl(url);
                Toast.makeText(this, "加载菜谱分类...", Toast.LENGTH_LONG).show();

                break;

            case R.id.eat3:
                url="https://m.xinshipu.com/jiachangzuofa/110056/";
                eat_webview.loadUrl(url);
                Toast.makeText(this, "加载宝宝食谱...", Toast.LENGTH_LONG).show();

                break;

            case R.id.eat4:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
