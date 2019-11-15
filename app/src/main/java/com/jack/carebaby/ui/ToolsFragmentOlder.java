package com.jack.carebaby.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BaseFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static cn.bgbsk.babycare.global.Data.messageText;
import static cn.bgbsk.babycare.global.Data.phoneNumber;

public class ToolsFragmentOlder extends BaseFragment{


    private Uri imageUri;
    public static File tempFile;

    //相机相关
    public static final int VIDEO_REQUEST = 0;// 录像
    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int CROP_PHOTO = 2;  //相册


    private Button Older_Tools_1;
    private Button Older_Tools_2;
    private Button Older_Tools_3;
    private Button Older_Tools_4;
    private Button Older_Tools_5;
    private Button Older_Tools_6;
    private Button Older_Tools_7;
    private Button Older_Tools_8;
    private Button Older_Tools_9;
    private Button Older_Tools_10;

    private TextView older_time;
    private TextView older_date;
    private TextView older_week;
    private TextView older_nongli;




    //日期获取
    Calendar calendar = Calendar.getInstance();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.older_fragment_tools, null);




        Older_Tools_1=v.findViewById(R.id.older_tools_1);
        Older_Tools_2=v.findViewById(R.id.older_tools_2);
        Older_Tools_3=v.findViewById(R.id.older_tools_3);
        Older_Tools_4=v.findViewById(R.id.older_tools_4);
        Older_Tools_5=v.findViewById(R.id.older_tools_5);
        Older_Tools_6=v.findViewById(R.id.older_tools_6);
        Older_Tools_7=v.findViewById(R.id.older_tools_7);
        Older_Tools_8=v.findViewById(R.id.older_tools_8);
        Older_Tools_9=v.findViewById(R.id.older_tools_9);
        Older_Tools_10=v.findViewById(R.id.older_tools_10);
        older_time=v.findViewById(R.id.older_time);
        older_date=v.findViewById(R.id.older_date);
        older_week=v.findViewById(R.id.older_week);
        older_nongli=v.findViewById(R.id.older_nongli);


        new TimeThread().start(); //启动新的线程展示时间

        older_nongli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),LunarCalendarActivity.class);
                startActivity(intent);
            }
        });

        Older_Tools_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),OlderTimealarmActivity.class);
                startActivity(intent);
            }
        });

        Older_Tools_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),OlderFlashlightActivity.class);
                startActivity(intent);
            }
        });


        Older_Tools_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),OlderFMActivity.class);
                startActivity(intent);
            }
        });

        Older_Tools_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),OlderNewsActivity.class);
                startActivity(intent);
            }
        });




        Older_Tools_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),OlderCCTVActivity.class);
                startActivity(intent);
            }
        });

        Older_Tools_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),OlderHealthActivity.class);
                startActivity(intent);
            }
        });

//        Button btn_protect = findViewById(R.id.older_tools_6);


        Older_Tools_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message(phoneNumber,messageText);
            }
        });

        Older_Tools_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(getActivity());
            }
        });




        Older_Tools_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),OlderLocateActivity.class);
                startActivity(intent);
            }
        });



        Older_Tools_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),OlderEatActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    long sysTime = System.currentTimeMillis();//获取系统时间
                    CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);//时间显示格式
                    CharSequence sysDateStr = DateFormat.format("MM月dd日", sysTime);//时间显示格式

                    @SuppressLint("WrongConstant") int week=calendar.get(Calendar.WEDNESDAY);

                    older_time.setText(sysTimeStr);
                    //older_date.setText(month+"月"+day+"日");
                    older_date.setText(sysDateStr);
                    older_week.setText("星期"+week);
                    break;
                default:
                    break;

            }
        }
    };


    //发短信
    private void message(String phone,String contents1){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);             //设置动作为发送短信
        intent.setData(Uri.parse("smsto:"+phone));           //设置发送的号码
        intent.putExtra("sms_body",contents1);           //设置发送的内容
        Toast.makeText(getActivity(), "跳转成功", Toast.LENGTH_SHORT).show();
        startActivity(intent);                               //启动 Activity
    }



    //打开相机
    public void openCamera(Activity activity) {
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                //检查是否有存储权限，以免崩溃
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    Toast.makeText(getActivity(), "请开启存储权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /*
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

}
