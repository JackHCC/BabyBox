package com.jack.carebaby.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;
import com.jack.carebaby.base.BaseFragment;
import com.jack.carebaby.utils.StringUtils;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v2.InputDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.bgbsk.babycare.global.Data.phoneNumber;

public class CameraFragmentOlder extends BaseFragment {



    protected Context mContext;

    private WebView camera_webview;
    private ImageView ivError;
    private EditText setUrl;
    private Button setUrl_Button;

    private Button button_call;
    private Button button_gethelp;

    String url;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext=getContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.older_fragment_camera, null);

        camera_webview=v.findViewById(R.id.camera_webview);
        ivError=v.findViewById(R.id.camera_webview_error);
        setUrl=v.findViewById(R.id.camera_seturl);
        setUrl_Button=v.findViewById(R.id.camera_seturl_button);

        button_call=v.findViewById(R.id.button_call);
        button_gethelp=v.findViewById(R.id.button_gethelp);

        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent=new Intent(getActivity(),OlderDialActivity.class);
                startActivity(intent);*/

                call(phoneNumber);

            }
        });

        button_gethelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhoneDialog();
            }
        });






        camera_webview.loadUrl("http://192.168.43.250:81/stream");

        setUrl_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="http://"+setUrl.getText()+"/stream";
                camera_webview.loadUrl(url);
                //refresh();
            }
        });


        camera_webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }


            //在开始加载网页时会回调
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                ivError.setVisibility(View.INVISIBLE);
                camera_webview.setVisibility(View.VISIBLE);
            }
            //加载错误的时候会回调
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
                ivError.setVisibility(View.VISIBLE);
                camera_webview.setVisibility(View.INVISIBLE);
            }

            //加载错误的时候会回调
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                if (webResourceRequest.isForMainFrame()) {
                    ivError.setVisibility(View.VISIBLE);
                    camera_webview.setVisibility(View.INVISIBLE);
                }
            }


        });

        //支持app内js交互
        camera_webview.getSettings().setJavaScriptEnabled(true);

        //自适应屏幕
        camera_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        camera_webview.getSettings().setLoadWithOverviewMode(true);

        //设置了支持缩放
        camera_webview.getSettings().setSupportZoom(true);

        //扩大比例缩放
        camera_webview.getSettings().setUseWideViewPort(true);

        //设置是否出现缩放工具
        camera_webview.getSettings().setBuiltInZoomControls(true);



        return v;
    }



    /**
     * 显示更换电话对话框
     */
    public void showPhoneDialog() {

        //从服务器获取电话
        //String phone = currentUser.getMobilePhoneNumber();

        InputDialog.show(mContext, "设置紧急电话", "请输入紧急情况下需要联系的家人：", "确定", new InputDialogOkButtonClickListener() {
            @Override
            public void onClick(Dialog dialog, String inputStr) {
                if (inputStr.equals("")) {
                    Toast.makeText(CameraFragmentOlder.this.getActivity(),
                            "内容不能为空！", Toast.LENGTH_LONG).show();
                } else {
                    if (StringUtils.checkPhoneNumber(inputStr)) {

                        String url = Data.getUrl();
                        String phone = Data.getPhone();
                        Data.setPhoneNumber(inputStr);
                        dialog.dismiss();
                        Log.d("Upload","click");
                        OkHttpClient okHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder().url(url+"/user/change/emergency?phone="+phone
                                +"&emergency="+inputStr).build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.d("UploadError", e.getMessage());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                JSONObject jsonObject = JSON.parseObject(response.body().string());
                                Looper.prepare();
                                Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                        });
                    } else {
                        Toast.makeText(CameraFragmentOlder.this.getActivity(),
                                "请输入正确的电话号码", Toast.LENGTH_LONG).show();
                    }
                }

                Toast.makeText(mContext, "您输入了：" + inputStr, Toast.LENGTH_SHORT).show();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setInputInfo(new InputInfo()
                .setMAX_LENGTH(11)                                          //设置最大长度11位
        );


    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
