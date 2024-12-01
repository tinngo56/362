package Models.CleanFacility;

import java.util.Scanner;

public class Pool extends HotelFacility {

    boolean mopFloor;
    boolean cleanTowels;
    boolean dryTowels;
    String name = "pool";


    public Pool(int id) {
        super(id);
    }

    @Override
    public String getCleaningInstructions() {
        StringBuilder instructions = new StringBuilder();
        instructions.append("Pool Cleaning Instructions:\n");
        instructions.append("- Mop the pool area floor using soap.\n");
        instructions.append("- Collect all used towels for washing.\n");
        instructions.append("- Dry the cleaned towels thoroughly before restocking.\n");
        return instructions.toString();
    }

    @Override
    public void createCleaningReport(Scanner scnr, int daysBetweenCleaning) {
        super.createCleaningReport(scnr, daysBetweenCleaning);

        System.out.println(getCleaningInstructions());

        System.out.print("Was the floor mopped?: ");
        mopFloor = nextBoolean(scnr);

        System.out.print("Were the towels cleaned?: ");
        cleanTowels = nextBoolean(scnr);

        System.out.print("Were the towels dried?: ");
        dryTowels = nextBoolean(scnr);

        System.out.println("\nPool Cleaning Report:");
        System.out.println("Floor mopped: " + mopFloor);
        System.out.println("Towels cleaned: " + cleanTowels);
        System.out.println("Towels dried: " + dryTowels);
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