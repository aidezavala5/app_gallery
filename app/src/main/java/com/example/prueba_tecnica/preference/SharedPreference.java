package com.example.prueba_tecnica.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.prueba_tecnica.R;
import com.example.prueba_tecnica.objects.Imagen;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Clase que se encarga de guardar la lista de favoritos ,y contiene los metodos para agregar y eliminar de favoritos
public class SharedPreference {

    public static final String PREFS_NAME = "IMAGE_APP";
    public static final String FAVORITES = "IMAGE_FAVORITE";


    public SharedPreference() {
        super();
    }

    public void saveFavorites(Context context, List<Imagen> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Imagen image, FirebaseAnalytics mFirebaseAnalytics) {
        List<Imagen> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Imagen>();
        favorites.add(image);
        saveFavorites(context, favorites);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, image.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, context.getResources().getString(R.string.add_fav));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, context.getResources().getString(R.string.image_add) + image.getId());
        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent(context.getResources().getString(R.string.name_event), params);
    }

    public void removeFavorite(Context context, int pos, FirebaseAnalytics mFirebaseAnalytics, Imagen image) {
        ArrayList<Imagen> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(pos);
            saveFavorites(context, favorites);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, image.getId());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, context.getResources().getString(R.string.name_removed));
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, context.getResources().getString(R.string.image_remov) + image.getId());
            Bundle params = new Bundle();
            mFirebaseAnalytics.logEvent(context.getResources().getString(R.string.name_event_remov), params);
        }

    }

    public ArrayList<Imagen> getFavorites(Context context) {
        SharedPreferences settings;
        List<Imagen> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Imagen[] favoriteItems = gson.fromJson(jsonFavorites,
                    Imagen[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Imagen>(favorites);
        } else
            return null;

        return (ArrayList<Imagen>) favorites;
    }

}
