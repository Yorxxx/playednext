package com.piticlistudio.playednext.image.model.entity.datasource;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmImageData extends RealmObject implements IImageData {

    @PrimaryKey
    private String id;

    @Required
    private String thumbUrl;

    private int width;

    private int height;

    public RealmImageData() {
        // Empty
    }

    public RealmImageData(String id, String thumbUrl, int width, int height) {
        this.id = id;
        this.thumbUrl = thumbUrl;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the id
     *
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the width
     *
     * @return the width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height
     *
     * @return the height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Returns the url
     *
     * @return the url
     */
    @Override
    public String getUrl() {
        return thumbUrl;
    }
}
