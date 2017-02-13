package com.piticlistudio.playednext.game.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.BaseGameTest;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.NetGame;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.realm.RealmList;
import it.cosenonjaviste.daggermock.DaggerMockRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test cases for Game
 * Created by jorge.garcia on 10/02/2017.
 */
public class GameRepositoryTest extends BaseGameTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();
    @Mock
    IGamedataRepository dataRepository;
    @Mock
    ICollectionRepository collectionRepository;
    @Mock
    ICompanyRepository companyRepository;

    private GameRepository repository;
    @Rule
    public DaggerMockRule<GamedataComponent> rule = new DaggerMockRule<>(GamedataComponent.class, new GamedataModule())
            .set(component -> {
                repository = component.plus(new GameModule(), new CollectionModule(), new CompanyModule()).repository();
            });
    private RealmGame localData = GameFactory.provideRealmGame(50, "title");

    @Before
    public void setUp() throws Exception {
        assertNotNull(repository);
    }

    @Test
    public void load() throws Exception {

        when(dataRepository.load(50)).thenReturn(Observable.just(localData));

        // Act
        TestObserver<Game> result = repository.load(50).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(game -> assertTrue(equalsGame(game, localData))));
    }

    @Test
    public void load_notFound() throws Exception {

        Throwable error = new Exception("bla");
        when(dataRepository.load(50)).thenReturn(Observable.error(error));

        // Act
        TestObserver<Game> result = repository.load(50).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(error)
                .assertNoValues()
                .assertNotComplete();
    }

    @Test
    public void load_mapError() throws Exception {

        when(dataRepository.load(50)).thenReturn(Observable.just(new RealmGame()));

        // Act
        TestObserver<Game> result = repository.load(50).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
    }

    @Test
    public void load_requestsAggregatedData() throws Exception {

        final int gameId = 50;

        IGameDatasource source = GameFactory.provideNetGame(gameId, "title");
        assertTrue(source.getCollection().isPresent());
        assertFalse(source.getCollection().get().data.isPresent());
        assertEquals(3, source.getDevelopers().size());

        Collection collection = Collection.create(10, "name");
        when(collectionRepository.load(anyInt())).thenReturn(Observable.just(collection).delay(5, TimeUnit.SECONDS));
        when(dataRepository.load(source.getId())).thenReturn(Observable.just(source).delay(2, TimeUnit.SECONDS));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                int id = (int) invocation.getArguments()[0];
                return Observable.just(Company.create(id, "company_" + id)).delay(10, TimeUnit.SECONDS);
            }
        }).when(companyRepository).load(anyInt());

        // Act
        TestObserver<Game> result = repository.load(gameId).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(1)
                .assertValue(check(game -> {
                    assertFalse(game.collection.isPresent());
                }));

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(6, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(2)
                .assertValueAt(1, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                }));


        verify(collectionRepository).load(source.getCollection().get().id);

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(11, TimeUnit.SECONDS);

        // Check if developers have been loaded
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(3)
                .assertValueAt(2, check(game -> {
                    assertEquals(source.getDevelopers().size(), game.developers.size());
                }));
    }

    @Test
    public void search() throws Exception {
        List<IGameDatasource> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(GameFactory.provideNetGame(i, "title" + i));
        }
        when(dataRepository.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<List<Game>> result = repository.search("1", 0, 10).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(games -> {
                    assertEquals(data.size(), games.size());
                    for (int i = 0; i < data.size(); i++) {
                        assertTrue(equalsGame(games.get(i), data.get(i)));
                    }
                }));
    }

    @Test
    public void search_filtersOutInvalidMapping() throws Exception {

        List<IGameDatasource> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(GameFactory.provideNetGame(i, "title" + i));
        }
        RealmGame invalid = new RealmGame();
        data.add(invalid);
        data.add(invalid);
        data.add(invalid);
        data.add(invalid);
        when(dataRepository.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<List<Game>> result = repository.search("1", 0, 10).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(games -> {
                    assertEquals(20, games.size());
                    for (int i = 0; i < 20; i++) {
                        assertTrue(equalsGame(games.get(i), data.get(i)));
                        assertFalse(equalsGame(games.get(i), invalid));
                    }
                }));
    }

    @Test
    public void search_error() throws Exception {

        Throwable error = new Exception();
        when(dataRepository.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.error(error));

        // Act
        TestObserver<List<Game>> result = repository.search("1", 0, 10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(Throwable.class);
    }

    @Test
    public void loadCollection_noCollection() throws Exception {

        NetGame source = GameFactory.provideNetGame(10, "title");
        source.collection = -1;
        Game to = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadCollection(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(collectionRepository);
    }

    @Test
    public void loadCollection_collectionAlreadyLoaded() throws Exception {

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        assertTrue(source.getCollection().isPresent());
        assertTrue(source.getCollection().get().data.isPresent());

        Game to = Game.create(10, "title");
        Collection collection = Collection.create(11, "collection");
        to.collection = Optional.of(collection);

        // Act
        TestObserver<Game> result = repository.loadCollection(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(collectionRepository);
    }

    @Test
    public void loadCollection_requestsCollection() throws Exception {

        NetGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getCollection().isPresent());
        assertFalse(source.getCollection().get().data.isPresent());

        Game to = Game.create(10, "title");

        Collection collection = Collection.create(10, "name");
        when(collectionRepository.load(anyInt())).thenReturn(Observable.just(collection).delay(1, TimeUnit.SECONDS));

        // Act
        TestObserver<Game> result = repository.loadCollection(source, to).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertTrue(to.collection.isPresent());
        assertEquals(collection, to.collection.get());
        verify(collectionRepository).load(source.getCollection().get().id);
    }

    @Test
    public void loadDevelopers_noDevelopers() throws Exception {

        NetGame source = GameFactory.provideNetGame(10, "title");
        source.developers.clear();
        Game to = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadDevelopers(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(companyRepository);
    }

    @Test
    public void loadDevelopers_developersAlreadyLoaded() throws Exception {

        Game to = Game.create(10, "title");
        List<Company> developers = new ArrayList<>();
        Company dev1 = Company.create(1, "name1");
        Company dev2 = Company.create(2, "name2");
        developers.add(dev1);
        developers.add(dev2);
        to.developers = developers;

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmCompany> realmCompanies = new RealmList<>();
        realmCompanies.add(new RealmCompany(1, "name1"));
        realmCompanies.add(new RealmCompany(2, "name2"));
        source.setDevelopers(realmCompanies);

        // Act
        TestObserver<Game> result = repository.loadDevelopers(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(companyRepository);
    }

    @Test
    public void loadDevelopers_requestsDevelopers() throws Exception {

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Company.create(id, "company_" + id));
        }).when(companyRepository).load(anyInt());

        Game to = Game.create(10, "title");
        to.developers = new ArrayList<>();

        NetGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getDevelopers().size() > 0);

        // Act
        TestObserver<Game> result = repository.loadDevelopers(source, to).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(to.developers.size(), source.getDevelopers().size());
        verify(companyRepository, times(source.getDevelopers().size())).load(anyInt());
    }
}