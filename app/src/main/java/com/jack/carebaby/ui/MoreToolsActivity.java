package com.jack.carebaby.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.utils.ThreeDSlidingLayout;


public class MoreToolsActivity extends Activity {

    /**
     * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
     */
    private ThreeDSlidingLayout slidingLayout;

    /**
     * menu按钮，点击按钮展示左侧布局，再点击一次隐藏左侧布局。
     */
    private Button menuButton;

    /**
     * 放在content布局中的ListView。
     */
    private ListView contentListView;

    /**
     * 作用于contentListView的适配器。
     */
    private ArrayAdapter<String> contentListAdapter;

    /**
     * 用于填充contentListAdapter的数据源。
     */
    private String[] contentItems = {"微信", "设置", "日历",
            "闹钟", "记事本", "录音机", "计算器",
            "浏览器", "FM收音机", "音乐", "视频",
            "微博", "清理", "读书", "其他",
            "更多精彩"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tools);

        slidingLayout = (ThreeDSlidingLayout) findViewById(R.id.more_tools_slidingLayout);
        menuButton = (Button) findViewById(R.id.more_tools_menuButton);
        contentListView = (ListView) findViewById(R.id.more_tools_contentList);
        contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                contentItems);
        contentListView.setAdapter(contentListAdapter);
        // 将监听滑动事件绑定在contentListView上
        slidingLayout.setScrollEvent(contentListView);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidingLayout.isLeftLayoutVisible()) {
                    slidingLayout.scrollToRightLayout();
                } else {
                    slidingLayout.scrollToLeftLayout();
                }
            }
        });
        contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = contentItems[position];
                switch (position){
                    case 0:
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                        if (intent != null) {
                            startActivity(intent);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装微信哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 1:
                        Intent intent1 = getPackageManager().getLaunchIntentForPackage("com.android.settings");
                        if (intent1 != null) {
                            startActivity(intent1);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装设置哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 2:
                        Intent intent2 = getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
                        if (intent2 != null) {
                            startActivity(intent2);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装日历哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 3:
                        Intent intent3 = getPackageManager().getLaunchIntentForPackage("com.android.deskclock");
                        if (intent3 != null) {
                            startActivity(intent3);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装闹钟哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 4:
                        Intent intent4 = getPackageManager().getLaunchIntentForPackage("com.example.android.notepad");
                        if (intent4 != null) {
                            startActivity(intent4);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装记事本哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 5:
                        Intent intent5 = getPackageManager().getLaunchIntentForPackage("com.android.soundrecorder");
                        if (intent5 != null) {
                            startActivity(intent5);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装录音机哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 6:
                        Intent intent6 = getPackageManager().getLaunchIntentForPackage("com.android.calculator2");
                        if (intent6 != null) {
                            startActivity(intent6);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装计算器哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 7:
                        Intent intent7 = getPackageManager().getLaunchIntentForPackage("com.android.browser");
                        if (intent7 != null) {
                            startActivity(intent7);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装浏览器哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 8:
                        Intent intent8 = getPackageManager().getLaunchIntentForPackage("com.huawei.android.FMRadio");
                        if (intent8 != null) {
                            startActivity(intent8);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装收音机哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 9:
                        Intent intent9 = getPackageManager().getLaunchIntentForPackage("com.android.mediacenter");
                        if (intent9 != null) {
                            startActivity(intent9);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装音乐哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 10:
                        Intent intent10 = getPackageManager().getLaunchIntentForPackage("com.qiyi.video");
                        if (intent10 != null) {
                            startActivity(intent10);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装爱奇艺视频哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 11:

                        Intent intent11 = getPackageManager().getLaunchIntentForPackage("com.sina.weibo");
                        if (intent11 != null) {
                            startActivity(intent11);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装微博哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 12:

                        Intent intent12 = getPackageManager().getLaunchIntentForPackage("com.fantasmosoft.free_memory_recover");
                        if (intent12 != null) {
                            startActivity(intent12);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装清理哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;


                    case 13:

                        Intent intent13 = getPackageManager().getLaunchIntentForPackage("com.tencent.weread");
                        if (intent13 != null) {
                            startActivity(intent13);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装微信读书哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 14:

                        Intent intent14 = getPackageManager().getLaunchIntentForPackage("com.google.android.voicesearch");
                        if (intent14 != null) {
                            startActivity(intent14);
                        } else {
                            // 没有安装要跳转的app应用，提醒一下
                            Toast.makeText(getApplicationContext(), "还没安装语音搜索哟，检查一下吧~", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 15:

                        Intent intent15 = new Intent(MoreToolsActivity.this,ApplicationGetActivity.class);

                        startActivity(intent15);

                        break;
                }
                Toast.makeText(MoreToolsActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
