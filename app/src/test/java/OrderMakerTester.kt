package com.kleshchin.danil.ordermaker;

import org.junit.Assert
import org.junit.Test

/**
 * Created by Danil Kleshchin on 21-May-18.
 */
class OrderMakerTester {
    @Test
    public fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        Assert.assertEquals(4, OrderMakerRepository.loadMeal(4))
    }
}
