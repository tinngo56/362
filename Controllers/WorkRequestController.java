package Controllers;

import Models.CleanFacility.*;
import Models.CompleteMaintenanceOrCleaningRequest.FacilityType;
import Models.CompleteMaintenanceOrCleaningRequest.WorkRequest;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.*;

public class WorkRequestController {

    //general
    private StorageHelper storageHelper;
    private final String WORK_REQUEST_STORE_NAME = "workRequests";
    private int nextId;

    public WorkRequestController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, WORK_REQUEST_STORE_NAME);
        this.nextId = determineNextId();
    }

//---------------------------store managing stuff--------------------------- TODO only to find section

    private int determineNextId() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(WORK_REQUEST_STORE_NAME).loadAll();
        int maxId = 0;
        for (Map<String, Object> readingMap : allReadings) {
            Number idNumber = (Number) readingMap.get("id");
            if (idNumber != null) {
                int id = idNumber.intValue();
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    private String getUnits(String item){
        //TODO
        return null;
    }

    public void saveWorkRequest(WorkRequest facility) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(facility);
        storageHelper.getStore(WORK_REQUEST_STORE_NAME).save(String.valueOf(facility.getId()), readingMap);
    }

    public WorkRequest getWorkRequest(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(WORK_REQUEST_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToHotelFacility(data) : null;
    }

    public void updateWorkRequest(WorkRequest reading) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(reading);
        storageHelper.getStore(WORK_REQUEST_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public int getNumWorkRequests() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(WORK_REQUEST_STORE_NAME).loadAll();
        return allReadings.size();
    }

    private Map<String, Object> convertHotelFacilityToMap(WorkRequest reading) {
        return reading.toMap();
    }

    public WorkRequest convertMapToHotelFacility(Map<String, Object> map) {
//        WorkRequest request = new WorkRequest();
        return new WorkRequest().fromMap(map);
    }

    public List<Map<String, Object>> getAllFacilityCleaningReports() throws IOException {
        return storageHelper.getStore(WORK_REQUEST_STORE_NAME).loadAll();
    }

//---------------------------interactive print stuff--------------------------- TODO only to find section


    private WorkRequest makeWorkRequestFromInput(Scanner scanner) throws IOException {
        System.out.println("Enter the following details to make a new work request:");

        int id = nextId++;
        while(true) {
            System.out.println("Select a facility to create a work request for (enter the number):");
            System.out.println("1. Pool");
            System.out.println("2. Game Room");
            System.out.println("3. Conference Room");
            System.out.println("4. Gym");
            int facilityChoice = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Is this a clean request? (y,n)");
            String s = scanner.nextLine().trim().toLowerCase();
            boolean isCleaningRequest = s.equals("y");
            System.out.println("Is this urgent? (y,n)");
            String str = scanner.nextLine().trim().toLowerCase();
            boolean urgent = str.equals("y");
            System.out.println("What is the issue?");
            String issue = scanner.nextLine();

            switch (facilityChoice) {
                case 1:
                    return new WorkRequest(isCleaningRequest,false,urgent, FacilityType.POOl, issue, id);
            case 2:
                return new WorkRequest(isCleaningRequest,false,urgent, FacilityType.GAME_ROOM, issue, id);
            case 3:
                return new WorkRequest(isCleaningRequest,false,urgent, FacilityType.CONFERENCE_ROOM, issue, id);
            case 4:
                return new WorkRequest(isCleaningRequest,false,urgent, FacilityType.GYM, issue, id);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void createAndSaveWorkRequestFromInput(Scanner scnr) throws IOException {
        saveWorkRequest(makeWorkRequestFromInput(scnr));
    }

    public void completeWorkRequest(Scanner scanner) throws IOException {
        System.out.println("=== Complete Work Request===");
        printAll();
        System.out.println("What id:");
        int want = scanner.nextInt();
        WorkRequest request = getWorkRequest(want);

        System.out.println("Is the issue: " + request.getIssue() + " resolved? (y or n)");
        String s = scanner.nextLine().trim().toLowerCase();
        boolean solved = s.equals("y");
        if(solved) {
            request.setResolved(true);
            System.out.println(request.toString() + "\nIs resolved");
        } else {
            System.out.println(request.toString() + "\nIs not resolved");
        }

        saveWorkRequest(request);
        System.out.println("Work request is up to date and saved.\n");
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void printAll() throws IOException {
        List<Map<String, Object>> allReadings = getAllFacilityCleaningReports();

        if (allReadings.isEmpty()) {
            System.out.println("No Work requests available.");
            return;
        }

        System.out.println("All Work requests:");
        System.out.println("----------------------");

        for (Map<String, Object> readingMap : allReadings) {
            for (Map.Entry<String, Object> entry : readingMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("----------------------");
        }
    }

    private String selectFacility(Scanner scnr, Set<String> chemicals) {
        List<String> chemicalList = new ArrayList<>(chemicals);
        System.out.println("Available Facilities:");
        for (int i = 0; i < chemicalList.size(); i++) {
            if (chemicalList.get(i).equals("lastModified")) {
                continue;
            }
            System.out.printf("%d. %s%n", i + 1, capitalize(chemicalList.get(i)));
        }
        System.out.println("0. Cancel");
        System.out.print("Select a chemical by number: ");

        int selection;
        try {
            selection = scnr.nextInt();
            scnr.nextLine();
        } catch (InputMismatchException e) {
            scnr.nextLine();
            System.out.println("Invalid input. Operation cancelled.");
            return null;
        }

        if (selection == 0) {
            return null;
        }

        if (selection < 1 || selection > chemicalList.size()) {
            System.out.println("Invalid selection. Operation cancelled.");
            return null;
        }

        return chemicalList.get(selection - 1);
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