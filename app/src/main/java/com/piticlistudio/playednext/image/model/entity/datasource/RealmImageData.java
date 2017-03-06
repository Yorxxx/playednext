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

    public void setId(String id) {
        this.id = id;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
