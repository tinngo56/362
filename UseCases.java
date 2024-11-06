import Controllers.*;
import Models.*;

import java.awt.print.Book;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UseCases {
    private final BookingController bookingController;
    private final CustomerController customerController;
    private final PaymentController paymentController;
    private final RoomController roomController;
    private final HotelController hotelController;

    public UseCases(String baseDirectory) throws IOException {
        this.bookingController = new BookingController(baseDirectory);
        this.customerController = new CustomerController(baseDirectory);
        this.hotelController = new HotelController(baseDirectory);
        this.paymentController = new PaymentController(baseDirectory);
        this.roomController = new RoomController(baseDirectory);
    }

    public void runUseCase(int useCaseNumber) throws IOException {
        switch (useCaseNumber) {
            case 1:
                BookHotelRoom();
                break;
            case 2:
                CheckOutOfHotelRoom();
                break;
            case 3:
                updateBooking();
                break;
            case 4:
                deleteBooking();
                break;
            case 5:
                createCustomer();
                break;
            case 6:
                getCustomer();
                break;
            case 7:
                updateCustomer();
                break;
            case 8:
                deleteCustomer();
                break;
            case 9:
                createPaymentMethod();
                break;
            case 10:
                getPaymentMethod();
                break;
            default:
                System.out.println("Invalid use case number.");
        }
    }

    // Use Case 1
    private void BookHotelRoom() throws IOException {
        Scanner scanner = new Scanner(System.in);

        Hotel hotel = hotelController.getHotel(1);
        List<Room> rooms = roomController.getAllRooms();
//        rooms.add(new Room(101, "Deluxe", 150.0, "AVAILABLE", "", ""));
//        rooms.add(new Room(102, "Standard", 100.0, "AVAILABLE", "", ""));
//        rooms.add(new Room(201, "Standard", 125.0, "AVAILABLE", "", ""));
//        rooms.add(new Room(301, "Suite", 300.0, "AVAILABLE", "", ""));
//        rooms.add(new Room(202, "Standard", 150.0, "AVAILABLE", "", ""));

        System.out.println("\n\n----- BOOK A HOTEL ROOM -----\n");
        if(hotel.getNumAvailableRooms() <= 0) {
            System.out.println("Hotel is sold out!");
            return;
        }

        boolean didBook = false;

        System.out.print("What room type? (Standard, Deluxe or Suite):  ");
        String roomType = scanner.next();

        for(Room room : rooms) {
            // if room is found matching request
            if(room.getRoomType().equals(roomType) && Objects.equals(room.getStatus(), "AVAILABLE")) {
                System.out.print("There is a room for $" + room.getPricePerNight() + " per night. " +
                        "Purchase? (y/n) ");
                char answer = scanner.next().charAt(0);
                if(answer == 'y') {
                    System.out.print("How many nights? ");
                    int nights = scanner.nextInt();
                    LocalDate checkoutDate = LocalDate.now().plusDays(nights);

                    System.out.println("");

                    room.setStatus("OCCUPIED");
                    room.setCurrentGuest("Bob");

                    int bookingId = bookingController.getNumOfBookings() + 1;
                    Booking booking = new Booking(bookingId, LocalDate.now().toString(),
                            checkoutDate.toString(), room.getPricePerNight() * nights, "Complete",
                            room.getRoomNumber(), false);

                    bookingController.createBooking(booking);
                    roomController.updateRoom(room);
                    hotel.setNumAvailableRooms(hotel.getNumAvailableRooms() - 1);
                    hotelController.updateHotel(hotel);

                    System.out.println("Booking complete! Your booking ID is " + booking + ". Please remember this for checkout. " +
                            "Checkout date: " + checkoutDate);
                    didBook = true;
                    break;
                }
            }
        }
        if(!didBook) {
            System.out.println("Could not find a room. Try again with different requirements.");
        }
    }

    // Use Case 2
    private void CheckOutOfHotelRoom() throws IOException {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = hotelController.getHotel(1);

        System.out.println("\n\n----- CHECK OUT -----\n");
        if(hotel.getNumAvailableRooms() >= hotel.getRoomCount()) {
            System.out.println("All rooms are not checked in.");
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

        System.out.print("\nWould you like to extend your stay? (y/n) ");
        char extend = scanner.next().charAt(0);
        if(extend == 'y') {
            System.out.print("How many nights? ");
            int nights = scanner.nextInt();
            LocalDate checkoutDate = LocalDate.now().plusDays(nights);
            booking.setCheckOutDate(checkoutDate.toString());
            booking.setTotalPrice(booking.getTotalPrice() + (room.getPricePerNight() * nights));
            bookingController.updateBooking(booking);
            System.out.println("Your stay has been extended to " + checkoutDate);
            return;
        }

        if(!Objects.equals(booking.getCheckOutDate(), LocalDate.now().toString())) {
            System.out.println("WARNING: You are checking out early (booking checkout date is " + booking.getCheckOutDate() + "). " +
                    "You will still be required to pay your remaining nights.");
            System.out.print("Do you want to do this? (y/n) ");
            char answer = scanner.next().charAt(0);
            if(answer == 'y') {

                room.setStatus("NEEDS CLEANING");
                room.setCurrentGuest("");
                hotel.setNumAvailableRooms(hotel.getNumAvailableRooms() + 1);
                roomController.updateRoom(room);
                bookingController.deleteBooking(bookingId);
                hotelController.updateHotel(hotel);
                System.out.println("Successfully checked out. Thanks for staying at " + hotel.getName());
            }
        }

    }

    private void createBooking() throws IOException {

    }

    private void getBooking() throws IOException {
        Booking booking = bookingController.getBooking(1);
        if (booking != null) {
            System.out.println("Booking details: " + booking.toMap());
        } else {
            System.out.println("Booking not found.");
        }
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
                System.out.println("Select a use case to run:");
                System.out.println("1. Book Hotel Room");
                System.out.println("2. Check Out Of Hotel Room");
                System.out.println("3. Update Booking");
                System.out.println("4. Delete Booking");
                System.out.println("5. Create Customer");
                System.out.println("6. Get Customer");
                System.out.println("7. Update Customer");
                System.out.println("8. Delete Customer");
                System.out.println("9. Create Payment Method");
                System.out.println("10. Get Payment Method");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int useCaseNumber = scanner.nextInt();
                if (useCaseNumber == 0) {
                    break;
                }
                useCases.runUseCase(useCaseNumber);
            }
        }
    }
}