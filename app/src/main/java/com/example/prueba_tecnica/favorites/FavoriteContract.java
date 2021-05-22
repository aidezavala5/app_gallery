package com.example.prueba_tecnica.favorites;

import android.content.Context;

import com.example.prueba_tecnica.objects.Imagen;
import com.example.prueba_tecnica.preference.SharedPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class FavoriteContract {

    interface View {

        void setMsjError();
        void sowFavorites(List<Imagen> fav);


    }
    interface Presenter {

        void getFavorites(Context context, SharedPreference sharedPreference);
        void setAnalyticsItemSelect(FirebaseAnalytics mFirebaseAnalytics, Context context, Imagen image);
    }
}
