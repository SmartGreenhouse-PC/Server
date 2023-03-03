package it.unibo.smartgh.greenhouseCommunication.entity;

import java.util.Arrays;
import java.util.Optional;

/**
 * The enum that represents the possible types of Parameter.
 */
public enum ParameterType {

    /**
     * The brightness parameter.
     */
    BRIGHTNESS("Bright", "brightness"),

    /**
     * The soil moisture parameter.
     */
    SOIL_MOISTURE("Soil", "soilMoisture"),

    /**
     * The humidity parameter.
     */
    HUMIDITY("Hum", "humidity"),

    /**
     * The temperature parameter.
     */
    TEMPERATURE("Temp", "temperature");

    private final String topic;
    private final String name;

    ParameterType(String topic, String name) {
        this.topic = topic;
        this.name = name;
    }

    /**
     * Gets the parameter's title.
     * @return the title of the parameter
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Gets the parameter's name.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns an optional of parameter object from the given parameterName.
     * @param parameterName the parameter name
     * @return the optional of the parameter
     */
    public static Optional<ParameterType> parameterOfParameterName(String parameterName) {
        return Arrays.stream(ParameterType.values()).filter(p -> p.name.equals(parameterName)).findFirst();
    }

    /**
     * Returns an optional of parameter object from the given parameterName.
     * @param parameterTopic the parameter name
     * @return the optional of the parameter
     */
    public static Optional<ParameterType> parameterOfParameterTopic(String parameterTopic) {
        return Arrays.stream(ParameterType.values()).filter(p -> p.topic.equals(parameterTopic)).findFirst();
    }
}
