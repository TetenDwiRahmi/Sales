package com.mobile.sales.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobile.sales.Adapter.OrderAdapter;
import com.mobile.sales.Interface.InterfaceUpdate;
import com.mobile.sales.Model.Order;
import com.mobile.sales.PrefManager;
import com.mobile.sales.Server;
import com.mobile.sales.databinding.ActivityOrderBinding;
import com.mobile.sales.databinding.ActivityOrderBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity  {
    private ActivityOrderBinding binding;
    PrefManager prefManager;
    List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AndroidNetworking.initialize(this);

        prefManager = new PrefManager(this);

        orderList = new ArrayList<>();
        binding.recyclerOrder.setHasFixedSize(true);
        binding.recyclerOrder.setLayoutManager(new LinearLayoutManager(this));

        row_pesanan();
    }

    private void row_pesanan() {
        AndroidNetworking.post(Server.site + "order.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() >= 1) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject d = response.getJSONObject(i);
                                    orderList.add(new Order(
                                            d.getString("id_order"),
                                            d.getString("nama_costumer"),
                                            d.getInt("total_harga"),
                                            d.getString("bentuk_pembayaran"),
                                            d.getString("tgl_order"),
                                            d.getInt("jumlah_item"),
                                            d.getInt("status")
                                    ));

                                }
                                OrderAdapter adapter = new OrderAdapter(OrderActivity.this, orderList);
                                binding.recyclerOrder.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("tag", "error Riwayat : " + anError);
                    }

                });

    }
}