package Models.CleanFacility;

import Models.Mappable;

import java.time.LocalDate;
import java.util.Scanner;

public abstract class HotelFacility extends Mappable implements Facility {
    protected String issues;
    private LocalDate lastCleaned;
    int id;

    //Used sanitation solution
    private boolean sanitizeSurfaces;

    /**
     * sets lastCleaned to today
     */
    public HotelFacility(int id){
        this.id = id;
        lastCleaned = LocalDate.now();
    }

    @Override
    public LocalDate getNextCleaningDate(int daysBetweenCleaning){
        return lastCleaned.plusDays(daysBetweenCleaning);
    }

    /**
     * Updates last cleaned to today and prompts for any issues to note
     * @param scnr used for input
     * @param daysBetweenCleaning used to schedual next cleaning
     */
    @Override
    public void createCleaningReport(Scanner scnr, int daysBetweenCleaning) {
        LocalDate neededCleaning = getNextCleaningDate(daysBetweenCleaning);

        System.out.println("Cleaning Report:");
        System.out.println("Got cleaned on last: " + getLastCleaned());
        System.out.println("Need cleaning by: " + neededCleaning);
        lastCleaned = LocalDate.now();
        System.out.println("Next cleaning was scheduled for: " + getNextCleaningDate(daysBetweenCleaning));

        System.out.println("Were there any issues during cleaning? (y,n): ");
        String s = scnr.nextLine().trim().toLowerCase();
        boolean issue = s.equals("y");
        if (issue) {
            System.out.print("Please describe the issues: ");
            issues = scnr.nextLine();
        } else {
            issues = "None";
        }

        System.out.println(getCleaningInstructions());
    }

    public int getId(){
        return id;
    }

    public LocalDate getLastCleaned(){
        return lastCleaned;
    }

    private boolean nextBoolean(Scanner scanner) {
        while (true) {
            System.out.println("Enter 'y' for yes or 'n' for no: ");
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
