package com.piticlistudio.playednext.game.ui.detail;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.ui.detail.interactor.GameDetailInteractor;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Test cases for GameDetailInteractor
 * Created by jorge.garcia on 16/02/2017.
 */
public class GameDetailInteractorTest extends BaseTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    GameRepository repository;

    @InjectMocks
    GameDetailInteractor interactor;

    @Test
    public void load() throws Exception {
        Game data = GameFactory.provide(10, "title");

        doReturn(Observable.just(data)).when(repository).load(10);

        // Act
        TestObserver<Game> result = interactor.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(data);
        verify(repository).load(10);
    }

}