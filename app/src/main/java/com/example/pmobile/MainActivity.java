package com.example.pmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button toRegister;
    Button AfterLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate");
        AfterLogin = (Button)findViewById(R.id.buttonLogin);
        toRegister = (Button)findViewById(R.id.buttonRegister);

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A_register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(A_register);
            }
        });

        //sementara untuk login
        AfterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A_login = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(A_login);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("onRestart");
    }
    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("onDestroy");
    }


}
