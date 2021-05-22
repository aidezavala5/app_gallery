package com.example.prueba_tecnica.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prueba_tecnica.R;
import com.example.prueba_tecnica.objects.Imagen;
import com.example.prueba_tecnica.preference.SharedPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class AdapterFavorites  extends RecyclerView.Adapter<AdapterFavorites.PixabayImageViewHolder> {

    private List<Imagen> pixabayImageList;
    private Context context;
    private static AdapterFavorites.OnItemClickListener itemClickListener;
    private AdapterFavorites.OnItemClickListener listener;
    SharedPreference sharedPreference;
    private FirebaseAnalytics mFirebaseAnalytics;


    public AdapterFavorites(List<Imagen> pixabayImageList, Context context, AdapterFavorites.OnItemClickListener listener) {
        this.pixabayImageList = pixabayImageList;
        this.context = context;
        this.listener = listener;
        this.sharedPreference = new SharedPreference();
        this.mFirebaseAnalytics = mFirebaseAnalytics;
    }

    @Override
    public AdapterFavorites.PixabayImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_imagen, parent, false);
        AdapterFavorites.PixabayImageViewHolder myViewHolder = new AdapterFavorites.PixabayImageViewHolder(view);
        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(AdapterFavorites.PixabayImageViewHolder holder, int position) {
        holder.title.setText(pixabayImageList.get(position).getTags());
        String ruta = "";
        ruta = pixabayImageList.get(position).getImageURL();
        if (ruta.equals("")) {
            holder.imageView.setImageDrawable(null);
        } else {
            Glide.with(context)
                    .load(ruta)
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.imageView);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    listener.onItemClickListener(pixabayImageList.get(position));
                                                }
                                            }

        );

    }

    @Override
    public int getItemCount() {
        return pixabayImageList.size();
    }

    public static class PixabayImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        ImageView favorite;

        public PixabayImageViewHolder(View v) {
            super(v);
            this.imageView = itemView.findViewById(R.id.imagen);
            this.title = itemView.findViewById(R.id.title);
            this.favorite = itemView.findViewById(R.id.image_favorite);
            this.favorite.setImageResource(R.drawable.ic_heart_white);
            favorite.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener {
        /**
         * @param image
         */

        public void onItemClickListener(Imagen image);
    }


    public void setOnItemClickListener(AdapterImagen.OnItemClickListener itemClickListene) {
        AdapterFavorites.itemClickListener = itemClickListener;
    }

}