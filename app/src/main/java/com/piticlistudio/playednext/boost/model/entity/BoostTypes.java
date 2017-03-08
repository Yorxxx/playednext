package com.piticlistudio.playednext.boost.model.entity;

/**
 * Type definitions for every boost
 * Created by jorge.garcia on 08/03/2017.
 */
public class BoostTypes {

    /**
     * Boost of the item being just released
     */
    public static class RECENTLY_RELEASED {
        public final static int id = 1;
        public final static int value = 500;
    }

    /**
     * Boost of the item that is celebrating a 10 year old launch
     */
    public static class TEN_YEARS_CELEBRATION {
        public final static int id = 2;
        public final static int value = 2000;
    }

    /**
     * Boost of the item that is on the waiting list
     */
    public static class WAITING_TIME {
        public final static int id = 2;
        public static int value = 0; // To be filled by calculator
    }
}
