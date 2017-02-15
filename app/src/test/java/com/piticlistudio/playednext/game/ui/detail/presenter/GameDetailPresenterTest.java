package com.piticlistudio.playednext.game.ui.detail.presenter;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test cases for GameDetail presenter
 * Created by jorge.garcia on 15/02/2017.
 */
public class GameDetailPresenterTest extends BaseTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    GameDetailContract.Interactor interactor;
    @Mock
    GameDetailContract.View view;
    @Captor
    ArgumentCaptor<Game> dataCaptor;
    @InjectMocks
    private GameDetailPresenter presenter;

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
    public void loadData() {

        Game initialData = GameFactory.provide(100, "title");
        initialData.collection = Optional.absent();
        initialData.genres.clear();
        initialData.developers.clear();
        initialData.publishers.clear();
        initialData.releases.clear();
        initialData.platforms.clear();

        Game secondaryData = GameFactory.provide(100, "title");
        initialData.publishers.clear();
        initialData.releases.clear();
        initialData.platforms.clear();

        Game finalData = GameFactory.provide(100, "title");

        when(interactor.load(100))
                .thenReturn(Observable.range(1, 3)
                        .delay(100, TimeUnit.MILLISECONDS)
                        .map(integer -> {
                            switch (integer) {
                                case 1:
                                    return initialData;
                                case 2:
                                    return secondaryData;
                                default:
                                    return finalData;
                            }
                        }));


        // Act
        presenter.loadData(100);

        testSchedulerRule.getTestScheduler().advanceTimeBy(30, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view, times(3)).setData(dataCaptor.capture());
        assertEquals(3, dataCaptor.getAllValues().size());
        assertEquals(initialData, dataCaptor.getAllValues().get(0));
        assertEquals(secondaryData, dataCaptor.getAllValues().get(1));
        assertEquals(finalData, dataCaptor.getAllValues().get(2));
        verify(view, times(3)).showContent();
        verify(interactor).load(100);
    }

    @Test
    public void loadData_error() {

        Game initialData = GameFactory.provide(100, "title");


        when(interactor.load(100))
                .thenReturn(Observable.range(1, 2)
                        .delay(100, TimeUnit.MILLISECONDS)
                        .flatMap(integer -> {
                            switch (integer) {
                                case 1:
                                    return Observable.just(initialData);
                                default:
                                    return Observable.error(new Exception());
                            }
                        }));


        // Act
        presenter.loadData(100);

        testSchedulerRule.getTestScheduler().advanceTimeBy(30, TimeUnit.SECONDS);

        // Assert
        verify(view).showLoading();
        verify(view).setData(initialData);
        verify(view).showContent();
        verify(view).showError(any(Throwable.class));
        verify(view).showContent();
        verify(interactor).load(100);
    }

}