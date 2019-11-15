package com.jack.carebaby.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;
import com.jack.carebaby.base.BaseFragment;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import cn.bgbsk.babycare.global.Data;


public class DataShowFragment extends BaseFragment {


    int Vbra[] = new int[10]; /*开辟了一个长度为10的数组*/
    int new_Vbra[] = new int[10]; /*开辟了一个长度为10的数组*/

    int Person[] = new int[10]; /*开辟了一个长度为10的数组*/
    int new_Person[] = new int[10]; /*开辟了一个长度为10的数组*/

    private static final String TAG = "MQTT";

    // MQTT 客户端
    private MqttAndroidClient mqttAndroidClient;
    // 连接 ActiveMQ 的URI
    private String serverUri = "tcp://bgbsk.cn:1883";
    // 客户端 ID，用以识别客户端
    private String clientId = "android_sample_"+Math.random()*100+Math.random()*100;

    //声明一个振动器对象
    private Vibrator mVibrator;



    private TextView main_cry;   //是否在哭
    private TextView main_status;      //婴儿状态
    private TextView main_others;   //是否有人靠近
    private TextView main_time;   //检测时间
    private TextView main_temp;   //检测温度
    private TextView main_temp_f;   //检测温度
    private TextView main_temp_level;   //检测温度等级
    private TextView main_humid;   //检测湿度
    private TextView main_humid_level;   //检测湿度等级
    private TextView main_light;   //检测光强
    private TextView main_light_level;   //检测光强等级
    private TextView main_vbra;   //检测光强等级


    private TextView datashow_help;

    private ImageView Camera_show;
    private ImageView Datashow_disconect_setting;
    private ImageView datashow_disconnect_help;

    private RelativeLayout Datashow_disconnect;
    private ScrollView Datashow_connect;



    //动画
    private AVLoadingIndicatorView baby_status_anim;
    private AVLoadingIndicatorView baby_status_anim_sleep;

    private CircularFillableLoaders circularFillableLoaders;
    private CircularFillableLoaders circularFillableLoaders_2;
    private CircularFillableLoaders circularFillableLoaders_3;

    int PROGRESS_TEMP = 68; //CircularFillableLoaders波浪起伏
    int PROGRESS_HUMID = 52; //CircularFillableLoaders波浪起伏
    int PROGRESS_LIGHT = 90; //CircularFillableLoaders波浪起伏


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_datashow, null);
        //获取组件


        main_cry = v.findViewById(R.id.baby_status_cry);
        main_status = v.findViewById(R.id.baby_status_boolean);
        main_others = v.findViewById(R.id.baby_status_others);
        main_time = v.findViewById(R.id.baby_status_time);
        main_temp = v.findViewById(R.id.main_data_temp);
        main_temp_f = v.findViewById(R.id.main_data_temp_f);
        main_temp_level = v.findViewById(R.id.main_data_temp_level);
        main_humid = v.findViewById(R.id.main_data_humid);
        main_humid_level = v.findViewById(R.id.main_data_humid_level);
        main_light = v.findViewById(R.id.main_data_light);
        main_light_level = v.findViewById(R.id.main_data_light_level);
        main_vbra = v.findViewById(R.id.baby_status_vibra);

        datashow_help=v.findViewById(R.id.main_middle_top_help);
        Camera_show=v.findViewById(R.id.baby_status_camera);
        Datashow_disconnect=v.findViewById(R.id.datashow_disconnect);
        Datashow_connect=v.findViewById(R.id.datashow_connect);
        Datashow_disconect_setting=v.findViewById(R.id.datashow_disconnect_setting);
        datashow_disconnect_help=v.findViewById(R.id.datashow_disconnect_help);

        circularFillableLoaders = v.findViewById(R.id.main_circle_temp);
        circularFillableLoaders_2 = v.findViewById(R.id.main_circle_humid);
        circularFillableLoaders_3 = v.findViewById(R.id.main_circle_light);

        circularFillableLoaders.setProgress(PROGRESS_TEMP);
        circularFillableLoaders.setAmplitudeRatio((float) PROGRESS_TEMP / 1000);
        circularFillableLoaders_2.setProgress(PROGRESS_HUMID);
        circularFillableLoaders_2.setAmplitudeRatio((float) PROGRESS_HUMID / 1000);
        circularFillableLoaders_3.setProgress(PROGRESS_LIGHT);
        circularFillableLoaders_3.setAmplitudeRatio((float) PROGRESS_LIGHT / 1000);


        baby_status_anim = v.findViewById(R.id.baby_status_anim);
        baby_status_anim_sleep = v.findViewById(R.id.baby_status_anim_sleep);
        stopAnim();
        startAnim_sleep();

        //下拉刷新
        final RefreshLayout refreshLayout = v.findViewById(R.id.datashow_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2400/*,false*/);//传入false表示刷新失败
            }
        });

        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()).setEnableHorizontalDrag(true));


        for(int i=0;i<10;i++){
            Vbra[i]=0;
        }
        for(int i=0;i<10;i++){
            Person[i]=0;
        }


        //查看说明
        datashow_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog2();
            }
        });

        //跳转摄像头
        Camera_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });


        //跳转设置
        Datashow_disconect_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        //跳转说明
        datashow_disconnect_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });


        /** 悬浮按钮*/

        BoomMenuButton bmb = v.findViewById(R.id.bmb_a);

        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.main_jiankong)
                .normalText("进入监控图像！")
                .imageRect(new Rect(35, 30, Util.dp2px(50), Util.dp2px(50)))
                .subNormalText("远程观察婴儿的实时状态～")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(getActivity(), CameraActivity.class);
                        startActivity(intent);
                    }
                });

        bmb.addBuilder(builder);

        HamButton.Builder builder3 = new HamButton.Builder()
                .normalImageRes(R.drawable.main_xiangji)
                .imageRect(new Rect(35, 30, Util.dp2px(50), Util.dp2px(50)))
                .normalText("进入数据分析界面！")
                .subNormalText("查看孩子的详细数据记录～")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        /*Uri uri1 = Uri.parse("http://192.168.137.29");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                        startActivity(intent);*/
                    }
                });
        bmb.addBuilder(builder3);

        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.main_gongneng)
                .imageRect(new Rect(35, 30, Util.dp2px(50), Util.dp2px(50)))
                .normalText("进入详细功能界面")
                .subNormalText("更多带娃功能帮助婴儿健康成长～")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        // When the boom-button corresponding this builder is clicked.
                        Intent intent = new Intent(getActivity(), WebToolsActivity.class);
                        startActivity(intent);
                    }
                });
        bmb.addBuilder(builder2);

        HamButton.Builder builder4 = new HamButton.Builder()
                .normalImageRes(R.drawable.main_kuozhan)
                .imageRect(new Rect(35, 30, Util.dp2px(50), Util.dp2px(50)))
                .normalText("进入扩展页面！")
                .subNormalText("获取更多精彩功能～")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(getActivity(), MoreToolsActivity.class);
                        startActivity(intent);
                    }
                });
        bmb.addBuilder(builder4);

        connect();

        /** 定时刷新页面*/


        mVibrator = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);


        return v;
    }



    void startAnim() {
        baby_status_anim.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        baby_status_anim.hide();
        // or avi.smoothToHide();
    }

    void startAnim_sleep() {
        baby_status_anim_sleep.show();
        // or avi.smoothToShow();
    }

    void stopAnim_sleep() {
        baby_status_anim_sleep.hide();
        // or avi.smoothToHide();
    }

    private void parseJSONWithGSON(String jsonData) throws InterruptedException {
//        Log.e("JSONDATA", jsonData);

        JSONObject jsonObject = JSONObject.parseObject(jsonData);

        int valueInt_Lis = jsonObject.getIntValue("LisLimit");
        int valueInt_PIR = jsonObject.getIntValue("PIRState");
        float valueFloat_Humid = jsonObject.getFloatValue("humData");
        float valueFloat_Temp = jsonObject.getFloatValue("temData");
        float valueFloat_Light = jsonObject.getFloatValue("var_light_a");
        float valueFloat_Lis = jsonObject.getFloatValue("LisAValue");
        int valueInt_Vbra = jsonObject.getIntValue("present");

        //振动延时检测
        for (int i=0;i<9;i++){
            new_Vbra[i]=Vbra[i+1];
        }

        new_Vbra[9]=valueInt_Vbra;

        for (int i=0;i<10;i++){
            Vbra[i]=new_Vbra[i];
        }

        //PIR延时检测
        for (int i=0;i<9;i++){
            new_Person[i]=Person[i+1];
        }

        new_Person[9]=valueInt_PIR;

        for (int i=0;i<10;i++){
            Person[i]=new_Person[i];
        }

        //振动数据处理
        String valueStr_Vbra = null;
        for (int i = 0; i < 10;i++){
            if (Vbra[i] == 1){
                valueStr_Vbra = "振动";
                break;
            }else {
                if (i == 9){
                    valueStr_Vbra = "平稳";
                }
            }
        }

        //PIR数据处理
        String valueStr_PIR = null;
        for (int i = 0; i < 10;i++){
            if (Person[i] == 1){
                valueStr_PIR = "有";
                break;
            }else {
                if (i == 9){
                    valueStr_PIR = "无";
                }
            }
        }




        int valueFloat_Temp_F = (int) valueFloat_Temp * 5 / 9 + 32;

        //处理声音数据
        String valueStr_Lis;
        if (valueInt_Lis == 0 && valueInt_Vbra == 1) {  //同时振动和哭声
            valueStr_Lis = "清醒中";
            //清醒手机端震动提醒
            mVibrator.vibrate(new long[]{1000, 1500},-1);

            //cancelVibration();
            startAnim();
            stopAnim_sleep();


        } else {
            valueStr_Lis = "熟睡中";
            startAnim_sleep();
            stopAnim();

        }

        double value_Lis_Level = (514 - valueFloat_Lis) / 0.4;   //514由电位器决定


        //处理人体红外数据
            /*String valueStr_PIR;
            if (valueInt_PIR == 1)
                valueStr_PIR = "有人";
            else
                valueStr_PIR = "无人";*/

        //处理温湿度数据
        String valueStr_Temp, valueStr_Humid;
        if (16 <= valueFloat_Temp && valueFloat_Temp <= 30)
            valueStr_Temp = "温度宜人";
        else if (valueFloat_Temp < 16 && valueFloat_Temp >= 6)
            valueStr_Temp = "温度偏低";
        else if (valueFloat_Temp < 6)
            valueStr_Temp = "温度过冷";
        else if (valueFloat_Temp <= 34 && valueFloat_Temp > 30)
            valueStr_Temp = "温度偏高";
        else
            valueStr_Temp = "温度过高";

        if (valueFloat_Humid < 28)
            valueStr_Humid = "空气偏干燥";
        else if (valueFloat_Humid >= 28 && valueFloat_Humid < 66)
            valueStr_Humid = "湿度宜人";
        else
            valueStr_Humid = "空气偏闷湿";


        //处理光敏数据
        String valueStr_Light;
        if (valueFloat_Light < 128){
            valueStr_Light = "光强过大";

        }
        else
            valueStr_Light = "光强正常";

        //处理振动数据
        /*String valueStr_Vbra;
        if (valueInt_Vbra == 1)
            valueStr_Vbra = "振动";
        else
            valueStr_Vbra = "平稳";*/


        Time t = new Time();
        t.setToNow();
        //设置数据展示
        main_cry.setText("哭声指数：" + value_Lis_Level);
        main_status.setText(valueStr_Lis);
        main_others.setText("附近有人检测：" + valueStr_PIR);
        main_time.setText("最近一次检测时间：" + t.hour + ":" + t.minute + ":" + t.second);
        main_temp_f.setText("华氏度：" + valueFloat_Temp_F + "℉");
        main_temp.setText(valueFloat_Temp + "℃");
        main_humid.setText(valueFloat_Humid + "%");
        main_temp_level.setText(valueStr_Temp);
        main_humid_level.setText(valueStr_Humid);
        main_light.setText((1000-valueFloat_Light) + "");
        main_light_level.setText(valueStr_Light);
        main_vbra.setText("检测到摇床：" + valueStr_Vbra);


//        Log.w("www", "time=" + time);
//        Log.w("www", "value=" + list.get(0) + "," + list.get(1) + "," + list.get(2) + "," + list.get(3) + "," + list.get(4));

    }

    /**
     * 使用 MQTT 协议连接 ActiveMQ
     */
    private void connect() {

        mqttAndroidClient = new MqttAndroidClient(getActivity(), serverUri, clientId);

//        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    // 自动重连
                    Log.i(TAG, "正在重连");
                } else {
                    // 已连接
                    Log.i(TAG, "已连接");
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.e(TAG, "连接中断");
            }

            @SuppressLint("WrongConstant")
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                switch (topic) {
                    case "data": {

                        Datashow_disconnect.setVisibility(View.INVISIBLE);
                        Datashow_connect.setVisibility(View.VISIBLE);
                        Log.i("MQTT", message.toString());
                        Data.setMqttStatus(true);
                        parseJSONWithGSON(message.toString());
                        break;

                    }
                    case "kill" :{
                        Datashow_disconnect.setVisibility(View.VISIBLE);
                        Datashow_connect.setVisibility(View.INVISIBLE);
                        Data.setMqttStatus(false);
                        break;


                    }
                }

                Log.i(TAG, message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            Log.i(TAG, "Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.i(TAG, "Connected to " + serverUri);
                    // 连接成功
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

                    // 订阅主题
                    subscribeToTopic("data");
                    subscribeToTopic("kill");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to connect to " + serverUri);
                    exception.printStackTrace();
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 订阅主题
     */
    private void subscribeToTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 1, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "主题订阅成功");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "主题订阅失败");
                    exception.printStackTrace();
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "主题订阅失败");
            e.printStackTrace();
        }
    }

    /**
     * 发布消息
     */
    public void publishMessage(String topic, String msg) {

        if (isConnected(mqttAndroidClient)) {
            try {
                MqttMessage message = new MqttMessage();
                message.setPayload(msg.getBytes());

                mqttAndroidClient.publish(topic, message, "Publish", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i(TAG, "发布信息成功");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "发布信息失败");
                    }
                });
            } catch (MqttException e) {
                Log.e(TAG, "信息发布失败:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected(MqttAndroidClient client) {
        return client != null && client.isConnected();
    }


    public void showDialog() {

        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_fragment_disconnect,null);
        mAlertDialog.setContentView(view);

        mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                mAlertDialog.cancel();
            }
        });
        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    public void showDialog2() {

        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_fragment_datashow,null);
        mAlertDialog.setContentView(view);

        mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                mAlertDialog.cancel();
            }
        });
        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }




    @SuppressLint("ResourceAsColor")
    private void cancelVibration() {

        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();


        Button button =new Button(getContext());
        button.setText("取消振动");


        mAlertDialog.setContentView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "成功取消振动", Toast.LENGTH_SHORT).show();
                mVibrator.cancel();

            }
        });

        mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                mAlertDialog.cancel();
            }
        });
        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }
}
