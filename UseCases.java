import Controllers.*;
import Models.*;
import Models.CleanFacility.HotelFacility;

import java.awt.print.Book;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class UseCases {
    private final BookingController bookingController;
    private final CustomerController customerController;
    private final PaymentController paymentController;
    private final RoomController roomController;
    private final HotelController hotelController;
    private final PoolMaintenanceController poolMaintenanceController;
    private final PoolChemicalsController poolChemicalsController;
    private final CookBreakfastController cookBreakfastController;
    private final CleanFacilityController cleanFacilityController;
    private final WorkRequestController workRequestController;

    private Customer customer = new Customer(1, "Bob Smith", "bob.smith@gmail.com", "Basic", "Visa", 0);

    public UseCases(String baseDirectory) throws IOException {
        CEO ceo = new CEO(1, "John Doe", "john.doe@example.com", "CEO", "ACTIVE", 5, 1000000.0, 50000.0);

        this.bookingController = new BookingController(baseDirectory, ceo);
        this.customerController = new CustomerController(baseDirectory);
        this.hotelController = new HotelController(baseDirectory);
        this.paymentController = new PaymentController(baseDirectory);
        this.roomController = new RoomController(baseDirectory);
        this.poolMaintenanceController = new PoolMaintenanceController(baseDirectory);
        this.poolChemicalsController = new PoolChemicalsController(baseDirectory);
        this.cookBreakfastController = new CookBreakfastController(baseDirectory);
        this.cleanFacilityController = new CleanFacilityController(baseDirectory);
        this.workRequestController = new WorkRequestController(baseDirectory);
    }

    public void runUseCaseByActor(int actor) throws IOException{
        Scanner scnr = new Scanner(System.in);
        switch (actor){
            case 1:
                //Should have access to most use cases
                managerUseCases(scnr);
                break;
            case 2:
                maintenanceEmployeeUseCases(scnr);
                break;
            case 3:
                kitchenStaffUseCases(scnr);
                break;
            case 4:
                customerUseCases(scnr);
                break;
            case 5:
                cleaningStaffUseCases(scnr);
                break;
            default:
                System.out.println("Invalid actor number. Please try again.");
        }
    }

    private void cleaningStaffUseCases(Scanner scnr) throws IOException {
        CleaningStaffController cleaningStaffController = new CleaningStaffController("hotel_data");
        CleaningStaff cleaningStaff = cleaningStaffController.getAvailableCleaningStaff();

        System.out.println("Cleaning Staff: " + cleaningStaff.getName());
        System.out.println("Status: " + cleaningStaff.getStatus());

        System.out.println("\nCleaning staff choose what to run:");
        System.out.println("1. Clean Room");
        System.out.println("0. Exit to change your Actor choice");
        System.out.print("Enter your choice: ");

        int choice = scnr.nextInt();
        scnr.nextLine();

        switch (choice) {
            case 0:
                return;
            case 1:
                System.out.println("Enter room number: ");
                String room = scnr.nextLine();
                cleaningStaffController.cleanRoom(room, cleaningStaff);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void kitchenStaffUseCases(Scanner scnr) throws IOException {
        while (true) {
            System.out.println("\nKitchen staff choose what to run:");
            System.out.println("==========Breakfast actions==========");
            System.out.println("1. Make Breakfast");
            System.out.println("2. Add Ingredient");
            System.out.println("3. Subtract Ingredient (do not use for normal breakfast cooking)");
            System.out.println("4. Set Ingredient Amount (do not use for normal breakfast cooking)");
            System.out.println("5. View All Breakfast Reports");
            System.out.println("6. Delete a Breakfast Report");
            System.out.println("7. View current Ingredient stock");
            System.out.println("8. Check Ingredient stock for gusts");
            System.out.println("0. Exit to change your Actor choice");
            System.out.print("Enter your choice: ");

            int choice = scnr.nextInt();
            scnr.nextLine();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    cookBreakfastController.makeBreakfast(scnr);
                    break;
                case 2:
                    cookBreakfastController.addIngredient(scnr);
                    break;
                case 3:
                    cookBreakfastController.subtractIngredient(scnr);
                    break;
                case 4:
                    cookBreakfastController.setIngredientAmount(scnr);
                    break;
                case 5:
                    cookBreakfastController.viewAllReports();
                    break;
                case 6:
                    cookBreakfastController.deleteReport(scnr);
                    break;
                case 7:
                    cookBreakfastController.viewStock();
                    break;
                case 8:
                    checkIngredients(scnr);
                    break;
                default:
                    System.out.println("Invalid action number. Please try again.");
            }
        }
    }

    private void maintenanceEmployeeUseCases(Scanner scnr) throws IOException {
        while (true) {
            System.out.println("\nMaintenance staff choose what to run:");
            System.out.println("==========Pool Actions==========");
            System.out.println("1. Complete New Pool Inspection");
            System.out.println("2. View All Pool Inspections");
            System.out.println("3. Complete New Chemical Reading");
            System.out.println("4. View All Chemical Readings and current Chemical Inventory");
            System.out.println("5. Add to Chemical Inventory");
            System.out.println("6. Subtract from Chemical Inventory");
            System.out.println("7. Set Chemical Inventory");
            System.out.println("==========Breakfast actions==========");
            System.out.println("8. Add Ingredient");
            System.out.println("9. Subtract Ingredient (do not use for normal breakfast cooking)");
            System.out.println("10. Set Ingredient Amount (do not use for normal breakfast cooking)");
            System.out.println("11. View current Ingredient stock");
            System.out.println("0. Exit to change your Actor choice");
            System.out.print("Enter your choice: ");

            int choice = scnr.nextInt();
            scnr.nextLine();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    completeNewPoolInspection(scnr);
                    break;
                case 2:
                    viewPoolInspections();
                    break;
                case 3:
                    completeNewChemicalReading(scnr);
                    break;
                case 4:
                    viewChemicalReadings();
                    break;
                case 5:
                    addToChemicalInventory(scnr);
                    break;
                case 6:
                    subtractFromChemicalInventory(scnr);
                    break;
                case 7:
                    setChemicalInventory(scnr);
                    break;
                case 8:
                    cookBreakfastController.addIngredient(scnr);
                    break;
                case 10:
                    cookBreakfastController.subtractIngredient(scnr);
                    break;
                case 11:
                    cookBreakfastController.setIngredientAmount(scnr);
                    break;
                case 12:
                    cookBreakfastController.viewStock();
                    break;
                default:
                    System.out.println("Invalid action number. Please try again.");
            }
        }    }

    private void managerUseCases(Scanner scnr) throws IOException {
        while (true) {
            System.out.println("\nManager choose what to run:");
            System.out.println("==========Pool Actions==========");
            System.out.println("1. Complete New Pool Inspection");
            System.out.println("2. View All Pool Inspections");
            System.out.println("3. Complete New Chemical Reading");
            System.out.println("4. View All Chemical Readings and current Chemical Inventory");
            System.out.println("5. Add to Chemical Inventory");
            System.out.println("6. Subtract from Chemical Inventory");
            System.out.println("7. Set Chemical Inventory");
            System.out.println("==========Breakfast actions==========");
            System.out.println("8. Make Breakfast");
            System.out.println("9. Add Ingredient");
            System.out.println("10. Subtract Ingredient (do not use for normal breakfast cooking)");
            System.out.println("11. Set Ingredient Amount (do not use for normal breakfast cooking)");
            System.out.println("12. View All Breakfast Reports");
            System.out.println("13. Delete a Breakfast Report");
            System.out.println("14. View current Ingredient stock");
            System.out.println("15. Check Ingredient stock for gusts");
            System.out.println("0. Exit to change your Actor choice");
            System.out.println("==========Franchise actions==========");
            System.out.println("16. Demonstrate Profit Cycle");
            System.out.println("17. Sign Franchise Agreement");
            System.out.println("==========Clean facility actions==========");
            System.out.println("23. Do facility cleaning");
            System.out.println("24. view all");
            System.out.println("25. Set cleaning frequency");
            System.out.println("==========Clean facility actions==========");
            System.out.println("26. Create a Work Request");
            System.out.println("27. View All Work Requests");
            System.out.println("28. Complete a Work Request");
            System.out.println("==========Manage current cleaning inventory actions==========");
            System.out.println("29. View current inventory of cleaning supplies");
            System.out.println("30. Set current inventory of cleaning supplies");


            System.out.print("Enter your choice: ");

            int choice = scnr.nextInt();
            scnr.nextLine();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    completeNewPoolInspection(scnr);
                    break;
                case 2:
                    viewPoolInspections();
                    break;
                case 3:
                    completeNewChemicalReading(scnr);
                    break;
                case 4:
                    viewChemicalReadings();
                    break;
                case 5:
                    addToChemicalInventory(scnr);
                    break;
                case 6:
                    subtractFromChemicalInventory(scnr);
                    break;
                case 7:
                    setChemicalInventory(scnr);
                    break;
                case 8:
                    cookBreakfastController.makeBreakfast(scnr);
                    break;
                case 9:
                    cookBreakfastController.addIngredient(scnr);
                    break;
                case 10:
                    cookBreakfastController.subtractIngredient(scnr);
                    break;
                case 11:
                    cookBreakfastController.setIngredientAmount(scnr);
                    break;
                case 12:
                    cookBreakfastController.viewAllReports();
                    break;
                case 13:
                    cookBreakfastController.deleteReport(scnr);
                    break;
                case 14:
                    cookBreakfastController.viewStock();
                    break;
                case 15:
                    checkIngredients(scnr);
                    break;
                case 16:
                    demonstrateProfitCycle();
                    break;
                case 17:
                    signFranchiseAgreement(scnr);
                    break;
                case 23:
                    doFacilityCleaningReport(scnr);
                    break;
                case 24:
                    viewAllFacilityCleaningReports();
                    break;
                case 25:
                    setCleaningFrequencyOfFacility(scnr);
                    break;
                // New Work Request cases
                case 26:
                    workRequestController.createAndSaveWorkRequestFromInput(scnr);
                    break;
                case 27:
                    viewAllWorkRequests();
                    break;
                case 28:
                    workRequestController.completeWorkRequest(scnr);
                case 29:
                    System.out.println(cleanFacilityController.getCleaningInventory());
                    break;
                case 30:
                    cleanFacilityController.setCleaningInventory(scnr);
                    break;
                default:
                    System.out.println("Invalid action number. Please try again.");
            }
        }
    }


    private void viewAllWorkRequests() throws IOException {
        workRequestController.printAll();
    }



    private void customerUseCases(Scanner scnr) throws IOException {
        while(true) {
            System.out.println("\nCustomer choose what to run:");
            System.out.println("1. Book Hotel Room");
            System.out.println("2. Check out of Hotel Room");
            System.out.println("0. Exit to change your Actor choice");
            System.out.print("Enter your choice: ");

            int choice = scnr.nextInt();
            scnr.nextLine();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    BookHotelRoom(scnr);
                    break;
                case 2:
                    CheckOutOfHotelRoom(scnr);
                    break;
                default:
                    System.out.println("Invalid action number. Please try again.");
            }
        }
    }

    //cleaning


    private void setCleaningFrequencyOfFacility(Scanner scnr) throws IOException {
        cleanFacilityController.changeDaysBetweenCleaning(scnr);
    }
    private void viewAllFacilityCleaningReports() throws IOException {
        List<Map<String, Object>> reports = cleanFacilityController.getAllFacilityCleaningReports();
        if (reports.isEmpty()) {
            System.out.println("No facility cleaning reports available.");
            return;
        }

        System.out.println("All Facility Cleaning Reports:");
        System.out.println("------------------------------");
        for (Map<String, Object> report : reports) {
            if(report.get("pool") != null) {
                System.out.println("Days between cleaning");
            } else {
                System.out.println("Save");
            }
            System.out.println(report);
            System.out.println("------------------------------");
        }
    }


    private void doFacilityCleaningReport(Scanner scnr) throws IOException {
        cleanFacilityController.completeFacilityCleaning(scnr);
        System.out.println("Facility cleaning report added successfully.");
    }

    private void checkIngredients(Scanner scnr) throws IOException {
        System.out.println("How many guests will need breakfast?");
        int guest = scnr.nextInt()/2;
        cookBreakfastController.checkIngredients(guest);
    }

    private void addToChemicalInventory(Scanner scnr) throws IOException {
        poolChemicalsController.addToChemicalInventory(scnr);
    }

    private void subtractFromChemicalInventory(Scanner scnr) throws IOException {
        poolChemicalsController.subtractFromChemicalInventory(scnr);
    }

    private void setChemicalInventory(Scanner scnr) throws IOException {
        poolChemicalsController.setChemicalInventory(scnr);
    }

    private void completeNewChemicalReading(Scanner scanner) throws IOException {
        poolChemicalsController.completePoolChemicalReview(scanner);
    }

    private void viewChemicalReadings() throws IOException {
        poolChemicalsController.printAll();
    }

    private void viewPoolInspections() throws IOException {
        poolMaintenanceController.printAll();
    }

    private void completeNewPoolInspection(Scanner scanner) throws IOException {
        poolMaintenanceController.makeAndSavePoolEquipmentInspectionFromInput(scanner);
    }

    /**
     * Books a hotel room
     * Nathan Turnis
     * @param scanner Scanner used for I/O
     * @throws IOException thrown if storage communication fails
     */
    private void BookHotelRoom(Scanner scanner) throws IOException {
        Hotel hotel = hotelController.getHotel(1);

        System.out.println("\n\n----- BOOK A HOTEL ROOM -----\n");
        if(hotelController.isHotelSoldOut(hotel)) {
            System.out.println("Hotel is sold out!");
            return;
        }

        System.out.print("What room type? (Standard, Deluxe or Suite):  ");
        String roomType = scanner.next();

        Room room = roomController.isARoomAvailableFromRequirements(roomType);
        if(room == null) {
            System.out.println("Could not find a room. Try again with different requirements.");
            return;
        }

        System.out.print("There is a room for $" + room.getPricePerNight() + " per night. " +
                "Purchase? (y/n) ");
        char answer = scanner.next().charAt(0);

        if(answer == 'y') {
            System.out.print("How many nights? ");
            int nights;
            try {
                nights = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid nights!");
                return;
            }

            Booking booking = bookingController.bookRoom(room, nights, customer);
            if(booking == null) return;
            room.setStatus("OCCUPIED");
            room.setCurrentGuest(customer.getName());
            roomController.updateRoom(room);
            hotel.setNumAvailableRooms(hotel.getNumAvailableRooms() - 1);
            hotelController.updateHotel(hotel);

            System.out.println("Booking complete! Your booking ID is " + booking.getId() + ". Please remember this for checkout. " +
                    "Checkout date: " + booking.getCheckOutDate());
        }
    }

    /**
     * Checks out of a hotel room
     * @param scanner Scanner used for I/O
     * @throws IOException thrown if storage communication fails
     */
    private void CheckOutOfHotelRoom(Scanner scanner) throws IOException {
        Hotel hotel = hotelController.getHotel(1);

        System.out.println("\n\n----- CHECK OUT -----\n");
        if(!hotelController.hasRoomsCheckedOut(hotel)) {
            System.out.println("There are no rooms checked out at this hotel. (" + hotel.getName() + ")");
            return;
        }

        System.out.print("\nPlease enter booking ID: ");
        int bookingId = scanner.nextInt();

        Booking booking = bookingController.getBooking(bookingId);
        if(booking == null) {
            System.out.println("Booking ID " + bookingId + " not found. Please try again.\n");
            return;
        }

        Room room = roomController.getRoom(booking.getRoomNum());

        // ---- EXTEND STAY? ----
        System.out.print("\nWould you like to extend your stay? (y/n) ");
        char extend = scanner.next().charAt(0);
        if(extend == 'y') {
            System.out.print("How many nights? ");
            int nights = scanner.nextInt();
            booking = bookingController.extendStay(booking, room, nights, customer);
            if(booking == null) {
                System.out.println("Failed to extend stay.");
                return;
            }
            System.out.println("Your stay has been extended to " + booking.getCheckOutDate());
            return;
        }

        // ---- IF EARLY CHECK OUT ----
        if(bookingController.isEarlyCheckout(booking)) {
            System.out.println("WARNING: You are checking out early (booking checkout date is " + booking.getCheckOutDate() + "). " +
                    "You will still be required to pay your remaining nights.");
            System.out.print("Do you want to do this? (y/n) ");
            char answer = scanner.next().charAt(0);
            if(answer == 'n') return;
        }

        // ---- UPDATE ROOM AND HOTEL, DELETE BOOKING ----
        room.setStatus("NEEDS CLEANING");
        room.setCurrentGuest("");
        hotel.setNumAvailableRooms(hotel.getNumAvailableRooms() + 1);
        roomController.updateRoom(room);
        bookingController.checkOut(booking);
        hotelController.updateHotel(hotel);
        System.out.println("Successfully checked out. Thanks for staying at " + hotel.getName());

    }

    // Use case 11 (core profit cycle)
    private void demonstrateProfitCycle() throws IOException {
        System.out.println("\n\n----- DEMONSTRATE PROFIT CYCLE -----\n");
        if(bookingController.getNumOfBookings() <= 0) {
            System.out.println("A booking must be placed first.");
            return;
        }
    
        double franchiseOwnerPay = bookingController.getFranchiseOwnerPay();
        double ceoPay = bookingController.getCEOPay();
    
        System.out.println("Franchise Owner's Pay: $" + franchiseOwnerPay);
        System.out.println("CEO's Pay: $" + ceoPay + '\n');
        }

    // Use case 12 (sign franchise agreement)
private void signFranchiseAgreement(Scanner scanner) throws IOException {
    System.out.println("\n\n----- SIGN FRANCHISE AGREEMENT -----\n");

    System.out.print("Enter start date (YYYY-MM-DD): ");
    String startDate = scanner.next();

    System.out.print("Enter end date (YYYY-MM-DD): ");
    String endDate = scanner.next();

    double fees = 3.0 + (Math.random() * 4.0); // Random fee between 3% and 7%
    System.out.println("Franchise fee: " + fees + "%");

    System.out.println("Select conditions from the list below:");
    String[] conditionsList = {
        "Maintain brand standards",
        "Participate in marketing campaigns",
        "Adhere to pricing guidelines",
        "Undergo regular inspections",
        "Provide regular financial reports",
        "Attend training sessions",
        "Use approved suppliers",
        "Pay royalties on time",
        "Maintain customer satisfaction",
        "Follow operational procedures"
    };

    for (int i = 0; i < conditionsList.length; i++) {
        System.out.println((i + 1) + ". " + conditionsList[i]);
    }

    System.out.print("Enter the numbers of the conditions you agree to (comma-separated): ");
    String[] selectedConditionsIndices = scanner.next().split(",");
    StringBuilder selectedConditions = new StringBuilder();
    for (String index : selectedConditionsIndices) {
        int conditionIndex = Integer.parseInt(index.trim()) - 1;
        if (conditionIndex >= 0 && conditionIndex < conditionsList.length) {
            selectedConditions.append(conditionsList[conditionIndex]).append("\n");
        }
    }

    String conditions = selectedConditions.toString();

    System.out.println("You have agreed to the following conditions:");
    System.out.println(conditions);

    FranchiseAgreementController franchiseAgreementController = new FranchiseAgreementController("hotel_data");
    int agreementId = franchiseAgreementController.getNumOfAgreements() + 1;
    FranchiseAgreement agreement = new FranchiseAgreement(agreementId, startDate, endDate, fees, conditions);

    franchiseAgreementController.createFranchiseAgreement(agreement);

    System.out.println("Franchise agreement signed successfully!");
    System.out.println("Agreement ID: " + agreementId);
}

    private void updateBooking() throws IOException {

    }

    private void deleteBooking() throws IOException {
        bookingController.deleteBooking(1);
        System.out.println("Booking deleted successfully.");
    }

    private void createCustomer() throws IOException {
        Customer customer = new Customer(1, "John Doe", "john.doe@example.com", "GOLD", "CREDIT_CARD", 5);
        customerController.createCustomer(customer);
        System.out.println("Customer created successfully.");
    }

    private void getCustomer() throws IOException {
        Customer customer = customerController.getCustomer(1);
        if (customer != null) {
            System.out.println("Customer details: " + customer.toMap());
        } else {
            System.out.println("Customer not found.");
        }
    }

    private void updateCustomer() throws IOException {
        Customer customer = new Customer(1, "John Doe", "john.doe@example.com", "PLATINUM", "CREDIT_CARD", 10);
        customerController.updateCustomer(customer);
        System.out.println("Customer updated successfully.");
    }

    private void deleteCustomer() throws IOException {
        customerController.deleteCustomer(1);
        System.out.println("Customer deleted successfully.");
    }

    private void createPaymentMethod() throws IOException {
        PaymentMethod paymentMethod = new PaymentMethod(1, "CREDIT_CARD", "1234567890123456", "12/23", "ACTIVE");
        paymentController.createPaymentMethod(paymentMethod);
        System.out.println("Payment method created successfully.");
    }

    private void getPaymentMethod() throws IOException {
        PaymentMethod paymentMethod = paymentController.getPaymentMethod(1);
        if (paymentMethod != null) {
            System.out.println("Payment method details: " + paymentMethod.toMap());
        } else {
            System.out.println("Payment method not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            UseCases useCases = new UseCases("hotel_data");

            while (true) {
                System.out.println("Select a actor to run:");
                System.out.println("1. Manager");
                System.out.println("2. Maintenance Staff");
                System.out.println("3. Kitchen Staff");
                System.out.println("4. Customer");
                System.out.println("5. Cleaning Staff");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int useCaseNumber = scanner.nextInt();
                if (useCaseNumber == 0) {
                    break;
                }
                useCases.runUseCaseByActor(useCaseNumber);
            }
        }
    }
}