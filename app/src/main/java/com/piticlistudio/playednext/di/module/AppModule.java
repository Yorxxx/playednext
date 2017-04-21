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
    public Context provideContext() {
        return this.application;
    }

    @Provides
    public Picasso picasso(Context context) {
        Picasso picasso = Picasso.with(context);
        picasso.setIndicatorsEnabled(true);
        return picasso;
    }

    @Provides
    public PlatformUIUtils providePlatformUtils() {
        return new PlatformUIUtils();
    }

}
