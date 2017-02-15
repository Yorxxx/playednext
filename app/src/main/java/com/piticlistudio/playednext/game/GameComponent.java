package com.piticlistudio.playednext.game;


import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;
import com.piticlistudio.playednext.platform.model.entity.Platform;

import dagger.Subcomponent;

@Subcomponent(modules = {GameModule.class, CollectionModule.class, CompanyModule.class, GenreModule.class, PlatformModule.class})
public interface GameComponent {

    GameRepository repository();
}
