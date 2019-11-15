package com.jack.carebaby.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;
import com.jack.carebaby.utils.ImgAdapter;

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

public class ImgShowActivity extends Activity {
    String url = Data.getUrl();
    String phone = Data.getPhone();
    private static final int COMPLETED = 0;
    private RecyclerView imglist;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_show);
        imglist = findViewById(R.id.recyclerview);

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
                ImgShowActivity.this.finish();
                break;
            // 刷新页面
            case R.id.refresh:
                Get();
                break;
        }
    }

    // 获取数据库信息
    public void Get() {
        final List<String> name = new ArrayList<>();
        Log.d("Get", "click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url + "/share/get?phone=" + phone).build();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COMPLETED) {
                    ImgAdapter imgadapter;
                    GridLayoutManager layoutManager = new GridLayoutManager(ImgShowActivity.this, 2);
                    imglist.setLayoutManager(layoutManager);
                    imgadapter = new ImgAdapter(name);
                    imglist.setAdapter(imgadapter);
                    imgadapter.notifyDataSetChanged();
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
                        name.add(jsonObject0.getString("image"));
                    }
                    Message message = new Message();
                    message.what = COMPLETED;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
