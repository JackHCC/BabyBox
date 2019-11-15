package com.jack.carebaby.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;
import com.jack.carebaby.utils.datepicker.CustomDatePicker;
import com.jack.carebaby.utils.datepicker.DateFormatUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlanBillAddActivity extends Activity implements View.OnClickListener {
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimerPicker;
    String url = Data.getUrl();
    String phone = Data.getPhone();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_add_plan);

        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        initTimerPicker();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 时间选择器
            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
            // 返回
            case R.id.back:
                PlanBillAddActivity.this.finish();
                break;
            // 提交
            case R.id.forward:
                EditText title = (EditText)findViewById(R.id.txt_title);
                EditText price = (EditText)findViewById(R.id.txt_price);
                if(title.length()==0)
                    Toast.makeText(this, "请输入账单名称", Toast.LENGTH_SHORT).show();
                else if(price.length()==0)
                    Toast.makeText(this, "请输入账单金额", Toast.LENGTH_SHORT).show();
                else {
                    Upload();
                    PlanBillAddActivity.this.finish();
                }
                break;
        }
    }

    // 新增账单
    public void Upload(){
        EditText title = (EditText)findViewById(R.id.txt_title);
        EditText price = (EditText)findViewById(R.id.txt_price);
        Log.d("Upload","click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url+"/bill/add?phone="+phone
                +"&title="+title.getText()+"&date="+mTvSelectedTime.getText()
                +"&price="+price.getText()).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("UploadError", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                Looper.prepare();
                Toast.makeText(PlanBillAddActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimerPicker.onDestroy();
    }

    private void initTimerPicker() {
        String beginTime = "2000-1-1 00:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        mTvSelectedTime.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
}
