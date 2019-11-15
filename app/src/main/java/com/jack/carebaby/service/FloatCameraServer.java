package com.jack.carebaby.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.jack.carebaby.R;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;


public class FloatCameraServer extends Service {

    private WindowManager windowManager;
    private View view;

    private final String TAG = "floatcamera";

    public FloatCameraServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();





    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initView();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * FloatWindow使用FloatWindow库展示悬浮框；可拖拽
     */
    @SuppressLint("ResourceType")
    private void initView() {
        /*ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.mipmap.floatwindow_button);*/



        FloatWindow
                .with(getApplicationContext())
                .setView(LayoutInflater.from(this).inflate(R.layout.fragment_camera,null))
                //.setView(imageView)
                .setWidth(Screen.width,0.65f)                               //设置控件宽高
                .setHeight(Screen.height,0.35f)
                .setX(100)                                   //设置控件初始位置
                .setY(Screen.height,0.2f)
                .setMoveType(MoveType.active,-100,-100)
                .setDesktopShow(true)
                .setTag("floatcamera")//桌面显示
                .setViewStateListener(new ViewStateListener() {
                    @Override
                    public void onPositionUpdate(int i, int i1) {

                    }

                    @Override
                    public void onShow() {

                    }

                    @Override
                    public void onHide() {

                    }

                    @Override
                    public void onDismiss() {

                    }

                    @Override
                    public void onMoveAnimStart() {

                    }

                    @Override
                    public void onMoveAnimEnd() {

                    }

                    @Override
                    public void onBackToDesktop() {

                    }
                })
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }
                })
                .build();
        FloatWindow.get("floatcamera").show();

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatCameraServer.this, "跳转成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FloatCameraServer.this,CameraActivity.class);
                startActivity(intent);
            }
        });*/
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 啊，ByKill");
        //mHandler.removeCallbacksAndMessages(null);
        if (windowManager != null) windowManager.removeView(view);
        FloatWindow.destroy("floatcamera");
    }
}
