package com.example.prueba_tecnica.details;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class DetailsPresenter implements DetailsContract.Presenter {
    private DetailsContract.View mView;

    public DetailsPresenter(DetailsContract.View mView) {
        this.mView = mView;
    }

    public  boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
