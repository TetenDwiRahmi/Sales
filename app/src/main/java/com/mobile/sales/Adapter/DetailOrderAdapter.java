package com.mobile.sales.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobile.sales.Interface.InterfaceUpdate;
import com.mobile.sales.Model.DetailOrder;
import com.mobile.sales.PrefManager;
import com.mobile.sales.R;
import com.mobile.sales.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.DetailViewRecHolder> {
    private List<DetailOrder> detailOrderList;
    private Context context;
    EditText editHarga, editJml;
    int id;
    InterfaceUpdate updateData;

    public DetailOrderAdapter(List<DetailOrder> detailOrderList, InterfaceUpdate updateData) {
        this.detailOrderList = detailOrderList;
        this.updateData = updateData;
    }

    @NonNull
    @Override
    public DetailOrderAdapter.DetailViewRecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_barang, null);
        return new DetailOrderAdapter.DetailViewRecHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailOrderAdapter.DetailViewRecHolder holder, final int position) {
        final DetailOrder detailOrder = detailOrderList.get(position);
        holder.namaBarang.setText(detailOrder.getNama_barang());
        Locale locale = new Locale("in", "ID");
        final NumberFormat rp = NumberFormat.getCurrencyInstance(locale);
        holder.hargaBarang.setText(rp.format(detailOrder.getHarga()));
        holder.jmlBarang.setText("" + detailOrder.getJml_barang());
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = detailOrder.getId();
                Log.d("tag", " ID Barang Adapter : " + id);

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                final View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.activity_update_pesanan, null);

                editHarga = dialogView.findViewById(R.id.editHarga);
                editJml = dialogView.findViewById(R.id.editJml);
                builder.setCancelable(true);

                AndroidNetworking.post(Server.site + "get_harga_jml.php")
                        .addBodyParameter("id", String.valueOf(detailOrder.getId()))
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
                                    Toast.makeText(context, "Item Tidak Ada", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("tag", "errordetail : " + anError);
                            }
                        });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateHargaJumlah();
                    }
                });

            }
        });
    }

    private void updateHargaJumlah() {
        AndroidNetworking.post(Server.site + "update_detail.php")
                .addBodyParameter("id", String.valueOf(id))
                .addBodyParameter("kode", String.valueOf(1))
                .addBodyParameter("harga", editHarga.getText().toString())
                .addBodyParameter("jml_barang", editJml.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getString("status").equalsIgnoreCase("1")) {
                                updateData.UpdateData();
                                Toast.makeText(context, "Berhasil Update Data", Toast.LENGTH_SHORT).show();
                            } else {
                                updateData.UpdateData();
                                Toast.makeText(context, "Gagal Update Data", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return detailOrderList.size();
    }

    public class DetailViewRecHolder extends RecyclerView.ViewHolder {
        TextView namaBarang, hargaBarang, jmlBarang;
        ImageView imgEdit;

        public DetailViewRecHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            namaBarang = itemView.findViewById(R.id.namaBarang);
            hargaBarang = itemView.findViewById(R.id.hargaBarang);
            jmlBarang = itemView.findViewById(R.id.jmlBarang);
            imgEdit = itemView.findViewById(R.id.imgEdit);

        }
    }

}
