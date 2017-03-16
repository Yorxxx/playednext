package com.piticlistudio.playednext.game.ui.search;

import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.ui.search.interactor.GameSearchInteractor;
import com.piticlistudio.playednext.game.ui.search.presenter.GameSearchPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class GameSearchModule {

    @Provides
    public GameSearchContract.Interactor provideInteractor(IGameRepository repository) {
        return new GameSearchInteractor(repository);
    }

    @Provides
    public GameSearchContract.Presenter providePresenter(GameSearchContract.Interactor interactor) {
        return new GameSearchPresenter(interactor);
    }
}
