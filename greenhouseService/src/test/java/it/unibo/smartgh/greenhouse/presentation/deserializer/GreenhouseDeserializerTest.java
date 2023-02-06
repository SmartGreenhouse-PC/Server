package it.unibo.smartgh.greenhouse.presentation.deserializer;

import com.google.gson.Gson;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.*;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct deserialization of a {@link Greenhouse} and its property.
 */
class GreenhouseDeserializerTest {

    private Greenhouse greenhouse;
    private Gson gson;

    @BeforeEach
    void setUp() {
        String id = "1";
        String name = "lemon";
        String description = "is a species of small evergreen trees in the flowering plant family Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.\"";
        String img = "http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg";
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

        Plant plant = new PlantBuilder(name)
                .description(description)
                .image(img)
                .parameters(parameters)
                .build();
        this.greenhouse = new GreenhouseImpl(id, plant, Modality.AUTOMATIC);
        this.gson = GsonUtils.createGson();
    }

    @Test
    void testDeserialization() {
        Greenhouse deserialized = this.gson.fromJson(this.gson.toJson(this.greenhouse), GreenhouseImpl.class);
        assertEquals(this.greenhouse.getId(), deserialized.getId());
        assertEquals(this.greenhouse.getPlant(), deserialized.getPlant());
        assertEquals(this.greenhouse.getActualModality(), deserialized.getActualModality());
    }
}