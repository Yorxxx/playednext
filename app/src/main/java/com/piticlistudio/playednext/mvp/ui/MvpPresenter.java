package com.piticlistudio.playednext.mvp.ui;


import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Base presenter
 * Created by jorge.garcia on 13/12/2016.
 */

public class MvpPresenter<V extends MvpView> implements IMvpPresenter<V> {

    private WeakReference<V> view;

    /**
     * Attachs the view
     *
     * @param view the view to attach
     */
    @Override
    public void attachView(V view) {
        this.view = new WeakReference<V>(view);
    }

    /**
     * Detachs the view
     *
     * @param retainInstance determines if is retaining state
     */
    @Override
    public void detachView(boolean retainInstance) {
        if (view != null) {
            view.clear();
            view = null;
        }
    }

    @Nullable
    public V getView() {
        if (view != null) {
            return view.get();
        }
        return null;
    }

    /**
     * Returns if view is available.
     * @return true if is not null and attached. False otherwise
     */
    public boolean isViewAvailable() {
        return getView() != null;
    }
}
