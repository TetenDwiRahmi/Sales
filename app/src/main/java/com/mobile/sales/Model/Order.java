package com.mobile.sales.Model;

public class Order {
    private String id_order;
    private String nama_costumer;
    private int total_harga;
    private String bentuk_pembayaran;
    private String tgl_order;
    private int jumlah_item;
    private int status;

    public Order(String id_order, String nama_costumer, int total_harga, String bentuk_pembayaran, String tgl_order, int jumlah_item, int status) {
        this.id_order = id_order;
        this.nama_costumer = nama_costumer;
        this.total_harga = total_harga;
        this.bentuk_pembayaran = bentuk_pembayaran;
        this.tgl_order = tgl_order;
        this.jumlah_item = jumlah_item;
        this.status = status;
    }

    public String getId_order() {
        return id_order;
    }

    public String getNama_costumer() {
        return nama_costumer;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public String getBentuk_pembayaran() {
        return bentuk_pembayaran;
    }

    public String getTgl_order() {
        return tgl_order;
    }

    public int getJumlah_item() {
        return jumlah_item;
    }

    public int getStatus() {
        return status;
    }
}
