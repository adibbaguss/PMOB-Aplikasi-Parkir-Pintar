package com.example.pmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private Button toMaps;
    SearchView SearchParking;
    ImageButton btnlogout;
    String token;
    SharedPreferences.Editor preferencesEditor;
    private String id_parkir;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        toMaps = findViewById(R.id.buttonParkir);
        TextView username = findViewById(R.id.tampilUsername);
        btnlogout = (ImageButton)findViewById(R.id.buttonLogout);
        SearchParking = (SearchView)findViewById(R.id.Search);


        //time
        Date date=new Date();
        TextView tgl =findViewById(R.id.tampilTanggal);
        String [] datefinal = date.toString().split(" ");
        tgl.setText(datefinal[0]+", "+datefinal[1]+" "+datefinal[2]+" "+datefinal[5]);




        SharedPreferences mSettings = HomeActivity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String name = mSettings.getString("name", "user name");
        token = mSettings.getString("token","token");
        preferencesEditor = mSettings.edit();
        username.setText(name);
        //cek pesanan parkir
        this.cekPesanParkir(token);

        //rekomendasi lokasi parkir
        this.getRekomParkir(token);


        //event listener button batal parkir
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            this.batalParkir(token);
            this.cekPesanParkir(token);
        });

        //event listener refresh button
        ImageButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            this.cekPesanParkir(token);
        });





        SearchParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle setSearch = new Bundle();
                String search = SearchParking.getQuery().toString().trim();
                Intent search_a = new Intent(HomeActivity.this,HasilPencarianActivity.class);

                setSearch.putString("keyword", search);
                search_a.putExtras(setSearch);

                startActivity(search_a);
                Toast.makeText(HomeActivity.this, "Pencarian" + search, Toast.LENGTH_SHORT).show();

            }
        });

        SearchParking.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //your code here
                return false;
            }
        });


        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesEditor.putString("token","false");
                preferencesEditor.apply();
                Intent loginscreen = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(loginscreen);
                Toast.makeText(HomeActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
            }
        });


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
                            JSONObject parkingLot = data.getJSONObject("parking_lot");
                            String msg = jsonObject.getString("message");
                            TextView lokasiParkir = findViewById(R.id.tampilLokasiParkir);
                            lokasiParkir.setText(parkingLot.getString("name"));

                            //parking id
                            TextView parkID = findViewById(R.id.park_id);
                            parkID.setVisibility(View.VISIBLE);
                            parkID.setText("Parking ID : "+Integer.toString(data.getInt("userpark_id")));


                            //tipe
                            TextView tipe = findViewById(R.id.tipe);
                            tipe.setVisibility(View.VISIBLE);
                            tipe.setText("Tipe : " + data.getString("type"));

                            //cost
                            TextView cost = findViewById(R.id.cost);
                            cost.setVisibility(View.VISIBLE);
                            cost.setText("Cost : Rp "+Integer.toString(data.getInt("cost"))+" /Jam");

                            //status
                            TextView status = findViewById(R.id.status);
                            status.setVisibility(View.VISIBLE);
                            status.setText("Status : "+data.getString("status"));

                            //tomaps button
                            toMaps.setVisibility(View.VISIBLE);



                            if(data.getString("status").equals("otw")){
                                Button btnCancel = findViewById(R.id.btnCancel);
                                btnCancel.setVisibility(View.VISIBLE);

                                TextView checkin = findViewById(R.id.checkinTime);
                                checkin.setVisibility(View.GONE);
                            }else{
                                Button btnCanel = findViewById(R.id.btnCancel);
                                btnCanel.setVisibility(View.GONE);

                                TextView checkin = findViewById(R.id.checkinTime);
                                checkin.setVisibility(View.VISIBLE);
                                checkin.setText("Checkin Time : "+data.getString("checkin_time"));
                            }
                            //mengambil id_parkir
                            id_parkir = data.getString("park_id");
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
                        keParkir.setVisibility(View.GONE);

                        //parkingID
                        TextView parkID =findViewById(R.id.park_id);
                        parkID.setVisibility(View.GONE);

                        //tipe
                        TextView tipe = findViewById(R.id.tipe);
                        tipe.setVisibility(View.GONE);


                        //cost
                        TextView cost = findViewById(R.id.cost);
                        cost.setVisibility(View.GONE);

                        //status
                        TextView status = findViewById(R.id.status);
                        status.setVisibility(View.GONE);

                        //button batal parkir
                        Button btnCancel = findViewById(R.id.btnCancel);
                        btnCancel.setVisibility(View.GONE);

                        //checkin time
                        TextView checkin = findViewById(R.id.checkinTime);
                        checkin.setVisibility(View.GONE);
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

        // intent ke MapsActivity
        toMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent ke tempat parkir atau ke maps
                    Bundle setLoc = new Bundle();
                    Intent A_maps = new Intent(HomeActivity.this,MapsActivity.class);

                    setLoc.putString("id_parkir", id_parkir);
                    A_maps.putExtras(setLoc);

                    Toast.makeText(HomeActivity.this, "Locatioan ID = " + id_parkir, Toast.LENGTH_SHORT).show();

                    startActivity(A_maps);
            }
        });
    }





//    function get rekomendasi parkir
    public void getRekomParkir(String token){
        String URI = getResources().getString(R.string.GET_ALL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URI,
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

                            //add fragment limit 2 data tempat parkir
                            for (int i=0;i<2;i++){
                                JSONObject childData = data.getJSONObject(i);
                                String id,nama,harga;
                                id = childData.getString("park_id");
                                nama = childData.getString("name");
                                harga = childData.getString("car_price");

                                ParkirFragment rekomParkirFrag = ParkirFragment.newInstance(id,nama,harga);
                                fragmentTransaction.add(R.id.rekompParkir, rekomParkirFrag);
                            }
                            fragmentTransaction.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "get data Error!" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView noDataRekom = findViewById(R.id.textviewLokasiTerdekat);
                        noDataRekom.setText("rekomendasi tidak ada");
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

    //function batal pesan parkir
    public void batalParkir(String token){
        String URI = getResources().getString(R.string.BATAL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray data = jsonObject.getJSONArray("data");
                            String msg = jsonObject.getString("message");
                            //ok respon
                            Toast.makeText(HomeActivity.this,""+msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "get data Error!" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("api error");
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


    //update data ketika kembali ke activity ini
    @Override
    public void onResume(){
        super.onResume();
        this.cekPesanParkir(token);
    }
}
