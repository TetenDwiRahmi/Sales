package com.mobile.sales.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobile.sales.PrefManager;
import com.mobile.sales.R;
import com.mobile.sales.Server;
import com.mobile.sales.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    PrefManager prefManager;
    boolean isPasswordVisible;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AndroidNetworking.initialize(this);

        prefManager = new PrefManager(this);

        binding.editPass.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.editPass.getRight() - binding.editPass.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = binding.editPass.getSelectionEnd();
                        if (isPasswordVisible) {
                            // hide Password
                            binding.editPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.v_visibility_off, 0);
                            binding.editPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else {
                            // show Password
                            binding.editPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.v_visibility, 0);
                            binding.editPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        binding.editPass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        if (prefManager.getLoginStatus()) {
            Intent i = new Intent(LoginActivity.this, OrderActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editEmail.getText().toString().isEmpty()) {
                    binding.editEmail.setError("Email harus diisi");
                } else if (binding.editPass.getText().toString().isEmpty()) {
                    binding.editPass.setError("Password harus diisi");
                } else {
                    login();
                }
            }
        });

    }

    private void login() {
        AndroidNetworking.post(Server.site + "login.php")
                .addBodyParameter("email", binding.editEmail.getText().toString())
                .addBodyParameter("password", binding.editPass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String kode = response.getString("kode");
                            if (kode.equals("1")) {
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                JSONObject user = response.getJSONObject("data");
                                String id = user.getString("id");
                                Log.d("tag", "ID Masuk : " + id);
                                prefManager.setIdUser(id);
                                prefManager.setLoginStatus(true);
                                Intent i = new Intent(LoginActivity.this, OrderActivity.class);
                                startActivity(i);

                            }else if(kode.equals("2")){
                                Toast.makeText(LoginActivity.this, response.getString("pesan"), Toast.LENGTH_SHORT).show();
                            }else if(kode.equals("3")){
                                Toast.makeText(LoginActivity.this, response.getString("pesan"), Toast.LENGTH_SHORT).show();
                            }else if(kode.equals("4")){
                                Toast.makeText(LoginActivity.this, response.getString("pesan"), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, response.getString("pesan"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(LoginActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}