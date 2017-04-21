package com.piticlistudio.playednext.genre.model.repository.datasource;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGDBGenre;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;

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
 * Test cases for NetGenreRepositoryImpl
 * Created by jorge.garcia on 14/02/2017.
 */
public class IGDBGenreRepositoryImplTest extends BaseTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    GenreModule.NetService service;

    @InjectMocks
    private IGDBGenreRepositoryImpl repository;

    private IGDBGenre data = IGDBGenre.create(10, "name", "url", "slug", 1000, 2000);

    @Test
    public void load() throws Exception {
        List<IGDBGenre> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<IGenreData> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);


        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_onErrorRetry() throws Exception {

        List<IGDBGenre> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString()))
                .thenReturn(Observable.fromCallable(new Callable<List<IGDBGenre>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<IGDBGenre> call() throws Exception {
                        if (!firstEmitted) { // We throw on first failure
                            firstEmitted = true;
                            throw new RuntimeException(":(");
                        } else {
                            return responseList;
                        }
                    }
                }));

        // Act
        TestObserver<IGenreData> result = repository.load(1).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_empty() throws Exception {

        List<IGDBGenre> responseList = new ArrayList<>();
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<IGenreData> result = repository.load(1).test();

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
    }

    @Test
    public void save() throws Exception {

        IGDBGenre data = IGDBGenre.create(10, "name", "url", "slug", 1000, 2000);

        // Act
        TestObserver<Void> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNotComplete()
                .assertNoValues();

    }

}