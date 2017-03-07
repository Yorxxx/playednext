package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.game.model.BaseGameTest;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.datasource.IGDBGame;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Mapper
 * Created by jorge.garcia on 10/02/2017.
 */
public class GameMapperTest extends BaseGameTest {

    @Rule
    public DaggerMockRule<GamedataComponent> rule = new DaggerMockRule<>(GamedataComponent.class, new GamedataModule());

    @InjectFromComponent
    private GameMapper mapper;

    @Test
    public void given_gameWithoutCollection_When_Transforms_Then_ReturnsGameWithoutCollection() throws Exception {

        IGDBGame data = GameFactory.provideNetGame(10, "title");
        data.collection = 0;

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().collection);
        assertFalse(result.get().collection.isPresent());
    }

    @Test
    public void given_notPresentCollectionData_When_Transforms_Then_ReturnsGameWithoutCollection() throws Exception {

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        NetworkEntityIdRelation<ICollectionData> collection = new NetworkEntityIdRelation<>(10, Optional.absent());
        when(data.getCollection()).thenReturn(Optional.of(collection));
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().collection);
        assertFalse(result.get().collection.isPresent());
    }

    @Test
    public void given_notPresentCover_When_Transforms_Then_ReturnsGameWithoutCover() throws Exception {

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().cover);
        assertFalse(result.get().cover.isPresent());
    }

    @Test
    public void given_notPresentScreenshot_When_Transforms_Then_ReturnsGameWithoutScreen() throws Exception {

        List<IImageData> screenshots = new ArrayList<>();
        screenshots.add(new RealmImageData());
        screenshots.add(new RealmImageData());
        screenshots.add(new RealmImageData());
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(screenshots);
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().screenshots);
        assertTrue(result.get().screenshots.isEmpty());
    }

    @Test
    public void given_notPresentDeveloper_When_Transforms_Then_ReturnsGameWithoutDeveloper() throws Exception {

        List<NetworkEntityIdRelation<ICompanyData>> companies = new ArrayList<>();
        companies.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmCompany())));
        companies.add(new NetworkEntityIdRelation<>(11, Optional.of(new RealmCompany())));
        companies.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmCompany())));
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(companies);
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().developers);
        assertTrue(result.get().developers.isEmpty());
    }

    @Test
    public void given_notPresentPublisher_When_Transforms_Then_ReturnsGameWithoutPublishers() throws Exception {

        List<NetworkEntityIdRelation<ICompanyData>> companies = new ArrayList<>();
        companies.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmCompany())));
        companies.add(new NetworkEntityIdRelation<>(11, Optional.of(new RealmCompany())));
        companies.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmCompany())));
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(companies);
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().publishers);
        assertTrue(result.get().publishers.isEmpty());
    }

    @Test
    public void given_notPresentGenre_When_Transforms_Then_ReturnsGameWithoutGenres() throws Exception {

        List<NetworkEntityIdRelation<IGenreData>> genres = new ArrayList<>();
        genres.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmGenre())));
        genres.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmGenre())));
        genres.add(new NetworkEntityIdRelation<>(14, Optional.of(new RealmGenre())));

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(genres);
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().genres);
        assertTrue(result.get().genres.isEmpty());
    }

    @Test
    public void given_notMappablePlatform_When_Transforms_Then_ReturnsGameWithoutPlatform() throws Exception {

        List<NetworkEntityIdRelation<IPlatformData>> platforms = new ArrayList<>();
        platforms.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmPlatform())));
        platforms.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmPlatform())));
        platforms.add(new NetworkEntityIdRelation<>(14, Optional.of(new RealmPlatform())));

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(platforms);

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().platforms);
        assertTrue(result.get().platforms.isEmpty());
    }

    @Test
    public void given_nullData_When_Transforms_Then_ReturnsAbsent() throws Exception {

        IGameDatasource data = null;

        Optional<Game> result = mapper.transform(data);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void given_dataWithoutName_When_Transforms_Then_ReturnsAbsent() throws Exception {

        RealmGame data = new RealmGame();
        data.setId(50);
        data.setName(null);

        Optional<Game> result = mapper.transform(data);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void given_validData_When_Transform_Then_ReturnsGameEntity() throws Exception {

        RealmGame data = GameFactory.provideRealmGame(0, "title");

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertTrue(result.isPresent());
        assertTrue("Should match game properties", equalsGame(result.get(), data));

    }
}