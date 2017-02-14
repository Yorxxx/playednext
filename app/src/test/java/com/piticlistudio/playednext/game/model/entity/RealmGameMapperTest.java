package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
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
 * Test cases
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmGameMapperTest extends BaseGameTest {

    @Rule
    public DaggerMockRule<GamedataComponent> rule = new DaggerMockRule<>(GamedataComponent.class, new GamedataModule());

    @InjectFromComponent
    private RealmGameMapper mapper;

    @Test
    public void transform() throws Exception {
        Game game = null;

        // Act
        Optional<RealmGame> result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

        game = GameFactory.provide(50, "title");

        // Act
        result = mapper.transform(game);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(equalsGame(game, result.get()));
    }

}