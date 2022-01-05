package com.example.pmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class InformasiParkirActivity extends FragmentActivity implements OnMapReadyCallback {
    private String park_id;
    private String token;
    private Double Lat;
    private Double Long;
    private String nama;
    private String type;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_parkir);
        //get id parkir dari intent
        Bundle dataIntent  = getIntent().getExtras();
        park_id = dataIntent.getString("park_id");

        //get token dari sharedpreferences
        SharedPreferences mSettings = InformasiParkirActivity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        token = mSettings.getString("token","token");

        //get data dari api dan set view
        this.getDetailData();
        Lat=-7.1181109;
        Long=112.4149811;

        //event listener btn pesan
        Button btnPesan = findViewById(R.id.buttonPesan);
        btnPesan.setOnClickListener(view -> {
            //cek seleted radio button
            RadioGroup radioGroup = findViewById(R.id.radioGroupParkir);
            int idRadio = radioGroup.getCheckedRadioButtonId();
            RadioButton seletedRadio = findViewById(idRadio);
            if (seletedRadio.getText().equals("Parkir Motor")){
                type = "bike";
            }else{
                type = "car";
            }
            this.pesanParkir();
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
                            //passing data to view
                            TextView namaParkir = findViewById(R.id.tampilTempat2);
                            TextView lokasiParkir = findViewById(R.id.lokasi);

                            //nama tempat parkir
                            namaParkir.setText(dataParkir.getString("name"));
                            nama = dataParkir.getString("name");

                            //lokasi parkir
                            lokasiParkir.setText(dataParkir.getString("location"));

                            //harga parkir
                            TextView hargaParkir = findViewById(R.id.tampilHarga);
                            hargaParkir.setText(dataParkir.getString("car_price"));

                            //bike slot
                            TextView bikeSlot = findViewById(R.id.tampilslotBike);
                            bikeSlot.setText(dataParkir.getString("bike_slot"));

                            //car slot
                            TextView carSlot = findViewById(R.id.tampilslotCar);
                            carSlot.setText(dataParkir.getString("car_slot"));

                            //operasional
                            TextView operasional = findViewById(R.id.tampilOperasional);
                            operasional.setText(dataParkir.getString("operational"));

                            //rating
                            TextView rating = findViewById(R.id.tampilRating);
                            rating.setText(dataParkir.getString("rating"));
                            RatingBar ratingBar = findViewById(R.id.tampilratingBar1);
                            ratingBar.setRating(Float.parseFloat(dataParkir.getString("rating")));

                            //facilities
                            JSONArray facilities = dataParkir.getJSONArray("facilities");
                            LinearLayout fasilitas = findViewById(R.id.isiFasilitas);
                            for (int i=0;i<facilities.length();i++){
                                JSONObject childData = facilities.getJSONObject(i);
                                //passing value
                                TextView namaFasilitas = new TextView(InformasiParkirActivity.this);
                                namaFasilitas.setText(childData.getString("fac_name"));
                                namaFasilitas.setTextColor(getResources().getColor(R.color.white));
                                TextView deskripsi = new TextView(InformasiParkirActivity.this);
                                deskripsi.setText(childData.getString("desc"));
                                deskripsi.setTextColor(getResources().getColor(R.color.white));
                                fasilitas.addView(namaFasilitas);
                                fasilitas.addView(deskripsi);
                            }

                            //update lat long
                            Lat = dataParkir.getDouble("latitude");
                            Long = dataParkir.getDouble("longitude");
                            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(InformasiParkirActivity.this);

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
        LatLng location = new LatLng(Lat, Long);
        mMap.addMarker(new MarkerOptions().position(location).title(nama));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14));
    }

    //function pesan parkir
    public void pesanParkir(){

        String URI = getResources().getString(R.string.PESAN);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("park_id",this.park_id);
            parameters.put("type",this.type);
        } catch (Exception e) {
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URI, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("berhasil");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("gagal");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
