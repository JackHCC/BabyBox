package com.jack.carebaby.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BabyInfoAdapter extends RecyclerView.Adapter<BabyInfoAdapter.ViewHolder>{
    private Context mContext;
    private List<String> id;
    private List<String> src;
    private List<String> content;
    private List<String> time;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView img;
        TextView babycontent;
        TextView babytime;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.id_index_gallery_item_image);
            babycontent = (TextView) itemView.findViewById(R.id.content);
            babytime = (TextView) itemView.findViewById(R.id.time);
        }
    }

    public BabyInfoAdapter(List<String> idList,List<String> imgList, List<String> contentList, List<String> timeList){
        id = idList;
        src = imgList;
        content = contentList;
        time = timeList;
    }

    @Override
    public BabyInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_baby_info_item,parent,false);
        return new BabyInfoAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final BabyInfoAdapter.ViewHolder holder, final int position) {
        final String sid = id.get(position);
        final Bitmap bmp = null;
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        holder.img.setImageBitmap((Bitmap) msg.obj);
                        break;
                }
            }
        };
        String scontent = content.get(position);
        String stime = time.get(position);
        holder.babycontent.setText(scontent);
        holder.babytime.setText(stime);
        String url = src.get(position);
        url = url.replace(" ", "%20");
        url = "https://babycare.bgbsk.cn/share/" + url;
        final File file = new File(mContext.getCacheDir(), src.get(position));
        //判断，缓存是否存在该文件
        if (file.exists()) {
            //如果缓存存在，从缓存中读取图片
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            holder.img.setImageBitmap(bitmap);
        } else {
            //如果缓存不存在，从网络中下载图片
            Thread th = new Thread() {
                @Override
                public void run() {
                    super.run();
                    //2.把网址封装成一个Url对象
                    try {
                        URL url = new URL("https://babycare.bgbsk.cn/share/" + src.get(position).replace(" ", "%20"));
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 二次确认框
                final String url = Data.getUrl();
                final String phone = Data.getPhone();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setMessage("确认删除吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Delete","click");
                        OkHttpClient okHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder().url(url+"/share/delete?id="+sid).build();

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
        if (src == null)
            return 0;
        else
            return src.size();
    }
}
