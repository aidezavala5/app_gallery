package com.example.prueba_tecnica.details;

import android.content.Context;

import com.example.prueba_tecnica.objects.Imagen;

import java.util.ArrayList;

public class DetailsContract {


    interface View {

    }

    interface Presenter {

        boolean isOnline(Context context);
    }

}
