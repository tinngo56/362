package Controllers;

import Models.SpaReservation.DayOfWeek;
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
        // Reset spa availability
        SpaAvailability spaAvailability = getSpaAvailability();
        spaAvailability.resetAvailability();
        updateSpaAvailability(spaAvailability);
        System.out.println("Spa availability has been reset.");

        // Clear all existing reservations
        clearAllReservations();
        System.out.println("All existing spa reservations have been cleared.");
    }

    private void clearAllReservations() throws IOException {
        List<Map<String, Object>> allReservations = storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).loadAll();
        for (Map<String, Object> reservationMap : allReservations) {
            SpaReservation reservation = convertMapToSpaReservation(reservationMap);
            storageHelper.getStore(SPA_RESERVATIONS_STORE_NAME).delete(String.valueOf(reservation.getId()));
        }
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

        // Prompt for customer's name
        String name;
        while (true) {
            System.out.print("Enter your name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                break;
            }
            System.out.println("Name cannot be empty. Please enter a valid name.");
        }

        // Choose time slot
        SpaAvailability spaAvailability = getSpaAvailability();
        TimeSlot selectedSlot = spaAvailability.chooseTimeSlot(scanner);
        if (selectedSlot == null) {
            System.out.println("No reservation made.");
            return null;
        }

        DayOfWeek day = selectedSlot.getDay();   // Enum
        int slot = selectedSlot.getSlot(); // 0-6

        // Get start and end times based on the selected slot
        double startTime = spaAvailability.times.get(slot).get(0);
        double endTime = spaAvailability.times.get(slot).get(1);

        // Ask for additional services
        System.out.print("Would you like a facial? (yes/y/no/n): ");
        String facialInput = scanner.nextLine().trim().toLowerCase();
        boolean facial = facialInput.equals("yes") || facialInput.equals("y");

        System.out.print("Would you like a basic massage? (yes/y/no/n): ");
        String massageBasicInput = scanner.nextLine().trim().toLowerCase();
        boolean massageBasic = massageBasicInput.equals("yes") || massageBasicInput.equals("y");

        System.out.print("Would you like a premium massage? (yes/y/no/n): ");
        String massagePremiumInput = scanner.nextLine().trim().toLowerCase();
        boolean massagePremium = massagePremiumInput.equals("yes") || massagePremiumInput.equals("y");

        System.out.print("Would you like a manicure? (yes/y/no/n): ");
        String manicureInput = scanner.nextLine().trim().toLowerCase();
        boolean manicure = manicureInput.equals("yes") || manicureInput.equals("y");

        System.out.print("Would you like a pedicure? (yes/y/no/n): ");
        String pedicureInput = scanner.nextLine().trim().toLowerCase();
        boolean pedicure = pedicureInput.equals("yes") || pedicureInput.equals("y");

        SpaReservation reservation = new SpaReservation(id, name, facial, massageBasic, massagePremium,
                manicure, pedicure, startTime, endTime, day.ordinal());

        System.out.println("\nSpa reservation created successfully!");
        System.out.println("Reservation Details:");
        System.out.println("ID: " + reservation.getId());
        System.out.println("Name: " + reservation.getName());
        System.out.println("Facial: " + (reservation.isFacial() ? "Yes" : "No"));
        System.out.println("Basic Massage: " + (reservation.isMassageBasic() ? "Yes" : "No"));
        System.out.println("Premium Massage: " + (reservation.isMassagePremium() ? "Yes" : "No"));
        System.out.println("Manicure: " + (reservation.isManicure() ? "Yes" : "No"));
        System.out.println("Pedicure: " + (reservation.isPedicure() ? "Yes" : "No"));
        System.out.println("Day: " + day.toString());
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
                SpaReservation reservation = convertMapToSpaReservation(readingMap);
                System.out.println("ID: " + reservation.getId());
                System.out.println("Name: " + reservation.getName());
                System.out.println("Day: " + reservation.getDay().toString());
                System.out.println("Facial: " + (reservation.isFacial() ? "Yes" : "No"));
                System.out.println("Basic Massage: " + (reservation.isMassageBasic() ? "Yes" : "No"));
                System.out.println("Premium Massage: " + (reservation.isMassagePremium() ? "Yes" : "No"));
                System.out.println("Manicure: " + (reservation.isManicure() ? "Yes" : "No"));
                System.out.println("Pedicure: " + (reservation.isPedicure() ? "Yes" : "No"));
                System.out.println("Start Time: " + reservation.getStartTime() + ":00");
                System.out.println("End Time: " + reservation.getEndTime() + ":00");
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
            SpaReservation reservation = convertMapToSpaReservation(readingMap);
            System.out.println("ID: " + reservation.getId());
            System.out.println("Name: " + reservation.getName());
            System.out.println("Day: " + reservation.getDay().toString());
            System.out.println("Facial: " + (reservation.isFacial() ? "Yes" : "No"));
            System.out.println("Basic Massage: " + (reservation.isMassageBasic() ? "Yes" : "No"));
            System.out.println("Premium Massage: " + (reservation.isMassagePremium() ? "Yes" : "No"));
            System.out.println("Manicure: " + (reservation.isManicure() ? "Yes" : "No"));
            System.out.println("Pedicure: " + (reservation.isPedicure() ? "Yes" : "No"));
            System.out.println("Start Time: " + reservation.getStartTime() + ":00");
            System.out.println("End Time: " + reservation.getEndTime() + ":00");
            System.out.println("----------------------");
        }
    }

    public void printReservationsForDay(Scanner scanner) throws IOException {
        System.out.println("---- View Reservations for a Specific Day ----");
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        while (true) {
            // Display days
            System.out.println("Select a day to view reservations (1-7) or 0 to cancel:");
            for (int i = 0; i < daysOfWeek.length; i++) {
                System.out.println((i + 1) + ". " + daysOfWeek[i]);
            }

            System.out.print("Enter your choice: ");
            int dayChoice;
            try {
                dayChoice = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a number between 0 and 7.");
                continue;
            }

            if (dayChoice == -1) {
                System.out.println("Operation cancelled.");
                return;
            }

            if (dayChoice < 0 || dayChoice >= daysOfWeek.length) {
                System.out.println("Invalid choice. Please select a valid day.");
                continue;
            }

            // Fetch all reservations
            List<Map<String, Object>> allReservations = getAllFacilityCleaningReports(); // Assuming this method fetches all reservations

            // Filter reservations for the selected day
            List<SpaReservation> reservationsForDay = new ArrayList<>();
            for (Map<String, Object> reservationMap : allReservations) {
                // Deserialize reservation
                SpaReservation reservation = convertMapToSpaReservation(reservationMap);
                if (reservation.getDay().ordinal() == dayChoice) {
                    reservationsForDay.add(reservation);
                }
            }

            // Display reservations
            if (reservationsForDay.isEmpty()) {
                System.out.println("No reservations found for " + daysOfWeek[dayChoice] + ".");
            } else {
                System.out.println("Reservations for " + daysOfWeek[dayChoice] + ":");
                System.out.println("---------------------------------------");
                for (SpaReservation reservation : reservationsForDay) {
                    System.out.println("ID: " + reservation.getId());
                    System.out.println("Name: " + reservation.getName());
                    System.out.println("Facial: " + (reservation.isFacial() ? "Yes" : "No"));
                    System.out.println("Basic Massage: " + (reservation.isMassageBasic() ? "Yes" : "No"));
                    System.out.println("Premium Massage: " + (reservation.isMassagePremium() ? "Yes" : "No"));
                    System.out.println("Manicure: " + (reservation.isManicure() ? "Yes" : "No"));
                    System.out.println("Pedicure: " + (reservation.isPedicure() ? "Yes" : "No"));
                    System.out.println("Start Time: " + reservation.getStartTime() + ":00");
                    System.out.println("End Time: " + reservation.getEndTime() + ":00");
                    System.out.println("---------------------------------------");
                }
            }

            // Option to view another day or exit
            System.out.print("Would you like to view reservations for another day? (yes/y to continue, any other key to exit): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!(response.equals("yes") || response.equals("y"))) {
                System.out.println("Returning to the manager menu.");
                break;
            }
        }
    }

    private String getDayName(int dayIndex) {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        if (dayIndex >= 0 && dayIndex < daysOfWeek.length) {
            return daysOfWeek[dayIndex];
        } else {
            return "Unknown";
        }
    }
}