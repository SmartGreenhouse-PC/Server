package it.unibo.smartgh.greenhouse.presentation.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unibo.smartgh.greenhouse.entity.plant.Parameter;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterBuilder;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct creation of a {@link Parameter} and its property.
 */
class ParameterSerializerTest {

    private Parameter parameter;
    private Gson gson;

    @BeforeEach
    void setUp() {
        this.parameter = new ParameterBuilder("brightness").min(0.0).max(1000.0).unit("lux").build();
        this.gson = GsonUtils.createGson();
    }

    @Test
    void testSerialization() {
        JsonObject json = new JsonObject();
        json.addProperty("name", this.parameter.getName());
        json.addProperty("min", this.parameter.getMin());
        json.addProperty("max", this.parameter.getMax());
        json.addProperty("unit", this.parameter.getUnit());

        assertEquals(json.toString(), this.gson.toJson(this.parameter));
    }
}