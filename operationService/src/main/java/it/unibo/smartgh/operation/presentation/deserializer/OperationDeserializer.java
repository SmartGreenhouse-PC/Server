package it.unibo.smartgh.operation.presentation.deserializer;

import com.google.gson.*;
import it.unibo.smartgh.operation.entity.Modality;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom {@link JsonDeserializer} for the {@link Operation} class. Used to convert a JSON object
 * into an {@link Operation} object.
 */
public class OperationDeserializer extends GeneralDeserializer implements JsonDeserializer<Operation> {

    @Override
    public Operation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        if (json instanceof JsonObject) {
            JsonObject object = (JsonObject) json;
            Date date;
            String greenhouseId = this.getPropertyAsString(object, "greenhouseId");
            try {
                date =formatter.parse(this.getPropertyAsString(object, "date"));
            } catch (ParseException e) {
                throw new JsonParseException("Not a valid date format (dd/MM/yyyy - HH:mm:ss)");
            }
            Modality modality = Modality.valueOf(this.getPropertyAsString(object, "modality").toUpperCase(Locale.ROOT));
            String parameter = this.getPropertyAsString(object, "parameter");
            String action = this.getPropertyAsString(object, "action");
            return new OperationImpl(greenhouseId, modality, date, parameter, action);
        } else {
            throw new JsonParseException("Not a valid operation");
        }
    }
}
