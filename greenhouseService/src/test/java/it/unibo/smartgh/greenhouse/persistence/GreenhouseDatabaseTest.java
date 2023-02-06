package it.unibo.smartgh.greenhouse.persistence;

import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct behaviour of the operations on the database.
 */
public class GreenhouseDatabaseTest {
    private static final GreenhouseDatabase greenhouseDatabase = new GreenhouseDatabaseImpl();
    private static final String ID = "63af0ae025d55e9840cbc1fa";
    private static final String ID_AUTOMATIC =  "63b29b0a3792e15bae3229a7";

    @Test
    public void testGetGreenhouse() {
        Greenhouse res = greenhouseDatabase.getGreenhouse(ID_AUTOMATIC);
        Map<ParameterType, Parameter> parameters = new HashMap<>(){{
            put(ParameterType.TEMPERATURE, new ParameterBuilder("temperature")
                    .min(8.0)
                    .max(35.0)
                    .unit("° C")
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
        Plant plant = new PlantBuilder("lemon AUTOMATIC")
                .description("is a species of small evergreen trees in the flowering plant family " +
                        "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
                .image("http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg")
                .parameters(parameters)
                .build();
        Greenhouse greenhouse = new GreenhouseImpl(ID_AUTOMATIC, plant, Modality.AUTOMATIC);
        assertEquals(greenhouse.getPlant().getName(), res.getPlant().getName());
        assertEquals(greenhouse.getActualModality(), res.getActualModality());
    }

    @Test
    public void testPutActualModality(){
        greenhouseDatabase.putActualModality(ID, Modality.MANUAL);
        Greenhouse res = greenhouseDatabase.getGreenhouse(ID);
        assertEquals(Modality.MANUAL, res.getActualModality());

        greenhouseDatabase.putActualModality(ID, Modality.AUTOMATIC);
        res = greenhouseDatabase.getGreenhouse(ID);
        assertEquals(Modality.AUTOMATIC, res.getActualModality());
    }

}
