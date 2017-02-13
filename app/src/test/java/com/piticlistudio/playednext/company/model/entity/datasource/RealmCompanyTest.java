package com.piticlistudio.playednext.company.model.entity.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for RealmCompany
 * Created by jorge.garcia on 13/02/2017.
 */
public class RealmCompanyTest {

    RealmCompany data = new RealmCompany(50, "name");

    @Test
    public void getId() throws Exception {
        assertEquals(50, data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name", data.getName());
    }

}