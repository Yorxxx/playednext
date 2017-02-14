package com.piticlistudio.playednext.image;

import com.piticlistudio.playednext.image.model.entity.ImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.RealmImageDataMapper;

import dagger.Module;
import dagger.Provides;

/**
 * Module for images
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class ImageModule {

    @Provides
    public ImageDataMapper provideMapper() {
        return new ImageDataMapper();
    }

    @Provides
    public RealmImageDataMapper provideRealmMapper() {
        return new RealmImageDataMapper();
    }
}
