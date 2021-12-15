package com.example.pmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {
    private static String URL_REGISTER = "https://apismartparking.000webhostapp.com/api/register";
    private Button AfterRegister;
    private ProgressBar loading;
    private EditText editTextNama, editTextUsername, editTextEmail, editTextPass,editTextConfPass, editTextPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AfterRegister = (Button) findViewById(R.id.buttonRegister);
        editTextNama = (EditText) findViewById(R.id.isiNama);
        editTextUsername = (EditText) findViewById(R.id.isiUsername);
        editTextEmail = (EditText) findViewById(R.id.isiEmail);
        editTextPass = (EditText) findViewById(R.id.isiPassword);
        editTextConfPass = (EditText) findViewById(R.id.isiConfPassword);
        editTextPhone = (EditText) findViewById(R.id.isiNomorPhone);
        loading = (ProgressBar)findViewById(R.id.Progress);

        AfterRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nama = editTextNama.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPass.getText().toString().trim();
                String c_password = editTextConfPass.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if(!nama.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !c_password.isEmpty() && !phone.isEmpty()){
                    if(password.equals(c_password)) Register();
                    else editTextConfPass.setError("Konfirmasi password salah");
                }else{
                    editTextNama.setError("Masukkan nama !");
                    editTextUsername.setError("Masukkan username !");
                    editTextEmail.setError("Masukkan email !");
                    editTextPass.setError("Masukkan password !");
                    editTextConfPass.setError("Masukkan konfirmasi password !");
                    editTextPhone.setError("Masukkan no handphone !");

                }

            }

        });
    }

    private void Register() {
        loading.setVisibility(View.VISIBLE);
        AfterRegister.setVisibility(View.GONE);
        final String nama = this.editTextNama.getText().toString().trim();
        final String username = this.editTextUsername.getText().toString().trim();
        final String email = this.editTextEmail.getText().toString().trim();
        final String password = this.editTextPass.getText().toString().trim();
        final String c_password = this.editTextConfPass.getText().toString().trim();
        final String phone = this.editTextPhone.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
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
                                Toast.makeText(RegisterActivity.this, "Register Sukses", Toast.LENGTH_SHORT).show();

                                Intent to_login = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(to_login);

                                loading.setVisibility(View.GONE);
                                AfterRegister.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Gagal Register!" + msg, Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                AfterRegister.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            AfterRegister.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Error!" + error.toString(),Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        AfterRegister.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                })

        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("name",nama);
                params.put("username", username);
                params.put("phone", phone);
                params.put("email", email);
                params.put("password", password);
                params.put("c_password", c_password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}