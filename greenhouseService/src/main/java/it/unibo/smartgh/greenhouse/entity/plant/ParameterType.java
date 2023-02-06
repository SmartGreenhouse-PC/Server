package it.unibo.smartgh.greenhouse.entity.plant;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

/**
 * The enum that represents the possible types of Parameter.
 */
public enum ParameterType {

    /**
     * The brightness parameter.
     */
    BRIGHTNESS("Luminosità", "brightness"),

    /**
     * The soil moisture parameter.
     */
    SOIL_MOISTURE("Umidità del suolo", "soilMoisture"),

    /**
     * The humidity parameter.
     */
    HUMIDITY("Umidità dell'aria", "humidity"),

    /**
     * The temperature parameter.
     */
    TEMPERATURE("Temperatura", "temperature");

    private final String title;
    private final String name;

    ParameterType(String title, String name) {
        this.title = new String(title.getBytes(), StandardCharsets.UTF_8);
        this.name = name;
    }

    /**
     * Gets the parameter's title.
     * @return the title of the parameter
     */
    public String getTitle() {
        return title;
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
    public static Optional<ParameterType> parameterOf(String parameterName) {
        return Arrays.stream(ParameterType.values()).filter(p -> p.name.equals(parameterName)).findFirst();
    }
}
