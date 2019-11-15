package com.jack.carebaby.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddBabyActivity extends Activity implements View.OnClickListener{

    private TextView birthday;
    private CustomDatePicker mTimerPicker;
    String url = Data.getUrl();
    String phone = Data.getPhone();

    private boolean man_selected=false;
    private boolean woman_selected=false;
    ImageView manicon,womanicon;
    TextView mantext,womantext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_add);

        manicon=(ImageView) findViewById(R.id.man_ic);
        womanicon=(ImageView) findViewById(R.id.woman_ic);
        mantext=(TextView) findViewById(R.id.man_tx);
        womantext=(TextView) findViewById(R.id.woman_tx);
        findViewById(R.id.ll_time).setOnClickListener(this);
        birthday = findViewById(R.id.tv_selected_time);
        initTimerPicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 时间选择器
            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(birthday.getText().toString());
                break;
            // 返回
            case R.id.back:
                AddBabyActivity.this.finish();
                break;
            // 提交
            case R.id.forward:
                EditText name = (EditText)findViewById(R.id.txt_name);
                if(name.length()==0)
                    Toast.makeText(this, "请输入宝宝姓名", Toast.LENGTH_SHORT).show();
                else if(getstatu()==0)
                    Toast.makeText(this, "请选择宝宝性别", Toast.LENGTH_SHORT).show();
                else {
                    Upload();
                    AddBabyActivity.this.finish();
                }
                break;
            case R.id.ll_male:
                switch (getstatu())
                {
                    case 0:
                    case 2:
                        turnToMan();
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
                break;
            case R.id.ll_female:
                switch (getstatu())
                {
                    case 0:
                    case 1:
                        turnToWoman();
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    public void Upload(){
        EditText name = (EditText)findViewById(R.id.txt_name);
        String sex = "";
        switch (getstatu())
        {
            case 0:
            case 1:
                sex = "男";
                break;
            case 2:
                sex = "女";
                break;
            default:
                break;
        }
        Log.d("Upload","click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url+"/baby/add?phone="+phone
                +"&name="+name.getText()+"&birthday="+birthday.getText()
                +"&sex="+sex).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("UploadError", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                Looper.prepare();
                Toast.makeText(AddBabyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
    }

    private void initTimerPicker() {
        String beginTime = "2000-1-1 00:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        birthday.setText(DateFormatUtils.long2Str(System.currentTimeMillis(), false));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                birthday.setText(DateFormatUtils.long2Str(timestamp, false));
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

    public void turnToWoman()
    {
        manicon.setImageResource(R.mipmap.male);
        mantext.setTextColor(Color.rgb(195,195,195));
        womanicon.setImageResource(R.mipmap.female_choose);
        womantext.setTextColor(Color.rgb(68,68,68));
        man_selected=false;
        woman_selected=true;
    }
    public void turnToMan()
    {
        manicon.setImageResource(R.mipmap.male_choose);
        mantext.setTextColor(Color.rgb(68,68,68));
        womanicon.setImageResource(R.mipmap.female);
        womantext.setTextColor(Color.rgb(195,195,195));
        man_selected=true;
        woman_selected=false;
    }

    //判断目前状态
    public int getstatu()
    {
        //均未选择
        if (!man_selected&&!woman_selected)
        {
            return 0;
        }
        else if(man_selected&&!woman_selected)   //选男
        {
            return 1;
        }
        else if(!man_selected&&woman_selected)  //选女
        {
            return 2;
        }
        else    //均选择 不可能发生
        {
            return -1;
        }
    }
}
