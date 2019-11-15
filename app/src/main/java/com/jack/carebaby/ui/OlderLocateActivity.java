package com.jack.carebaby.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BasePage;

public class OlderLocateActivity extends BasePage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_locate);

        Button btn_protect = findViewById(R.id.protect);
        //导航功能
        btn_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = getPackageManager().getLaunchIntentForPackage("com.autonavi.minimap");

                if (intent != null) {

                    startActivity(intent);
                } else {
                    // 没有安装要跳转的app应用，提醒一下
                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装高德地图吧~", Toast.LENGTH_LONG).show();
                }



            }
        });


    }
}
