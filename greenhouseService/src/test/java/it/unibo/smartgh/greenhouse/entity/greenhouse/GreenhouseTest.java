package it.unibo.smartgh.greenhouse.entity.greenhouse;

import it.unibo.smartgh.greenhouse.entity.plant.*;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct creation of a {@link Greenhouse} and its property.
 */
class GreenhouseTest {

    private static Greenhouse greenhouse;
    private static String id;
    private static Plant plant;

    @BeforeAll
    static void setUp() {
        id = "1";
        Map<ParameterType, Parameter> parameters = new HashMap<>(){{
            put(ParameterType.TEMPERATURE, new ParameterBuilder("temperature")
                    .min(8.0)
                    .max(35.0)
                    .unit("\u2103")
                    .build());
            put(ParameterType.BRIGHTNESS, new ParameterBuilder("brightness")
                    .min(4200.0)
                    .max(130000.0)
                    .unit("Lux")
                    .build());
            put(ParameterType.SOIL_MOISTURE, new ParameterBuilder("soilMoisture")
                    .min(20.0)
                    .max(65.0)
                    .unit("%")
                    .build());
            put(ParameterType.HUMIDITY, new ParameterBuilder("humidity")
                    .min(30.0)
                    .max(80.0)
                    .unit("%")
                    .build());
        }};

        plant = new PlantBuilder("lemon")
                .description("is a species of small evergreen trees in the flowering plant family " +
                        "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
                .image("http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg")
                .parameters(parameters)
                .build();
        greenhouse = new GreenhouseImpl(id, plant, Modality.AUTOMATIC);
    }

    @Test
    void testGetId() {
        assertEquals(id, greenhouse.getId());
    }

    @Test
    void testGetPlant() {
        assertEquals(plant, greenhouse.getPlant());
    }

    @Test
    void testGetActualModality() {
        assertEquals(Modality.AUTOMATIC, greenhouse.getActualModality());
    }
}