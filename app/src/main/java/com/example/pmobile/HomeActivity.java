package com.example.pmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button toInfo;
    Button toMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toInfo = (Button) findViewById(R.id.buttonInfoParkir);
        toMaps = (Button)findViewById(R.id.buttonParkir);

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
}