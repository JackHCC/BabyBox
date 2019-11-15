package cn.bgbsk.babycare.global;

import android.app.Application;
import android.content.SharedPreferences;

import java.sql.Timestamp;

public class Data extends Application {

    private static String url;
    private static String username;
    private static String phone;
    private static String img;
    private static int loginStatus;
    private static Timestamp created;
    private static String cameraIP;


    private static String countingshow;
    private static String countingshowolder;
    private static boolean mqttStatus;


    private static String registerTime;

    private static SharedPreferences loginSP;
    private static SharedPreferences.Editor loginEdit;

    public static String phoneNumber="18056929880";//预设手机号

    public static String messageText="晚饭做好啦，早点回来吃饭~";

    public static boolean boxsStatus=true;




    @Override
    public void onCreate() {
        /*用户信息*/
        url = "https://babycare.bgbsk.cn";
        username = "未登录";
        loginStatus = 0;
        phone = "未登录";
        mqttStatus = false;

        super.onCreate();
    }


    /**用户相关函数*/
    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPhone() {
        return phone;
    }

    public static String getImg() { return img; }

    public static String getPhoneNumber() { return phoneNumber; }

    public static void setPhoneNumber(String phonenumber) {
        Data.phoneNumber = phonenumber;
    }

    public static void setUsername(String username) {
        Data.username = username;
    }

    public static void setImg(String img) {
        Data.img = img;
    }

    public static int getLoginStatus() {
        return loginStatus;
    }

    public static Timestamp getCreated() {
        return created;
    }

    public static void setCreated(Timestamp created) {
        Data.created = created;
    }

    public static void setLoginStatus(int loginStatus) {
        Data.loginStatus = loginStatus;
    }

    public static void setPhone(String phone) {
        Data.phone = phone;
    }

    public static boolean isMqttStatus() {
        return mqttStatus;
    }

    public static void setMqttStatus(boolean mqttStatus) {
        Data.mqttStatus = mqttStatus;
    }

    public static SharedPreferences getLoginSP() {
        return loginSP;
    }

    public static void setLoginSP(SharedPreferences loginSP) {
        Data.loginSP = loginSP;
    }

    public static SharedPreferences.Editor getLoginEdit() {
        return loginEdit;
    }

    public static void setLoginEdit(SharedPreferences.Editor loginEdit) {
        Data.loginEdit = loginEdit;
    }

    public static String getRegisterTime() {
        return registerTime;
    }

    public static void setRegisterTime(String registerTime) {
        Data.registerTime = registerTime;
    }


    public static String getCountingshow() {
        return countingshow;
    }

    public static void setCountingshow(String countingshow) {
        Data.countingshow = countingshow;
    }

    public static String getCountingshowolder() {
        return countingshowolder;
    }

    public static void setCountingshowolder(String countingshowolder) {
        Data.countingshowolder = countingshowolder;
    }

    public static String getCameraIP() {
        return cameraIP;
    }

    public static void setCameraIP(String cameraIP) {
        Data.cameraIP = cameraIP;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
