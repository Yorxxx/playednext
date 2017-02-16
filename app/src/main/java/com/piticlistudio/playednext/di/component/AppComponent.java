package com.piticlistudio.playednext.di.component;

import android.content.Context;

import com.piticlistudio.playednext.di.module.AppModule;
import com.squareup.picasso.Picasso;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {

    Context context();

    Picasso picasso();
}
