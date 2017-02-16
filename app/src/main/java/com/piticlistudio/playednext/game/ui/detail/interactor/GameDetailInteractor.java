package com.piticlistudio.playednext.game.ui.detail.interactor;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Interactor implementation
 * Created by jorge.garcia on 15/02/2017.
 */

public class GameDetailInteractor implements GameDetailContract.Interactor {

    private final GameRepository repository;

    @Inject
    public GameDetailInteractor(GameRepository repository) {
        this.repository = repository;
    }

    /**
     * Loads the game with the specified id.
     *
     * @param id the id to load
     * @return an Observable that emits the loaded data
     */
    @Override
    public Observable<Game> load(int id) {
        return repository.load(id);
    }
}
