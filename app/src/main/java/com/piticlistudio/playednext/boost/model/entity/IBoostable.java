package com.piticlistudio.playednext.boost.model.entity;

/**
 * An interface defining boostable items
 * Created by jorge.garcia on 08/03/2017.
 */

public interface IBoostable {

    /**
     * Returns if the current entity has the boost enabled
     *
     * @return true if is enabled. False otherwise
     */
    boolean isBoostEnabled();

    /**
     * Returns the last release
     *
     * @return the last release
     */
    long getLastRelease();

    /**
     * Returns the first release
     *
     * @return the first release
     */
    long getFirstRelease();

    /**
     * Returns since when is the item being on the todo list
     *
     * @return the timestamp
     */
    long getWaitingStartedAt();

    /**
     * Returns the number of times the item has been completed
     * @return the number of times
     */
    int getCompletedCount();
}
