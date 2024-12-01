package Models.CleanFacility;

import java.util.Scanner;

public class Gym extends HotelFacility {

    boolean cleanEquipment;
    boolean mopFloor;
    boolean sanitizeArea;
    String name = "gym";

    public Gym(int id) {
        super(id);
    }

    @Override
    public String getCleaningInstructions() {
        StringBuilder instructions = new StringBuilder();
        instructions.append("Gym Cleaning Instructions:\n");
        instructions.append("- Wipe down and sanitize all gym equipment.\n");
        instructions.append("- Mop the gym floor thoroughly.\n");
        instructions.append("- Sanitize the workout area to prevent germ spread.\n");
        return instructions.toString();
    }

    @Override
    public void createCleaningReport(Scanner scnr, int daysBetweenCleaning) {
        super.createCleaningReport(scnr, daysBetweenCleaning);

        System.out.println(getCleaningInstructions());

        System.out.print("Was the equipment cleaned?: ");
        cleanEquipment = nextBoolean(scnr);

        System.out.print("Was the floor mopped?: ");
        mopFloor = nextBoolean(scnr);

        System.out.print("Was the workout area sanitized?: ");
        sanitizeArea = nextBoolean(scnr);

        System.out.println("\nGym Cleaning Report:");
        System.out.println("Equipment cleaned: " + cleanEquipment);
        System.out.println("Floor mopped: " + mopFloor);
        System.out.println("Area sanitized: " + sanitizeArea);
    }
    private boolean nextBoolean(Scanner scanner) {
        while (true) {
            System.out.print("Enter 'y' for yes or 'n' for no: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }
}
