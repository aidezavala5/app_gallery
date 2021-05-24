package com.example.prueba_tecnica.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba_tecnica.R;
import com.example.prueba_tecnica.adapter.AdapterFavorites;
import com.example.prueba_tecnica.adapter.AdapterImagen;
import com.example.prueba_tecnica.details.DetailsActivity;
import com.example.prueba_tecnica.gallery.GalleryActivity;
import com.example.prueba_tecnica.gallery.GalleryContract;
import com.example.prueba_tecnica.gallery.GalleryPresenter;
import com.example.prueba_tecnica.objects.Imagen;
import com.example.prueba_tecnica.preference.SharedPreference;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

//clase para mostrar las imagenes favoritas guardadas
public class FavoriteActivity extends AppCompatActivity implements AdapterFavorites.OnItemClickListener , FavoriteContract.View {

    private RecyclerView recyclerView;
    private SharedPreference sharedPreference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FavoriteContract.Presenter mPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_list);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPreference = new SharedPreference();
        mPresenter = new FavoritePresenter(this);
        toolbar.setTitle(getResources().getString(R.string.title_fav));
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPresenter.getFavorites(getApplicationContext(),sharedPreference);
    }

    @Override
    public void setMsjError() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content) , getResources().getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE).setAction(R.string.closed, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.show();
    }

    public void sowFavorites(List<Imagen> fav){
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(FavoriteActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        AdapterFavorites adapterImagen = new AdapterFavorites(fav ,FavoriteActivity.this, FavoriteActivity.this::onItemClickListener);
        recyclerView.setAdapter(adapterImagen);
    }

    @Override
    public void onItemClickListener(Imagen image) {
        mPresenter.setAnalyticsItemSelect(mFirebaseAnalytics,getApplicationContext(),image);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.url),image.getImageURL());
        startActivity(intent);
    }
}
