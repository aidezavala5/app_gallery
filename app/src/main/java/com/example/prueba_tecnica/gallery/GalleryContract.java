package com.example.prueba_tecnica.gallery;

import android.content.Context;

import com.example.prueba_tecnica.objects.Imagen;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class GalleryContract {

    interface View {

        void setMsjError();
        void setArrayImages(ArrayList<Imagen> images);


    }
    interface Presenter {

        void loadImages(Context context);
        void  searchImage(Context context, String title);
        boolean isOnline(Context context);
        void setAnalyticsItemSelect(FirebaseAnalytics mFirebaseAnalytics, Context context, Imagen image);
    }
}
