package com.example.lipin.volley;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

import dalvik.annotation.TestTarget;

//InputStream最後拿到的byte 基本型別不能使用泛型,所以給予byte陣列
public class BradInputStreamRequest extends Request<byte[]> {
    private Response.Listener<byte[]>listener;
    private Map<String,String> responseHeader;
    private Map<String,String> params;
    //改寫新的一個建構式
    public BradInputStreamRequest(
            int method,
            String url,
            Map<String,String>params,
            Response.Listener<byte[]>listener,//新增這個
            @Nullable Response.ErrorListener errorlistener) {

        super(method, url, errorlistener);

        this.listener =listener;
        this.params= params;

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    //參數的資料資料已經回應回來,需要接收去做回應
    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        responseHeader = response.headers;
        //               成功的話   拿取response的data資料,這串是解析,請示著記起來
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
    //傳遞資料
    @Override
    protected void deliverResponse(byte[] response) {
        listener.onResponse(response);
    }
}
