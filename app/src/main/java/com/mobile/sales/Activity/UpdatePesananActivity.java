package com.mobile.sales.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.mobile.sales.PrefManager;
import com.mobile.sales.R;
import com.mobile.sales.Server;
import com.mobile.sales.databinding.ActivityOrderBinding;
import com.mobile.sales.databinding.ActivityUpdatePesananBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePesananActivity extends AppCompatActivity {
    private ActivityUpdatePesananBinding binding;
    EditText editHarga, editJml;
    String idB;
    int h,j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePesananBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AndroidNetworking.initialize(this);

        Intent i = new Intent(getIntent());
        idB = i.getStringExtra("id_b");
        Log.d("ID", "ID Update : " + idB);

        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePesananActivity.this);
        final View dialogView = LayoutInflater.from(UpdatePesananActivity.this).inflate(R.layout.activity_update_pesanan, null);

        editHarga = dialogView.findViewById(R.id.editHarga);
        editJml = dialogView.findViewById(R.id.editJml);

        AndroidNetworking.post(Server.site + "get_harga_jml.php")
                .addBodyParameter("id", idB)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() >= 1) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject k = response.getJSONObject(i);
                                    editHarga.setText(k.getString("harga"));
                                    editJml.setText(k.getString("jml_barang"));
                                }
                                builder.setView(dialogView);
                                builder.setCancelable(true);
                                builder.show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(UpdatePesananActivity.this, "Item Tidak Ada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("tag", "errordetail : " + anError);
                    }
                });

    }
}