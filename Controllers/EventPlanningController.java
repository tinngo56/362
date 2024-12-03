package Controllers;

import Models.Rent.RentableRoom;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EventPlanningController {
    private StorageHelper roomStorageHelper;
    private static final String ROOMS_NAME = "RentableRooms";
    private int nextRoomId = 1;

    public EventPlanningController(String baseDirectory) throws IOException {
        this.roomStorageHelper = new StorageHelper(baseDirectory, ROOMS_NAME);
        List<Map<String, Object>> roomsData = roomStorageHelper.getStore(ROOMS_NAME).loadAll();
        if (roomsData.isEmpty()) {
            // If no rooms exist, create a new default room
            createRoom(new RentableRoom(nextRoomId, "conference", 100, 20));
        }
    }

    // Create a new room
    public void createRoom(RentableRoom room) throws IOException {
        room.setId(nextRoomId);
        nextRoomId++;
        roomStorageHelper.getStore(ROOMS_NAME).save(String.valueOf(room.getId()), room.toMap());
    }

    // Retrieve a room by its ID
    public RentableRoom getRoom(int id) throws IOException {
        Map<String, Object> data = roomStorageHelper.getStore(ROOMS_NAME).load(String.valueOf(id));
        return data != null ? new RentableRoom().fromMap(data) : null;
    }

    // Update a room
    public void updateRoom(RentableRoom room) throws IOException {
        roomStorageHelper.getStore(ROOMS_NAME).save(String.valueOf(room.getId()), room.toMap());
        System.out.println("Room updated successfully: " + room.getName());
    }

    // Delete a room by its ID
    public void deleteRoom(int id) throws IOException {
        roomStorageHelper.getStore(ROOMS_NAME).delete(String.valueOf(id));
        System.out.println("Room with ID " + id + " deleted successfully.");
    }

    public void createBooking(Scanner scanner) throws IOException {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();  // Read the event name

        printAllRoomNamesWithIds();  // Display all room names with their IDs

        System.out.print("Enter room ID: ");
        int id = scanner.nextInt();  // Read the selected room ID
        scanner.nextLine();  // Consume the newline character

        RentableRoom room = getRoom(id);  // Retrieve the room by ID

        if (room == null) {
            System.out.println("Room does not exist. Please select a valid room.");
            return;  // End the flow here if the room doesn't exist
        }

        System.out.print("Enter date to book room (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();  // Read the date to book the room

        // Step 2: Check if the room is already booked on the selected date
        if (room.isBookedOnDate(dateString)) {
            System.out.println("Room is already booked for the selected date. Please choose a different date.");
            return;  // End the flow here if the room is already booked
        }

        System.out.print("Enter number of guests: ");
        int guests = scanner.nextInt();  // Read the number of guests
        scanner.nextLine();  // Consume the newline character

        // Step 3: Check if the number of guests exceeds the room's capacity
        if (guests <= 0) {
            System.out.println("Number of guests input is invalid. Please enter a valid number.");
            return;  // End the flow here if the number of guests is invalid
        }

        if (guests > room.getCapacity()) {
            System.out.println("Room capacity exceeded. Maximum capacity: " + room.getCapacity());
            return;  // End the flow here if the room cannot accommodate the guests
        }

        // Step 4: Confirm booking details with the customer
        double totalCost = calculateCost(room, guests);  // Calculate the total cost
        System.out.printf("Total cost: $%.2f%n", totalCost);  // Display the total cost
        System.out.print("Confirm booking? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();  // Ask for confirmation

        if ("yes".equals(confirmation)) {
            // Step 5: Book the room and update the records
            room.addBookingDate(dateString);  // Add the booking date
            updateRoom(room);  // Save the room with the new booking details

            System.out.println("Booking confirmed for event: " + eventName);
        } else {
            // Step 6: Customer decides not to proceed with the booking
            System.out.println("Booking cancelled.");
        }
    }


    // Cancel a booking for a room
    public void cancelBooking(Scanner scanner) throws IOException {
        printAllRoomNamesWithIds();

        System.out.print("Enter the room ID to cancel the booking: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        System.out.print("Enter the date of the booking to cancel (yyyy-MM-dd): ");
        String date = scanner.nextLine();

        // Step 1: Retrieve the room by its ID
        RentableRoom room = getRoom(roomId);

        if (room == null) {
            System.out.println("Room with ID " + roomId + " does not exist.");
            return;  // End the flow here if the room doesn't exist
        }

        // Step 2: Check if the room has the booking on the specified date
        if (!room.isBookedOnDate(date)) {
            System.out.println("No booking found for room " + roomId + " on the specified date.");
            return;  // End the flow here if the room is not booked for that date
        }

        // Step 3: Remove the booking date from the bookedDates list
        room.removeBookingDate(date);  // Remove the specified booking date

        // Step 4: Save the updated room state
        updateRoom(room);

        System.out.println("Booking for room ID " + roomId + " on date " + date + " has been successfully canceled.");
    }

    // Print all room names with their IDs
    public void printAllRoomNamesWithIds() throws IOException {
        List<Map<String, Object>> roomsData = roomStorageHelper.getStore(ROOMS_NAME).loadAll();  // Retrieve all rooms
        System.out.println("=== All Room Names and IDs ===");

        // Iterate over the rooms and print the ID and Name of each room
        for (Map<String, Object> roomData : roomsData) {
            RentableRoom room = new RentableRoom().fromMap(roomData);  // Convert data map to RentableRoom object
            System.out.println("Room ID: " + room.getId() + " | Room Name: " + room.getName());
        }
    }

    // Calculate the total cost for booking
    private double calculateCost(RentableRoom room, int guests) {
        return room.getRatePerPerson() * guests;
    }
}
