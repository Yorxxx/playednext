package com.piticlistudio.playednext.genre.model.repository;

import com.piticlistudio.playednext.genre.model.entity.GenreMapper;
import com.piticlistudio.playednext.genre.model.repository.datasource.IGenreRepositoryDatasource;

import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertNotNull;

/**
 * Test cases for GenreRepository
 * Created by jorge.garcia on 06/03/2017.
 */
public class GenreRepositoryTest {

    @Mock
    IGenreRepositoryDatasource dbImpl;

    @Mock
    IGenreRepositoryDatasource remoteImpl;

    @Mock
    GenreMapper mapper;

    GenreRepository repository;

    @Test
    public void given_dependencies_When_instantiatesRepository_Then_ReturnsRepository() throws Exception {

        repository = new GenreRepository(dbImpl, remoteImpl, mapper);

        assertNotNull(repository);
    }
}