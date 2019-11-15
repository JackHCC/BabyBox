package com.jack.carebaby.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BaseFragment;
import com.jack.carebaby.utils.WebViewFragmentUtil;

public class SystemLiveFragment extends BaseFragment {

    private WebView live_webview;
    private String url;

    private TextView system_title_head_note;

    private LinearLayout garbage;
    private LinearLayout alarm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_live, null);

        live_webview=v.findViewById(R.id.live_webview);

        system_title_head_note=v.findViewById(R.id.system_title_head_note);

        system_title_head_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveDialog();

            }
        });

        garbage=v.findViewById(R.id.live_title_body_3);
        alarm=v.findViewById(R.id.live_title_body_1);


        url="https://www.evolife.cn/category/homeware/page/11";
        live_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();

        webViewFragmentUtil.WebViewUtil(live_webview);


        garbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LiveGarbageActivity.class) ;
                startActivity(intent);

            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LiveAlarmActivity.class) ;
                startActivity(intent);

            }
        });


        return v;
    }

    private void showLiveDialog() {

        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_fragment_live,null);
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
}
