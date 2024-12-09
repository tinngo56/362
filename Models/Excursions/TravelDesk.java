package Models.Excursions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TravelDesk {
    private List<Excursion> excursions;

    public TravelDesk() {
        this.excursions = new ArrayList<>();
    }

    public TravelDesk(List<Excursion> excursions) {
        this.excursions = excursions;
    }

    public void addExcursion(Excursion excursion) {
        excursions.add(excursion);
    }

    public List<Excursion> getAvailableExcursions() {
        List<Excursion> availableExcursions = new ArrayList<>();
        for (Excursion excursion : excursions) {
            if (excursion.isAvailable()) {
                availableExcursions.add(excursion);
            }
        }
        return availableExcursions;
    }

    public Optional<Excursion> bookExcursion(String name) {
        for (Excursion excursion : excursions) {
            if (excursion.getName().equalsIgnoreCase(name) && excursion.isAvailable()) {
                excursion.bookSlot();
                return Optional.of(excursion);
            }
        }
        return Optional.empty();
    }

    public void showExcursions() {
        System.out.println("Available Excursions:");
        for (Excursion excursion : excursions) {
            if(excursion.isAvailable()) {
                System.out.println(excursion);
            }
        }
    }
}

