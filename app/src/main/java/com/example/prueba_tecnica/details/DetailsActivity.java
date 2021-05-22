package com.example.prueba_tecnica.details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prueba_tecnica.R;
import com.example.prueba_tecnica.favorites.FavoriteContract;
import com.example.prueba_tecnica.favorites.FavoritePresenter;
import com.example.prueba_tecnica.gallery.GalleryContract;
import com.google.android.material.snackbar.Snackbar;

public class DetailsActivity extends AppCompatActivity implements  DetailsContract.View {

    private  ImageView imageView;
    private DetailsContract.Presenter mPresenter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_imagen);
        imageView= findViewById(R.id.imagen_details);
        mPresenter = new DetailsPresenter(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra(getResources().getString(R.string.url));
        this.findViewById(android.R.id.content);
        showImage(url);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.title);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void showImage( String url){
        if(mPresenter.isOnline(DetailsActivity.this)) {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
        }else{
            setMsjError();
        }
    }


    public void setMsjError() {

        Snackbar snackbar = Snackbar.make(getApplication(), getWindow().getCurrentFocus() , getResources().getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE).setAction(R.string.closed, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.show();
    }
}
