package com.piticlistudio.playednext.company.model.entity.datasource;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.image.model.entity.datasource.NetImageData;
import com.piticlistudio.playednext.utils.AutoGson;

import java.util.List;

/**
 * Entity defining a company representation by net provider
 * Created by jorge.garcia on 13/02/2017.
 */
@AutoValue
@AutoGson
public abstract class NetCompany implements ICompanyData {

    public int country;
    public List<Integer> published;
    public List<Integer> developed;
    public NetImageData logo;
    public String description;
    public String website;

    public static NetCompany create(int id, String name, String url, String slug, long createdAt, long updatedAt) {
        return new AutoValue_NetCompany(id, name, url, slug, createdAt, updatedAt);
    }

    public abstract int id();

    public abstract String name();

    public abstract String url();

    public abstract String slug();

    public abstract long created_at();

    public abstract long updated_at();

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public int getId() {
        return id();
    }

    /**
     * Returns the name
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name();
    }
}
