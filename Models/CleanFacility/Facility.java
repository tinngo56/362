package Models.CleanFacility;

import java.time.LocalDate;
import java.util.Scanner;

public interface Facility {
    String getCleaningInstructions();
    void createCleaningReport(Scanner scnr, int daysBetweenCleaning);
//    int getDaysBetweenCleaning();
    LocalDate getNextCleaningDate(int daysBetweenCleaning);
}
