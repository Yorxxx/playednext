package com.piticlistudio.playednext;

import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.game.model.entity.datasource.NetGame;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Factory for game entities
 * Created by jorge.garcia on 10/02/2017.
 */

public class GameFactory {

//    /**
//     * Provides a Game entity.
//     *
//     * @param id    the id of the game
//     * @param title the title of the game
//     * @return a game with property values
//     * @see Game
//     */
//    public static Game provide(int id, String title) {
//        Game data = Game.create(id, title);
//        data.setStoryline("storyline");
//        data.setSummary("summary");
//        data.hypes = 179;
//        data.rating = 98.3154255976391;
//
//        ImageData cover = ImageData.create("cover", 100, 75, "coverUrl");
//        data.cover = Optional.of(cover);
//
//        GameCollection collection = GameCollection.create(100, "collection1");
//        data.collection = Optional.of(collection);
//
//        Company developer1 = Company.create(10, "developer1");
//        Company developer2 = Company.create(20, "developer2");
//        List<Company> developers = new ArrayList<>();
//        developers.add(developer1);
//        developers.add(developer2);
//        data.developers = developers;
//
//        Company publisher1 = Company.create(45, "publisher1");
//        List<Company> publishers = new ArrayList<>();
//        publishers.add(publisher1);
//        data.publishers = publishers;
//
//        Genre genre1 = Genre.create(1, "genre1");
//        Genre genre2 = Genre.create(2, "genre2");
//        Genre genre3 = Genre.create(3, "genre3");
//        List<Genre> genres = new ArrayList<>();
//        genres.add(genre1);
//        genres.add(genre2);
//        genres.add(genre3);
//        data.genres = genres;
//
//        ImageData screen1 = ImageData.create("screenshot1", 101, 78, "screenshot1Url");
//        ImageData screen2 = ImageData.create("screenshot2", 201, 78, "screenshot2Url");
//        ImageData screen3 = ImageData.create("screenshot3", 301, 78, "screenshot3Url");
//        ImageData screen4 = ImageData.create("screenshot4", 401, 78, "screenshot4Url");
//        ImageData screen5 = ImageData.create("screenshot5", 501, 78, "screenshot5Url");
//        List<ImageData> screenshots = new ArrayList<>();
//        screenshots.add(screen1);
//        screenshots.add(screen2);
//        screenshots.add(screen3);
//        screenshots.add(screen4);
//        screenshots.add(screen5);
//        data.screenshots = screenshots;
//
//        Platform platform1 = Platform.create(1, "platform1");
//        Platform platform2 = Platform.create(2, "platform2");
//        List<Platform> platforms = new ArrayList<>();
//        platforms.add(platform1);
//        platforms.add(platform2);
//        data.platforms = platforms;
//
//        GameRelease release1 = GameRelease.create(PlatformFactory.provide(10, "platform10"), ReleaseDateFactory.provide(8));
//        GameRelease release2 = GameRelease.create(PlatformFactory.provide(3, "platform3"), ReleaseDateFactory.provide(8));
//        GameRelease release3 = GameRelease.create(PlatformFactory.provide(3, "platform3"), ReleaseDateFactory.provide(5));
//        List<GameRelease> releases = new ArrayList<>();
//        releases.add(release1);
//        releases.add(release2);
//        releases.add(release3);
//        data.releases = releases;
//
//        return data;
//    }

    /**
     * Provides a RealmGame entity
     *
     * @param id the id of the game.
     * @return the game provided
     * @see RealmGame
     */
    public static RealmGame provideRealmGame(int id, String title) {
        RealmGame data = new RealmGame();
        data.setId(id);
        data.setName(title);
        data.setStoryline("storyline");
        data.setSummary("summary");
        data.setCollection(new RealmCollection(50, "collection"));
        return data;
    }

    /**
     * Provides a NEt game
     * @param id the id of the game
     * @param title the title
     * @return the NetGame
     */
    public static NetGame provideNetGame(int id, String title) {

        NetGame response = NetGame.create(id, title, "slug", "url", 1000, 2500);
//        response.cover = IGDBImageData.create("url", 800, 500, "imageid");
        response.category = 100;
        response.collection = 200;
        response.hypes = 179;
        Integer[] devs = {10, 11, 12};
        response.developers = new ArrayList<>(Arrays.asList(devs));
        Integer[] genres = {20, 21, 22};
        response.genres = new ArrayList<>(Arrays.asList(genres));
        response.popularity = 2.0d;
        Integer[] publishers = {20, 21, 22};
        response.publishers = new ArrayList<>(Arrays.asList(publishers));
//        response.screenshots = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            response.screenshots.add(IGDBImageData.create("url", 800, 500, "i"));
//        }
        response.rating = 88;
        response.rating_count = 99;
//        response.release_dates = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            IGDBReleaseDate date = IGDBReleaseDate.create(i*10000, i+1, "humandate_"+i);
//            IGDBGameRelease release = IGDBGameRelease.create(i, i, date);
//            response.release_dates.add(release);
//        }

        response.summary = "ABC";
        response.storyline = "storyline";
//        response.time_to_beat = new HashMap<>();
//        for (int i = 0; i < 5; i++) {
//            response.time_to_beat.put(String.valueOf(i), (long)i);
//        }
        return response;
    }
}
