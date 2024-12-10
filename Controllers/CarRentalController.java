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
    private StorageHelper customerStorageHelper;
    private StorageHelper vehicleStorageHelper;
    private StorageHelper agreementStorageHelper;
    private StorageHelper insuranceStorageHelper;
    private StorageHelper roomStorageHelper;

    private final String DATA_CUSTOMER_NAME = "customers";
    private final String DATA_VEHICLE_NAME = "rental_vehicles";
    private final String DATA_AGREEMENT_NAME = "rental_agreements";
    private final String DATA_INSURANCE_NAME = "insurance_options";
    private final String DATA_ROOM_NAME = "hotel_rooms";

    private final String STATUS_AVAILABLE = "AVAILABLE";
    private final String STATUS_RENTED = "RENTED";
    private final String STATUS_MAINTENANCE = "MAINTENANCE";
    private final String AGREEMENT_STATUS_ACTIVE = "ACTIVE";
    private final String AGREEMENT_STATUS_COMPLETED = "COMPLETED";
    private final String AGREEMENT_STATUS_CANCELLED = "CANCELLED";

    public CarRentalController(String baseDirectory) throws IOException {
        customerStorageHelper = new StorageHelper(baseDirectory, DATA_CUSTOMER_NAME);
        vehicleStorageHelper = new StorageHelper(baseDirectory, DATA_VEHICLE_NAME);
        agreementStorageHelper = new StorageHelper(baseDirectory, DATA_AGREEMENT_NAME);
        insuranceStorageHelper = new StorageHelper(baseDirectory, DATA_INSURANCE_NAME);
        roomStorageHelper = new StorageHelper(baseDirectory, DATA_ROOM_NAME);
    }

    public List<VehicleForRent> getAvailableVehicles() throws IOException {
        List<Map<String, Object>> vehicles = vehicleStorageHelper.getStore(DATA_VEHICLE_NAME)
                .loadAll();
        return vehicles.stream()
                .filter(v -> v.get("status").equals(STATUS_AVAILABLE))
                .map(v -> (VehicleForRent) new VehicleForRent().fromMap(v))
                .collect(Collectors.toList());
    }

    public boolean verifyCustomerStay(int roomNumber) throws IOException {
        Map<String, Object> roomData = roomStorageHelper.getStore(DATA_ROOM_NAME)
                .load(String.valueOf(roomNumber));
        if (roomData == null) {
            return false;
        }
        Room room = (Room) new Room().fromMap(roomData);
        return room.isOccupied();
    }

    public RentalAgreement initiateRental(Scanner scanner) throws IOException {
        System.out.println("\n=== Initiating Vehicle Rental ===");
        
        // Verify room and customer
        System.out.println("Enter room number:");
        int roomNumber = Integer.parseInt(scanner.nextLine());
        if (!verifyCustomerStay(roomNumber)) {
            throw new IOException("Invalid room number or customer not staying at hotel");
        }

        // Get customer information
        System.out.println("Enter customer ID:");
        String customerId = scanner.nextLine();
        Customer customer = (Customer) new Customer()
                .fromMap(customerStorageHelper.getStore(DATA_CUSTOMER_NAME).load(customerId));

        // Display available vehicles
        List<VehicleForRent> availableVehicles = getAvailableVehicles();
        System.out.println("\nAvailable Vehicles:");
        for (VehicleForRent vehicle : availableVehicles) {
            System.out.printf("%s - %s %s (%d) - $%.2f per day%n",
                vehicle.getVehicleId(), vehicle.getMake(), vehicle.getModel(),
                vehicle.getYear(), vehicle.getDailyRate());
        }

        // Vehicle selection
        System.out.println("\nEnter vehicle ID:");
        String vehicleId = scanner.nextLine();
        VehicleForRent selectedVehicle = (VehicleForRent) new VehicleForRent()
                .fromMap(vehicleStorageHelper.getStore(DATA_VEHICLE_NAME).load(vehicleId));

        // Rental period
        System.out.println("Enter rental duration (days):");
        int rentalDays = Integer.parseInt(scanner.nextLine());

        // Insurance options
        List<Map<String, Object>> insuranceOptions = insuranceStorageHelper
                .getStore(DATA_INSURANCE_NAME).loadAll();
        System.out.println("\nInsurance Options:");
        for (Map<String, Object> option : insuranceOptions) {
            Insurance insurance = (Insurance) new Insurance().fromMap(option);
            System.out.printf("%s - Coverage: $%.2f - Rate: $%.2f per day%n",
                insurance.getType(), insurance.getCoverage(), insurance.getDailyRate());
        }

        System.out.println("Select insurance (enter type or 'none'):");
        String insuranceType = scanner.nextLine();
        Insurance selectedInsurance = null;
        if (!"none".equalsIgnoreCase(insuranceType)) {
            selectedInsurance = (Insurance) new Insurance()
                    .fromMap(insuranceStorageHelper.getStore(DATA_INSURANCE_NAME).load(insuranceType));
        }

        // Create rental agreement
        RentalAgreement agreement = createRentalAgreement(
            customer, selectedVehicle, rentalDays, selectedInsurance);

        // Save agreement
        agreementStorageHelper.getStore(DATA_AGREEMENT_NAME)
                .save(agreement.getAgreementId(), agreement.toMap());

        // Update vehicle status
        selectedVehicle.setStatus(STATUS_RENTED);
        vehicleStorageHelper.getStore(DATA_VEHICLE_NAME)
                .save(vehicleId, selectedVehicle.toMap());

        return agreement;
    }

    private RentalAgreement createRentalAgreement(
            Customer customer, VehicleForRent vehicle, int rentalDays, Insurance insurance) {
        RentalAgreement agreement = new RentalAgreement();
        agreement.setAgreementId(UUID.randomUUID().toString());
        agreement.setCustomer(customer);
        agreement.setVehicle(vehicle);
        agreement.setStartTime(LocalDateTime.now());
        agreement.setEndTime(LocalDateTime.now().plusDays(rentalDays));
        agreement.setInsuranceSelected(insurance != null);
        agreement.setDailyRate(vehicle.getDailyRate());
        agreement.setStatus(AGREEMENT_STATUS_ACTIVE);
        
        // Calculate deposit based on insurance selection
        double deposit = insurance != null ? vehicle.getDailyRate() * 2 : vehicle.getDailyRate() * 4;
        agreement.setDeposit(deposit);

        // Apply loyalty discount if applicable
        if (customer.getNumberOfStays() > 5) {
            agreement.applyLoyaltyDiscount(0.10); // 10% discount for loyal customers
        }

        return agreement;
    }

    public void extendRental(Scanner scanner) throws IOException {
        System.out.println("\n=== Extending Rental Period ===");
        System.out.println("Enter agreement ID:");
        String agreementId = scanner.nextLine();

        RentalAgreement agreement = (RentalAgreement) new RentalAgreement()
                .fromMap(agreementStorageHelper.getStore(DATA_AGREEMENT_NAME).load(agreementId));

        System.out.println("Enter additional days:");
        int additionalDays = Integer.parseInt(scanner.nextLine());

        // Extend the rental period
        agreement.extendRental(agreement.getEndTime().plusDays(additionalDays));

        // Update deposit if needed
        if (!agreement.isInsuranceSelected()) {
            double additionalDeposit = agreement.getDailyRate() * additionalDays * 2;
            agreement.setDeposit(agreement.getDeposit() + additionalDeposit);
        }

        // Save updated agreement
        agreementStorageHelper.getStore(DATA_AGREEMENT_NAME)
                .save(agreementId, agreement.toMap());

        System.out.println("Rental period extended successfully");
        System.out.println("New return date: " + agreement.getEndTime());
        System.out.println("Additional deposit required: $" + 
                (agreement.isInsuranceSelected() ? 0 : agreement.getDailyRate() * additionalDays * 2));
    }

    public void completeRental(Scanner scanner) throws IOException {
        System.out.println("\n=== Completing Rental ===");
        System.out.println("Enter agreement ID:");
        String agreementId = scanner.nextLine();

        RentalAgreement agreement = (RentalAgreement) new RentalAgreement()
                .fromMap(agreementStorageHelper.getStore(DATA_AGREEMENT_NAME).load(agreementId));

        // Complete the rental
        agreement.completeRental();
        
        // Update vehicle status
        VehicleForRent vehicle = agreement.getVehicle();
        vehicle.setStatus(STATUS_AVAILABLE);
        vehicleStorageHelper.getStore(DATA_VEHICLE_NAME)
                .save(vehicle.getVehicleId(), vehicle.toMap());

        // Save completed agreement
        agreementStorageHelper.getStore(DATA_AGREEMENT_NAME)
                .save(agreementId, agreement.toMap());

        // Display final cost
        System.out.println("Rental completed successfully");
        System.out.println("Total cost: $" + agreement.calculateTotalCost());
        System.out.println("Deposit to be refunded: $" + agreement.getDeposit());
    }

    public void displayRentalStatus(String agreementId) throws IOException {
        Map<String, Object> agreementData = agreementStorageHelper.getStore(DATA_AGREEMENT_NAME)
                .load(agreementId);
        if (agreementData == null) {
            System.out.println("Agreement not found");
            return;
        }

        RentalAgreement agreement = (RentalAgreement) new RentalAgreement().fromMap(agreementData);
        System.out.println("\n=== Rental Status ===");
        System.out.println("Agreement ID: " + agreement.getAgreementId());
        System.out.println("Customer: " + agreement.getCustomer().getName());
        System.out.println("Vehicle: " + agreement.getVehicle().getMake() + " " + 
                agreement.getVehicle().getModel());
        System.out.println("Start Time: " + agreement.getStartTime());
        System.out.println("End Time: " + agreement.getEndTime());
        System.out.println("Insurance: " + (agreement.isInsuranceSelected() ? "Yes" : "No"));
        System.out.println("Daily Rate: $" + agreement.getDailyRate());
        System.out.println("Total Cost: $" + agreement.calculateTotalCost());
        System.out.println("Status: " + agreement.getStatus());
    }
}