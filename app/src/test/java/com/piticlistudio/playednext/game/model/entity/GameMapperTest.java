package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.BaseGameTest;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.GamedataModule;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;

import org.junit.Rule;
import org.junit.Test;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

import static org.junit.Assert.*;

/**
 * Mapper
 * Created by jorge.garcia on 10/02/2017.
 */
public class GameMapperTest extends BaseGameTest {

    @Rule
    public DaggerMockRule<GamedataComponent> rule = new DaggerMockRule<>(GamedataComponent.class, new GamedataModule());

    @InjectFromComponent
    private GameMapper mapper;

    @Test
    public void transform() throws Exception {

        RealmGame data = null;

        Optional<Game> result = mapper.transform(data);
        assertFalse(result.isPresent());

        // Arrange
        data = new RealmGame();

        // Act
        result = mapper.transform(data);

        // Assert
        assertFalse(result.isPresent());

        // Arrange
        data = GameFactory.provideRealmGame(0, "title");

        // Act
        result = mapper.transform(data);

        // Assert
        assertTrue(result.isPresent());
        assertTrue("Should match game properties", equalsGame(result.get(), data));
    }

}