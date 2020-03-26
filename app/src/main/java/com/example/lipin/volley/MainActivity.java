package com.example.lipin.volley;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private String[] permissions ={Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File dowloadDir;
    @BindView(R.id.mesg) TextView mesg;
    @BindView(R.id.img) ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //查詢是否有外部儲存權限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //沒有權限就去要
            ActivityCompat.requestPermissions(this,
                    permissions,
                    123);//有需要分別區分在做
        }else {


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }else {

        }

    }
    //抓網路原始碼json介紹
    public void test1(View view) {

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://gnn.gamer.com.tw/detail.php?sn=194572",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mesg.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        mainApp.queue.add(request);


    }

    public void test2(View view) {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        mainApp.queue.add(request);
    }
    //抓json資料傳上來
    private void parseJSON(String json) {
        mesg.setText("");//清掉舊的畫面資料
        img.setImageBitmap(null);
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                mesg.append(row.getString("Name") + ":"
                        + row.getString("Address") + "\n");
            }
        } catch (JSONException e) {
            Log.v("brad", e.toString());
        }
    }
    //抓圖片
    public void test3(View view) {
        ImageRequest imageRequest = new ImageRequest(
                "https://p2.bahamut.com.tw/B/2KU/50/8470caa41bb463b21f05c162d3184dy5.JPG?w=1000",
                new Response.Listener<Bitmap>() {
                    @Override
                    //自動轉成Bitmap
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                },
                0, 0,//指定圖的寬高,0是原圖寬高
                Bitmap.Config.ARGB_4444,//調色盤
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        mainApp.queue.add(imageRequest);
    }
    //抓JsonArray陣列資料
    public void test4(View view){
        JsonArrayRequest request = new JsonArrayRequest(
                "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseJSON2(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        mainApp.queue.add(request);
    }
    private void parseJSON2(JSONArray jsonArray){
        mesg.setText("");//清掉舊的畫面資料
        img.setImageBitmap(null);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                mesg.append(row.getString("Name") + ":"
                        + row.getString("Address") + "\n");
            }
        } catch (JSONException e) {
            Log.v("brad", e.toString());
        }
    }
    public void test5(View view){
        BradInputStreamRequest request = new BradInputStreamRequest(
                Request.Method.GET,
                "https://pdfmyurl.com/?url=https://www.apple.com/tw-edu/shop",
                null,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        savePDF(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("brad",error.toString());
                    }
                }
        );
        //因為TimeoutError而做這串
        request.setRetryPolicy(new DefaultRetryPolicy(
                20*1000,//多增加20秒時間
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        mainApp.queue.add(request);
    }
    //儲存pdf方法
    private void savePDF(byte[] PDF){
        //外部資料夾DOWNLOADS的位置寫法
        File dowloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File saveFile = new File(dowloadDir,"lipin.pdf");
        try {
            BufferedOutputStream bout =
                    new BufferedOutputStream(new FileOutputStream(saveFile));
            bout.write(PDF);
            bout.flush();
            bout.close();
            Toast.makeText(this,"Save ok",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Log.v("brad",e.toString());
        }

    }
}
