package com.piticlistudio.playednext.game.ui.search.interactor;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.ui.search.GameSearchContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Interactor implementation.
 * Created by jorge.garcia on 21/02/2017.
 */

public class GameSearchInteractor implements GameSearchContract.Interactor {

    private final IGameRepository repository;

    @Inject
    public GameSearchInteractor(IGameRepository repository) {
        this.repository = repository;
    }

    /**
     * Searches games with the specified query match
     *
     * @param query  the name of the game to search
     * @param offset the number of items to skip
     * @param limit  the max amount items to return
     * @return an Observable that emits the list of games loaded
     */
    @Override
    public Observable<List<Game>> search(String query, int offset, int limit) {
        return repository.search(query, offset, limit);
    }
}
