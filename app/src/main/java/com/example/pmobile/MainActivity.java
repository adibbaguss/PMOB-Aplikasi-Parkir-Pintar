package com.example.pmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button toRegister,AfterLogin;
    private EditText editText_email, editText_password;
    private ProgressBar loading;
    private static String URL_LOGIN = "https://apismartparking.000webhostapp.com/api/login";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate");

        editText_email = (EditText)findViewById(R.id.inputEmail);
        editText_password = (EditText)findViewById(R.id.inputPassword);
        AfterLogin = (Button)findViewById(R.id.buttonLogin);
        toRegister = (Button)findViewById(R.id.buttonRegister);
        loading = (ProgressBar)findViewById(R.id.Progress);
        loading.setVisibility(View.INVISIBLE);

        toRegister.setOnClickListener(this);
        AfterLogin.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {
        if(v == AfterLogin){
            String email = editText_email.getText().toString().trim();
            String pass = editText_password.getText().toString().trim();

            if(!email.isEmpty() && !pass.isEmpty()){
                Login(email,pass);
            }else{
                editText_email.setError("Masukkan Email:");
                editText_password.setError("Masukkan Password:");
            }
        }else if(v == toRegister){
            Intent register = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(register);
        }
    }

    private void Login(final String email, final String pass) {
        loading.setVisibility(View.VISIBLE);
        AfterLogin.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success= jsonObject.getBoolean("success");
                            JSONObject data = jsonObject.getJSONObject("data");
                            String token = data.getString("token");
                            String msg = jsonObject.getString("message");
                            if(success) {
                                SharedPreferences mSettings = MainActivity.this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString("token", token);
                                editor.apply();



                                Toast.makeText(MainActivity.this, "Login Sukses"+token, Toast.LENGTH_SHORT).show();

                                Intent to_home = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(to_home);

                                loading.setVisibility(View.GONE);
                                AfterLogin.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(MainActivity.this, "Gagal Login!" + msg, Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                AfterLogin.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Login Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            AfterLogin.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error!" + error.toString(),Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        AfterLogin.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                })

        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password", pass);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
