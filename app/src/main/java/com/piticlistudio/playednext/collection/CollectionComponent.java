package com.piticlistudio.playednext.collection;

import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.di.module.IGDBModule;

import dagger.Component;

@Component(modules = {CollectionModule.class, IGDBModule.class})
public interface CollectionComponent {

    ICollectionRepository repository();
}
