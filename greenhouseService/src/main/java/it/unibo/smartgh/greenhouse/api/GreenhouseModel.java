package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.greenhouse.controller.GreenhouseController;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.Parameter;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterType;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Double.valueOf;
/**
 * Implementation of the Greenhouse service API.
 */
public class GreenhouseModel implements GreenhouseAPI{
    private static int OPERATION_PORT;
    private static String OPERATION_HOST;
    private static String CLIENT_COMMUNICATION_HOST;
    private static int CLIENT_COMMUNICATION_PORT;

    private static final Map<String, Map<String, String>> PARAMETERS = new HashMap<>();
    private final Vertx vertx;
    private final GreenhouseController greenhouseController;

    /**
     * Constructor of the greenhouse model.
     * @param vertx the actual vertx instance.
     * @param greenhouseController the greenhouse service controller.
     */
    public GreenhouseModel(Vertx vertx, GreenhouseController greenhouseController) {
        this.vertx = vertx;
        this.greenhouseController = greenhouseController;

        try {
            InputStream is = GreenhouseModel.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);
            CLIENT_COMMUNICATION_HOST = properties.getProperty("clientCommunication.host");
            CLIENT_COMMUNICATION_PORT = Integer.parseInt(properties.getProperty("clientCommunication.port"));
            OPERATION_HOST = properties.getProperty("operation.host");
            OPERATION_PORT = Integer.parseInt(properties.getProperty("operation.port"));

            new ArrayList<>(
                    Arrays.asList("brightness", "temperature", "humidity", "soilMoisture")).forEach(p -> {
                PARAMETERS.put(p, new HashMap<>(){{
                    put("host", properties.getProperty(p + ".host"));
                    put("port", properties.getProperty(p + ".port"));
                }});
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Future<Greenhouse> getGreenhouse(String id) {
        Promise<Greenhouse> promise = Promise.promise();
        try {
            Greenhouse greenhouse = greenhouseController.getGreenhouse(id);
            promise.complete(greenhouse);
        } catch (NoSuchElementException ex) {
            promise.fail("No greenhouse");
        }
        return promise.future();
    }

    @Override
    public Future<Void> putActualModality(String id, Modality modality) {
        Promise<Void> promise = Promise.promise();
        try {
            greenhouseController.changeActualModality(id, modality);
            notifyChangeModality(id, modality);
            promise.complete();
        } catch (NoSuchElementException ex) {
            promise.fail("invalid id");
        }
        return promise.future();
    }

    @Override
    public Future<Void> insertAndCheckParams(String id, JsonObject parameters) {
        Promise<Void> promise = Promise.promise();
        try {
            Greenhouse greenhouse = greenhouseController.getGreenhouse(id);
            storeParameters(id, parameters);//.onFailure(e -> promise.fail("parameter post error")); TODO add comment
            checkAlarm(greenhouse, parameters);
        } catch (NoSuchElementException ex) {
            promise.fail("No greenhouse");
        }
        promise.complete();
        return promise.future();
    }

    private void checkAlarm(Greenhouse greenhouse, JsonObject parameters) {
        Plant plant = greenhouse.getPlant();
        Map<ParameterType, Parameter> parametersMap = plant.getParameters();

        checkTemperature(greenhouse,
                parametersMap.get(ParameterType.TEMPERATURE).getMin(),
                parametersMap.get(ParameterType.TEMPERATURE).getMax(),
                valueOf(parameters.getValue("Temp").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);

        checkBrightness(greenhouse,
                parametersMap.get(ParameterType.BRIGHTNESS).getMin(),
                parametersMap.get(ParameterType.BRIGHTNESS).getMax(),
                valueOf(parameters.getValue("Bright").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);

        checkSoilMoisture(greenhouse,
                parametersMap.get(ParameterType.SOIL_MOISTURE).getMin(),
                parametersMap.get(ParameterType.SOIL_MOISTURE).getMax(),
                valueOf(parameters.getValue("Soil").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);

        checkHumidity(greenhouse,
                parametersMap.get(ParameterType.HUMIDITY).getMin(),
                parametersMap.get(ParameterType.HUMIDITY).getMax(),
                valueOf(parameters.getValue("Hum").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);
    }

    private void insertOperation (String ghId, String parameter, String action, String modality){
        WebClient client = WebClient.create(vertx);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        client.post(OPERATION_PORT, OPERATION_HOST, "/operations")
                .sendJsonObject(
                        new JsonObject()
                                .put("greenhouseId", ghId)
                                .put("modality", modality)
                                .put("date", formatter.format(new Date()))
                                .put("parameter", parameter)
                                .put("action", action)
                );

    }

    private void checkHumidity(Greenhouse gh, Double min, Double max, Double hum, boolean automatic) {
        String parameter = "humidity";
        String status = "normal";
        if (hum.compareTo(max) > 0){
            status = "alarm";
            if (automatic) {
                insertOperation(gh.getId(), parameter, "VENTILATION on", gh.getActualModality().toString());
            }
        } else {
            if (hum.compareTo(min) < 0) status = "alarm";
            if (automatic) {
                insertOperation(gh.getId(), parameter, "VENTILATION off", gh.getActualModality().toString());
            }
        }
        sendToClient(gh.getId(), parameter, hum, status);
    }

    private void checkSoilMoisture(Greenhouse gh, Double min, Double max, Double soil, boolean automatic) {
        String parameter = "soilMoisture";
        String status = "normal";
        if (soil.compareTo(min) < 0){
            status = "alarm";
            if (automatic) {
                insertOperation(gh.getId(), parameter, "IRRIGATION on", gh.getActualModality().toString());
            }
        } else {
            if (soil.compareTo(max) > 0) status = "alarm";
            if (automatic) {
                insertOperation(gh.getId(), parameter, "IRRIGATION off", gh.getActualModality().toString());
            }
        }
        sendToClient(gh.getId(), parameter, soil, status);
    }

    private void checkBrightness(Greenhouse gh, Double min, Double max, Double brigh, boolean automatic) {
        String parameter = "brightness";
        String status = "normal";
        if (brigh.compareTo(min) < 0){
            status = "alarm";
            if (automatic) {
                int newBrigh = brigh.compareTo(255.0) >= 0 ? 255 : brigh.intValue() + 5;
                insertOperation(gh.getId(), parameter, "LUMINOSITY " + newBrigh, gh.getActualModality().toString());
            }
        }  else if (brigh.compareTo(max) > 0) {
            status = "alarm";
            if (automatic) {
                int newBrigh = brigh.compareTo(0.0) <= 0 ? 0 : brigh.intValue() - 5;
                insertOperation(gh.getId(), parameter, "LUMINOSITY " + newBrigh, gh.getActualModality().toString());
            }
        }
        sendToClient(gh.getId(), parameter, brigh, status);
    }

    private void checkTemperature(Greenhouse gh, Double min, Double max, Double temp, boolean automatic) {
        String parameter = "temperature";
        String status = "normal";
        if (temp.compareTo(min) < 0){
            status = "alarm";
            if (automatic) {
                insertOperation(gh.getId(), parameter, "TEMPERATURE increase", gh.getActualModality().toString());
            }
        } else if (temp.compareTo(max) > 0) {
            status = "alarm";
            if (automatic) {
                insertOperation(gh.getId(), parameter, "TEMPERATURE decrease", gh.getActualModality().toString());
            }
        } else {
            if (temp.compareTo(min + 5.0) >= 0 || temp.compareTo(max - 5.0) <= 0) {
                if(automatic) {
                    insertOperation(gh.getId(), parameter, "TEMPERATURE turn-off", gh.getActualModality().toString());
                }
            }
        }
        sendToClient(gh.getId(), parameter, temp, status);
    }

    private void sendToClient(String id, String parameter, Double value, String status){
        WebClient client = WebClient.create(vertx);
        Promise<Void> promise = Promise.promise();
        List<Future> futures = new ArrayList<>();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            futures.add(client.post(CLIENT_COMMUNICATION_PORT, CLIENT_COMMUNICATION_HOST, "/clientCommunication/parameter")
                    .sendJsonObject(
                            new JsonObject()
                                    .put("greenhouseId", id)
                                    .put("parameterName", parameter)
                                    .put("value", value)
                                    .put("date", formatter.format(new Date()))
                                    .put("status", status)
                    ));
        promise.future();
    }

    private void storeParameters(String id, JsonObject parameters) {
        WebClient client = WebClient.create(vertx);

        Promise<Void> promise = Promise.promise();
        List<Future> futures = new ArrayList<>();
        for (String p: parameters.fieldNames()) {
            String path = "";
            switch (p) {
                case "Temp":{
                    path = "temperature";
                    break;
                }
                case "Soil": {
                    path = "soilMoisture";
                    break;
                }
                case "Bright": {
                    path = "brightness";
                    break;
                }
                case "Hum": {
                    path = "humidity";
                    break;
                }
            }
            String host = PARAMETERS.get(path).get("host");
            int port = Integer.parseInt(PARAMETERS.get(path).get("port"));
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            futures.add(client.post(port, host, "/"+ path)
                    .sendJsonObject(
                            new JsonObject()
                                    .put("greenhouseId", id)
                                    .put("value", parameters.getValue(p).toString())
                                    .put("date", formatter.format(new Date()))));
        }

        CompositeFuture.all(futures)
                .onSuccess(res -> promise.complete())
                .onFailure(ex -> promise.future());
        promise.future();
    }

    private void notifyChangeModality(String id, Modality modality){
        WebClient client = WebClient.create(vertx);
        client.post(CLIENT_COMMUNICATION_PORT, CLIENT_COMMUNICATION_HOST, "/clientCommunication/greenhouse/modality/notify")
                .sendJsonObject(
                        new JsonObject()
                                .put("greenhouseId", id)
                                .put("modality", modality)
                );
    }
}
