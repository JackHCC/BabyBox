package com.jack.carebaby.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BasePage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bgbsk.babycare.global.Data;

import static cn.bgbsk.babycare.global.Data.getPhone;
import static cn.bgbsk.babycare.global.Data.getUsername;

public class UserManageActivity extends BasePage {


    private TextView name;
    private TextView title;
    private TextView phone;
    private ImageView img;

    private LinearLayout ChangeUser;
    private LinearLayout ChangePassword;
    private LinearLayout Exit;

    File file;

    private SharedPreferences loginSP;
    private SharedPreferences.Editor loginEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);

        initView();

        name.setText(getUsername());
        title.setText(getUsername());
        phone.setText(getPhone());

        loginSP = this.getSharedPreferences("login", Context.MODE_PRIVATE);

        ChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserManageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(UserManageActivity.this,LoginActivity.class);
                startActivity(intent);*/
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.setLoginStatus(0);
                Data.setUsername("未登录");
                Data.setPhone("未登录");
                loginSP.edit().clear().commit();
                Intent intent = new Intent(UserManageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Data.getLoginStatus() == 1) {
                    Intent intent = new Intent(UserManageActivity.this, HeadImgChangeActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(UserManageActivity.this, "请先登录", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initView() {
        name = findViewById(R.id.user_manage_header_topname);
        title = findViewById(R.id.user_manage_header_title);
        phone = findViewById(R.id.user_manage_header_phone);
        ChangeUser = findViewById(R.id.user_manage_login);
        ChangePassword = findViewById(R.id.user_manage_password);
        Exit = findViewById(R.id.user_manage_exit);
        img = findViewById(R.id.user_manage_header_image);


        if (Data.getImg() != null && Data.getLoginStatus() == 1) {
            final Bitmap bmp = null;
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 0:
                            Toast.makeText(UserManageActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            img.setImageBitmap((Bitmap) msg.obj);
                            break;
                    }
                }
            };

            String url = "https://babycare.bgbsk.cn/avatar/" + Data.getImg();
            final File file = new File(UserManageActivity.this.getCacheDir(), Data.getImg());
            //判断，缓存是否存在该文件
            if (file.exists()) {
                //如果缓存存在，从缓存中读取图片
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                img.setImageBitmap(bitmap);
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
    }

}
