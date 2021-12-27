package com.example.pmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InformasiParkirActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String park_id;
    private String token;
    private Double Lat;
    private Double Long;
    private String nama;
   // private Integer slotbike,slotcar,pricecar,pricebike;



    ImageButton btnlogout;
    SharedPreferences.Editor preferencesEditor;

    private GoogleMap mMap;


//    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_parkir);
        btnlogout = (ImageButton)findViewById(R.id.buttonLogout2);
        //get id parkir dari intent
        Bundle dataIntent  = getIntent().getExtras();
        park_id = dataIntent.getString("park_id");
        park_id = dataIntent.getString("park_id");

        //get token dari sharedpreferences
        SharedPreferences mSettings = InformasiParkirActivity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        token = mSettings.getString("token","token");
        preferencesEditor = mSettings.edit();
        //get data dari api dan set view
        this.getDetailData();

        //maps


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //logout

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesEditor.putString("token","false");
                preferencesEditor.apply();
                Intent loginscreen = new Intent(InformasiParkirActivity.this, MainActivity.class);
                startActivity(loginscreen);
                Toast.makeText(InformasiParkirActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
            }
        });
    }






    //get detai data from api
    public void getDetailData(){
        String URI = getResources().getString(R.string.PARK_DETAIL);
        URI+=park_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            JSONObject dataParkir = data.getJSONObject(0);
                            String msg = jsonObject.getString("message");
                            TextView namaParkir = findViewById(R.id.tampilTempat2);
                            TextView lokasiParkir = findViewById(R.id.lokasi);
                            namaParkir.setText(dataParkir.getString("name"));
                            nama = dataParkir.getString("name");
                            lokasiParkir.setText(dataParkir.getString("location"));


                            TextView slotCar = findViewById(R.id.tampilslotCar);
                            slotCar.setText(dataParkir.getString("car_slot"));


                            TextView slotBike = findViewById(R.id.tampilslotBike);
                            slotBike.setText(dataParkir.getString("bike_slot"));

                            TextView Operational = findViewById(R.id.tampilOperasional);
                            Operational.setText(dataParkir.getString("operational"));

                            TextView carprice = findViewById(R.id.tampilHarga);
                            carprice.setText(dataParkir.getString("car_price"));

                            Lat = dataParkir.getDouble("latitude");
                            Long = dataParkir.getDouble("longitude");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InformasiParkirActivity.this, "get data Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error respon
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(-33.32,34.32 );
        mMap.addMarker(new MarkerOptions().position(location).title(nama));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }
}
