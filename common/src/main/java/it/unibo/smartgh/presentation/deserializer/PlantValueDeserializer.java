package it.unibo.smartgh.presentation.deserializer;

import com.google.gson.*;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Deserializer for the class {@link PlantValueImpl}.
 */
public class PlantValueDeserializer  extends GeneralDeserializer implements JsonDeserializer<PlantValue> {

    @Override
    public PlantValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            Date date;
            Double value;
            String greenhouseId = this.getPropertyAsString(object, "greenhouseId");
            try {
                System.out.println(this.getPropertyAsString(object, "date"));
                date = formatter.parse(this.getPropertyAsString(object, "date"));
            } catch (ParseException e) {
                throw new JsonParseException("Not a valid date format (dd/MM/yyyy - HH:mm:ss)");
            }
            value = this.getPropertyAs(object, "value", Double.class, context);
            return new PlantValueImpl(greenhouseId, date, value);
        }else{
            throw new JsonParseException("Not a valid plant value");
        }
    }

}
