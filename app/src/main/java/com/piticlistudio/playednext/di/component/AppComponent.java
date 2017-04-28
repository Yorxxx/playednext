package com.piticlistudio.playednext.di.component;

import android.content.Context;

import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.di.module.IGDBModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.GameRelationModule;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;
import com.piticlistudio.playednext.platform.ui.PlatformUIUtils;
import com.piticlistudio.playednext.relationinterval.RelationIntervalModule;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class,
        GameRelationModule.class, RelationIntervalModule.class, IGDBModule.class, GameModule.class,
        CollectionModule.class, CompanyModule.class, GenreModule.class, PlatformModule.class})
public interface AppComponent {

    Context context();

    Picasso picasso();

    PlatformUIUtils platformUtils();

    IGameRelationRepository repository();
    IGameRepository gameRepository();
    RelationIntervalRepository intervalRepository();
}
