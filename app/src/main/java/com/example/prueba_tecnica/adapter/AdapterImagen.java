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

//Adaptador para mostrar la galeria de imagenes
public class AdapterImagen extends RecyclerView.Adapter<AdapterImagen.PixabayImageViewHolder> {

    private List<Imagen> pixabayImageList;
    private Context context;
    private static OnItemClickListener itemClickListener;
    private AdapterImagen.OnItemClickListener listener;
    SharedPreference sharedPreference;
    private FirebaseAnalytics mFirebaseAnalytics;


    public AdapterImagen(List<Imagen> pixabayImageList, Context context, AdapterImagen.OnItemClickListener listener, FirebaseAnalytics mFirebaseAnalytics) {
        this.pixabayImageList = pixabayImageList;
        this.context = context;
        this.listener = listener;
        this.sharedPreference = new SharedPreference();
        this.mFirebaseAnalytics = mFirebaseAnalytics;
    }

    @Override
    public PixabayImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_imagen, parent, false);
        PixabayImageViewHolder myViewHolder = new PixabayImageViewHolder(view);
        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(PixabayImageViewHolder holder, int position) {
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

        if (checkFavoriteItem(pixabayImageList.get(position), false)) {
            holder.favorite.setImageResource(R.drawable.ic_heart);
        } else {
            holder.favorite.setImageResource(R.drawable.ic_heart_white);
        }


        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFavoriteItem(pixabayImageList.get(position), true)) {
                    holder.favorite.setImageResource(R.drawable.ic_heart_white);
                    Toast.makeText(context,
                            context.getResources().getString(
                                    R.string.remove_favr),
                            Toast.LENGTH_SHORT).show();
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_heart);
                    sharedPreference.addFavorite(context, pixabayImageList.get(position), mFirebaseAnalytics);
                    Toast.makeText(context,
                            context.getResources().getString(R.string.add_favr),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

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
        }
    }

    public interface OnItemClickListener {
        /**
         * @param image
         */

        public void onItemClickListener(Imagen image);
    }


    public void setOnItemClickListener(AdapterImagen.OnItemClickListener itemClickListene) {
        AdapterImagen.itemClickListener = itemClickListener;
    }

    public boolean checkFavoriteItem(Imagen checkImagen, Boolean onClick) {
        boolean check = false;
        int i = 0;
        List<Imagen> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (Imagen product : favorites) {
                if (product.getId().equals(checkImagen.getId())) {
                    check = true;
                    if (onClick) {
                        sharedPreference.removeFavorite(context, i, mFirebaseAnalytics, product);
                    }
                    break;
                }
            }
        }
        return check;
    }


}
