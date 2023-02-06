package it.unibo.smartgh.greenhouse.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.lang.Double.valueOf;

/**
 * Implementation of the Greenhouse service database.
 */
public class GreenhouseDatabaseImpl implements GreenhouseDatabase{
    private final static String DB_NAME = "greenhouse";
    private final static String COLLECTION_NAME = "greenhouse";

    private static String HOST;
    private static int PORT;

    public GreenhouseDatabaseImpl() {

        try {
            InputStream is = GreenhouseDatabaseImpl.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            HOST = properties.getProperty("mongodb.host");
            PORT = Integer.parseInt(properties.getProperty("mongodb.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putActualModality(String id, Modality modality) {
        this.getGreenhouse(id); //used for check the id
        MongoCollection<Document> collection = connection();
        collection.updateOne(
                new BasicDBObject("_id", new ObjectId(id)),
                new BasicDBObject("$set", new BasicDBObject("modality", modality.name()))
        );
    }

    @Override
    public Greenhouse getGreenhouse(String id) {
        MongoCollection<Document> collection = connection();
        Bson filter = Filters.eq("_id", new ObjectId(id));
        FindIterable<Document> documents = collection.find(filter);
        Document doc = documents.iterator().next();
        List list = new ArrayList<>(doc.values());
        Document plantDoc = (Document) list.get(1);
        Map<ParameterType, Parameter> parameters = new HashMap<>();
        ((Collection<Document>) plantDoc.get("parameters")).forEach(d -> {
            parameters.put( ParameterType.parameterOf(d.getString("name")).get(),
                    new ParameterBuilder(d.getString("name"))
                    .min(valueOf(d.get("min").toString()))
                    .max(valueOf(d.get("max").toString()))
                    .unit(d.getString("unit"))
                    .build());
        });
        Plant plant = new PlantBuilder(plantDoc.get("name", String.class))
                .description(plantDoc.get("description", String.class))
                .image(plantDoc.get("img", String.class))
                .parameters(parameters)
                .build();
        Modality modality = Modality.valueOf(doc.get("modality", String.class).toUpperCase());
        return new GreenhouseImpl(id, plant, modality);
    }

    private MongoCollection<Document> connection(){
        MongoClient mongoClient = MongoClients.create("mongodb://" + HOST + ":" + PORT);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        return database.getCollection(COLLECTION_NAME);
    }
}
