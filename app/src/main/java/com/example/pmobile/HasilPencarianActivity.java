package com.example.pmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HasilPencarianActivity extends AppCompatActivity {
    String keyword;
    String token;
    SharedPreferences.Editor preferencesEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_pencarian);

        TextView GetValueSearch = findViewById(R.id.getsearch);



        SharedPreferences mSettings = HasilPencarianActivity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        token = mSettings.getString("token","token");
        preferencesEditor = mSettings.edit();
        keyword = getIntent().getStringExtra("keyword");
        GetValueSearch.setText(keyword);
//
        this.getHasilPencarian();



    }


//        function get rekomendasi parkir
    public void getHasilPencarian() {
        String URI = getResources().getString(R.string.FIND);
        URI+=keyword;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            String msg = jsonObject.getString("message");
                            //ok respon
                                //add fragment
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject childData = data.getJSONObject(i);
                                    String id, nama, harga;
                                    id = childData.getString("park_id");
                                    nama = childData.getString("name");
                                    harga = childData.getString("car_price");

                                    ParkirFragment rekomParkirFrag = ParkirFragment.newInstance(id, nama, harga);
                                    fragmentTransaction.add(R.id.rekompParkir, rekomParkirFrag);
                                }
                                fragmentTransaction.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HasilPencarianActivity.this, "get data Error!" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LinearLayout container = findViewById(R.id.rekompParkir);
                        TextView hasil = new TextView(HasilPencarianActivity.this);
                        hasil.setText("Data Tidak Ditemukan");
                        hasil.setTextColor(getResources().getColor(R.color.black));
                        hasil.setGravity(Gravity.CENTER_HORIZONTAL);
                        container.addView(hasil);
                    }
                }) {
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