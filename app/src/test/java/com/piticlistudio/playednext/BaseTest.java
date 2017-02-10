package com.piticlistudio.playednext;


import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Base test entity.
 * Automatically adds common requirements, like Mockito
 * Created by jorge.garcia on 15/12/2016.
 */

public class BaseTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    protected static <T> Predicate<T> check(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return true;
        };
    }
}
