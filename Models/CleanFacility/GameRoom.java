package Models.CleanFacility;

import java.util.Scanner;

public class GameRoom extends HotelFacility {

    boolean cleanGameEquipment;
    boolean vacuumFloor;
    boolean sanitizeHighTouchSurfaces;
    String name = "game room";

    public GameRoom(int id) {
        super(id);
    }

    @Override
    public String getCleaningInstructions() {
        StringBuilder instructions = new StringBuilder();
        instructions.append("Game Room Cleaning Instructions:\n");
        instructions.append("- Clean and organize all game equipment.\n");
        instructions.append("- Vacuum the floor thoroughly.\n");
        instructions.append("- Sanitize high-touch surfaces like controllers and tables.\n");
        return instructions.toString();
    }

    @Override
    public void createCleaningReport(Scanner scnr, int daysBetweenCleaning) {
        super.createCleaningReport(scnr, daysBetweenCleaning);

        System.out.println(getCleaningInstructions());

        System.out.print("Was the game equipment cleaned?: ");
        cleanGameEquipment = nextBoolean(scnr);

        System.out.print("Was the floor vacuumed?: ");
        vacuumFloor = nextBoolean(scnr);

        System.out.print("Were high-touch surfaces sanitized?: ");
        sanitizeHighTouchSurfaces = nextBoolean(scnr);

        System.out.println("\nGame Room Cleaning Report:");
        System.out.println("Game equipment cleaned: " + cleanGameEquipment);
        System.out.println("Floor vacuumed: " + vacuumFloor);
        System.out.println("High-touch surfaces sanitized: " + sanitizeHighTouchSurfaces);
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