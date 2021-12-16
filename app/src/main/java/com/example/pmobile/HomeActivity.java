package com.example.pmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Button toInfo;
    Button toMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toInfo = (Button) findViewById(R.id.buttonInfoParkir);
        toMaps = (Button)findViewById(R.id.buttonParkir);
        TextView username = findViewById(R.id.tampilUsername);

        SharedPreferences mSettings = HomeActivity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String name = mSettings.getString("name", "user name");
        String token = mSettings.getString("token","token");
        username.setText(name);
        this.cekPesanParkir(token);



        //intent ke tempat parkir atau ke maps
        toMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A_maps = new Intent(HomeActivity.this,MapsActivity.class);
                startActivity(A_maps);
            }
        });


        //intent ke data parkir
        toInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A_info = new Intent(HomeActivity.this,InformasiParkirActivity.class);
                startActivity(A_info);
            }
        });


    }


    //logut belum dibuat functionnya
    public void logout(){
        Toast.makeText(this,"Log Out success",Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logut:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//function cekpesanparkir
    public void cekPesanParkir(String token){
        String URI = getResources().getString(R.string.DETAIL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String msg = jsonObject.getString("message");
                            TextView lokasiParkir = findViewById(R.id.tampilLokasiParkir);
                            lokasiParkir.setText("parkir di id parkir : "+data.getString("park_id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "get data Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView lokasiParkir = findViewById(R.id.tampilLokasiParkir);
                        lokasiParkir.setText("User Belum Memesan Parkir");
                        Button keParkir = findViewById(R.id.buttonParkir);
                        keParkir.setVisibility(View.INVISIBLE);
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}