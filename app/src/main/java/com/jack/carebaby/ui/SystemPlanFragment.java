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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.carebaby.R;
import com.jack.carebaby.base.BaseFragment;

import cn.bgbsk.babycare.global.Data;

public class SystemPlanFragment extends BaseFragment {




    private TextView system_title_head_note;

    private LinearLayout Gallery;
    private LinearLayout WHeight;
    private LinearLayout Record;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_plan, null);


        system_title_head_note=v.findViewById(R.id.system_title_head_note);
        Gallery=v.findViewById(R.id.system_title_body_1);
        Record=v.findViewById(R.id.system_title_body_2);
        WHeight=v.findViewById(R.id.system_title_body_3);



        system_title_head_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlanDialog();

            }
        });


        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Data.getLoginStatus()==1) {
                    Intent intent = new Intent(getActivity(), ImgShowActivity.class);

                    startActivity(intent);
                }
                else
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_LONG).show();

            }
        });

        Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Data.getLoginStatus()==1) {
                    Intent intent = new Intent(getActivity(), BabyInfoShowActivity.class);

                    startActivity(intent);
                }
                else
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_LONG).show();

            }
        });

        WHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Data.getLoginStatus()==1) {
                    Intent intent = new Intent(getActivity(), PlanWHeightActivity.class);

                    startActivity(intent);
                }
                else
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_LONG).show();

            }
        });




        return v;
    }

   private void showPlanDialog() {

        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_fragment_plan,null);
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
