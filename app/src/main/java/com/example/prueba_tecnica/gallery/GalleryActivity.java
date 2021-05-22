package com.example.prueba_tecnica.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.prueba_tecnica.details.DetailsActivity;
import com.example.prueba_tecnica.R;
import com.example.prueba_tecnica.adapter.AdapterImagen;
import com.example.prueba_tecnica.favorites.FavoriteActivity;
import com.example.prueba_tecnica.objects.Imagen;
import com.example.prueba_tecnica.preference.SharedPreference;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements AdapterImagen.OnItemClickListener , GalleryContract.View {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private GalleryContract.Presenter mPresenter;
    private Toolbar toolbar;
    private MenuItem searchMenuItem;
    private  TextView noResults;
    private FirebaseAnalytics mFirebaseAnalytics;
    SharedPreference sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mPresenter = new GalleryPresenter(this);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_list);
        progressBar = (ProgressBar) findViewById(R.id.activity_main_progress);
        mPresenter.loadImages(GalleryActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title));
        setSupportActionBar(toolbar);
        noResults = (TextView) findViewById(R.id.activity_main_no_results_text);
        noResults.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        this.sharedPreference = new SharedPreference();

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        getMenuInflater().inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(searchListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all:
                mPresenter.loadImages(GalleryActivity.this);
                return true;
            case R.id.action_favorite:

                List<Imagen> favorites = sharedPreference.getFavorites(GalleryActivity.this);
                if (favorites.size() > 0) {
                    Intent intent = new Intent(this, FavoriteActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar snackbar = Snackbar.make(recyclerView, "no hay favoritos", Snackbar.LENGTH_INDEFINITE).setAction(R.string.closed, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String title) {
            searchMenuItem.collapseActionView();
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.GONE);
            mPresenter.searchImage(GalleryActivity.this,title.toUpperCase());
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };




    @Override
    public void setArrayImages(ArrayList<Imagen> images) {
        if (images.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this, 2);
            recyclerView.setLayoutManager(mLayoutManager);

            AdapterImagen adapterImagen = new AdapterImagen(images, GalleryActivity.this, GalleryActivity.this::onItemClickListener,mFirebaseAnalytics);
            recyclerView.setAdapter(adapterImagen);
        } else {
            noResults.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void initSnackbar() {
        progressBar.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(recyclerView, getResources().getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE).setAction(R.string.closed, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.show();
    }

    @Override
    public void onItemClickListener(Imagen image) {

      /*  Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, image.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getResources().getString(R.string.select));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, getResources().getString(R.string.selected)+image.getId());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);*/
        mPresenter.setAnalyticsItemSelect(mFirebaseAnalytics,getApplicationContext(),image);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.url),image.getImageURL());
        startActivity(intent);

    }

    @Override
    public void setMsjError() {
        initSnackbar();

    }



}