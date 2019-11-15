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

public class DailyTripActivity extends BasePage {

    private WebView trip_webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_trip);

        trip_webview=findViewById(R.id.trip_webview);

        url="https://m.ctrip.com/html5/?sourceid=497&allianceid=4897&sid=182042&sepopup=150";
        trip_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();
        webViewFragmentUtil.WebViewUtil(trip_webview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.trip_menu,menu);

        return true;
    }

    //监听菜单点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.trip1:
                url="https://m.tuniu.com/?p=14820&cmpid=mkt_03033903&utm_source=m.baidu.com&utm_campaign=baidupz&utm_medium=baidupz";
                trip_webview.loadUrl(url);
                Toast.makeText(DailyTripActivity.this, "加载途牛...", Toast.LENGTH_LONG).show();


                break;

            case R.id.trip2:

                url="https://m.mafengwo.cn/";
                trip_webview.loadUrl(url);
                Toast.makeText(DailyTripActivity.this, "加载马蜂窝...", Toast.LENGTH_LONG).show();

                break;

            case R.id.trip3:
                url="https://m.qyer.com/";
                trip_webview.loadUrl(url);
                Toast.makeText(DailyTripActivity.this, "加载穷游...", Toast.LENGTH_LONG).show();

                break;

            case R.id.trip4:
                url="https://m.ly.com/";
                trip_webview.loadUrl(url);
                Toast.makeText(DailyTripActivity.this, "加载同城...", Toast.LENGTH_LONG).show();

                break;
            case R.id.trip5:
                url="https://m.lvmama.com/?losc=019926&utm_source=baidu&utm_medium=zhuanqu&utm_campaign=wap";
                trip_webview.loadUrl(url);
                Toast.makeText(DailyTripActivity.this, "加载驴妈妈...", Toast.LENGTH_LONG).show();


                break;
            case R.id.trip6:
                DailyTripActivity.this.finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
