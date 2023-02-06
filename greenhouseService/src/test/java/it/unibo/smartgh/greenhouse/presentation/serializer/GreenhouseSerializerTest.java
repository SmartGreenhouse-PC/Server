package it.unibo.smartgh.greenhouse.presentation.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.*;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct serialization of a {@link Greenhouse} and its property.
 */
class GreenhouseSerializerTest {

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
                    .unit("℃")
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

    private JsonObject serializeTemperatureParameter(){
        JsonObject temperatureParameter = new JsonObject();
        temperatureParameter.addProperty("name", "temperature");
        temperatureParameter.addProperty("min", 8.0);
        temperatureParameter.addProperty("max", 35.0);
        temperatureParameter.addProperty("unit", "\u2103");
        return temperatureParameter;
    }

    private JsonObject serializeBrightnessParameter(){
        JsonObject brightnessParameter = new JsonObject();
        brightnessParameter.addProperty("name", "brightness");
        brightnessParameter.addProperty("min", 4200.0);
        brightnessParameter.addProperty("max", 130000.0);
        brightnessParameter.addProperty("unit", "Lux");
        return brightnessParameter;
    }

    private JsonObject serializeHumidityParameter(){
        JsonObject humidityParameter = new JsonObject();
        humidityParameter.addProperty("name", "humidity");
        humidityParameter.addProperty("min", 30.0);
        humidityParameter.addProperty("max", 80.0);
        humidityParameter.addProperty("unit", "%");
        return humidityParameter;
    }

    private JsonObject serializeSoilMoistureParameter(){
        JsonObject soilMoistureParameter = new JsonObject();
        soilMoistureParameter.addProperty("name", "soilMoisture");
        soilMoistureParameter.addProperty("min", 20.0);
        soilMoistureParameter.addProperty("max", 65.0);
        soilMoistureParameter.addProperty("unit", "%");
        return soilMoistureParameter;
    }



    @Test
    void testSerialization() {
        JsonObject json = new JsonObject();
        JsonObject jsonPlant = new JsonObject();
        JsonArray array = new JsonArray();


        array.add(this.serializeTemperatureParameter());
        array.add(this.serializeSoilMoistureParameter());
        array.add(this.serializeBrightnessParameter());
        array.add(this.serializeHumidityParameter());
        jsonPlant.addProperty("name", this.greenhouse.getPlant().getName());
        jsonPlant.addProperty("description", this.greenhouse.getPlant().getDescription());
        jsonPlant.addProperty("img", this.greenhouse.getPlant().getImg());
        jsonPlant.add("parameters", array);

        json.add("plant", jsonPlant);
        json.addProperty("id", this.greenhouse.getId());
        json.addProperty("modality", this.greenhouse.getActualModality().toString());

        System.out.println(json);

        try {
            JSONAssert.assertEquals(json.toString(), this.gson.toJson(this.greenhouse), false);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}