package com.piticlistudio.playednext.platform.model.repository;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.repository.datasource.IPlatformRepositoryDatasource;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;

/**
 * Test cases
 * Created by jorge.garcia on 06/03/2017.
 */
public class PlatformRepositoryTest extends BaseTest {

    @Mock
    IPlatformRepositoryDatasource dbImpl;

    @Mock
    IPlatformRepositoryDatasource remoteImpl;

    @Mock
    PlatformMapper mapper;

    PlatformRepository repository;

    @Test
    public void given_dependencies_When_InstantiatesData_Then_ReturnsRepository() throws Exception {

        repository = new PlatformRepository(dbImpl, remoteImpl, mapper);

        assertNotNull(repository);
    }
}