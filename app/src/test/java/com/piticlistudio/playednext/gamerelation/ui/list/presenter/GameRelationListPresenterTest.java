package com.piticlistudio.playednext.gamerelation.ui.list.presenter;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListContract;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test cases for GameRelationListPresenter
 * Created by jorge.garcia on 01/03/2017.
 */
public class GameRelationListPresenterTest extends BaseTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    GameRelationListContract.Interactor interactor;

    @Mock
    GameRelationListContract.View view;

    @InjectMocks
    GameRelationListPresenter presenter;

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
    public void given_viewIsDetached_When_BeforeLoading_Then_DetachesView() throws Exception {

        presenter.detachView(false);

        // Assert
        assertNull(presenter.loadDisposable);
        assertNull(presenter.getView());
    }

    @Test
    public void given_viewIsDetached_When_IsLoadingData_Then_DetachesView() throws Exception {

        // Arrange
        List<GameRelation> completedItems = new ArrayList<>();
        completedItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        completedItems.add(GameRelation.create(Game.create(11, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(completedItems).delay(1, TimeUnit.SECONDS)).when(interactor).loadCompletedItems();

        List<GameRelation> currentItems = new ArrayList<>();
        currentItems.add(GameRelation.create(Game.create(12, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(currentItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadCurrentItems();

        List<GameRelation> waitingItems = new ArrayList<>();
        waitingItems.add(GameRelation.create(Game.create(13, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(waitingItems).delay(1500, TimeUnit.MILLISECONDS)).when(interactor).loadWaitingItems();

        presenter.loadData();
        testSchedulerRule.getTestScheduler().advanceTimeBy(1, TimeUnit.SECONDS);

        // Act
        // Detach view while it is loading data
        presenter.detachView(false);

        // Advance remaining time
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view, never()).setData(any(), any(), any());
        verify(view, never()).showContent();
        assertNull(presenter.loadDisposable);
        assertNull(presenter.getView());
    }

    @Test
    public void given_loadData_When_ViewIsNotAttached_Then_DoesNothing() throws Exception {

        // Arrange
        presenter.detachView(false);

        // Act
        presenter.loadData();

        // Assert
        verifyZeroInteractions(view);
        verifyZeroInteractions(interactor);
    }

    @Test
    public void given_interactorLoadCompletedError_When_Load_Then_EmitsError() throws Exception {
        Throwable error = new Exception("bla");
        doReturn(Observable.error(error).delay(1, TimeUnit.SECONDS)).when(interactor).loadCompletedItems();

        List<GameRelation> currentItems = new ArrayList<>();
        currentItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(currentItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadCurrentItems();

        List<GameRelation> waitingItems = new ArrayList<>();
        waitingItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(waitingItems).delay(1500, TimeUnit.MILLISECONDS)).when(interactor).loadWaitingItems();

        // Act
        presenter.loadData();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view, never()).setData(anyList(), anyList(), anyList());
        verify(view, never()).showContent();
        verify(view).showError(error);
    }

    @Test
    public void given_interactorLoadCurrentError_When_Load_Then_EmitsError() throws Exception {
        Throwable error = new Exception("bla");
        doReturn(Observable.error(error).delay(1, TimeUnit.SECONDS)).when(interactor).loadCurrentItems();

        List<GameRelation> currentItems = new ArrayList<>();
        currentItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(currentItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadCompletedItems();

        List<GameRelation> waitingItems = new ArrayList<>();
        waitingItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(waitingItems).delay(1500, TimeUnit.MILLISECONDS)).when(interactor).loadWaitingItems();

        // Act
        presenter.loadData();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view, never()).setData(anyList(), anyList(), anyList());
        verify(view, never()).showContent();
        verify(view).showError(error);
    }

    @Test
    public void given_interactorLoadWaitingError_When_Load_Then_EmitsError() throws Exception {
        Throwable error = new Exception("bla");
        doReturn(Observable.error(error).delay(1, TimeUnit.SECONDS)).when(interactor).loadWaitingItems();

        List<GameRelation> currentItems = new ArrayList<>();
        currentItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(currentItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadCurrentItems();

        List<GameRelation> waitingItems = new ArrayList<>();
        waitingItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(waitingItems).delay(1500, TimeUnit.MILLISECONDS)).when(interactor).loadCompletedItems();

        // Act
        presenter.loadData();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view, never()).setData(anyList(), anyList(), anyList());
        verify(view, never()).showContent();
        verify(view).showError(error);
    }

    @Test
    public void given_interactorLoadSuccess_When_Load_Then_ShowsData() throws Exception {
        List<GameRelation> completedItems = new ArrayList<>();
        completedItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        completedItems.add(GameRelation.create(Game.create(11, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(completedItems).delay(1, TimeUnit.SECONDS)).when(interactor).loadCompletedItems();

        List<GameRelation> currentItems = new ArrayList<>();
        currentItems.add(GameRelation.create(Game.create(12, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(currentItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadCurrentItems();

        List<GameRelation> waitingItems = new ArrayList<>();
        waitingItems.add(GameRelation.create(Game.create(13, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(waitingItems).delay(1500, TimeUnit.MILLISECONDS)).when(interactor).loadWaitingItems();

        // Act
        presenter.loadData();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view).setData(completedItems, currentItems, waitingItems);
        verify(view).showContent();
        verify(view, never()).showError(any());
    }

    @Test
    public void given_interactorLoadEmitsTwice_When_Load_Then_ShowsDataTwice() throws Exception {
        List<GameRelation> completedItemsFirstEmission = new ArrayList<>();
        completedItemsFirstEmission.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        completedItemsFirstEmission.add(GameRelation.create(Game.create(11, "title"), System.currentTimeMillis()));

        List<GameRelation> completedItemsSecondEmission = new ArrayList<>();
        completedItemsSecondEmission.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        completedItemsSecondEmission.add(GameRelation.create(Game.create(11, "title"), System.currentTimeMillis()));
        completedItemsSecondEmission.add(GameRelation.create(Game.create(21, "title"), System.currentTimeMillis()));

        doAnswer(invocation -> Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .map(aLong -> {
                    if (aLong == 0)
                        return completedItemsFirstEmission;
                    return completedItemsSecondEmission;
                })).when(interactor).loadCompletedItems();


        List<GameRelation> currentItems = new ArrayList<>();
        currentItems.add(GameRelation.create(Game.create(12, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(currentItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadCurrentItems();

        List<GameRelation> waitingItems = new ArrayList<>();
        waitingItems.add(GameRelation.create(Game.create(13, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(waitingItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadWaitingItems();

        // Act
        presenter.loadData();
        testSchedulerRule.getTestScheduler().advanceTimeBy(1, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view).setData(completedItemsFirstEmission, currentItems, waitingItems);
        verify(view).showContent();
        verify(view, never()).showError(any());

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(1, TimeUnit.SECONDS);

        // Assert
        verify(view).setData(completedItemsSecondEmission, currentItems, waitingItems);
        verify(view, times(2)).showContent();
    }

    @Test
    public void given_relationUpdate_When_SaveIsRequested_Then_SavesData() throws Exception {

        long currentTime = System.currentTimeMillis();
        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, 1000);
        data.setUpdatedAt(1500);
        RelationInterval currentInterval = RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000);
        data.getStatuses().add(currentInterval);
        assertEquals(0, currentInterval.getEndAt());

        when(interactor.save(data)).thenReturn(Observable.just(data).delay(1, TimeUnit.SECONDS));
        RelationInterval newInterval = RelationInterval.create(2, RelationInterval.RelationType.PLAYING, System.currentTimeMillis());
        when(interactor.create(RelationInterval.RelationType.PLAYING)).thenReturn(newInterval);

        // Act
        presenter.save(data, RelationInterval.RelationType.PLAYING);

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(1, TimeUnit.SECONDS);

        // Assert
        verifyZeroInteractions(view);
        assertEquals(2, data.getStatuses().size());
        verify(interactor).create(RelationInterval.RelationType.PLAYING);
        assertEquals(currentInterval, data.getStatuses().get(0));
        assertTrue(currentInterval.getEndAt() >= currentTime);
        assertTrue(currentInterval.getEndAt() >= currentInterval.startAt());
        assertEquals(newInterval, data.getStatuses().get(1));
        assertEquals(0, newInterval.getEndAt());
        assertTrue(data.getCurrent().isPresent());
        assertEquals(newInterval, data.getCurrent().get());
        verify(interactor).save(data);
    }

    @Test
    public void given_saveRelation_When_NoCurrentStatus_Then_AddsAndSavesData() throws Exception {

        long currentTime = System.currentTimeMillis();
        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, 1000);
        data.setUpdatedAt(1500);
        assertFalse(data.getCurrent().isPresent());

        when(interactor.save(data)).thenReturn(Observable.just(data).delay(1, TimeUnit.SECONDS));
        RelationInterval newInterval = RelationInterval.create(2, RelationInterval.RelationType.PLAYING, System.currentTimeMillis());
        when(interactor.create(RelationInterval.RelationType.PLAYING)).thenReturn(newInterval);

        // Act
        presenter.save(data, RelationInterval.RelationType.PLAYING);

        testSchedulerRule.getTestScheduler().advanceTimeBy(1, TimeUnit.SECONDS);

        // Assert
        verifyZeroInteractions(view);
        assertEquals(1, data.getStatuses().size());
        verify(interactor).create(RelationInterval.RelationType.PLAYING);
        assertEquals(newInterval, data.getStatuses().get(0));
        assertTrue(newInterval.startAt() >= currentTime);
        assertEquals(0, newInterval.getEndAt());
        assertTrue(data.getCurrent().isPresent());
        assertEquals(newInterval, data.getCurrent().get());
        verify(interactor).save(data);
    }

    @Test
    public void given_showData_When_ViewIsNull_Then_DoesNotInteractWithTheView() throws Exception {

        List<GameRelation> completedItems = new ArrayList<>();
        completedItems.add(GameRelation.create(Game.create(10, "title"), System.currentTimeMillis()));
        completedItems.add(GameRelation.create(Game.create(11, "title"), System.currentTimeMillis()));
        completedItems.add(GameRelation.create(Game.create(21, "title"), System.currentTimeMillis()));

        List<GameRelation> currentItems = new ArrayList<>();
        currentItems.add(GameRelation.create(Game.create(12, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(currentItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadCurrentItems();

        List<GameRelation> waitingItems = new ArrayList<>();
        waitingItems.add(GameRelation.create(Game.create(13, "title"), System.currentTimeMillis()));
        doReturn(Observable.just(waitingItems).delay(500, TimeUnit.MILLISECONDS)).when(interactor).loadWaitingItems();

        presenter.detachView(false);

        // Act
        presenter.showData(presenter.new ItemsResult(completedItems, currentItems, waitingItems));

        // Assert
        verifyZeroInteractions(view);
    }

    @Test
    public void given_showError_When_ViewIsNull_Then_DoesNotShowError() throws Exception {

        Throwable error = new Exception("bla");
        presenter.detachView(false);

        // Act
        presenter.showError(error);

        // Assert
        verifyZeroInteractions(view);
    }
}