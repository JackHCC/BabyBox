package com.jack.carebaby.service;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.ui.CameraActivity;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;


public class FloatWindowServer extends Service {

    private WindowManager windowManager;
    private View view;

    private final String TAG = "floatwindow";

    public FloatWindowServer() {
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
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.mipmap.floatwindow_button);



        FloatWindow
                .with(getApplicationContext())
                //.setView(LayoutInflater.from(this).inflate(R.layout.fragment_person,null))
                .setView(imageView)
                .setWidth(Screen.width,0.15f)                               //设置控件宽高
                .setHeight(Screen.height,0.15f)
                .setX(100)                                   //设置控件初始位置
                .setY(Screen.height,0.3f)
                .setMoveType(MoveType.active,-100,-100)
                .setDesktopShow(true)                        //桌面显示
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
        FloatWindow.get().show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatWindowServer.this, "跳转成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FloatWindowServer.this,CameraActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 啊，ByKill");
        //mHandler.removeCallbacksAndMessages(null);
        if (windowManager != null) windowManager.removeView(view);
        FloatWindow.destroy();
    }
}
