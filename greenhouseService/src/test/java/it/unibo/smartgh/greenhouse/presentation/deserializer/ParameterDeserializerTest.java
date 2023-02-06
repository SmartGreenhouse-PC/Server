package it.unibo.smartgh.greenhouse.presentation.deserializer;

import com.google.gson.Gson;
import it.unibo.smartgh.greenhouse.entity.plant.Parameter;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterBuilder;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterImpl;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct deserialization of a {@link Parameter} and its property.
 */
class ParameterDeserializerTest {

    private Parameter parameter;
    private Gson gson;

    @BeforeEach
    void setUp() {
        this.parameter = new ParameterBuilder("brightness").min(0.0).max(1000.0).unit("lux").build();
        this.gson = GsonUtils.createGson();
    }

    @Test
    void testDeserialization() {
        Parameter deserialized = this.gson.fromJson(this.gson.toJson(this.parameter), ParameterImpl.class);
        assertEquals(this.parameter.getName(), deserialized.getName());
        assertEquals(this.parameter.getMin(), deserialized.getMin());
        assertEquals(this.parameter.getMax(), deserialized.getMax());
        assertEquals(this.parameter.getUnit(), deserialized.getUnit());
    }
}