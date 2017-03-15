package com.piticlistudio.playednext.game;

import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.game.ui.detail.interactor.GameDetailInteractor;
import com.piticlistudio.playednext.game.ui.detail.presenter.GameDetailPresenter;
import com.piticlistudio.playednext.game.ui.search.GameSearchContract;
import com.piticlistudio.playednext.game.ui.search.interactor.GameSearchInteractor;
import com.piticlistudio.playednext.game.ui.search.presenter.GameSearchPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * A module that provides Game classes
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class GameModule {

    @Provides
    public GameDetailContract.Interactor provideDetailInteractor(GameRepository repository) {
        return new GameDetailInteractor(repository);
    }

    @Provides
    public GameSearchContract.Interactor provideSearchInteractor(GameRepository repository) {
        return new GameSearchInteractor(repository);
    }

    @Provides
    public GameSearchContract.Presenter provideSearchPresenter(GameSearchContract.Interactor interactor) {
        return new GameSearchPresenter(interactor);
    }

    @Provides
    public GameDetailContract.Presenter provideDetailPresenter(GameDetailContract.Interactor interactor) {
        return new GameDetailPresenter(interactor);
    }
}
