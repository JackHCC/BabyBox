package com.jack.carebaby.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jack.carebaby.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder>{
    private Context mContext;
    private List<String> src;


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.id_index_gallery_item_image);
        }
    }

    public ImgAdapter(List<String> imgList){
        src = imgList;
    }

    @Override
    public ImgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_img_item,parent,false);
        return new ImgAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ImgAdapter.ViewHolder holder, final int position) {
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
    }

    @Override
    public int getItemCount() {
        if (src == null)
            return 0;
        else
            return src.size();
    }
}
