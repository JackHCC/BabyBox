package com.jack.carebaby.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.jack.carebaby.R;
import com.jack.carebaby.base.BasePage;
import com.jack.carebaby.entity.TabEntity;
import com.jack.carebaby.utils.ViewFindUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.bgbsk.babycare.global.Data.boxsStatus;
import static cn.bgbsk.babycare.global.Data.phoneNumber;

public class HomePage extends BasePage {

    // MQTT 客户端
    private MqttAndroidClient mqttAndroidClient;
    // 连接 ActiveMQ 的URI
    private String serverUri = "tcp://bgbsk.cn:1883";
    // 客户端 ID，用以识别客户端
    private String clientId = "camera_client"+Math.random()*100+Math.random()*100;


    private Context mContext = this;

    /**
     * 传感器
     */
    private SensorManager sensorManager;
    private HomePage.ShakeSensorListener shakeListener;

    /**
     * 判断一次摇一摇动作
     */
    private boolean isShake = false;

    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION};



    //babybox模式和older模式切换
    private RelativeLayout babyboxs;
    private RelativeLayout olderboxs;

    private SharedPreferences loginSP;
    private SharedPreferences.Editor loginEdit;
    private String url = Data.getUrl();



    /**婴儿模式*/
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private String[] mTitles = { "数据单","生态圈", "个人中心"};

    private int[] mIconUnselectIds = {
            R.mipmap.tab_more_unselect, R.mipmap.tab_home_unselect,/*R.mipmap.tab_speech_unselect,*/
            R.mipmap.tab_contact_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_more_select, R.mipmap.tab_home_select, /*R.mipmap.tab_speech_select,*/
            R.mipmap.tab_contact_select };
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private ViewPager mViewPager;
    private CommonTabLayout TabLayout_Home;



    /**老人模式*/
    private ArrayList<Fragment> mFragments_older = new ArrayList<>();
    private String[] mTitles_older = { "老人工具","监控页", "个人中心"};
    private int[] mIconUnselectIds_older = {
            R.mipmap.tab_more_unselect,R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect};
    private int[] mIconSelectIds_older = {
            R.mipmap.tab_more_select,R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select };
    private ArrayList<CustomTabEntity> mTabEntities_older = new ArrayList<>();
    private View mDecorView_older;
    private ViewPager mViewPager_older;
    private CommonTabLayout TabLayout_Home_older;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //初始化组件
        initView();

        initFragment();

        initFragmentOlder();

        connect();

        /*摇一摇相关*/
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new HomePage.ShakeSensorListener();

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int l = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            int m = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]);
            int n = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[3]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED || l != PackageManager.PERMISSION_GRANTED || m != PackageManager.PERMISSION_GRANTED ||
                    n != PackageManager.PERMISSION_GRANTED) { startRequestPermission();}}

        // 如果没有授予该权限，就去提示用户请求


        /**Babyboxs页面初始化设置*/

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.home_viewpage);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout_Home = ViewFindUtils.find(mDecorView, R.id.home_tablayout);

        Home_Select();
        //两位数
        TabLayout_Home.showMsg(0, 6);
        TabLayout_Home.setMsgMargin(0, -5, 5);

        //设置未读消息红点
        TabLayout_Home.showDot(1);
        MsgView rtv_2_2 = TabLayout_Home.getMsgView(2);
        if (rtv_2_2 != null) {
            UnreadMsgUtils.setSize(rtv_2_2, dp2px(7.5f));
        }

        /*//设置未读消息背景
        TabLayout_Home.showMsg(3, 5);
        TabLayout_Home.setMsgMargin(3, 0, 5);
        MsgView rtv_2_3 = TabLayout_Home.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }*/


        /**Olderboxs页面设置*/

        for (int i = 0; i < mTitles_older.length; i++) {
            mTabEntities_older.add(new TabEntity(mTitles_older[i], mIconSelectIds_older[i], mIconUnselectIds_older[i]));
        }

        mDecorView_older = getWindow().getDecorView();
        mViewPager_older = ViewFindUtils.find(mDecorView_older, R.id.olderboxs_home_viewpage);
        mViewPager_older.setAdapter(new MyPagerAdapter_older(getSupportFragmentManager()));

        TabLayout_Home_older = ViewFindUtils.find(mDecorView_older, R.id.olderboxs_home_tablayout);

        Home_Select_older();
        /*//两位数
        TabLayout_Home_older.showMsg(0, 6);
        TabLayout_Home_older.setMsgMargin(0, -5, 5);

        //设置未读消息红点
        TabLayout_Home_older.showDot(1);
        MsgView rtv_2_2_older = TabLayout_Home_older.getMsgView(2);
        if (rtv_2_2_older != null) {
            UnreadMsgUtils.setSize(rtv_2_2, dp2px(7.5f));
        }*/

        loginSP = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        if (loginSP.getString("phone",null)!= null){
            String phone = loginSP.getString("phone",null);
            String password = loginSP.getString("password",null);

            Map map = new HashMap();
            map.put("phone", phone);
            map.put("password", password);
            String param = JSON.toJSONString(map);
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            final RequestBody requestBody = RequestBody.create(param, mediaType);
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder().post(requestBody).url(url + "/user/login").build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("LoginError", e.getMessage());
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    int status = jsonObject.getInteger("status");

                    if (status == 200) {
                        Data.setPhone(jsonObject.getString("phone"));
                        Data.setUsername(jsonObject.getString("username"));
                        Data.setImg(jsonObject.getString("imgUrl"));
                        Data.setPhoneNumber(jsonObject.getString("emergency"));
                        Data.setCreated(jsonObject.getTimestamp("created"));
                        Data.setRegisterTime(jsonObject.getString("created"));
                        Data.setLoginStatus(1);
                        //这里添加登录成功相关东西
                    }

                }
            });

        }

    }


    private void initView(){
        babyboxs=findViewById(R.id.babyboxs);
        olderboxs=findViewById(R.id.olderboxs);
    }


    /**
     * 开始提交请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    /**
     * 用户权限 申请 的回调方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                } else {
                    //获取权限成功提示，可以不要
                    Toast toast = Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
    }

    //actionbar添加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //监听菜单点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.actionbar:
                //模式切换
                if (!boxsStatus){
                    olderboxs.setVisibility(View.INVISIBLE);
                    babyboxs.setVisibility(View.VISIBLE);

                    boxsStatus=!boxsStatus;
                    Toast.makeText(this, "切换为婴儿看照模式", Toast.LENGTH_SHORT).show();

                }
                else{
                    olderboxs.setVisibility(View.VISIBLE);
                    babyboxs.setVisibility(View.INVISIBLE);

                    boxsStatus=!boxsStatus;
                    Toast.makeText(this, "切换为老人照顾模式", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.notes:
                Intent intent=new Intent(HomePage.this,com.memorandum.MainActivity.class);
                startActivity(intent);
                break;

            case R.id.help:
                Intent intent2=new Intent(this,HelpAllActivity.class);
                startActivity(intent2);
                break;

            case R.id.setting:
                Intent intent3=new Intent(this,SettingActivity.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_title_body_4:
                if(Data.getLoginStatus()==1)
                    startActivity(new Intent("com.jack.carebaby.ui.PlanBillActivity"));
                else
                    Toast.makeText(this, "请先登录", Toast.LENGTH_LONG).show();
                break;


        }
    }

    public void initFragment(){
        mFragments.add(new DataShowFragment()); //数据单
        mFragments.add(new SystemFragment()); //生态圈
        mFragments.add(new PersonFragment());//个人中心

    }

    public void initFragmentOlder(){

        mFragments_older.add(new ToolsFragmentOlder());
        mFragments_older.add(new CameraFragmentOlder());

        mFragments_older.add(new PersonFragmentOlder());//个人中心

    }

    Random mRandom = new Random();

    private void Home_Select() {
        TabLayout_Home.setTabData(mTabEntities);
        TabLayout_Home.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    TabLayout_Home.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(TabLayout_Home.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout_Home.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
    }


    private void Home_Select_older() {
        TabLayout_Home_older.setTabData(mTabEntities_older);
        TabLayout_Home_older.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager_older.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    TabLayout_Home_older.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(TabLayout_Home.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager_older.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout_Home_older.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager_older.setCurrentItem(0);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


    private class MyPagerAdapter_older extends FragmentPagerAdapter {
        public MyPagerAdapter_older(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments_older.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles_older[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments_older.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**设置震动*/
    @Override
    protected void onResume() {
        //注册监听加速度传感器
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    protected void onPause() {
        /**
         * 资源释放
         */
        sensorManager.unregisterListener(shakeListener);
        super.onPause();
    }

    private class ShakeSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //避免一直摇
            if (isShake) {
                return;
            }
            float[] values = event.values;
            /*
             * x : x轴方向的重力加速度，向右为正
             * y : y轴方向的重力加速度，向前为正
             * z : z轴方向的重力加速度，向上为正
             */
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            float z = Math.abs(values[2]);
            //加速度超过72，摇一摇成功
            if (x > 72 || y > 72 || z > 72) {
                isShake = true;
                //播放声音
                //playSound(MainTabActivity.this);
                //震动，注意权限
                vibrate( 500);
                //仿网络延迟操作，这里可以去请求服务器...
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //弹框
                        showDialog();
                    }
                },1000);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    private void showDialog() {

        call(phoneNumber);

        isShake = false;
    }

    //打电话
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }


    private void connect() {

        mqttAndroidClient = new MqttAndroidClient(HomePage.this, serverUri, clientId);

//        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    // 自动重连
                } else {
                    // 已连接
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
            }

            @SuppressLint("WrongConstant")
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                switch (topic) {
                    case "ip":{
                        Data.setCameraIP(message.toString());
                        break;
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    // 连接成功
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

                    // 订阅主题
                    subscribeToTopic("ip");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    exception.printStackTrace();
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }


    private void subscribeToTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 1, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    exception.printStackTrace();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
