package com.piticlistudio.playednext.company.model.repository.datasource;

import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.NetCompany;

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
 * Created by jorge.garcia on 13/02/2017.
 */
public class NetCompanyRepositoryImplTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    CompanyModule.NetService service;

    @InjectMocks
    private NetCompanyRepositoryImpl repository;

    private NetCompany data = NetCompany.create(10, "name", "url", "slug", 1000, 2000);

    @Test
    public void load() throws Exception {
        List<NetCompany> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<ICompanyData> result = repository.load(1).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);


        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_onErrorRetry() throws Exception {

        List<NetCompany> responseList = new ArrayList<>();
        responseList.add(data);
        when(service.load(anyInt(), anyString()))
                .thenReturn(Observable.fromCallable(new Callable<List<NetCompany>>() {
                    private boolean firstEmitted;

                    @Override
                    public List<NetCompany> call() throws Exception {
                        if (!firstEmitted) { // We throw on first failure
                            firstEmitted = true;
                            throw new RuntimeException(":(");
                        } else {
                            return responseList;
                        }
                    }
                }));

        // Act
        TestObserver<ICompanyData> result = repository.load(1).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValue(data);
    }

    @Test
    public void load_empty() throws Exception {

        List<NetCompany> responseList = new ArrayList<>();
        when(service.load(anyInt(), anyString())).thenReturn(Observable.just(responseList).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<ICompanyData> result = repository.load(1).test();

        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
    }

    @Test
    public void save() throws Exception {

        NetCompany data = NetCompany.create(10, "name", "url", "slug", 1000, 2000);

        // Act
        TestObserver<ICompanyData> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNotComplete()
                .assertNoValues();

    }
}