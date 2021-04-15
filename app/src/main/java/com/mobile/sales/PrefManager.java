package com.mobile.sales;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "data_app";

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIdUser(String idUser) {
        editor.putString("idUser", idUser);
        editor.apply();
    }

    public String getIdUser() {
        return pref.getString("idUser", "");
    }

    public void setTotal(int total) {
        editor.putInt("total", total);
        editor.apply();
    }

    public int getTotal() {
        return pref.getInt("total", 0);
    }

    public void setStatus(String status) {
        editor.putString("status", status);
        editor.apply();
    }

    public String getStatus() {
        return pref.getString("status", "");
    }

    public void setLoginStatus(boolean islogin) {
        editor.putBoolean("login", islogin);
        editor.apply();
    }

    public boolean getLoginStatus() {
        return pref.getBoolean("login", false);
    }

}
