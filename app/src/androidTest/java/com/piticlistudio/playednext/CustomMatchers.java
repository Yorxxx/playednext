package com.piticlistudio.playednext;

import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class CustomMatchers {

    public static Matcher<View> withHint(final String expectedHint) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                String hint = ((EditText) view).getHint().toString();

                return expectedHint.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public static Matcher<View> withAlpha(final float expectedAlpha) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                float alpha = view.getAlpha();

                return alpha == expectedAlpha;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public static Matcher<View> withError(CharSequence error) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return error.equals(editText.getError());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with error: " + error);
            }
        };
    }
}
