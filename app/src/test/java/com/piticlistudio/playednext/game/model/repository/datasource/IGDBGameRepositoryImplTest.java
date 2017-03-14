package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.datasource.IGDBGame;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test cases for NetGameRepository
 * Created by jorge.garcia on 10/02/2017.
 */
public class IGDBGameRepositoryImplTest extends BaseTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    GamedataModule.NetService service;

    @InjectMocks
    private IGDBGameRepositoryImpl repository;


    @Test
    public void Given_EmptyList_When_Load_Then_ThrowsError() throws Exception {

        List<IGDBGame> responseList = new ArrayList<>();
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(1, TimeUnit.SECONDS));

        // Act
        TestObserver<IGameDatasource> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, TimeUnit.SECONDS);

        // Assert
        result.assertNotComplete()
                .assertNoValues()
                .assertError(Throwable.class);
    }

    @Test
    public void Given_MultipleValues_When_load_Then_ReturnsFirst() throws Exception {

        IGDBGame response1 = GameFactory.provideNetGame(1, "1");
        IGDBGame response2 = GameFactory.provideNetGame(2, "2");
        List<IGDBGame> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(1, TimeUnit.SECONDS));

        // Act
        TestObserver<IGameDatasource> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .assertValue(response1);
    }

    @Test
    public void Given_Error_When_Load_Then_RetriesRequest() throws Exception {

        IGDBGame response1 = GameFactory.provideNetGame(1, "1");
        IGDBGame response2 = GameFactory.provideNetGame(2, "2");
        List<IGDBGame> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);
        when(service.load(anyInt(), anyString()))
                .thenReturn(Observable.fromCallable(new Callable<List<IGDBGame>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<IGDBGame> call() throws Exception {
                        if (!firstEmitted) { // We throw on first failure
                            firstEmitted = true;
                            throw new RuntimeException(":(");
                        } else {
                            return responseList;
                        }
                    }
                }));

        // Act
        TestObserver<IGameDatasource> result = repository.load(1).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .assertValue(response1);
    }

    @Test
    public void Given_Error_When_Search_Then_Retries() throws Exception {

        IGDBGame response1 = GameFactory.provideNetGame(1, "1");
        IGDBGame response2 = GameFactory.provideNetGame(2, "2");
        List<IGDBGame> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        when(service.search(anyInt(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.fromCallable(new Callable<List<IGDBGame>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<IGDBGame> call() throws Exception {
                        if (!firstEmitted) { // We throw on first failure
                            firstEmitted = true;
                            throw new RuntimeException(":(");
                        } else {
                            return responseList;
                        }
                    }
                }));

        // Act
        TestObserver<List<IGameDatasource>> result = repository.search("query", 0, 10).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(iGameDatasources -> assertEquals(responseList, iGameDatasources)));
    }

    @Test
    public void Given_Success_When_Search_Then_ReturnsSearchResult() throws Exception {

        IGDBGame response1 = GameFactory.provideNetGame(1, "1");
        IGDBGame response2 = GameFactory.provideNetGame(2, "2");
        List<IGDBGame> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);
        when(service.search(anyInt(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.just(responseList).delay(1, TimeUnit.SECONDS));

        // Act
        TestObserver<List<IGameDatasource>> result = repository.search("query", 0, 10).test();

        testSchedulerRule.getTestScheduler().advanceTimeBy(2, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(iGameDatasources -> assertEquals(responseList, iGameDatasources)));
    }

    @Test
    public void Given_nothing_When_Save_Then_EmitsError() throws Exception {

        IGDBGame data = GameFactory.provideNetGame(1, "name");

        // Act
        TestObserver<IGameDatasource> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNotComplete()
                .assertNoValues();

    }
}