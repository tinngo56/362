import Controllers.CarRentalController;
import Models.*;
import Storage.StorageHelper;
import java.io.IOException;
import java.util.Scanner;

public class TinUseCases {
    private static CarRentalController controller;
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            // Initialize controller with data directory
            controller = new CarRentalController("./hotel_data");
            scanner = new Scanner(System.in);

            // Initialize sample data
            // initializeSampleData();

            // Start main program loop
            boolean running = true;
            while (running) {
                displayMenu();
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        initiateRental();
                        break;
                    case "2":
                        extendRental();
                        break;
                    case "3":
                        completeRental();
                        break;
                    case "4":
                        checkRentalStatus();
                        break;
                    case "5":
                        listAvailableVehicles();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing system: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Hotel Car Rental System ===");
        System.out.println("1. Rent a Vehicle");
        System.out.println("2. Extend Rental");
        System.out.println("3. Complete Rental");
        System.out.println("4. Check Rental Status");
        System.out.println("5. List Available Vehicles");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void initiateRental() {
        try {
            RentalAgreement agreement = controller.initiateRental(scanner);
            System.out.println("\nRental agreement created successfully!");
            System.out.println("Agreement ID: " + agreement.getAgreementId());
        } catch (IOException e) {
            System.err.println("Error creating rental: " + e.getMessage());
        }
    }

    private static void extendRental() {
        try {
            controller.extendRental(scanner);
        } catch (IOException e) {
            System.err.println("Error extending rental: " + e.getMessage());
        }
    }

    private static void completeRental() {
        try {
            controller.completeRental(scanner);
        } catch (IOException e) {
            System.err.println("Error completing rental: " + e.getMessage());
        }
    }

    private static void checkRentalStatus() {
        try {
            System.out.println("Enter agreement ID:");
            String agreementId = scanner.nextLine();
            controller.displayRentalStatus(agreementId);
        } catch (IOException e) {
            System.err.println("Error checking rental status: " + e.getMessage());
        }
    }

    private static void listAvailableVehicles() {
        try {
            System.out.println("\nAvailable Vehicles:");
            controller.getAvailableVehicles().forEach(vehicle -> 
                System.out.printf("%s - %s %s (%d) - $%.2f per day%n",
                    vehicle.getVehicleId(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getDailyRate())
            );
        } catch (IOException e) {
            System.err.println("Error listing vehicles: " + e.getMessage());
        }
    }

    private static void initializeSampleData() throws IOException {
        StorageHelper vehicleStorage = new StorageHelper("./hotel_data", "rental_vehicles");
        StorageHelper customerStorage = new StorageHelper("./hotel_data", "customers");
        StorageHelper roomStorage = new StorageHelper("./hotel_data", "rooms");
        StorageHelper insuranceStorage = new StorageHelper("./hotel_data", "insurance_options");

        // Initialize sample vehicles
        VehicleForRent[] vehicles = {
            createVehicle("V001", "Toyota", "Camry", 2023, 65.0),
            createVehicle("V002", "Honda", "CR-V", 2023, 75.0),
            createVehicle("V003", "BMW", "X5", 2023, 120.0),
            createVehicle("V004", "Tesla", "Model 3", 2023, 100.0)
        };

        for (VehicleForRent vehicle : vehicles) {
            vehicleStorage.getStore("rental_vehicles").save(vehicle.getVehicleId(), vehicle.toMap());
        }

        // Initialize sample customers
        Customer[] customers = {
            createCustomer(1, "John Doe", "123-456-7890", "GOLD", "CREDIT_CARD", 10),
            createCustomer(2, "Jane Smith", "098-765-4321", "SILVER", "CREDIT_CARD", 5)
        };

        for (Customer customer : customers) {
            customerStorage.getStore("customers").save(String.valueOf(customer.getId()), customer.toMap());
        }

        // Initialize sample rooms
        Room[] rooms = {
            createRoom(101, true),
            createRoom(102, true),
            createRoom(103, false)
        };

        for (Room room : rooms) {
            roomStorage.getStore("rooms").save(String.valueOf(room.getRoomNumber()), room.toMap());
        }

        // Initialize insurance options
        Insurance[] insuranceOptions = {
            createInsurance("BASIC", 50000.0, 15.0),
            createInsurance("PREMIUM", 100000.0, 25.0),
            createInsurance("ULTIMATE", 200000.0, 35.0)
        };

        for (Insurance insurance : insuranceOptions) {
            insuranceStorage.getStore("insurance_options").save(insurance.getType(), insurance.toMap());
        }
    }

    private static VehicleForRent createVehicle(String id, String make, String model, int year, double rate) {
        VehicleForRent vehicle = new VehicleForRent();
        vehicle.setVehicleId(id);
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setDailyRate(rate);
        vehicle.setStatus("AVAILABLE");
        vehicle.setCurrentLocation("HOTEL_GARAGE");
        return vehicle;
    }

    private static Customer createCustomer(int id, String name, String contact, String loyalty, String payment, int stays) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setContactInfo(contact);
        customer.setLoyaltyProgramLevel(loyalty);
        customer.setPaymentMethod(payment);
        customer.setNumberOfStays(stays);
        return customer;
    }

    private static Room createRoom(int roomNumber, boolean occupied) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.isOccupied(occupied);
        room.setRoomType("STANDARD");
        room.setStatus(occupied ? "OCCUPIED" : "AVAILABLE");
        return room;
    }

    private static Insurance createInsurance(String type, double coverage, double rate) {
        Insurance insurance = new Insurance();
        insurance.setType(type);
        insurance.setCoverage(coverage);
        insurance.setDailyRate(rate);
        return insurance;
    }
}