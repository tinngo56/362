package Controllers;

import Models.*;
import Storage.StorageHelper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CarRentalController {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    // Storage helpers
    private final StorageHelper vehicleStorage;
    private final StorageHelper agreementStorage;
    private final StorageHelper insuranceStorage;
    private final StorageHelper roomStorage;

    // Constants
    private static final String DATA_VEHICLE_NAME = "rental_vehicles";
    private static final String DATA_AGREEMENT_NAME = "rental_agreements";
    private static final String DATA_INSURANCE_NAME = "insurance_options";
    private static final String DATA_ROOM_NAME = "rooms";

    private static final String STATUS_AVAILABLE = "AVAILABLE";
    private static final String STATUS_RENTED = "RENTED";
    private static final String AGREEMENT_STATUS_ACTIVE = "ACTIVE";

    /**
     * Initialize the controller with the base directory for data storage
     */
    public CarRentalController(String baseDirectory) throws IOException {
        this.vehicleStorage = new StorageHelper(baseDirectory, DATA_VEHICLE_NAME);
        this.agreementStorage = new StorageHelper(baseDirectory, DATA_AGREEMENT_NAME);
        this.insuranceStorage = new StorageHelper(baseDirectory, DATA_INSURANCE_NAME);
        this.roomStorage = new StorageHelper(baseDirectory, DATA_ROOM_NAME);
    }

    /**
     * Get a list of all available vehicles for rent
     */
    public List<VehicleForRent> getAvailableVehicles() throws IOException {
        return loadAllVehicles().stream()
                .filter(v -> STATUS_AVAILABLE.equals(v.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Load all vehicles from storage
     */
    private List<VehicleForRent> loadAllVehicles() throws IOException {
        return vehicleStorage.getStore(DATA_VEHICLE_NAME)
                .loadAll().stream()
                .map(v -> (VehicleForRent) new VehicleForRent().fromMap(v))
                .collect(Collectors.toList());
    }

    /**
     * Get a specific vehicle by ID
     */
    public VehicleForRent getVehicleById(String vehicleId) throws IOException {
        return loadAllVehicles().stream()
                .filter(v -> vehicleId.equals(v.getVehicleId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Verify if a customer is staying in the given room
     */
    public boolean verifyCustomerStay(int roomNumber) throws IOException {
        Map<String, Object> roomData = roomStorage.getStore(DATA_ROOM_NAME)
                .load(String.valueOf(roomNumber));
        return roomData != null;
    }

    /**
     * Get all available insurance options
     */
    private List<Insurance> getInsuranceOptions() throws IOException {
        return insuranceStorage.getStore(DATA_INSURANCE_NAME)
                .loadAll().stream()
                .map(i -> (Insurance) new Insurance().fromMap(i))
                .collect(Collectors.toList());
    }

    /**
     * Get a specific insurance option by type
     */
    private Insurance getInsuranceByType(String type) throws IOException {
        Map<String, Object> insuranceData = insuranceStorage.getStore(DATA_INSURANCE_NAME)
                .load(type);
        return insuranceData != null ? (Insurance) new Insurance().fromMap(insuranceData) : null;
    }

    /**
     * Initiate a new rental agreement
     */
    public RentalAgreement initiateRental(Scanner scanner) throws IOException {
        System.out.println("\n=== Initiating Vehicle Rental ===");
        
        // Verify customer's room
        int roomNumber = promptForInt(scanner, "Enter room number:");
        if (!verifyCustomerStay(roomNumber)) {
            throw new IllegalStateException("Invalid room number or customer not staying at hotel");
        }

        // Display and select vehicle
        List<VehicleForRent> availableVehicles = getAvailableVehicles();
        displayAvailableVehicles(availableVehicles);
        
        String vehicleId = promptForString(scanner, "\nEnter vehicle ID:");
        VehicleForRent selectedVehicle = getVehicleById(vehicleId);
        if (selectedVehicle == null || !STATUS_AVAILABLE.equals(selectedVehicle.getStatus())) {
            throw new IllegalStateException("Selected vehicle is not available");
        }

        // Get rental duration
        int rentalDays = promptForInt(scanner, "Enter rental duration (days):");
        if (rentalDays <= 0) {
            throw new IllegalArgumentException("Rental duration must be positive");
        }

        // Display and select insurance
        displayInsuranceOptions(getInsuranceOptions());
        String insuranceType = promptForString(scanner, "Select insurance (enter type or 'none'):");
        Insurance selectedInsurance = !"none".equalsIgnoreCase(insuranceType) 
                ? getInsuranceByType(insuranceType) 
                : null;

        // Create and save the agreement
        RentalAgreement agreement = createRentalAgreement(selectedVehicle, rentalDays, selectedInsurance);
        saveRentalAgreement(agreement);
        updateVehicleStatus(selectedVehicle, STATUS_RENTED);

        return agreement;
    }

    /**
     * Create a new rental agreement
     */
    private RentalAgreement createRentalAgreement(VehicleForRent vehicle, int rentalDays, Insurance insurance) {
        RentalAgreement agreement = new RentalAgreement();
        agreement.setAgreementId(UUID.randomUUID().toString());
        agreement.setVehicle(vehicle);
        agreement.setStartTime(LocalDateTime.now());
        agreement.setEndTime(LocalDateTime.now().plusDays(rentalDays));
        agreement.setInsurance(insurance);
        agreement.setDailyRate(vehicle.getDailyRate());
        agreement.setStatus(AGREEMENT_STATUS_ACTIVE);
        
        // Calculate deposit based on insurance
        double deposit = (insurance != null ? 2 : 4) * vehicle.getDailyRate();
        agreement.setDeposit(deposit);

        return agreement;
    }

    /**
     * Extend an existing rental
     */
    public void extendRental(Scanner scanner) throws IOException {
        System.out.println("\n=== Extending Rental Period ===");
        
        String agreementId = promptForString(scanner, "Enter agreement ID:");
        RentalAgreement agreement = loadRentalAgreement(agreementId);
        if (agreement == null) {
            throw new IllegalStateException("Agreement not found");
        }

        int additionalDays = promptForInt(scanner, "Enter additional days:");
        if (additionalDays <= 0) {
            throw new IllegalArgumentException("Additional days must be positive");
        }

        agreement.setEndTime(agreement.getEndTime().plusDays(additionalDays));
        saveRentalAgreement(agreement);

        System.out.println("Rental period extended successfully");
        System.out.println("New return date: " + agreement.getEndTime());
        System.out.println("Total cost: $" + agreement.calculateTotalCost());
    }

    /**
     * Complete a rental agreement
     */
    public void completeRental(Scanner scanner) throws IOException {
        System.out.println("\n=== Completing Rental ===");
        
        String agreementId = promptForString(scanner, "Enter agreement ID:");
        RentalAgreement agreement = loadRentalAgreement(agreementId);
        if (agreement == null) {
            throw new IllegalStateException("Agreement not found");
        }

        agreement.setStatus("COMPLETED");
        saveRentalAgreement(agreement);

        VehicleForRent vehicle = agreement.getVehicle();
        updateVehicleStatus(vehicle, STATUS_AVAILABLE);

        System.out.println("Rental completed successfully");
        System.out.println("Total cost: $" + agreement.calculateTotalCost());
        System.out.println("Deposit to be refunded: $" + agreement.getDeposit());
    }

    /**
     * Display the status of a rental agreement
     */
    public void displayRentalStatus(String agreementId) throws IOException {
        RentalAgreement agreement = loadRentalAgreement(agreementId);
        if (agreement == null) {
            System.out.println("Agreement not found");
            return;
        }

        System.out.println("\n=== Rental Status ===");
        System.out.println("Agreement ID: " + agreement.getAgreementId());
        System.out.println("Vehicle: " + agreement.getVehicle().getMake() + " " + 
                agreement.getVehicle().getModel());
        System.out.println("Start Time: " + agreement.getStartTime());
        System.out.println("End Time: " + agreement.getEndTime());
        System.out.println("Daily Rate: $" + agreement.getDailyRate());
        System.out.println("Total Cost: $" + agreement.calculateTotalCost());
        System.out.println("Status: " + agreement.getStatus());
    }

    // Helper methods for storage operations
    private void saveRentalAgreement(RentalAgreement agreement) throws IOException {
        agreementStorage.getStore(DATA_AGREEMENT_NAME)
                .save(agreement.getAgreementId(), agreement.toMap());
    }

    private RentalAgreement loadRentalAgreement(String agreementId) throws IOException {
        Map<String, Object> agreementData = agreementStorage.getStore(DATA_AGREEMENT_NAME)
                .load(agreementId);
        return agreementData != null ? (RentalAgreement) new RentalAgreement().fromMap(agreementData) : null;
    }

    private void updateVehicleStatus(VehicleForRent vehicle, String status) throws IOException {
        vehicle.setStatus(status);
        vehicleStorage.getStore(DATA_VEHICLE_NAME)
                .save(vehicle.getVehicleId(), vehicle.toMap());
    }

    // Helper methods for user input and display
    private void displayAvailableVehicles(List<VehicleForRent> vehicles) {
        System.out.println("\nAvailable Vehicles:");
        vehicles.forEach(v -> System.out.printf("%s - %s %s (%d) - $%.2f per day%n",
                v.getVehicleId(), v.getMake(), v.getModel(),
                v.getYear(), v.getDailyRate()));
    }

    private void displayInsuranceOptions(List<Insurance> options) {
        System.out.println("\nInsurance Options:");
        options.forEach(i -> System.out.printf("%s - Coverage: $%.2f - Rate: $%.2f per day%n",
                i.getType(), i.getCoverage(), i.getDailyRate()));
    }

    private String promptForString(Scanner scanner, String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    private int promptForInt(Scanner scanner, String prompt) {
        System.out.println(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }
}