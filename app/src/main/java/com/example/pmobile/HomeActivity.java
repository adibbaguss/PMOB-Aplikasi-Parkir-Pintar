package com.example.pmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
        String token = mSettings.getString("token", "missing");
        username.setText(token);



//        //intent ke tempat parkir atau ke maps
//        toMaps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent A_maps = new Intent(HomeActivity.this,MapsActivity.class);
//                startActivity(A_maps);
//            }
//        });
//
//
//        //intent ke data parkir
//        toInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent A_info = new Intent(HomeActivity.this,InformasiParkirActivity.class);
//                startActivity(A_info);
//            }
//        });


    }
}