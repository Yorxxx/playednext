package com.piticlistudio.playednext.game.ui.search.presenter;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.TestSubjectSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.ui.search.GameSearchContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases
 * Created by jorge.garcia on 21/02/2017.
 */
public class GameSearchPresenterTest extends BaseTest {

    @Rule
    public TestSubjectSchedulerRule testSchedulerRule = new TestSubjectSchedulerRule();

    @Mock
    GameSearchContract.Interactor interactor;

    @Mock
    GameSearchContract.View view;

    @InjectMocks
    GameSearchPresenter presenter;

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
    public void search() throws Exception {

        List<Game> data = new ArrayList<>();
        data.add(Game.create(1, "title1"));
        data.add(Game.create(2, "title2"));
        when(interactor.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));

        // Act
        presenter.search("query", 0, 15);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view, never()).showError(any());
        verify(view).setData(data);
        verify(view).showContent();
        verify(interactor).search("query", 0, 15);
    }

    @Test
    public void searchIsDebounced() throws Exception {

        List<Game> data = new ArrayList<>();
        data.add(Game.create(1, "title1"));
        data.add(Game.create(2, "title2"));
        when(interactor.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));

        // Act
        presenter.search("fu", 0, 15);
        presenter.search("full", 0, 15);
        presenter.search("full qu", 0, 15);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view, times(3)).showLoading();
        verify(view, times(1)).setData(data);
        verify(view).showContent();
        verify(interactor, never()).search("fu", 0, 15);
        verify(interactor, never()).search("full", 0, 15);
        verify(interactor).search("full qu", 0, 15);

        // Act
        presenter.search("full que", 0, 15);

        testSchedulerRule.getTestScheduler().advanceTimeBy(500, TimeUnit.MILLISECONDS);

        // Assert
        verify(interactor).search("full que", 0, 15);
    }

    @Test
    public void showError() throws Exception {

        Exception error = new TimeoutException();
        when(interactor.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.error(error));

        // Act
        presenter.search("query", 0, 15);

        // Assert
        testSchedulerRule.getTestScheduler().advanceTimeBy(1, TimeUnit.SECONDS);
        verify(view).showLoading();
        verify(view, never()).setData(anyListOf(Game.class));
        verify(view).showError(error);
    }
}