package com.piticlistudio.playednext.game.ui.detail;

import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.ui.detail.interactor.GameDetailInteractor;
import com.piticlistudio.playednext.game.ui.detail.presenter.GameDetailPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class GameDetailModule {

    @Provides
    public GameDetailContract.Interactor provideInteractor(IGameRepository repository) {
        return new GameDetailInteractor(repository);
    }

    @Provides
    public GameDetailContract.Presenter providePresenter(GameDetailContract.Interactor interactor) {
        return new GameDetailPresenter(interactor);
    }
}
