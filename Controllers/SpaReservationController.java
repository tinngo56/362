package Controllers;

import Models.SpaReservation.SpaAvailability;
import Models.SpaReservation.SpaReservation;
import Models.SpaReservation.TimeSlot;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.*;

public class SpaReservationController {

    // General
    private StorageHelper storageHelper;
    private StorageHelper availabilityStorageHelper;
    private final String SPA_RESERVATIONS_STORE_NAME = "spaReservations";
    private final String AVAILABILITY_STORE_NAME = "spaReservationsAvailability";
    private final String AVAILABILITY_KEY = "spaAvailability";
    private int nextId;

    public SpaReservationController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, SPA_RESERVATIONS_STORE_NAME);
        this.availabilityStorageHelper = new StorageHelper(baseDirectory, AVAILABILITY_STORE_NAME);
        this.nextId = determineNextId();
        initSpaAvailability();
    }

    //---------------------------Store Managing Stuff---------------------------

    private int determineNextId() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).loadAll();
        int maxId = 0;
        for (Map<String, Object> readingMap : allReadings) {
            Number idNumber = (Number) readingMap.get("id");
            if (idNumber != null) {
                int id = idNumber.intValue();
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    private void initSpaAvailability() throws IOException {
        StorageHelper.DataStore<?> store = availabilityStorageHelper.getStore(AVAILABILITY_STORE_NAME);
        Map<String, Object> availabilityData = store.load(AVAILABILITY_KEY);
        if (availabilityData == null || !(availabilityData instanceof Map)) {
            SpaAvailability initialAvailability = new SpaAvailability();
            store.save(AVAILABILITY_KEY, initialAvailability.toMap());
            System.out.println("Initialized spa availability with default slots.");
        }
    }

    public void resetSpaAvailability() throws IOException {
        SpaAvailability spaAvailability = getSpaAvailability();
        spaAvailability.resetAvailability();
        updateSpaAvailability(spaAvailability);
        System.out.println("Spa availability has been reset.");
    }

    private void updateSpaAvailability(SpaAvailability spaAvailability) throws IOException {
        StorageHelper.DataStore<?> store = availabilityStorageHelper.getStore(AVAILABILITY_STORE_NAME);
        store.save(AVAILABILITY_KEY, spaAvailability.toMap());
    }

    private SpaAvailability getSpaAvailability() throws IOException {
        StorageHelper.DataStore<?> store = availabilityStorageHelper.getStore(AVAILABILITY_STORE_NAME);
        Map<String, Object> data = store.load(AVAILABILITY_KEY);
        if (data == null || !(data instanceof Map)) {
            initSpaAvailability();
            data = store.load(AVAILABILITY_KEY);
        }
        return new SpaAvailability(data);
    }

    public void saveSpaReservation(SpaReservation reservation) throws IOException {
        Map<String, Object> readingMap = convertSpaReservationToMap(reservation);
        storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).save(String.valueOf(reservation.getId()), readingMap);
    }

    public SpaReservation getSpaReservation(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToSpaReservation(data) : null;
    }

    public void updateSpaReservation(SpaReservation reservation) throws IOException {
        Map<String, Object> readingMap = convertSpaReservationToMap(reservation);
        storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).save(String.valueOf(reservation.getId()), readingMap);
    }

    public int getNumSpaReservations() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).loadAll();
        return allReadings.size();
    }

    private Map<String, Object> convertSpaReservationToMap(SpaReservation reservation) {
        return reservation.toMap();
    }

    public SpaReservation convertMapToSpaReservation(Map<String, Object> map) {
        return new SpaReservation(map);
    }

    public List<Map<String, Object>> getAllFacilityCleaningReports() throws IOException {
        return storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).loadAll();
    }

    //---------------------------Interactive Print Stuff---------------------------

    public SpaReservation makeAndSaveSpaReservationFromInput(Scanner scanner) throws IOException {
        System.out.println("---- Make a Spa Reservation ----");

        int id = nextId++;

        // Choose time slot
        SpaAvailability spaAvailability = getSpaAvailability();
        TimeSlot selectedSlot = spaAvailability.chooseTimeSlot(scanner);
        if (selectedSlot == null) {
            System.out.println("No reservation made.");
            return null;
        }

        int day = selectedSlot.getDay();   // 0-6
        int slot = selectedSlot.getSlot(); // 0-6

        // Get start and end times based on the selected slot
        double startTime = spaAvailability.times.get(slot).get(0);
        double endTime = spaAvailability.times.get(slot).get(1);

        System.out.print("Would you like a facial? (yes/no): ");
        boolean facial = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.print("Would you like a basic massage? (yes/no): ");
        boolean massageBasic = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.print("Would you like a premium massage? (yes/no): ");
        boolean massagePremium = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.print("Would you like a manicure? (yes/no): ");
        boolean manicure = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.print("Would you like a pedicure? (yes/no): ");
        boolean pedicure = scanner.nextLine().equalsIgnoreCase("yes");

        SpaReservation reservation = new SpaReservation(id, facial, massageBasic, massagePremium, manicure, pedicure, startTime, endTime);

        System.out.println("\nSpa reservation created successfully!");
        System.out.println("Reservation Details:");
        System.out.println("ID: " + reservation.getId());
        System.out.println("Facial: " + (reservation.isFacial() ? "Yes" : "No"));
        System.out.println("Basic Massage: " + (reservation.isMassageBasic() ? "Yes" : "No"));
        System.out.println("Premium Massage: " + (reservation.isMassagePremium() ? "Yes" : "No"));
        System.out.println("Manicure: " + (reservation.isManicure() ? "Yes" : "No"));
        System.out.println("Pedicure: " + (reservation.isPedicure() ? "Yes" : "No"));
        System.out.println("Start Time: " + reservation.getStartTime() + ":00");
        System.out.println("End Time: " + reservation.getEndTime() + ":00");

        saveSpaReservation(reservation);
        updateSpaAvailability(spaAvailability);
        return reservation;
    }

    public void resetSpaAvailabilityForNextWeek() throws IOException {
        resetSpaAvailability();
    }

    public void printAll(String s) throws IOException {
        List<Map<String, Object>> allReadings = getAllFacilityCleaningReports();

        if (allReadings.isEmpty()) {
            System.out.println("No SpaReservations available.");
            return;
        }

        System.out.println("All SpaReservations:");
        System.out.println("----------------------");

        for (Map<String, Object> readingMap : allReadings) {
            if (readingMap.containsKey("SpaReservationState") && readingMap.get("SpaReservationState").equals(s)) {
                for (Map.Entry<String, Object> entry : readingMap.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("----------------------");
            }
        }
    }

    public void printAll() throws IOException {
        List<Map<String, Object>> allReadings = getAllFacilityCleaningReports();

        if (allReadings.isEmpty()) {
            System.out.println("No SpaReservations available.");
            return;
        }

        System.out.println("All SpaReservations:");
        System.out.println("----------------------");

        for (Map<String, Object> readingMap : allReadings) {
            for (Map.Entry<String, Object> entry : readingMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("----------------------");
        }
    }
}