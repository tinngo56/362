import Controllers.*;
import Models.*;
import Models.Vending.VendingMachine;
import Storage.StorageHelper;

import java.awt.print.Book;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class UseCases {
    private final EmailController emailController;
    private final RewardsController rewardsController;
    private final BookingController bookingController;
    private final CustomerController customerController;
    private final PaymentController paymentController;
    private final RoomController roomController;
    private final HotelController hotelController;
    private final PoolMaintenanceController poolMaintenanceController;
    private final PoolChemicalsController poolChemicalsController;
    private final CookBreakfastController cookBreakfastController;
    private final VendingController vendingController;
    private final KeyCardController keyCardController;
    private final FacilityController facilityController;
    private final RoomServiceController roomServiceController;
    private final KitchenController kitchenController;
    private final RoomServiceStaffController roomServiceStaffController;
    private final CleanFacilityController cleanFacilityController;
    private final WorkRequestController workRequestController;
    private final BikeCheckoutController bikeCheckoutController;
    private final SpaReservationController spaReservationController;
    private final HiringController hiringController;
    private final ApplicantController applicantController;
    private final PayrollController payrollController;
    private final GroundsMaintenanceController groundsMaintenanceController;
    private final EventPlanningController eventPlanningController;

    private Customer customer = new Customer(1, "Bob Smith", "bob.smith@gmail.com", "Basic", "Visa", 0);

    public UseCases(String baseDirectory) throws IOException {
        CEO ceo = new CEO(1, "John Doe", "john.doe@example.com", "CEO", "ACTIVE", 5, 1000000.0, 50000.0);

        this.emailController = new EmailController(baseDirectory);
        this.rewardsController = new RewardsController(baseDirectory);
        this.bookingController = new BookingController(baseDirectory, ceo);
        this.customerController = new CustomerController(baseDirectory);
        this.hotelController = new HotelController(baseDirectory);
        this.paymentController = new PaymentController(baseDirectory);
        this.roomController = new RoomController(baseDirectory);
        this.poolMaintenanceController = new PoolMaintenanceController(baseDirectory);
        this.poolChemicalsController = new PoolChemicalsController(baseDirectory);
        this.cookBreakfastController = new CookBreakfastController(baseDirectory);
        this.vendingController = new VendingController(baseDirectory);
        this.keyCardController = new KeyCardController(baseDirectory);
        this.facilityController = new FacilityController();
        this.roomServiceController = new RoomServiceController(baseDirectory);
        this.kitchenController = new KitchenController(baseDirectory);
        this.roomServiceStaffController = new RoomServiceStaffController(baseDirectory);
        this.cleanFacilityController = new CleanFacilityController(baseDirectory);
        this.workRequestController = new WorkRequestController(baseDirectory);
        this.bikeCheckoutController = new BikeCheckoutController(baseDirectory);
        this.spaReservationController = new SpaReservationController(baseDirectory);
        this.hiringController = new HiringController(baseDirectory);
        this.applicantController = new ApplicantController(baseDirectory);
        this.payrollController = new PayrollController(baseDirectory);
        this.groundsMaintenanceController = new GroundsMaintenanceController(baseDirectory);
        this.eventPlanningController = new EventPlanningController(baseDirectory);
    }

    public void runUseCaseByActor(int actor, Scanner scnr) throws IOException{
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
            case 6:
                applicantUseCases(scnr);
                break;
            case 7:
                groundsKeeperUseCases(scnr);
                break;
            default:
                System.out.println("Invalid actor number. Please try again.");
        }
    }

    private void applicantUseCases(Scanner scnr) throws IOException {
        while (true) {
            System.out.println("\nApplicant Actions:");
            System.out.println("1. Apply to Job Posting");
            System.out.println("2. Accept or Decline Job Offer");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scnr.nextInt();
            scnr.nextLine();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    applicantController.applyToJobPosting(scnr);
                    break;
                case 2:
                    applicantController.acceptOrDeclineJobOffer(scnr);
                    break;
                default:
                    System.out.println("Invalid action number. Please try again.");
            }
        }
    }

    private void groundsKeeperUseCases(Scanner scnr) throws IOException {
        while (true) {
            System.out.println("\nGrounds Keeper choose what to run:");
            System.out.println("1. Create Grounds Inspection");
            System.out.println("2. Modify Grounds Inspection");
            System.out.println("3. Generate Inspection Report");
            System.out.println("4. Log Unexpected Issues");
            System.out.println("5. Validate and Replace Grounds Log");
            System.out.println("6. Cancel Grounds Inspection");
            System.out.println("0. Exit to change your Actor choice");
            System.out.print("Enter your choice: ");

            int choice = scnr.nextInt();
            scnr.nextLine(); // Consume the newline

            switch (choice) {
                case 0:
                    return;
                case 1:
                    groundsMaintenanceController.createAndSaveGroundsInspectionFromInput(scnr);
                    break;
                case 2:
                    groundsMaintenanceController.modifyGroundsInspection(scnr);
                    break;
                case 3:
                    groundsMaintenanceController.generateInspectionReport();
                    break;
                case 4:
                    groundsMaintenanceController.logUnexpectedIssues(scnr);
                    break;
                case 5:
                    groundsMaintenanceController.validateAndReplaceGroundsLog(scnr);
                    break;
                case 6:
                    groundsMaintenanceController.cancelGroundsInspection(scnr); // New option
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
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
                cleaningStaffController.cleanRoom(room, cleaningStaff, scnr);
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
            System.out.println("==========Vending actions==========");
            System.out.println("18. Add Item to Vending Machine");
            System.out.println("19. Restock the Vending Machine");
            System.out.println("=========Other actions========");
            System.out.println("20. Access Facility");
            System.out.println("==========Clean facility actions==========");
            System.out.println("23. Do facility cleaning");
            System.out.println("24. view all");
            System.out.println("25. Set cleaning frequency");
            System.out.println("==========Work request actions==========");
            System.out.println("26. Create a Work Request");
            System.out.println("27. View All Work Requests");
            System.out.println("28. Complete a Work Request");
            System.out.println("==========Manage current cleaning inventory actions==========");
            System.out.println("29. View current inventory of cleaning supplies");
            System.out.println("30. Set current inventory of cleaning supplies");
            System.out.println("==========Manage bikes actions==========");
            System.out.println("31. Add a bike");
            System.out.println("32. Maintain a bike");
            System.out.println("33. Check in a bike");
            System.out.println("34. Check out a bike");
            System.out.println("35. See current bike fleet");
            System.out.println("==========Spa actions==========");
            System.out.println("36. Reset for Spa for next week");
            System.out.println("37. Reserve spa");
            System.out.println("38. View Reservations for a Specific Day");
            System.out.println("==========Marketing actions==========");
            System.out.println("39. Send marketing email");
            System.out.println("==========Hiring actions==========");
            System.out.println("40. Request New Hire");
            System.out.println("41. Shortlist Applicants");
            System.out.println("42. Interview Shortlisted Applicants");
            System.out.println("43. Select Candidate");
            System.out.println("44. Cancel Hiring Process");
            System.out.println("45. Schedule interview");
            System.out.println("==========Payroll actions==========");
            System.out.println("46. Verify Employee Payroll Info");
            System.out.println("47. Pay Employee");
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
                case 18:
                    vendingController.addItemToVendingMachine();
                    break;
                case 19:
                    vendingController.restockVendingMachine();
                    break;
                case 20:
                    accessFacility(scnr, true);
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
                case 26:
                    workRequestController.createAndSaveWorkRequestFromInput(scnr);
                    break;
                case 27:
                    workRequestController.printAll();
                    break;
                case 28:
                    workRequestController.completeWorkRequest(scnr);
                case 29:
                    System.out.println(cleanFacilityController.getCleaningInventory());
                    break;
                case 30:
                    cleanFacilityController.setCleaningInventory(scnr);
                    break;
                case 31:
                    bikeCheckoutController.createAndSaveBikeFromInput(scnr);
                    break;
                case 32:
                    bikeCheckoutController.maintainBike(scnr);
                    break;
                case 33:
                    bikeCheckoutController.checkInBike(scnr);
                    break;
                case 34:
                    bikeCheckoutController.checkOutBike(scnr);
                    break;
                case 35:
                    bikeCheckoutController.printAll();
                    break;
                case 36:
                    spaReservationController.resetSpaAvailabilityForNextWeek();
                    break;
                case 37:
                    spaReservationController.makeAndSaveSpaReservationFromInput(scnr);
                    break;
                case 38:
                    spaReservationController.printReservationsForDay(scnr);
                    break;
                case 39:
                    sendMassEmail();
                    break;
                case 40:
                    hiringController.requestNewHire(scnr);
                    break;
                case 41:
                    hiringController.shortListApplicants(scnr);
                    break;
                case 42:
                    hiringController.interviewShortlistedApplicants(scnr);
                    break;
                case 43:
                    hiringController.selectCandidate(scnr);
                    break;
                case 44:
                    hiringController.cancelHiringProcess(scnr);
                    break;
                case 45:
                    hiringController.scheduleInterview(scnr);
                    break;
                case 46:
                    payrollController.verifyPayrollInfo(scnr);
                    break;
                case 47:
                    payrollController.payEmployee(scnr);
                    break;
                default:
                    System.out.println("Invalid action number. Please try again.");
            }
        }
    }

    private void customerUseCases(Scanner scnr) throws IOException {
        while(true) {
            System.out.println("\nCustomer choose what to run:");
            System.out.println("1. Book Hotel Room");
            System.out.println("2. Check out of Hotel Room");
            System.out.println("3. Book Room with Rewards");
            System.out.println("4. Vending");
            System.out.println("5. Access Facility");
            System.out.println("6. Room Service");
            System.out.println("==========Spa actions==========");
            System.out.println("7. Reserve spa");
            System.out.println("8. View Reservations for a Specific Day");
            System.out.println("==========Manage bikes actions==========");
            System.out.println("9. Check in a bike");
            System.out.println("10. Check out a bike");
            System.out.println("11. See current bike fleet");
            System.out.println("==========Event Booking Actions==========");
            System.out.println("12. Book Event Room");
            System.out.println("13. Cancel Event Room Booking");
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
                case 3:
                    bookRoomWithRewards();
                    break;
                case 4:
                    useVendingMachine(scnr);
                    break;
                case 5:
                    accessFacility(scnr, false);
                    break;
                case 6:
                    roomService(scnr);
                    break;
                case 7:
                    spaReservationController.makeAndSaveSpaReservationFromInput(scnr);
                    break;
                case 8:
                    spaReservationController.printReservationsForDay(scnr);
                    break;
                case 9:
                    bikeCheckoutController.checkInBike(scnr);
                    break;
                case 10:
                    bikeCheckoutController.checkOutBike(scnr);
                    break;
                case 11:
                    bikeCheckoutController.printAll();
                    break;
                case 12:
                    eventPlanningController.createBooking(scnr);
                    break;
                case 13:
                    eventPlanningController.cancelBooking(scnr);
                    break;
                default:
                    System.out.println("Invalid action number. Please try again.");
            }
        }
    }

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



    private void roomService(Scanner scnr) throws IOException {
        System.out.println("\n----- ROOM SERVICE -----\n");

        // Get room number
        System.out.print("Enter room number: ");
        int roomNumber = scnr.nextInt();
        scnr.nextLine();

        // Validate room
        if (!roomController.isRoomOccupied(roomNumber)) {
            System.out.println("Room is not occupied.");
            return;
        }
        RoomServiceStaff staff = roomServiceStaffController.getAvailableRoomServiceStaff();

        // Display menu and create order
        System.out.println("----- MENU -----");
        try {
            roomServiceController.printMenu();

            RoomServiceOrder order = roomServiceController.createOrder(roomNumber, scnr);

            roomServiceStaffController.assignOrder(staff, roomNumber + "_" + order.getOrderId());

            roomServiceStaffController.requestOrder(order, staff);

            kitchenController.prepareOrder(order, scnr);

            roomServiceStaffController.deliverOrder(order, staff);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            roomServiceStaffController.updateStaffStatus(String.valueOf(staff.getId()), "AVAILABLE");
        }
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
            KeyCard card = keyCardController.newKeyCard(booking, room);
            room.setStatus("OCCUPIED");
            room.setCurrentGuest(customer.getName());
            roomController.updateRoom(room);
            hotel.setNumAvailableRooms(hotel.getNumAvailableRooms() - 1);
            hotelController.updateHotel(hotel);

            System.out.println("Booking complete! Your booking ID is " + booking.getId() + ". Please remember this for checkout. " +
                    "Checkout date: " + booking.getCheckOutDate() + "Room number: " + room.getRoomNumber());
            System.out.println("Your access level is " + card.getAccessLevel());
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

    /*
     * Allows a customer to book a hotel room using their rewards points. If the customer is a rewards member, they can redeem their points for a discount on the room price.
     */
public void bookRoomWithRewards() throws IOException {
    Scanner scanner = new Scanner(System.in);

    Hotel hotel = hotelController.getHotel(1);

    System.out.println("\n\n----- BOOK A HOTEL ROOM WITH REWARDS -----\n");
    if (hotelController.isHotelSoldOut(hotel)) {
        System.out.println("Hotel is sold out!");
        return;
    }

    System.out.print("What room type? (Standard, Deluxe or Suite): ");
    String roomType = scanner.next();

    Room room = roomController.isARoomAvailableFromRequirements(roomType);
    if (room == null) {
        System.out.println("Could not find a room. Try again with different requirements.");
        return;
    }

    System.out.print("There is a room for $" + room.getPricePerNight() + " per night. Purchase? (y/n) ");
    char answer = scanner.next().charAt(0);

    if (answer == 'y') {
        System.out.print("How many nights? ");
        int nights;
        try {
            nights = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid nights!");
            return;
        }

        double totalPrice = nights * room.getPricePerNight();
        double finalPrice = totalPrice;
        double rewardsUsed = 0.0;
        int customerId = -1; // Declare customerId outside the if block

        System.out.print("Is the customer a rewards member? (yes/no): ");
        String isRewardsMember = scanner.next();

        if (isRewardsMember.equalsIgnoreCase("yes")) {
            System.out.print("Enter customer ID: ");
            customerId = scanner.nextInt();

            StorageHelper.DataStore<Map<String, Object>> customerStore = rewardsController.getCustomerStorageHelper().getStore("customers");
            Map<String, Object> customerData = customerStore.load(String.valueOf(customerId));

            if (customerData != null && customerData.containsKey("loyaltyProgramLevel") && customerData.containsKey("rewardsAvailable")) {
                double rewardsAvailable = (double) customerData.get("rewardsAvailable");

                System.out.print("Do they want to redeem their rewards? (yes/no): ");
                String redeemRewards = scanner.next();

                if (redeemRewards.equalsIgnoreCase("yes")) {
                    rewardsUsed = Math.min(rewardsAvailable, totalPrice);
                    finalPrice = totalPrice - rewardsUsed;

                    // Update the loyalty program with the new points
                    int pointsAccumulated = ((Number) customerData.get("pointsAccumulated")).intValue();
                    int pointsUsed = (int) (rewardsUsed / 0.5);
                    customerData.put("pointsAccumulated", pointsAccumulated - pointsUsed);
                    customerData.put("rewardsAvailable", (pointsAccumulated - pointsUsed) * 0.5);

                    customerStore.save(String.valueOf(customerId), customerData);
                }
            } else {
                System.out.println("Customer is not a rewards member.");
            }
        }

        System.out.println("Price before rewards: $" + totalPrice);
        System.out.println("Rewards used: $" + rewardsUsed);
        System.out.println("Final price after rewards: $" + finalPrice);
        

        Customer customer = customerController.getCustomer(customerId);
        Booking booking = bookingController.bookRoom(room, nights, customer);
        if (booking == null) return;
        keyCardController.newKeyCard(booking, room);
        room.setStatus("OCCUPIED");
        room.setCurrentGuest(customer.getName());
        roomController.updateRoom(room);
        hotel.setNumAvailableRooms(hotel.getNumAvailableRooms() - 1);
        hotelController.updateHotel(hotel);

        System.out.println("Booking confirmed for customer ID: " + customer.getId());
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Check-out date: " + booking.getCheckOutDate());
    }
}

    /*
     * Demonstrates the profit cycle by showing the franchise owner's pay and the CEO's pay for a booking that has been placed.
     */
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

    /*
     * Signs a franchise agreement by creating a new FranchiseAgreement object and storing it in the database.
     */
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

    /*
     * Sends a mass email to all customers in the database.
     */
public void sendMassEmail() throws IOException {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter email subject: ");
    String subject = scanner.nextLine();

    System.out.print("Enter email message: ");
    String message = scanner.nextLine();

    EmailController emailController = new EmailController("hotel_data");
    emailController.sendMassEmail(subject, message);
}

    // Use case Vending Machine
    private void useVendingMachine(Scanner scanner) throws IOException {
        VendingMachine machine = vendingController.getHotelVendingMachine();
        System.out.println("---- VENDING MACHINE ----\n");

        System.out.print("Please insert between $0.00 and $50.00. Any unused money will be returned at the end" +
                "of this transaction.\n$");

        double money = scanner.nextDouble();
        vendingController.addMoney(machine, money);

        while(true) {
            System.out.println("Current balance: $" + machine.getBalance());
            System.out.println("Choose a slot: ");
            machine.displaySlots();
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int key;
            try {
                key = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Must enter a number.\n\n");
                scanner.nextLine();
                continue;
            }
            if(key == 0) {
                break;
            }
            vendingController.purchaseItem(key, machine);
        }

        System.out.println("Thanks for using! Returning $" + machine.getBalance());
    }

    public void accessFacility(Scanner scanner, boolean isManager) throws IOException {
        System.out.println("\n----- ACCESS FACILITY -----\n");

        Booking booking;
        if(!isManager) {
            System.out.print("Enter booking ID: ");
            int id = scanner.nextInt();
            booking = bookingController.getBooking(id);
            if (booking == null) {
                System.out.println("No booking found.");
                return;
            }
        } else {
            System.out.println("Access granted!");
            return;
        }
        KeyCard keyCard = keyCardController.getKeyCardFromBooking(booking.getId());

        System.out.println("Choose facility/room:");
        System.out.println("1. Room");
        System.out.println("2. Vending");
        System.out.println("3. Staff Room");

        System.out.print("Choice: ");
        int choice = scanner.nextInt();

        Facility facility = null;

        switch(choice) {
            case 1:
                System.out.println("Enter room number to access: ");
                int roomNum = scanner.nextInt();
                facility = roomController.getRoom(roomNum);
                break;
            case 2:
                facility = vendingController.getHotelVendingMachine();
                break;
            case 3:
                System.out.println("Access denied.");
            default:
                System.out.println("Invalid action.");
        }

        if(facilityController.checkAccessLevel(facility, keyCard, booking)) {
            System.out.println("Access granted!");
        } else {
            System.out.println("Access denied.");
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
                System.out.println("6. Applicant");
                System.out.println("7. Grounds Keeper");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int useCaseNumber = scanner.nextInt();
                if (useCaseNumber == 0) {
                    break;
                }
                useCases.runUseCaseByActor(useCaseNumber, scanner);
            }
        }
    }
}