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

public class SystemDailyFragment extends BaseFragment {

    private LinearLayout trip;
    private LinearLayout eat;
    private LinearLayout music;
    private LinearLayout know;

    private WebView community_webview;
    private String url;

    private TextView system_title_head_note;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_daily, null);

        trip=v.findViewById(R.id.daily_title_body_4);
        eat=v.findViewById(R.id.daily_title_body_1);
        know=v.findViewById(R.id.daily_title_body_2);
        music=v.findViewById(R.id.daily_title_body_3);

        system_title_head_note=v.findViewById(R.id.system_title_head_note);



        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DailyTripActivity.class) ;
                startActivity(intent);

            }
        });

        eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DailyEatActivity.class) ;
                startActivity(intent);

            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), OlderDialActivity.class) ;
                startActivity(intent);

            }
        });

        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DailyKnowActivity.class) ;
                startActivity(intent);

            }
        });

        system_title_head_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailyDialog();

            }
        });



        community_webview=v.findViewById(R.id.community_webview);



        url="http://m.mama.cn/";
        community_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();

        webViewFragmentUtil.WebViewUtil(community_webview);
        


        return v;
    }



    private void showDailyDialog() {

        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_fragment_daily,null);
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
