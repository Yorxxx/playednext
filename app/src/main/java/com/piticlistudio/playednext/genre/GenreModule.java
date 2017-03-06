package com.piticlistudio.playednext.genre;

import com.piticlistudio.playednext.APIKeys;
import com.piticlistudio.playednext.genre.model.entity.GenreMapper;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGDBGenre;
import com.piticlistudio.playednext.genre.model.repository.GenreRepository;
import com.piticlistudio.playednext.genre.model.repository.IGenreRepository;
import com.piticlistudio.playednext.genre.model.repository.datasource.IGDBGenreRepositoryImpl;
import com.piticlistudio.playednext.genre.model.repository.datasource.IGenreRepositoryDatasource;
import com.piticlistudio.playednext.genre.model.repository.datasource.RealmGenreRepositoryImpl;

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
public class GenreModule {

    @Provides
    public NetService service(Retrofit retrofit) {
        return retrofit.create(NetService.class);
    }

    @Provides
    @Named("net")
    public IGenreRepositoryDatasource provideNetRepository(NetService service) {
        return new IGDBGenreRepositoryImpl(service);
    }

    @Provides
    @Named("db")
    public IGenreRepositoryDatasource provideDBRepository() {
        return new RealmGenreRepositoryImpl();
    }

    @Provides
    public IGenreRepository provideRepository(@Named("db") IGenreRepositoryDatasource local,
                                              @Named("net") IGenreRepositoryDatasource remote,
                                              GenreMapper mapper) {
        return new GenreRepository(local, remote, mapper);
    }

    public interface NetService {

        @Headers({
                "Accept: application/json",
                "X-Mashape-Key: " + APIKeys.IGDB_KEY
        })
        @GET("/genres/{id}/")
        Observable<List<IGDBGenre>> load(@Path("id") int id, @Query("fields") String fields);
    }
}
