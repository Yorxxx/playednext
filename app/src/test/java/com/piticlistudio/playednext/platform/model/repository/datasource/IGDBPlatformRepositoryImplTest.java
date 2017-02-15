package com.piticlistudio.playednext.platform.model.repository.datasource;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.platform.PlatformModule;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.NetPlatform;

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
 * Test cases for IGDBPlatform repository
 * Created by jorge.garcia on 15/02/2017.
 */
public class IGDBPlatformRepositoryImplTest extends BaseTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    PlatformModule.NetService service;

    @InjectMocks
    private IGDBPlatformRepositoryImpl repository;

    private NetPlatform data = NetPlatform.create(10, "name", "slug", "url", 1000, 2000);

    @Test
    public void load() throws Exception {
        List<NetPlatform> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<IPlatformData> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);


        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_onErrorRetry() throws Exception {

        List<NetPlatform> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString()))
                .thenReturn(Observable.fromCallable(new Callable<List<NetPlatform>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<NetPlatform> call() throws Exception {
                        if (!firstEmitted) { // We throw on first failure
                            firstEmitted = true;
                            throw new RuntimeException(":(");
                        } else {
                            return responseList;
                        }
                    }
                }));

        // Act
        TestObserver<IPlatformData> result = repository.load(1).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_empty() throws Exception {

        List<NetPlatform> responseList = new ArrayList<>();
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<IPlatformData> result = repository.load(1).test();

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
    }

    @Test
    public void save() throws Exception {

        // Act
        TestObserver<IPlatformData> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
    }
}