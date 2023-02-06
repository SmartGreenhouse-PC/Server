package it.unibo.smartgh.operation.presentation.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unibo.smartgh.operation.entity.Modality;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;
import it.unibo.smartgh.operation.presentation.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct serialization of a {@link Operation} and its property.
 */
class OperationSerializerTest {

    private Operation operation;
    private Gson gson;

    @BeforeEach
    void setUp() {
        this.operation = new OperationImpl("63af0ae025d55e9840cbc1fa", Modality.AUTOMATIC, new Date(), "brightness", "increase");
        this.gson = GsonUtils.createGson();
    }

    @Test
    void testSerialize() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        JsonObject json = new JsonObject();
        json.addProperty("greenhouseId", this.operation.getGreenhouseId());
        json.addProperty("modality", this.operation.getModality().modality());
        json.addProperty("date", formatter.format(this.operation.getDate()));
        json.addProperty("parameter", this.operation.getParameter());
        json.addProperty("action", this.operation.getAction());

        assertEquals(json.toString(), this.gson.toJson(this.operation));
    }
}