package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.NetGame;

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

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test cases for NetGameRepository
 * Created by jorge.garcia on 10/02/2017.
 */
public class NetGameRepositoryImplTest extends BaseTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    GamedataModule.NetService service;

    @InjectMocks
    private NetGameRepositoryImpl repository;


    @Test
    public void load() throws Exception {

        NetGame response1 = GameFactory.provideNetGame(1, "1");
        NetGame response2 = GameFactory.provideNetGame(2, "2");
        List<NetGame> responseList = new ArrayList<>();
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
    public void load_onErrorRetry() throws Exception {

        NetGame response1 = GameFactory.provideNetGame(1, "1");
        NetGame response2 = GameFactory.provideNetGame(2, "2");
        List<NetGame> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);
        when(service.load(anyInt(), anyString()))
                .thenReturn(Observable.fromCallable(new Callable<List<NetGame>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<NetGame> call() throws Exception {
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
    public void search() throws Exception {

        NetGame response1 = GameFactory.provideNetGame(1, "1");
        NetGame response2 = GameFactory.provideNetGame(2, "2");
        List<NetGame> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        when(service.search(anyInt(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.fromCallable(new Callable<List<NetGame>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<NetGame> call() throws Exception {
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
                .assertValue(check(new Consumer<List<IGameDatasource>>() {
                    @Override
                    public void accept(List<IGameDatasource> iGameDatasources) throws Exception {
                        assertEquals(responseList, iGameDatasources);
                    }
                }));
    }

    @Test
    public void search_retry() throws Exception {

        NetGame response1 = GameFactory.provideNetGame(1, "1");
        NetGame response2 = GameFactory.provideNetGame(2, "2");
        List<NetGame> responseList = new ArrayList<>();
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
                .assertValue(check(new Consumer<List<IGameDatasource>>() {
                    @Override
                    public void accept(List<IGameDatasource> iGameDatasources) throws Exception {
                        assertEquals(responseList, iGameDatasources);
                    }
                }));
    }
}