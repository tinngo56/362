package Models.CleanFacility;

import java.util.Scanner;

public class ConferenceRoom extends HotelFacility {

    boolean cleanTables;
    boolean vacuumFloor;
    boolean organizeChairs;
    String name = "conference room";

    public ConferenceRoom(int id) {
        super(id);
    }

    @Override
    public String getCleaningInstructions() {
        StringBuilder instructions = new StringBuilder();
        instructions.append("Conference Room Cleaning Instructions:\n");
        instructions.append("- Clean and sanitize all tables and surfaces.\n");
        instructions.append("- Vacuum the floor to ensure cleanliness.\n");
        instructions.append("- Arrange chairs neatly for the next event.\n");
        return instructions.toString();
    }

    @Override
    public void createCleaningReport(Scanner scnr, int daysBetweenCleaning) {
        super.createCleaningReport(scnr, daysBetweenCleaning);

        System.out.println(getCleaningInstructions());

        System.out.print("Were the tables cleaned?: ");
        cleanTables = nextBoolean(scnr);

        System.out.print("Was the floor vacuumed?: ");
        vacuumFloor = nextBoolean(scnr);

        System.out.print("Were the chairs organized?: ");
        organizeChairs = nextBoolean(scnr);

        System.out.println("\nConference Room Cleaning Report:");
        System.out.println("Tables cleaned: " + cleanTables);
        System.out.println("Floor vacuumed: " + vacuumFloor);
        System.out.println("Chairs organized: " + organizeChairs);
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
