package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.datasource.IGDBGame;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class GamedataRepositoryTest extends BaseTest {

    @Rule
    public DaggerMockRule<GamedataComponent> rule = new DaggerMockRule<>(GamedataComponent.class, new GamedataModule());

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    @Named("db")
    public IGamedatasourceRepository dbImpl;

    @Mock
    @Named("net")
    public IGamedatasourceRepository netImpl;

    @InjectFromComponent
    private GamedataRepository repository;

    private RealmGame localData = GameFactory.provideRealmGame(10, "title");
    private IGDBGame remoteData = GameFactory.provideNetGame(10, "title");


    @Test
    public void load_locally() throws Exception {

        when(dbImpl.load(1)).thenReturn(Single.just(localData));

        // Act
        TestObserver<IGameDatasource> result = repository.load(1).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(localData);
        verifyZeroInteractions(netImpl);
    }

    @Test
    public void load_fromRemote() throws Exception {

        Throwable error = new Throwable();
        when(dbImpl.load(1)).thenReturn(Single.error(error));
        when(netImpl.load(1)).thenReturn(Single.just((IGameDatasource) remoteData).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<IGameDatasource> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(remoteData);
        verify(netImpl).load(1);
        verify(dbImpl).load(1);
    }

    @Test
    public void load_error() throws Exception {

        Throwable error = new Throwable();
        when(dbImpl.load(1)).thenReturn(Single.error(error));
        when(netImpl.load(1)).thenReturn(Single.error(error));

        // Act
        TestObserver<IGameDatasource> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertError(error)
                .assertNotComplete()
                .assertNoValues();
        verify(netImpl).load(1);
        verify(dbImpl).load(1);
    }

    @Test
    public void search() throws Exception {

        List<IGameDatasource> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(GameFactory.provideNetGame(i, "title" + i));
        }
        when(netImpl.search(anyString(), anyInt(), anyInt())).thenReturn(Single.just(data).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<List<IGameDatasource>> result = repository.search("1", 0, 10).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(data);
        verifyZeroInteractions(dbImpl);
    }

    @Test
    public void search_error() throws Exception {

        Throwable error = new Exception();
        when(netImpl.search(anyString(), anyInt(), anyInt())).thenReturn(Single.error(error));

        // Act
        TestObserver<List<IGameDatasource>> result = repository.search("1", 0, 10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(Throwable.class);
        verifyZeroInteractions(dbImpl);
    }

    @Test
    public void save() throws Exception {

        IGameDatasource data = GameFactory.provideRealmGame(50, "title");
        when(dbImpl.save(data)).thenReturn(Single.just(data));

        // Act
        TestObserver<IGameDatasource> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(data);
        verifyZeroInteractions(netImpl);
    }
}