package com.example.prueba_tecnica.gallery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba_tecnica.R;
import com.example.prueba_tecnica.objects.Imagen;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryPresenter implements GalleryContract.Presenter {

    private GalleryContract.View mView;
    private String IMAGE_TAG = "webformatURL";
    private String TITLE_TAG = "tags";
    private String ID_TAG = "id";

    public GalleryPresenter(GalleryContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadImages(Context context) {
        ArrayList<Imagen> imagenList1 = new ArrayList<>();
        String sURL = context.getResources().getString(R.string.api);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjectRequest;

        if (isOnline(context)) {
            jsObjectRequest = new JsonObjectRequest(Request.Method.GET, sURL, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Imagen imagen = new Imagen("", "", "");
                                JSONArray jsonArray = response.getJSONArray(context.getResources().getString(R.string.hits));
                                for (int i = 0; i < 20; i++) {
                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String imageUrl = hit.getString(IMAGE_TAG);
                                    String title = hit.getString(TITLE_TAG);
                                    String id = hit.getString(ID_TAG);
                                    imagen.setImageURL(imageUrl);
                                    imagen.setTags(title);
                                    imagenList1.add(new Imagen(imageUrl, title, id));

                                }
                                mView.setArrayImages(imagenList1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // initSnackbar(R.string.error);
                            mView.setMsjError();

                        }
                    });
            requestQueue.add(jsObjectRequest);

        }else{
            mView.setMsjError();
        }
    }



    @Override
    public void  searchImage(Context context, String title) {

        ArrayList<Imagen> imagenList1 = new ArrayList<>();
        String sURL = context.getResources().getString(R.string.api);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjectRequest;
        jsObjectRequest = new JsonObjectRequest(Request.Method.GET, sURL, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Imagen imagen = new Imagen("", "","");
                            JSONArray jsonArray = response.getJSONArray(context.getResources().getString(R.string.hits));
                            for (int i = 0; i < 20; i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String imageUrl = hit.getString(IMAGE_TAG);
                                String title = hit.getString(TITLE_TAG);
                                String id= hit.getString(ID_TAG);
                                imagen.setImageURL(imageUrl);
                                imagen.setTags(title);
                                imagenList1.add(new Imagen(imageUrl, title,id));

                            }
                            search(title,imagenList1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // initSnackbar(R.string.error);
                        mView.setMsjError();

                    }
                });
        requestQueue.add(jsObjectRequest);

    }

    public void search(String title,ArrayList<Imagen>listImagen) {
        ArrayList<Imagen> resultImage= new ArrayList<>();
        for (int i = 0; i < listImagen.size(); i++) {
            if (listImagen.get(i).getTags().contains(title)) {
               resultImage.add(listImagen.get(i));
            }
        }
        if(resultImage.size()>0){
            mView.setArrayImages(resultImage);
        }else{
            mView.setArrayImages(resultImage);
        }
    }



    public  boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    @Override
    public void setAnalyticsItemSelect(FirebaseAnalytics mFirebaseAnalytics, Context context, Imagen image) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, image.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, context.getString(R.string.select));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, context.getString(R.string.selected)+image.getId());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);
    }
}
