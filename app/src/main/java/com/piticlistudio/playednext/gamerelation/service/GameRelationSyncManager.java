package com.piticlistudio.playednext.gamerelation.service;

import android.app.AlarmManager;
import android.util.Log;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * A manager for syncing game relations
 * Created by jorge on 29/6/17.
 */

public class GameRelationSyncManager {

    private final IGameRelationRepository localRelationRepository;
    private final IGameRepository gameRepository;

    @Inject
    public GameRelationSyncManager(IGameRelationRepository localRelationRepository, IGameRepository gameRepository) {
        this.localRelationRepository = localRelationRepository;
        this.gameRepository = gameRepository;
    }

    public Observable<Game> sync() {
        return localRelationRepository.loadAll()
                .take(1)
                .observeOn(Schedulers.io())
                .flatMap(iGameRelationDatasources ->
                        Observable.fromIterable(iGameRelationDatasources)
                                .filter(gameRelation -> System.currentTimeMillis() - gameRelation.game().syncedAt > AlarmManager.INTERVAL_DAY*3)
                                .flatMap(gameRelation -> gameRepository.load(gameRelation.game().id())
                                        .onErrorReturnItem(gameRelation.game())
                                        .flatMap(game -> gameRepository.save(game).toObservable())
                                )
                );
    }
}
