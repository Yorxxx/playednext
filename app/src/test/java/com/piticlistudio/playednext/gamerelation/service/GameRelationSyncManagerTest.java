package com.piticlistudio.playednext.gamerelation.service;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSubjectSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.gamerelation.ui.detail.presenter.GameRelationDetailPresenter;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Test cases for GameRelationSyncManager
 * Created by jorge on 29/6/17.
 */
public class GameRelationSyncManagerTest extends BaseTest {

    @Rule
    public TestSubjectSchedulerRule testSchedulerRule = new TestSubjectSchedulerRule();
    @Mock
    IGameRelationRepository relationRepository;
    @Mock
    IGameRepository gameRepository;

    @InjectMocks
    GameRelationSyncManager manager;

    @Test
    public void given_emptylist_then_should_not_request_data() throws Exception {
        doReturn(Observable.just(new ArrayList<>())).when(relationRepository).loadAll();

        // Act
        TestObserver<Game> result = manager.sync().test();
        result.awaitTerminalEvent();

        // Assert
        verify(relationRepository).loadAll();
        verifyZeroInteractions(gameRepository);
    }

    @Test
    public void given_relationsWithSyncedgame_Then_should_not_request_data() throws Exception {

        List<GameRelation> relations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Game game = Game.create(i, "title");
            game.syncedAt = System.currentTimeMillis();
            relations.add(GameRelation.create(game, System.currentTimeMillis()));
        }
        doReturn(Observable.just(relations)).when(relationRepository).loadAll();

        // Act
        TestObserver<Game> result = manager.sync().test();
        result.awaitTerminalEvent();

        // Assert
        verify(relationRepository).loadAll();
        verifyZeroInteractions(gameRepository);
    }

    @Test
    public void given_relationsWithNonSyncedData_Then_should_requestDataForUnsyncedOnes() throws Exception {
        Game syncedGame1 = Game.create(0, "title");
        syncedGame1.syncedAt = System.currentTimeMillis();
        Game syncedGame2 = Game.create(1, "title");
        syncedGame2.syncedAt = System.currentTimeMillis();
        Game unsyncedGame1 = Game.create(2, "title");
        Game syncedGame3 = Game.create(3, "title");
        syncedGame3.syncedAt = System.currentTimeMillis();
        Game unsyncedGame2 = Game.create(4, "title");

        List<GameRelation> relations = new ArrayList<>();
        relations.add(GameRelation.create(syncedGame1, System.currentTimeMillis()));
        relations.add(GameRelation.create(syncedGame2, System.currentTimeMillis()));
        relations.add(GameRelation.create(unsyncedGame1, System.currentTimeMillis()));
        relations.add(GameRelation.create(unsyncedGame2, System.currentTimeMillis()));
        relations.add(GameRelation.create(syncedGame3, System.currentTimeMillis()));
        doReturn(Observable.just(relations)).when(relationRepository).loadAll();

        final Game updatedGame = Game.create(100, "updated_title");
        doReturn(Observable.just(updatedGame)).when(gameRepository).load(anyInt());
        doAnswer(invocation -> Completable.complete()).when(gameRepository).save(updatedGame);

        // Act
        TestObserver<Game> result = manager.sync().test();
        result.awaitTerminalEvent();

        // Assert
        verify(relationRepository).loadAll();
        verify(gameRepository).load(unsyncedGame1.id());
        verify(gameRepository).load(unsyncedGame2.id());
        verify(gameRepository, never()).load(syncedGame1.id());
        verify(gameRepository, never()).load(syncedGame2.id());
        verify(gameRepository, never()).load(syncedGame3.id());
        verify(gameRepository, times(2)).load(anyInt());
        verify(gameRepository, times(2)).save(updatedGame);
    }

    @Test
    public void given_loadError_When_refreshingData_Then_ShouldKeepRefreshingData() throws Exception {
        Game syncedGame1 = Game.create(0, "title");
        syncedGame1.syncedAt = System.currentTimeMillis();
        Game syncedGame2 = Game.create(1, "title");
        syncedGame2.syncedAt = System.currentTimeMillis();
        Game unsyncedGame1 = Game.create(2, "title");
        Game syncedGame3 = Game.create(3, "title");
        syncedGame3.syncedAt = System.currentTimeMillis();
        Game unsyncedGame2 = Game.create(4, "title");

        List<GameRelation> relations = new ArrayList<>();
        relations.add(GameRelation.create(syncedGame1, System.currentTimeMillis()));
        relations.add(GameRelation.create(syncedGame2, System.currentTimeMillis()));
        relations.add(GameRelation.create(unsyncedGame1, System.currentTimeMillis()));
        relations.add(GameRelation.create(unsyncedGame2, System.currentTimeMillis()));
        relations.add(GameRelation.create(syncedGame3, System.currentTimeMillis()));
        doReturn(Observable.just(relations)).when(relationRepository).loadAll();

        final Game updatedGame = Game.create(100, "updated_title");
        doReturn(Observable.just(updatedGame)).when(gameRepository).load(4);
        doReturn(Observable.error(new Exception("foo"))).when(gameRepository).load(2);
        doAnswer(invocation -> Completable.complete()).when(gameRepository).save(any());

        // Act
        TestObserver<Game> result = manager.sync().test();
        result.awaitTerminalEvent();

        // Assert
        verify(relationRepository).loadAll();
        verify(gameRepository).load(unsyncedGame1.id());
        verify(gameRepository).load(unsyncedGame2.id());
        verify(gameRepository, never()).load(syncedGame1.id());
        verify(gameRepository, never()).load(syncedGame2.id());
        verify(gameRepository, never()).load(syncedGame3.id());
        verify(gameRepository, times(2)).load(anyInt());
        verify(gameRepository).save(unsyncedGame1);
        verify(gameRepository).save(updatedGame);
    }
}