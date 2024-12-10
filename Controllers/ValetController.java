package Controllers;

import Models.*;
import Storage.StorageHelper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ValetController {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private StorageHelper valetStaffStorageHelper;
    private StorageHelper vehicleStorageHelper;
    private StorageHelper inspectionStorageHelper;
    private StorageHelper parkingSpaceStorageHelper;

    private final String DATA_STAFF_NAME = "valet_staff";
    private final String DATA_VEHICLE_NAME = "vehicles";
    private final String DATA_INSPECTION_NAME = "vehicle_inspections";
    private final String DATA_PARKING_SPACE = "parking_spaces";

    private final String STATUS_AVAILABLE = "AVAILABLE";
    private final String STATUS_BUSY = "BUSY";
    private final String STATUS_PARKED = "PARKED";
    private final String STATUS_RETRIEVED = "RETRIEVED";

    private ValetStaff valetStaff;

    public ValetController(String baseDirectory) throws IOException {
        valetStaffStorageHelper = new StorageHelper(baseDirectory, DATA_STAFF_NAME);
        vehicleStorageHelper = new StorageHelper(baseDirectory, DATA_VEHICLE_NAME);
        inspectionStorageHelper = new StorageHelper(baseDirectory, DATA_INSPECTION_NAME);
        parkingSpaceStorageHelper = new StorageHelper(baseDirectory, DATA_PARKING_SPACE);
    }

    public ValetStaff getAvailableValetStaff() throws IOException {
        List<Map<String, Object>> list_of_valet_staff = valetStaffStorageHelper.getStore(DATA_STAFF_NAME)
                .loadAll();
        List<Map<String, Object>> availableStaff = list_of_valet_staff.stream()
                .filter(staff -> staff.get("status").equals(STATUS_AVAILABLE))
                .collect(Collectors.toList());
        if (availableStaff.isEmpty()) {
            throw new IOException("No available valet staff");
        }
        valetStaff = (ValetStaff) new ValetStaff().fromMap(availableStaff.get(0));
        System.out.println("Valet " + valetStaff.getName() + " is ready to serve.");
        return valetStaff;
    }

    public void parkVehicle(ValetStaff staff, Scanner scanner) throws IOException {
        try {
            System.out.println("\n=== Parking a Vehicle ===");
            System.out.println("Enter license plate number:");
            String licensePlate = scanner.nextLine();
            // Update staff status to busy
            updateStaffStatus(staff, STATUS_BUSY);

            // Create vehicle record if not exists
            Vehicle vehicle = new Vehicle();
            if (vehicleStorageHelper.getStore(DATA_VEHICLE_NAME).load(licensePlate) == null) {
                System.out.println("Enter vehicle make: ");
                vehicle.setMake(scanner.nextLine());
                System.out.println("Enter vehicle model: ");
                vehicle.setModel(scanner.nextLine());
            } else {
                vehicle = (Vehicle) new Vehicle()
                        .fromMap(vehicleStorageHelper.getStore(DATA_VEHICLE_NAME).load(licensePlate));
            }

            // Perform inspection
            VehicleInspection inspection = performInspection(licensePlate, staff, scanner);
            
            // Find parking space
            String parkingSpace = findAvailableSpace();
            if (parkingSpace == null) {
                parkingSpace = handleFullParking(staff);
            }

            // Update vehicle information
            vehicle.setLicensePlate(licensePlate);
            vehicle.setParkingSpace(parkingSpace);
            vehicle.setParkTime(LocalDateTime.now());
            vehicle.setStatus(STATUS_PARKED);
            vehicle.setParkedBy(staff.getName());
            vehicle.setFuelLevel(inspection.getFuelLevel());
            vehicle.setCondition(inspection.isHasDamage() ? "Damaged" : "Good");

            // Generate claim ticket
            String claimTicket = generateClaimTicket(licensePlate, parkingSpace);
            vehicle.setClaimTicket(claimTicket);

            // Save vehicle data
            vehicleStorageHelper.getStore(DATA_VEHICLE_NAME).save(licensePlate, vehicle.toMap());

            // Update parking space status
            updateParkingSpace(parkingSpace, licensePlate, staff);

            System.out.println("Claim Ticket: " + claimTicket);

            System.out.println("\nChecking vehicle status:");
            String status = checkVehicleStatus(licensePlate);
            System.out.println(status);

        } finally {
            // Always set staff back to available
            updateStaffStatus(staff, STATUS_AVAILABLE);
        }
    }

    private VehicleInspection performInspection(String licensePlate, ValetStaff staff, Scanner scanner) 
            throws IOException {
        VehicleInspection inspection = new VehicleInspection();
        inspection.setInspectionId(UUID.randomUUID().toString());
        inspection.setLicensePlate(licensePlate);
        inspection.setInspectedBy(staff.getName());
        inspection.setInspectionTime(LocalDateTime.now());

        System.out.println("Current fuel level (0.0-1.0): ");
        inspection.setFuelLevel(Double.parseDouble(scanner.nextLine()));

        System.out.println("Any visible damage? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            inspection.setHasDamage(true);
            System.out.println("Describe the damage: ");
            inspection.setDamageDetails(scanner.nextLine());
        }

        System.out.println("List visible personal items (comma-separated): ");
        String items = scanner.nextLine();
        if (!items.trim().isEmpty()) {
            inspection.setPersonalItems(Arrays.asList(items.split(",")));
        }

        inspectionStorageHelper.getStore(DATA_INSPECTION_NAME)
                .save(inspection.getInspectionId(), inspection.toMap());

        return inspection;
    }

    private String findAvailableSpace() throws IOException {
        List<Map<String, Object>> spaces = parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE)
                .loadAll();
        return spaces.stream()
                .filter(space -> space.get("status").equals(STATUS_AVAILABLE))
                .map(space -> (String) space.get("spaceId"))
                .findFirst()
                .orElse(null);
    }

    private String handleFullParking(ValetStaff staff) throws IOException {
        // Create overflow parking space
        String overflowId = "OF_" + System.currentTimeMillis();
        ParkingSpace overflow = new ParkingSpace();
        overflow.setSpaceId(overflowId);
        overflow.setStatus(STATUS_AVAILABLE);
        overflow.setOverflow(true);
        parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE)
                .save(overflowId, overflow.toMap());
        return overflowId;
    }

    private void updateParkingSpace(String spaceId, String licensePlate, ValetStaff staff) 
            throws IOException {
        Map<String, Object> space = parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE)
                .load(spaceId);
        space.put("status", STATUS_PARKED);
        space.put("vehicleLicense", licensePlate);
        space.put("lastUpdated", LocalDateTime.now().format(DATE_FORMAT));
        parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE).save(spaceId, space);
    }

    private String generateClaimTicket(String licensePlate, String parkingSpace) {
        return String.format("%s_%s_%s", 
            licensePlate,
            parkingSpace,
            LocalDateTime.now().format(DATE_FORMAT));
    }

    private void updateStaffStatus(ValetStaff staff, String status) throws IOException {
        staff.setStatus(status);
        valetStaffStorageHelper.getStore(DATA_STAFF_NAME)
                .save(String.valueOf(staff.getId()), staff.toMap());
    }

    public void retrieveVehicle(Scanner scanner, ValetStaff staff) throws IOException {
        System.out.println("\n=== Retrieving the Vehicle ===");
        System.out.println("Enter claim ticket:");
        String claimTicket = scanner.nextLine();
        
        System.out.println("\nRetrieving vehicle...");
        String licensePlate = null;
        try {
            updateStaffStatus(staff, STATUS_BUSY);
            
            String[] ticketParts = claimTicket.split("_");
            licensePlate = ticketParts[0];
            String parkingSpace = ticketParts[1];

            // Verify vehicle exists
            Vehicle vehicle = (Vehicle) new Vehicle()
                    .fromMap(vehicleStorageHelper.getStore(DATA_VEHICLE_NAME).load(licensePlate));

            if (!vehicle.getClaimTicket().equals(claimTicket)) {
                throw new IOException("Invalid claim ticket");
            }

            // Update vehicle status
            vehicle.setStatus(STATUS_RETRIEVED);
            vehicleStorageHelper.getStore(DATA_VEHICLE_NAME).save(licensePlate, vehicle.toMap());

            // Free up parking space
            ParkingSpace space = (ParkingSpace) new ParkingSpace()
                    .fromMap(parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE).load(parkingSpace));
            space.setStatus(STATUS_AVAILABLE);
            space.setVehicleLicense(null);
            
            // If it's an overflow space, delete it
            if (space.isOverflow()) {
                parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE).delete(parkingSpace);
            } else {
                parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE)
                    .save(parkingSpace, space.toMap());
            }

        } finally {
            updateStaffStatus(staff, STATUS_AVAILABLE);
            System.out.println("\nFinal vehicle status:");
            String status = checkVehicleStatus(licensePlate);
            System.out.println(status);
        }
    }

    public int getNextId() throws IOException {
        List<Map<String, Object>> list_of_valet_staff = valetStaffStorageHelper.getStore(DATA_STAFF_NAME)
                .loadAll();
        return list_of_valet_staff.size() + 1;
    }

    public void createValetStaff() throws IOException {
        int id = this.getNextId();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter contact info:");
        String contactInfo = scanner.nextLine();
        System.out.println("Enter wage:");
        double wage = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.println("Enter shift hours:");
        String shiftHours = scanner.nextLine();
        System.out.println("Enter experience (years):");
        int experience = scanner.nextInt();
        
        ValetStaff staff = new ValetStaff(id, name, contactInfo, STATUS_AVAILABLE, 
                                         wage, shiftHours, experience);
        valetStaffStorageHelper.getStore(DATA_STAFF_NAME)
            .save(String.valueOf(id), staff.toMap());
    }

    public void initializeParkingSpaces(int numSpaces) throws IOException {
        for (int i = 1; i <= numSpaces; i++) {
            ParkingSpace space = new ParkingSpace();
            String spaceId = String.format("PS%03d", i);
            space.setSpaceId(spaceId);
            space.setStatus(STATUS_AVAILABLE);
            space.setOverflow(false);
            
            parkingSpaceStorageHelper.getStore(DATA_PARKING_SPACE)
                .save(spaceId, space.toMap());
        }
    }

    public String checkVehicleStatus(String licensePlate) throws IOException {
        Map<String, Object> vehicleData = vehicleStorageHelper.getStore(DATA_VEHICLE_NAME)
            .load(licensePlate);
        if (vehicleData == null) {
            return "Vehicle not found";
        }
        Vehicle vehicle = (Vehicle) new Vehicle().fromMap(vehicleData);
        return String.format("Status: %s\nSpace: %s\nParked at: %s\nLicense Plate: %s\nMake: %s\nModel: %s\nFuel Level: %f\nCondition: %s\nClaim Ticket: %s\nParked By: %s", 
            vehicle.getStatus(), 
            vehicle.getParkingSpace(), 
            vehicle.getParkTime(), 
            vehicle.getLicensePlate(), 
            vehicle.getMake(), 
            vehicle.getModel(), 
            vehicle.getFuelLevel(), 
            vehicle.getCondition(), 
            vehicle.getClaimTicket(), 
            vehicle.getParkedBy());
    }
}