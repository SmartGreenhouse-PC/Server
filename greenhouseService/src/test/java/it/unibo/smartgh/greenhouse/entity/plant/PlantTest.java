package it.unibo.smartgh.greenhouse.entity.plant;

import it.unibo.smartgh.plantValue.entity.PlantValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct creation of a {@link Plant} and its property.
 */
class PlantTest {

    private static Plant plant;
    private static String name;
    private static String description;
    private static String img;
    private static Map<ParameterType, Parameter> parameters;

    @BeforeEach
    void setUp() {
        name = "lemon";
        description = "is a species of small evergreen trees in the flowering plant family Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.\"";
        img = "http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg";
        parameters = new HashMap<>(){{
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

        plant = new PlantBuilder(name)
                .description(description)
                .image(img)
                .parameters(parameters)
                .build();
    }

    @Test
    void testGetName() {
        assertEquals(name, plant.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals(description, plant.getDescription());
    }

    @Test
    void testGetImg() {
        assertEquals(img, plant.getImg());
    }

    @Test
    void testGetParameters() {
        assertEquals(parameters, plant.getParameters());
    }
}