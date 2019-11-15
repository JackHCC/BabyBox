package com.jack.carebaby.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import com.jack.carebaby.ui.BabyImgChangeActivity;

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

public class BabyAdapter extends RecyclerView.Adapter<BabyAdapter.ViewHolder>{
    private Context mContext;
    private List<String> id;
    private List<String> name;
    private List<String> birthday;
    private List<String> sex;
    private List<String> img;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView babyname;
        TextView babybirthday;
        TextView babysex;
        ImageView babyimg;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            babyname = (TextView) itemView.findViewById(R.id.name);
            babybirthday = (TextView) itemView.findViewById(R.id.birthday);
            babysex = (TextView) itemView.findViewById(R.id.sex);
            babyimg = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    public BabyAdapter(List<String> idList,List<String> nameList,List<String> birthdayList,List<String> sexList,List<String> urlList){
        id = idList;
        name = nameList;
        birthday = birthdayList;
        sex = sexList;
        img = urlList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_baby_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final BabyAdapter.ViewHolder holder, final int position) {
        final String url = Data.getUrl();
        final String phone = Data.getPhone();
        final String sid = id.get(position);
        String sname = name.get(position);
        String sbirthday = birthday.get(position);
        String ssex = sex.get(position);
        holder.babyname.setText(sname);
        holder.babybirthday.setText(sbirthday);
        holder.babysex.setText(ssex);

        System.out.println(img.get(position));
        if(img.get(position) == "0"){ }
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
            final File file = new File(mContext.getCacheDir(), img.get(position));
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
                            URL url = new URL("https://babycare.bgbsk.cn/babyAvatar/" + img.get(position));
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
                        final Request request = new Request.Builder().url(url+"/baby/delete?id="+sid).build();

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
        holder.babyimg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(mContext, BabyImgChangeActivity.class);
                intent.putExtra("id",sid);
                mContext.startActivity(intent);
                File file1 = new File(mContext.getCacheDir(), img.get(position));
                file1.delete();
                return true;
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

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "";
    }

    public static long getId(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getIdColumn(context, contentUri, selection, selectionArgs);
            }
        } else {
            return getIdColumn(context, uri, null, null);
        }
        return 0;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }
    public static long getIdColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_id";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getLong(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
