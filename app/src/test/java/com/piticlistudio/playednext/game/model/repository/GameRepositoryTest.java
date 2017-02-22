package com.piticlistudio.playednext.game.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.BaseGameTest;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.NetGame;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.genre.model.repository.IGenreRepository;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.PlatformModule;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.platform.model.repository.IPlatformRepository;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

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
import static org.mockito.Matchers.any;
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
    @Mock
    IGenreRepository genreRepository;
    @Mock
    IPlatformRepository platformRepository;

    private GameRepository repository;
    @Rule
    public DaggerMockRule<GamedataComponent> rule = new DaggerMockRule<>(GamedataComponent.class, new GamedataModule())
            .set(component -> repository = component.plus(new AppModule(null), new GameModule(), new CollectionModule(), new CompanyModule
                    (), new GenreModule(), new PlatformModule())
                    .repository());
    private RealmGame localData = GameFactory.provideRealmGame(50, "title");

    @Before
    public void setUp() throws Exception {
        assertNotNull(repository);
    }

    @Test
    public void save() throws Exception {
        Game data = GameFactory.provide(50, "name");
        doAnswer(invocation -> Observable.just(invocation.getArguments()[0])).when(dataRepository).save(any());

        // Act
        TestObserver<Game> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(value -> {
                    assertEquals(data, value);
                }));
        verify(dataRepository).save(any(RealmGame.class));
    }

    @Test
    public void save_mapInvalid() throws Exception {

        Game data = null;

        // Act
        TestObserver<Game> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
        verifyZeroInteractions(dataRepository);
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
        assertEquals(3, source.getPublishers().size());
        assertEquals(3, source.getGenres().size());

        Collection collection = Collection.create(10, "name");
        when(collectionRepository.load(anyInt())).thenReturn(Observable.just(collection).delay(5, TimeUnit.SECONDS));
        when(dataRepository.load(source.getId())).thenReturn(Observable.just(source).delay(2, TimeUnit.SECONDS));

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Company.create(id, "company_" + id)).delay(10, TimeUnit.SECONDS);
        }).when(companyRepository).load(anyInt());

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Genre.create(id, "genre_" + id)).delay(15, TimeUnit.SECONDS);
        }).when(genreRepository).load(anyInt());

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Platform.create(id, "platform_" + id)).delay(20, TimeUnit.SECONDS);
        }).when(platformRepository).load(anyInt());

        // Act
        TestObserver<Game> result = repository.load(gameId).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(1)
                .assertValue(check(game -> assertFalse(game.collection.isPresent())));

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
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Check if developers and publishers have been loaded
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(4)
                .assertValueAt(3, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                    assertEquals(source.getDevelopers().size(), game.developers.size());
                    assertEquals(source.getPublishers().size(), game.publishers.size());
                }));
        verify(companyRepository, times(6)).load(anyInt());


        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(6, TimeUnit.SECONDS);

        // Check if genres have been loaded
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(5)
                .assertValueAt(4, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                    assertEquals(source.getDevelopers().size(), game.developers.size());
                    assertEquals(source.getPublishers().size(), game.publishers.size());
                    assertEquals(source.getGenres().size(), game.genres.size());
                }));
        verify(genreRepository, times(3)).load(anyInt());

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(6, TimeUnit.SECONDS);

        // Check if genres have been loaded
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(7)
                .assertValueAt(6, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                    assertEquals(source.getDevelopers().size(), game.developers.size());
                    assertEquals(source.getPublishers().size(), game.publishers.size());
                    assertEquals(source.getGenres().size(), game.genres.size());
                    assertEquals(source.getPlatforms().size(), game.platforms.size());
                    assertEquals(source.getReleases().size(), game.releases.size());
                }));
        verify(platformRepository, times(source.getPlatforms().size()+source.getReleases().size())).load(anyInt());
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
    public void loadMissingCompanies_empty() throws Exception {

        // Act
        TestObserver<List<Company>> result = repository.loadMissingCompanies(new ArrayList<>(), new ArrayList<>()).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(companyRepository);
    }

    @Test
    public void loadMissingCompanies_allLoaded() throws Exception {

        List<Company> loaded = new ArrayList<>();
        Company dev1 = Company.create(1, "name1");
        Company dev2 = Company.create(2, "name2");
        loaded.add(dev1);
        loaded.add(dev2);

        List<NetworkEntityIdRelation<ICompanyData>> request = new ArrayList<>();
        request.add(new NetworkEntityIdRelation<>(1, Optional.of(new RealmCompany(1, "name1"))));
        request.add(new NetworkEntityIdRelation<>(2, Optional.of(new RealmCompany(2, "name2"))));

        // Act
        TestObserver<List<Company>> result = repository.loadMissingCompanies(request, loaded).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(companyRepository);
    }

    @Test
    public void loadMissingCompanies_requestsMissing() throws Exception {

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Company.create(id, "company_" + id));
        }).when(companyRepository).load(anyInt());

        List<Company> loaded = new ArrayList<>();
        Company dev1 = Company.create(1, "name1");
        Company dev2 = Company.create(2, "name2");
        loaded.add(dev1);
        loaded.add(dev2);

        List<NetworkEntityIdRelation<ICompanyData>> request = new ArrayList<>();
        request.add(new NetworkEntityIdRelation<>(1, Optional.of(new RealmCompany(1, "name1"))));
        request.add(new NetworkEntityIdRelation<>(2, Optional.of(new RealmCompany(2, "name2"))));
        request.add(new NetworkEntityIdRelation<>(3, Optional.absent()));
        request.add(new NetworkEntityIdRelation<>(4, Optional.absent()));

        // Act
        TestObserver<List<Company>> result = repository.loadMissingCompanies(request, loaded).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertComplete()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(check(data -> {
                    assertEquals(2, data.size());
                    assertEquals(3, data.get(0).id());
                    assertEquals(4, data.get(1).id());
                }));

        verify(companyRepository, times(2)).load(anyInt());
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

    @Test
    public void loadGenres_empty() throws Exception {

        NetGame from = GameFactory.provideNetGame(10, "title");
        from.genres.clear();
        Game dest = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadGenres(from, dest).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(genreRepository);
    }

    @Test
    public void loadGenres_genresAlreadyLoaded() throws Exception {
        Game to = Game.create(10, "title");
        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.create(1, "name1");
        Genre genre2 = Genre.create(2, "name2");
        genres.add(genre1);
        genres.add(genre2);
        to.genres = genres;

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmGenre> realmGenres = new RealmList<>();
        realmGenres.add(new RealmGenre(1, "name1"));
        realmGenres.add(new RealmGenre(2, "name2"));
        source.setGenres(realmGenres);

        // Act
        TestObserver<Game> result = repository.loadGenres(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(genreRepository);
    }

    @Test
    public void loadGenre_requestsGenre() throws Exception {

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Genre.create(id, "genre_" + id));
        }).when(genreRepository).load(anyInt());

        Game to = Game.create(10, "title");
        to.genres = new ArrayList<>();

        NetGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getGenres().size() > 0);

        // Act
        TestObserver<Game> result = repository.loadGenres(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(to.genres.size(), source.getGenres().size());
        verify(genreRepository, times(source.getGenres().size())).load(anyInt());
    }

    @Test
    public void loadPlatforms_noPlatforms() throws Exception {

        NetGame from = GameFactory.provideNetGame(10, "title");
        from.release_dates.clear();
        Game dest = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadPlatforms(from, dest).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(platformRepository);
    }

    @Test
    public void loadPlatforms_platformsAlreadyLoaded() throws Exception {
        Game to = Game.create(10, "title");
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.create(1, "name1");
        Platform platform2 = Platform.create(2, "name2");
        platforms.add(platform1);
        platforms.add(platform2);
        to.platforms = platforms;

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmPlatform> realmPlatforms = new RealmList<>();
        realmPlatforms.add(new RealmPlatform(1, "name1"));
        realmPlatforms.add(new RealmPlatform(2, "name2"));
        source.setPlatforms(realmPlatforms);

        // Act
        TestObserver<Game> result = repository.loadPlatforms(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(platformRepository);
    }

    @Test
    public void loadPlatforms_requestsPlatform() throws Exception {

        Game to = Game.create(10, "title");
        to.platforms = new ArrayList<>();

        NetGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getPlatforms().size() > 0);

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Platform.create(id, "platform_" + id));
        }).when(platformRepository).load(anyInt());

        // Act
        TestObserver<Game> result = repository.loadPlatforms(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(to.platforms.size(), source.getPlatforms().size());
        verify(platformRepository, times(source.getPlatforms().size())).load(anyInt());
    }

    @Test
    public void loadReleases_noReleases() throws Exception {

        NetGame from = GameFactory.provideNetGame(10, "title");
        from.release_dates.clear();
        Game dest = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadReleases(from, dest).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(platformRepository);
    }

    @Test
    public void loadReleases_requestsReleases() throws Exception {

        doAnswer(invocation -> {
            int id = (int)invocation.getArguments()[0];
            return Observable.just(Platform.create(id, "platform_"+id));
        }).when(platformRepository).load(anyInt());

        Game to = Game.create(10, "title");
        to.releases = new ArrayList<>();

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmGameRelease> realmReleases = new RealmList<>();
        RealmPlatform realmPlatform1 = new RealmPlatform(1, "name1");
        RealmReleaseDate realmDate1 = new RealmReleaseDate("human-date", 1000);
        RealmPlatform realmPlatform2 = new RealmPlatform(2, "name2");
        RealmReleaseDate realmDate2 = new RealmReleaseDate("human-date", 1000);
        realmReleases.add(new RealmGameRelease(realmPlatform1, realmDate1));
        realmReleases.add(new RealmGameRelease(realmPlatform2, realmDate2));
        source.setReleases(realmReleases);

        // Act
        TestObserver<Game> result = repository.loadReleases(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(to.releases.size(), source.getReleases().size());
        verify(platformRepository, times(source.getReleases().size())).load(anyInt());
    }
}