package com.piticlistudio.playednext.di.module;

import android.app.Application;
import android.content.Context;

import com.piticlistudio.playednext.platform.ui.PlatformUIUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return this.application;
    }

    @Provides
    @Singleton
    public Picasso picasso(Context context) {
        Picasso picasso = Picasso.with(context);
        picasso.setIndicatorsEnabled(true);
        return picasso;
    }

    @Provides
    @Singleton
    public PlatformUIUtils providePlatformUtils() {
        return new PlatformUIUtils();
    }

}
