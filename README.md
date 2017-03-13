# Played. NEXT!!

This repository contains the sourcecode of a todo Android app regarding videogames. Allows to determine your backlog games, which of those are you playing, completed or waiting to be played

The main purpose of the app is to enhance my programming skills, mostly Android ones. So I implemented the following features
1. MVP architecture
2. Dependency Injection with Dagger 2
3. RxJava2
4. Some libraries I wanted to try, like AirBnB Epoxy's Adapter.

### MVP
There are a lot of tutorials out there introducing MVP, so I don't think is needed to explain the implementation. 
In case of doubt, I followed the patterns provided by the great [Hannes Dorfmann](https://twitter.com/sockeqwe), mainly is implementation for [Mosby](http://hannesdorfmann.com/android/mosby) an Android MVP library.
For a briefing, every view has a detailed contract, specifying the interfaces for the View, Presenter and Interactor of that module.

```java

public interface GameDetailContract {

    interface View extends MvpView {

        /**
         * Shows the loading status.
         */
        void showLoading();

        /**
         * Shows the main content of the view
         */
        void showContent();

        /**
         * Sets the data to show.
         *
         * @param data the data to show
         */
        void setData(Game data);

        /**
         * Shows an error
         *
         * @param error the error to show.
         */
        void showError(Throwable error);

        /**
         * Loads the data for the specified identifier
         *
         * @param gameId the id of the game to load.
         */
        void loadData(int gameId);
    }

    interface Presenter<V extends MvpView> extends IMvpPresenter<V> {

        /**
         * Loads the data
         *
         * @param id the id to load
         */
        void loadData(int id);
    }

    interface Interactor {

        /**
         * Loads the game with the specified id.
         *
         * @param id the id to load
         * @return an Observable that emits the loaded data
         */
        Observable<Game> load(int id);
    }
}
```

### Dagger2
Dependency injection is provided by (Dagger)[https://github.com/google/dagger]
I'm still missing the concept of scopes, so it's yet to be perfect or implemented. Feel free to implement it on your own.
For testing, I wanted to try (DaggerMock)[https://github.com/fabioCollini/DaggerMock]. It's a JUnit rule to override Dagger 2 objects.

### RxJava2
I implemented the next version of RxJava in this sample, just to give it a try. I already worked with RxJava1, but I wanted to try the new features, 
One thing to note is that, since RxJava2 does not allowing emitting null values, I also added to the proyect *Optionals*, like those provided by (Fernando Cejas)[https://fernandocejas.com/2016/02/20/how-to-use-optional-on-android-and-java/]
Because, well that could introduce me to Kotlin :)
