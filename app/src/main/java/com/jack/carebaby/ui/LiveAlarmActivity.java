package com.jack.carebaby.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BasePage;

public class LiveAlarmActivity extends BasePage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_alarm);

        Button btn_alarm = findViewById(R.id.button);
        //导航功能
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.deskclock");

                if (intent != null) {

                    startActivity(intent);
                } else {
                    // 没有安装要跳转的app应用，提醒一下
                    Toast.makeText(getApplicationContext(), "闹钟出了点问题，检查一下闹钟吧~", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
