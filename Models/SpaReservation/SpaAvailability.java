package Models.SpaReservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SpaAvailability {
    public List<List<Integer>> times;
    public List<List<Boolean>> available;

    public SpaAvailability() {
        initializeDefaults();
    }

    @SuppressWarnings("unchecked")
    public SpaAvailability(Map<String, Object> map) {
        // Convert times
        List<List<Double>> timesDoubleList = (List<List<Double>>) map.get("times");
        this.times = new ArrayList<>();
        for (List<Double> innerList : timesDoubleList) {
            List<Integer> timePair = new ArrayList<>();
            for (Double time : innerList) {
                timePair.add(time.intValue());
            }
            this.times.add(timePair);
        }

        // Convert available
        List<List<Boolean>> availableList = (List<List<Boolean>>) map.get("available");
        this.available = new ArrayList<>();
        for (List<Boolean> innerList : availableList) {
            this.available.add(new ArrayList<>(innerList));
        }
    }

    private void initializeDefaults() {
        times = new ArrayList<>();
        times.add(List.of(9, 10));
        times.add(List.of(10, 11));
        times.add(List.of(11, 12));
        times.add(List.of(12, 13));
        times.add(List.of(13, 14));
        times.add(List.of(14, 15));
        times.add(List.of(15, 16));

        available = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            List<Boolean> dayAvailability = new ArrayList<>();
            for (int j = 0; j < times.size(); j++) {
                dayAvailability.add(true);
            }
            available.add(dayAvailability);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        // Convert times to List<List<Double>> for consistency with Gson
        List<List<Double>> timesDoubleList = new ArrayList<>();
        for (List<Integer> timePair : times) {
            List<Double> timePairDouble = new ArrayList<>();
            for (Integer time : timePair) {
                timePairDouble.add(time.doubleValue());
            }
            timesDoubleList.add(timePairDouble);
        }
        map.put("times", timesDoubleList);
        map.put("available", available);
        return map;
    }

    public void resetAvailability() {
        for (List<Boolean> dayAvailability : available) {
            for (int i = 0; i < dayAvailability.size(); i++) {
                dayAvailability.set(i, true);
            }
        }
    }

    public void printSpaAvailability() {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int day = 0; day < available.size(); day++) {
            System.out.println((day + 1) + ". Day: " + daysOfWeek[day]);
            for (int slot = 0; slot < available.get(day).size(); slot++) {
                String status = available.get(day).get(slot) ? "Available" : "Booked";
                System.out.println("    " + (slot + 1) + ". Time Slot: " + times.get(slot).get(0) + ":00 - " + times.get(slot).get(1) + ":00 | " + status);
            }
            System.out.println();
        }
    }

    public TimeSlot chooseTimeSlot(Scanner scnr) {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        while (true) {
            // Display days
            System.out.println("Choose a day (1-7) or 0 to exit:");
            for (int i = 0; i < daysOfWeek.length; i++) {
                System.out.println((i + 1) + ". " + daysOfWeek[i]);
            }

            System.out.print("Enter your choice: ");
            int dayChoice;
            try {
                dayChoice = scnr.nextInt() - 1;
            } catch (Exception e) {
                scnr.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a number between 0 and 7.");
                continue;
            }

            if (dayChoice == -1) {
                System.out.println("Exiting without making a reservation.");
                return null;
            }

            if (dayChoice < 0 || dayChoice >= daysOfWeek.length) {
                System.out.println("Invalid choice. Please select a valid day.");
                continue;
            }

            // Display available time slots for the chosen day
            System.out.println("Available time slots for " + daysOfWeek[dayChoice] + ":");
            boolean hasAvailableSlot = false;
            for (int slot = 0; slot < available.get(dayChoice).size(); slot++) {
                if (available.get(dayChoice).get(slot)) {
                    System.out.println((slot + 1) + ". " + times.get(slot).get(0) + ":00 - " + times.get(slot).get(1) + ":00 (Available)");
                    hasAvailableSlot = true;
                }
            }

            if (!hasAvailableSlot) {
                System.out.println("No available slots for " + daysOfWeek[dayChoice] + ". Please choose another day.");
                continue;
            }

            System.out.println("Choose a time slot (1-" + available.get(dayChoice).size() + ") or 0 to go back:");
            System.out.print("Enter your choice: ");
            int slotChoice;
            try {
                slotChoice = scnr.nextInt() - 1;
            } catch (Exception e) {
                scnr.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a valid time slot number.");
                continue;
            }

            if (slotChoice == -1) {
                System.out.println("Returning to the day selection menu.");
                continue;
            }

            if (slotChoice < 0 || slotChoice >= available.get(dayChoice).size() || !available.get(dayChoice).get(slotChoice)) {
                System.out.println("Invalid or unavailable time slot. Please try again.");
                continue;
            }

            // Confirm reservation
            System.out.println("You have chosen " + daysOfWeek[dayChoice] + ", " +
                    times.get(slotChoice).get(0) + ":00 - " + times.get(slotChoice).get(1) + ":00.");
            System.out.print("Do you want to confirm this reservation time? (yes/y/no/n): ");
            scnr.nextLine(); // Clear the buffer
            String confirmation = scnr.nextLine().trim().toLowerCase();
            if (confirmation.equals("yes") || confirmation.equals("y")) {
                available.get(dayChoice).set(slotChoice, false);
                System.out.println("Reservation time confirmed! Chose your your spa services.");
                return new TimeSlot(DayOfWeek.fromInt(dayChoice), slotChoice);
            } else {
                System.out.println("Reservation time not confirmed. Returning to the menu.");
            }
        }
    }
}