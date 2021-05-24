package com.example.prueba_tecnica.favorites;

import android.content.Context;
import android.os.Bundle;

import com.example.prueba_tecnica.R;
import com.example.prueba_tecnica.details.DetailsContract;
import com.example.prueba_tecnica.gallery.GalleryContract;
import com.example.prueba_tecnica.objects.Imagen;
import com.example.prueba_tecnica.preference.SharedPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class FavoritePresenter implements FavoriteContract.Presenter {
    private FavoriteContract.View mView;

    public FavoritePresenter(FavoriteContract.View mView) {
        this.mView = mView;
    }

    public void getFavorites(Context context, SharedPreference sharedPreference) {
        List<Imagen> favorites = sharedPreference.getFavorites(context);
        if(favorites!=null) {
            if (favorites.size() > 0) {
                mView.sowFavorites(favorites);
            } else {
                mView.setMsjError();
            }
        }else{
            mView.setMsjError();
        }

    }

    public void setAnalyticsItemSelect(FirebaseAnalytics mFirebaseAnalytics, Context context, Imagen image) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, image.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, context.getString(R.string.select));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, context.getString(R.string.selected) + image.getId());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);

    }


}
