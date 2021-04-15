package com.mobile.sales.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobile.sales.Activity.DetailOrderActivity;
import com.mobile.sales.Activity.OrderActivity;
import com.mobile.sales.Model.Order;
import com.mobile.sales.PrefManager;
import com.mobile.sales.R;
import com.mobile.sales.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewRecHolder> {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewRecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_order, null);
        return new OrderAdapter.OrderViewRecHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewRecHolder holder, int position) {
        final Order order = orderList.get(position);
        holder.namaCustomer.setText(order.getNama_costumer());
        holder.tglOrder.setText(order.getTgl_order());
        holder.bentukPembayaran.setText(order.getBentuk_pembayaran());
        Locale locale = new Locale("in", "ID");
        final NumberFormat rp = NumberFormat.getCurrencyInstance(locale);
        holder.totalHarga.setText(rp.format(order.getTotal_harga()));
        holder.jumlahItem.setText(order.getJumlah_item() + " Item");
        holder.idOrder.setText(order.getId_order());
        int s = order.getStatus();
        if(s == 1){
            holder.statusACC.setVisibility(View.VISIBLE);
        }else{
            holder.statusACC.setVisibility(View.GONE);
        }
        holder.layoutOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailOrderActivity.class);
                i.putExtra("id_order", order.getId_order());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewRecHolder extends RecyclerView.ViewHolder {
        TextView namaCustomer, tglOrder, bentukPembayaran, totalHarga, jumlahItem, idOrder, statusACC;
        RelativeLayout layoutOrder;

        public OrderViewRecHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            namaCustomer = itemView.findViewById(R.id.namaCustomer);
            tglOrder = itemView.findViewById(R.id.tglOrder);
            bentukPembayaran = itemView.findViewById(R.id.bentukPembayaran);
            totalHarga = itemView.findViewById(R.id.totalHarga);
            jumlahItem = itemView.findViewById(R.id.jumlahItem);
            idOrder = itemView.findViewById(R.id.idOrder);
            layoutOrder = itemView.findViewById(R.id.layoutOrder);
            statusACC = itemView.findViewById(R.id.statusACC);
        }
    }
}
