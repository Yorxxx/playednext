package com.piticlistudio.playednext.mvp.ui;

import com.piticlistudio.playednext.mvp.ui.MvpView;

/**
 * Root interface for mvp presenters
 * Created by jorge.garcia on 15/02/2017.
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * Attachs the view
     *
     * @param view the view to attach
     */
    void attachView(V view);

    /**
     * Detachs the view
     *
     * @param retainInstance determines if is retaining state
     */
    void detachView(boolean retainInstance);
}
