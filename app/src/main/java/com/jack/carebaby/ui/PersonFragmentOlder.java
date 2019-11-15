package com.jack.carebaby.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;
import com.jack.carebaby.base.BaseFragment;
import com.jack.carebaby.utils.OlderAdapter;
import com.memorandum.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.bgbsk.babycare.global.Data.getCountingshowolder;
import static cn.bgbsk.babycare.global.Data.getLoginStatus;
import static cn.bgbsk.babycare.global.Data.getUsername;
import static cn.bgbsk.babycare.global.Data.setCountingshowolder;

public class PersonFragmentOlder extends BaseFragment{


    private TextView Login;
    private TextView Count;
    private ImageView EditName;
    private ImageView HeadImg;

    private LinearLayout Setting;
    private LinearLayout More;
    private LinearLayout Help;
    private LinearLayout Know;
    private LinearLayout NewOlder;
    private LinearLayout Record;

    private static final int COMPLETED = 0;
    private RecyclerView olderlist;

    private int count_baby;
    private String register_time;

    private String count_show;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume(){
        super.onResume();
        String phone = Data.getPhone();
        if(phone!="未登录")
            Get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.older_fragment_person, null);

        HeadImg=v.findViewById(R.id.person_title_head_img);
        Login=v.findViewById(R.id.person_title_head_name);
        EditName=v.findViewById(R.id.person_title_head_namechange);
        NewOlder = v.findViewById(R.id.person_body_head_1);
        Setting = v.findViewById(R.id.person_body_head_3);
        More = v.findViewById(R.id.person_body_head_4);
        Help = v.findViewById(R.id.person_body_foot_5);
        Know = v.findViewById(R.id.person_body_foot_3);
        Record = v.findViewById(R.id.person_body_foot_2);
        olderlist = v.findViewById(R.id.person_content_older_list);
        Count = v.findViewById(R.id.person_title_head_time);

        if(Data.getImg() != null && Data.getLoginStatus() == 1) {
            final Bitmap bmp = null;
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 0:
                            Toast.makeText(getContext(), "请求失败！", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            HeadImg.setImageBitmap((Bitmap) msg.obj);
                            break;
                    }
                }
            };

            String url = "https://babycare.bgbsk.cn/avatar/" + Data.getImg();
            final File file = new File(getContext().getCacheDir(), Data.getImg());
            //判断，缓存是否存在该文件
            if (file.exists()) {
                //如果缓存存在，从缓存中读取图片
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                HeadImg.setImageBitmap(bitmap);
            } else {
                //如果缓存不存在，从网络中下载图片
                Thread th = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        //2.把网址封装成一个Url对象
                        try {
                            URL url = new URL("https://babycare.bgbsk.cn/avatar/" + Data.getImg());
                            //3.获取客户端和服务器的链接对象，此时还没有建立连接
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            //4.对连接对象初始化
                            //设置请求方法，注意大写
                            conn.setRequestMethod("GET");
                            //设置连接超时
                            conn.setConnectTimeout(5000);
                            //设置读取超时
                            conn.setReadTimeout(5000);
                            //发送请求，与服务器建立连接
                            conn.connect();
                            //如果响应码为200,说明请求成功
                            if (conn.getResponseCode() == 200) {
                                //获取服务器响应头里中流，流里的数据就是客户端请求的数据
                                InputStream in = conn.getInputStream();
                                //将数据缓存到内存中
                                FileOutputStream fos = new FileOutputStream(file);
                                byte[] b = new byte[1024];
                                int len = 0;
                                while ((len = in.read(b)) != -1) {
                                    fos.write(b, 0, len);
                                }
                                fos.close();
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = bitmap;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                th.start();
            }
        }

        Login.setText(Data.getUsername());

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLoginStatus()==0){
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(),getUsername()+"已登录",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            }
        });

        EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserManageActivity.class);
                startActivity(intent);
            }
        });

        Know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DailyKnowActivity.class);
                startActivity(intent);
            }
        });

        More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MoreToolsActivity.class);
                startActivity(intent);
            }
        });

        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),HelpAllActivity.class);
                startActivity(intent);
            }
        });

        Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        NewOlder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.getLoginStatus()==1) {
                    Intent intent = new Intent(getActivity(), AddOlderActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(NewOlder.getContext(), "请先登录", Toast.LENGTH_LONG).show();
            }
        });
        String phone = Data.getPhone();
        if(phone!="未登录")
            Get();
        return v;
    }


    // 获取数据库信息
    public void Get(){
        String url = Data.getUrl();
        String phone = Data.getPhone();
        final List<String> id = new ArrayList<>();
        final List<String> name = new ArrayList<>();
        final List<String> birthday = new ArrayList<>();
        final List<String> sex = new ArrayList<>();
        final List<String> emephone = new ArrayList<>();
        Log.d("Get","click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url+"/olds/get?phone="+phone).build();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COMPLETED) {
                    OlderAdapter olderadpapter;
                    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                    olderlist.setLayoutManager(layoutManager);
                    olderadpapter = new OlderAdapter(id,name,birthday,sex,emephone);
                    olderlist.setAdapter(olderadpapter);
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
                JSONArray jsonArray = jsonObject.getJSONArray("olds");

                //设置宝宝数展示
                count_baby=jsonArray.size();
                register_time=String.valueOf(Data.getCreated());


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());//获取当前时间

                try
                {
                    Date d1 = df.parse(register_time);
                    Date d2 = df.parse(String.valueOf(df.format(date)));
                    long diff = d2.getTime()-d1.getTime();//这样得到的差值是微秒级别
                    long days = diff / (1000 * 60 * 60 * 24);
                    /*long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                    long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);*/

                    count_show="您已入驻"+days+"天，祝老人长命百岁~";
                    setCountingshowolder(count_show);
                }
                catch (Exception e)
                {
                }

                if (jsonArray!=null){
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject0 = jsonArray.getJSONObject(i);
                        id.add(jsonObject0.getString("_id"));
                        name.add(jsonObject0.getString("name"));
                        birthday.add(jsonObject0.getString("birthday"));
                        sex.add(jsonObject0.getString("sex"));
                        emephone.add(jsonObject0.getString("phone"));
                    }
                    Message message = new Message();
                    message.what = COMPLETED;
                    handler.sendMessage(message);
                }
            }
        });

        Count.setText(String.valueOf(getCountingshowolder()));
    }
}
