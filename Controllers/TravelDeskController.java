package Controllers;

import Models.Excursions.Excursion;
import Storage.StorageHelper;

import java.io.IOException;

public class TravelDeskController {

    private StorageHelper storage;
    private final String EXCURSION_STORAGE = "excursions";

    public TravelDeskController(String baseDirectory) throws IOException {
        this.storage = new StorageHelper(baseDirectory, EXCURSION_STORAGE);
    }

    public void createExcursion(Excursion excursion) throws IOException {
        storage.getStore(EXCURSION_STORAGE).save(excursion.getName(), excursion.toMap());
    }



}
