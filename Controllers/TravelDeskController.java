package Controllers;

import Models.Excursions.Excursion;
import Models.Excursions.ExcursionBooking;
import Models.Excursions.TravelDesk;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TravelDeskController {

    private StorageHelper storage;
    private final String EXCURSION_STORAGE = "excursions";
    private final String EXCURSION_BOOKING_STORAGE = "excursion_bookings";

    private TravelDesk travelDesk;

    public TravelDeskController(String baseDirectory) throws IOException {
        this.storage = new StorageHelper(baseDirectory, EXCURSION_STORAGE);
        this.storage.initializeStores(EXCURSION_BOOKING_STORAGE);
        this.travelDesk = new TravelDesk(getAllExcursions());
    }

    public void createExcursion(Excursion excursion) throws IOException {
        storage.getStore(EXCURSION_STORAGE).save(excursion.getName(), excursion.toMap());
    }

    public Excursion getExcursion(String excursionName) throws IOException {
        Map<String, Object> data = storage.getStore(EXCURSION_STORAGE).load(excursionName);
        return data != null ? new Excursion().fromMap(data) : null;
    }

    public List<Excursion> getAllExcursions() throws IOException {
        List<Excursion> excursions = new ArrayList<>();
        List<Map<String, Object>> data = storage.getStore(EXCURSION_STORAGE).loadAll();
        for(Map<String, Object> excursion : data) {
            excursions.add(new Excursion().fromMap(excursion));
        }
        return excursions;
    }

    public List<Excursion> getAvailableExcursions() throws IOException {
        return travelDesk.getAvailableExcursions();
    }

    public void displayAvailableExcursions() throws IOException {
        travelDesk.showExcursions();
    }

    public void deleteExcursion(String excursionName) throws IOException {
        storage.getStore(EXCURSION_STORAGE).delete(excursionName);
    }

    public void addExcursion() throws IOException {
        String name;
        double price;
        String description;
        int slots;

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter name of excursion: ");
        name = scanner.next();

        if(getExcursion(name) != null) {
            System.out.println("\nERROR: Excursion already exists!\n");
            return;
        }

        System.out.print("Enter price of excursion: ");
        price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter description of excursion: ");
        description = scanner.nextLine();
        System.out.print("Enter number of available slots for excursion: ");
        slots = scanner.nextInt();

        Excursion excursion = new Excursion(name, price, description, slots);
        createExcursion(excursion);
        travelDesk.addExcursion(excursion);

        System.out.println("\nExcursion created.\n");
    }

    public void displayAllExcursions() throws IOException {
        List<Excursion> excursions = getAllExcursions();
        for(Excursion excursion : excursions) {
            System.out.println(excursion);
        }
    }

    public void removeExcursion() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter name: ");
        String name = scanner.nextLine();

        if(getExcursion(name) == null) {
            System.out.println("\nERROR: Excursion does not exist!\n");
            return;
        }

        deleteExcursion(name);
        System.out.println("\nExcursion " + name + " removed.\n");
    }

    private void createBooking(ExcursionBooking booking) throws IOException {
        storage.getStore(EXCURSION_BOOKING_STORAGE).save(String.valueOf(booking.getId()), booking.toMap());
    }

    private ExcursionBooking getBooking(int id) throws IOException {
        Map<String, Object> data = storage.getStore(EXCURSION_BOOKING_STORAGE).load(String.valueOf(id));
        return data != null ? new ExcursionBooking().fromMap(data) : null;
    }

    private List<ExcursionBooking> getAllBookings() throws IOException {
        List<Map<String, Object>> data = storage.getStore(EXCURSION_BOOKING_STORAGE).loadAll();
        List<ExcursionBooking> bookings = new ArrayList<>();
        for(Map<String, Object> excursion : data) {
            bookings.add(new ExcursionBooking().fromMap(excursion));
        }
        return bookings;
    }

    private void deleteBooking(int id) throws IOException {
        storage.getStore(EXCURSION_BOOKING_STORAGE).delete(String.valueOf(id));
    }

    public ExcursionBooking newBooking(String excursionName, String date, int numPeople) throws IOException {
        Excursion excursion = getExcursion(excursionName);
        if(excursion == null) {
            System.out.println("\nERROR: Excursion does not exist!\n");
            return null;
        }

        for(int i = 0; i < numPeople; i++) {
            excursion.bookSlot();
        }

        ExcursionBooking booking = new ExcursionBooking(getAllBookings().size() + 1, date, excursionName, numPeople);
        createBooking(booking);
        createExcursion(excursion);
        updateTravelDesk();

        return booking;
    }

    public void removeBooking(int id) throws IOException {
        ExcursionBooking booking = getBooking(id);
        if(booking == null) {
            System.out.println("\nERROR: Booking does not exist!\n");
            return;
        }

        Excursion excursion = getExcursion(booking.getExcursionName());
        excursion.setAvailableSlots(excursion.getAvailableSlots() + booking.getBookingSize());
        createExcursion(excursion);

        deleteBooking(id);
        updateTravelDesk();
    }

    public void DisplayAllBookings() throws IOException {
        List<ExcursionBooking> bookings = getAllBookings();
        for(ExcursionBooking booking : bookings) {
            System.out.println(booking);
        }
    }

    public void updateTravelDesk() throws IOException {
        this.travelDesk = new TravelDesk(getAllExcursions());
    }

}
