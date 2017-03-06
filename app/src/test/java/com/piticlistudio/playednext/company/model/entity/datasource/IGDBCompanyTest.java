package com.piticlistudio.playednext.company.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 13/02/2017.
 */
public class IGDBCompanyTest {

    private IGDBCompany data = IGDBCompany.create(50, "company_name", "company_url", "company_slug", 1000, 2000);

    @Test
    public void create() throws Exception {
        assertNotNull(data);
        assertEquals(50, data.id());
        assertEquals("company_name", data.name());
        assertEquals("company_url", data.url());
        assertEquals("company_slug", data.slug());
        assertEquals(1000, data.created_at());
        assertEquals(2000, data.updated_at());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(50, data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("company_name", data.getName());
    }
}