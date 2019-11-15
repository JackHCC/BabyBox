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
import com.jack.carebaby.utils.BabyInfoAdapter;

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

public class BabyInfoShowActivity extends Activity {
    String url = Data.getUrl();
    String phone = Data.getPhone();
    private static final int COMPLETED = 0;
    private RecyclerView imglist;

    private FloatingActionButton fab;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_info_show);
        imglist = findViewById(R.id.recycler_baby);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BabyInfoShowActivity.this, BabyInfoAddActivity.class);
//                startActivity(new Intent("com.jack.carebaby.ui.BabyInfoAddActivity"));
                startActivity(intent);
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
                BabyInfoShowActivity.this.finish();
                break;
            // 悬浮按钮，新增账单
//            case R.id.fab:
//                startActivity(new Intent("com.jack.carebaby.ui.BabyInfoAddActivity"));
//                break;
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
        final List<String> content = new ArrayList<>();
        final List<String> time = new ArrayList<>();
        Log.d("Get", "click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url + "/share/get?phone=" + phone).build();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COMPLETED) {
                    BabyInfoAdapter babyinfoadapter;
                    GridLayoutManager layoutManager = new GridLayoutManager(BabyInfoShowActivity.this, 1);
                    imglist.setLayoutManager(layoutManager);
                    babyinfoadapter = new BabyInfoAdapter(id, name, content, time);
                    imglist.setAdapter(babyinfoadapter);
                    babyinfoadapter.notifyDataSetChanged();
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
                JSONArray jsonArray = jsonObject.getJSONArray("share");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject0 = jsonArray.getJSONObject(i);
                        id.add(jsonObject0.getString("_id"));
                        name.add(jsonObject0.getString("image"));
                        content.add(jsonObject0.getString("content"));
                        time.add(jsonObject0.getString("time"));
                    }
                    Message message = new Message();
                    message.what = COMPLETED;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
