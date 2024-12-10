import java.util.Scanner;

import Controllers.ValetController;
import Models.ValetStaff;

public class TinUseCases {
    public static void main(String[] args) {
        try {
            // Initialize controller with data directory
            ValetController controller = new ValetController("./data");
            Scanner scanner = new Scanner(System.in);
            ValetStaff staff = controller.getAvailableValetStaff();
            System.out.println("\nStarting vehicle parking process...");
            controller.parkVehicle(staff, scanner);

            controller.retrieveVehicle(scanner, staff);

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}