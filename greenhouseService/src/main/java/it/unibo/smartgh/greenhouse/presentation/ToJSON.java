package it.unibo.smartgh.greenhouse.presentation;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterType;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for converting objects to JsonObject.
 */
public class ToJSON {
    /**
     * Convert the greenhouse object into jsonObject.
     * @param gh the greenhouse object.
     * @return its JsonObject representation.
     */
    public static JsonObject greenhouseToJSON(Greenhouse gh){
        JsonObject greenhouse = new JsonObject();
        JsonObject plant = plantToJSON(gh.getPlant());
        greenhouse.put("plant", plant);
        greenhouse.put("modality", gh.getActualModality().name());
        return greenhouse;
    }

    /**
     * Convert the plant object into jsonObject.
     * @param p the plant object.
     * @return its JsonObject representation.
     */
    private static JsonObject plantToJSON(Plant p) {
        JsonObject plant = new JsonObject();
        JsonArray parameters = paramsToJsonObject(p);
        plant.put("name", p.getName());
        plant.put("description", p.getDescription());
        plant.put("parameters", parameters);
        return plant;
    }

    private static JsonArray paramsToJsonObject(Plant plant) {
        JsonArray jsonArray = new JsonArray();
        plant.getParameters().forEach((t,p) -> {
            jsonArray.add(new JsonObject()
                    .put("name", p.getName())
                    .put("min", p.getMin().toString())
                    .put("max", p.getMax().toString())
                    .put("unit", p.getUnit()));
        });
        return jsonArray;
    }

    /**
     * Convert the modality object into jsonObject.
     * @param mod the modality object.
     * @return its JsonObject representation.
     */
    public static JsonObject modalityToJSON(Modality mod){
        JsonObject modality = new JsonObject();
        modality.put("modality", mod.name());
        return modality;
    }

    /**
     * Get the minimum and maximum value of the parameter of a plant like JsonObject.
     * @param plant of the greenhouse.
     * @param param for which wants the optimal values.
     * @return the JsonObject containing the minimum and maximum value of the parameter.
     */
    public static JsonObject paramToJSON(Plant plant, String param){
        JsonObject parameter = new JsonObject();
        String original = param.toLowerCase();
        parameter.put("min", plant.getParameters().get(ParameterType.parameterOf(original).get()).getMin());
        parameter.put("max", plant.getParameters().get(ParameterType.parameterOf(original).get()).getMax());
        return parameter;
    }
}
