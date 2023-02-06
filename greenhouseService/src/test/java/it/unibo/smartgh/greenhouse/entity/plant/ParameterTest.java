package it.unibo.smartgh.greenhouse.entity.plant;

import it.unibo.smartgh.plantValue.entity.PlantValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct creation of a {@link Parameter} and its property.
 */
class ParameterTest {

    private static Parameter parameter;
    private static String name;
    private static Double min;
    private static Double max;

    private static String unit;

    @BeforeAll
    static void setUp() {
        name = "parameter test";
        min = 10.0;
        max = 100.0;
        unit = "mt";
        parameter = new ParameterBuilder(name).min(min).max(max).unit(unit).build();
    }

    @Test
    void testGetName() {
        assertEquals(name, parameter.getName());
    }

    @Test
    void testGetMin() {
        assertEquals(min, parameter.getMin());
    }

    @Test
    void testGetMax() {
        assertEquals(max, parameter.getMax());
    }

    @Test
    void testGetUnit() {
        assertEquals(unit, parameter.getUnit());
    }
}