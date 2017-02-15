package com.piticlistudio.playednext.platform;

import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.repository.datasource.IGenreRepositoryDatasource;
import com.piticlistudio.playednext.genre.model.repository.datasource.RealmGenreRepositoryImpl;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.NetPlatform;
import com.piticlistudio.playednext.platform.model.repository.datasource.IPlatformRepositoryDatasource;
import com.piticlistudio.playednext.platform.model.repository.datasource.RealmPlatformRepositoryImpl;

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
 * Module for collections
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class PlatformModule {

    @Provides
    public NetService service(Retrofit retrofit) {
        return retrofit.create(NetService.class);
    }

//    @Provides
//    @Named("net")
//    public IGenreRepositoryDatasource<IGenreData> provideNetRepository(NetService service) {
//        return new NetGenreRepositoryImpl(service);
//    }

    @Provides
    @Named("db")
    public IPlatformRepositoryDatasource<IPlatformData> provideDBRepository() {
        return new RealmPlatformRepositoryImpl();
    }

//    @Provides
//    public IGenreRepository provideRepository(@Named("db") IGenreRepositoryDatasource<IGenreData> local,
//                                              @Named("net") IGenreRepositoryDatasource<IGenreData> remote,
//                                              GenreMapper mapper) {
//        return new GenreRepository(local, remote, mapper);
//    }

    public interface NetService {

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: XxTvUubZsDmshGVnDjpP4ZnVFfaLp1FLO7Vjsnzi8CSsAfuObi"
        })
        @GET("/platforms/{id}/")
        Observable<List<NetPlatform>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
