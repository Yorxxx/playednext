package com.piticlistudio.playednext.game.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;
import com.piticlistudio.playednext.gamerelease.model.entity.GameRelease;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGameReleaseDateData;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.repository.IGenreRepository;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.repository.IPlatformRepository;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDateMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Repository for Game entities.
 * Unlike other repositories, and due to limitation on repository datasource implementation, any requests tries to retrieve all
 * associated data of the entity (ie companies, genres...) in case is not loaded.
 * Created by jorge.garcia on 10/02/2017.
 */

public class GameRepository implements IGameRepository {

    private final IGamedataRepository repository;
    private final GameMapper mapper;
    private final RealmGameMapper realmMapper;
    private final ICollectionRepository collectionRepository;
    private final ICompanyRepository companyRepository;
    private final IGenreRepository genreRepository;
    private final IPlatformRepository platformRepository;
    private final ReleaseDateMapper dateMapper;

    @Inject
    public GameRepository(IGamedataRepository repository, GameMapper mapper, RealmGameMapper realmMapper,
                          ICollectionRepository collectionRepository, ICompanyRepository companyRepository,
                          IGenreRepository genreRepository, IPlatformRepository platformRepository,
                          ReleaseDateMapper dateMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.realmMapper = realmMapper;
        this.collectionRepository = collectionRepository;
        this.companyRepository = companyRepository;
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
        this.dateMapper = dateMapper;
    }

    /**
     * Loads the game with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the game loaded
     */
    @Override
    public Observable<Game> load(int id) {
        return repository.load(id)
                .flatMap(new Function<IGameDatasource, ObservableSource<Game>>() {
                    @Override
                    public ObservableSource<Game> apply(IGameDatasource iGameDatasource) throws Exception {
                        return mapSource(iGameDatasource)
                                .flatMap(new Function<Game, ObservableSource<Game>>() {
                                    @Override
                                    public ObservableSource<Game> apply(Game game) throws Exception {
                                        List<Observable<Game>> operators = new ArrayList<>();
                                        operators.add(Observable.just(game));
                                        operators.add(loadCollection(iGameDatasource, game));
                                        operators.add(loadDevelopers(iGameDatasource, game));
                                        operators.add(loadPublishers(iGameDatasource, game));
                                        operators.add(loadGenres(iGameDatasource, game));
                                        operators.add(loadPlatforms(iGameDatasource, game));
                                        operators.add(loadReleases(iGameDatasource, game));
                                        return Observable.merge(operators);
                                    }
                                });
                    }
                });
    }

    /**
     * Searches game that matches the specified query name
     * TODO should retrieve all aggregated data??
     *
     * @param query  the name to query.
     * @param offset the offset of the query
     * @param limit  the max amount of items to return
     * @return a list of games matching the query.
     */
    @Override
    public Observable<List<Game>> search(String query, int offset, int limit) {
        return repository.search(query, offset, limit)
                .flatMap(sources -> Observable.fromIterable(sources)
                        .map(mapper::transform)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList().toObservable());
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return the saved data.
     */
    @Override
    public Observable<Game> save(Game data) {
        Optional<RealmGame> model = realmMapper.transform(data);
        if (model.isPresent()) {
            return repository.save(model.get())
                    .flatMap(this::mapSource);
        } else return Observable.error(new Exception("Cannot map"));
    }

    private Observable<Game> mapSource(IGameDatasource iGameDatasource) {
        Optional<Game> result = mapper.transform(iGameDatasource);
        if (!result.isPresent())
            return Observable.error(new Exception("Cannot map"));
        return Observable.just(result.get());
    }

    /**
     * Loads the collection from the supplied source
     * By checking on the source data, requests the collection associated and updates the requested
     * Game.
     * If game has collection already loaded or source has no data to load, nothing will be emitted.
     *
     * @param from the source containing the collection info
     * @param to   the entity in which to set the data
     * @return an Observable that emits the request with the collection on the source, loaded.
     */
    protected Observable<Game> loadCollection(IGameDatasource from, Game to) {
        if (to.collection.isPresent()) {
            return Observable.empty();
        }
        if (!from.getCollection().isPresent()) {
            return Observable.empty();
        }
        return collectionRepository.load(from.getCollection().get().id)
                .map(collection -> {
                    to.collection = Optional.fromNullable(collection);
                    return to;
                });
    }

    /**
     * Loads the missing companies that are not present on the loaded list
     * If companies is empty or loadedList has every data loaded, does not emit anything
     *
     * @param companies  the list of companies to request
     * @param loadedList the list of already loaded companies.
     * @return an Observable that emits the new loaded companies
     */
    Observable<List<Company>> loadMissingCompanies(List<NetworkEntityIdRelation<ICompanyData>> companies, List<Company> loadedList) {
        if (companies.isEmpty())
            return Observable.empty();

        List<Integer> missingIds = new ArrayList<>();
        for (NetworkEntityIdRelation<ICompanyData> iCompanyDataNetworkEntityIdRelation : companies) {
            int requestedId = iCompanyDataNetworkEntityIdRelation.id;
            boolean found = false;
            for (Company company : loadedList) {
                if (company.id() == requestedId)
                    found = true;
            }
            if (!found)
                missingIds.add(requestedId);
        }
        if (missingIds.size() == 0)
            return Observable.empty();
        return Observable.fromIterable(missingIds)
                .flatMap(companyRepository::load)
                .toList()
                .toObservable();
    }

    /**
     * Loads the developers from the supplied source
     * Will only request the developers that are present in source but not on destination
     * If source has no developers, does not emit anything.
     *
     * @param from the source containing the developers info
     * @param to   the entity in which to set the data
     * @return an Observable that emits the request with the developers on the source, loaded.
     */
    Observable<Game> loadDevelopers(IGameDatasource from, Game to) {
        return loadMissingCompanies(from.getDevelopers(), to.developers)
                .map(companies -> {
                    to.developers.addAll(companies);
                    return to;
                });
    }

    /**
     * Loads the publishers from the supplied source
     * Will only request the publishers that are present in source but not on destination
     * If source has no publishers, does not emit anything.
     *
     * @param from the source containing the publishers info
     * @param to   the entity in which to set the data
     * @return an Observable that emits the request with the publishers on the source, loaded.
     */
    Observable<Game> loadPublishers(IGameDatasource from, Game to) {
        return loadMissingCompanies(from.getPublishers(), to.publishers)
                .map(companies -> {
                    to.publishers.addAll(companies);
                    return to;
                });

    }

    /**
     * Loads the genres from the supplied source
     * Will only request the genres that are present in source but not on destination
     * If source has no genres, does not emit anything.
     *
     * @param from the source containing the genres info
     * @param dest the entity in which to set the data
     * @return an Observable that emits the request with the genres on the source, loaded.
     */
    Observable<Game> loadGenres(IGameDatasource from, Game dest) {
        if (from.getGenres().isEmpty())
            return Observable.empty();
        List<Integer> missingIds = new ArrayList<>();
        for (NetworkEntityIdRelation<IGenreData> iGenreDataNetworkEntityIdRelation : from.getGenres()) {
            int requestedId = iGenreDataNetworkEntityIdRelation.id;
            boolean found = false;
            for (Genre genre : dest.genres) {
                if (genre.id() == requestedId)
                    found = true;
            }
            if (!found)
                missingIds.add(requestedId);
        }
        if (missingIds.size() == 0)
            return Observable.empty();
        return Observable.fromIterable(missingIds)
                .flatMap(genreRepository::load)
                .toList()
                .map(list -> {
                    dest.genres.addAll(list);
                    return dest;
                })
                .toObservable();
    }

    /**
     * Loads the platforms from the supplied source
     * Will only request the platforms that are present in source but not on destination
     * If source has no platforms, does not emit anything.
     *
     * @param from the source containing the platforms info
     * @param dest the entity in which to set the data
     * @return an Observable that emits the request with the platforms on the source, loaded.
     */
    Observable<Game> loadPlatforms(IGameDatasource from, Game dest) {
        if (from.getPlatforms().isEmpty())
            return Observable.empty();

        List<Integer> missingIds = new ArrayList<>();
        for (NetworkEntityIdRelation<IPlatformData> iPlatformDataNetworkEntityIdRelation : from.getPlatforms()) {
            int requestedId = iPlatformDataNetworkEntityIdRelation.id;
            boolean found = false;
            for (Platform platform : dest.platforms) {
                if (platform.id() == requestedId)
                    found = true;
            }
            if (!found)
                missingIds.add(requestedId);
        }
        if (missingIds.size() == 0)
            return Observable.empty();
        return Observable.fromIterable(missingIds)
                .flatMap(platformRepository::load)
                .toList()
                .map(data -> {
                    dest.platforms.addAll(data);
                    return dest;
                })
                .toObservable();
    }

    /**
     * Loads the releases from the supplied source
     * If source has no releases, does not emit anything.
     *
     * @param from the source containing the platforms info
     * @param dest the entity in which to set the data
     * @return an Observable that emits the request with the releases on the source, loaded.
     */
    Observable<Game> loadReleases(IGameDatasource from, Game dest) {
        if (from.getReleases().isEmpty())
            return Observable.empty();

        // TODO: 15/02/2017 should improve by checking date and platforms
        if (from.getReleases().size() == dest.releases.size())
            return Observable.empty();

        return Observable.fromIterable(from.getReleases())
                .flatMap(new Function<IGameReleaseDateData, ObservableSource<Optional<GameRelease>>>() {
                    @Override
                    public ObservableSource<Optional<GameRelease>> apply(IGameReleaseDateData iGameReleaseDateData) throws Exception {
                        return platformRepository.load(iGameReleaseDateData.getPlatform().id)
                                .map(platform -> {
                                    Optional<ReleaseDate> releaseDate = dateMapper.transform(iGameReleaseDateData.getDate());
                                    if (!releaseDate.isPresent())
                                        return Optional.absent();
                                    return Optional.of(GameRelease.create(platform, releaseDate.get()));
                                });
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList()
                .map(data -> {
                    dest.releases = data;
                    return dest;
                }).toObservable();
    }
}
