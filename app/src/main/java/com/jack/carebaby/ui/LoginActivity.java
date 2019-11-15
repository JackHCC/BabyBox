package com.jack.carebaby.ui;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jack.carebaby.R.id;
import static com.jack.carebaby.R.layout;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;


    private TextView preUserName;

    private String url = Data.getUrl();

    private SharedPreferences loginSP;
    private SharedPreferences.Editor loginEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);
        loginSP = getSharedPreferences("login", Context.MODE_PRIVATE);
        loginEdit = loginSP.edit();
        initView();
        setListener();
    }

    private void initView() {
        etUsername = findViewById(id.et_username);
        etPassword = findViewById(id.et_password);
        btGo = findViewById(id.bt_go);
        cv = findViewById(id.cv);
        fab = findViewById(id.fab);
//        preUserName = RegisterActivity.findViewById()
    }

    private void setListener() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                Intent i2 = new Intent(LoginActivity.this,HomePage.class);
                startActivity(i2, oc2.toBundle());
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fab, fab.getTransitionName());
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
            }
        });

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("phone", etUsername.getText());
                map.put("password", etPassword.getText());
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
                            loginEdit.putString("phone", String.valueOf(etUsername.getText()));
                            loginEdit.putString("password", String.valueOf(etPassword.getText()));
                            loginEdit.commit();
                            Data.setPhone(jsonObject.getString("phone"));
                            Data.setUsername(jsonObject.getString("username"));
                            Data.setImg(jsonObject.getString("imgUrl"));
                            Data.setPhoneNumber(jsonObject.getString("emergency"));
                            Data.setCreated(jsonObject.getTimestamp("created"));
                            Data.setRegisterTime(jsonObject.getString("created"));
                            Data.setLoginStatus(1);
                            finish();
                            //这里添加登录成功相关东西
                        }
                        
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                        Looper.loop();

                    }
                });
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }
}
