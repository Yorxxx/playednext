package com.piticlistudio.playednext.gamerelation.model.repository.datasource;

import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for RealmGameRelationRepository implementation
 * Created by jorge.garcia on 27/02/2017.
 */
public class RealmGameRelationRepositoryImplTest extends BaseAndroidTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private RealmGameRelationRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        File tempFolder = testFolder.newFolder("realmdata");
        RealmConfiguration config = new RealmConfiguration.Builder(tempFolder).build();
        Realm.setDefaultConfiguration(config);
        repository = new RealmGameRelationRepositoryImpl();
    }

    @Test
    public void Given_EmptyDatabase_When_LoadingRelation_Then_EmitsError() throws Exception {

        // Act
        TestObserver<IGameRelationDatasource> result = repository.load(10).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
        result.assertNotComplete();
    }

    @Test
    public void Given_StoredEntity_When_RequestingRelation_Then_EmitsEntity() throws Exception {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmGameRelation data = realm.createObject(RealmGameRelation.class);
        RealmGame game = realm.createObject(RealmGame.class);
        game.setId(10);
        game.setName("title");
        data.setId(game.getId());
        data.setUpdatedAt(1000);
        data.setCreatedAt(1000);
        data.setGame(game);
        realm.commitTransaction();

        // Act
        TestObserver<IGameRelationDatasource> result = repository.load(data.getId()).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors();
        result.assertComplete();
        result.assertValueCount(1);
        result.assertValue(check(value -> {
            assertEquals(data.getId(), value.getId());
            assertEquals(data.getUpdatedAt(), value.getUpdatedAt());
            assertEquals(data.getCreatedAt(), value.getCreatedAt());
            assertEquals(data.getGame().getId(), value.getGame().getId());
            assertEquals(data.getGame().getName(), value.getGame().getName());
        }));
    }

    @Test
    public void Given_GameRelationEntity_When_SavesOnDatabase_Then_PersistsData() throws Exception {

        Realm realm = Realm.getDefaultInstance();
        RealmGameRelation data = new RealmGameRelation();
        RealmGame game = GameFactory.provideRealmGame(10, "title");
        data.setId(game.getId());
        data.setUpdatedAt(1000);
        data.setCreatedAt(1000);
        data.setGame(game);
        assertEquals(0, realm.where(RealmGameRelation.class).count());
        realm.close();

        // Act
        TestObserver<Void> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete();
        realm = Realm.getDefaultInstance();
        assertEquals(1, realm.where(RealmGameRelation.class).count());
        realm.close();
    }

    // TODO Realm does not return values
//    @Test
//    public void given_emptyDatabase_When_LoadsAll_Then_EmitsEmptyList() throws Exception {
//
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                Realm realm = Realm.getDefaultInstance();
//                assertEquals(0, realm.where(RealmGameRelation.class).count());
//                realm.close();
//
//                // Act
//                TestObserver<List<IGameRelationDatasource>> result = repository.loadAll().test();
//
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                // Assert
//                result.assertNoErrors()
//                        .assertNotComplete()
//                        .assertValue(check(value -> {
//                            assertTrue(value.isEmpty());
//                        }));
//            }
//        });
//    }
//
//    @Test
//    public void given_database_When_LoadsAll_Then_EmitsUpdatedList() throws Exception {
//
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                // Act
//                TestObserver<List<IGameRelationDatasource>> result = repository.loadAll().test();
//
//                // Assert
//                result.assertNoErrors()
//                        .assertNotComplete()
//                        .assertValue(check(value -> {
//                            assertTrue(value.isEmpty());
//                        }));
//
//                RealmGameRelation data = new RealmGameRelation();
//                RealmGame game = GameFactory.provideRealmGame(10, "title");
//                data.setId(game.getId());
//                data.setUpdatedAt(1000);
//                data.setCreatedAt(1000);
//                data.setGame(game);
//                Realm realm = Realm.getDefaultInstance();
//                realm.beginTransaction();
//                realm.copyToRealmOrUpdate(data);
//                realm.commitTransaction();
//
//                try {
//                    Thread.sleep(50000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                result.assertNoErrors()
//                        .assertNotComplete()
//                        .assertValue(check(values -> {
//                            assertEquals(1, values.size());
//                            assertEquals(data.getId(), values.get(0).getId());
//                        }));
//            }
//        });
//
//    }
}