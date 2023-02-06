package it.unibo.smartgh.operation.presentation.deserializer;

import com.google.gson.Gson;
import it.unibo.smartgh.operation.entity.Modality;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;
import it.unibo.smartgh.operation.presentation.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct deserialization of a {@link Operation} and its property.
 */
class OperationDeserializerTest {

    private Operation operation;
    private Gson gson;

    @BeforeEach
    void setUp() {
        this.operation = new OperationImpl("63af0ae025d55e9840cbc1fa", Modality.AUTOMATIC, new Date(), "brightness", "increase");
        this.gson = GsonUtils.createGson();
    }

    @Test
    void testDeserialization() {
        Operation deserialized = this.gson.fromJson(this.gson.toJson(this.operation), OperationImpl.class);
        assertEquals(this.operation.getGreenhouseId(), deserialized.getGreenhouseId());
        assertEquals(this.operation.getModality(), deserialized.getModality());
        assertEquals(this.operation.getDate().toString(), deserialized.getDate().toString());
        assertEquals(this.operation.getParameter(), deserialized.getParameter());
        assertEquals(this.operation.getAction(), deserialized.getAction());
    }
}