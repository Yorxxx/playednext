package com.piticlistudio.playednext.game;


import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.module.IGDBModule;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import dagger.Component;

@Component(modules = {IGDBModule.class, GameModule.class, CollectionModule.class, CompanyModule.class, GenreModule.class, PlatformModule
        .class})
public interface GameComponent {

    IGameRepository repository();
}
