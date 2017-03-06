package com.piticlistudio.playednext.game.model;

import com.piticlistudio.playednext.APIKeys;
import com.piticlistudio.playednext.game.model.entity.datasource.IGDBGame;
import com.piticlistudio.playednext.game.model.repository.datasource.GamedataRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.IGDBGameRepositoryImpl;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedatasourceRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.RealmGameRepositoryImpl;

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
 * A module that provides GameData classes
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class GamedataModule {

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
    public IGamedataRepository provideRepository(@Named("db") IGamedatasourceRepository dbImpl,
                                                 @Named("net") IGamedatasourceRepository netImpl) {
        return new GamedataRepository(dbImpl, netImpl);
    }

    public interface NetService {
        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: " + APIKeys.IGDB_KEY
        })
        @GET("/games/")
        Observable<List<IGDBGame>> search(@Query("offset") int offset, @Query("search") String query, @Query("fields") String fields,
                                          @Query("limit") int limit);

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: " + APIKeys.IGDB_KEY
        })
        @GET("/games/{id}/")
        Observable<List<IGDBGame>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
