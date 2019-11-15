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

public class SystemHospitalFragment extends BaseFragment {


    private LinearLayout Handle;
    private LinearLayout Yimiao;
    private LinearLayout Ask;
    private LinearLayout Note;

    private WebView hospital_webview;
    private String url;

    private TextView system_title_head_note;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_hospital, null);
        
        hospital_webview=v.findViewById(R.id.hospital_webview);

        Handle=v.findViewById(R.id.hospital_title_body_1);
        Yimiao=v.findViewById(R.id.hospital_title_body_2);
        Ask=v.findViewById(R.id.hospital_title_body_3);
        Note=v.findViewById(R.id.hospital_title_body_4);

        system_title_head_note=v.findViewById(R.id.system_title_head_note);

        system_title_head_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHospitalDialog();

            }
        });



       Handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),HospitalHandleActivity.class) ;
                startActivity(intent);

            }
        });

        Ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HospitalAskActivity.class) ;
                startActivity(intent);

            }
        });

        Yimiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HospitalYimiaoActivity.class) ;
                startActivity(intent);

            }
        });

        Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(getActivity(), HospitalYimiaoActivity.class) ;
                startActivity(intent);*/

            }
        });



        url="https://m.drmed.cn/self-diagnosis";
        hospital_webview.loadUrl(url);

        WebViewFragmentUtil webViewFragmentUtil=new WebViewFragmentUtil();

        webViewFragmentUtil.WebViewUtil(hospital_webview);

        return v;
    }

    private void showHospitalDialog() {

        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_fragment_hospital,null);
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
