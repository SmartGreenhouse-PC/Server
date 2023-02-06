package it.unibo.smartgh.plantValue.controller;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct creation of a {@link PlantValueController} and its behaviour.
 */
class PlantValueControllerImplTest {

    private static final String PLANT_VALUE_TEST_DB_NAME = "plant_value";
    private static final String COLLECTION_NAME = "plantValues";
    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fa";

    private static PlantValueController controller;
    private static PlantValue plantValue;

    @BeforeAll
    static void setUp() {
        Date date = new Date();
        Double value = 100.0;
        plantValue = new PlantValueImpl(GREENHOUSE_ID, date, value);
        String mongodbHost = "localhost";
        int mongodbPort = 27017;
        PlantValueDatabase database = new PlantValueDatabaseImpl(PLANT_VALUE_TEST_DB_NAME, COLLECTION_NAME, mongodbHost, mongodbPort);
        controller = new PlantValueControllerImpl(database);
        controller.insertPlantValue(plantValue);
    }

    @Test
    void getCurrentValue() {
        try {
            assertEquals(plantValue, controller.getCurrentValue(GREENHOUSE_ID));
        } catch (EmptyDatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getHistoryData() {
        int limit = 1;
        assertTrue(controller.getHistoryData(GREENHOUSE_ID, limit).contains(plantValue));
    }
}