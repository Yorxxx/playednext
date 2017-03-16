package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.RealmCollectionMapper;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.RealmCompanyMapper;
import com.piticlistudio.playednext.game.model.BaseGameTest;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelease.model.entity.GameRelease;
import com.piticlistudio.playednext.gamerelease.model.entity.RealmGameReleaseMapper;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.RealmGenreMapper;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.model.entity.RealmImageDataMapper;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.RealmPlatformMapper;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmGameMapperTest extends BaseGameTest {

    @Mock
    RealmImageDataMapper imageMapper;

    @Mock
    RealmCollectionMapper collectionMapper;

    @Mock
    RealmCompanyMapper companyMapper;

    @Mock
    RealmGenreMapper genreMapper;

    @Mock
    RealmGameReleaseMapper dateMapper;

    @Mock
    RealmPlatformMapper platformMapper;

    @InjectMocks
    private RealmGameMapper mapper;

    @Test
    public void given_nullData_When_Transform_Then_ReturnsAbsent() throws Exception {

        Game game = null;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_nullDataCover_When_Transform_Then_ReturnsGameWithoutCover() throws Exception {

        Game game = Game.create(10, "title");
        game.cover = null;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCover());
        assertFalse(result.get().getCover().isPresent());
    }

    @Test
    public void given_nonPresentCover_When_Transform_Then_ReturnsGameWithoutCover() throws Exception {
        Game game = Game.create(10, "title");
        game.cover = Optional.absent();

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCover());
        assertFalse(result.get().getCover().isPresent());
    }

    @Test
    public void given_coverMapError_When_Transform_Then_ReturnsGameWithoutCover() throws Exception {

        when(imageMapper.transform(any())).thenReturn(Optional.absent());
        Game game = Game.create(10, "title");
        ImageData image = ImageData.create("id", 100, 100, "url");
        game.cover = Optional.of(image);

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCover());
        assertFalse(result.get().getCover().isPresent());
        verify(imageMapper).transform(image);
    }

    @Test
    public void given_nullCollection_When_Transform_Then_ReturnsGameWithoutCollection() throws Exception {

        Game game = Game.create(10, "title");
        game.collection = null;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCollection());
        assertFalse(result.get().getCollection().isPresent());
    }

    @Test
    public void given_gameWithoutCollection_When_Transform_Then_ReturnsGameWithoutCollection() throws Exception {

        Game game = Game.create(10, "title");
        game.collection = Optional.absent();

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCollection());
        assertFalse(result.get().getCollection().isPresent());
    }

    @Test
    public void given_collectionMapError_When_Transform_Then_ReturnsGameWithoutCollection() throws Exception {

        when(collectionMapper.transform(any())).thenReturn(Optional.absent());
        Collection collection = Collection.create(10, "title");
        Game game = Game.create(10, "title");
        game.collection = Optional.of(collection);

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCollection());
        assertFalse(result.get().getCollection().isPresent());
        verify(collectionMapper).transform(collection);
    }

    @Test
    public void given_developerMapError_When_Transform_Then_ReturnsGameWithoutDeveloper() throws Exception {

        when(companyMapper.transform(any())).thenReturn(Optional.absent());
        List<Company> companies = new ArrayList<>();
        Company company1 = Company.create(10, "title");
        Company company2 = Company.create(11, "title2");
        companies.add(company1);
        companies.add(company2);
        Game game = Game.create(10, "title");
        game.developers = companies;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getDevelopers());
        assertTrue(result.get().getDevelopers().isEmpty());
        verify(companyMapper).transform(company1);
        verify(companyMapper).transform(company2);
    }

    @Test
    public void given_publisherMapError_When_Transform_Then_ReturnsGameWithoutDeveloper() throws Exception {

        when(companyMapper.transform(any())).thenReturn(Optional.absent());
        List<Company> companies = new ArrayList<>();
        Company company1 = Company.create(10, "title");
        Company company2 = Company.create(11, "title2");
        companies.add(company1);
        companies.add(company2);
        Game game = Game.create(10, "title");
        game.publishers = companies;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getPublishers());
        assertTrue(result.get().getPublishers().isEmpty());
        verify(companyMapper).transform(company1);
        verify(companyMapper).transform(company2);
    }

    @Test
    public void given_screenshotMapError_When_Transform_Then_ReturnsGameWithoutScreenshots() throws Exception {
        when(imageMapper.transform(any())).thenReturn(Optional.absent());
        List<ImageData> screenshots = new ArrayList<>();
        ImageData screen1 = ImageData.create("10", 100, 200, "url1");
        ImageData screen2 = ImageData.create("11", 100, 200, "url2");
        screenshots.add(screen1);
        screenshots.add(screen2);
        Game game = Game.create(10, "title");
        game.screenshots = screenshots;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getScreenshots());
        assertTrue(result.get().getScreenshots().isEmpty());
        verify(imageMapper).transform(screen2);
        verify(imageMapper).transform(screen1);
    }

    @Test
    public void given_genreMapError_WheN_Transform_Then_ReturnsGameWithoutGenre() throws Exception {

        when(genreMapper.transform(any())).thenReturn(Optional.absent());
        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.create(10, "title");
        Genre genre2 = Genre.create(11, "title2");
        genres.add(genre1);
        genres.add(genre2);
        Game game = Game.create(10, "title");
        game.genres = genres;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getGenres());
        assertTrue(result.get().getGenres().isEmpty());
        verify(genreMapper).transform(genre1);
        verify(genreMapper).transform(genre2);
    }

    @Test
    public void given_dateMapError_When_Transform_Then_ReturnsGameWithoutReleases() throws Exception {
        when(dateMapper.transform(any())).thenReturn(Optional.absent());
        List<GameRelease> releases = new ArrayList<>();
        ReleaseDate release1 = ReleaseDate.create(1000, "human");
        ReleaseDate release2 = ReleaseDate.create(2000, "human2");
        GameRelease gamerelease1 = GameRelease.create(Platform.create(10, "platform"), release1);
        GameRelease gamerelease2 = GameRelease.create(Platform.create(10, "platform"), release2);
        releases.add(gamerelease1);
        releases.add(gamerelease2);
        Game game = Game.create(10, "title");
        game.releases = releases;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getReleases());
        assertTrue(result.get().getReleases().isEmpty());
        verify(dateMapper).transform(gamerelease1);
        verify(dateMapper).transform(gamerelease2);
    }

    @Test
    public void given_platformMapError_When_Transform_Then_ReturnsGameWithoutPlatforms() throws Exception {
        when(platformMapper.transform(any())).thenReturn(Optional.absent());
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.create(10, "title");
        Platform platform2 = Platform.create(12, "title2");
        platforms.add(platform1);
        platforms.add(platform2);
        Game game = Game.create(10, "title");
        game.platforms = platforms;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getPlatforms());
        assertTrue(result.get().getPlatforms().isEmpty());
        verify(platformMapper).transform(platform1);
        verify(platformMapper).transform(platform2);
    }

    public void transform() throws Exception {
        Game game = null;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        game = GameFactory.provide(50, "title");

        // Act
        result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(equalsGame(game, result.get()));
    }

}