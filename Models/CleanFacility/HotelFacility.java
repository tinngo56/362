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
     */
    @Override
    public void createCleaningReport(Scanner scnr, int daysBetweenCleaning) {
        LocalDate neededCleaning = getNextCleaningDate(daysBetweenCleaning);
        lastCleaned = LocalDate.now();

        System.out.println("Cleaning Report:");
        System.out.println("Need cleaning by: " + neededCleaning);
        System.out.println("Next cleaning was scheduled for: " + getNextCleaningDate(daysBetweenCleaning));

        System.out.print("Were there any issues during cleaning? (yes/no): ");
        String response = scnr.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            System.out.print("Please describe the issues: ");
            issues = scnr.nextLine();
        } else {
            issues = "None";
        }

        System.out.println(getCleaningInstructions());
    }
}
