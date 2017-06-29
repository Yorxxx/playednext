package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.CompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.gamerelease.model.entity.GameRelease;
import com.piticlistudio.playednext.gamerelease.model.entity.GameReleaseMapper;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGameReleaseDateData;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.GenreMapper;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.model.entity.ImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GameMapper implements Mapper<Game, IGameDatasource> {

    private final CollectionMapper collectionMapper;
    private final ImageDataMapper imageMapper;
    private final CompanyMapper companyMapper;
    private final GenreMapper genreMapper;
    private final GameReleaseMapper releaseMapper;
    private final PlatformMapper platformMapper;

    @Inject
    public GameMapper(CollectionMapper collectionMapper, ImageDataMapper imageMapper, CompanyMapper companyMapper, GenreMapper genreMapper,
                      GameReleaseMapper releaseMapper, PlatformMapper platformMapper) {
        this.collectionMapper = collectionMapper;
        this.imageMapper = imageMapper;
        this.companyMapper = companyMapper;
        this.genreMapper = genreMapper;
        this.releaseMapper = releaseMapper;
        this.platformMapper = platformMapper;
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<Game> transform(IGameDatasource data) {
        if (data == null || data.getName() == null)
            return Optional.absent();
        Game result = Game.create(data.getId(), data.getName());
        result.storyline = data.getStoryline();
        result.summary = data.getSummary();
        result.syncedAt = data.syncedAt();
        if (data.getCollection().isPresent() && data.getCollection().get().data.isPresent()) {
            result.collection = collectionMapper.transform(data.getCollection().get().getData());
        }
        if (data.getCover().isPresent()) {
            result.cover = imageMapper.transform(data.getCover().get());
        }
        for (IImageData iImageData : data.getScreenshots()) {
            Optional<ImageData> image = imageMapper.transform(iImageData);
            if (image.isPresent())
                result.screenshots.add(image.get());
        }
        List<Company> developers = new ArrayList<>();
        for (NetworkEntityIdRelation<ICompanyData> iCompanyDataNetworkEntityIdRelation : data.getDevelopers()) {
            if (iCompanyDataNetworkEntityIdRelation.data.isPresent()) {
                Optional<Company> company = companyMapper.transform(iCompanyDataNetworkEntityIdRelation.data.get());
                if (company.isPresent()) {
                    developers.add(company.get());
                }
            }
        }
        result.developers = developers;

        List<Company> publishers = new ArrayList<>();
        for (NetworkEntityIdRelation<ICompanyData> iCompanyDataNetworkEntityIdRelation : data.getPublishers()) {
            if (iCompanyDataNetworkEntityIdRelation.data.isPresent()) {
                Optional<Company> company = companyMapper.transform(iCompanyDataNetworkEntityIdRelation.data.get());
                if (company.isPresent()) {
                    publishers.add(company.get());
                }
            }
        }
        result.publishers = publishers;

        List<Genre> genres = new ArrayList<>();
        for (NetworkEntityIdRelation<IGenreData> iGenreDataNetworkEntityIdRelation : data.getGenres()) {
            if (iGenreDataNetworkEntityIdRelation.data.isPresent()) {
                Optional<Genre> genre = genreMapper.transform(iGenreDataNetworkEntityIdRelation.data.get());
                if (genre.isPresent())
                    genres.add(genre.get());
            }
        }
        result.genres = genres;

        List<GameRelease> releases = new ArrayList<>();
        for (IGameReleaseDateData iGameReleaseDateData : data.getReleases()) {
            Optional<GameRelease> release = releaseMapper.transform(iGameReleaseDateData);
            if (release.isPresent())
                releases.add(release.get());
        }
        result.releases = releases;

        List<Platform> platforms = new ArrayList<>();
        for (NetworkEntityIdRelation<IPlatformData> iPlatformDataNetworkEntityIdRelation : data.getPlatforms()) {
            if (iPlatformDataNetworkEntityIdRelation.data.isPresent()) {
                Optional<Platform> platform = platformMapper.transform(iPlatformDataNetworkEntityIdRelation.getData());
                if (platform.isPresent()) {
                    platforms.add(platform.get());
                }
            }
        }
        result.platforms = platforms;

        return Optional.of(result);
    }
}
