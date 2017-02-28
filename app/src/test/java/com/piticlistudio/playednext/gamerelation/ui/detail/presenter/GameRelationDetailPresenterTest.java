package com.piticlistudio.playednext.gamerelation.ui.detail.presenter;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test cases
 * Created by jorge.garcia on 27/02/2017.
 */
public class GameRelationDetailPresenterTest extends BaseTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();
    @Mock
    GameRelationDetailContract.Interactor interactor;
    @Mock
    GameRelationDetailContract.View view;
    @InjectMocks
    GameRelationDetailPresenter presenter;

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> testSchedulerRule.getTestScheduler());
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.reset();
    }

    @Test
    public void Given_InteractorLoadSuccess_When_LoadingRelation_Then_ShowsRelation() throws Exception {

        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        doReturn(Observable.just(data)).when(interactor).load(10);

        // Act
        presenter.loadData(10);

        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view).setData(data);
        verify(view).showContent();
        verify(view, never()).showError(any(Throwable.class));
        verify(interactor).load(10);
        verify(interactor, never()).create(10);
    }

    @Test
    public void Given_InteractorLoadError_When_LoadingRelation_ThenCreatesAndShowsRelation() throws Exception {

        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        doReturn(Observable.just(data)).when(interactor).create(10);
        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(interactor).load(10);

        // Act
        presenter.loadData(10);

        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        verify(interactor).load(10);
        verify(interactor).create(10);
        verify(view).showLoading();
        verify(view).setData(data);
        verify(view).showContent();
        verify(view, never()).showError(any(Throwable.class));
        verify(interactor).load(10);
        verify(interactor).create(10);
    }

    @Test
    public void Given_InteractorLoadAndCreationError_When_LoadingRelation_ThenShowsError() throws Exception {

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(interactor).create(10);

        doReturn(Observable.error(error)).when(interactor).load(10);

        // Act
        presenter.loadData(10);

        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        verify(interactor).load(10);
        verify(interactor).create(10);
        verify(view).showLoading();
        verify(view, never()).setData(any());
        verify(view, never()).showContent();
        verify(view).showError(error);
        verify(interactor).load(10);
        verify(interactor).create(10);
    }

    @Test
    public void Given_MultipleConsecutiveSaveCalls_When_SavingRelation_ThenSavesOnlyOnceAndLast() throws Exception {

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, 1);
        long updatedAt = data.getUpdatedAt();
        when(interactor.save(data)).thenReturn(Observable.just(data).delay(1, TimeUnit.SECONDS));
        doAnswer(invocation -> {
            RelationInterval.RelationType type = (RelationInterval.RelationType)invocation.getArguments()[0];
            return RelationInterval.create(10, type, System.currentTimeMillis());
        }).when(interactor).create(any(RelationInterval.RelationType.class));

        // Act
        presenter.save(data, RelationInterval.RelationType.DONE);
        presenter.save(data, RelationInterval.RelationType.NONE);
        presenter.save(data, RelationInterval.RelationType.PLAYING);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(interactor, times(1)).save(data);
        verify(view).setData(data);
        verify(view).showContent();
        verify(view, never()).showError(any(Throwable.class));
        assertTrue(data.getCurrent().isPresent());
        assertEquals(RelationInterval.RelationType.PLAYING, data.getCurrent().get().type());
        assertTrue(data.getCurrent().get().startAt() > 0);
        assertEquals(1, data.getStatuses().size());
        assertTrue(data.getUpdatedAt() > updatedAt);
    }

    @Test
    public void Given_SingleSaveCall_When_SavingRelation_ThenSavesData() throws Exception {

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        long updatedAt = data.getUpdatedAt();
        data.getStatuses().add(RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000));
        when(interactor.save(data)).thenReturn(Observable.just(data).delay(1, TimeUnit.SECONDS));
        doAnswer(invocation -> {
            RelationInterval.RelationType type = (RelationInterval.RelationType)invocation.getArguments()[0];
            return RelationInterval.create(10, type, System.currentTimeMillis());
        }).when(interactor).create(any(RelationInterval.RelationType.class));

        // Act
        presenter.save(data, RelationInterval.RelationType.PLAYING);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).setData(data);
        verify(view).showContent();
        verify(view, never()).showError(any(Throwable.class));
        assertEquals(2, data.getStatuses().size());
        assertTrue(data.getCurrent().isPresent());
        assertEquals(RelationInterval.RelationType.PLAYING, data.getCurrent().get().type());
        assertTrue(data.getStatuses().get(0).getEndAt() > 0);
        assertTrue(data.getStatuses().get(1).startAt() >= data.getStatuses().get(0).getEndAt());
        assertEquals(0, data.getStatuses().get(1).getEndAt());
        assertTrue(data.getUpdatedAt() > updatedAt);
    }
}