package com.example.lipin.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mesg) TextView mesg;
    @BindView(R.id.img) ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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

    public void test3(View view) {
        ImageRequest imageRequest = new ImageRequest(
                "https://p2.bahamut.com.tw/B/2KU/50/8470caa41bb463b21f05c162d3184dy5.JPG?w=1000",
                new Response.Listener<Bitmap>() {
                    @Override
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

}
