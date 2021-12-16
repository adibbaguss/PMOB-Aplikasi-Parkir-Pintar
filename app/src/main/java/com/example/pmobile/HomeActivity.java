package com.example.pmobile;

import androidx.appcompat.app.AppCompatActivity;

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

}