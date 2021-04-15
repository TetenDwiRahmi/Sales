package com.mobile.sales.Model;

public class DetailOrder {
    private int id;
    private String nama_barang;
    private int harga;
    private int jml_barang;

    public DetailOrder(int id, String nama_barang, int harga, int jml_barang) {
        this.id = id;
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.jml_barang = jml_barang;
    }

    public int getId() {
        return id;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public int getHarga() {
        return harga;
    }

    public int getJml_barang() {
        return jml_barang;
    }
}
