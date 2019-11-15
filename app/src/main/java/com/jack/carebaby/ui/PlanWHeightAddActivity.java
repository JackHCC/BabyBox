package com.jack.carebaby.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;
import com.jack.carebaby.utils.datepicker.CustomDatePicker;
import com.jack.carebaby.utils.datepicker.DateFormatUtils;
import com.lsp.RulerView;

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

public class PlanWHeightAddActivity extends AppCompatActivity implements View.OnClickListener{
    private RulerView heightview;
    private RulerView weightview;
    private CustomDatePicker mTimerPicker;
    private TextView time;
    private TextView babyname;
    private AlertDialog alertDialog2;
    private int index;
    String height;
    String weight;
    String bid;

    private static final int COMPLETED = 0;
    String url = Data.getUrl();
    String phone = Data.getPhone();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_wheight_add);
        findViewById(R.id.ll_time).setOnClickListener(this);
        time = findViewById(R.id.tv_selected_time);
        babyname = (TextView)findViewById(R.id.tv_selected_baby);
        initTimerPicker();
        heightview = (RulerView) findViewById(R.id.height);
        weightview = (RulerView) findViewById(R.id.weight);

        heightview.setOnChooseResulterListener(new RulerView.OnChooseResulterListener() {
            @Override
            public void onEndResult(String result) {
                height = result;
            }

            @Override
            public void onScrollResult(String height) {
            }
        });

        weightview.setOnChooseResulterListener(new RulerView.OnChooseResulterListener() {
            @Override
            public void onEndResult(String result) {
                weight = result;
            }

            @Override
            public void onScrollResult(String height) {
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // 时间选择器
            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(time.getText().toString());
                break;
            // 返回
            case R.id.back:
                PlanWHeightAddActivity.this.finish();
                break;
            // 提交
            case R.id.forward:
                if(bid==null)
                    Toast.makeText(this, "请选择宝宝", Toast.LENGTH_SHORT).show();
                else if(weight=="0.0")
                    Toast.makeText(this, "请输入体重", Toast.LENGTH_SHORT).show();
                else if(height=="0.0")
                    Toast.makeText(this, "请输入身高", Toast.LENGTH_SHORT).show();
                else {
                    Upload();
                    PlanWHeightAddActivity.this.finish();
                }
                break;
            case R.id.person_content_baby:
                showSingleAlertDialog(v);
                break;
        }
    }

    private void initTimerPicker() {
        String beginTime = "2000-1-1 00:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        time.setText(DateFormatUtils.long2Str(System.currentTimeMillis(), false));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                time.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(false);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    public void showSingleAlertDialog(View view){
        final List<String> ids = new ArrayList<>();
        final List<String> names = new ArrayList<>();
        String url = Data.getUrl();
        String phone = Data.getPhone();
        Log.d("Get","click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url+"/baby/get?phone="+phone).build();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COMPLETED) {
                    final String[] name = names.toArray(new String[names.size()]);
                    final String[] id = ids.toArray(new String[ids.size()]);
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PlanWHeightAddActivity.this);
                    alertBuilder.setTitle("请选择宝宝");
                    alertBuilder.setSingleChoiceItems(name, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            index = i;
                        }
                    });

                    alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            babyname.setText(name[index]);
                            bid = id[index];
                            alertDialog2.dismiss();
                        }
                    });

                    alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog2.dismiss();
                        }
                    });

                    alertDialog2 = alertBuilder.create();
                    alertDialog2.show();
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
                if (jsonArray!=null){
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject0 = jsonArray.getJSONObject(i);
                        ids.add(jsonObject0.getString("_id"));
                        names.add(jsonObject0.getString("name"));
                    }
                    Message message = new Message();
                    message.what = COMPLETED;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public void Upload(){
        Log.d("Upload","click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url+"/baby/data/add?phone="+phone
                +"&id="+bid+"&time="+time.getText()
                +"&weight="+weight+"&height="+height).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("UploadError", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                Looper.prepare();
                Toast.makeText(PlanWHeightAddActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
    }
}
