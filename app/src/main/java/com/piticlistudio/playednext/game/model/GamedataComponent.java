package com.piticlistudio.playednext.game.model;


import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.di.module.IGDBModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.repository.datasource.GamedataRepository;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import dagger.Component;

@Component(modules = {GamedataModule.class, IGDBModule.class})
public interface GamedataComponent {

    GamedataRepository repository();
    GameMapper mapper();
    RealmGameMapper realmMapper();

    GameComponent plus(AppModule appModule, GameModule module, CollectionModule cm, CompanyModule com, GenreModule gm, PlatformModule pm);
}
