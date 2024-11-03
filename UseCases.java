import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UseCases {
    public static void main(String[] args) {
        /*Room room = new Room(1, 2, "Deluxe", 150.0, "Available");
        Booking booking = new Booking(1, "2023-10-01", "2023-10-05", 600.0, "Paid", room);
        Hotel hotel = new Hotel(1, "Grand Hotel", "New York", 100, 5000.0, 20000.0, 4.5);

        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Hotel Name: " + hotel.getName());
        System.out.println("Room Type: " + booking.getRoom().getRoomType());*/

        Scanner scanner = new Scanner(System.in);

        Hotel hotel = new Hotel(1, "Grand Hotel", "New York", 5,
                5000.0, 20000.0, 4.5, 5);
        ArrayList<Room> rooms = GenerateRooms(hotel);

        while(true) {
            System.out.println("\nWelcome! Choose an actor: \n");
            PrintActors();

            System.out.print("Enter actor number: ");
            int actor = scanner.nextInt();

            switch (actor) {
                case 0:
                    System.exit(0);

                    // ACTOR: CUSTOMER
                case 1:
                    CustomerScenario(scanner, hotel, rooms);
                    break;

                default:
                    continue;
            }

        }

    }

    /**
     * Generates some pre-defined rooms
     * @param hotel The hotel in which the rooms belong to
     * @return List of rooms
     */
    public static ArrayList<Room> GenerateRooms(Hotel hotel) {
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1, 2, "Deluxe", 150.0, "Available", hotel));
        rooms.add(new Room(2, 1, "Standard", 100.0, "Available", hotel));
        rooms.add(new Room(3, 2, "Standard", 125.0, "Available", hotel));
        rooms.add(new Room(4, 2, "Suite", 300.0, "Available", hotel));
        rooms.add(new Room(5, 3, "Standard", 150.0, "Available", hotel));
        return rooms;
    }

    /**
     * Prints the actors for the system.
     */
    public static void PrintActors() {
        System.out.println("1. Customer");
        System.out.println("0 to exit.");
    }

    /**
     * Use cases for customer
     * @param scanner main loop scanner
     * @param hotel hotel for the use case
     * @param rooms list of rooms for the user case
     */
    public static void CustomerScenario(Scanner scanner, Hotel hotel, ArrayList<Room> rooms) {
        Customer customer = new Customer(1, "Bob", "bob@gmail.com", "Silver",
                "Card", 0);

        System.out.println("\nWhat would you like to do?");
        System.out.print("1. Book a hotel room\n2. Check out of hotel room\nEnter action: ");
        int action = scanner.nextInt();

        // BOOK HOTEL ROOM
        if(action == 1) {
            System.out.println("\n\n----- BOOK A HOTEL ROOM -----\n");
            if(hotel.getNumAvailableRooms() <= 0) {
                System.out.println("Hotel is sold out!");
                return;
            }

            boolean didBook = false;

            System.out.print("How many beds? ");
            int beds = scanner.nextInt();
            System.out.print("What room type? (Standard, Deluxe or Suite):  ");
            String roomType = scanner.next();

            for(Room room : rooms) {
                // if room is found matching request
                if(room.getRoomType().equals(roomType) && room.getBedCount() == beds) {
                    System.out.print("There is a room for $" + room.getPricePerNight() + " per night. " +
                            "Purchase? (y/n) ");
                    char answer = scanner.next().charAt(0);
                    if(answer == 'y') {
                        System.out.print("How many nights? ");
                        int nights = scanner.nextInt();
                        LocalDate checkoutDate = LocalDate.now().plusDays(nights);
                        Booking booking = new Booking(1, LocalDate.now().toString(), checkoutDate.toString(),
                                room.getPricePerNight() * nights, "Complete", room);
                        System.out.println("Booking complete! Checkout date: " + checkoutDate);
                        didBook = true;
                        break;
                    }
                }
            }
            if(!didBook) {
                System.out.println("Could not find a room. Try again with different requirements.");
            }

            // CHECK OUT OF HOTEL ROOM
        } else {
            System.out.println("\n\n----- CHECK OUT OF A HOTEL ROOM -----\n");


        }
    }
}