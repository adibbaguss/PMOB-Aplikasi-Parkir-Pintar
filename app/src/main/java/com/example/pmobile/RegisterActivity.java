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
    // memanggil api
    private static String URL_REGISTER = "https://apismartparking.000webhostapp.com/api/register";
    // deklari variabel yang akan digunakan
    private Button AfterRegister;
    private ProgressBar loading;
    private EditText editTextNama, editTextUsername, editTextEmail, editTextPass,editTextConfPass, editTextPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // menghubungkan variabel dengan id yang ada pada xml
        AfterRegister = (Button) findViewById(R.id.buttonRegister);
        editTextNama = (EditText) findViewById(R.id.isiNama);
        editTextUsername = (EditText) findViewById(R.id.isiUsername);
        editTextEmail = (EditText) findViewById(R.id.isiEmail);
        editTextPass = (EditText) findViewById(R.id.isiPassword);
        editTextConfPass = (EditText) findViewById(R.id.isiConfPassword);
        editTextPhone = (EditText) findViewById(R.id.isiNomorPhone);
        loading = (ProgressBar)findViewById(R.id.Progress);
        loading.setVisibility(View.INVISIBLE);

        // ketika mengklik button Register
        AfterRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // menerima inputan pada form
                String nama = editTextNama.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPass.getText().toString().trim();
                String c_password = editTextConfPass.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                // kondisi ketika semua inputan tidak kosong
                if(!nama.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !c_password.isEmpty() && !phone.isEmpty()){
                    // kondisi pencocokan password dan konfirmasi password
                    if(password.equals(c_password)) Register();
                    else editTextConfPass.setError("Konfirmasi password salah");
                }else{
                    //ketika kondisi inputan ada yang kosong
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

    // function register
    private void Register() {
        loading.setVisibility(View.VISIBLE);
        AfterRegister.setVisibility(View.GONE);
        // meneriama semua inputan pada form dan disimpan di variabel baru
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
                            // parsing data pada json
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success= jsonObject.getBoolean("success");
                            JSONObject data = jsonObject.getJSONObject("data");
                            String token = data.getString("token");
                            String msg = jsonObject.getString("message");
                            // ketika kondisi success = true
                            if(success) {
                                Toast.makeText(RegisterActivity.this, "Register Sukses", Toast.LENGTH_SHORT).show();

                                Intent to_login = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(to_login);

                                loading.setVisibility(View.GONE);
                                AfterRegister.setVisibility(View.VISIBLE);
                            } else {
                                // konsdisi success = false
                                Toast.makeText(RegisterActivity.this, "Gagal Register!" + msg, Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                AfterRegister.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            // parsing data ada yang salah
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            AfterRegister.setVisibility(View.VISIBLE);
                        }
                    }
                },
                // respon error
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
            // parameter menginputkan data
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