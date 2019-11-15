package com.jack.carebaby.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.carebaby.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bgbsk.babycare.global.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BabyWHeightAdapter extends RecyclerView.Adapter<BabyWHeightAdapter.ViewHolder>{
    private Context mContext;
    private List<String> id;
    private List<String> name;
    private List<String> birthday;
    private List<String> sex;
    private List<String> imgurl;
    private static final int COMPLETED = 0;
    String url = Data.getUrl();
    String phone = Data.getPhone();


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView babyname;
        TextView babybirthday;
        TextView babysex;
        ImageView babyimg;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            babyname = (TextView) itemView.findViewById(R.id.name);
            babybirthday = (TextView) itemView.findViewById(R.id.birthday);
            babysex = (TextView) itemView.findViewById(R.id.sex);
            babyimg = (ImageView) itemView.findViewById(R.id.img);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        }
    }

    public BabyWHeightAdapter(List<String> idList,List<String> nameList,List<String> birthdayList,List<String> sexList,List<String> imgList){
        id = idList;
        name = nameList;
        birthday = birthdayList;
        sex = sexList;
        imgurl = imgList;
    }

    @Override
    public BabyWHeightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_baby_wheight_item,parent,false);
        return new BabyWHeightAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final BabyWHeightAdapter.ViewHolder holder, final int position) {
        final List<String> bid = new ArrayList<>();
        final List<String> weight = new ArrayList<>();
        final List<String> height = new ArrayList<>();
        final List<String> time = new ArrayList<>();
        final String sid = id.get(position);
        String sname = name.get(position);
        String sbirthday = birthday.get(position);
        String ssex = sex.get(position);
        holder.babyname.setText(sname);
        holder.babybirthday.setText(sbirthday);
        holder.babysex.setText(ssex);

        if(imgurl.get(position) == "0"){ }
        else {
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
                            holder.babyimg.setImageBitmap((Bitmap) msg.obj);
                            break;
                    }
                }
            };
            final File file = new File(mContext.getCacheDir(), imgurl.get(position));
            //判断，缓存是否存在该文件
            if (file.exists()) {
                //如果缓存存在，从缓存中读取图片
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                holder.babyimg.setImageBitmap(bitmap);
            } else {
                //如果缓存不存在，从网络中下载图片
                Thread th = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        //2.把网址封装成一个Url对象
                        try {
                            URL url = new URL("https://babycare.bgbsk.cn/babyAvatar/" + imgurl.get(position));
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


        Log.d("Get","click");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url+"/baby/data/get?phone="+phone+"&id="+sid).build();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COMPLETED) {
                    WHeightAdapter wheightadapter = new WHeightAdapter(bid,weight,height,time);
                    GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
                    holder.recyclerView.setAdapter(wheightadapter);
                    holder.recyclerView.setLayoutManager(layoutManager);
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
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject0 = jsonArray.getJSONObject(i);
                    bid.add(jsonObject0.getString("_id"));
                    weight.add(jsonObject0.getString("weight"));
                    height.add(jsonObject0.getString("height"));
                    time.add(jsonObject0.getString("time"));
                }
                Message message = new Message();
                message.what = COMPLETED;
                handler.sendMessage(message);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (name == null)
            return 0;
        else
            return name.size();
    }

}
