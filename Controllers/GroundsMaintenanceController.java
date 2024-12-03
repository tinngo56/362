package Controllers;

import Models.Grounds.Grounds;
import Storage.StorageHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GroundsMaintenanceController {

    private final StorageHelper storageHelper;
    private final String GROUNDS_INSPECTION_STORE_NAME = "groundsInspections"; // Store name
    private int nextId;

    public GroundsMaintenanceController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, GROUNDS_INSPECTION_STORE_NAME);
        this.nextId = 0;
    }

    public void createGroundsInspection(Grounds groundsInspection) throws IOException {
        groundsInspection.setId(nextId);
        storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).save(String.valueOf(groundsInspection.getId()), groundsInspection.toMap());
    }

    public Grounds getGroundsInspection(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).load(String.valueOf(id));
        return data != null ? new Grounds().fromMap(data) : null;
    }

    public void updateGroundsInspection(Grounds groundsInspection) throws IOException {
        storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).save(String.valueOf(groundsInspection.getId()), groundsInspection.toMap());
    }

    public void deleteGroundsInspection(int id) throws IOException {
        storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).delete(String.valueOf(id));
    }

    public void cancelGroundsInspection(Scanner scanner) throws IOException {
        System.out.print("Enter the ID of the Grounds Inspection to cancel: ");
        int id = getIntInput(scanner, "ID: ");

        // Fetch the inspection to ensure it exists
        Grounds inspection = getGroundsInspection(id);
        if (inspection == null) {
            System.out.println("No Grounds Inspection found with ID: " + id);
            return;
        }

        System.out.println("Are you sure you want to delete this Grounds Inspection? (yes/no)");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            deleteGroundsInspection(id); // Deletes the inspection
            System.out.println("Grounds Inspection with ID " + id + " has been successfully canceled.");
        } else {
            System.out.println("Cancellation aborted.");
        }
    }


    public void createAndSaveGroundsInspectionFromInput(Scanner scanner) throws IOException {
        System.out.println("=== Create New Grounds Inspection ===");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);

        printInspectionChecksGrass();
        double grassHeight = getDoubleInput(scanner, "Enter current grass height (in cm): ");

        printInspectionChecksBushes();
        double bushLength = getDoubleInput(scanner, "Enter average bush length (in cm): ");

        printInspectionChecksTrees();
        int treeCount = getIntInput(scanner, "Enter the number of trees: ");

        printInspectionChecksWeeds();
        int weedCount = getIntInput(scanner, "Enter the number of weeds: ");

        this.nextId = nextId + 1;
        Grounds inspection = new Grounds(nextId, date, grassHeight, bushLength, treeCount, weedCount, "");
        createGroundsInspection(inspection);
        System.out.println("Ground inspection made with id of : " + nextId);
    }

        public void modifyGroundsInspection(Scanner scanner) throws IOException {
        System.out.print("Enter Grounds Inspection ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Grounds inspection = getGroundsInspection(id);

        if (inspection != null) {
            System.out.println("=== Modify Grounds Inspection ===");
            System.out.println("Current Grass Height: " + inspection.getGrassHeight() + " cm");
            inspection.setGrassHeight(getDoubleInput(scanner, "Enter new grass height (in cm): "));

            System.out.println("Current Bush Length: " + inspection.getBushLength() + " cm");
            inspection.setBushLength(getDoubleInput(scanner, "Enter new bush length (in cm): "));

            System.out.println("Current Tree Count: " + inspection.getTreeCount());
            inspection.setTreeCount(getIntInput(scanner, "Enter new tree count: "));

            System.out.println("Current Weed Count: " + inspection.getWeedCount());
            inspection.setWeedCount(getIntInput(scanner, "Enter new weed count: "));

            updateGroundsInspection(inspection);
            System.out.println("Grounds inspection updated successfully.");
        } else {
            System.out.println("Inspection not found.");
        }
    }



    public void generateInspectionReport() throws IOException {
        List<Map<String, Object>> allInspections = storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).loadAll();
        System.out.println("=== Grounds Maintenance Inspection Report ===");
        for (Map<String, Object> data : allInspections) {
            Grounds inspection = new Grounds().fromMap(data);
            System.out.println(inspection); // Uses the updated toString method
        }
    }



    private double getDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public void logUnexpectedIssues(Scanner scanner) throws IOException {
        System.out.print("Enter Grounds Inspection ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Grounds inspection = getGroundsInspection(id);
        if (inspection == null) {
            System.out.println("Inspection not found for the given ID.");
            return;
        }

        System.out.println("=== Log Unexpected Issues ===");
        System.out.println("Current Notes: " + (inspection.getNotes() == null ? "None" : inspection.getNotes()));

        System.out.print("Enter details about the issue: ");
        String issueDetails = scanner.nextLine();

        System.out.print("Mark the task as completed now? (yes/no): ");
        String isCompleted = scanner.nextLine().trim().toLowerCase();

        String status;
        if ("yes".equals(isCompleted)) {
            status = "Task completed: ";
            System.out.println("Issue logged as completed.");
        } else {
            status = "Task scheduled for later: ";
            System.out.println("Issue logged for later resolution.");
        }

        // Append the new issue to the existing notes
        String updatedNotes = (inspection.getNotes() == null ? "" : inspection.getNotes() + "\n")
                + status + issueDetails;
        inspection.setNotes(updatedNotes);

        updateGroundsInspection(inspection);
        System.out.println("Updated notes saved successfully.");
    }


    private void printInspectionChecksGrass() {
        System.out.println("\n=== Grass Inspection Checks ===");
        System.out.println("1. Measure grass height in multiple areas.");
        System.out.println("2. Check for signs of overgrowth or unevenness.");
        System.out.println("3. Ensure grass is healthy and free of pests.");
    }

    private void printInspectionChecksBushes() {
        System.out.println("\n=== Bush Inspection Checks ===");
        System.out.println("1. Measure the average length of bushes.");
        System.out.println("2. Check for overgrowth obstructing pathways.");
        System.out.println("3. Inspect for signs of disease or pests.");
    }

    private void printInspectionChecksTrees() {
        System.out.println("\n=== Tree Inspection Checks ===");
        System.out.println("1. Count the total number of trees.");
        System.out.println("2. Check for dead or overgrown branches.");
        System.out.println("3. Inspect for pest infestations or signs of disease.");
    }

    private void printInspectionChecksWeeds() {
        System.out.println("\n=== Weed Inspection Checks ===");
        System.out.println("1. Count the number of weeds in key areas.");
        System.out.println("2. Check for weed growth near pathways or flowerbeds.");
        System.out.println("3. Assess the need for weeding or herbicide application.");
    }

    public void validateAndReplaceGroundsLog(Scanner scanner) throws IOException {
        List<Map<String, Object>> allInspections = storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).loadAll();

        if (allInspections.isEmpty()) {
            System.out.println("No previous logs found.");
            return;
        }

        System.out.println("=== Previous Grounds Inspection Logs ===");
        for (Map<String, Object> data : allInspections) {
            Grounds inspection = new Grounds().fromMap(data);
            System.out.println(inspection);
        }

        System.out.print("\nDoes the state of the grounds make sense? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("no".equals(response)) {
            System.out.print("Enter the ID of the log to replace: ");
            int idToReplace = getIntInput(scanner, "Log ID: ");

            // Verify log exists
            Grounds inspectionToDelete = getGroundsInspection(idToReplace);
            if (inspectionToDelete == null) {
                System.out.println("Log with ID " + idToReplace + " not found.");
                return;
            }

            // Prompt for new log creation
            System.out.println("Creating a new log to replace the previous one...");
            deleteGroundsInspection(idToReplace); // Delete old log
            createAndSaveGroundsInspectionFromInput(scanner); // Create new log
            System.out.println("Previous log replaced successfully.");
        } else if ("yes".equals(response)) {
            System.out.println("Grounds state confirmed as accurate. No changes made.");
        } else {
            System.out.println("Invalid response. Please try again.");
        }
    }

}


//package Controllers;
//
//import Models.EmployeeHiring.JobPosting;
//import Models.Grounds.GroundsInspection;
//import Storage.StorageHelper;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class GroundsMaintenanceController {
//
//    private final StorageHelper storageHelper;
//    private final String GROUNDS_INSPECTION_STORE_NAME = "groundsInspections"; // Store name
//    private int nextId;
//
//    public GroundsMaintenanceController(String baseDirectory) throws IOException {
//        this.storageHelper = new StorageHelper(baseDirectory, GROUNDS_INSPECTION_STORE_NAME);
//        this.nextId = 1;
//    }
//
//
//    public void createGroundsInspection(GroundsInspection groundsInspection) throws IOException {
//        groundsInspection.setId(nextId);
//        nextId++;
//        storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).save(String.valueOf(groundsInspection.getId()), groundsInspection.toMap());
//    }
//
//    public GroundsInspection getGroundsInspection(int id) throws IOException {
//        Map<String, Object> data = storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).load(String.valueOf(id));
//        return data != null ? new GroundsInspection().fromMap(data) : null;
//    }
//
//    public void updateGroundsInspection(GroundsInspection groundsInspection) throws IOException {
//        storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).save(String.valueOf(groundsInspection.getId()), groundsInspection.toMap());
//    }
//
//    public void deleteGroundsInspection(int id) throws IOException {
//        storageHelper.getStore(GROUNDS_INSPECTION_STORE_NAME).delete(String.valueOf(id));
//    }
//
//    public void createAndSaveGroundsInspectionFromInput(Scanner scanner) throws IOException {
//        System.out.println("=== Create New Grounds Inspection ===");
//
//        int id = nextId++;
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String date = LocalDate.now().format(formatter);
//
//        printInspectionChecksGrass();
//        double grassHeight = getDoubleInput(scanner, "Enter current grass height (in cm): ");
//
//        printInspectionChecksBushes();
//        double bushLength = getDoubleInput(scanner, "Enter average bush length (in cm): ");
//
//        printInspectionChecksTrees();
//        int treeCount = getIntInput(scanner, "Enter the number of trees: ");
//
//        printInspectionChecksWeeds();
//        int weedCount = getIntInput(scanner, "Enter the number of weeds: ");
//
//        GroundsInspection inspection = new GroundsInspection(id, date, grassHeight, bushLength, treeCount, weedCount);
//        createGroundsInspection(inspection);
//        System.out.println("Ground inspection made with id of : " + id);
//    }
//
//    private double getDoubleInput(Scanner scanner, String prompt) {
//        while (true) {
//            System.out.print(prompt);
//            try {
//                return Double.parseDouble(scanner.nextLine().trim());
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input. Please enter a valid number.");
//            }
//        }
//    }
//
//    private int getIntInput(Scanner scanner, String prompt) {
//        while (true) {
//            System.out.print(prompt);
//            try {
//                return Integer.parseInt(scanner.nextLine().trim());
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input. Please enter a valid integer.");
//            }
//        }
//    }
//
//    private void printInspectionChecksGrass() {
//        System.out.println("\n=== Grass Inspection Checks ===");
//        System.out.println("1. Measure grass height in multiple areas.");
//        System.out.println("2. Check for signs of overgrowth or unevenness.");
//        System.out.println("3. Ensure grass is healthy and free of pests.");
//    }
//
//    private void printInspectionChecksBushes() {
//        System.out.println("\n=== Bush Inspection Checks ===");
//        System.out.println("1. Measure the average length of bushes.");
//        System.out.println("2. Check for overgrowth obstructing pathways.");
//        System.out.println("3. Inspect for signs of disease or pests.");
//    }
//
//    private void printInspectionChecksTrees() {
//        System.out.println("\n=== Tree Inspection Checks ===");
//        System.out.println("1. Count the total number of trees.");
//        System.out.println("2. Check for dead or overgrown branches.");
//        System.out.println("3. Inspect for pest infestations or signs of disease.");
//    }
//
//    private void printInspectionChecksWeeds() {
//        System.out.println("\n=== Weed Inspection Checks ===");
//        System.out.println("1. Count the number of weeds in key areas.");
//        System.out.println("2. Check for weed growth near pathways or flowerbeds.");
//        System.out.println("3. Assess the need for weeding or herbicide application.");
//    }
//
//
//    public void modifyGroundsInspection(Scanner scanner) throws IOException {
//        System.out.print("Enter Grounds Inspection ID: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        GroundsInspection inspection = getGroundsInspection(id);
//
//        if (inspection != null) {
//            System.out.println("=== Modify Grounds Inspection ===");
//            System.out.println("Current Grass Height: " + inspection.getGrassHeight() + " cm");
//            inspection.setGrassHeight(getDoubleInput(scanner, "Enter new grass height (in cm): "));
//
//            System.out.println("Current Bush Length: " + inspection.getBushLength() + " cm");
//            inspection.setBushLength(getDoubleInput(scanner, "Enter new bush length (in cm): "));
//
//            System.out.println("Current Tree Count: " + inspection.getTreeCount());
//            inspection.setTreeCount(getIntInput(scanner, "Enter new tree count: "));
//
//            System.out.println("Current Weed Count: " + inspection.getWeedCount());
//            inspection.setWeedCount(getIntInput(scanner, "Enter new weed count: "));
//
//            createGroundsInspection(inspection); // Save updated inspection
//            System.out.println("Grounds inspection updated successfully.");
//        } else {
//            System.out.println("Inspection not found.");
//        }
//    }
//
//    public
//
//
//
//}
