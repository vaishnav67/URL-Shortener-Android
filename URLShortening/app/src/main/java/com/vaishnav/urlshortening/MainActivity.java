package com.vaishnav.urlshortening;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.input);
                EditText output = (EditText) findViewById(R.id.output);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://api-ssl.bitly.com/v4/shorten";
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            output.setText(obj.getString("link"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String body = null;
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("long_url", input.getText().toString());
                        MyData.put("domain", "bit.ly");
                        MyData.put("group_guid","GROUP ID HERE");
                        return new JSONObject(MyData).toString().getBytes();
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/json");
                        params.put("Authorization", "API-KEY HERE");
                        return params;
                    }

                };
                queue.add(MyStringRequest);
            }
        });

    }

}