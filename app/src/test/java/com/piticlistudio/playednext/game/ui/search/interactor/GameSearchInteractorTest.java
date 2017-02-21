package com.piticlistudio.playednext.game.ui.search.interactor;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Test cases for GameSearchInteractor
 * Created by jorge.garcia on 21/02/2017.
 */
public class GameSearchInteractorTest extends BaseTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    IGameRepository repository;

    @InjectMocks
    GameSearchInteractor interactor;

    @Test
    public void search() throws Exception {

        List<Game> data = new ArrayList<>();
        data.add(Game.create(1, "title1"));
        data.add(Game.create(2, "title2"));
        doReturn(Observable.just(data).delay(2, TimeUnit.SECONDS)).when(repository).search(anyString(), anyInt(), anyInt());

        // Act
        TestObserver<List<Game>> result = interactor.search("query", 0, 50).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertValue(data)
                .assertComplete();
        verify(repository).search("query", 0, 50);
    }

    @Test
    public void search_error() throws Exception {

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error).delay(2, TimeUnit.SECONDS)).when(repository).search(anyString(), anyInt(), anyInt());

        // Act
        TestObserver<List<Game>> result = interactor.search("query", 0, 50).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertError(error)
                .assertNoValues()
                .assertNotComplete();
        verify(repository).search("query", 0, 50);
    }

}