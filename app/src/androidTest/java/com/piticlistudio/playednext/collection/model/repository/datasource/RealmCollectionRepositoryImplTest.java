package com.piticlistudio.playednext.collection.model.repository.datasource;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import io.reactivex.observers.TestObserver;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for RealmCOllection repository
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmCollectionRepositoryImplTest extends BaseAndroidTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    private RealmCollectionRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        File tempFolder = testFolder.newFolder("realmdata");
        RealmConfiguration config = new RealmConfiguration.Builder(tempFolder).build();
        Realm.setDefaultConfiguration(config);
        repository = new RealmCollectionRepositoryImpl();
    }

    @Test
    public void load() throws Exception {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmCollection data = realm.createObject(RealmCollection.class);
        data.setName("name");
        data.setId(0);
        realm.commitTransaction();

        // Act
        TestObserver<ICollectionData> result = repository.load(data.getId()).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors();
        result.assertComplete();
        result.assertValueCount(1);
        result.assertValue(check(value -> {
            assertEquals(data.getId(), value.getId());
            assertEquals(data.getName(), value.getName());
        }));
    }

    @Test
    public void load_notFound() throws Exception {
        // Act
        TestObserver<ICollectionData> result = repository.load(10).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
        result.assertNotComplete();
    }

    @Test
    public void save() throws Exception {

        RealmCollection data = new RealmCollection(50, "name");

        // Act
        TestObserver<ICollectionData> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValue(data);

    }

}