package com.piticlistudio.playednext.gamerelation.ui.detail.presenter;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.TestSubjectSchedulerRule;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test cases
 * Created by jorge.garcia on 27/02/2017.
 */
public class GameRelationDetailPresenterTest extends BaseTest {

    @Rule
    public TestSubjectSchedulerRule testSchedulerRule = new TestSubjectSchedulerRule();
    @Mock
    GameRelationDetailContract.Interactor interactor;
    @Mock
    GameRelationDetailContract.View view;
    @InjectMocks
    GameRelationDetailPresenter presenter;

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> testSchedulerRule.getTestScheduler());
    }

    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.reset();
    }

    @Test
    public void given_viewIsNotAttached_When_LoadData_Then_DoesNotHaveAnyViewInteraction() throws Exception {

        presenter.attachView(null);

        // Act
        presenter.loadData(10);

        // Assert
        verifyZeroInteractions(view);
        verifyZeroInteractions(interactor);
    }

    @Test
    public void Given_InteractorLoadSuccess_When_LoadingRelation_Then_ShowsRelation() throws Exception {

        presenter.attachView(view);

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

        presenter.attachView(view);

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

        presenter.attachView(view);

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
    public void Given_LoadIsCalledMultipleTimesWithSameId_When_LoadData_Then_OnlyRequestsOnce() throws Exception {
        presenter.attachView(view);

        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        doReturn(Observable.just(data)).when(interactor).create(10);
        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(interactor).load(10);

        // Act
        for (int i = 0; i < 5; i++) {
            presenter.loadData(10);
        }

        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        verify(interactor, times(1)).load(10);
        verify(interactor, times(1)).create(10);
        verify(view, times(5)).showLoading();
        verify(view, times(1)).setData(data);
        verify(view, times(1)).showContent();
        verify(view, never()).showError(any(Throwable.class));
    }

    @Test
    public void Given_LoadIsCalledMultipleTimesWithDifferentId_When_LoadData_Then_ShowsOnlyLast() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, 0);
        GameRelation data2 = GameRelation.create(game, 1);
        GameRelation data3 = GameRelation.create(game, 2);
        GameRelation data4 = GameRelation.create(game, 3);
        GameRelation data5 = GameRelation.create(game, 4);
        doAnswer(invocation -> {
            int argument = (Integer)invocation.getArguments()[0];
            switch (argument) {
                case 0:
                    return Observable.just(data);
                case 1:
                    return Observable.just(data2);
                case 2:
                    return Observable.just(data3);
                case 3:
                    return Observable.just(data4);
                default:
                    return Observable.just(data5);
            }
        }).when(interactor).create(anyInt());
        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(interactor).load(anyInt());

        // Act
        for (int i = 0; i < 5; i++) {
            presenter.loadData(i);
        }

        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        verify(interactor, times(1)).load(4);
        verify(interactor, times(1)).create(4);
        verify(view, times(5)).showLoading();
        verify(view, never()).setData(data);
        verify(view, never()).setData(data2);
        verify(view, never()).setData(data3);
        verify(view, never()).setData(data4);
        verify(view).setData(data5);
        verify(view, times(1)).showContent();
        verify(view, never()).showError(any(Throwable.class));
    }

    @Test
    public void given_viewDetaches_When_LoadsData_Then_DoesNotShowData() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        doReturn(Observable.just(data).delay(5, TimeUnit.SECONDS)).when(interactor).load(10);

        // Act
        presenter.loadData(10);
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, TimeUnit.SECONDS);
        presenter.detachView(true); // Detach view while loading data
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view, never()).setData(data);
        verify(view, never()).showContent();
        verify(view, never()).showError(any(Throwable.class));
        verify(interactor).load(10);
        verify(interactor, never()).create(10);
    }

    @Test
    public void Given_MultipleConsecutiveSaveCalls_When_SavingRelation_ThenSavesOnlyOnceAndLast() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, 1);
        long updatedAt = data.getUpdatedAt();
        when(interactor.save(data)).thenReturn(Completable.complete().delay(1, TimeUnit.SECONDS));
        doAnswer(invocation -> {
            RelationInterval.RelationType type = (RelationInterval.RelationType)invocation.getArguments()[0];
            return RelationInterval.create(10, type, System.currentTimeMillis());
        }).when(interactor).create(any(RelationInterval.RelationType.class));

        // Act
        presenter.save(data, RelationInterval.RelationType.DONE, true);
        presenter.save(data, RelationInterval.RelationType.DONE, false);
        presenter.save(data, RelationInterval.RelationType.PLAYING, true);

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
        assertTrue(data.getUpdatedAt() >= updatedAt);
    }

    @Test
    public void Given_SingleSaveCall_When_SavingRelation_ThenSavesData() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        long updatedAt = data.getUpdatedAt();
        data.getStatuses().add(RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000));
        when(interactor.save(data)).thenReturn(Completable.complete().delay(1, TimeUnit.SECONDS));
        doAnswer(invocation -> {
            RelationInterval.RelationType type = (RelationInterval.RelationType)invocation.getArguments()[0];
            return RelationInterval.create(10, type, System.currentTimeMillis());
        }).when(interactor).create(any(RelationInterval.RelationType.class));

        // Act
        presenter.save(data, RelationInterval.RelationType.PLAYING, true);

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
        assertTrue(data.getUpdatedAt() >= updatedAt);
    }

    @Test
    public void given_disabledStatus_When_SavingRelation_ThenSavesData() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        long updatedAt = data.getUpdatedAt();
        data.getStatuses().add(RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000));
        when(interactor.save(data)).thenReturn(Completable.complete().delay(1, TimeUnit.SECONDS));

        // Act
        presenter.save(data, RelationInterval.RelationType.PENDING, false);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).setData(data);
        verify(view).showContent();
        verify(view, never()).showError(any(Throwable.class));
        assertEquals(1, data.getStatuses().size());
        assertFalse(data.getCurrent().isPresent());
        assertTrue(data.getStatuses().get(0).getEndAt() > 0);
        assertTrue(data.getUpdatedAt() >= updatedAt);
    }

    @Test
    public void given_detachView_When_IsLoadingData_Then_ClearsLoadDisposable() throws Exception {

        presenter.attachView(view);

        // Arrange
        Game game = Game.create(10, "title");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        doReturn(Observable.just(data).delay(5, TimeUnit.SECONDS)).when(interactor).load(10);

        presenter.loadData(10);

        assertNotNull(presenter.loadDisposable);
        assertFalse(presenter.loadDisposable.isDisposed());

        // Act
        presenter.detachView(false);

        // Assert
        assertTrue(presenter.loadDisposable.isDisposed());
    }

    @Test
    public void given_detachesView_When_IsSavingData_Then_DisposesSave() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        data.getStatuses().add(RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000));
        when(interactor.save(data)).thenReturn(Completable.complete().delay(1, TimeUnit.SECONDS));
        doAnswer(invocation -> {
            RelationInterval.RelationType type = (RelationInterval.RelationType)invocation.getArguments()[0];
            return RelationInterval.create(10, type, System.currentTimeMillis());
        }).when(interactor).create(any(RelationInterval.RelationType.class));

        presenter.save(data, RelationInterval.RelationType.PLAYING, true);

        assertNotNull(presenter.saveDisposable);
        assertFalse(presenter.saveDisposable.isDisposed());

        // Act
        presenter.detachView(false);

        // Assert
        assertTrue(presenter.saveDisposable.isDisposed());
    }

    @Test
    public void given_detachesRetainedView_When_IsSavingData_Then_DoesNotDisposesSaveAction() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        long updatedAt = data.getUpdatedAt();
        data.getStatuses().add(RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000));
        when(interactor.save(data)).thenReturn(Completable.complete().delay(1, TimeUnit.SECONDS));

        // Act
        presenter.save(data, RelationInterval.RelationType.PENDING, false);
        testSchedulerRule.getTestScheduler().advanceTimeBy(500, TimeUnit.MILLISECONDS);
        presenter.detachView(true);

        // Continue
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verifyZeroInteractions(view);
        assertEquals(1, data.getStatuses().size());
        assertFalse(data.getCurrent().isPresent());
        assertTrue(data.getStatuses().get(0).getEndAt() > 0);
        assertTrue(data.getUpdatedAt() >= updatedAt);
        verify(interactor).save(data);
    }

    @Test
    public void given_errorAfterDetachedView_When_SavingData_Then_DoesNotShowError() throws Exception {

        presenter.attachView(view);

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        data.getStatuses().add(RelationInterval.create(1, RelationInterval.RelationType.PENDING, 1000));
        Throwable error = new Exception("bla");
        doAnswer(invocation -> Observable.error(error).delay(5, TimeUnit.SECONDS)).when(interactor).save(data);

        // Act
        presenter.save(data, RelationInterval.RelationType.PLAYING, false);
        presenter.detachView(true);

        // Continue
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        verify(view, never()).showError(any(Throwable.class));
        verify(view, never()).showContent();
        verify(interactor).save(data);
    }
}