package it.unibo.smartgh.plantValue.presentation.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.presentation.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct deserialization of a {@link PlantValue} and its property.
 */
class PlantValueSerializerTest {

    private PlantValue plantValue;
    private Gson gson;


    @BeforeEach
    void setUp() {
        this.plantValue = new PlantValueImpl("63af0ae025d55e9840cbc1fa", new Date(), 100.0);
        this.gson = GsonUtils.createGson();
    }

    @Test
    void testSerialization() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        JsonObject json = new JsonObject();
        json.addProperty("greenhouseId", this.plantValue.getGreenhouseId());
        json.addProperty("date", formatter.format(this.plantValue.getDate()));
        json.addProperty("value", this.plantValue.getValue());

        assertEquals(json.toString(), this.gson.toJson(this.plantValue));
    }
}