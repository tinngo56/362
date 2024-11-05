import Controllers.*;
import Models.*;
import java.io.IOException;
import java.util.*;

public class UseCases {
    private final BookingController bookingController;
    private final CustomerController customerController;
    private final PaymentController paymentController;
    private final HotelController hotelController;

    public UseCases(String baseDirectory) throws IOException {
        this.bookingController = new BookingController(baseDirectory);
        this.customerController = new CustomerController(baseDirectory);
        this.hotelController = new HotelController(baseDirectory);
        this.paymentController = new PaymentController(baseDirectory);
    }

    public void runUseCase(int useCaseNumber) throws IOException {
        switch (useCaseNumber) {
            case 1:
                createBooking();
                break;
            case 2:
                getBooking();
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

    private void createBooking() throws IOException {
        Booking booking = new Booking(1, "2023-11-01", "2023-11-05", 500.0, "PAID", new Room(101, "STANDARD", 150.0, "AVAILABLE", null, "2023-10-31"));
        bookingController.createBooking(booking);
        System.out.println("Booking created successfully.");
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
        Booking booking = new Booking(1, "2023-11-01", "2023-11-06", 600.0, "PAID", new Room(101, "STANDARD", 150.0, "AVAILABLE", null, "2023-10-31"));
        bookingController.updateBooking(booking);
        System.out.println("Booking updated successfully.");
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
                System.out.println("1. Create Booking");
                System.out.println("2. Get Booking");
                System.out.println("3. Update Booking");
                System.out.println("4. Delete Booking");
                System.out.println("5. Create Customer");
                System.out.println("6. Get Customer");
                System.out.println("7. Update Customer");
                System.out.println("8. Delete Customer");
                System.out.println("9. Create Payment Method");
                System.out.println("10. Get Payment Method");
                System.out.println("0. Exit");

                int useCaseNumber = scanner.nextInt();
                if (useCaseNumber == 0) {
                    break;
                }
                useCases.runUseCase(useCaseNumber);
            }
        }
    }
}