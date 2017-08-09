package com.piticlistudio.playednext.game;

import com.piticlistudio.playednext.APIKeys;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.RealmCollectionMapper;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.company.model.entity.CompanyMapper;
import com.piticlistudio.playednext.company.model.entity.RealmCompanyMapper;
import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.IGDBGame;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.GamedataRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.IGDBGameRepositoryImpl;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedatasourceRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.RealmGameRepositoryImpl;
import com.piticlistudio.playednext.gamerelease.model.entity.GameReleaseMapper;
import com.piticlistudio.playednext.gamerelease.model.entity.RealmGameReleaseMapper;
import com.piticlistudio.playednext.genre.model.entity.GenreMapper;
import com.piticlistudio.playednext.genre.model.entity.RealmGenreMapper;
import com.piticlistudio.playednext.genre.model.repository.IGenreRepository;
import com.piticlistudio.playednext.image.model.entity.ImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.RealmImageDataMapper;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.RealmPlatformMapper;
import com.piticlistudio.playednext.platform.model.repository.IPlatformRepository;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDateMapper;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A module that provides Game classes
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class GameModule {

    @Provides
    public NetService service(Retrofit retrofit) {
        return retrofit.create(NetService.class);
    }

    @Provides
    @Named("net")
    public IGamedatasourceRepository provideNetRepository(NetService service) {
        return new IGDBGameRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public IGamedatasourceRepository provideDBRepository() {
        return new RealmGameRepositoryImpl();
    }

    @Provides
    public IGamedataRepository provideDataRepository(@Named("db") IGamedatasourceRepository dbImpl,
                                                     @Named("net") IGamedatasourceRepository netImpl) {
        return new GamedataRepository(dbImpl, netImpl);
    }

    @Provides
    public GameMapper mapper(CollectionMapper cm, ImageDataMapper im, CompanyMapper com, GenreMapper gm, GameReleaseMapper grm,
                             PlatformMapper pm) {
        return new GameMapper(cm, im, com, gm, grm, pm);
    }

    @Provides
    public RealmGameMapper realmMapper(RealmCollectionMapper cm, RealmImageDataMapper im, RealmCompanyMapper com, RealmGenreMapper gm,
                                       RealmGameReleaseMapper grm, RealmPlatformMapper pm) {
        return new RealmGameMapper(im, cm, com, gm, grm, pm);
    }

    @Provides
    public IGameRepository provideRepository(IGamedataRepository iGamedataRepository, GameMapper gm, RealmGameMapper rm,
                                             ICollectionRepository cr, ICompanyRepository comr, IGenreRepository gr, IPlatformRepository
                                                     pr, ReleaseDateMapper rdm) {
        return new GameRepository(iGamedataRepository, gm, rm, cr, comr, gr, pr, rdm);
    }

    public interface NetService {
        @Headers({
                "Accept: application/json",
                "user-key: " + APIKeys.IGDB_KEY
        })
        @GET("/games/")
        Observable<List<IGDBGame>> search(@Query("offset") int offset, @Query("search") String query, @Query("fields") String fields,
                                          @Query("limit") int limit);

        @Headers({
                "Accept: application/json",
                "user-key: " + APIKeys.IGDB_KEY
        })
        @GET("/games/{id}/")
        Observable<List<IGDBGame>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
