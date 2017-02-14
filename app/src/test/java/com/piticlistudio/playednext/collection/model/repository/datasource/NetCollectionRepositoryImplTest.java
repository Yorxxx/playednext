package com.piticlistudio.playednext.collection.model.repository.datasource;

import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.NetCollection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class NetCollectionRepositoryImplTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    CollectionModule.NetService service;

    @InjectMocks
    private NetCollectionRepositoryImpl repository;

    private NetCollection data = NetCollection.create(10, "name", "url", 1000, 2000, new ArrayList<>());

    @Test
    public void load() throws Exception {
        List<NetCollection> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<ICollectionData> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);


        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_onErrorRetry() throws Exception {

        List<NetCollection> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString()))
                .thenReturn(Observable.fromCallable(new Callable<List<NetCollection>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<NetCollection> call() throws Exception {
                        if (!firstEmitted) { // We throw on first failure
                            firstEmitted = true;
                            throw new RuntimeException(":(");
                        } else {
                            return responseList;
                        }
                    }
                }));

        // Act
        TestObserver<ICollectionData> result = repository.load(1).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_empty() throws Exception {

        List<NetCollection> responseList = new ArrayList<>();
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<ICollectionData> result = repository.load(1).test();

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
    }

    @Test
    public void save() throws Exception {

        NetCollection data = NetCollection.create(50, "name", "url", 1000, 2000, new ArrayList<>());

        // Act
        TestObserver<ICollectionData> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
    }
}