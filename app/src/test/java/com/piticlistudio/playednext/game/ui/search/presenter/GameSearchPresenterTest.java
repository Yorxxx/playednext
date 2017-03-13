package com.piticlistudio.playednext.game.ui.search.presenter;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSubjectSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
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

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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
    public void given_detachesView_When_Any_Then_DisposesSearch() throws Exception {

        presenter.detachView(false);

        // Assert
        assertTrue(presenter.searchDisposable.isDisposed());
    }

    @Test
    public void given_viewNotAttached_When_Search_Then_DoesNotInteract() throws Exception {

        presenter.detachView(false);

        List<Game> data = new ArrayList<>();
        data.add(Game.create(1, "title1"));
        data.add(Game.create(2, "title2"));
        when(interactor.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));

        // Act
        presenter.search("query", 0, 15);

        // Assert
        verifyZeroInteractions(view);
        verifyZeroInteractions(interactor);
    }

    @Test
    public void given_searchSuccess_When_Search_Then_SearchesData() throws Exception {

        List<Game> data = new ArrayList<>();
        data.add(Game.create(1, "title1"));
        data.add(Game.create(2, "title2"));
        when(interactor.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));

        // Act
        presenter.search("query", 0, 15);

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(interactor).search("query", 0, 15);
        verify(view).setData(data);
        verify(view).showContent();
    }

    @Test
    public void given_searchError_When_Search_Then_ShowsError() throws Exception {

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error).delay(2, TimeUnit.SECONDS)).when(interactor).search(anyString(), anyInt(), anyInt());

        // Act
        presenter.search("query", 0, 15);
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(interactor).search("query", 0, 15);
        verify(view, never()).setData(any());
        verify(view, never()).showContent();
        verify(view).showError(error);
    }

    @Test
    public void given_showData_When_ViewIsNull_Then_DoesNotInteractWithTheView() throws Exception {

        List<Game> data = new ArrayList<>();
        data.add(Game.create(1, "title1"));
        data.add(Game.create(2, "title2"));
        presenter.detachView(false);

        // Act
        presenter.showData(data);

        // Assert
        verifyZeroInteractions(view);
    }

    @Test
    public void given_showError_When_ViewIsNull_Then_DoesNotInteractWithTheView() throws Exception {

        Throwable error = new Exception("bla");

        presenter.detachView(false);

        // Act
        presenter.showError(error);

        // Assert
        verifyZeroInteractions(view);
    }

    @Test
    public void given_consecutiveSearches_When_Search_Then_DebounceUntilLast() throws Exception {
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
}