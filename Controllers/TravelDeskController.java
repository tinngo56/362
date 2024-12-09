package Controllers;

import Models.Excursions.Excursion;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TravelDeskController {

    private StorageHelper storage;
    private final String EXCURSION_STORAGE = "excursions";

    public TravelDeskController(String baseDirectory) throws IOException {
        this.storage = new StorageHelper(baseDirectory, EXCURSION_STORAGE);
    }

    public void createExcursion(Excursion excursion) throws IOException {
        storage.getStore(EXCURSION_STORAGE).save(excursion.getName(), excursion.toMap());
    }

    public Excursion getExcursion(String excursionName) throws IOException {
        Map<String, Object> data = storage.getStore(EXCURSION_STORAGE).load(excursionName);
        return data != null ? new Excursion().fromMap(data) : null;
    }

    public List<Excursion> getAllExcursions() throws IOException {
        List<Excursion> excursions = new ArrayList<>();
        List<Map<String, Object>> data = storage.getStore(EXCURSION_STORAGE).loadAll();
        for(Map<String, Object> excursion : data) {
            excursions.add(new Excursion().fromMap(excursion));
        }
        return excursions;
    }

    public void deleteExcursion(String excursionName) throws IOException {
        storage.getStore(EXCURSION_STORAGE).delete(excursionName);
    }

    public void addExcursion() throws IOException {
        String name;
        double price;
        String description;
        int slots;

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter name of excursion: ");
        name = scanner.next();

        if(getExcursion(name) != null) {
            System.out.println("\nERROR: Excursion already exists!\n");
            return;
        }

        System.out.print("Enter price of excursion: ");
        price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter description of excursion: ");
        description = scanner.nextLine();
        System.out.print("Enter number of available slots for excursion: ");
        slots = scanner.nextInt();

        Excursion excursion = new Excursion(name, price, description, slots);
        createExcursion(excursion);

        System.out.println("\nExcursion created.\n");
    }

    public void displayAllExcursions() throws IOException {
        List<Excursion> excursions = getAllExcursions();
        for(Excursion excursion : excursions) {
            System.out.println(excursion);
        }
    }

    public void removeExcursion() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter name: ");
        String name = scanner.nextLine();

        if(getExcursion(name) == null) {
            System.out.println("\nERROR: Excursion does not exist!\n");
            return;
        }

        deleteExcursion(name);
        System.out.println("\nExcursion " + name + " removed.\n");
    }



}
