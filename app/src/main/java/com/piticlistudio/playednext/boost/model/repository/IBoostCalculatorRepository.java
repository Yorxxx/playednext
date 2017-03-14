package com.piticlistudio.playednext.boost.model.repository;

import com.piticlistudio.playednext.boost.model.entity.BoostItem;
import com.piticlistudio.playednext.boost.model.entity.IBoostable;

import java.util.List;

import io.reactivex.Observable;

/**
 * Definition of any class that wants to act as calculator for boost
 * Created by jorge.garcia on 08/03/2017.
 */

public interface IBoostCalculatorRepository {

    /**
     * Loads the boost items applicable for the specified item
     *
     * @param item the item to calculate its boost
     * @return an Observable that emits the list of boostItems
     */
    Observable<List<BoostItem>> load(IBoostable item);
}
