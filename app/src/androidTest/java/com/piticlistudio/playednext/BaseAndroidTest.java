package com.piticlistudio.playednext;

import android.support.test.InstrumentationRegistry;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Base Android Junit tests
 * Created by jorge.garcia on 16/01/2017.
 */
public class BaseAndroidTest {

    protected static <T> Predicate<T> check(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return true;
        };
    }

    protected AndroidApplication getApp() {
        return (AndroidApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }
}
