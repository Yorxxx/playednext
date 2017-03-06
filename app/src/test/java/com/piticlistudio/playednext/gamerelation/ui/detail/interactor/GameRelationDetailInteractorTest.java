package com.piticlistudio.playednext.gamerelation.ui.detail.interactor;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.GameRelationRepository;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Test cases
 * Created by jorge.garcia on 27/02/2017.
 */
public class GameRelationDetailInteractorTest extends BaseTest {

    @Mock
    GameRelationRepository repository;

    @Mock
    GameRepository gameRepository;

    @Mock
    RelationIntervalRepository intervalRepository;

    @InjectMocks
    GameRelationDetailInteractor interactor;

    @Test
    public void given_ExistingRelationOnRepository_when_loadsRelation_Then_EmitsRelation() throws Exception {

        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        doReturn(Observable.just(data)).when(repository).load(10);

        // Act
        TestObserver<GameRelation> result = interactor.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(data);
        verify(repository).load(10);
    }

    @Test
    public void given_NonStoredRelation_When_loadsRelation_Then_EmitsError() throws Exception {

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(repository).load(10);

        // Act
        TestObserver<GameRelation> result = interactor.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(error)
                .assertNoValues()
                .assertNotComplete();
        verify(repository).load(10);
    }

    @Test
    public void given_GameRelation_When_SavesRelation_Then_SavesAndEmitsRelation() throws Exception {

        doAnswer(invocation -> Observable.just(invocation.getArguments()[0])).when(repository).save(any());
        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());

        // Act
        TestObserver<GameRelation> result = interactor.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(data);
        verify(repository).save(data);
    }

    @Test
    public void given_SaveError_When_SavesRelation_Then_EmitsError() throws Exception {
        Throwable error = new Exception("bla");
        doAnswer(invocation -> Observable.error(error)).when(repository).save(any());

        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());

        // Act
        TestObserver<GameRelation> result = interactor.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(error)
                .assertNotComplete()
                .assertNoValues();
        verify(repository).save(data);
    }

    @Test
    public void given_CreateRelationRequestForGame_When_createsRelation_Then_EmitsNewRelation() throws Exception {

        long currentTime = System.currentTimeMillis();
        Game game = Game.create(10, "title");
        doReturn(Observable.just(game)).when(gameRepository).load(10);

        // Act
        TestObserver<GameRelation> result = interactor.create(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(data -> {
                    assertNotNull(data);
                    assertNotNull(data.game());
                    assertTrue(data.createdAt() >= currentTime);
                }));
        verify(gameRepository).load(10);
    }

    @Test
    public void given_NonExistingGame_When_CreatesRelationForGame_Then_EmitsError() throws Exception {

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(gameRepository).load(10);

        // Act
        TestObserver<GameRelation> result = interactor.create(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(error)
                .assertNotComplete()
                .assertNoValues();
        verify(gameRepository).load(10);
    }

    @Test
    public void given_empty_When_CreateInterval_Then_RequestsRepository() throws Exception {

        doAnswer(invocation -> {
            RelationInterval.RelationType type = (RelationInterval.RelationType)invocation.getArguments()[0];
            return RelationInterval.create(1, type, System.currentTimeMillis());
        }).when(intervalRepository).create(any());

        RelationInterval result = interactor.create(RelationInterval.RelationType.DONE);

        // Assert
        assertNotNull(result);
        verify(intervalRepository).create(RelationInterval.RelationType.DONE);
    }
}