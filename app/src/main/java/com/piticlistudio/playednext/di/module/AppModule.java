package com.piticlistudio.playednext.di.module;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    public Context provideContext() {
        return this.application;
    }

    @Provides
    public Picasso picasso(Context context) {
        return Picasso.with(context);
    }
}
