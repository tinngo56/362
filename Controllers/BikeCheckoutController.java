package Controllers;


import Models.CheckOutBike.Bike;
import Storage.StorageHelper;


import java.io.IOException;
import java.util.*;


public class BikeCheckoutController {


    //general
    private StorageHelper storageHelper;
    private final String BIKES_STORE_NAME = "bikes";
    private int nextId;
    private String IN = "in";
    private String OUT = "out";
    private String MAINTENANCE = "maintenance";


    public BikeCheckoutController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, BIKES_STORE_NAME);
        this.nextId = determineNextId();
    }


//---------------------------store managing stuff--------------------------- TODO only to find section


    private int determineNextId() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(BIKES_STORE_NAME).loadAll();
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


    public void saveBike(Bike facility) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(facility);
        storageHelper.getStore(BIKES_STORE_NAME).save(String.valueOf(facility.getId()), readingMap);
    }


    public Bike getBike(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(BIKES_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToHotelFacility(data) : null;
    }


    public void updateBike(Bike reading) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(reading);
        storageHelper.getStore(BIKES_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }


    public int getNumBikes() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(BIKES_STORE_NAME).loadAll();
        return allReadings.size();
    }


    private Map<String, Object> convertHotelFacilityToMap(Bike reading) {
        return reading.toMap();
    }


    public Bike convertMapToHotelFacility(Map<String, Object> map) {
//        Bike request = new Bike();
        return new Bike(map);
    }


    public List<Map<String, Object>> getAllFacilityCleaningReports() throws IOException {
        return storageHelper.getStore(BIKES_STORE_NAME).loadAll();
    }


//---------------------------interactive print stuff--------------------------- TODO only to find section




    private Bike makeBikeFromInput(Scanner scanner) throws IOException {
        System.out.println("Enter the following details to make add a new bike to the fleet:");
        int id = nextId++;
        System.out.println("What is the name of the bike?");
        String name = scanner.nextLine();
        System.out.println("What is the brand of the bike");
        String brand = scanner.nextLine();
        Bike b = new Bike(name,brand,id,IN);
        System.out.println(b);
        return b;
    }


    public void createAndSaveBikeFromInput(Scanner scnr) throws IOException {
        saveBike(makeBikeFromInput(scnr));
    }


    public void checkOutBike(Scanner scanner) throws IOException {
        System.out.println("=== Checking out a bike===");
        System.out.println("Are you a checked in guest or an employee? (y,n)");
        String s = scanner.nextLine().trim().toLowerCase();
        if(s.equals("n")) {
            System.out.println("You must be a guest or an employee to check out a bike");
            return;
        }
        printAll(IN);
        System.out.println("What id to check out:");
        int want = scanner.nextInt();
        Bike request = getBike(want);

        if(!processPayment(scanner)){
            System.out.println("You need a successful payment to checkout a bike going back to possible use cases");
            return;
        }

        request.setBikeState(OUT);

        saveBike(request);
        System.out.println("Bike has been checked out and saved.\n");
    }

    public boolean processPayment(Scanner scanner) {
        System.out.println("=== Bike Rental Payment ===");

        System.out.println("Enter the number of days you want to rent the bike ($10 per day):");
        int rentalDays = scanner.nextInt();
        scanner.nextLine();

        if (rentalDays <= 0) {
            System.out.println("Invalid number of days. Please try again.");
            return false;
        }

        int totalCost = rentalDays * 10;
        System.out.println("The total cost for renting the bike is: $" + totalCost);

        System.out.println("Select payment method: (1) Cash (2) Card");
        int paymentMethod = scanner.nextInt();
        scanner.nextLine();

        if (paymentMethod == 1) {
            System.out.println("Please hand over $" + totalCost + " in cash to the cashier.");
            System.out.println("Payment successful. Enjoy your ride!");
            return true;
        } else if (paymentMethod == 2) {
            System.out.println("Enter your card number (16 digits):");
            String cardNumber = scanner.nextLine().trim();

            if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                System.out.println("Invalid card number. Payment failed.");
                return false;
            }

            System.out.println("Enter card expiration date (MM/YY):");
            String expirationDate = scanner.nextLine().trim();

            System.out.println("Enter CVV (3 digits):");
            String cvv = scanner.nextLine().trim();

            if (cvv.length() != 3 || !cvv.matches("\\d+")) {
                System.out.println("Invalid CVV. Payment failed.");
                return false;
            }

            System.out.println("Processing payment...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Payment successful. Enjoy your ride!");
            return true;
        } else {
            System.out.println("Invalid payment method selected. Please try again.");
            return false;
        }
    }


    public void checkInBike(Scanner scanner) throws IOException {
        System.out.println("=== Checking in a bike===");
        printAll(OUT);
        System.out.println("What id to check in:");
        int want = scanner.nextInt();
        Bike request = getBike(want);
        scanner.nextLine();
        System.out.println("Does the bike need tune up?(y,n)");
        String s = scanner.nextLine().toLowerCase().trim();
        if(s.equals("y")) {
            request.setBikeState(MAINTENANCE);
        } else {
            request.setBikeState(IN);
        }

        saveBike(request);
        System.out.println("Bike has been checked in and saved.\n");
    }

    public void maintainBike(Scanner scanner) throws IOException {
        System.out.println("=== Maintain a bike===");
        printAll(MAINTENANCE);
        System.out.println("What id to maintain in:");
        int want = scanner.nextInt();
        Bike request = getBike(want);
        scanner.nextLine();
        System.out.println("Was the tune up and maintenance success?(y,n)");

        String s = scanner.nextLine().toLowerCase().trim();
        if(s.equals("y")) {
            request.setBikeState(IN);
        } else {
            request.setBikeState(MAINTENANCE);
        }

        saveBike(request);
        System.out.println("Bike has been saved.\n");
    }

    public void printAll(String s) throws IOException {
        List<Map<String, Object>> allReadings = getAllFacilityCleaningReports();


        if (allReadings.isEmpty()) {
            System.out.println("No Bikes available.");
            return;
        }


        System.out.println("All Bikes:");
        System.out.println("----------------------");


        for (Map<String, Object> readingMap : allReadings) {
            if(readingMap.get("bikeState").equals(s)) {
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
            System.out.println("No Bikes available.");
            return;
        }


        System.out.println("All Bikes:");
        System.out.println("----------------------");


        for (Map<String, Object> readingMap : allReadings) {
            for (Map.Entry<String, Object> entry : readingMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("----------------------");
        }
    }
}