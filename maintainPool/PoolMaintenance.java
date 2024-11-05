package maintainPool;

import util.FileWriterUtility;
import util.CardScan;

import java.util.Scanner;

public class PoolMaintenance extends FacilityMaintenance {

    private FileWriterUtility fileWriter;
    private ChemicalReading chemicalReading;
    private Supplies supplies;
    private boolean maintenanceSuccess;
    private String errorMessage;
    private boolean cleaningSuppliesLow;

    public PoolMaintenance(String logFilePath) {
        this.fileWriter = new FileWriterUtility(logFilePath);
        this.chemicalReading = new ChemicalReading();
        this.supplies = new Supplies();
        this.maintenanceSuccess = true;
        this.errorMessage = "";
        this.cleaningSuppliesLow = false;
    }

    @Override
    public void inspectEquipment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Is the equipment in good condition? (yes/no): ");
        String equipmentStatus = scanner.nextLine();

        if (equipmentStatus.equalsIgnoreCase("no")) {
            maintenanceSuccess = false;
            errorMessage += "Equipment malfunction detected. ";
            fileWriter.writeToFile("Equipment inspection failed due to malfunction.");
        } else {
            fileWriter.writeToFile("Inspected pool equipment; all in good condition.");
        }
    }

    public void inspectEquipmentForTesting(boolean equipmentOK) {
        if (!equipmentOK) {
            maintenanceSuccess = false;
            errorMessage += "Equipment malfunction detected. ";
            fileWriter.writeToFile("Equipment inspection failed due to malfunction.");
        } else {
            fileWriter.writeToFile("Inspected pool equipment; all in good condition.");
        }
    }

    @Override
    public void checkConditionLevels() {
        Scanner scanner = new Scanner(System.in);

        // chlorine level
        System.out.print("Enter chlorine level: ");
        float chlorineLevel = scanner.nextFloat();
        chemicalReading.setChemicalReading("Chlorine", chlorineLevel);

        // pH level
        System.out.print("Enter pH level: ");
        float pHLevel = scanner.nextFloat();
        chemicalReading.setChemicalReading("pH", pHLevel);

        // alkalinity level
        System.out.print("Enter alkalinity level: ");
        float alkalinityLevel = scanner.nextFloat();
        chemicalReading.setChemicalReading("Alkalinity", alkalinityLevel);

        String chemicalIssues = chemicalReading.isWithinAcceptableRange();
        if (chemicalIssues != null) {
            maintenanceSuccess = false;
            errorMessage += chemicalIssues;
            fileWriter.writeToFile("Chemical issues detected: " + chemicalIssues);
        } else {
            fileWriter.writeToFile("All chemical levels are within acceptable ranges.");
        }
    }

    public void checkConditionLevelsForTesting(float chlorine, float pH, float alkalinity) {
        chemicalReading.setChemicalReading("Chlorine", chlorine);
        chemicalReading.setChemicalReading("pH", pH);
        chemicalReading.setChemicalReading("Alkalinity", alkalinity);

        String chemicalIssues = chemicalReading.isWithinAcceptableRange();
        if (chemicalIssues != null) {
            maintenanceSuccess = false;
            errorMessage += chemicalIssues;
            fileWriter.writeToFile("Chemical issues detected: " + chemicalIssues);
        } else {
            fileWriter.writeToFile("All chemical levels are within acceptable ranges.");
        }
    }

    @Override
    public void cleanFacility() {
        System.out.println("Cleaning pool area, seating, and sanitizing high-contact surfaces.");
        fileWriter.writeToFile("Performed cleaning and sanitization in the pool area.");
        //TODO
        boolean cleaningOK = true;
        if (!cleaningOK) {
            maintenanceSuccess = false;
            errorMessage += "Cleaning incomplete due to low supplies. ";
            fileWriter.writeToFile("Cleaning incomplete due to low supplies.");
        }
    }

    @Override
    public void checkSupplyLevels() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter towels stock: ");
        int towelStock = scanner.nextInt();
        supplies.setSupplyAmount("Towels", towelStock);

        System.out.print("Enter cleaning solution stock: ");
        int cleaningSolutionStock = scanner.nextInt();
        supplies.setSupplyAmount("CleaningSolution", cleaningSolutionStock);

        if (towelStock < 10 || cleaningSolutionStock < 10) {
            cleaningSuppliesLow = true;
            errorMessage += "Cleaning supplies below threshold. ";
            fileWriter.writeToFile("Supplies below threshold, restock needed.");
        } else {
            fileWriter.writeToFile("Sufficient supplies in stock.");
        }
    }

    public void checkSupplyLevelsForTesting(int towels, int cleaningSolution) {
        supplies.setSupplyAmount("Towels", towels);
        supplies.setSupplyAmount("CleaningSolution", cleaningSolution);

        if (towels < 10 || cleaningSolution < 10) {
            cleaningSuppliesLow = true;
            errorMessage += "Cleaning supplies below threshold. ";
            fileWriter.writeToFile("Supplies below threshold, restock needed.");
        } else {
            fileWriter.writeToFile("Sufficient supplies in stock.");
        }
    }

    @Override
    public void exitFacility() {
        super.exitFacility();

        if (maintenanceSuccess && cleaningSuppliesLow) {
            errorMessage += "Cleaning supplies are low, consider restocking.";
        }
        fileWriter.writeToPoolSave(maintenanceSuccess, errorMessage);
    }
}