package com.piticlistudio.playednext.company.model.entity.datasource;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Entity that defines a company (ie a publisher or developer or manufacturer)
 * Created by jorge.garcia on 12/01/2017.
 */
public class RealmCompany extends RealmObject implements ICompanyData {

    @PrimaryKey
    private int id;

    @Required
    private String name;

    public RealmCompany() {
        // Empty
    }

    public RealmCompany(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
