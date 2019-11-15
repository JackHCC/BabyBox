package com.jack.carebaby.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jack.carebaby.R;

public class ApplicationGetActivity extends AppCompatActivity {

    //PackageManager packageManager =getPackageManager();

    private Button getApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_get);

        initView();

        /*getApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent =packageManager.getLaunchIntentForPackage("com.tencent.mm");
                if(intent==null){
                    Toast.makeText(ApplicationGetActivity.this, "该手机暂不支持该功能", Toast.LENGTH_LONG).show();
                }else{
                    startActivity(intent);
                }


            }
        });*/
    }


    private void initView(){
        getApp=findViewById(R.id.getApp);

    }
}
