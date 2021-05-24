package com.example.prueba_tecnica.objects;

import android.text.TextUtils;

//Clase que contiene el objeto Imagen
public class Imagen {
    private String id;
    private String tags;
    private String imageURL;


    public Imagen(String imageUrl, String title, String id) {
        this.imageURL = imageUrl;
        this.tags = title;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setTags(String tags) {
        this.tags = tags;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTags() {
        if (tags == null) return "";
        if (tags.contains(", ")) {
            String[] splitTags = tags.toUpperCase().split(", ");
            return TextUtils.join(" - ", splitTags);
        } else return tags;
    }


}
