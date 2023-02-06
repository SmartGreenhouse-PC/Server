package it.unibo.smartgh.plantValue.entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct creation of a {@link PlantValue} and its property.
 */
class PlantValueTest {

    private static String greenhouseId;
    private static Date date;
    private static double value;
    private static PlantValue plantValue;

    @BeforeAll
    static void setUp() {
        greenhouseId = "63af0ae025d55e9840cbc1fa";
        date = new Date();
        value = 100.0;
        plantValue = new PlantValueImpl(greenhouseId, date, value);
    }

    @Test
    void testGetGreenhouseId() {
        assertEquals(greenhouseId, plantValue.getGreenhouseId());
    }

    @Test
    void testGetDate() {
        assertEquals(date, plantValue.getDate());
    }

    @Test
    void testGetValue() {
        assertEquals(value, plantValue.getValue());
    }

}