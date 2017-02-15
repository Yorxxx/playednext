package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.RealmCollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.RealmCompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelease.model.entity.GameRelease;
import com.piticlistudio.playednext.gamerelease.model.entity.RealmGameReleaseMapper;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.RealmGenreMapper;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.model.entity.RealmImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.RealmPlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import javax.inject.Inject;

import io.realm.RealmList;

/**
 * Maps a Game entity into a RealGame entity
 * Created by jorge.garcia on 14/02/2017.
 */

public class RealmGameMapper implements Mapper<RealmGame, Game> {

    private final RealmImageDataMapper imageMapper;
    private final RealmCollectionMapper collectionMapper;
    private final RealmCompanyMapper companyMapper;
    private final RealmGenreMapper genreMapper;
    private final RealmGameReleaseMapper dateMapper;
    private final RealmPlatformMapper platformMapper;

    @Inject
    public RealmGameMapper(RealmImageDataMapper imageMapper, RealmCollectionMapper collectionMapper, RealmCompanyMapper companyMapper,
                           RealmGenreMapper genreMapper, RealmGameReleaseMapper dateMapper, RealmPlatformMapper platformMapper) {
        this.imageMapper = imageMapper;
        this.collectionMapper = collectionMapper;
        this.companyMapper = companyMapper;
        this.genreMapper = genreMapper;
        this.dateMapper = dateMapper;
        this.platformMapper = platformMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmGame> transform(Game data) {
        if (data == null)
            return Optional.absent();
        RealmGame result = new RealmGame();
        result.setId(data.id());
        result.setName(data.title());
        result.setStoryline(data.storyline);
        result.setSummary(data.summary);

        if (data.cover != null && data.cover.isPresent()) {
            Optional<RealmImageData> image = imageMapper.transform(data.cover.get());
            if (image.isPresent())
                result.setCover(image.get());
        }

        if (data.collection != null && data.collection.isPresent()) {
            Optional<RealmCollection> collection = collectionMapper.transform(data.collection.get());
            if (collection.isPresent())
                result.setCollection(collection.get());
        }

        RealmList<RealmCompany> developers = new RealmList<>();
        for (Company developer : data.developers) {
            Optional<RealmCompany> company = companyMapper.transform(developer);
            if (company.isPresent())
                developers.add(company.get());
        }
        result.setDevelopers(developers);

        RealmList<RealmCompany> publishers = new RealmList<>();
        for (Company publisher : data.publishers) {
            Optional<RealmCompany> company = companyMapper.transform(publisher);
            if (company.isPresent())
                publishers.add(company.get());
        }
        result.setPublishers(publishers);

        RealmList<RealmImageData> screens = new RealmList<>();
        for (ImageData screenshot : data.screenshots) {
            Optional<RealmImageData> image = imageMapper.transform(screenshot);
            if (image.isPresent())
                screens.add(image.get());
        }
        result.setScreenshots(screens);

        RealmList<RealmGenre> genres = new RealmList<>();
        for (Genre genre : data.genres) {
            Optional<RealmGenre> genredata = genreMapper.transform(genre);
            if (genredata.isPresent())
                genres.add(genredata.get());
        }
        result.setGenres(genres);

        RealmList<RealmGameRelease> releases = new RealmList<>();
        for (GameRelease release : data.releases) {
            Optional<RealmGameRelease> releaseOptional = dateMapper.transform(release);
            if (releaseOptional.isPresent())
                releases.add(releaseOptional.get());
        }
        result.setReleases(releases);

        RealmList<RealmPlatform> platforms = new RealmList<>();
        for (Platform platform : data.platforms) {
            Optional<RealmPlatform> platformOptional = platformMapper.transform(platform);
            if (platformOptional.isPresent())
                platforms.add(platformOptional.get());
        }
        result.setPlatforms(platforms);

        return Optional.of(result);
    }
}
