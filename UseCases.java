public class UseCases {
    public static void main(String[] args) {
        Room room = new Room(1, 2, "Deluxe", 150.0, "Available");
        Booking booking = new Booking(1, "2023-10-01", "2023-10-05", 600.0, "Paid", room);
        Hotel hotel = new Hotel(1, "Grand Hotel", "New York", 100, 5000.0, 20000.0, 4.5);

        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Hotel Name: " + hotel.getName());
        System.out.println("Room Type: " + booking.getRoom().getRoomType());
    }
}