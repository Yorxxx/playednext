package com.piticlistudio.playednext.game.model;

import com.piticlistudio.playednext.game.model.entity.datasource.NetGame;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedatasourceRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.NetGameRepositoryImpl;
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

    public interface NetService {
        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: XxTvUubZsDmshGVnDjpP4ZnVFfaLp1FLO7Vjsnzi8CSsAfuObi"
        })
        @GET("/games/")
        Observable<List<NetGame>> search(@Query("offset") int offset, @Query("search") String query, @Query("fields") String fields,
                                         @Query("limit") int limit);

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: XxTvUubZsDmshGVnDjpP4ZnVFfaLp1FLO7Vjsnzi8CSsAfuObi"
        })
        @GET("/games/{id}/")
        Observable<List<NetGame>> load(@Path("id") int id, @Query("fields") String fields);
    }

    @Provides
    public NetService service(Retrofit retrofit) {
        return retrofit.create(NetService.class);
    }

    @Provides
    @Named("net")
    public IGamedatasourceRepository provideNetRepository(NetService service) {
        return new NetGameRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public IGamedatasourceRepository provideDBRepository() {
        return new RealmGameRepositoryImpl();
    }
}
