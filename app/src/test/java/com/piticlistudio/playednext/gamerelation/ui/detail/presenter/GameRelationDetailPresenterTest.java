package com.piticlistudio.playednext.gamerelation.ui.detail.presenter;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        GameRelation data2 = GameRelation.create(game, 2);
        GameRelation data3 = GameRelation.create(game, 3);
        when(interactor.save(data)).thenReturn(Observable.just(data).delay(1, TimeUnit.SECONDS));
        when(interactor.save(data2)).thenReturn(Observable.just(data2).delay(1, TimeUnit.SECONDS));
        when(interactor.save(data3)).thenReturn(Observable.just(data3).delay(1, TimeUnit.SECONDS));

        // Act
        presenter.save(data);
        presenter.save(data2);
        presenter.save(data3);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(interactor, never()).save(data);
        verify(interactor, never()).save(data2);
        verify(interactor).save(data3);
        verify(view).setData(data3);
        verify(view).showContent();
        verify(view, never()).showError(any(Throwable.class));
    }

    @Test
    public void Given_SingleSaveCall_When_SavingRelation_ThenSavesData() throws Exception {

        Game game = Game.create(10, "name");
        GameRelation data = GameRelation.create(game, System.currentTimeMillis());
        when(interactor.save(data)).thenReturn(Observable.just(data).delay(1, TimeUnit.SECONDS));

        // Act
        presenter.save(data);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).setData(data);
        verify(view).showContent();
        verify(view, never()).showError(any(Throwable.class));
    }
}