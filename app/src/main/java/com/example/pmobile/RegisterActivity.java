package com.example.pmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {
    Button AfterRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AfterRegister = (Button) findViewById(R.id.buttonRegister);

        //intent sementara setelah register
        AfterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A_Register = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(A_Register);
            }
        });
    }
}