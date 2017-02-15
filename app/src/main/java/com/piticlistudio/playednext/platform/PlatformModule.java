package com.piticlistudio.playednext.platform;

import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.repository.datasource.IGenreRepositoryDatasource;
import com.piticlistudio.playednext.genre.model.repository.datasource.RealmGenreRepositoryImpl;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.NetPlatform;
import com.piticlistudio.playednext.platform.model.repository.IPlatformRepository;
import com.piticlistudio.playednext.platform.model.repository.PlatformRepository;
import com.piticlistudio.playednext.platform.model.repository.datasource.IGDBPlatformRepositoryImpl;
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

    @Provides
    @Named("net")
    public IPlatformRepositoryDatasource<IPlatformData> provideNetRepository(NetService service) {
        return new IGDBPlatformRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public IPlatformRepositoryDatasource<IPlatformData> provideDBRepository() {
        return new RealmPlatformRepositoryImpl();
    }

    @Provides
    public IPlatformRepository provideRepository(@Named("db") IPlatformRepositoryDatasource<IPlatformData> local,
                                                 @Named("net") IPlatformRepositoryDatasource<IPlatformData> remote,
                                                 PlatformMapper mapper) {
        return new PlatformRepository(local, remote, mapper);
    }

    public interface NetService {

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: XxTvUubZsDmshGVnDjpP4ZnVFfaLp1FLO7Vjsnzi8CSsAfuObi"
        })
        @GET("/platforms/{id}/")
        Observable<List<NetPlatform>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
