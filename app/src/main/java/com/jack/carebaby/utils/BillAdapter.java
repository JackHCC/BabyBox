package com.jack.carebaby.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>{
    private Context mContext;
    private List<String> id;
    private List<String> title;
    private List<String> date;
    private List<String> price;

    private static final int COMPLETED = 0;

    String url = Data.getUrl();
    String phone = Data.getPhone();


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView billtitle;
        TextView billdate;
        TextView billprice;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            billtitle = (TextView) itemView.findViewById(R.id.title);
            billdate = (TextView) itemView.findViewById(R.id.date);
            billprice = (TextView) itemView.findViewById(R.id.price);
        }
    }

    public BillAdapter(List<String> idList,List<String> titleList,List<String> dateList,List<String> priceList){
        id = idList;
        title = titleList;
        date = dateList;
        price = priceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_fragment_bill_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String sid = id.get(position);
        String stitle = title.get(position);
        String sdate = date.get(position);
        String sprice = price.get(position);
        holder.billtitle.setText(stitle);
        holder.billdate.setText(sdate);
        holder.billprice.setText(sprice+"￥");
        // 长按删除事件
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 二次确认框
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setMessage("确认删除吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Delete","click");
                        OkHttpClient okHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder().url(url+"/bill/delete?id="+sid).build();

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.d("DeleteError", e.getMessage());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                JSONObject jsonObject = JSON.parseObject(response.body().string());
                            }
                        });
                        if (Activity.class.isInstance(mContext)) {
                            Activity activity = (Activity) mContext;
                            activity.recreate();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
}
