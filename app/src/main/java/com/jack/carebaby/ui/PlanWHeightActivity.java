package com.jack.carebaby.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;
import com.jack.carebaby.utils.BabyWHeightAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlanWHeightActivity extends Activity {
    String url = Data.getUrl();
    String phone = Data.getPhone();
    private static final int COMPLETED = 0;
    private FloatingActionButton fab;
    private RecyclerView babylist;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_wheight);
        fab = findViewById(R.id.fab);
        babylist = findViewById(R.id.recycler_baby);

        //下拉刷新
        final RefreshLayout refreshLayout = findViewById(R.id.bill_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2400/*,false*/);//传入false表示刷新失败


            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        Get();
    }


    public void onResume(){
        super.onResume();
        Get();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.back:
                PlanWHeightActivity.this.finish();
                break;
            // 悬浮按钮，新增账单
            case R.id.fab:
                startActivity(new Intent("com.jack.carebaby.ui.PlanWHeightAddActivity"));
                break;
            // 刷新页面
            case R.id.refresh:
                Get();
                break;
        }
    }

    // 获取数据库信息
    public void Get() {
        final List<String> id = new ArrayList<>();
        final List<String> name = new ArrayList<>();
        final List<String> birthday = new ArrayList<>();
        final List<String> sex = new ArrayList<>();
        final List<String> imgurl = new ArrayList<>();
        final JSONArray jsonArray = new JSONArray();
        Log.d("Get", "click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url + "/baby/get?phone=" + phone).build();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COMPLETED) {
                    BabyWHeightAdapter babyeheightadapter;
                    GridLayoutManager layoutManager = new GridLayoutManager(PlanWHeightActivity.this, 1);
                    babylist.setLayoutManager(layoutManager);
                    babyeheightadapter = new BabyWHeightAdapter(id, name, birthday, sex, imgurl);
                    babylist.setAdapter(babyeheightadapter);
                    babyeheightadapter.notifyDataSetChanged();
                }
            }
        };
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("GetError", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                JSONArray jsonArray = jsonObject.getJSONArray("babys");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject0 = jsonArray.getJSONObject(i);
                        id.add(jsonObject0.getString("_id"));
                        name.add(jsonObject0.getString("name"));
                        birthday.add(jsonObject0.getString("birthday"));
                        sex.add(jsonObject0.getString("sex"));
                        if(jsonObject0.getString("imgUrl") == null){
                            imgurl.add("0");
                        }
                        else
                            imgurl.add(jsonObject0.getString("imgUrl"));
                    }
                    Message message = new Message();
                    message.what = COMPLETED;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
