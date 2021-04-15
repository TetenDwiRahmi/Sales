package com.mobile.sales.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.mobile.sales.Adapter.DetailOrderAdapter;
import com.mobile.sales.Interface.InterfaceUpdate;
import com.mobile.sales.Model.DetailOrder;
import com.mobile.sales.PrefManager;
import com.mobile.sales.Server;
import com.mobile.sales.databinding.ActivityDetailOrderBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailOrderActivity extends AppCompatActivity implements InterfaceUpdate {
    private ActivityDetailOrderBinding binding;
    List<DetailOrder> detailOrderList;
    String id_order;
    int tot, hrg,jml;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AndroidNetworking.initialize(this);

        prefManager = new PrefManager(this);

        Intent i = new Intent(getIntent());
        id_order = i.getStringExtra("id_order");
        Log.d("id_order", "ID ORDER : " + id_order);

        detailOrderList = new ArrayList<>();
        binding.recyclerDO.setHasFixedSize(true);
        binding.recyclerDO.setLayoutManager(new LinearLayoutManager(this));

        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailOrderActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });

        binding.btnBelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });

        getDetailOrder();
    }

    private void updateStatus() {
        AndroidNetworking.post(Server.site + "update_detail.php")
                .addBodyParameter("id_order", id_order)
                .addBodyParameter("kode", String.valueOf(2))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                Log.d("tag", "Status Berhasil diubah");
                                binding.btnACC.setVisibility(View.VISIBLE);
                                binding.btnBelum.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(DetailOrderActivity.this, "Gagal Update Data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("tag", "Koneksi Bermasalah");
                    }
                });
    }


    private void getDetailOrder() {
        AndroidNetworking.post(Server.site + "get_costumer.php")
                .addBodyParameter("id_order", id_order)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject data = response.getJSONObject(i);
                                binding.namaCostumerDO.setText(data.getString("nama_costumer"));
                                binding.alamatCostumerDO.setText(data.getString("alamat_costumer"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "code : " + anError);
                        Toast.makeText(DetailOrderActivity.this, "Gagal Get Data Customer", Toast.LENGTH_SHORT).show();
                    }
                });

        AndroidNetworking.post(Server.site + "detail_order.php")
                .addBodyParameter("id_order", id_order)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() >= 1) {
                            try {
                                tot = 0;
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject k = response.getJSONObject(i);
                                    detailOrderList.add(new DetailOrder(
                                            k.getInt("id"),
                                            k.getString("nama_barang"),
                                            k.getInt("harga"),
                                            k.getInt("jml_barang")
                                    ));

                                    hrg = k.getInt("harga");
                                    jml = k.getInt("jml_barang");
                                    tot = (hrg*jml) + tot;

                                    prefManager.setTotal(tot);
                                    updateharga();

                                    Locale locale = new Locale("in", "ID");
                                    final NumberFormat rp = NumberFormat.getCurrencyInstance(locale);
                                    binding.totalBelanja.setText(rp.format(tot));

                                    prefManager.setStatus(k.getString("status"));
                                    Log.d("tag", "Status : " + prefManager.getStatus() + " di ID ORD : " + id_order);

                                    if (Integer.parseInt(prefManager.getStatus()) == 1) {
                                        binding.btnACC.setVisibility(View.VISIBLE);
                                        binding.btnBelum.setVisibility(View.GONE);
                                    }

                                }
                                adapterDetail();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DetailOrderActivity.this, "Item Tidak Ada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("tag", "errordetail : " + anError);
                    }
                });
    }

    private void adapterDetail() {
        DetailOrderAdapter adapter = new DetailOrderAdapter(detailOrderList, this);
        binding.recyclerDO.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void UpdateData() {
        AndroidNetworking.post(Server.site + "detail_order.php")
                .addBodyParameter("id_order", id_order)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() >= 1) {
                            detailOrderList.clear();
                            try {
                                tot=0;
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject k = response.getJSONObject(i);
                                    detailOrderList.add(new DetailOrder(
                                            k.getInt("id"),
                                            k.getString("nama_barang"),
                                            k.getInt("harga"),
                                            k.getInt("jml_barang")
                                    ));

                                    hrg = k.getInt("harga");
                                    jml = k.getInt("jml_barang");
                                    tot = (hrg*jml) + tot;

                                    Locale locale = new Locale("in", "ID");
                                    final NumberFormat rp = NumberFormat.getCurrencyInstance(locale);
                                    binding.totalBelanja.setText(rp.format(tot));
                                    adapterDetail();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DetailOrderActivity.this, "Item Tidak Ada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("tag", "errordetail : " + anError);
                    }
                });
    }


    private void updateharga() {
        AndroidNetworking.post(Server.site + "update_detail.php")
                .addBodyParameter("id_order", String.valueOf(id_order))
                .addBodyParameter("kode", String.valueOf(3))
                .addBodyParameter("total_harga", String.valueOf(prefManager.getTotal()))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getString("status").equalsIgnoreCase("1")) {
                                Log.d("tag", "Update Total di Order Berhasil : " + prefManager.getTotal());
                            } else {
                                Log.d("tag", "Update Total di Order Gagal");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("tag", "Koneksi Bermasalah");
                    }
                });
    }
}