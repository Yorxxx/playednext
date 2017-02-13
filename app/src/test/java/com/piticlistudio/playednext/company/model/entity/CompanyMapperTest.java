package com.piticlistudio.playednext.company.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 * Created by jorge.garcia on 13/02/2017.
 */
public class CompanyMapperTest extends BaseTest {

    @InjectMocks
    CompanyMapper mapper;

    @Test
    public void transform() throws Exception {

        RealmCompany data = null;

        // Act
        Optional<Company> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data = new RealmCompany(50, null);

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        // Arrange
        data.setName("name");

        // Act
        result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(data.getId(), result.get().id());
        assertEquals(data.getName(), result.get().name());
    }

}