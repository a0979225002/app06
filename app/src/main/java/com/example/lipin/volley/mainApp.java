package com.example.lipin.volley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//共用文件 extends Application系統自動把他創出來,不用去new com.example.lipin.volley.mainApp
//需要去mainfests內  android:name="com.example.lipin.volley.mainApp"
public class mainApp extends Application {
    public static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);
    }
}
