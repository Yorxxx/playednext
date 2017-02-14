package com.piticlistudio.playednext.company.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmCompanyMapperTest extends BaseTest {

    @InjectMocks
    RealmCompanyMapper mapper;

    @Test
    public void transform() throws Exception {

        Company data = null;

        // Act
        Optional<RealmCompany> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data = Company.create(50, "name");

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.id(), result.get().getId());
        assertEquals(data.name(), result.get().getName());
    }

}