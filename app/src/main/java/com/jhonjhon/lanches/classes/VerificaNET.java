package com.jhonjhon.lanches.classes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class VerificaNET {
    Activity actividade;
    public VerificaNET(Activity activity) {
        this.actividade = activity;
    }
    public boolean IsInternet(){
        ConnectivityManager connectivityManager=(ConnectivityManager) actividade.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null;
    }
}
