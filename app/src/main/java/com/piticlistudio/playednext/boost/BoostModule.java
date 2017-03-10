package com.piticlistudio.playednext.boost;

import com.piticlistudio.playednext.boost.model.repository.BoostCalculatorRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Module for boosts
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class BoostModule {

    @Provides
    BoostCalculatorRepository calculatorRepository() {
        return new BoostCalculatorRepository();
    }
}
