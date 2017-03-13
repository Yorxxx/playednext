package com.piticlistudio.playednext.game;


import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.game.ui.detail.presenter.GameDetailPresenter;
import com.piticlistudio.playednext.game.ui.detail.view.adapter.GameDetailAdapter;
import com.piticlistudio.playednext.game.ui.search.presenter.GameSearchPresenter;
import com.piticlistudio.playednext.game.ui.search.view.adapter.GameSearchAdapter;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import dagger.Subcomponent;

@Subcomponent(modules = {AppModule.class, GameModule.class, CollectionModule.class, CompanyModule.class, GenreModule.class, PlatformModule
        .class})
public interface GameComponent {

    GameRepository repository();

    GameDetailContract.Presenter detailPresenter();

    GameDetailAdapter detailAdapter();

    GameSearchPresenter searchPresenter();

    GameSearchAdapter searchAdapter();
}
