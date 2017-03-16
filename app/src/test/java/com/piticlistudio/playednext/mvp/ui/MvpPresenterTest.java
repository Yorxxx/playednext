package com.piticlistudio.playednext.mvp.ui;

import com.piticlistudio.playednext.BaseTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Test cases for MVPPresenter
 * Created by jorge.garcia on 14/03/2017.
 */
public class MvpPresenterTest extends BaseTest {

    private TestView view = new TestView();

    private MvpPresenter<TestView> presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new MvpPresenter<>();
    }

    @Test
    public void Given_nothing_When_Attach_Then_CreatesAWeakReference() throws Exception {

        // Act
        presenter.attachView(view);

        // Assert
        assertNotNull(presenter.view);
        assertEquals(view, presenter.view.get());
    }

    @Test
    public void Given_AttachedView_When_Detach_Then_ClearsReference() throws Exception {

        // Arrange
        presenter.attachView(view);

        // Act
        presenter.detachView(false);

        // Assert
        assertNull(presenter.view);
    }

    @Test
    public void Given_ViewNotAttached_When_Detach_Then_DoesNothing() throws Exception {

        // Act
        presenter.detachView(false);

        // Assert
        assertNull(presenter.view);
    }

    @Test
    public void Given_AttachedView_When_getView_Then_ReturnsView() throws Exception {

        // Arrange
        presenter.attachView(view);

        // Act
        MvpView result = presenter.getView();

        // Assert
        assertNotNull(result);
        assertEquals(view, result);
    }

    @Test
    public void Given_NotAttachedView_When_getView_Then_ReturnsNull() throws Exception {

        // Act
        MvpView result = presenter.getView();

        // Assert
        assertNull(result);
    }

    private class TestView implements MvpView {

    }
}