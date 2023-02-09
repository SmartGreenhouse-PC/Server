package it.unibo.smartgh.greenhouse.controller;

import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabase;

import java.util.List;

/**
 * Implementation of the greenhouse service controller.
 */
public class GreenhouseControllerImpl implements GreenhouseController {
    private final GreenhouseDatabase greenhouseDatabase;

    /**
     * Constructor of the greenhouse controller.
     * @param greenhouseDatabase the greenhouse service database.
     */
    public GreenhouseControllerImpl(GreenhouseDatabase greenhouseDatabase) {
        this.greenhouseDatabase = greenhouseDatabase;
    }

    @Override
    public void changeActualModality(String id, Modality modality) {
        this.greenhouseDatabase.putActualModality(id, modality);
    }

    @Override
    public Greenhouse getGreenhouse(String id) {
        return this.greenhouseDatabase.getGreenhouse(id);
    }

    @Override
    public List<String> getAllGreenhousesId() {
        return this.greenhouseDatabase.getAllGreenhousesId();
    }
}
