package it.unibo.smartgh.plantValue.presentation.deserializer;

import com.google.gson.Gson;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.presentation.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct deserialization of a {@link PlantValue} and its property.
 */
class PlantValueDeserializerTest {

    private PlantValue plantValue;
    private Gson gson;


    @BeforeEach
    void setUp() {
        this.plantValue = new PlantValueImpl("63af0ae025d55e9840cbc1fa", new Date(), 100.0);
        this.gson = GsonUtils.createGson();
    }

    @Test
    void testDeserialization() {
        PlantValue deserialized = this.gson.fromJson(this.gson.toJson(this.plantValue), PlantValueImpl.class);
        assertEquals(this.plantValue.getGreenhouseId(), deserialized.getGreenhouseId());
        assertEquals(this.plantValue.getDate().toString(), deserialized.getDate().toString());
        assertEquals(this.plantValue.getValue(), deserialized.getValue());
    }
}